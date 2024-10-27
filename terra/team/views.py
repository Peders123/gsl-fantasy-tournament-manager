import requests

from django.core.handlers.asgi import ASGIRequest
from django.http import JsonResponse, HttpResponseForbidden
from django.shortcuts import render, redirect
from django.views.decorators.http import require_http_methods


BASE_URL = "http://192.168.64.1:8002"


def overview(request: ASGIRequest):

    divisions = requests.get(f"{BASE_URL}/division/").json()

    return render(request, "user/overview.html", context={
        "admin": request.user.is_superuser,
        "divisions": divisions
    })


@require_http_methods(["GET"])
def division_teams(request: ASGIRequest, division_id):
    if request.headers.get('X-Requested-With') != 'XMLHttpRequest':
        return HttpResponseForbidden("Forbidden: This endpoint can only be accessed by internal requests.")

    response = requests.get(f"{BASE_URL}/team/users/{division_id}/").json()
    
    return JsonResponse({"teams": response})