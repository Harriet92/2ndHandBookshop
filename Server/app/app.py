import logging
from flask import Flask
import sys

from .conf import config


def create_app():
    application = Flask(__name__)
    application.config.from_object(config)
    return application


def set_logging(application):
    debug = application.config['DEBUG']
    if not debug:
        stream_handler = logging.StreamHandler()
        application.logger.addHandler(stream_handler)
        application.logger.setLevel(logging.INFO)
    else:
        application.logger.setLevel(logging.DEBUG)


app = create_app()
set_logging(app)

from . import views

