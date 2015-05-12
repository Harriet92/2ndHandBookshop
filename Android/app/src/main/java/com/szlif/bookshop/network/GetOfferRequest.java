package com.szlif.bookshop.network;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.szlif.bookshop.models.OfferDetail;
import com.szlif.bookshop.models.User;

public class GetOfferRequest extends RetrofitSpiceRequest<OfferDetail, Bookshop> {

    private String token;
    private int offerId;

    public GetOfferRequest(String token, int offerId) {

        super(OfferDetail.class, Bookshop.class);
        this.offerId = offerId;
        this.token = token;

    }

    @Override
    public OfferDetail loadDataFromNetwork() throws Exception {

        return getService().GetOffer(token, offerId);

    }

    public String createCacheKey() {
        return String.format("offer.%d", this.offerId);
    }
}
