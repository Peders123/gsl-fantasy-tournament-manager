from django.contrib import admin
from .models import Tournament
from .models import User
from .models import Captain
from .models import Player 
# Register your models here.

admin.site.register(Tournament)
admin.site.register(User)
admin.site.register(Captain)
admin.site.register(Player)
