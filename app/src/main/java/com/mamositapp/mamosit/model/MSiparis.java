package com.mamositapp.mamosit.model;

public class MSiparis {
    private String gonderilecekhesap,tarih,userid,usermail,username,kanitimg,odendigitarih;
    private int durum,oncekipuan,fiyat;

    public MSiparis() {
    }

    public MSiparis(String gonderilecekhesap, String tarih, String userid, String usermail, String username, String kanitimg, String odendigitarih, int durum, int oncekipuan, int fiyat) {
        this.gonderilecekhesap = gonderilecekhesap;
        this.tarih = tarih;
        this.userid = userid;
        this.usermail = usermail;
        this.username = username;
        this.kanitimg = kanitimg;
        this.odendigitarih = odendigitarih;
        this.durum = durum;
        this.oncekipuan = oncekipuan;
        this.fiyat = fiyat;
    }

    public int getFiyat() {
        return fiyat;
    }

    public void setFiyat(int fiyat) {
        this.fiyat = fiyat;
    }

    public String getKanitimg() {
        return kanitimg;
    }

    public void setKanitimg(String kanitimg) {
        this.kanitimg = kanitimg;
    }

    public String getOdendigitarih() {
        return odendigitarih;
    }

    public void setOdendigitarih(String odendigitarih) {
        this.odendigitarih = odendigitarih;
    }

    public String getGonderilecekhesap() {
        return gonderilecekhesap;
    }

    public void setGonderilecekhesap(String gonderilecekhesap) {
        this.gonderilecekhesap = gonderilecekhesap;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsermail() {
        return usermail;
    }

    public void setUsermail(String usermail) {
        this.usermail = usermail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getDurum() {
        return durum;
    }

    public void setDurum(int durum) {
        this.durum = durum;
    }

    public int getOncekipuan() {
        return oncekipuan;
    }

    public void setOncekipuan(int oncekipuan) {
        this.oncekipuan = oncekipuan;
    }
}
