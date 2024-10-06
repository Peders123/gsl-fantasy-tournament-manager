from datetime import datetime, timedelta
from hashlib import md5
import json
import os

import requests


class HiRezAPI:


    def __init__(self, endpoint, save_session = True):

        with open('/data/thoth/secrets.json', 'r', encoding='utf8') as file:
            secrets = json.load(file)

        self.name_cls = self.__class__.__name__
        self.dev_id = secrets["tokens"]["hirez"]["dev-id"]
        self.auth_key = secrets["tokens"]["hirez"]["auth-key"]
        self.endpoint = endpoint
        self.headers = {}
        self.save_session = save_session
        self.session = self.session_to_json('r')

    def sign(self, api_method):

        signature = f'{self.dev_id}{api_method.lower()}{self.auth_key}{self.timestamp()}'

        return md5(signature.encode("utf-8")).hexdigest()

    def get_session(self):

        if datetime.now() - self.session['time'] > timedelta(minutes=14, seconds=59):

            self.session['id'] = self.create_session()

            if self.save_session:
                self.session_to_json('w')
                self.test_session()

        return self.session['id']

    def request(self, url, *args, **kwargs):

        r = requests.get(url=url, headers={**self.headers, **kwargs.pop('headers', {})}, *args, **kwargs)
        if r.headers.get('Content-Type', '').rfind('json') != -1:
            return r.json()

        return r.text

    def timestamp(self):

        return datetime.now().strftime('%Y%m%d%H%M%S')

    def create_url(self, api_method, *args):

        url = f'{self.endpoint}{api_method}json/{self.dev_id}/{self.sign(api_method)}/{self.get_session()}/{self.timestamp()}'

        for arg in args:
            url += str(f'/{arg}')

        print(url)

        return url

    def session_to_json(self, method):

        session_data_path = "data/session_data.json"

        if method == 'r' or method == 'read':
            if self.save_session and os.path.isfile(session_data_path):
                with open(session_data_path, "r") as read_file:
                    data_json = json.load(read_file)
                if self.__class__.__name__ in data_json:
                    self.session = data_json[self.__class__.__name__]
                    self.session['time'] = datetime.strptime(self.session['time'], "%Y-%m-%d %H:%M:%S.%f")
                    return self.session

            self.session = {'id': None, 'time': datetime.now() - timedelta(minutes=15)}

            return self.session

        if method == 'w' or method == 'write':
            if os.path.isfile(session_data_path):
                with open(session_data_path, "r") as read_file:
                    data_json = json.load(read_file)
                data_json[self.name_cls] = self.session
                with open(session_data_path, "w") as write_file:
                    json.dump(data_json, write_file, default=str, indent=4)
            else:
                with open(session_data_path, "w") as write_file:
                    json.dump(({self.name_cls: self.session}), write_file, default=str, indent=4)

    # Connectivity, Development, & System Status

    def ping(self):

        return self.request(f'{self.endpoint}pingjson')

    def create_session(self):

        sign = self.sign("createsession")
        url_session = f'{self.endpoint}{"createsession"}json/{self.dev_id}/{sign}/{self.timestamp()}'
        self.session['time'] = datetime.now()

        response = self.request(url_session)

        if response.get('ret_msg', '').lower() == 'approved':

            return response.get('session_id')

        return 0

    def test_session(self):

        return self.request(self.create_url('testsession'))


    def data_used(self):

        return self.request(self.create_url('getdataused'))

    def server_status(self):

        return self.request(self.create_url('gethirezserverstatus'))

    def patch_info(self):

        return self.request(self.create_url('getpatchinfo'))



class SmiteAPI(HiRezAPI):

    def __init__(self, endpoint):

        super().__init__(endpoint)

    def get_demo_details(self, match_id=None):

        return self.request(self.create_url('getdemodetails', match_id))

    def get_esports_pro_league_details(self):

        return self.request(self.create_url('getesportsproleaguedetails'))

    def get_god_ranks(self, player=None):

        return self.request(self.create_url('getgodranks', player))

    def get_gods(self):

        return self.request(self.create_url('getgods', 1))

    def get_god_leaderboard(self, god_id, queue):

        return self.request(self.create_url('getgodleaderboard', god_id, queue))

    def get_god_skins(self, god_id):

        return self.request(self.create_url('getgodskins', god_id, 1))

    def get_god_recommended_items(self, god_id):

        return self.request(self.create_url('getgodrecommendeditems', god_id, 1))

    def get_items(self):

        return self.request(self.create_url('getitems', 1))

    def get_match_details(self, match_id):

        return self.request(self.create_url('getmatchdetails', match_id))

    def get_match_details_batch(self, match_ids):

        if match_ids > 10:
            return None

        str_match_ids = ','.join([str(id) for id in match_ids])
        return self.request(self.create_url('getmatchdetails', str_match_ids))


    def get_league_leaderboard(self, queue, tier, season):

        return self.request(self.create_url('getleagueleaderboard', queue, tier, season))

    def get_match_history(self, player_id):

        return self.request(self.create_url('getmatchhistory', player_id))

    def get_motd(self):

        return self.request(self.create_url('getmotd'))

    def get_player(self, player=None):

        return self.request(self.create_url('getplayer', player))

    def get_player_status(self, player_id):

        return self.request(self.create_url('getplayerstatus', player_id))

    def get_queue_stats(self, player_id, queue):

        return self.request(self.create_url('getqueuestats', player_id, queue))

    def get_top_matches(self):

        return self.request(self.create_url('gettopmatches'))

    def get_player_achievments(self, player_id):

        return self.request(self.create_url('getplayerachievements', player_id))

    def get_patch_info(self):

        return self.request(self.create_url('getpatchinfo'))


class PcSmiteAPI(SmiteAPI):

    def __init__(self):

        super().__init__("https://api.smitegame.com/smiteapi.svc/")


    def get_friends(self, player):

        return self.request(self.create_url('getfriends', player))


class XboxSmiteAPI(SmiteAPI):

    def __init__(self):

        super().__init__("https://api.xbox.smitegame.com/smiteapi.svc/")


class Ps4SmiteAPI(SmiteAPI):

    def __init__(self):

        super().__init__("https://api.ps4.smitegame.com/smiteapi.svc/")
