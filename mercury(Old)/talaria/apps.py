"""
Sets up app config for the Talaria app.
"""
from django.apps import AppConfig


class TalariaConfig(AppConfig):
    """
    Basic config for Talaria app.
    """
    default_auto_field = 'django.db.models.BigAutoField'
    name = 'talaria'
