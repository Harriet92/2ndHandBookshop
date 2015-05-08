package com.szlif.bookshop.pojo;


public class OfferDetail {
    private String url;
    private int id;
    private int ownerid;
    private int purchaserid;
    private String created;
    private String expiresat;
    private String booktitle;
    private String bookauthor;
    private String description;
    private String tags;
    private String photobase64;
    private int price;
    private int status;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(int ownerid) {
        this.ownerid = ownerid;
    }

    public int getPurchaserid() {
        return purchaserid;
    }

    public void setPurchaserid(int purchaserid) {
        this.purchaserid = purchaserid;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getExpiresat() {
        return expiresat;
    }

    public void setExpiresat(String expiresat) {
        this.expiresat = expiresat;
    }

    public String getBooktitle() {
        return booktitle;
    }

    public void setBooktitle(String booktitle) {
        this.booktitle = booktitle;
    }

    public String getBookauthor() {
        return bookauthor;
    }

    public void setBookauthor(String bookauthor) {
        this.bookauthor = bookauthor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getPhotobase64() {
        return photobase64;
    }

    public void setPhotobase64(String photobase64) {
        this.photobase64 = photobase64;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
