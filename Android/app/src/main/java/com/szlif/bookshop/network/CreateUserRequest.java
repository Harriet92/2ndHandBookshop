package com.szlif.bookshop.network;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.szlif.bookshop.models.User;

public class CreateUserRequest extends RetrofitSpiceRequest<User, Bookshop> {

    private String name;
    private String email;
    private String password;

    public CreateUserRequest(String name, String email, String password) {

        super(User.class, Bookshop.class);
        this.name = name;
        this.email = email;
        this.password = password;

    }

    @Override
    public User loadDataFromNetwork() throws Exception {

        return getService().CreateUser(name, password, email);

    }

}
