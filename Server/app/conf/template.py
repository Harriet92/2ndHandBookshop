# rename to local.py and fill connection data

USER = 'user'
PASS = 'pass'
URL = 'localhost'
DB = 'db_name'


class LocalConfig(object):
    # uncomment for postgres
    # SQLALCHEMY_DATABASE_URI = 'postgres://{0}:{1}@{2}:{3}/{4}'.format(
    #     USER, PASS, URL, '5432', DB
    # )
    SQLALCHEMY_DATABASE_URI = "sqlite:///wpam.db"
    DEBUG = True
