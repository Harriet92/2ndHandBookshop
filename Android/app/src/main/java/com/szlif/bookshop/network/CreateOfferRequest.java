package com.szlif.bookshop.network;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.szlif.bookshop.models.OfferDetail;
import com.szlif.bookshop.models.User;

public class CreateOfferRequest extends RetrofitSpiceRequest<OfferDetail, Bookshop> {

    private String token;
    private String author;
    private String description;
    private String photo;
    private String tags;
    private int status;
    private Float latitude;
    private Float longitude;
    private int price;
    private String bookTitle;

    public CreateOfferRequest(String token, String bookTitle, int price, String author, String description, String photo,
    String tags, int status, Float latitude, Float longitude) {

        super(OfferDetail.class, Bookshop.class);
        this.bookTitle = bookTitle;
        this.price = price;
        this.token = token;
        this.author = author;
        this.description = description;
        this.photo = photo;
        this.tags = tags;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public OfferDetail loadDataFromNetwork() throws Exception {

        return getService().CreateOffer(token, bookTitle, price, author, description, photo, tags, status, latitude, longitude);

    }

}
