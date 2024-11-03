import requests

from django.conf import settings
from django.shortcuts import render

from .models import Match, Game


def overview(request, match_id):

    match_details = requests.get(
        f"{settings.BASE_URL}/match/{match_id}/",
        headers=settings.HEADERS
    ).json()
    match = Match.from_dict(match_details)
    games = [Game.from_dict(game, match) for game in match_details["games"]]
    match.set_games(games)

    return render(request, 'match/overview.html', context={
        'match': match,
    })
