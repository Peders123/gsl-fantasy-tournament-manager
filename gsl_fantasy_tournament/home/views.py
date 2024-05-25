from django.shortcuts import render

from tournament.models import Tournament


def index(request):

    tournaments = Tournament.objects.all()

    context = {
        "tournaments": tournaments
    }

    return render(request, 'home/home.html', context)
