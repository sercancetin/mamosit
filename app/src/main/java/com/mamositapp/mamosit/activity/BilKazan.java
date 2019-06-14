package com.mamositapp.mamosit.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mamositapp.mamosit.R;
import com.mamositapp.mamosit.model.MSoru;
import com.mamositapp.mamosit.model.PersonelAdw;
import com.mamositapp.mamosit.model.User;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class BilKazan extends AppCompatActivity implements RewardedVideoAdListener {
    @BindView(R.id.txtsorubilsahipolancoin)
    TextView _txtsorubilsahipolancoin;
    @BindView(R.id.txtsorubilpara)
    TextView _txtsorubilpara;
    @BindView(R.id.toplanan)
    TextView _toplanan;
    @BindView(R.id.toplamsoru)
    TextView toplamsoru;
    @BindView(R.id.btnsorubila)
    Button _btnsorubila;
    @BindView(R.id.btnsorubilb)
    Button _btnsorubilb;
    @BindView(R.id.btnsorubilc)
    Button _btnsorubilc;
    @BindView(R.id.sorualani)
            TextView _sorualani;
    @BindView(R.id.imgsorubilimg)
    ImageView _imgsorubilimg;
    @BindView(R.id.coinkazanc)
            TextView coinkazanc;
    @BindView(R.id.lytbutons)
    LinearLayout _lytbutons;
    @BindView(R.id.imgsorubilpersonelad)
            ImageView _imgsorubilpersonelad;
    Dialog dialog;
    //database soru çek
    Random rnd;
    String firebasegelencevap,referansinidsi;
    int dataTableKolayCount=10;
    //database soru bitiş
    Context context=this;
    String gelenid;
    float[] gelenParaDizi;
    boolean gelendilturkecemi;
    int userPuan;
    //soru için
    String secilenButon;
    MediaPlayer dogrumusic;
    MediaPlayer yanlismusic;
    MediaPlayer tick;
    MediaPlayer alttick;
    boolean butonsecildimi;
    int gelinenToplamSoru;
    Dialog dialogYanlisSoru;
    int toplananCoinSayi;
    //yanlış dialog için
    Button anaekrandon;
    TextView toplanancoin, dogrusoru;
    //yanlış dialog için bitiş
    //soru için bitiş
    //reklam başlangıç
    private RewardedVideoAd rewardedVideoAd;
    private InterstitialAd mInterstitialAd;
    //kisisel reklam
    private int reklamsayac,referansininpuani,referansinakazandirdigitoplampuan,yuzdeonu;
    private String reklamLink,reklamImg;
    //kisiselreklam bitiş
    AdView mAdView;
    //reklam bitiş
    int odulalindimi = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soru_bil_kazan);
        ButterKnife.bind(this);
        gelenid=getIntent().getStringExtra(getResources().getString(R.string.userid));
        gelendilturkecemi = getIntent().getBooleanExtra(getResources().getString(R.string.dil),true);
        gelenParaDizi = getIntent().getFloatArrayExtra(getResources().getString(R.string.paracevirdizi));
        referansinidsi = getIntent().getStringExtra(getResources().getString(R.string.referansinidsi));
        dataTabloCountKolay();
        profilcek();
        soruCek(dataTableKolayCount);
        reklamTanimlama();
        referansPuanCek();
        //sesler
        dogrumusic = MediaPlayer.create(this,R.raw.dogru);
        yanlismusic = MediaPlayer.create(this,R.raw.yanlis);
        tick = MediaPlayer.create(this,R.raw.tick);
        alttick = MediaPlayer.create(context,R.raw.alttick);
        //sesler bitiş
        kisiselReklamCek();
    }
    //<<<<<<<<<<<<<<<<  Tıklamalar Başlangıç >>>>>>>>>>>>>>>>>>>>>>>
    @OnClick(R.id.btnsorubila) void btnsorubilaClick(){
        secim(_btnsorubila,_btnsorubilb,_btnsorubilc,"a");
        secildiktenSonraZamanBaslat(firebasegelencevap);
    }
    @OnClick(R.id.btnsorubilb) void btnsorubilbClick(){
        secim(_btnsorubilb,_btnsorubila,_btnsorubilc,"b");
        secildiktenSonraZamanBaslat(firebasegelencevap);
    }
    @OnClick(R.id.btnsorubilc) void btnsorubilcClick(){
        secim(_btnsorubilc,_btnsorubilb,_btnsorubila,"c");
        secildiktenSonraZamanBaslat(firebasegelencevap);
    }
    @OnClick(R.id.imgsorubilpersonelad) void imgcevirkazanpersoneladClick(){
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(reklamLink)));
            kisiselRekamSayacArttir();
        }catch (ActivityNotFoundException e){
            //Toast.makeText(this,"Link hazır değil.",Toast.LENGTH_SHORT).show();
        }
    }
    //<<<<<<<<<<<<<<<<  Tıklamalar Bitiş >>>>>>>>>>>>>>>>>>>>>>>
    void secim(Button xyz,Button xyc,Button xya, String secilen){
        xyz.setBackgroundResource(R.drawable.answerbgchoice);
        xyc.setBackgroundResource(R.drawable.answerbg);
        xya.setBackgroundResource(R.drawable.answerbg);
        secilenButon=secilen;
        tick.start();
    }
    void dogruOlan(final Button abc){
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(10); //You can manage the blinking time with this parameter
        anim.setStartOffset(10);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.RESTART);
        abc.startAnimation(anim);
        abc.setBackgroundResource(R.drawable.answerbggreen);
        secilenButon = "-";
    }
    void yanlisOlan(final Button abc){
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(20); //You can manage the blinking time with this parameter
        anim.setStartOffset(30);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.RESTART);
        abc.startAnimation(anim);
        abc.setBackgroundResource(R.drawable.answerbgred);
        yanlisSoruDialog(0);
    }
    void dogruyuBul(String dogru, String _secilenButon){
        if (dogru.equals(_secilenButon)){
            switch (_secilenButon){
                case "a": dogruOlan(_btnsorubila); yeniSoruZamanDialog(); break;
                case "b": dogruOlan(_btnsorubilb); yeniSoruZamanDialog(); break;
                case "c": dogruOlan(_btnsorubilc); yeniSoruZamanDialog(); break;
            }
            dogrumusic.start();
        } else {
            switch (_secilenButon){
                case "a": yanlisOlan(_btnsorubila); break;
                case "b": yanlisOlan(_btnsorubilb); break;
                case "c": yanlisOlan(_btnsorubilc); break;
                default: Toast.makeText(this,getResources().getString(R.string.bilkazansiksecilmedi),Toast.LENGTH_SHORT).show();
            }
            switch (dogru){
                case "a": dogruOlan(_btnsorubila); break;
                case "b": dogruOlan(_btnsorubilb); break;
                case "c": dogruOlan(_btnsorubilc); break;
            }
            yanlismusic.start();

        }
    }
    private void yeniSoruZamanDialog(){
        toplananCoin(_toplanan,coinkazanc);
        _3saniyesonrasorucek();
    }
    private void _3saniyesonrasorucek(){
        new CountDownTimer(10,1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                gelinenToplamSoru = Integer.parseInt(toplamsoru.getText().toString())+1;
                if (gelinenToplamSoru>15){
                    dialog.dismiss();
                    yanlisSoruDialog(1);
                } else if(gelinenToplamSoru == 10){
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                        mInterstitialAd.setAdListener(new AdListener(){
                            @Override
                            public void onAdClosed() {
                                toplamsoru.setText(String.valueOf(gelinenToplamSoru));
                                sifirla();
                                soruCek(dataTableKolayCount);
                                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                            }
                        });
                    } else {
                        toplamsoru.setText(String.valueOf(gelinenToplamSoru));
                        sifirla();
                        soruCek(dataTableKolayCount);
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }
                } else if(gelinenToplamSoru == 5){
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                        mInterstitialAd.setAdListener(new AdListener(){
                            @Override
                            public void onAdClosed() {
                                toplamsoru.setText(String.valueOf(gelinenToplamSoru));
                                sifirla();
                                soruCek(dataTableKolayCount);
                                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                            }
                        });
                    } else {
                        toplamsoru.setText(String.valueOf(gelinenToplamSoru));
                        sifirla();
                        soruCek(dataTableKolayCount);
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }
                } else {
                    toplamsoru.setText(String.valueOf(gelinenToplamSoru));
                    sifirla();
                    soruCek(dataTableKolayCount);
                }
            }
        }.start();
    }
    void toplananCoin(TextView tx1, TextView tx2){
        toplananCoinSayi = Integer.parseInt(tx1.getText().toString())+ Integer.parseInt(tx2.getText().toString());
        tx1.setText(String.valueOf(toplananCoinSayi));
    }
    void puanYaz(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(gelenid).child(getResources().getString(R.string.puan));
                reference.setValue(userPuan + toplananCoinSayi);
        referansinaPuanVer(toplananCoinSayi);
    }
    private void yanlisSoruDialog(int a){
        dialogYanlisSoru = new Dialog(context);
        dialogYanlisSoru.setContentView(R.layout.yanlissorudialog);
        anaekrandon = dialogYanlisSoru.findViewById(R.id.anaekran);
        toplanancoin = dialogYanlisSoru.findViewById(R.id.toplanancoin);
        dogrusoru = dialogYanlisSoru.findViewById(R.id.dogrusoru);
        dialogYanlisSoru.setCancelable(true);
        dogrusoru.setText(toplamsoru.getText().toString());
        toplanancoin.setText(_toplanan.getText().toString());
        Objects.requireNonNull(dialogYanlisSoru.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogYanlisSoru.show();
        puanYaz();
        _lytbutons.setVisibility(View.INVISIBLE);
        if (a==0){
            anaekrandon.setVisibility(View.INVISIBLE);
        }
        anaekrandon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rewardedVideoAd.isLoaded()) {
                    rewardedVideoAd.show();
                } else {
                    Toast.makeText(BilKazan.this,getResources().getString(R.string.sonratekrardene),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void secildiktenSonraZamanBaslat(final String dogru){
        new CountDownTimer(100, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                //here you can have your logic to set text to edittext
            }
            public void onFinish() {
                dogruyuBul(dogru,secilenButon);
            }
        }.start();
    }
    private void sifirla(){
        _btnsorubila.setBackgroundResource(R.drawable.answerbg);
        _btnsorubilb.setBackgroundResource(R.drawable.answerbg);
        _btnsorubilc.setBackgroundResource(R.drawable.answerbg);
        _btnsorubila.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        _btnsorubilb.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        _btnsorubilc.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }
    private void profilcek(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(gelenid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            User user;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (user!=null){
                    userPuan = user.getPuan();
                    referansinakazandirdigitoplampuan = user.getRefverdigitoplampuan();
                    _txtsorubilsahipolancoin.setText(String.valueOf(userPuan));
                    if (userPuan>100){
                        _txtsorubilpara.setText(puanParaCevir(_sorualani.getText().toString(),gelendilturkecemi));
                    } else {
                        _txtsorubilpara.setText("0.001");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private String puanParaCevir(String a, boolean c){
        if (a.equals("Bu film kaç yılında vizyona çıkmıştır?")|| c){
            String b = String.valueOf(userPuan*gelenParaDizi[1]/gelenParaDizi[0]);
            if (b.length()>6){
                return  "₺ "+b.substring(0,6);
            }
            return "₺ "+b;
        } else {
            String b = String.valueOf(userPuan*gelenParaDizi[3]/gelenParaDizi[2]);
            if (b.length()>6){
                return  "$ "+b.substring(0,6);
            }
            return "$ "+b;
        }
    }
    void dataTabloCountKolay(){
        DatabaseReference dataTableRef = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.sorular));
        dataTableRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataTableKolayCount =(int) dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void soruCek(final int firebasedakiSoruAdeti){
        girisDialog(getResources().getString(R.string.bilkazansoruhazirlaniyor));
        rnd = new Random();
        final int rnd1 = rnd.nextInt(firebasedakiSoruAdeti)+1;
        DatabaseReference refsorucek= FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.sorular)).child(Integer.toString(rnd1));
        refsorucek.addListenerForSingleValueEvent(new ValueEventListener() {
            MSoru mSoru;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mSoru = dataSnapshot.getValue(MSoru.class);
                if (mSoru!=null){
                    Picasso.get().load(mSoru.getImg()).into(_imgsorubilimg);
                    _btnsorubila.setText(String.valueOf(mSoru.getA()));
                    _btnsorubilb.setText(String.valueOf(mSoru.getB()));
                    _btnsorubilc.setText(String.valueOf(mSoru.getC()));
                    firebasegelencevap = mSoru.getCev();
                }
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void girisDialog(String a){
        dialog = new SpotsDialog.Builder().setContext(this)
                .setMessage(a)
                .setCancelable(false)
                .setTheme(R.style.Custom)
                .build();
        dialog.show();
    }
    private void status(String status){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(gelenid);
        HashMap<String,Object> map = new HashMap<>();
        map.put(getResources().getString(R.string.status),status);
        databaseReference.updateChildren(map);
    }
    @Override
    protected void onResume() {
        super.onResume();
        status("on");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("off");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode== android.view.KeyEvent.KEYCODE_BACK){
            oyundanCikisDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void oyundanCikisDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(BilKazan.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        //builder.setTitle("Başlangı");
        builder.setMessage(getResources().getString(R.string.baslangicagit));
        builder.setCancelable(true);
        builder.setNegativeButton(getResources().getString(R.string.hayir), new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.evet), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
        builder.show();
    }
    //kisisel reklam başlangıç
    private void reklamTanimlama(){
        //reklam başlangıç
        MobileAds.initialize(this, getResources().getString(R.string.admobappid));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.sorubilgecisreklamid));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        //odullu
        // Use an activity context to get the rewarded video instance.
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        rewardedVideoAd.setRewardedVideoAdListener((RewardedVideoAdListener) this);
        loadRewardedVideoAd();

        mAdView = findViewById(R.id.sorubiladw);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //reklam bitiş
    }
    private void kisiselReklamGosger(){
        if (reklamImg.length()>4){
            kisiselReklamSayacCek();
            mAdView.setVisibility(View.INVISIBLE);
            _imgsorubilpersonelad.setVisibility(View.VISIBLE);
            Picasso.get().load(reklamImg).into(_imgsorubilpersonelad);
        } else {
            mAdView.setVisibility(View.VISIBLE);
        }
    }
    private void kisiselReklamCek(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.oyunkontrol))
                .child(getResources().getString(R.string.reklamalanlari)).child(getResources().getString(R.string.kisiselreklamalanlar)).child(getResources().getString(R.string.bilkazanactivity));
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            PersonelAdw personelAdw;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                personelAdw = dataSnapshot.getValue(PersonelAdw.class);
                if (personelAdw!=null){
                    reklamImg = personelAdw.getImg();
                    reklamLink = personelAdw.getLink();
                    //Toast.makeText(getApplicationContext(),reklamImg + reklamLink,Toast.LENGTH_SHORT).show();
                    kisiselReklamGosger();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void kisiselReklamSayacCek(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.oyunkontrol))
                .child(getResources().getString(R.string.reklamalanlari)).child(getResources().getString(R.string.kisiselreklamalanlar))
                .child(getResources().getString(R.string.bilkazanactivity)).child(getResources().getString(R.string.sayac));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reklamsayac = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void kisiselRekamSayacArttir(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.oyunkontrol))
                .child(getResources().getString(R.string.reklamalanlari)).child(getResources().getString(R.string.kisiselreklamalanlar))
                .child(getResources().getString(R.string.bilkazanactivity)).child(getResources().getString(R.string.sayac));
        reference.setValue(reklamsayac+1);
    }
    //kisisel reklam bitiş
    private void loadRewardedVideoAd() {
        rewardedVideoAd.loadAd(getResources().getString(R.string.sorubilodulluid),
                new AdRequest.Builder().build());
    }

    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<< Referans için işlemler başlangıç >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    private void referansPuanCek(){
        if (referansinidsi.length()>5){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(referansinidsi).child(getResources().getString(R.string.kasa));
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    referansininpuani = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    private void referansinaPuanVer(int _gelenpuan){
        if (referansinidsi.length()>5){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(referansinidsi).child(getResources().getString(R.string.kasa));
            yuzdeonu = _gelenpuan * 10 / 100;
            reference.setValue(referansininpuani+ yuzdeonu);
            referansinaKazandirdigiToplamPuan();
        }
    }
    private void referansinaKazandirdigiToplamPuan(){
        DatabaseReference rf = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(gelenid).child(getResources().getString(R.string.refverdigitoplampuan));
        rf.setValue(referansinakazandirdigitoplampuan+yuzdeonu);
    }
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<< Referans için işlemler bitiş >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {
        if (odulalindimi == 0){
            odulPuanYaz();
            odulalindimi = 1;
        }

    }
    void odulPuanYaz(){
        anaekrandon.setVisibility(View.INVISIBLE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(gelenid).child(getResources().getString(R.string.puan));
        reference.setValue(userPuan + 100);
        Toast.makeText(this,100+getResources().getString(R.string.sorubilpuaneklendi),Toast.LENGTH_SHORT).show();
        referansinaPuanVer(100);
    }
}
