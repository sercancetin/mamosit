package com.mamositapp.mamosit.model;

public class MHavuzBilgi {
    private String date,link;
    private int puandegeri,gosterim;

    public MHavuzBilgi() {
    }

    public MHavuzBilgi(int puandegeri, String date, int gosterim) {
        this.puandegeri = puandegeri;
        this.date = date;
        this.gosterim = gosterim;
    }

    public int getPuandegeri() {
        return puandegeri;
    }

    public void setPuandegeri(int puandegeri) {
        this.puandegeri = puandegeri;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getGosterim() {
        return gosterim;
    }

    public void setGosterim(int gosterim) {
        this.gosterim = gosterim;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
