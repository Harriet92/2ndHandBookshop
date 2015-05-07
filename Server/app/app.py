import logging
from flask import Flask
import sys

from .conf import config


def create_app():
    application = Flask(__name__)
    application.config.from_object(config)
    application.logger.setLevel(logging.DEBUG if application.config['DEBUG'] else logging.INFO)
    return application


app = create_app()

from . import views

