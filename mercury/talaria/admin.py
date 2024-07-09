"""
Django admin module.
"""
from django.contrib import admin
from rest_framework.authtoken.models import Token

admin.register(Token)
