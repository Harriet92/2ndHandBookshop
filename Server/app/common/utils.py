
from flask_restful import abort


def get_object_or_404(cls, **kwargs):
    obj = cls.query.filter_by(**kwargs).first()
    if not obj:
        return create_error_message("Object not found")

    return obj


def create_error_message(message):
    return {'error': message}


def is_email_valid(email):
    return '@' in email


def enum(**enums):
    return type('Enum', (), enums)

