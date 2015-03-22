from flask_restful import marshal_with, Resource, fields

from ..models import Book
from ..common.utils import get_object_or_404

book_detail = {
    'name': fields.String,
    'category': fields.String,
    'author': fields.String,
    'url': fields.Url('books')
}

books_fields = {
    'array': fields.Nested(book_detail)
}


class BookListAPI(Resource):
    @marshal_with(books_fields)
    def get(self):
        print Book.query.all()
        return {'array': Book.query.all()}

    def post(self):
        return {'error': 'Not implemented!'}


class BookAPI(Resource):
    @marshal_with(book_detail)
    def get(self, id):
        book = get_object_or_404(Book, id=id)
        return book