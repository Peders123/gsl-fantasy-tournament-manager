import requests

from django.shortcuts import render

from .models import Match, Game


def overview(request, match_id):

    match_details = requests.get(f"http://192.168.64.1:8002/match/{match_id}/").json()
    print(match_details, flush=True)
    match = Match.from_dict(match_details)
    games = [Game.from_dict(game, match) for game in match_details["games"]]
    match.set_games(games)

    # print(Match.objects.get(id=match_id), flush=True)

    return render(request, 'match/overview.html', context={
        'match': match,
    })
