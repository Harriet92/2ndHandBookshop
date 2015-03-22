from flask_restful import Api

from .app import app
from .resources.book import BookListAPI, BookAPI
from .resources.user import UserListAPI, UserAPI


def register_resources(api):
    api.add_resource(UserListAPI, '/users', endpoint='users')
    api.add_resource(UserAPI, '/users/<int:id>', endpoint='user')
    api.add_resource(BookListAPI, '/books', endpoint='books')
    api.add_resource(BookAPI, '/books/<int:id>', endpoint='book')

api = Api(app)
register_resources(api)
