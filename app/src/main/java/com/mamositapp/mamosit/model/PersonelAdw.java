package com.mamositapp.mamosit.model;

public class PersonelAdw {
    private String img,link;
    private int sayac;

    public PersonelAdw() {
    }

    public PersonelAdw(String img, String link, int sayac) {
        this.img = img;
        this.link = link;
        this.sayac = sayac;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getSayac() {
        return sayac;
    }

    public void setSayac(int sayac) {
        this.sayac = sayac;
    }
}
