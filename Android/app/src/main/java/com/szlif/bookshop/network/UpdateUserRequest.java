package com.szlif.bookshop.network;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.szlif.bookshop.models.User;

public class UpdateUserRequest extends RetrofitSpiceRequest<User, Bookshop> {

    private String token;
    private int money;
    private int userId;

    public UpdateUserRequest(String token, int userId, int money) {

        super(User.class, Bookshop.class);
        this.userId = userId;
        this.token = token;

        this.money = money;
    }

    @Override
    public User loadDataFromNetwork() throws Exception {

        return getService().UpdateUser(token, userId, money);

    }
}
