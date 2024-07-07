import json
import os

from django.apps import apps
from django.core.management.base import BaseCommand
from django.db import router

from talaria.models import Tournament, User, Captain, Player


class Command(BaseCommand):
    help = "Does some magical work"

    def handle(self):

        db_alias = router.db_for_write(Tournament)
        Tournament.objects.using(db_alias).all().delete()
        User.objects.using(db_alias).all().delete()
        Captain.objects.using(db_alias).all().delete()
        Player.objects.using(db_alias).all().delete()

        self.stdout.write(f"CWD: {os.getcwd()}")
        print(db_alias)

        with open("dData.json", encoding="ascii") as data_file:
            data = json.load(data_file)

        user_entries = [entry for entry in data if entry['model'] == 'talaria.User']
        tournament_entries = [entry for entry in data if entry['model'] == 'talaria.Tournament']
        captain_entries = [entry for entry in data if entry['model'] == 'talaria.Captain']
        player_entries = [entry for entry in data if entry['model'] == 'talaria.Player']

        for entry in user_entries:
            model = apps.get_model('talaria', 'User')
            model.objects.using(db_alias).create(**entry['fields'])

        for entry in tournament_entries:
            model = apps.get_model('talaria', 'Tournament')
            model.objects.using(db_alias).create(**entry['fields'])

        for entry in captain_entries:
            model = apps.get_model('talaria', 'Captain')
            fields = entry['fields']
            fields['user_id'] = User.objects.using(db_alias).get(pk=fields['user_id'])
            model.objects.using(db_alias).create(**fields)

        for entry in player_entries:
            model = apps.get_model('talaria', 'Player')
            fields = entry['fields']
            fields['user_id'] = User.objects.using(db_alias).get(pk=fields['user_id'])
            fields['tournament_id'] = Tournament.objects.using(db_alias).get(
                pk=fields['tournament_id'])
            if 'captain_id' in fields and fields['captain_id'] is not None:
                fields['captain_id'] = Captain.objects.using(db_alias).get(pk=fields['captain_id'])
            model.objects.using(db_alias).create(**fields)
