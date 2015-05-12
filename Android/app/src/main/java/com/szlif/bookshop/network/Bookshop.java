package com.szlif.bookshop.network;

import com.szlif.bookshop.models.OfferDetail;
import com.szlif.bookshop.models.Session;
import com.szlif.bookshop.models.User;

import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PATCH;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.Field;

public interface Bookshop {

    @GET("/users/{id}")
    User GetUser(@Query("token") String token, @Path("id") int userId);

    @FormUrlEncoded
    @POST("/users")
    User CreateUser(@Field("name") String name, @Field("password") String password, @Field("email") String email);

    @GET("/users")
    User.List GetUsers(@Query("token") String token);

    @FormUrlEncoded
    @POST("/users/login")
    Session Login(@Field("login") String nameOrEmail, @Field("password") String password);

    @GET("/offers")
    OfferDetail.List GetOffers(@Query("token") String token);

    @FormUrlEncoded
    @POST("/offers")
    OfferDetail CreateOffer(@Query("token") String token, @Field("booktitle") String bookTitle, @Field("price") int price);

    @GET("/offers/{id}")
    OfferDetail GetOffer(@Query("token") String token, @Path("id") int offerId);

    @FormUrlEncoded
    @PATCH("/offers/{id}")
    OfferDetail UpdateOffer(@Query("token") String token, @Path("id") int offerId, @Field("status") int status);

}
