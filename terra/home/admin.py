from django.contrib import admin

from .models import Suggestion

class SuggestionAdmin(admin.ModelAdmin):
    
    list_display = ("suggestion_id","player_name","discord_nametag","suggested_value","tournament_id")
    
admin.site.register(Suggestion, SuggestionAdmin)
