from django.shortcuts import redirect, render
from django.urls import reverse

from tournament.models import Captain


def auction(request):

    errno = request.GET.get('errno', None)
    error = request.GET.get('error', None)

    discord_id = "<blank>"

    if 'discord' in request.session.keys():
        discord_id = str(request.session['discord']['id'])

    captain = Captain.objects.filter(user_id=discord_id).exists()

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

    return render(request, 'auction/room.html', {'room_name': room_name})
