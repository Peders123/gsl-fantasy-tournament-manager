from django.shortcuts import redirect, render
from django.urls import reverse

from .models import Bidder, Room, Biddee
from tournament.models import Captain, Player, Tournament


def room(request, t_id):

    discord_id = None

    if 'discord' in request.session.keys():
        discord_id = str(request.session['discord']['id'])

    if not Captain.objects.filter(user_id=discord_id).exists():
        url = reverse('home')
        error = "User is not registered as a captain."
        return redirect(f"{url}?errno=401&error={error}")

    tournament = Tournament.objects.get(tournament_id=t_id)

    if not tournament.is_past():
        url = reverse('home')
        error = "This tournament hasn't started yet."
        return redirect(f"{url}?errno=401&error={error}")

    bidders = Bidder.objects.filter(
        tournament_id=t_id,
        currently_in=True
    ).order_by('join_order')

    bidders_list = [
        {
            'username': bidder.captain_id.smite_name,
            'team_name': bidder.captain_id.team_name,
            'captain_budget': bidder.captain_id.captain_budget
        }
        for bidder in bidders
    ]

    biddees = Biddee.objects.filter(
        tournament_id=t_id
    ).order_by('draft_order')

    biddees_list = [
        {
            'username': biddee.player_id.smite_name,
            'team_id': biddee.player_id.captain_id.smite_name + ":" + biddee.player_id.captain_id.team_name
        }
        for biddee in biddees
    ]

    players = Player.objects.filter(captain_id=None, tournament_id=tournament)

    current_room = Room.objects.filter(tournament_id=t_id)
    if current_room.exists():
        current_room = current_room[0]
    else:
        current_room = Room(tournament_id=Tournament.objects.get(tournament_id=t_id))
        current_room.save()

    return render(request, 'auction/room.html', {
        'bidders': bidders_list,
        'biddees': biddees_list,
        'players': players,
        't_id': str(t_id),
        'room': current_room
    })