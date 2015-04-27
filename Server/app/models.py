import datetime
import hashlib

from flask_sqlalchemy import SQLAlchemy

from app import app

db = SQLAlchemy(app)


class User(db.Model):
    __tablename__ = "users"
    
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(255), unique=True, nullable=False)
    email = db.Column(db.String(255), unique=True, nullable=False)
    token = db.Column(db.String(128))

    @classmethod
    def create(cls, name, email, password):
        token = cls._get_password_token(password)
        return User(token=token, name=name, email=email)

    def check_password(self, password):
        return self.token == self._get_password_token(password)

    @staticmethod
    def _get_password_token(password):
        return md5(password)

    def __unicode__(self):
        return '<User %r>' % self.name

    def __str__(self):
        return self.__unicode__()


class Book(db.Model):
    __tablename__ = "books"

    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(255))
    category_id = db.Column(db.Integer, db.ForeignKey('categories.id'), nullable=False)
    category = db.relationship(
        'Category',
        uselist=False,
        backref=db.backref('books', uselist=True)
    )

    author_id = db.Column(db.Integer, db.ForeignKey('authors.id'), nullable=False)
    author = db.relationship(
        'Author',
        uselist=False,
        backref=db.backref('books', uselist=True)
    )


class Author(db.Model):
    __tablename__ = "authors"

    id = db.Column(db.Integer, primary_key=True)
    full_name = db.Column(db.String(255))


class Category(db.Model):
    __tablename__ = "categories"

    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(50))

    def __repr__(self):
        return '<Category %r>' % self.name


class BookOffer(db.Model):
    __tablename__ = "offers"

    id = db.Column(db.Integer, primary_key=True)
    created = db.Column(db.DateTime, default=datetime.datetime.now)
    owner_id = db.Column(db.Integer, db.ForeignKey('users.id'), nullable=False)
    owner = db.relationship(
        'User',
        uselist=False,
        backref=db.backref('offers', uselist=True)
    )

    book_id = db.Column(db.Integer, db.ForeignKey('books.id'), nullable=False)
    book = db.relationship(
        'Book',
        uselist=False,
        backref=db.backref('offers', uselist=True)
    )


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


def md5(word):
    m = hashlib.md5()
    m.update(word)
    return m.hexdigest()



db.create_all()
db.session.commit()