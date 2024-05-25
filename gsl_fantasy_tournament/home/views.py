from django.http import HttpResponse

from django.shortcuts import render


def index(request):

    context = {
        "test_context": "HELLO WORLD"
    }

    return render(request, 'home/home.html', context)

    # return HttpResponse("GSL Home")
