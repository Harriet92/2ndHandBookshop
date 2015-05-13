import datetime
import hashlib

from flask_sqlalchemy import SQLAlchemy

from .app import app
from .common.const import OfferStatus

db = SQLAlchemy(app)


class User(db.Model):
    __tablename__ = "users"

    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(255), unique=True, nullable=False)
    email = db.Column(db.String(255), unique=True, nullable=False)
    token = db.Column(db.String(128), nullable=False)
    currency_count = db.Column(db.Integer, nullable=False)
    bought = db.Column(db.Integer, default=0, nullable=False)
    sold = db.Column(db.Integer, default=0, nullable=False)
    register_timestamp = db.Column(db.DateTime, nullable=False, default=datetime.datetime.utcnow)
    login_timestamp = db.Column(db.DateTime, nullable=False, default=datetime.datetime.utcnow)

    @classmethod
    def create(cls, name, email, password, currency_count=0):
        token = cls._get_password_token(password)
        return User(token=token, name=name, email=email, currency_count=currency_count)

    def check_password(self, password):
        return self.token == self._get_password_token(password)

    @staticmethod
    def _get_password_token(password):
        return md5(password)

    def __unicode__(self):
        return '<User %r>' % self.name

    def __str__(self):
        return self.__unicode__()


class BookOffer(db.Model):
    __tablename__ = "offers"

    id = db.Column(db.Integer, primary_key=True)
    created = db.Column(db.DateTime, default=datetime.datetime.now)
    expiresat = db.Column(db.DateTime)
    ownerid = db.Column(db.Integer, db.ForeignKey('users.id'), nullable=False)
    owner = db.relationship(
        'User',
        uselist=False,
        foreign_keys='BookOffer.ownerid',
        backref=db.backref('offers', uselist=True)
    )

    purchaserid = db.Column(db.Integer, db.ForeignKey('users.id'))
    purchaser = db.relationship(
        'User',
        uselist=False,
        foreign_keys='BookOffer.purchaserid',
        backref=db.backref('purchased', uselist=True)
    )

    booktitle = db.Column(db.String(100), nullable=False)
    bookauthor = db.Column(db.String(100))
    description = db.Column(db.String(255))
    price = db.Column(db.Integer, default=0, nullable=False)
    status = db.Column(db.Integer, default=OfferStatus.ADDED, nullable=False)
    photobase64 = db.Column(db.String)
    tags = db.Column(db.String)
    latitude = db.Column(db.String)
    longitude = db.Column(db.String)

    @classmethod
    def create(cls, title, author, ownerid, description, price, photobase64, tags, expiration_time_in_sec, longitude,
               latitude):
        expiration_date = datetime.datetime.utcnow() + datetime.timedelta(seconds=expiration_time_in_sec)
        return BookOffer(
            ownerid=ownerid, booktitle=title, bookauthor=author, description=description, price=price,
            photobase64=photobase64, tags=tags, expiresat=expiration_date, status=OfferStatus.ADDED,
            latitude=latitude, longitude=longitude)


class Session(db.Model):
    __tablename__ = "sessions"

    id = db.Column(db.Integer, primary_key=True)
    token = db.Column(db.String(128), nullable=False)
    expiration_date = db.Column(db.DateTime, nullable=False)
    user_id = db.Column(db.Integer, db.ForeignKey('users.id'), nullable=False)
    user = db.relationship(
        'User',
        uselist=False,
        backref=db.backref('session', uselist=False)
    )

    @staticmethod
    def create(user_id, session_expiration_time_in_sec):
        token = md5(str(user_id) + str(datetime.datetime.now()))
        expiration_date = datetime.datetime.utcnow() + datetime.timedelta(seconds=session_expiration_time_in_sec)
        return Session(token=token, expiration_date=expiration_date, user_id=user_id)

#
# class UserMessage(db.Model):
#     __tablename__ = "messages"
#
#     id = db.Column(db.Integer, primary_key=True)
#     createdat = db.Column(db.DateTime, default=datetime.datetime.now)
#     isread = db.Column(db.Boolean)
#     receiverid = db.Column(db.Integer, db.ForeignKey('users.id'), nullable=False)
#     receiver = db.relationship(
#         'User',
#         uselist=False,
#         foreign_keys='UserMessages.receiverid',
#         backref=db.backref('offers', uselistre=True)
#     )
#
#     senderid = db.Column(db.Integer, db.ForeignKey('users.id'))
#     sender = db.relationship(
#         'User',
#         uselist=False,
#         foreign_keys='BookOffer.senderid',
#         backref=db.backref('purchased', uselist=True)
#     )
#
#     email = db.Column(db.String(100))
#
#     @classmethod
#     def create(cls, createdat, isread, receiverid, senderid, email):
#         return UserMessage(createdat=createdat, isread=isread, receiverid=receiverid, senderid=senderid, email=email)


def md5(word):
    m = hashlib.md5()
    m.update(word + app.config.get('SALT', ''))
    return m.hexdigest()


db.create_all()
db.session.commit()