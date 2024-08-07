from django.shortcuts import redirect, render
from django.urls import reverse

from .models import Bidder
from tournament.models import Captain


def auction(request):

    errno = request.GET.get('errno', None)
    error = request.GET.get('error', None)

    return render(request, 'auction/auction.html', context={
        'errno': errno,
        'error': error
    })


def room(request, room_name):

    discord_id = None

    if 'discord' in request.session.keys():
        discord_id = str(request.session['discord']['id'])

    if not Captain.objects.filter(user_id=discord_id).exists():
        url = reverse('auction')
        error = "User is not registered as a captain."
        return redirect(f"{url}?errno=401&error={error}")
    
    bidders = Bidder.objects.filter(
        tournament_id=1,
        currently_in=True
    ).order_by('join_order')

    print(len(bidders))

    bidders_list = [
        {
            'username': bidder.captain_id.smite_name,
            'team_name': bidder.captain_id.team_name
        }
        for bidder in bidders
    ]

    return render(request, 'auction/room.html', {
        'room_name': room_name,
        'bidders': bidders_list
    })
