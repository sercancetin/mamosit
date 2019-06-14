package com.mamositapp.mamosit.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mamositapp.mamosit.R;
import com.mamositapp.mamosit.ShowNotification;
import com.mamositapp.mamosit.model.MAnasayfaKontrol;
import com.mamositapp.mamosit.model.User;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

import static android.view.animation.AnimationUtils.loadAnimation;

public class Home extends AppCompatActivity implements RewardedVideoAdListener {
    Context context = this;
    AlertDialog dialog;
    @BindView(R.id.txthomepuan)
    TextView _txthomepuan;
    @BindView(R.id.txthomepara)
    TextView _txthomepara;
    @BindView(R.id.txthomezaman)
    TextView _txthomezaman;
    @BindView(R.id.txthhomezamanpuan)
    TextView _txthhomezamanpuan;
    @BindView(R.id.txthomeaccount)
    TextView _txthomeaccount;
    @BindView(R.id.crdhomecark)
    CardView _crdhomecark;
    @BindView(R.id.crdhomesoru)
    CardView _crdhomesoru;
    @BindView(R.id.crdhomeodemeal)
    CardView _crdhomeodemeal;
    @BindView(R.id.crdhomepuankazan)
    CardView _crdhomepuankazan;
    @BindView(R.id.crdhometakimim)
    CardView _crdhometakimim;
    @BindView(R.id.crdhomeodemealanlar)
    CardView _crdhomeodemealanlar;
    @BindView(R.id.crdhomeyatirkazan)
    CardView _crdhomeyatirkazan;
    @BindView(R.id.lythomegunicipuan)
    LinearLayout _lythomegunicipuan;
    @BindView(R.id.txthomegunlukpuan)
    TextView _txthomegunlukpuan;
    @BindView(R.id.imghomecark)
    ImageView _imghomecark;
    @BindView(R.id.lythomeetkinlik)
    LinearLayout _lythomeetkinlik;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String usersongiris,firebaseversiyon,instagram,useradres,versiyon;
    int userpuan,userban,bakimmodu,dolarpuan,tlpuan,puanverpuan,puanverzaman,
            gunici, zamanpuankazanmaaktif, useretkinlik,yirmicevirmesonrasiodul;
    float dolarpara,tlpara;
    CountDownTimer puanverenzaman;
    Intent intent;
    //reklam icin
    private RewardedVideoAd mRewardedVideoAd;
    static int gunlukgirisodullu,etkinlikcarkiodullu,zamanpuankazanodullu;
    private InterstitialAd mInterstitialAd;
    //reklam bitiş
    //tarih cek
    SharedPreferences sharedPreferences;
    String time;
    //tarih cek bitiş
    //ozel gün rulet
    private Dialog dialog1;
    ImageView ruletimg;
    ImageView belirtec;
    Button ruletcevir,ruletbasla;
    Random ruletRnd;
    TextView ruletsonuc;
    private MediaPlayer ruletSesMedia,tick,kazanc;
    private MediaPlayer ruletSonuc;
    int degree = 0;
    int oldDegree =0;
    //ozel gün rulet bitiş
    float[] paraceviridizi;
    boolean turkcemi;
    //referansı için ihtiyaclar
    String referansinidsi;
    int referansinakazandirdigitoplampuan,referansininpuani,yuzdeonu;
    //referansı için ihtiyaclar bitiş
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        //tarih
        sharedPreferences = this.getSharedPreferences(getResources().getString(R.string.zaman),Context.MODE_PRIVATE);
        time = sharedPreferences.getString(getResources().getString(R.string.sitezaman),"00-00");
        //tarih bitiş
        oneDayNotification();
        if (wifiControl()){
            bilgileriCek();
        } else {
            wifiDialog();
        }
        //reklam başlangıç
        MobileAds.initialize(this, getResources().getString(R.string.admobappid));
        //odullu
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(Home.this);
        mRewardedVideoAd.setRewardedVideoAdListener((RewardedVideoAdListener) this);
        loadRewardedVideoAd1();
        //odullu bitiş
        //gecisbaşlangic
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.homegecisreklamid));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        //gecisbitiş
        //reklam bitiş
        tick = MediaPlayer.create(this,R.raw.tick);
        kazanc = MediaPlayer.create(this,R.raw.cash);
    }
    //<<<<<<<<<<<<<<<<<<<<<<<<<< Click Başlangıç >>>>>>>>>>>>>>>>>>>>>>>>>>>
    @OnClick(R.id.crdhomepuankazan) void crdhomepuankazanClick(){
        tick.start();
        if (wifiControl()){
            if (versiyon.equals(getResources().getString(R.string.appversiyon))){
                if (zamanpuankazanmaaktif == 1) {
                    if (mInterstitialAd.isLoaded()){
                        mInterstitialAd.show();
                        mInterstitialAd.setAdListener(new AdListener(){
                            @Override
                            public void onAdClosed() {
                                zamanBitinceTiklaPuanEkle();
                                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                                super.onAdClosed();
                            }
                        });
                    }
                }
            } else {
                Snackbar.make(_crdhomepuankazan,getResources().getString(R.string.yenigüncelversiyon),Snackbar.LENGTH_SHORT)
                        .setAction(getResources().getString(R.string.guncelle),onClickListenerGuncelle).show();
            }
        } else {
            wifiDialog();
        }
    }
    @OnClick(R.id.lythomegunicipuan) void lythomegunicipuanClick(){
        tick.start();
        if (wifiControl()){
            if (versiyon.equals(getResources().getString(R.string.appversiyon))){
                if (mRewardedVideoAd.isLoaded()){
                    odullureklamaktif(0,1,0);
                    mRewardedVideoAd.show();
                } else {
                    if (mInterstitialAd.isLoaded()){
                        mInterstitialAd.setAdListener(new AdListener(){
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                gunIciEkstraTiklaPuanEkle();
                                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                            }
                        });
                    } else {
                        Toast.makeText(this,getResources().getString(R.string.sonratekrardene),Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Snackbar.make(_crdhomepuankazan,getResources().getString(R.string.yenigüncelversiyon),Snackbar.LENGTH_SHORT)
                        .setAction(getResources().getString(R.string.guncelle),onClickListenerGuncelle).show();
            }
        } else {
            wifiDialog();
        }
    }
    @OnClick(R.id.lythomeetkinlik) void lythomeetkinlikClick(){
        tick.start();
        if (useretkinlik==1){
            if (mRewardedVideoAd.isLoaded()){
                odullureklamaktif(0,0,1);
                mRewardedVideoAd.show();
            } else {
                if (mInterstitialAd.isLoaded()){
                    mInterstitialAd.setAdListener(new AdListener(){
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            ozelGunDialogCagir();
                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                        }
                    });
                } else {
                    Toast.makeText(this,getResources().getString(R.string.sonratekrardene),Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Snackbar.make(_lythomeetkinlik,getResources().getString(R.string.zatenbirkerecevirdiniz),Snackbar.LENGTH_SHORT).show();
        }
    }
    @OnClick(R.id.crdhomecark) void crdhomecarkClick(){
        tick.start();
        if (wifiControl()){
            if (versiyon.equals(getResources().getString(R.string.appversiyon))){
                intent = new Intent(this,CevirKazan.class);
                intent.putExtra(getResources().getString(R.string.userid),user.getUid());
                intent.putExtra(getResources().getString(R.string.dil),turkcemi);
                intent.putExtra(getResources().getString(R.string.paracevirdizi),paraceviridizi);
                intent.putExtra(getResources().getString(R.string.yirmicevirmesonrasiodul),yirmicevirmesonrasiodul);
                intent.putExtra(getResources().getString(R.string.referansinidsi),referansinidsi);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            } else {
                Snackbar.make(_crdhomepuankazan,getResources().getString(R.string.yenigüncelversiyon),Snackbar.LENGTH_SHORT)
                        .setAction(getResources().getString(R.string.guncelle),onClickListenerGuncelle).show();
            }
        } else {
            wifiDialog();
        }
    }
    @OnClick(R.id.crdhomesoru) void crdhomesoruClick(){
        tick.start();
        if (wifiControl()){
            if (versiyon.equals(getResources().getString(R.string.appversiyon))){
                intent = new Intent(this,BilKazan.class);
                intent.putExtra(getResources().getString(R.string.userid),user.getUid());
                intent.putExtra(getResources().getString(R.string.dil),turkcemi);
                intent.putExtra(getResources().getString(R.string.paracevirdizi),paraceviridizi);
                intent.putExtra(getResources().getString(R.string.referansinidsi),referansinidsi);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            } else {
                Snackbar.make(_crdhomepuankazan,getResources().getString(R.string.yenigüncelversiyon),Snackbar.LENGTH_SHORT)
                        .setAction(getResources().getString(R.string.guncelle),onClickListenerGuncelle).show();
            }
        } else {
            wifiDialog();
        }
    }
    @OnClick(R.id.crdhomeodemeal) void _crdhomeodemealClick(){
        tick.start();
        if (wifiControl()){
            intent = new Intent(this,OdemeAl.class);
            intent.putExtra(getResources().getString(R.string.userid),user.getUid());
            intent.putExtra(getResources().getString(R.string.tarih),time);
            intent.putExtra(getResources().getString(R.string.dil),turkcemi);
            intent.putExtra(getResources().getString(R.string.paracevirdizi),paraceviridizi);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        } else {
            Toast.makeText(this,getResources().getString(R.string.internetbaglantihatasi),Toast.LENGTH_SHORT).show();
        }
    }
    @OnClick(R.id.crdhomeodemealanlar) void _crdhomeodemealanlarClick(){
        tick.start();
        startActivity(new Intent(this,Siparis.class));
    }
    @OnClick(R.id.crdhomegorevyap) void _crdhomecekilisekatilClick(){
        tick.start();
        startActivity(new Intent(this,GorevYap.class));
    }
    @OnClick(R.id.crdhomeyatirkazan) void _crdhomeyatirkazanClick(){
        tick.start();
        intent = new Intent(this,YatirKazan.class);
        intent.putExtra(getResources().getString(R.string.userid),user.getUid());
        intent.putExtra(getResources().getString(R.string.usermail),user.getEmail());
        intent.putExtra(getResources().getString(R.string.tarih),time);
        intent.putExtra(getResources().getString(R.string.dil),turkcemi);
        intent.putExtra(getResources().getString(R.string.paracevirdizi),paraceviridizi);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

    }
    @OnClick(R.id.crdhometakimim) void _crdhometakimim(){
        tick.start();
        intent = new Intent(this,Takimim.class);
        intent.putExtra(getResources().getString(R.string.userid),user.getUid());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }
    @OnClick(R.id.lythomehesap) void _lythomehesapClick(){
        tick.start();
        intent = new Intent(this,Hesabim.class);
        intent.putExtra(getResources().getString(R.string.para),_txthomepara.getText().toString());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }
    //<<<<<<<<<<<<<<<<<<<<<<<<<< Click Bitiş >>>>>>>>>>>>>>>>>>>>>>>>>>>
    View.OnClickListener onClickListenerGuncelle = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.playlink1))));
            }catch (ActivityNotFoundException e){
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.playlink2))));
            }
        }
    };


    private void girisDialog(String a){
        dialog = new SpotsDialog.Builder().setContext(context)
                .setMessage(a)
                .setCancelable(false)
                .setTheme(R.style.Custom)
                .build();
        dialog.show();
    }
    private void oyundanCikisDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(Home.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setTitle(getResources().getString(R.string.uygulamacikis1));
        builder.setMessage(getResources().getString(R.string.uygulamacikis2));
        builder.setCancelable(true);
        builder.setNegativeButton(getResources().getString(R.string.hayir), new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.evet), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finishAffinity();
            }
        });
        builder.show();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode== android.view.KeyEvent.KEYCODE_BACK){
            oyundanCikisDialog();
        }
        return super.onKeyDown(keyCode, event);
    }
    protected boolean wifiControl() {
        // TODO Auto-generated method stub
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    private void wifiDialog(){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.internetbaglantihatasi));
        builder.setMessage(getResources().getString(R.string.splahnetbilgi));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getText(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finishAffinity();
            }
        });
        builder.show();
    }
    private void bilgileriCek(){
        anaSayfaKontrolCek();
        animationBaslat();
    }

    private void animationBaslat() {
        Animation a = loadAnimation(this,R.anim.donme);
        _imghomecark.startAnimation(a);
    }

    private void userBilgiCek(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            User model;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model = dataSnapshot.getValue(User.class);
                if (model!=null){
                    usersongiris = model.getSongiris();
                    userpuan = model.getPuan();
                    userban = model.getBan();
                    useradres = model.getAdres();
                    useretkinlik = model.getUseretkinlik();
                    referansinidsi = model.getKimgetirdi();
                    referansinakazandirdigitoplampuan = model.getRefverdigitoplampuan();
                    _txthomepuan.setText(String.valueOf(model.getPuan()));
                    turkcemi = turkiyeKelimesiVarmi();
                    if (userpuan>100){
                        _txthomepara.setText(puanParaCevir(_txthomeaccount.getText().toString(),turkcemi));
                    } else {
                        _txthomepara.setText("0.001");
                    }
                    referansPuanCek();
                   //Toast.makeText(getApplicationContext(),usersongiris+"---"+time,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dialog.dismiss();
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
        DatabaseReference rf = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(user.getUid()).child(getResources().getString(R.string.refverdigitoplampuan));
        rf.setValue(referansinakazandirdigitoplampuan+yuzdeonu);
    }
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<< Referans için işlemler bitiş >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    private void anaSayfaKontrolCek(){
        girisDialog(getResources().getString(R.string.sunucubaglantikuruluyor));
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.oyunkontrol)).child(getResources().getString(R.string.anasayfakontrol));
        reference.addValueEventListener(new ValueEventListener() {
            MAnasayfaKontrol m;
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                m = dataSnapshot.getValue(MAnasayfaKontrol.class);
                if (m!=null){
                    firebaseversiyon = m.getVersiyon();
                    bakimmodu = m.getBakimmodu();
                    dolarpara = m.getDolarpara();
                    dolarpuan = m.getDolarpuan();
                    puanverpuan = m.getPuanverpuan();
                    puanverzaman = m.getPuanverzaman();
                    tlpara = m.getTlpara();
                    tlpuan = m.getTlpuan();
                    instagram = m.getInstgram();
                    versiyon = m.getVersiyon();
                    gunici = m.getGunicibirkereverilenpuan();
                    yirmicevirmesonrasiodul = m.getYirmicevirmesonrasiodul();
                    _txthomegunlukpuan.setText("+"+ gunici);
                    userBilgiCek();
                    gelenleriCalistir();
                    paraceviridizi = new float[]{tlpuan,tlpara,dolarpuan,dolarpara};
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void gelenleriCalistir(){
        girisDialog(getResources().getString(R.string.sunucugelenkontrolediliyor));
        new CountDownTimer(2200, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if (bakimmodu==0){
                    if (userban==0){
                        zamanPuanVerenBaslat();
                        hediyeGuniciPuanGozuksunmu(usersongiris);
                        etkinlikCarkiAktifEt();
                    } else {
                        secenekliDialog(getResources().getString(R.string.banlandiniz),getResources().getString(R.string.banaciklama));
                    }
                } else {
                    secenekliDialog(getResources().getString(R.string.bakimdayiz),getResources().getString(R.string.bakimaciklama));
                }
                dialog.dismiss();
            }
        }.start();
    }
    private void secenekliDialog(String baslik,String icerik){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle(baslik);
        builder.setMessage(icerik);
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getText(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finishAffinity();
            }
        });
        builder.show();
    }
    private void zamanPuanVerenBaslat(){
        zamanpuankazanmaaktif = 0;
        _txthhomezamanpuan.setText(String.valueOf(puanverpuan));
        puanverenzaman = new CountDownTimer(puanverzaman,1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                _txthomezaman.setText(""+millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                _txthomezaman.setText("0");
                zamanpuankazanmaaktif = 1;
                //customNotification();
            }
        }.start();
    }
    private void zamanBitinceTiklaPuanEkle(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(user.getUid()).child(getResources().getString(R.string.puan));
        reference.setValue(userpuan+puanverpuan);
        zamanPuanVerenBaslat();
        Toast.makeText(this,puanverpuan+getResources().getString(R.string.zamanpuaneklendi),Toast.LENGTH_SHORT).show();
        referansinaPuanVer(puanverpuan);
    }
    private void gunIciEkstraTiklaPuanEkle(){
        _lythomegunicipuan.setVisibility(View.INVISIBLE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(user.getUid()).child(getResources().getString(R.string.puan));
        reference.setValue(userpuan+gunici);
        userTarihDegistir();
        referansinaPuanVer(gunici);
        Toast.makeText(this,gunici+getResources().getString(R.string.gunlukpuaneklendi),Toast.LENGTH_SHORT).show();
    }
    private void userTarihDegistir(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(user.getUid()).child(getResources().getString(R.string.songiris));
        reference.setValue(time);
        if (useretkinlik ==0){
            userEtkinlikDegistir(1);
        }
    }
    private void etkinlikCarkiAktifEt(){
        Query query = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.oyunkontrol))
                .child(getResources().getString(R.string.tekliveriler)).child(getResources().getString(R.string.etkinlikcarki));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int a = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));
                if (a==1){
                    _lythomeetkinlik.setVisibility(View.VISIBLE);
                    carkEtkinligiAnimation();
                }  else {
                    _lythomeetkinlik.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void ozelGunDialogCagir(){
        final Animation sarkac = AnimationUtils.loadAnimation(context,R.anim.sarkac);
        dialog1=new Dialog(Home.this);
        dialog1.setContentView(R.layout.popup_rulet);
        ruletcevir = (Button) dialog1.findViewById(R.id.ozelgunruletcevir);
        ruletimg = (ImageView) dialog1.findViewById(R.id.ozelgunruletimg);
        belirtec = (ImageView) dialog1.findViewById(R.id.ozelgunbelirtec);
        ruletRnd = new Random();
        ruletSesMedia = MediaPlayer.create(context,R.raw.ruletses);
        ruletSonuc = MediaPlayer.create(context,R.raw.ruletsonuc);
        ruletsonuc = dialog1.findViewById(R.id.ozelgunruletsonuc);
        ruletbasla = dialog1.findViewById(R.id.ozelgunruletkapat);
        ruletbasla.setVisibility(View.INVISIBLE);
        
        if (wifiControl()){
            ruletcevir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ruletSesMedia = MediaPlayer.create(context,R.raw.ruletses);
                    oldDegree = degree % 360;
                    degree = ruletRnd.nextInt(3600)+720;
                    RotateAnimation rotateAnimation = new RotateAnimation(oldDegree,degree,RotateAnimation.RELATIVE_TO_SELF,0.5f, RotateAnimation.RELATIVE_TO_SELF,0.5f);
                    rotateAnimation.setDuration(3600);
                    rotateAnimation.setFillAfter(true);
                    rotateAnimation.setInterpolator(new DecelerateInterpolator());
                    rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            ruletSesMedia.start();
                            ruletcevir.setVisibility(View.INVISIBLE);
                            belirtec.startAnimation(sarkac);
                            ozelgunsifirla();
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            int carksonuc = ozelGunCurrentNumber(360-(degree%360));
                            String as =carksonuc+getResources().getString(R.string.ekstrapuaneklendi);
                            ruletsonuc.setText(as);
                            ruletSesMedia.reset();
                            belirtec.clearAnimation();
                            ruletSonuc.start();
                            ruletbasla.setVisibility(View.VISIBLE);
                            uyeCarkSonucuPuanArttir(carksonuc);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    ruletimg.startAnimation(rotateAnimation);
                }
            });
        } else {
            Toast.makeText(context,getResources().getString(R.string.internetbaglantikesildi),Toast.LENGTH_SHORT).show();
        }

        Objects.requireNonNull(dialog1.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.show();
        dialog1.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (ruletSesMedia.isPlaying()){
                    ruletSesMedia.reset();
                }
                belirtec.clearAnimation();
                Toast.makeText(context,getResources().getString(R.string.kapandi),Toast.LENGTH_SHORT).show();
            }
        });
        ruletbasla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ruletSesMedia.reset();
                belirtec.clearAnimation();
                dialog1.dismiss();
            }
        });
    }
    private int ozelGunCurrentNumber(int _degree){
        int sonuc=0;
        if (_degree <= 15) {
            sonuc = 50;
        } else if (_degree <= 45){
            sonuc = 250;
        } else if (_degree <= 75){
            sonuc = 100;
        } else if (_degree <= 105){
            sonuc = 350;
        } else if (_degree <= 135){
            sonuc = 400;
        } else if (_degree <= 165){
            sonuc = 150;
        } else if (_degree <= 195){
            sonuc = 600;
        } else if (_degree <= 225){
            sonuc = 50;
        } else if (_degree <= 255){
            sonuc = 750;
        } else if (_degree <= 285){
            sonuc = 2500;
        } else if (_degree <= 315){
            sonuc = 10;
        } else if (_degree <= 345){
            sonuc = 1000;
        } else if (_degree <= 360){
            sonuc = 50;
        }
        return sonuc;
    }
    private void uyeCarkSonucuPuanArttir(int carksonuc1){
        int a = userpuan+carksonuc1;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(user.getUid()).child(getResources().getString(R.string.puan));
        databaseReference.setValue(a);
        Toast.makeText(this,carksonuc1+getResources().getString(R.string.carkpuaneklendi),Toast.LENGTH_SHORT).show();
        referansinaPuanVer(carksonuc1);
    }
    private void ozelgunsifirla(){
        userEtkinlikDegistir(0);
    }
    private void userEtkinlikDegistir(int a){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(user.getUid()).child(getResources().getString(R.string.useretkinlik));
        reference.setValue(a);
    }
    private String puanParaCevir(String a, boolean c){
        if (a.equals("Hesabım")|| c){
            String b = String.valueOf(userpuan*tlpara/tlpuan);
            if (b.length()>6){
                return  "₺ "+b.substring(0,6);
            }
            return "₺ "+b;
        } else {
            String b = String.valueOf(userpuan*dolarpara/dolarpuan);
            if (b.length()>6){
                return  "$ "+b.substring(0,6);
            }
            return "$ "+b;
        }
    }
    private boolean turkiyeKelimesiVarmi(){
        if (useradres.matches("(.*)Türkiye(.*)")){
            //Toast.makeText(this,"if 1 var",Toast.LENGTH_SHORT).show();
            return true;
        } else if (useradres.matches("(.*)Türkiye")){
            //Toast.makeText(this,"if 2 var",Toast.LENGTH_SHORT).show();
            return true;
        } else if (useradres.matches("(.*)Turkey")){
            return true;
        } else if (useradres.matches("(.*)türkiye")){
            return true;
        } else if (useradres.matches("(.*)türkiye(.*)")){
            //Toast.makeText(this,"if 1 var",Toast.LENGTH_SHORT).show();
            return true;
        } else return useradres.matches("(.*)Turkey(.*)");
    }
    private void hediyeGuniciPuanGozuksunmu(String usersongiristarih){
        if (!usersongiristarih.equals(time)){
            hediGunlukAnimationGel();
            _lythomegunicipuan.setVisibility(View.VISIBLE);
        }
    }
    private void hediGunlukAnimationGel(){
        Animation as1 = loadAnimation(this,R.anim.lefttoright);
        _lythomegunicipuan.startAnimation(as1);
    }
    private void carkEtkinligiAnimation(){
        Animation as = loadAnimation(this,R.anim.rightoleft);
        _lythomeetkinlik.startAnimation(as);
    }
    private void customNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("kanal_id","kanalad", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("kanal_desc");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"kanal_id")
                .setSmallIcon(R.drawable.duyuru)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("Zaman doldu. Puanını şimdi al.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1,builder.build());
    }
    private void oneDayNotification(){
        Intent notificationIntent = new Intent(context, ShowNotification.class);
        PendingIntent contentIntent = PendingIntent.getService(context, 0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.cancel(contentIntent);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + AlarmManager.INTERVAL_DAY, AlarmManager.INTERVAL_DAY, contentIntent);
    }

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
        loadRewardedVideoAd1();
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
        /*if (zamanpuankazanodullu==1){
            zamanpuankazanodullu = 0;
            zamanBitinceTiklaPuanEkle();
        } else */if (gunlukgirisodullu == 1){
            gunlukgirisodullu = 0;
            gunIciEkstraTiklaPuanEkle();
        } else if (etkinlikcarkiodullu == 1){
            etkinlikcarkiodullu = 0;
            ozelGunDialogCagir();
        }
    }
    private void odullureklamaktif(int zamanpuankazanodullu, int gunlukgirisodullu,int etkinlikcarkiodullu){
        Home.zamanpuankazanodullu = zamanpuankazanodullu;
        Home.gunlukgirisodullu = gunlukgirisodullu;
        Home.etkinlikcarkiodullu = etkinlikcarkiodullu;
    }
    private void loadRewardedVideoAd1() {
        mRewardedVideoAd.loadAd(getResources().getString(R.string.homeodulluid),
                new AdRequest.Builder().build());
    }

    private void status(String status){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(user.getUid());
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
}
