from functools import wraps

from flask_restful import reqparse, marshal


def marshal_except_error(fields):
    def decorator(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            result = func(*args, **kwargs)
            if isinstance(result, dict) and result.get('error'):
                return result
            return marshal(result, fields)
        return wrapper
    return decorator


def require_arguments(request_parser):
    """
    wrapper for function requiring parameters
    :param request_parser: Request parser or iterable with reqparse.Arguments
    :return: wrapped function
    """
    if not isinstance(request_parser, reqparse.RequestParser):
        request_parser = _create_parser(request_parser)

    def decorator(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            arguments = request_parser.parse_args()
            return func(params=arguments, *args, **kwargs)
        return wrapper
    return decorator


def _create_parser(data):
    parser = reqparse.RequestParser()
    for v in data:
        parser.add_argument(v)
    return parser
