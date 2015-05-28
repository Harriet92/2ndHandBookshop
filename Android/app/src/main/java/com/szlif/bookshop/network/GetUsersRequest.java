package com.szlif.bookshop.network;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.szlif.bookshop.models.User;

public class GetUsersRequest extends RetrofitSpiceRequest<User.List, Bookshop> {

    private String token;

    public GetUsersRequest(String token) {

        super(User.List.class, Bookshop.class);
        this.token = token;

    }

    @Override
    public User.List loadDataFromNetwork() throws Exception {

        return getService().GetUsers(token);

    }

    public String createCacheKey() {
        return "users";
    }
}
