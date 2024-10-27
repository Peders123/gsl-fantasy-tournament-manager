import re

import requests

from django.core.handlers.asgi import ASGIRequest
from django.shortcuts import render, redirect


BASE_URL = "http://192.168.64.1:8002"


def is_admin(func):
    def decorator(request: ASGIRequest, *args, **kwargs):
        if not request.user.is_superuser:
            return redirect("home")
        return(func(request, *args, **kwargs))
    return decorator


@is_admin
def overview(request):

    return render(request, "staff/overview.html")


@is_admin
def create(request: ASGIRequest):

    if request.POST:

        data = request.POST
        team_response = requests.get(f"{BASE_URL}/team/{data['division']}/{data['franchise']}")

        player_response = requests.post(f"{BASE_URL}/user/", json={
            "discord_name": data["name"]
        })

        requests.post(f"{BASE_URL}/user/team/", json={
            "user_id": player_response.json()["id"],
            "team_id": team_response.json()["id"],
            "join_date": None,
            "leave_date": None,
        })

    divisions = requests.get(f"{BASE_URL}/division/").json()
    franchises = requests.get(f"{BASE_URL}/franchise/").json()

    if not request.user.is_superuser:
        return redirect("player_overview")

    return render(request, "staff/create.html", context={
        "divisions": divisions,
        "franchises": franchises
    })


@is_admin
def transfer(request: ASGIRequest):

    if request.POST:
        data = request.POST

        if "drop" in data:
            team_id = None

        else:
            team_response = requests.get(f"{BASE_URL}/team/{data['division']}/{data['franchise']}")
            team_id = team_response.json()["id"]

        requests.post(f"{BASE_URL}/user/team/", json={
            "user_id": data["user"],
            "team_id": team_id,
            "join_date": None,
            "leave_date": None,
        })

    divisions = requests.get(f"{BASE_URL}/division/").json()
    franchises = requests.get(f"{BASE_URL}/franchise/").json()
    users = requests.get(f"{BASE_URL}/user/").json()

    if not request.user.is_superuser:
        return redirect("player_overview")

    return render(request, "staff/transfer.html", context={
        "divisions": divisions,
        "franchises": franchises,
        "users": users
    })


@is_admin
def assign(request: ASGIRequest):

    if request.POST:

        data = list(request.POST.items())

        for i in range(1, len(data), 2):

            _, user_id = data[i]
            _, player_id = data[i + 1]

            if user_id:
                requests.patch(f"{BASE_URL}/player/{player_id}/set_user/?user_id={int(user_id)}")

    players = requests.get(f"{BASE_URL}/player/unassigned/").json()
    users = requests.get(f"{BASE_URL}/user/").json()

    if not request.user.is_superuser:
        return redirect("player_overview")

    for player in players:
        player["player_name"] = re.sub(r"\[[^\]]*\]", "", player["player_name"])

    return render(request, "staff/assign.html", context={
        "players": players,
        "users": users
    })

