from flask import Flask

from conf.local import LocalConfig


def create_app():
    application = Flask(__name__)
    application.config.from_object(LocalConfig)
    return application


app = create_app()

from . import views

