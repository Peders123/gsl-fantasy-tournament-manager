from django.shortcuts import redirect, render

from tournament.models import Tournament, Player, Captain, User, Suggestion


def overview(request, t_id):
    signed_in = False
    discord_id = ""

    if 'discord' in request.session.keys():
        signed_in = True
        discord_id = request.session['discord']['id']

    tournament = Tournament.objects.get(tournament_id=t_id)

    return render(request, 'tournament/overview.html', context={
        'discord_id': discord_id,
        'signed_in': signed_in,
        "tournament": tournament
    })


def auction(request, t_id):
    signed_in = False
    discord_id = ""

    if 'discord' in request.session.keys():
        signed_in = True
        discord_id = request.session['discord']['id']

    tournament = Tournament.objects.get(tournament_id=t_id)

    return render(request, 'tournament/auction.html', context={
        'discord_id': discord_id,
        "signed_in": signed_in,
        "tournament": tournament
    })


def players(request, t_id):
    signed_in = False
    discord_id = ""

    if 'discord' in request.session.keys():
        signed_in = True
        discord_id = request.session['discord']['id']

    tournament = Tournament.objects.get(tournament_id=t_id)

    tournament_players = Player.objects.filter(tournament_id=tournament)

    return render(request, 'tournament/players.html', context={
        'discord_id': discord_id,
        'signed_in': signed_in,
        "tournament": tournament,
        "players": tournament_players
    })


def captains(request, t_id):
    signed_in = False
    discord_id = ""

    if 'discord' in request.session.keys():
        signed_in = True
        discord_id = request.session['discord']['id']

    tournament = Tournament.objects.get(tournament_id=t_id)

    tournament_captains = Captain.objects.filter(tournament_id=tournament)

    return render(request, 'tournament/captains.html', context={
        'discord_id': discord_id,
        'signed_in': signed_in,
        "tournament": tournament,
        "captains": tournament_captains
    })


def suggestions(request, t_id):
    signed_in = False
    discord_id = None
    tournament = Tournament.objects.get(tournament_id=t_id)
    all_players = Player.objects.all()

    if 'discord' in request.session.keys():
        signed_in = True
        discord_id = str(request.session['discord']['id'])

    if not Captain.objects.filter(user_id=discord_id).exists():
        return redirect('home')

    return render(request, 'tournament/suggestions.html', context={
        "tournament": tournament,
        'signed_in': signed_in,
        'discord_id': discord_id,
        "players": all_players
    })


def player_suggestion(request, t_id, p_id):
    signed_in = False
    discord_id = None
    post = 0
    tournament = Tournament.objects.get(tournament_id=t_id)
    player = Player.objects.get(player_id=p_id)
    if 'discord' in request.session.keys():
        signed_in = True
        discord_id = str(request.session['discord']['id'])
    if not Captain.objects.filter(user_id=discord_id).exists():
        return redirect('home')
    user = User.objects.get(user_id=discord_id)
    try:
        value_input = request.POST['value']
        post = 1
    except KeyError:
        post = 0
    if post == 1:
        Suggestion.objects.create(
            player_name=player.smite_name,
            tournament_id=t_id,
            discord_nametag=user.discord_name,
            suggested_value=value_input
        )
        return redirect('home')
    else:
        return render(request, 'tournament/player_suggestion.html', context={
            "tournament": tournament,
            'signed_in': signed_in,
            'discord_id': discord_id,
            "player": player
        })
