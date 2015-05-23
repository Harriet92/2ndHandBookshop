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

    public OfferDetail(String booktitle, String bookauthor, String description) {
        this.booktitle = booktitle;
        this.bookauthor = bookauthor;
        this.description = description;
    }

    public static class List extends Error {
        public ArrayList<OfferDetail> array;
    }

}
