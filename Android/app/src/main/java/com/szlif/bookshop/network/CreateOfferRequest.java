package com.szlif.bookshop.network;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.szlif.bookshop.models.OfferDetail;
import com.szlif.bookshop.models.User;

public class CreateOfferRequest extends RetrofitSpiceRequest<OfferDetail, Bookshop> {

    private String token;
    private int price;
    private String bookTitle;

    public CreateOfferRequest(String token, String bookTitle, int price) {

        super(OfferDetail.class, Bookshop.class);
        this.bookTitle = bookTitle;
        this.price = price;
        this.token = token;

    }

    @Override
    public OfferDetail loadDataFromNetwork() throws Exception {

        return getService().CreateOffer(token, bookTitle, price);

    }

}
