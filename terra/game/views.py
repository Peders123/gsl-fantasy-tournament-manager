import requests

from django.conf import settings
from django.shortcuts import render


def overview(request, game_id):

    game_details = requests.get(
        f"{settings.BASE_URL}/game/{game_id}/",
        headers=settings.HEADERS
    ).json()
    match_details = requests.get(
        f"{settings.BASE_URL}/match/{game_details['match_id']}/",
        headers=settings.HEADERS
    ).json()

    game_number = 0
    for count, game in enumerate(match_details["games"]):
        if game_id == game["id"]:
            game_number = count + 1
            break

    order_team_data = []
    chaos_team_data = []
    player_ids = []

    for player_data in game_details["total_player_data"]:
        order_team_data.append(player_data) if player_data["team_id"] == game_details["order_team_id"] else chaos_team_data.append(player_data)
        player_ids.append(player_data["player_id"])

    players = requests.post(
        f"{settings.BASE_URL}/player/batch/",
        headers=settings.HEADERS,
        json={"ids": player_ids}
    ).json()

    del game_details["total_player_data"]

    teams = [
        {
            "team_id": game_details["order_team_id"],
            "team_data": order_team_data
        },
        {
            "team_id": game_details["chaos_team_id"],
            "team_data": chaos_team_data
        },
    ]

    return render(request, "game/overview.html", context={
        "match": match_details,
        "game": game_details,
        "teams": teams,
        "players": players,
        "number": game_number,
    })
