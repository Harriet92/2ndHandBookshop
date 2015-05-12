package com.szlif.bookshop.network;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.szlif.bookshop.models.OfferDetail;
import com.szlif.bookshop.models.User;

public class GetOffersRequest extends RetrofitSpiceRequest<OfferDetail.List, Bookshop> {

    private String token;

    public GetOffersRequest(String token) {

        super(OfferDetail.List.class, Bookshop.class);
        this.token = token;

    }

    @Override
    public OfferDetail.List loadDataFromNetwork() throws Exception {

        return getService().GetOffers(token);

    }

    public String createCacheKey() {
        return "offers";
    }
}
