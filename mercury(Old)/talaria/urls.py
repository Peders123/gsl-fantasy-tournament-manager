from django.urls import include, path

from rest_framework.routers import DefaultRouter
from .views import TournamentViewSet, UserViewSet, CaptainViewSet, PlayerViewSet

router = DefaultRouter()
router.register('tournaments', TournamentViewSet)
router.register('users', UserViewSet)
router.register('captains', CaptainViewSet)
router.register('players', PlayerViewSet)

urlpatterns = [
    path('', include(router.urls))
]
