package com.mamositapp.mamosit.model;

public class MHesapFirebase {
    private String contactmail;
    private int referansbaslarkenkipuan;

    public MHesapFirebase() {
    }

    public MHesapFirebase(String contactmail, int referansbaslarkenkipuan) {
        this.contactmail = contactmail;
        this.referansbaslarkenkipuan = referansbaslarkenkipuan;
    }

    public String getContactmail() {
        return contactmail;
    }

    public void setContactmail(String contactmail) {
        this.contactmail = contactmail;
    }

    public int getReferansbaslarkenkipuan() {
        return referansbaslarkenkipuan;
    }

    public void setReferansbaslarkenkipuan(int referansbaslarkenkipuan) {
        this.referansbaslarkenkipuan = referansbaslarkenkipuan;
    }
}
