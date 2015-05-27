import datetime
import math

from flask_restful import Resource, reqparse, marshal
from flask import g
from sqlalchemy import desc, or_

from ..resources.representations import offers_fields, offer_detail
from ..models import BookOffer, db
from ..common.log import Loggable
from ..common.reqparse import marshal_except_error, require_arguments
from ..common.utils import get_object_or_404, create_error_message
from ..common.login import require_login
from ..common.const import OfferStatus


new_offer_parameters = (
    reqparse.Argument('booktitle', type=str, required=True, help="You must provide book title!"),
    reqparse.Argument('bookauthor', type=str),
    reqparse.Argument('description', type=str),
    reqparse.Argument('photobase64', type=str),
    reqparse.Argument('price', type=int, required=True, help="You must provide book price!"),
    reqparse.Argument('tags', type=str),
    reqparse.Argument('longitude', type=float),
    reqparse.Argument('latitude', type=float)
)

offers_filter_parameters = (
    reqparse.Argument('offers_per_page', type=int, default=100),
    reqparse.Argument('page', type=int, default=0),
    reqparse.Argument('author', type=str),
    reqparse.Argument('title', type=str),
    reqparse.Argument('close', type=int),
    reqparse.Argument('owner_id', type=str),
    reqparse.Argument('purchaser_id', type=str),
    reqparse.Argument('tags', type=str),
    reqparse.Argument('longitude', type=float),
    reqparse.Argument('latitude', type=float),
    reqparse.Argument('status', type=int)
)

set_status_parameters = (
    reqparse.Argument('status', type=int, required=True, help="You must provide new status!"),
)


class BookOfferListAPI(Loggable, Resource):
    BOOK_EXPIRATION_TIME = 3600 * 24 * 7
    CLOSE_DISTANCE = 10

    @require_login
    @marshal_except_error(offers_fields)
    @require_arguments(offers_filter_parameters)
    def get(self, params):
        query = BookOffer.query
        not_null_filters = []
        if params.purchaser_id is not None:
            not_null_filters.append(BookOffer.purchaserid == params.purchaser_id)
        if params.owner_id is not None:
            not_null_filters.append(BookOffer.ownerid == params.owner_id)
        if params.title is not None:
            not_null_filters.append(BookOffer.booktitle.ilike(self.filter_to_query(params.title)))
        if params.author is not None:
            not_null_filters.append(BookOffer.bookauthor.ilike(self.filter_to_query(params.author)))

        if len(not_null_filters) > 0:
            query = query.filter(or_(*not_null_filters))

        if params.status:
            query = query.filter_by(status=params.status)

        if params.tags:
            query = query.filter(BookOffer.tags.ilike(self.filter_to_query(params.tags)))
        query = query.order_by(desc(BookOffer.created))

        if params.close == 1 and params.longitude is not None and params.latitude is not None:
            with_location = filter(
                lambda offer: offer.longitude is not None and offer.latitude is not None,
                query.all())
            close = filter(
                lambda offer: self._is_nearby(offer.longitude, params.longitude, offer.latitude, params.latitude),
                with_location)
            offset = params.offers_per_page * params.page
            array = close[offset: offset + params.offers_per_page]
        else:
            array = query.offset(params.offers_per_page * params.page).limit(params.offers_per_page).all()
        return {'array': array}

    @staticmethod
    def filter_to_query(filter):
        if '*' in filter or '_' in filter:
            return filter.replace('*', '%').replace('?', '_')
        else:
            return '%{0}%'.format(filter)

    @require_login
    @require_arguments(new_offer_parameters)
    def post(self, params):
        if not self._is_title_long_enough(params.booktitle):
            return create_error_message('Title is too short!')

        offer = BookOffer.create(
            title=params.booktitle,
            ownerid=g.user.id,
            author=params.bookauthor,
            description=params.description,
            tags=params.tags,
            photobase64=params.photobase64,
            price=params.price,
            expiration_time_in_sec=self.BOOK_EXPIRATION_TIME,
            latitude=params.latitude,
            longitude=params.longitude
        )
        db.session.add(offer)
        db.session.commit()

        offer = BookOffer.query.filter_by(id=offer.id).first()

        return marshal(offer, offer_detail), 201

    @staticmethod
    def _is_title_long_enough(string):
        return len(string) > 3

    def _is_nearby(self, long1, long2, lat1, lat2):
        r = 6371
        dlat = _to_radian(lat2 - lat1)
        dlon = _to_radian(long2 - long1)
        a = math.sin(dlat / 2) * math.sin(dlat / 2) + math.cos(_to_radian(lat1)) * math.cos(
            _to_radian(lat2)) * math.sin(dlon / 2) * math.sin(dlon / 2)
        c = 2 * math.asin(min(1, math.sqrt(a)))
        d = r * c
        return d < self.CLOSE_DISTANCE


def _to_radian(val):
    return (math.pi / 180) * val


class BookOfferAPI(Loggable, Resource):
    @require_login
    @marshal_except_error(offer_detail)
    def get(self, id):
        return get_object_or_404(BookOffer, id=id)

    @require_login
    @marshal_except_error(offer_detail)
    @require_arguments(set_status_parameters)
    def patch(self, id, params):
        offer = BookOffer.query.filter_by(id=id).first()
        if not offer:
            return create_error_message('Offer with provided id does not exist!')
        if offer.owner == g.user and params.status == OfferStatus.PURCHASE_REQUESTED:
            return create_error_message('Cannot purchase your own offer!')
        if params.status == OfferStatus.PURCHASE_REQUESTED and not self._is_offer_available(offer):
            return create_error_message('Offer has already been purchased')

        if params.status == OfferStatus.PURCHASE_REQUESTED:
            offer.purchaser = g.user
        elif params.status == OfferStatus.ADDED:
            offer.purchaser = None
        elif params.status == OfferStatus.FINALIZED:
            if offer.purchaser == None:
                return create_error_message("You must request purchase first!")

            if offer.purchaser.money >= offer.price:
                offer.purchaser.money -= offer.price
                offer.owner.money += offer.price
                offer.purchaser.bought += 1
                offer.owner.sold += 1
            else:
                return create_error_message("Not enough money!")

        offer.status = params.status
        db.session.commit()

        return offer

    @staticmethod
    def _is_offer_available(offer):
        return offer.status == OfferStatus.FINALIZED or (
            offer.status == OfferStatus.ADDED and (not offer.expiresat or offer.expiresat > datetime.datetime.now()))
