from functools import wraps

from flask_restful import reqparse, marshal

from ..app import app


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


def log_requested_arguments(request_parser):
    debug_str = 'requested arguments:'
    for arg in request_parser.args:
        debug_str += '\n\tname "{0}", type {1}'.format(arg.name, str(arg.type))
    app.logger.debug(debug_str)


def log_received_arguments(args):
    debug_str = 'received arguments:'
    for arg in args.iteritems():
        debug_str += '\n\tname "{0}", value {1}'.format(arg[0], arg[1])
    app.logger.debug(debug_str)


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
            log_requested_arguments(request_parser)
            arguments = request_parser.parse_args()
            log_received_arguments(arguments)
            return func(params=arguments, *args, **kwargs)
        return wrapper
    return decorator


def _create_parser(data):
    parser = reqparse.RequestParser()
    for v in data:
        parser.add_argument(v)
    return parser
