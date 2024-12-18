"""
Django settings for terra project.

Generated by 'django-admin startproject' using Django 4.2.

For more information on this file, see
https://docs.djangoproject.com/en/4.2/topics/settings/

For the full list of settings and their values, see
https://docs.djangoproject.com/en/4.2/ref/settings/
"""
import json
import os
import sys
from pathlib import Path

with open(os.path.join('secrets.json')) as secrets:
    CREDENTIALS = json.load(secrets)

# Build paths inside the project like this: BASE_DIR / 'subdir'.
BASE_DIR = Path(__file__).resolve().parent.parent

# Define the path to your custom migrations
MIGRATIONS_PATH = Path("/data/terra/mount/terra/migrations")

# Add the custom migrations path to the system path
sys.path.append(str(MIGRATIONS_PATH))

MIGRATION_MODULES = {
    'auction': 'mount.terra.migrations',
}

CSRF_TRUSTED_ORIGINS = ['https://tanukismiteleague.com']
# Quick-start development settings - unsuitable for production
# See https://docs.djangoproject.com/en/4.2/howto/deployment/checklist/

# SECURITY WARNING: keep the secret key used in production secret!
SECRET_KEY = CREDENTIALS['tokens']['secret-key']['terra']

# SECURITY WARNING: don't run with debug turned on in production!
DEBUG = False if os.environ['BUILD_TYPE'] == "ops" else True

ALLOWED_HOSTS = ['*']

BASE_URL = "http://192.168.64.1:8002" if os.environ["BUILD_TYPE"] == "dev" else "https://thoth.tanukismiteleague.com"
HEADERS = {
    "Authorization": CREDENTIALS['tokens']['secret-key']['thoth']
}

DATABASE_SETUPS = {
    'dev': {
        'default': {
            'ENGINE': "django.db.backends.postgresql",
            'NAME': "postgres",
            'USER': "Pedro",
            'PASSWORD': CREDENTIALS['passwords']['postgres']['Pedro'],
            'HOST': "djehuty.postgres.database.azure.com",
            'PORT': "5432",
            'OPTIONS': {"sslmode": "require"},
        }
    },
    'ops': {
        'default': {
            'ENGINE': "django.db.backends.postgresql",
            'NAME': "djehuty",
            'USER': "Cadueceus",
            'PASSWORD': CREDENTIALS['passwords']['djehuty']['Cadueceus'],
            'HOST': "djehuty.postgres.database.azure.com",
            'PORT': "5432",
            'OPTIONS': {"sslmode": "require"},
        }
    }
}


# Application definition

INSTALLED_APPS = [
    'whitenoise.runserver_nostatic',
    'home.apps.HomeConfig',
    'game.apps.GameConfig',
    'tournament.apps.TournamentConfig',
    'auction.apps.AuctionConfig',
    'match.apps.MatchConfig',
    'user.apps.UserConfig',
    'team.apps.TeamConfig',
    'staff.apps.StaffConfig',
    'django.contrib.admin',
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
]

MIDDLEWARE = [
    'django.middleware.security.SecurityMiddleware',
    'whitenoise.middleware.WhiteNoiseMiddleware',
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.common.CommonMiddleware',
    'django.middleware.csrf.CsrfViewMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
    'whitenoise.middleware.WhiteNoiseMiddleware',
]

ROOT_URLCONF = 'terra.urls'

TEMPLATES = [
    {
        'BACKEND': 'django.template.backends.django.DjangoTemplates',
        'DIRS': [os.path.join(BASE_DIR, 'templates')],
        'APP_DIRS': True,
        'OPTIONS': {
            'context_processors': [
                'django.template.context_processors.debug',
                'django.template.context_processors.request',
                'django.contrib.auth.context_processors.auth',
                'django.contrib.messages.context_processors.messages',
            ],
        },
    },
]

WSGI_APPLICATION = 'terra.wsgi.application'


# Database
# https://docs.djangoproject.com/en/4.2/ref/settings/#databases

DATABASES = DATABASE_SETUPS[os.environ['BUILD_TYPE']]

# Password validation
# https://docs.djangoproject.com/en/4.2/ref/settings/#auth-password-validators

AUTH_PASSWORD_VALIDATORS = [
    {
        'NAME': 'django.contrib.auth.password_validation.UserAttributeSimilarityValidator',
    },
    {
        'NAME': 'django.contrib.auth.password_validation.MinimumLengthValidator',
    },
    {
        'NAME': 'django.contrib.auth.password_validation.CommonPasswordValidator',
    },
    {
        'NAME': 'django.contrib.auth.password_validation.NumericPasswordValidator',
    },
]


CACHES = {
    "default": {
        "BACKEND": "django.core.cache.backends.locmem.LocMemCache",
    }
}


# Internationalization
# https://docs.djangoproject.com/en/4.2/topics/i18n/

LANGUAGE_CODE = 'en-us'

TIME_ZONE = 'UTC'

USE_I18N = True

USE_TZ = True


# Static files (CSS, JavaScript, Images)
# https://docs.djangoproject.com/en/4.2/howto/static-files/

STATIC_URL = '/static/'
STATIC_ROOT = os.path.join(BASE_DIR, 'staticfiles')
STATICFILES_DIRS = (
    os.path.join(BASE_DIR, 'static'),
)
STATICFILES_STORAGE = 'whitenoise.storage.CompressedManifestStaticFilesStorage'


# Default primary key field type
# https://docs.djangoproject.com/en/4.2/ref/settings/#default-auto-field

DEFAULT_AUTO_FIELD = 'django.db.models.BigAutoField'

ASGI_APPLICATION = 'terra.asgi.application'
CHANNEL_LAYERS = {
    'default': {
        'BACKEND': 'channels_redis.core.RedisChannelLayer',
        'CONFIG': {
            'hosts': [('rediss://:{password}@{hostname}:{port}'.format(password=CREDENTIALS['passwords']['redis']['password'], hostname="odin.redis.cache.windows.net", port=6380))]
        }
    }
}
