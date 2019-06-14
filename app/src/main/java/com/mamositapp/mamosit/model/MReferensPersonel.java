package com.mamositapp.mamosit.model;

public class MReferensPersonel {
    private int puan,gecmistoplamkasa,kasa;

    public MReferensPersonel() {
    }

    public MReferensPersonel(int puan, int gecmistoplamkasa, int kasa) {
        this.puan = puan;
        this.gecmistoplamkasa = gecmistoplamkasa;
        this.kasa = kasa;
    }

    public int getPuan() {
        return puan;
    }

    public void setPuan(int puan) {
        this.puan = puan;
    }

    public int getGecmistoplamkasa() {
        return gecmistoplamkasa;
    }

    public void setGecmistoplamkasa(int gecmistoplamkasa) {
        this.gecmistoplamkasa = gecmistoplamkasa;
    }

    public int getKasa() {
        return kasa;
    }

    public void setKasa(int kasa) {
        this.kasa = kasa;
    }
}
