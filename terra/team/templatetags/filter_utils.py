from datetime import datetime

from django import template

register = template.Library()

@register.filter
def shorten(input: str):
    return f"{''.join([word[0] for word in input.split()])}"


@register.filter
def get_result(match):

    team_1_id = match["team1"]["id"]
    team_2_id = match["team2"]["id"]

    wins = [0, 0]

    for game in match["games"]:
        if game["winning_team_id"] == team_1_id:
            wins[0] += 1
        else:
            wins[1] += 1

    return {
        "result": f"{wins[0]} - {wins[1]}",
        "winner": team_1_id if wins[0] > wins[1] else team_2_id
    }


@register.filter
def convert_time(datestr: str):

    dt = datetime.fromisoformat(datestr)

    # Format the datetime object into the desired format
    formatted_date = dt.strftime("%a - %d %b - %I:%M %p")

    # Replace the day with its ordinal representation
    day = dt.day
    ordinal_day = f"{day}{'th' if 10 <= day % 100 <= 20 else {1: 'st', 2: 'nd', 3: 'rd'}.get(day % 10, 'th')}"
    formatted_date = formatted_date.replace(f"{day:02}", ordinal_day)

    return formatted_date
