# Generated by Django 4.2 on 2024-05-25 14:19

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('tournament', '0003_tournament_description_tournament_title'),
    ]

    operations = [
        migrations.AlterField(
            model_name='tournament',
            name='date',
            field=models.DateTimeField(),
        ),
    ]
