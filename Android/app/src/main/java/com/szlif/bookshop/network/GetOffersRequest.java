package com.szlif.bookshop.network;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.szlif.bookshop.models.OfferDetail;
import com.szlif.bookshop.models.User;

public class GetOffersRequest extends RetrofitSpiceRequest<OfferDetail.List, Bookshop> {

    private String token;
    private Integer per_page;
    private Integer page;
    private String author;
    private String title;
    private Integer close;
    private Integer ownerId;
    private Integer purchaserId;
    private String tags;
    private Integer status;

    public GetOffersRequest(String token, Integer per_page, Integer page, String author, String title,
                            Integer close, Integer ownerId, Integer purchaserId, String tags, Integer status) {

        super(OfferDetail.List.class, Bookshop.class);
        this.token = token;
        
        this.per_page = per_page;
        this.page = page;
        this.author = author;
        this.title = title;
        this.close = close;
        this.ownerId = ownerId;
        this.purchaserId = purchaserId;
        this.tags = tags;
        this.status = status;
    }

    @Override
    public OfferDetail.List loadDataFromNetwork() throws Exception {

        return getService().GetOffers(token, per_page, page, author, title, close, ownerId, purchaserId, tags, status);

    }

    public String createCacheKey() {
        return "offers";
    }
}
