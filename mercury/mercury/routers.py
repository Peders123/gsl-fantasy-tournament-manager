class AdminRouter:
    """
    A router to control all database operations on models in the
    admin application to the 'dev' database.
    """
    def db_for_read(self, model, **hints):
        """
        Attempts to read admin models go to 'dev'.
        """
        if model._meta.app_label == 'admin' or model._meta.app_label == 'auth':
            return 'dev'
        return None

    def db_for_write(self, model, **hints):
        """
        Attempts to write admin models go to 'dev'.
        """
        if model._meta.app_label == 'admin' or model._meta.app_label == 'auth':
            return 'dev'
        return None

    def allow_relation(self, obj1, obj2, **hints):
        """
        Allow relations if a model in the admin app is involved.
        """
        if obj1._meta.app_label == 'admin' or obj2._meta.app_label == 'admin':
            return True
        return None

    def allow_migrate(self, db, app_label, model_name=None, **hints):
        """
        Make sure the admin app only appears in the 'dev' database.
        """
        if app_label == 'admin' or app_label == 'auth':
            return db == 'dev'
        return None
