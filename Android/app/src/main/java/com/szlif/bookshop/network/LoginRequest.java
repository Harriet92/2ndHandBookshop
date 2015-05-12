package com.szlif.bookshop.network;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.szlif.bookshop.models.Session;
import com.szlif.bookshop.models.User;

public class LoginRequest extends RetrofitSpiceRequest<Session, Bookshop> {

    private String login;
    private String password;

    public LoginRequest(String nameOrEmail, String password) {

        super(Session.class, Bookshop.class);
        this.login = nameOrEmail;
        this.password = password;

    }

    @Override
    public Session loadDataFromNetwork() throws Exception {

        return getService().Login(login, password);

    }

    public String createCacheKey() {
        return String.format("session.%s", this.login);
    }
}
