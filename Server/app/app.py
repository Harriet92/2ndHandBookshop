from flask import Flask

from .conf import config


def create_app():
    application = Flask(__name__)
    application.config.from_object(config)
    return application


app = create_app()

from . import views

