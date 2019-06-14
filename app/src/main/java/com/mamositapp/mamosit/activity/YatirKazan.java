package com.mamositapp.mamosit.activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mamositapp.mamosit.R;
import com.mamositapp.mamosit.model.MHavuzBilgi;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class YatirKazan extends AppCompatActivity {
    @BindView(R.id.txtkatilimsayisi)
    TextView _txtkatilimsayisi;
    @BindView(R.id.txtcekilistarih)
    TextView _txtcekilistarih;
    @BindView(R.id.txtuserkatilimsayisi)
    TextView _txtuserkatilimsayisi;
    @BindView(R.id.txtusercikmaolasiligi)
    TextView _txtusercikmaolasiligi;
    @BindView(R.id.txthavuzcoin)
    TextView _txthavuzcoin;
    @BindView(R.id.eklenececoin)
    TextView _eklenececoin;
    @BindView(R.id.havuztl)
    TextView _havuztl;
    @BindView(R.id.lytcekilissonucu)
    LinearLayout _lytcekilissonucu;
    @BindView(R.id.lytcekilisekatil)
    LinearLayout _lytcekilisekatil;
    @BindView(R.id.txtyatirkatilmasayiniz)
            TextView _txtyatirkatilmasayiniz;
    int katilansayisi=0;
    int cekilisDegeri=0;
    int havuzdakiCoin = 0;
    int userCoin=0;
    int userkatilimsayisi=0;
    Dialog dialog;
    MediaPlayer mediaPlayer;
    String firebaseTarih,firebaseLink;
    String gelenid,gelentarih,gelenmail;
    float[] gelenParaDizi;
    boolean gelendilturkecemi;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yatir_kazan);
        ButterKnife.bind(this);
        gelenid = getIntent().getStringExtra(getResources().getString(R.string.userid));
        gelenmail = getIntent().getStringExtra(getResources().getString(R.string.usermail));
        gelentarih = getIntent().getStringExtra(getResources().getString(R.string.tarih));
        gelendilturkecemi = getIntent().getBooleanExtra(getResources().getString(R.string.dil),true);
        gelenParaDizi = getIntent().getFloatArrayExtra(getResources().getString(R.string.paracevirdizi));
        mediaPlayer = MediaPlayer.create(this,R.raw.cash);
        userCoinCek();
        havuzCekilisCek();
        //reklam başlangıç
        MobileAds.initialize(this, getResources().getString(R.string.admobappid));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.carkcevirgecisreklamid));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        //reklam bitiş
    }
    //<<<<<<<<<<<<<<<<<<<< Tıklamalar >>>>>>>>>>>>>>>>>>>>>>
    @OnClick(R.id.btnyatirkazanbilgilendirici) void btnyatirkazanbilgilendiriciClick(){
        bilgi();
    }

    //<<<<<<<<<<<<<<<<<<<< Tıklamalar Bitiş >>>>>>>>>>>>>>>>>>>
    @OnClick(R.id.lytcekilisekatil) void lytcekilisekatilClick(){
        if (internetKonrol()){
            if (userCoin>=cekilisDegeri){
                if (mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                    mInterstitialAd.setAdListener(new AdListener(){
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            mediaPlayer.start();
                            cekiliseKatil();
                            cekiliseKatildiktanSonraBeklet();
                        }
                    });
                } else {
                    mediaPlayer.start();
                    cekiliseKatil();
                    cekiliseKatildiktanSonraBeklet();
                }
            } else {
                Snackbar.make(_lytcekilisekatil,getResources().getString(R.string.puaninizkatilmakicinyeterlideğil), Snackbar.LENGTH_LONG)
                        .setAction("s", null).show();
            }
        } else {
            Snackbar.make(_lytcekilisekatil,getResources().getString(R.string.internetbaglantihatasi), Snackbar.LENGTH_LONG)
                    .setAction("s", null).show();
        }
    }
    @OnClick(R.id.lytcekilissonucu) void lytcekilissonucuClick(){
        try {
            if (mInterstitialAd.isLoaded()){
                mInterstitialAd.show();
                mInterstitialAd.setAdListener(new AdListener(){
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(firebaseLink)));
                    }
                });
            } else {
                this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(firebaseLink)));
            }
        }catch (ActivityNotFoundException e){
            Toast.makeText(this,"Sonra deneyin.", Toast.LENGTH_SHORT).show();
        }
    }
    private String puanParaCevir(String a, boolean c){
        if (a.equals("Katılma sayınız:")|| c){
            String b = String.valueOf(yuzde20al(havuzdakiCoin)*gelenParaDizi[1]/gelenParaDizi[0]);
            if (b.length()>6){
                return  "₺ "+b.substring(0,6);
            }
            return "₺ "+b;
        } else {
            String b = String.valueOf(yuzde20al(havuzdakiCoin)*gelenParaDizi[3]/gelenParaDizi[2]);
            if (b.length()>6){
                return  "$ "+b.substring(0,6);
            }
            return "$ "+b;
        }
    }
    private void havuzaKatilanlar(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("havuzkontrol").child("havuzakatilanlar").child(firebaseTarih);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                katilansayisi =(int) dataSnapshot.getChildrenCount();
                _txtkatilimsayisi.setText(String.valueOf(katilansayisi));
                havuzdakiCoin = cekilisDegeri*katilansayisi;
                _txthavuzcoin.setText(String.valueOf(yuzde20al(havuzdakiCoin)));
                if (havuzdakiCoin>100){
                    _havuztl.setText(puanParaCevir(_txtyatirkatilmasayiniz.getText().toString(),gelendilturkecemi));
                } else {
                    _havuztl.setText("0.001");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void havuzCekilisCek(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("havuzkontrol").child("havuzcekilis");
        databaseReference.addValueEventListener(new ValueEventListener() {
            MHavuzBilgi mHavuzBilgi;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mHavuzBilgi = dataSnapshot.getValue(MHavuzBilgi.class);
                if (mHavuzBilgi!=null){
                    firebaseTarih = mHavuzBilgi.getDate();
                    _txtcekilistarih.setText(firebaseTarih);
                    firebaseLink = mHavuzBilgi.getLink();
                    cekilisDegeri = mHavuzBilgi.getPuandegeri();
                    _eklenececoin.setText(String.valueOf(cekilisDegeri));
                    if (mHavuzBilgi.getGosterim()==1){
                        _lytcekilissonucu.setVisibility(View.INVISIBLE);
                        _lytcekilisekatil.setVisibility(View.VISIBLE);
                    } else {
                        _lytcekilissonucu.setVisibility(View.VISIBLE);
                        _lytcekilisekatil.setVisibility(View.INVISIBLE);
                    }
                    havuzaKatilanlar();
                    userKatilimSayisiCek();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private int yuzde20al(int a){
        int b =(int) (a*0.2);
        return a-b;
    }
    private void userCoinCek(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user").child(gelenid)
                .child("puan");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userCoin = Integer.valueOf(String.valueOf(dataSnapshot.getValue()));
                //Toast.makeText(getApplicationContext(),userCoin+" coininiz var",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void cekiliseKatil(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("havuzkontrol").child("havuzakatilanlar").child(firebaseTarih);
        String key = databaseReference.push().getKey();
        HashMap<String,Object> map = new HashMap<>();
        map.put("date",gelentarih);
        map.put("mail",gelenmail);
        map.put("id",gelenid);
        map.put("oncekipuan",userCoin);
        assert key != null;
        databaseReference.child(key).setValue(map);
        userCoinDus();
    }
    private void userCoinDus(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user").child(gelenid)
                .child("puan");
        databaseReference.setValue(userCoin-cekilisDegeri);
    }
    private void userKatilimSayisiCek(){
        Query query = FirebaseDatabase.getInstance().getReference("havuzkontrol").child("havuzakatilanlar").child(firebaseTarih).orderByChild("id").equalTo(gelenid);;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userkatilimsayisi = Integer.parseInt(String.valueOf(dataSnapshot.getChildrenCount()));
                _txtuserkatilimsayisi.setText(String.valueOf(userkatilimsayisi));
                cikmaSansiCek();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void cikmaSansi(int _userkatilimsayisi,int _katilansayisi){
        if (_userkatilimsayisi>0 && _katilansayisi>0){
            double a = (double)100*_userkatilimsayisi/_katilansayisi;
            String strsans = String.valueOf(a);
            if (strsans.length()>4){
                _txtusercikmaolasiligi.setText(String.valueOf(a).substring(0,4));
            } else {
                _txtusercikmaolasiligi.setText(strsans);
            }

        } else {
            _txtusercikmaolasiligi.setText("0");
        }

    }
    private void cikmaSansiCek(){
        new CountDownTimer(1300,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                cikmaSansi(userkatilimsayisi,katilansayisi);
            }
        }.start();
    }
    private void girisDialog(String a){
        dialog = new SpotsDialog.Builder().setContext(this)
                .setMessage(a)
                .setCancelable(false)
                .setTheme(R.style.Custom)
                .build();
        dialog.show();
    }
    private void bilgi(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Referans Nedir?");
        builder.setMessage(getResources().getString(R.string.yatirkazannedir)+"\n"+getResources().getString(R.string.heruyebir)+"\n" +
                getResources().getString(R.string.birikenpuan));
        builder.setCancelable(false);
    /*builder.setNegativeButton("İPTAL", new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog, int id) {

        //İptal butonuna basılınca yapılacaklar.Sadece kapanması isteniyorsa boş bırakılacak

      }
    }); */
        builder.setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.show();
    }
    private void cekiliseKatildiktanSonraBeklet(){
        girisDialog("Biraz bekleyin...");
        new CountDownTimer(2600,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"Katılım gerçekleşti.",Toast.LENGTH_SHORT).show();
            }
        }.start();
    }
    protected boolean internetKonrol() {
        // TODO Auto-generated method stub
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
    private void status(String status){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user").child(gelenid);
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
