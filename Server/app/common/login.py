import datetime

import flask
from flask import g

from ..conf import config
from ..common.utils import create_error_message
from ..models import Session, User
from ..resources.session import refresh_session


def require_login(func):
    def wrapper(*args, **kwargs):
        token = flask.request.args.get('token') or flask.request.form['token']
        if not token:
            return create_error_message('You must provide token parameter.')

        session = Session.query.filter_by(token=token).first()
        if not session and config.DEBUG:
            user = User.query.order_by('id').limit(1).first()
            if not user:
                return create_error_message('There is no users in database!')
            session = Session.create(user.id, 1)

        if not session or session.expiration_date < datetime.datetime.utcnow():
            return create_error_message('Invalid token')

        refresh_session(session)
        g.user = User.query.filter_by(id=session.user_id).first()
        return func(*args, **kwargs)
    return wrapper
