package com.szlif.bookshop.network;

import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

public class BookshopService extends RetrofitGsonSpiceService {

    private final static String BASE_URL = "https://sheltered-forest-8633.herokuapp.com";

    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(Bookshop.class);
    }

    @Override
    protected String getServerUrl() {
        return BASE_URL;
    }



}
