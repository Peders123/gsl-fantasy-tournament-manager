from django.shortcuts import render

from datetime import datetime

from tournament.models import Tournament

def overview(request, date):

    tournament = Tournament.objects.get(date=datetime.strptime(date, '%Y%m%d').date())

    return render(request, 'tournament/overview.html', context={
        "tournament": tournament
    })
