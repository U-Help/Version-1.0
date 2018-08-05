package com.example.fyg.login;


public class HelpObj {
    private String date;
    private String src;
    private String dst;

    public HelpObj(String date, String src, String dst) {
        this.date = date;
        this.src = src;
        this.dst = dst;
    }

    public String getDate() {
        return date;
    }

    public String getSrc() {
        return src;
    }

    public String getDst() {
        return dst;
    }
}

