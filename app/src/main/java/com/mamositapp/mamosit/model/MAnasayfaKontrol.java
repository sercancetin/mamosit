package com.mamositapp.mamosit.model;

public class MAnasayfaKontrol {
    private String versiyon,instgram;
    private int bakimmodu, dolarpuan, puanverpuan, puanverzaman,tlpuan, gunicibirkereverilenpuan,yirmicevirmesonrasiodul;
    private float dolarpara,tlpara;

    public MAnasayfaKontrol() {
    }

    public MAnasayfaKontrol(String versiyon, String instgram, int bakimmodu, int dolarpuan, int puanverpuan, int puanverzaman, int tlpuan, int gunicibirkereverilenpuan, float dolarpara, float tlpara) {
        this.versiyon = versiyon;
        this.instgram = instgram;
        this.bakimmodu = bakimmodu;
        this.dolarpuan = dolarpuan;
        this.puanverpuan = puanverpuan;
        this.puanverzaman = puanverzaman;
        this.tlpuan = tlpuan;
        this.gunicibirkereverilenpuan = gunicibirkereverilenpuan;
        this.dolarpara = dolarpara;
        this.tlpara = tlpara;
    }

    public int getYirmicevirmesonrasiodul() {
        return yirmicevirmesonrasiodul;
    }

    public void setYirmicevirmesonrasiodul(int yirmicevirmesonrasiodul) {
        this.yirmicevirmesonrasiodul = yirmicevirmesonrasiodul;
    }

    public int getGunicibirkereverilenpuan() {
        return gunicibirkereverilenpuan;
    }

    public void setGunicibirkereverilenpuan(int gunicibirkereverilenpuan) {
        this.gunicibirkereverilenpuan = gunicibirkereverilenpuan;
    }

    public String getInstgram() {
        return instgram;
    }

    public void setInstgram(String instgram) {
        this.instgram = instgram;
    }

    public String getVersiyon() {
        return versiyon;
    }

    public void setVersiyon(String versiyon) {
        this.versiyon = versiyon;
    }

    public int getBakimmodu() {
        return bakimmodu;
    }

    public void setBakimmodu(int bakimmodu) {
        this.bakimmodu = bakimmodu;
    }

    public int getDolarpuan() {
        return dolarpuan;
    }

    public void setDolarpuan(int dolarpuan) {
        this.dolarpuan = dolarpuan;
    }

    public int getPuanverpuan() {
        return puanverpuan;
    }

    public void setPuanverpuan(int puanverpuan) {
        this.puanverpuan = puanverpuan;
    }

    public int getPuanverzaman() {
        return puanverzaman;
    }

    public void setPuanverzaman(int puanverzaman) {
        this.puanverzaman = puanverzaman;
    }

    public int getTlpuan() {
        return tlpuan;
    }

    public void setTlpuan(int tlpuan) {
        this.tlpuan = tlpuan;
    }

    public float getDolarpara() {
        return dolarpara;
    }

    public void setDolarpara(float dolarpara) {
        this.dolarpara = dolarpara;
    }

    public float getTlpara() {
        return tlpara;
    }

    public void setTlpara(float tlpara) {
        this.tlpara = tlpara;
    }
}
