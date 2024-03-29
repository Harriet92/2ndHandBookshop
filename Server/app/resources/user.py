from flask_restful import Resource, reqparse, marshal

from flask import g
from .representations import users_fields, user_detail

from ..models import User, db
from ..common.utils import get_object_or_404, is_email_valid, create_error_message
from ..common.reqparse import require_arguments, marshal_except_error
from ..common.log import Loggable
from ..common.login import require_login

register_parameters = (
    reqparse.Argument('name', type=str, required=True, help="You must provide name!"),
    reqparse.Argument('email', type=str, required=True, help="You must provide email!"),
    reqparse.Argument('password', type=str, required=True, help="You must provide password!")
)

patch_user_parameters = (
    reqparse.Argument('money', type=int),
)


class UserListAPI(Loggable, Resource):
    @require_login
    @marshal_except_error(users_fields)
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


class UserAPI(Loggable, Resource):
    @require_login
    @marshal_except_error(user_detail)
    def get(self, id):
        user = get_object_or_404(User, id=id)
        return user

    @require_login
    @require_arguments(patch_user_parameters)
    @marshal_except_error(user_detail)
    def patch(self, id, params):
        user = get_object_or_404(User, id=id)
        if user != g.user:
            return create_error_message("You can't change other user data!")

        if params.money:
            user.money = params.money
            db.session.commit()

        user = get_object_or_404(User, id=id)
        return user


def _is_password_valid(password):
    return len(password) > 5
