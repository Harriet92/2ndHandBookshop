package com.szlif.bookshop.models;


import java.util.ArrayList;

public class OfferDetail extends Error {

    public int id;
    public int ownerid;
    public int purchaserid;
    public String created;
    public String expiresat;
    public String booktitle;
    public String bookauthor;
    public String description;
    public String tags;
    public String photobase64;
    public int price;
    public int status;

    public static class List extends ArrayList<OfferDetail> {}

}
