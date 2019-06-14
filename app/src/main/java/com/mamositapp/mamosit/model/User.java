package com.mamositapp.mamosit.model;

public class User {
    private String kimgetirdi,mail,name,songiris,user_name, adres,status,google_id;
    private int puan,ban, refverdigitoplampuan, useretkinlik,kasa,gecmistoplamkasa;

    public User() {
    }

    public User(String kimgetirdi, String mail, String name, String songiris,
                String user_name, String adres, String status, String google_id,
                int puan, int ban, int refverdigitoplampuan, int useretkinlik,
                int kasa, int gecmistoplamkasa) {
        this.kimgetirdi = kimgetirdi;
        this.mail = mail;
        this.name = name;
        this.songiris = songiris;
        this.user_name = user_name;
        this.adres = adres;
        this.status = status;
        this.google_id = google_id;
        this.puan = puan;
        this.ban = ban;
        this.refverdigitoplampuan = refverdigitoplampuan;
        this.useretkinlik = useretkinlik;
        this.kasa = kasa;
        this.gecmistoplamkasa = gecmistoplamkasa;
    }

    public int getGecmistoplamkasa() {
        return gecmistoplamkasa;
    }

    public void setGecmistoplamkasa(int gecmistoplamkasa) {
        this.gecmistoplamkasa = gecmistoplamkasa;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGoogle_id() {
        return google_id;
    }

    public void setGoogle_id(String google_id) {
        this.google_id = google_id;
    }

    public String getOnline() {
        return status;
    }

    public int getKasa() {
        return kasa;
    }

    public void setKasa(int kasa) {
        this.kasa = kasa;
    }

    public void setOnline(String status) {
        this.status = status;
    }

    public int getUseretkinlik() {
        return useretkinlik;
    }

    public void setUseretkinlik(int useretkinlik) {
        this.useretkinlik = useretkinlik;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public int getRefverdigitoplampuan() {
        return refverdigitoplampuan;
    }

    public void setRefverdigitoplampuan(int refverdigitoplampuan) {
        this.refverdigitoplampuan = refverdigitoplampuan;
    }


    public String getKimgetirdi() {
        return kimgetirdi;
    }

    public void setKimgetirdi(String kimgetirdi) {
        this.kimgetirdi = kimgetirdi;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSongiris() {
        return songiris;
    }

    public void setSongiris(String songiris) {
        this.songiris = songiris;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getPuan() {
        return puan;
    }

    public void setPuan(int puan) {
        this.puan = puan;
    }

    public int getBan() {
        return ban;
    }

    public void setBan(int ban) {
        this.ban = ban;
    }
}
