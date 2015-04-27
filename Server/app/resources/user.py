from flask_restful import fields, Resource, marshal_with, reqparse, marshal

from ..models import User, db
from ..common.utils import get_object_or_404, is_email_valid, create_error_message
from ..common.reqparse import require_arguments
from ..common.login import require_login

user_detail = {
    'name': fields.String,
    'url': fields.Url('user'),
    'email': fields.String
}

users_fields = {
    'array': fields.Nested(user_detail)
}

register_parameters = (
    reqparse.Argument('name', type=str, required=True, help="You must provide name!"),
    reqparse.Argument('email', type=str, required=True, help="You must provide email!"),
    reqparse.Argument('password', type=str, required=True, help="You must provide password!")
)


class UserListAPI(Resource):
    @require_login
    @marshal_with(users_fields)
    def get(self):
        return {'array': User.query.all()}

    @require_arguments(register_parameters)
    def post(self, params):
        user = User.query.filter((User.name == params.name) | (User.email == params.email)).first()
        if user:
            return create_error_message('Nickname or email are not unique!')
        if not is_email_valid(params.email):
            return create_error_message('Email is invalid!')
        if not _is_password_valid(params.password):
            return create_error_message('Password is invalid!')

        user = User.create(params.name, params.email, params.password)
        db.session.add(user)
        db.session.commit()

        user = User.query.filter_by(id=user.id).first()

        return marshal(user, user_detail), 201


class UserAPI(Resource):
    @require_login
    @marshal_with(user_detail)
    def get(self, id):
        user = get_object_or_404(User, id=id)
        return user


def _is_password_valid(password):
    return len(password) > 5
