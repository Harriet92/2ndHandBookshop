import os


class ProductionConfig(object):
    SQLALCHEMY_DATABASE_URI = os.environ['HEROKU_POSTGRESQL_VIOLET_URL']
    DEBUG = False
    SALT = 'soliiimy'
