import os


config = None

if 'DYNO' in os.environ:
    from .production import ProductionConfig
    config = ProductionConfig
else:
    from.local import LocalConfig
    config = LocalConfig