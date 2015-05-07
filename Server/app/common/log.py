from ..app import app


class Loggable(object):

    def __init__(self):
        self.prefix = str(self.__class__)

    def log_info(self, message):
        app.logger.info(self.prefix + ' ' + message)

    def log_debug(self, message):
        app.logger.debug(self.prefix + ' ' + message)

    def log_warning(self, message):
        app.logger.warning(self.prefix + ' ' + message)

    def log_error(self, message):
        app.logger.error(self.prefix + ' ' + message)