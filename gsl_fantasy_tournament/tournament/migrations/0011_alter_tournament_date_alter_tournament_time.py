# Generated by Django 4.2 on 2024-07-04 22:12

import datetime
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('tournament', '0010_alter_player_player_id_alter_tournament_date_and_more'),
    ]

    operations = [
        migrations.AlterField(
            model_name='tournament',
            name='date',
            field=models.DateField(default=datetime.datetime(2024, 7, 4, 22, 12, 18, 600240, tzinfo=datetime.timezone.utc)),
        ),
        migrations.AlterField(
            model_name='tournament',
            name='time',
            field=models.TimeField(default=datetime.datetime(2024, 7, 4, 22, 12, 18, 600240, tzinfo=datetime.timezone.utc)),
        ),
    ]