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
