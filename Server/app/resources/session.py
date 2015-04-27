import datetime
from flask_restful import Resource, reqparse, fields, marshal

from ..common.reqparse import require_arguments
from ..common.utils import create_error_message
from ..models import Session, User, db

session_data = {
    'expiration_date': fields.DateTime,
    'user_id': fields.Integer,
    'token': fields.String
}

login_parameters = (
    reqparse.Argument('login', type=str, required=True, help="You must provide login!"),
    reqparse.Argument('password', type=str, required=True, help="You must provide password!")
)

SESSION_TIME_IN_SECONDS = 3600


def refresh_session(session):
    session.expiration_date = datetime.datetime.now() + datetime.timedelta(seconds=SESSION_TIME_IN_SECONDS)
    db.session.commit()


class SessionAPI(Resource):
    @require_arguments(login_parameters)
    def post(self, params):
        user = User.query.filter((User.name == params.login) | (User.email == params.login)).first()
        if not user:
            return create_error_message('User with name or email {0} not found!'.format(params.login))
        if not user.check_password(params.password):
            return create_error_message('Password not match!')

        if user.session:
            refresh_session(user.session)
            return marshal(user.session, session_data)
        else:
            session = Session.create(user.id, SESSION_TIME_IN_SECONDS)
            db.session.add(session)
            db.session.commit()
            return marshal(session, session_data), 201

