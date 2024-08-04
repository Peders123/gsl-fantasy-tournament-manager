from django.http import HttpResponse, JsonResponse
from django.shortcuts import redirect, render
import requests
import json
import os
from tournament.models import Tournament


def index(request):

    tournaments = Tournament.objects.all()

    signed_in = False
    discord_id = ""

    if 'user' in request.session.keys():
        signed_in = True
        discord_id = request.session['user']['id']

    context = {
        'signed_in': signed_in,
        'discord_id': discord_id
    }

    return render(request, 'home/home.html', context)


def login(request):
    
    with open('secrets.json') as secrets:
        url = json.load(secrets)['tokens']['discord-oauth'][os.environ['BUILD_TYPE']]['url']

    return redirect(url)


def login_redirect(request):

    code = request.GET.get('code')
    user = exchange_code(code)

    request.session['user'] = user

    return redirect('home')


def exchange_code(code):

    with open('secrets.json') as secrets:
        oauth = json.load(secrets)['tokens']['discord-oauth'][os.environ['BUILD_TYPE']]
        client_id = oauth['client-id']
        client_secret = oauth['client-secret']
        redirect = "http://127.0.0.1:8000/login/redirect"

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
