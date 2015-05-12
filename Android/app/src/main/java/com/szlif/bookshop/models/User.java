package com.szlif.bookshop.models;

import java.util.ArrayList;

public class User {

    public String name;
    public String url;
    public String email;
    public int money;

    public static class List extends ArrayList<User> {}

}