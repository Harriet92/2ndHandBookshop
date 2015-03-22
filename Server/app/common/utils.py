
from flask_restful import abort


def get_object_or_404(cls, **kwargs):
    obj = cls.query.filter_by(**kwargs).first()
    if not obj:
        abort(404)

    return obj


def is_email_valid(email):
    return '@' in email

