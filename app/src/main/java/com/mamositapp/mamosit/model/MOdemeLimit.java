package com.mamositapp.mamosit.model;

public class MOdemeLimit {
    private int banka, ininal,papara,kontor,bitcoin,googleplay;

    public MOdemeLimit() {
    }

    public int getBanka() {
        return banka;
    }

    public MOdemeLimit(int banka, int ininal, int papara, int kontor, int bitcoin, int googleplay) {
        this.banka = banka;
        this.ininal = ininal;
        this.papara = papara;
        this.kontor = kontor;
        this.bitcoin = bitcoin;
        this.googleplay = googleplay;
    }

    public void setBanka(int banka) {
        this.banka = banka;
    }

    public int getIninal() {
        return ininal;
    }

    public void setIninal(int ininal) {
        this.ininal = ininal;
    }

    public int getPapara() {
        return papara;
    }

    public void setPapara(int papara) {
        this.papara = papara;
    }

    public int getKontor() {
        return kontor;
    }

    public void setKontor(int kontor) {
        this.kontor = kontor;
    }

    public int getBitcoin() {
        return bitcoin;
    }

    public void setBitcoin(int bitcoin) {
        this.bitcoin = bitcoin;
    }

    public int getGoogleplay() {
        return googleplay;
    }

    public void setGoogleplay(int googleplay) {
        this.googleplay = googleplay;
    }
}
