package com.mamositapp.mamosit.model;

public class MTakimim {
    private String name;
    private int refverdigitoplampuan;

    public MTakimim() {
    }

    public MTakimim(String name, int puan) {
        this.name = name;
        this.refverdigitoplampuan = puan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRefverdigitoplampuan() {
        return refverdigitoplampuan;
    }

    public void setRefverdigitoplampuan(int refverdigitoplampuan) {
        this.refverdigitoplampuan = refverdigitoplampuan;
    }
}
