import json

from asgiref.sync import sync_to_async
from channels.generic.websocket import AsyncWebsocketConsumer

from tournament.models import Captain


class AuctionConsumer(AsyncWebsocketConsumer):

    async def connect(self):

        self.room_group_name = "auction"

        await self.channel_layer.group_add(self.room_group_name, self.channel_name)
        await self.accept()

        captain = await self.get_captain_user()
        username = captain.smite_name

        await self.channel_layer.group_send(
            self.room_group_name, {
                'type': 'connection',
                'user': username
            }
        )

    async def disconnect(self, code):

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

    async def connection(self, event):

        user = event['user']

        await self.send(text_data=json.dumps({
            'type': 'connection',
            'user': user
        }))

    async def message(self, event):

        message = event['message']

        await self.send(text_data=json.dumps({
            'type': 'message',
            'message': message
        }))

    @sync_to_async
    def get_captain_user(self):

        return Captain.objects.filter(user_id=self.scope['session']['discord']['id'])[0]
