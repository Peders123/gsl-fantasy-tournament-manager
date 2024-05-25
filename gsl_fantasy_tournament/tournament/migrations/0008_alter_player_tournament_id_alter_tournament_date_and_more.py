# Generated by Django 4.2 on 2024-05-25 16:27

import datetime
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('tournament', '0007_remove_user_tournament_id_player_tournament_id_and_more'),
    ]

    operations = [
        migrations.AlterField(
            model_name='player',
            name='tournament_id',
            field=models.ForeignKey(default=1, on_delete=django.db.models.deletion.CASCADE, to='tournament.tournament'),
        ),
        migrations.AlterField(
            model_name='tournament',
            name='date',
            field=models.DateField(default=datetime.datetime(2024, 5, 25, 16, 27, 5, 529281, tzinfo=datetime.timezone.utc)),
        ),
        migrations.AlterField(
            model_name='tournament',
            name='time',
            field=models.TimeField(default=datetime.datetime(2024, 5, 25, 16, 27, 5, 529281, tzinfo=datetime.timezone.utc)),
        ),
    ]