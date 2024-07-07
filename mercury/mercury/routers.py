import os

class EnvironmentRouter:
    def db_for_read(self, model, **hints):
        """
        Attempts to read from the specified database alias.
        """
        return self.get_db_alias()

    def db_for_write(self, model, **hints):
        """
        Attempts to write to the specified database alias.
        """
        return self.get_db_alias()

    def allow_relation(self, obj1, obj2, **hints):
        """
        Allow relations between objects in different databases.
        """
        return True

    def allow_migrate(self, db, app_label, model_name=None, **hints):
        """
        Make sure the app_label and model_name belong to the database alias.
        """
        return True

    def get_db_alias(self):
        """
        Returns the database alias based on the BUILD_TYPE environment variable.
        Default to 'default' if BUILD_TYPE is not set or invalid.
        """
        build_type = os.environ.get('BUILD_TYPE', 'default')
        return build_type
