import asyncio
import json
import sys

from asgiref.sync import sync_to_async
from channels.generic.websocket import AsyncWebsocketConsumer

from .models import Bidder, Room, Biddee
from tournament.models import Tournament, Captain, Player


active_connections = {}


class AuctionConsumer(AsyncWebsocketConsumer):

    async def connect(self):

        self.timer_task = None
        self.tournament_id = self.scope['url_route']['kwargs']['tournament_id']
        self.captain = await self.get_captain_user()

        if self.captain.smite_name in active_connections:
            await self.channel_layer.group_discard(self.room_group_name, active_connections[self.captain.smite_name])

        active_connections[self.captain.smite_name] = self.channel_name

        existing_bidder = await self.get_bidder()
        self.bidder = existing_bidder

        if existing_bidder:
            await self.set_bidder_in(False)

        tournament = await self.get_tournament_from_id(self.tournament_id)
        self.auction_room = await self.get_room_from_tournament_id(tournament.tournament_id)
        self.room_group_name = "tournament" + self.tournament_id + "auction"

        if not existing_bidder:
            self.bidder = await self.create_bidder()
        else:
            await self.set_bidder_in(True)

        if not await self.get_current_selector_exists():
            await self.set_current_selector()
        elif not await self.get_next_selector_exists():
            await self.set_next_selector()

        await self.channel_layer.group_add(self.room_group_name, self.channel_name)
        await self.accept()
        
        username = self.captain.smite_name
        team_name = self.captain.team_name
        biddees = await self.get_biddees()

        await self.channel_layer.group_send(
            self.room_group_name, {
                'type': 'connection',
                'user': username,
                'teamName': team_name,
                'captainBudget': self.captain.captain_budget,
                'biddees': biddees
            }
        )

    async def disconnect(self, code):

        if self.captain.smite_name in active_connections:
            del active_connections[self.captain.smite_name]

        await self.set_bidder_in(False)

        await self.channel_layer.group_send(
            self.room_group_name, {
                'type': 'disconnection',
                'user': self.captain.smite_name,
                'teamName': self.captain.team_name
            }
        )

        await self.channel_layer.group_discard(self.room_group_name, self.channel_name)

    async def receive(self, text_data):

        text_data_json = json.loads(text_data)

        data_type = text_data_json['type']

        if data_type == "message":

            message = text_data_json['message']

            await self.channel_layer.group_send(
                self.room_group_name, {
                    'type': 'message',
                    'message': message
                }
            )

        elif data_type == "stagePlayer":

            sender = await self.check_if_selector()

            if sender:

                await self.set_current_selector_null()

                await self.set_highest_bidder(self.captain, 0)
                await self.set_current_asset(text_data_json['playerId'])

                await self.channel_layer.group_send(
                    self.room_group_name, {
                        'type': 'stagePlayer',
                        'playerId': text_data_json['playerId'],
                        'captainName': self.captain.smite_name
                    }
                )

        elif data_type == "placeBid":

            self.auction_room = await self.refresh_auction_room()

            if not await self.check_if_highest_bidder() and not await self.check_overbudget(text_data_json['bidAmount']) and not await self.check_if_team_full(self.captain):

                new_bid = await self.increment_highest_bidder(self.captain, text_data_json['bidAmount'])

                await self.channel_layer.group_send(
                    self.room_group_name,  {
                        'type': 'placeBid',
                        'bidAmount': new_bid,
                        'captainName': self.captain.smite_name
                    }
                )

        elif data_type == "buyPlayer":

            self.auction_room = await self.refresh_auction_room()

            sender = await self.check_if_highest_bidder()

            if sender:

                await self.update_budget()
                await self.assign_player()
                await self.create_biddee()
                await self.set_current_asset(None)

                budget = await self.get_budget_string()

                team_id = await self.get_team_id()
                await self.set_highest_bidder(None, 0)

                await self.rotate_selectors()

                await self.channel_layer.group_send(
                    self.room_group_name, {
                        'type': 'buyPlayer',
                        'playerId': text_data_json['playerId'],
                        'teamId': team_id,
                        'newBudget': budget
                    }
                )

    async def connection(self, event):

        self.auction_room = await self.refresh_auction_room()
        captain = await self.get_current_selector()

        await self.send(text_data=json.dumps({
            'type': 'connection',
            'user': event['user'],
            'teamName': event['teamName'],
            'captainBudget': event['captainBudget'],
            'biddees': event['biddees'],
            'selector': captain.smite_name
        }))

    async def disconnection(self, event):

        await self.send(text_data=json.dumps({
            'type': 'disconnection',
            'user': event['user'],
            'teamName': event['teamName']
        }))

    async def message(self, event):

        message = event['message']

        await self.send(text_data=json.dumps({
            'type': 'message',
            'message': message
        }))

    async def stagePlayer(self, event):

        player = await self.get_player_from_id(int(event['playerId']))

        await self.send(text_data=json.dumps({
            'type': 'stagePlayer',
            'playerId': player.player_id,
            'playerName': player.smite_name,
            'playerValue': player.estimated_value,
            'captainName': event['captainName']
        }))

        await self.start_timer()

    async def placeBid(self, event):

        if not self.time_left:
            self.time_left = 5

        if self.time_left < 5:
            self.time_left = 6

        await self.send(text_data=json.dumps({
            'type': 'placeBid',
            'captainName': event['captainName'],
            'bidAmount': event['bidAmount']
        }))

    async def buyPlayer(self, event):

        self.auction_room = await self.refresh_auction_room()
        captain = await self.get_current_selector()
        player = await self.get_player_from_id(int(event['playerId']))

        await self.send(text_data=json.dumps({
            'type': 'buyPlayer',
            'playerName': player.smite_name,
            'teamId': event['teamId'],
            'newBudget': event['newBudget'],
            'selector': captain.smite_name
        }))

    async def complete(self, event):

        await self.send(text_data=json.dumps({
            'type': 'complete'
        }))

    @sync_to_async
    def get_captain_user(self):

        return Captain.objects.filter(
            user_id=self.scope['session']['discord']['id']
        )[0]

    @sync_to_async
    def get_bidder(self):

        bidder = Bidder.objects.filter(
            captain_id=self.captain.captain_id,
            tournament_id=self.tournament_id
        )

        if bidder.exists():
            return bidder[0]
        return None

    @sync_to_async
    def create_bidder(self):

        bidder = Bidder(
            captain_id=self.captain,
            tournament_id=Tournament.objects.get(tournament_id=self.tournament_id),
            join_order=len(Bidder.objects.filter(
                tournament_id=self.tournament_id
            )),
            currently_in=True
        )
        bidder.save()

        return bidder

    @sync_to_async
    def set_bidder_in(self, currently_in):

        self.bidder.currently_in = currently_in
        self.bidder.save()

    @sync_to_async
    def get_player_from_id(self, player_id):

        return Player.objects.get(
            player_id=player_id
        )
    
    @sync_to_async
    def get_tournament_from_id(self, tournament_id):

        return Tournament.objects.get(
            tournament_id=int(tournament_id)
        )
    
    @sync_to_async
    def get_room_from_tournament_id(self, tournament_id):

        return Room.objects.get(
            tournament_id=int(tournament_id)
        )
    
    @sync_to_async
    def set_highest_bidder(self, captain, bid):

        self.auction_room.current_highest_bidder = captain
        self.auction_room.current_highest_bid = bid
        self.auction_room.save()

    @sync_to_async
    def set_current_asset(self, player_id):

        if not player_id:
            self.auction_room.current_asset = None
        else:
            player = Player.objects.get(player_id=int(player_id))
            self.auction_room.current_asset = player

        self.auction_room.save()

    @sync_to_async
    def increment_highest_bidder(self, captain, bid):

        current_bid = self.auction_room.current_highest_bid
        self.auction_room.current_highest_bidder =  captain
        self.auction_room.current_highest_bid = int(bid) + int(current_bid)
        self.auction_room.save()

        return self.auction_room.current_highest_bid

    
    async def start_timer(self):

        if self.timer_task and not self.timer_task.done():
            self.timer_task.cancel()

        time_left = 20
        self.timer_task = asyncio.create_task(self.run_timer(time_left))

    async def run_timer(self, time_left):
        self.time_left = time_left
        try:
            while self.time_left > 0:
                await self.send(text_data=json.dumps({
                    'type': 'timerUpdate',
                    'time_left': self.time_left
                }))
                await asyncio.sleep(1)
                self.time_left -= 1

            await self.send(text_data=json.dumps({
                'type': 'timerFinished'
            }))
        except asyncio.CancelledError:
            pass

    @sync_to_async
    def refresh_auction_room(self):
        tournament = Tournament.objects.get(tournament_id=self.tournament_id)
        return Room.objects.get(tournament_id=tournament)
    
    @sync_to_async
    def get_team_id(self):
        return self.auction_room.current_highest_bidder.smite_name + ":" + self.auction_room.current_highest_bidder.team_name
    
    @sync_to_async
    def check_if_highest_bidder(self):
        if not self.auction_room.current_highest_bidder:
            return False
        if not self.auction_room.current_highest_bidder.captain_id:
            return False
        return self.auction_room.current_highest_bidder.captain_id == self.captain.captain_id
    
    @sync_to_async
    def check_overbudget(self, bid):
        current_bid = self.auction_room.current_highest_bid
        return int(bid) + int(current_bid) > self.captain.captain_budget
    
    @sync_to_async
    def update_budget(self):
        budget = self.captain.captain_budget
        budget -= self.auction_room.current_highest_bid
        self.captain.captain_budget = budget
        self.captain.save()

        return budget

    @sync_to_async
    def get_budget_string(self):
        return str(self.captain.team_name) + " - " + str(self.captain.captain_budget)
    
    @sync_to_async
    def assign_player(self):
        player = self.auction_room.current_asset
        player.captain_id = self.captain
        player.save()

    @sync_to_async
    def create_biddee(self):

        tournament = Tournament.objects.get(tournament_id=self.tournament_id)
        bidder = Bidder.objects.get(tournament_id=tournament, captain_id=self.auction_room.current_highest_bidder)
        draft_order = len(Biddee.objects.filter(tournament_id=tournament, bidder_id=bidder)) + 1

        biddee = Biddee(
            bidder_id=bidder,
            tournament_id=tournament,
            player_id=self.auction_room.current_asset,
            bidded_amount=self.auction_room.current_highest_bid,
            draft_order=draft_order
        )

        biddee.save()

    @sync_to_async
    def get_biddees(self):

        tournament = Tournament.objects.get(tournament_id=self.tournament_id)
        bidder = Bidder.objects.get(tournament_id=tournament, captain_id=self.captain)

        biddees = Biddee.objects.filter(bidder_id=bidder)

        biddees_list = [
            {
                'username': biddee.player_id.smite_name,
                'team_id': biddee.player_id.captain_id.smite_name + ":" + biddee.player_id.captain_id.team_name
            }
            for biddee in biddees
        ]

        return biddees_list
    
    @sync_to_async
    def set_current_selector(self):

        tournament = Tournament.objects.get(tournament_id=self.tournament_id)
        bidder = Bidder.objects.get(tournament_id=tournament, captain_id=self.captain)
        self.auction_room.current_selector = bidder
        self.auction_room.save()

    @sync_to_async
    def set_current_selector_null(self):

        self.auction_room.current_selector = None
        self.auction_room.save()

    @sync_to_async
    def set_next_selector(self):

        tournament = Tournament.objects.get(tournament_id=self.tournament_id)
        bidder = Bidder.objects.get(tournament_id=tournament, captain_id=self.captain)

        self.auction_room.next_selector = bidder
        self.auction_room.save()

    @sync_to_async
    def get_current_selector(self):
        if not self.auction_room.current_selector:
            return None
        return self.auction_room.current_selector.captain_id
    
    @sync_to_async
    def get_current_selector_exists(self):
        if self.auction_room.current_selector:
            return True
        return False
    
    @sync_to_async
    def get_next_selector_exists(self):
        if self.auction_room.next_selector:
            return True
        return False
    
    @sync_to_async
    def check_if_selector(self):
        if not self.auction_room.current_selector:
            return False
        return self.auction_room.current_selector.captain_id == self.captain
    
    @sync_to_async
    def rotate_selectors(self):
        draft_order = self.auction_room.next_selector.join_order
        self.auction_room.current_selector = self.auction_room.next_selector

        tournament = Tournament.objects.get(tournament_id=self.tournament_id)
        bidders = len(Bidder.objects.filter(tournament_id=tournament))

        # Move to the next bidder in the draft order
        draft_order += 1

        # Reset to the first bidder if we've reached the end
        if draft_order == bidders:
            draft_order = 0

        # Get the next bidder
        bidder = Bidder.objects.get(tournament_id=tournament, join_order=draft_order)

        # Check if the bidder's team is full
        if not self.check_if_team_full(bidder.captain_id):
            # If the team is not full, select this bidder as the next selector
            self.auction_room.next_selector = bidder
            self.auction_room.save()
            return  # Exit the loop once a valid bidder is found
        
        else:
            bidder = Bidder.objects.get(tournament_id=tournament, join_order=draft_order+1)
            self.auction_room.next_selector = bidder
            self.auction_room.save()

    @sync_to_async
    def check_if_team_full(self, captain):

        return len(Player.objects.filter(captain_id=captain)) >= 4
