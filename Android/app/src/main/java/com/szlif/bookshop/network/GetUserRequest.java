package com.szlif.bookshop.network;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.szlif.bookshop.models.User;

public class GetUserRequest extends RetrofitSpiceRequest<User, Bookshop> {

    private String token;
    private int userId;

    public GetUserRequest(String token, int userId) {

        super(User.class, Bookshop.class);
        this.userId = userId;
        this.token = token;

    }

    @Override
    public User loadDataFromNetwork() throws Exception {

        return getService().GetUser(token, userId);

    }
}
