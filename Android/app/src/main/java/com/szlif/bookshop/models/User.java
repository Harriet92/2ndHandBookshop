package com.szlif.bookshop.models;

import java.util.ArrayList;

public class User extends Error {

    public String name;
    public String url;
    public String email;
    public int id;
    public int sold;
    public int bought;
    public int money;

    public static class List extends Error {
        public ArrayList<User> array;
    }
}