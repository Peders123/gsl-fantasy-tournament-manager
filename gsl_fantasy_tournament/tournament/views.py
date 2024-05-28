from django.shortcuts import render

from datetime import datetime

from tournament.models import Tournament, Player 



def overview(request, date):

    tournament = Tournament.objects.get(date=datetime.strptime(date, '%Y%m%d').date())

    return render(request, 'tournament/overview.html', context={
        "tournament": tournament
    })


def auction(request, date):

    tournament = Tournament.objects.get(date=datetime.strptime(date, '%Y%m%d').date())

    return render(request, 'tournament/auction.html', context={
        "tournament": tournament
    })


def players(request, date):

    tournament = Tournament.objects.get(date=datetime.strptime(date, '%Y%m%d').date())

    tournament_players = Player.objects.filter(tournament_id=tournament)

    return render(request, 'tournament/players.html', context={
        "tournament": tournament,
        "players": tournament_players
    })

