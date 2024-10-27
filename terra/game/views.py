import requests

from django.shortcuts import render


def overview(request, game_id):

    game_details = requests.get(f"http://192.168.64.1:8002/game/{game_id}/").json()
    match_details = requests.get(f"http://192.168.64.1:8002/match/{game_details['match_id']}/")

    order_team_data = []
    chaos_team_data = []

    for player_data in game_details["total_player_data"]:
        order_team_data.append(player_data) if player_data["team_id"] == game_details["order_team_id"] else chaos_team_data.append(player_data)

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
    })
