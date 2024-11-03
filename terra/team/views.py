import requests

from django.core.handlers.asgi import ASGIRequest
from django.http import JsonResponse, HttpResponseForbidden
from django.shortcuts import render
from django.views.decorators.http import require_http_methods


BASE_URL = "http://192.168.64.1:8002"


def overview(request: ASGIRequest):

    divisions = requests.get(f"{BASE_URL}/division/").json()

    return render(request, "team/overview.html", context={
        "divisions": divisions
    })


def profile(request: ASGIRequest, team_id: int):

    team = requests.get(f"{BASE_URL}/team/{team_id}/").json()
    history = requests.get(f"{BASE_URL}/team/{team_id}/history/").json()

    standings = requests.get(f"{BASE_URL}/divisions/{team['division']['id']}/standings/").json()

    standing_data = []
    rank = 0

    for count, team_data in enumerate(standings):
        if team_data["id"] == team["id"]:
            standing_data = team_data
            rank = count
            break

    return render(request, "team/profile.html", context={
        "team": team,
        "matches": history,
        "standing": standing_data,
        "rank": rank,
    })


def standings(request: ASGIRequest, division_id: int):

    division = requests.get(f"{BASE_URL}/division/{division_id}/").json()
    standings = requests.get(f"{BASE_URL}/divisions/{division_id}/standings/").json()

    return render(request, "team/standings.html", context={
        "division": division,
        "standings": standings,
    })


@require_http_methods(["GET"])
def division_teams(request: ASGIRequest, division_id):
    if request.headers.get('X-Requested-With') != 'XMLHttpRequest':
        return HttpResponseForbidden("Forbidden: This endpoint can only be accessed by internal requests.")

    response = requests.get(f"{BASE_URL}/team/users/{division_id}/").json()
    
    return JsonResponse({"teams": response})
