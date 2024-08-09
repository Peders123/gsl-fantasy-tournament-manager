import asyncio
import json
from time import sleep

from asgiref.sync import sync_to_async
from channels.generic.websocket import AsyncWebsocketConsumer

from .models import Bidder, Room
from tournament.models import Tournament, Captain, Player


class AuctionConsumer(AsyncWebsocketConsumer):

    async def connect(self):

        self.timer_task = None
        self.tournament_id = self.scope['url_route']['kwargs']['tournament_id']

        tournament = await self.get_tournament_from_id(self.tournament_id)

        self.auction_room = await self.get_room_from_tournament_id(tournament.tournament_id)
        self.room_group_name = "tournament" + self.tournament_id + "auction"

        await self.channel_layer.group_add(self.room_group_name, self.channel_name)
        await self.accept()

        self.captain = await self.get_captain_user()
        username = self.captain.smite_name
        team_name = self.captain.team_name

        bidder = await self.get_bidder()

        if not bidder:
            self.bidder = await self.create_bidder()
        else:
            self.bidder = bidder
            await self.set_bidder_in(True)

        await self.channel_layer.group_send(
            self.room_group_name, {
                'type': 'connection',
                'user': username,
                'teamName': team_name
            }
        )

    async def disconnect(self, code):

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

            await self.set_highest_bidder(self.captain, 0)

            await self.channel_layer.group_send(
                self.room_group_name, {
                    'type': 'stagePlayer',
                    'playerId': text_data_json['playerId'],
                    'captainId': self.captain.smite_name
                }
            )

        elif data_type == "buyPlayer":

            self.auction_room = await self.refresh_auction_room()

            sender = await self.check_if_highest_bidder()

            if sender:

                team_id = await self.get_team_id()

                await self.channel_layer.group_send(
                    self.room_group_name, {
                        'type': 'buyPlayer',
                        'playerId': text_data_json['playerId'],
                        'teamId': team_id
                    }
                )

    async def connection(self, event):

        await self.send(text_data=json.dumps({
            'type': 'connection',
            'user': event['user'],
            'teamName': event['teamName']
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
            'captainId': event['captainId']
        }))

        await self.start_timer()

    async def buyPlayer(self, event):

        player = await self.get_player_from_id(int(event['playerId']))

        await self.send(text_data=json.dumps({
            'type': 'buyPlayer',
            'playerName': player.smite_name,
            'teamId': event['teamId']
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
    
    async def start_timer(self):

        if self.timer_task and not self.timer_task.done():
            self.timer_task.cancel()

        time_left = 5
        self.timer_task = asyncio.create_task(self.run_timer(time_left))

    async def run_timer(self, time_left):
        try:
            while time_left > 0:
                await self.send(text_data=json.dumps({
                    'type': 'timerUpdate',
                    'time_left': time_left
                }))
                await asyncio.sleep(1)
                time_left -= 1

            await self.send(text_data=json.dumps({
                'type': 'timerFinished'
            }))
        except asyncio.CancelledError:
            pass

    async def timerUpdate(self, event):
        await self.send(text_data=json.dumps({
            'type': 'timerUpdate',
            'time_left': event['time_left']
        }))

    async def timerFinished(self, event):
        await self.send(text_data=json.dumps({
            'type': 'timerFinished'
        }))

    @sync_to_async
    def refresh_auction_room(self):
        tournament = Tournament.objects.get(tournament_id=self.tournament_id)
        return Room.objects.get(tournament_id=tournament)
    
    @sync_to_async
    def get_team_id(self):
        return self.auction_room.current_highest_bidder.smite_name + ":" + self.auction_room.current_highest_bidder.team_name
    
    @sync_to_async
    def check_if_highest_bidder(self):
        return self.auction_room.current_highest_bidder.captain_id == self.captain.captain_id
