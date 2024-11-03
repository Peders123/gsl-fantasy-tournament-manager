import json

from django import template
from django.core.cache import cache
from match.models import Match

register = template.Library()

@register.filter
def get_team(match: Match, team_id):
    return match.get_team(team_id)


@register.filter
def get_god_image(god_id):
    god_data = cache.get("god_data")

    if god_data is None:
        with open("/data/assets/smite/data_gods.json", "r") as file:
            god_data = json.load(file)
        cache.set("god_data", god_data, timeout=60*60*24)

    for god in god_data:
        if god["id"] == god_id:
            return god["godIcon_URL"]
    return None


@register.filter
def get_god_name(god_id):
    god_data = cache.get("god_data")

    if god_data is None:
        with open("/data/assets/smite/data_gods.json", "r", encoding="Windows-1252") as file:
            god_data = json.load(file)
        cache.set("god_data", god_data, timeout=60*60*24)

    for god in god_data:
        if god["id"] == god_id:
            return god["Name"]
    return None
