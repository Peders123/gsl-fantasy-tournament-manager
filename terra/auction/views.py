from django.shortcuts import render

from tournament.models import Captain


def index(request):

    discord_id = "<blank>"

    if 'discord' in request.session.keys():
        discord_id = str(request.session['discord']['id'])

    captain = Captain.objects.filter(user_id=discord_id).exists()

    return render(request, 'auction/index.html', context={
        'captain': captain
    })


def room(request, room_name):

    return render(request, 'auction/room.html', {'room_name': room_name})
