from datetime import datetime

from django.shortcuts import redirect, render
import requests
import json
import os
from tournament.models import Tournament
from match.models import Match, Team


def index(request):

    signed_in = False
    discord_id = ""

    if 'discord' in request.session.keys():
        signed_in = True
        discord_id = request.session['discord']['id']

    errno = request.GET.get('errno', None)
    error = request.GET.get('error', None)

    total_match_data = requests.get("http://192.168.64.1:8002/match/display/").json()

    ud_matches = []
    ld_matches = []

    for match_data in total_match_data:
        if datetime.fromisoformat(match_data["match_date_time"]) > datetime.now():
            if match_data["team1"]["division"]["division_rank"] == 0:
                ud_matches.append(Match.from_dict(match_data))
            else:
                ld_matches.append(Match.from_dict(match_data))

    return render(request, 'home/home.html', context={
        'signed_in': signed_in,
        'discord_id': discord_id,
        'ud_matches': ud_matches,
        "ld_matches": ld_matches,
        'errno': errno,
        'error': error
    })


def login(request):

    with open('secrets.json') as secrets:
        url = json.load(secrets)['tokens']['discord-oauth'][os.environ['BUILD_TYPE']]['url']

    return redirect(url)


def login_redirect(request):

    code = request.GET.get('code')
    discord = exchange_code(code)

    request.session['discord'] = discord

    return redirect('home')


def logout(request):

    if 'discord' in request.session:
        del request.session['discord']

    return redirect('home')


def exchange_code(code):

    if os.environ['BUILD_TYPE'] == 'dev':
        redirect = "http://127.0.0.1:8000/login/redirect"
    else:
        redirect = "https://tanukismiteleague.com/login/redirect"

    with open('secrets.json') as secrets:
        oauth = json.load(secrets)['tokens']['discord-oauth'][os.environ['BUILD_TYPE']]
        client_id = oauth['client-id']
        client_secret = oauth['client-secret']

    headers = {
        'Content-Type': 'application/x-www-form-urlencoded'
    }

    data = {
        'client_id': client_id,
        'client_secret': client_secret,
        'grant_type': "authorization_code",
        'code': code,
        'redirect_uri': redirect,
        'scope': "identify"
    }

    response = requests.post(
        url='https://discord.com/api/oauth2/token',
        headers=headers,
        data=data
    )

    credentials = response.json()
    access_token = credentials['access_token']
    response = requests.get("https://discord.com/api/v6/users/@me", headers={
        'Authorization': f'Bearer {access_token}'
    })

    user = response.json()

    return user