USER = 'valar'
PASS = 'hunter'
URL = 'localhost'
DB = 'wpam'


class LocalConfig(object):
    SQLALCHEMY_DATABASE_URI = 'postgres://{0}:{1}@{2}:{3}/{4}'.format(
        USER, PASS, URL, '5432', DB
    )
    DEBUG = True
