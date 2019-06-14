package com.mamositapp.mamosit.model;

public class MGorev {
    private String img,link,baslik,aciklama,mail;

    public MGorev() {
    }

    public MGorev(String img, String link, String baslik, String aciklama, String mail) {
        this.img = img;
        this.link = link;
        this.baslik = baslik;
        this.aciklama = aciklama;
        this.mail = mail;
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

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
