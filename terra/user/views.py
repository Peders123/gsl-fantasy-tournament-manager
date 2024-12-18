import re

import requests

from django.conf import settings
from django.core.handlers.asgi import ASGIRequest
from django.http import JsonResponse, HttpResponseForbidden
from django.shortcuts import render, redirect
from django.views.decorators.http import require_http_methods


def overview(request: ASGIRequest):

    divisions = requests.get(
        f"{settings.BASE_URL}/division/",
        headers=settings.HEADERS
    ).json()

    return render(request, "user/overview.html", context={
        "admin": request.user.is_superuser,
        "divisions": divisions
    })


@require_http_methods(["GET"])
def division_teams(request: ASGIRequest, division_id):
    if request.headers.get('X-Requested-With') != 'XMLHttpRequest':
        return HttpResponseForbidden("Forbidden: This endpoint can only be accessed by internal requests.")

    response = requests.get(
        f"{settings.BASE_URL}/team/users/{division_id}/",
        headers=settings.HEADERS
    ).json()
    
    return JsonResponse({"teams": response})


def transfer(request: ASGIRequest):

    if request.POST:
        data = request.POST

        if "drop" in data:
            team_id = None

        else:
            team_response = requests.get(
                f"{settings.BASE_URL}/team/{data['division']}/{data['franchise']}",
                headers=settings.HEADERS
            )
            team_id = team_response.json()["id"]

        requests.post(
            f"{settings.BASE_URL}/user/team/",
            headers=settings.HEADERS,
            json={
                "user_id": data["user"],
                "team_id": team_id,
                "join_date": None,
                "leave_date": None,
            }
        )

    divisions = requests.get(
        f"{settings.BASE_URL}/division/",
        headers=settings.HEADERS
    ).json()
    franchises = requests.get(
        f"{settings.BASE_URL}/franchise/",
        headers=settings.HEADERS
    ).json()
    users = requests.get(
        f"{settings.BASE_URL}/user/",
        headers=settings.HEADERS
    ).json()

    if not request.user.is_superuser:
        return redirect("player_overview")

    return render(request, "user/transfer.html", context={
        "divisions": divisions,
        "franchises": franchises,
        "users": users
    })


def assign(request: ASGIRequest):

    if request.POST:

        data = list(request.POST.items())

        for i in range(1, len(data), 2):

            _, user_id = data[i]
            _, player_id = data[i + 1]

            if user_id:
                requests.patch(
                    f"{settings.BASE_URL}/player/{player_id}/set_user/",
                    headers=settings.HEADERS,
                    json={"user_id": int(user_id)}
                )

    players = requests.get(
        f"{settings.BASE_URL}/player/unassigned/",
        headers=settings.HEADERS
    ).json()
    users = requests.get(
        f"{settings.BASE_URL}/user/",
        headers=settings.HEADERS
    ).json()

    if not request.user.is_superuser:
        return redirect("player_overview")

    for player in players:
        player["player_name"] = re.sub(r"\[[^\]]*\]", "", player["player_name"])

    return render(request, "user/assign.html", context={
        "players": players,
        "users": users
    })
