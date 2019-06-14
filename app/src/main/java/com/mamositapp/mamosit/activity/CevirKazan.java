package com.mamositapp.mamosit.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mamositapp.mamosit.R;
import com.mamositapp.mamosit.model.PersonelAdw;
import com.mamositapp.mamosit.model.User;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class CevirKazan extends AppCompatActivity implements RewardedVideoAdListener  {
    @BindView(R.id.imgcarkkendisi)
    ImageView _imgcarkkendisi;
    @BindView(R.id.lytcevir)
    LinearLayout _lytcevir;
    @BindView(R.id.txttoplamcevirme)
    TextView _txtcevirmetoplam;
    @BindView(R.id.lytwait) LinearLayout _lytwait;
    @BindView(R.id.carksonuc) TextView _carksonuc;
    @BindView(R.id.txtsahipolancoin) TextView _txtsahipolancoin;
    @BindView(R.id.progresscevirkazan)
    ProgressBar progresscevirkazan;
    @BindView(R.id.txtcevirkazanpara) TextView _txtcevirkazanpara;
    @BindView(R.id.txtkazandiginizpuan) TextView _txtkazandiginizpuan;
    @BindView(R.id.btncevirkazanodulal)
    Button _btncevirkazanodulal;
    @BindView(R.id.txtcevirkazan20sonraodul) TextView _txtcevirkazan20sonraodul;
    @BindView(R.id.imgcevirkazanpersonelad) ImageView _imgcevirkazanpersonelad;
    //rulet için
    Random ruletRnd;
    int degree = 0;
    int oldDegree =0;
    MediaPlayer ruletSesMedia;
    MediaPlayer ruletSonuc;
    MediaPlayer puanAlmaSes;
    //because there is 37 sectors on the wheel (9.72 degress each)
    private static final float FACTOR = 4.86f;
    //rulet bitiş
    int carksonuc;
    int userPuan,gelenyirmicevirmesonrasiodul,cark20cevirmegeldimi,uyepuanarttirmakistedimi;
    private int cevirmesayisi =0;
    boolean gelendilturkecemi;
    String gelenid;
    //float gelenTlPara,gelenDolaraPara;
    //int gelenTlPuan,gelenDolaraPuan;
    float[] gelenParaDizi;
    private Dialog dialog;
    private RewardedVideoAd mRewardedVideoAd;
    private InterstitialAd mInterstitialAd;
    private int cikis =0;
    //kisisel reklam
    private int reklamsayac,referansininpuani,referansinakazandirdigitoplampuan,yuzdeonu;
    private String reklamLink,reklamImg,referansinidsi;
    //kisiselreklam bitiş
    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cevir_kazan);
        ButterKnife.bind(this);
        gelenid=getIntent().getStringExtra(getResources().getString(R.string.userid));
        gelendilturkecemi = getIntent().getBooleanExtra(getResources().getString(R.string.dil),true);
        gelenParaDizi = getIntent().getFloatArrayExtra(getResources().getString(R.string.paracevirdizi));
        gelenyirmicevirmesonrasiodul = getIntent().getIntExtra(getResources().getString(R.string.yirmicevirmesonrasiodul),50);
        referansinidsi = getIntent().getStringExtra(getResources().getString(R.string.referansinidsi));
        _txtcevirkazan20sonraodul.setText(String.valueOf(gelenyirmicevirmesonrasiodul));
        profilcek();
        referansPuanCek();
        kisiselReklamCek();
        //reklam başlangıç
        MobileAds.initialize(this, getResources().getString(R.string.admobappid));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.carkcevirgecisreklamid));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        //odullu
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener((RewardedVideoAdListener) this);
        loadRewardedVideoAd();

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //reklam bitiş
    }
    //<<<<<<<<<<<<<<< Tıklamalar >>>>>>>>>>>>>>>>
    @OnClick(R.id.lytcevir) void lytcevirClick(){
        if (cevirmesayisi %5 == 4){
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
                mInterstitialAd.setAdListener(new AdListener(){
                    @Override
                    public void onAdClosed() {
                        cevirKontrol();
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }
                });
            } else {
                cevirKontrol();
            }
        } else {
            cevirKontrol();
        }
    }
    @OnClick(R.id.btncevirkazanodulal) void _btncevirkazanodulalClick(){
        if (mRewardedVideoAd.isLoaded()) {
            cark20cevirmegeldimi = 1;
            mRewardedVideoAd.show();
        } else {
            Toast.makeText(CevirKazan.this,getResources().getString(R.string.sonratekrardene),Toast.LENGTH_SHORT).show();
        }
    }
    @OnClick(R.id.imgcevirkazanpersonelad) void imgcevirkazanpersoneladClick(){
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(reklamLink)));
            kisiselRekamSayacArttir();
        }catch (ActivityNotFoundException e){
            //Toast.makeText(this,"Link hazır değil.",Toast.LENGTH_SHORT).show();
        }
    }
    //<<<<<<<<<<<<<<< Tıklamalar Bitiş >>>>>>>>>>>>>>>>
    private void profilcek(){
        girisDialog("");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(gelenid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            User user;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (user!=null){
                    userPuan = user.getPuan();
                    referansinakazandirdigitoplampuan = user.getRefverdigitoplampuan();
                    _txtsahipolancoin.setText(String.valueOf(userPuan));
                    if (userPuan>100){
                        _txtcevirkazanpara.setText(puanParaCevir(_txtkazandiginizpuan.getText().toString(),gelendilturkecemi));
                    } else {
                        _txtcevirkazanpara.setText("0.001");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dialog.dismiss();
    }
    private void cevirKontrol(){
        cevir();
    }
    private void cevir(){
        if (wifiControl()){
            cevirmeyeBasla();
        } else{
            Toast.makeText(this,getResources().getString(R.string.internetbaglantihatasi),Toast.LENGTH_SHORT).show();
        }
    }
    private void cevirmeyeBasla(){
        ruletSesMedia = MediaPlayer.create(this,R.raw.ruletses);
        ruletSonuc = MediaPlayer.create(this,R.raw.ruletsonuc);
        ruletRnd = new Random();
        oldDegree = degree % 360;
        degree = ruletRnd.nextInt(3600)+720;
        RotateAnimation rotateAnimation = new RotateAnimation(oldDegree,degree,RotateAnimation.RELATIVE_TO_SELF,0.5f, RotateAnimation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(4000);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setInterpolator(new DecelerateInterpolator());
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ruletSesMedia.start();
                _lytwait.setVisibility(View.VISIBLE);
                _lytcevir.setVisibility(View.INVISIBLE);
                ceviriArttir();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                carksonuc=currentNumber(360-(degree%360));
                _carksonuc.setText(String.valueOf(carksonuc));
                ruletSesMedia.reset();
                ruletSonuc.start();
                _lytcevir.setVisibility(View.VISIBLE);
                _lytwait.setVisibility(View.INVISIBLE);

                if (carksonuc>9){
                    coinArttirOdullu();
                } else {
                    uyePuanArttir(0);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        _imgcarkkendisi.startAnimation(rotateAnimation);
    }
    private int currentNumber(int _degree){
        int sonuc=0;
        if (_degree <= 15) {
            sonuc = 1;
        } else if (_degree <= 45){
            sonuc = 6;
        } else if (_degree <= 75){
            sonuc = 8;
        } else if (_degree <= 105){
            sonuc = 1;
        } else if (_degree <= 135){
            sonuc = 3;
        } else if (_degree <= 165){
            sonuc = 2;
        } else if (_degree <= 195){
            sonuc = 4;
        } else if (_degree <= 225){
            sonuc = 3;
        } else if (_degree <= 255){
            sonuc = 5;
        } else if (_degree <= 285){
            sonuc = 2;
        } else if (_degree <= 315){
            sonuc = 5;
        } else if (_degree <= 345){
            sonuc = 10;
        } else if (_degree <= 360){
            sonuc = 1;
        }
        return sonuc;
    }
    protected boolean wifiControl() {
        // TODO Auto-generated method stub
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    private void ceviriArttir(){
        cevirmesayisi++;
        _txtcevirmetoplam.setText(String.valueOf(cevirmesayisi));
        progresscevirkazan.setProgress(cevirmesayisi);
        if (cevirmesayisi==20){
            progresscevirkazan.setVisibility(View.INVISIBLE);
            _btncevirkazanodulal.setVisibility(View.VISIBLE);
            cevirmesayisi = 0 ;
        }
    }
    private void uyePuanArttir(int ekstra){
        int a = userPuan+carksonuc+ekstra;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(gelenid).child(getResources().getString(R.string.puan));
        databaseReference.setValue(a);
        uyepuanarttirmakistedimi = 0;
        Toast.makeText(this,carksonuc+ekstra+getResources().getString(R.string.carkpuaneklendi),Toast.LENGTH_SHORT).show();
        referansinaPuanVer(carksonuc+ekstra);
    }
    private void uye20OdulSonraPuanArttir(){
        puanAlmaSes = MediaPlayer.create(this,R.raw.cashsound);
        _btncevirkazanodulal.setVisibility(View.INVISIBLE);
        progresscevirkazan.setProgress(0);
        progresscevirkazan.setVisibility(View.VISIBLE);
        cevirmesayisi = 0;
        cark20cevirmegeldimi = 0;
        puanAlmaSes.start();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(gelenid).child(getResources().getString(R.string.puan));
        databaseReference.setValue(userPuan+gelenyirmicevirmesonrasiodul);
        Toast.makeText(this,gelenyirmicevirmesonrasiodul+getResources().getString(R.string.carkpuaneklendi),Toast.LENGTH_SHORT).show();
    }
    private void loadRewardedVideoAd() {
        //Toast.makeText(this,"odül yüklü",Toast.LENGTH_SHORT).show();
        mRewardedVideoAd.loadAd(getResources().getString(R.string.carkcevirodulluid),
                new AdRequest.Builder().build());
    }
    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {
        loadRewardedVideoAd();
    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(com.google.android.gms.ads.reward.RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {
        if (cark20cevirmegeldimi == 1){
            uye20OdulSonraPuanArttir();
        } else if (uyepuanarttirmakistedimi==1) {
            uyePuanArttir(20);
        }
    }

    private void coinArttirOdullu(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(CevirKazan.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setTitle("+20 Ekstra Coin Kazan");
        builder.setMessage(getResources().getString(R.string.reklamizlepuankazan));
        builder.setCancelable(true);
        builder.setNegativeButton(getResources().getString(R.string.iptal), new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id) {
                uyePuanArttir(0);
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.tamam), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (mRewardedVideoAd.isLoaded()) {
                    cark20cevirmegeldimi = 0;
                    uyepuanarttirmakistedimi = 1;
                    mRewardedVideoAd.show();
                } else {
                    Toast.makeText(CevirKazan.this,getResources().getString(R.string.sonratekrardene),Toast.LENGTH_SHORT).show();
                    uyePuanArttir(0);
                }
            }
        });
        builder.show();
    }
    private void girisDialog(String a){
        dialog = new SpotsDialog.Builder().setContext(this)
                .setMessage(a)
                .setCancelable(false)
                .setTheme(R.style.Custom)
                .build();
        dialog.show();
    }
    private String puanParaCevir(String a, boolean c){
        if (a.equals("Kazandığınız puan")|| c){
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

    private void kisiselReklamGosger(){
        if (reklamImg.length()>4){
            kisiselReklamSayacCek();
            mAdView.setVisibility(View.INVISIBLE);
            _imgcevirkazanpersonelad.setVisibility(View.VISIBLE);
            Picasso.get().load(reklamImg).into(_imgcevirkazanpersonelad);
        } else {
            mAdView.setVisibility(View.VISIBLE);
        }
    }
    private void kisiselReklamCek(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.oyunkontrol))
                .child(getResources().getString(R.string.reklamalanlari)).child(getResources().getString(R.string.kisiselreklamalanlar)).child(getResources().getString(R.string.carkactiviyt));
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
                .child(getResources().getString(R.string.reklamalanlari))
                .child(getResources().getString(R.string.kisiselreklamalanlar))
                .child(getResources().getString(R.string.carkactiviyt)).child(getResources().getString(R.string.sayac));
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
                .child(getResources().getString(R.string.reklamalanlari))
                .child(getResources().getString(R.string.kisiselreklamalanlar))
                .child(getResources().getString(R.string.carkactiviyt)).child(getResources().getString(R.string.sayac));
        reference.setValue(reklamsayac+1);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode== android.view.KeyEvent.KEYCODE_BACK){
            oyundanCikisDialog();
        }
        return super.onKeyDown(keyCode, event);
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
    private void oyundanCikisDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(CevirKazan.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
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
    //<<<<<<<<<<<<<<<<<<< Online >>>>>>>>>>>>>>>>>>>>>>
    private void status(String status){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(gelenid);
        HashMap<String,Object> map = new HashMap<>();
        map.put("status",status);
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

    //<<<<<<<<<<<<<<<<<<< Online Bitiş>>>>>>>>>>>>>>>>>>>>>>
}
