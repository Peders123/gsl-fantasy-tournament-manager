import re

import requests

from django.core.handlers.asgi import ASGIRequest
from django.shortcuts import render, redirect


def overview(request: ASGIRequest):

    team_player_data = requests.get("http://192.168.64.1:8002/team/users/")

    return render(request, "user/overview.html", context={
        "admin": request.user.is_superuser,
        "teams": team_player_data.json()
    })


def create(request: ASGIRequest):

    if request.POST:

        data = request.POST
        team_response = requests.get(f"http://192.168.64.1:8002/team/{data['division']}/{data['franchise']}")

        player_response = requests.post("http://192.168.64.1:8002/user/", json={
            "discord_name": data["name"]
        })

        requests.post("http://192.168.64.1:8002/user/team/", json={
            "user_id": player_response.json()["id"],
            "team_id": team_response.json()["id"],
            "join_date": None,
            "leave_date": None,
        })

    divisions = requests.get("http://192.168.64.1:8002/division/").json()
    franchises = requests.get("http://192.168.64.1:8002/franchise/").json()

    if not request.user.is_superuser:
        return redirect("player_overview")

    return render(request, "user/create.html", context={
        "divisions": divisions,
        "franchises": franchises
    })


def assign(request: ASGIRequest):

    if request.POST:

        data = list(request.POST.items())

        for i in range(1, len(data), 2):

            _, user_id = data[i]
            _, player_id = data[i + 1]

            if user_id:
                requests.patch(f"http://192.168.64.1:8002/player/{player_id}/set_user/", json={"user_id": int(user_id)})

    players = requests.get("http://192.168.64.1:8002/player/unassigned/").json()
    users = requests.get("http://192.168.64.1:8002/user/").json()

    if not request.user.is_superuser:
        return redirect("player_overview")

    for player in players:
        player["player_name"] = re.sub(r"\[[^\]]*\]", "", player["player_name"])

    return render(request, "user/assign.html", context={
        "players": players,
        "users": users
    })
