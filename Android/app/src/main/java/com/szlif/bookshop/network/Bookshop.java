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
    @PATCH("/users/{id}")
    User UpdateUser(@Query("token") String token, @Path("id") int userId, @Field("money") int money);

    @FormUrlEncoded
    @POST("/users")
    User CreateUser(@Field("name") String name, @Field("password") String password, @Field("email") String email);

    @GET("/users")
    User.List GetUsers(@Query("token") String token);

    @FormUrlEncoded
    @POST("/users/login")
    Session Login(@Field("login") String nameOrEmail, @Field("password") String password);

    @GET("/offers")
    OfferDetail.List GetOffers(@Query("token") String token, @Query("offers_per_page") Integer per_page,
                               @Query("page") Integer page, @Query("author") String author, @Query("title") String title,
                               @Query("close") Integer close, @Query("owner_id") Integer ownerId, @Query("purchaser_id") Integer purchaserId,
                               @Query("tags") String tags, @Query("status") Integer status, @Query("latitude") Float latitude,
                               @Query("longitude") Float longitude);

    @FormUrlEncoded
    @POST("/offers")
    OfferDetail CreateOffer(@Query("token") String token, @Field("booktitle") String bookTitle,
                            @Field("price") int price, @Field("bookauthor") String author, @Field("description") String description, @Field("photobase64") String photo,
                            @Field("tags") String tags, @Field("status") int status, @Field("latitude") Float latitude,
                            @Field("longitude") Float longitude);

    @GET("/offers/{id}")
    OfferDetail GetOffer(@Query("token") String token, @Path("id") int offerId);

    @FormUrlEncoded
    @PATCH("/offers/{id}")
    OfferDetail UpdateOffer(@Query("token") String token, @Path("id") int offerId, @Field("status") int status);

}
