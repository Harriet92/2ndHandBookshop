import datetime

import flask
from flask import g

from ..common.utils import create_error_message
from ..models import Session
from ..resources.session import refresh_session


def require_login(func):
    def wrapper(*args, **kwargs):
        token = flask.request.args.get('token')
        if not token:
            return create_error_message('You must provide token parameter.')

        session = Session.query.filter_by(token=token).first()
        if not session or session.expiration_date < datetime.datetime.utcnow():
            return create_error_message('Invalid token')

        refresh_session(session)
        g.user = session.user
        return func(*args, **kwargs)
    return wrapper
