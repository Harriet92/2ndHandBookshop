from flask_restful import Api

from .app import app
from .resources.session import SessionAPI
from .resources.book import BookListAPI, BookAPI
from .resources.user import UserListAPI, UserAPI
from .resources.offer import BookOfferAPI, BookOfferListAPI, SetOfferStatus


def register_resources(api):
    api.add_resource(UserListAPI, '/users', endpoint='users')
    api.add_resource(UserAPI, '/users/<int:id>', endpoint='user')
    api.add_resource(SessionAPI, '/users/login', endpoint='login')
    api.add_resource(BookListAPI, '/books', endpoint='books')
    api.add_resource(BookAPI, '/books/<int:id>', endpoint='book')
    api.add_resource(BookOfferListAPI, '/offers', endpoint='offers')
    api.add_resource(BookOfferAPI, '/offers/<int:id>', endpoint='offer')
    api.add_resource(SetOfferStatus, '/offers/setstatus', endpoint='setstatus')

api = Api(app)
register_resources(api)
