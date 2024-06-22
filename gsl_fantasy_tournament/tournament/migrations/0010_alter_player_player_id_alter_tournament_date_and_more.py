# Generated by Django 5.0.6 on 2024-06-22 14:00

import datetime
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('tournament', '0009_alter_tournament_date_alter_tournament_time'),
    ]

    operations = [
        migrations.AlterField(
            model_name='player',
            name='player_id',
            field=models.AutoField(primary_key=True, serialize=False),
        ),
        migrations.AlterField(
            model_name='tournament',
            name='date',
            field=models.DateField(default=datetime.datetime(2001, 11, 5, 0, 0)),
        ),
        migrations.AlterField(
            model_name='tournament',
            name='time',
            field=models.TimeField(default=datetime.datetime(1, 1, 1, 12, 0)),
        ),
    ]
