from flask_restful import Resource

from .representations import books_fields, book_detail
from ..models import Book
from ..common.reqparse import marshal_except_error
from ..common.utils import get_object_or_404, create_error_message
from ..common.login import require_login


class BookListAPI(Resource):
    @require_login
    @marshal_except_error(books_fields)
    def get(self):
        print Book.query.all()
        return {'array': Book.query.all()}

    @require_login
    def post(self):
        return create_error_message('Not implemented!')


class BookAPI(Resource):
    @require_login
    @marshal_except_error(book_detail)
    def get(self, id):
        return get_object_or_404(Book, id=id)
