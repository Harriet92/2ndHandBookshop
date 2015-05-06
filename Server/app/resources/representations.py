from flask_restful import fields


class Money(fields.Integer):

    def __init__(self, precision=2, **kwargs):
        self._precision = precision
        super(Money, self).__init__(**kwargs)

    def format(self, value):
        return 1.0 * value / 10 ** self._precision


user_detail = {
    'name': fields.String,
    'url': fields.Url('user'),
    'email': fields.String,
    'money': Money
}

users_fields = {
    'array': fields.Nested(user_detail)
}

session_data = {
    'expiration_date': fields.DateTime,
    'token': fields.String,
    'user': fields.Nested(user_detail)
}

book_detail = {
    'name': fields.String,
    'category': fields.String,
    'author': fields.String,
    'url': fields.Url('books')
}

books_fields = {
    'array': fields.Nested(book_detail)
}
