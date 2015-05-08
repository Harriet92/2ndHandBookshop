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

offer_detail = {
    'id': fields.Integer,
    'ownerid': fields.Integer,
    'purchaserid': fields.Integer,
    'created': fields.DateTime,
    'expiresat': fields.DateTime,
    'booktitle': fields.String,
    'bookauthor': fields.String,
    'description': fields.String,
    'tags': fields.String,
    'photobase64': fields.Raw,
    'price': fields.Integer,
    'status': fields.Integer
}

offers_fields = {
    'array': fields.Nested(offer_detail)
}
