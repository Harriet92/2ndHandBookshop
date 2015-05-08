package com.szlif.bookshop.requests;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;
import com.szlif.bookshop.R;
import com.szlif.bookshop.pojo.User;

public class GetUserRequest extends SpringAndroidSpiceRequest<User>{

    private String url = "/user/%d";

    private int userId;

    public GetUserRequest(int userId) {
        super(User.class);
        this.userId = userId;
        this.url = String.format(this.url, userId);
    }

    @Override
    public User loadDataFromNetwork() throws Exception {


        return getRestTemplate().getForObject("https://sheltered-forest-8633.herokuapp.com/users/2?token=d7f62520c6e65b4e240648cd74957719", User.class);

    }

    public String createCacheKey() {
        return String.format("user.%d", this.userId);
    }
}
