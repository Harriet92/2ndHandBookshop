package com.szlif.bookshop.network;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.szlif.bookshop.models.OfferDetail;
import com.szlif.bookshop.models.User;

public class UpdateOfferRequest extends RetrofitSpiceRequest<OfferDetail, Bookshop> {

    private String token;
    private int status;
    private int offerId;

    public UpdateOfferRequest(String token, int offerId, int status) {

        super(OfferDetail.class, Bookshop.class);
        this.offerId = offerId;
        this.token = token;
        this.status = status;

    }

    @Override
    public OfferDetail loadDataFromNetwork() throws Exception {

        return getService().UpdateOffer(token, offerId, status);

    }

    public String createCacheKey() {
        return String.format("offer.%d", this.offerId);
    }
}
