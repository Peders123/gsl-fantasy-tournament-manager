"""
Django admin module.
"""
from django.contrib import admin
from rest_framework.authtoken.models import Token
from .models import Tournament, Captain, Player, User


class TournamentAdmin(admin.ModelAdmin):

    list_display = ('tournament_id', 'title', 'datetime')


class CaptainAdmin(admin.ModelAdmin):

    list_display = ('get_discord_name', 'smite_name', 'team_name', 'captain_budget')

    def get_discord_name(self, obj):
        return obj.user_id.discord_name
    get_discord_name.short_description = 'Discord Name'


class PlayerAdmin(admin.ModelAdmin):

    list_display = ('get_discord_name', 'smite_name', 'role_1', 'role_2', 'estimated_value')

    def get_discord_name(self, obj):
        return obj.user_id.discord_name
    get_discord_name.short_description = 'Discord Name'


class UserAdmin(admin.ModelAdmin):

    list_display = ('user_id', 'discord_name')


admin.register(Token)
admin.site.register(Tournament, TournamentAdmin)
admin.site.register(Captain, CaptainAdmin)
admin.site.register(Player, PlayerAdmin)
admin.site.register(User, UserAdmin)
