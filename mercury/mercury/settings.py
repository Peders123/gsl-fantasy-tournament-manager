"""
Django settings for mercury project.

Generated by 'django-admin startproject' using Django 4.2.

For more information on this file, see
https://docs.djangoproject.com/en/4.2/topics/settings/

For the full list of settings and their values, see
https://docs.djangoproject.com/en/4.2/ref/settings/
"""
import os
import sys
import json
from pathlib import Path

# Build paths inside the project like this: BASE_DIR / 'subdir'.
BASE_DIR = Path(__file__).resolve().parent.parent

# Define the path to your custom migrations
MIGRATIONS_PATH = Path("/data/mercury/mount/mercury/migrations")

# Add the custom migrations path to the system path
sys.path.append(str(MIGRATIONS_PATH))

MIGRATION_MODULES = {
    'talaria': 'mount.mercury.migrations',
}

# Quick-start development settings - unsuitable for production
# See https://docs.djangoproject.com/en/4.2/howto/deployment/checklist/
with open(os.path.join('secrets.json'),"r") as A:
    credential = json.load(A)
# SECURITY WARNING: keep the secret key used in production secret!
SECRET_KEY = 'django-insecure-&5w+v%pk339$m6+n)(pujq8@zl#$&o*#7h_14$k#eb6u+1azsf'

# SECURITY WARNING: don't run with debug turned on in production!
DEBUG = True

ALLOWED_HOSTS = ['*']

DATABASE_SETUPS = {
    "dev": {
        'default': {
            'NAME': BASE_DIR / 'db.sqlite3',
            'ENGINE': 'django.db.backends.sqlite3',
        }
    },
    "ops": {
        "default": {
            "ENGINE": "django.db.backends.postgresql",
            "NAME": "djehuty",
            "USER": "Cadueceus",
            "PASSWORD": credential["password"]["djehuty"]["Cadueceus"],  
            "HOST": "djehuty.postgres.database.azure.com",
            "PORT": "5432",
            "OPTIONS": {"sslmode": "require"},
        }
    }
}

# Application definition

INSTALLED_APPS = [
    'talaria.apps.TalariaConfig',
    'rest_framework',
    'rest_framework.authtoken',
    'django.contrib.admin',
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
]

REST_FRAMEWORK = {
    'DEFAULT_AUTHENTICATION_CLASSES': [
        'rest_framework.authentication.TokenAuthentication',
        'rest_framework.authentication.SessionAuthentication'
    ],
    'DEFAULT_PERMISSION_CLASSES': [
        'rest_framework.permissions.IsAuthenticated'
    ]
}

MIDDLEWARE = [
    'django.middleware.security.SecurityMiddleware',
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.common.CommonMiddleware',
    'django.middleware.csrf.CsrfViewMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
]

ROOT_URLCONF = 'mercury.urls'

TEMPLATES = [
    {
        'BACKEND': 'django.template.backends.django.DjangoTemplates',
        'DIRS': [],
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

WSGI_APPLICATION = 'mercury.wsgi.application'


# Database
# https://docs.djangoproject.com/en/4.2/ref/settings/#databases

print(f"BUILD: {os.environ['BUILD_TYPE']}")
print(os.getcwd())
print(os.listdir("mount/mercury"))

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




# Internationalization
# https://docs.djangoproject.com/en/4.2/topics/i18n/

LANGUAGE_CODE = 'en-us'

TIME_ZONE = 'UTC'

USE_I18N = True

USE_TZ = True


# Static files (CSS, JavaScript, Images)
# https://docs.djangoproject.com/en/4.2/howto/static-files/

STATIC_URL = 'static/'

# Default primary key field type
# https://docs.djangoproject.com/en/4.2/ref/settings/#default-auto-field

DEFAULT_AUTO_FIELD = 'django.db.models.BigAutoField'
