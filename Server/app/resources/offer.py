from flask_restful import Resource, fields, reqparse, marshal
from flask import g
import datetime
from ..models import BookOffer, db
from ..common.reqparse import marshal_except_error, require_arguments
from ..common.utils import get_object_or_404, create_error_message
from ..common.login import require_login
from ..common.enums import OfferStatus

offer_detail = {
    'url': fields.Url('books'),
    'id': fields.Integer,
    'ownerid': fields.Integer,
    'purchaserid': fields.Integer,
    'created': fields.DateTime,
    'expiresat': fields.DateTime,
    'booktitile': fields.String,
    'bookauthor': fields.String,
    'description': fields.String,
    'tags': fields.String,
    'photobase64': fields.String,
    'price': fields.Integer,
    'status': fields.Integer
}

offers_fields = {
    'array': fields.Nested(offer_detail)
}

new_offer_parameters = (
    reqparse.Argument('booktitle', type=str, required=True, help="You must provide book title!"),
    reqparse.Argument('ownerid', type=str, required=True, help="You must provide id of the owner!"),
    reqparse.Argument('title', type=str),
    reqparse.Argument('expiresat', type=str),
    reqparse.Argument('bookauthor', type=str),
    reqparse.Argument('description', type=str),
    reqparse.Argument('photobase64', type=str),
    reqparse.Argument('price', type=str),
    reqparse.Argument('tags', type=str),
)

set_status_parameters = (
    reqparse.Argument('offerid', type=int, required=True, help="You must provide offer id!"),
    reqparse.Argument('status', type=int, required=True, help="You must provide new status!"),
    reqparse.Argument('purchaserid', type=str, required=False)
)


class BookOfferListAPI(Resource):
    #@require_login
    @marshal_except_error(offers_fields)
    def get(self):
        print(BookOffer.query.all())
        return {'array': BookOffer.query.all()}

    #@require_login
    @require_arguments(new_offer_parameters)
    def post(self, params):
        if not _is_string_long_enough(params.booktitle):
            return create_error_message('Title is too short!')

        offer = BookOffer.create(
            title=params.booktitle,
            ownerid=params.ownerid,
            expiresat=params.expiresat,
            author=params.bookauthor,
            description=params.description,
            tags=params.tags,
            photobase64=params.photobase64,
            price=params.price
        )
        db.session.add(offer)
        db.session.commit()

        offer = BookOffer.query.filter_by(id=offer.id).first()

        return marshal(offer, offer_detail), 201


class BookOfferAPI(Resource):
    #@require_login
    @marshal_except_error(offer_detail)
    def get(self, id):
        return get_object_or_404(BookOffer, id=id)


# Used for:
# requesting purchase offer - requires providing id of the purchaser,
# invoked by the purchaser;
# setting offer as finalized - invoked by the owner after NFC payment confirmation;
#  setting offer as cancelled - invoked by the owner

class SetOfferStatus(Resource):
    #@require_login
    @require_arguments(set_status_parameters)
    def post(self, params):
        if params.status == OfferStatus.PURCHASE_REQUESTED and (not params.purchaserid or len(params.purchaserid) == 0):
            return create_error_message('Purchase request must include id of the purchaser!')

        offer = BookOffer.query.filter_by(id=params.offerid).first()
        if not offer:
            return create_error_message('Offer with provided id does not exist!')
        if offer.ownerid == params.purchaserid:
            return create_error_message('Cannot purchase your own offer!')
        if params.status == OfferStatus.PURCHASE_REQUESTED and not _is_offer_available(offer):
            return create_error_message('Offer has already been purchased')
        #if params.status != OfferStatus.PURCHASE_REQUESTED and g.user.id != offer.ownerid:
        #    return create_error_message('Cannot modify someone else s offer!')

        if params.status == OfferStatus.PURCHASE_REQUESTED:
            result = db.session.query(BookOffer).filter(BookOffer.id == params.offerid). \
                update({'status': params.status, 'purchaserid': params.purchaserid})
        else:
            result = db.session.query(BookOffer).filter(BookOffer.id == params.offerid). \
                update({'status': params.status})
        db.session.commit()

        return result == 1


def _is_string_long_enough(string):
    return len(string) > 3


def _is_offer_available(offer):
    return offer.status == OfferStatus.ADDED and (not offer.expiresat or offer.expiresat > datetime.datetime.now())