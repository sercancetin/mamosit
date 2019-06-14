package com.mamositapp.mamosit.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mamositapp.mamosit.R;
import com.mamositapp.mamosit.model.MOdemeLimit;
import com.mamositapp.mamosit.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class OdemeAl extends AppCompatActivity {
    TextView _txtodemecumle;
    private InterstitialAd mInterstitialAd;
    AdView mAdView;
    String gelenid,gelentarih,strpara,usermail,useradres;
    boolean gelendilturkecemi;
    float[] gelenParaDizi;
    private Dialog dialog;
    int userPuan,banka,bitcoin,googleplay,ininal,kontor,papara;
    int userparadeger;
    MediaPlayer media,tick;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    //odeme popup
    EditText _edtodemeisim,_edtodemetel,_edtodemehesapno,_edtodemeozelnot;
    TextView _txtodemetur;
    Button _btnodemeonay;
    //odeme popup bitiş
    Dialog odemepopup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odeme_al);
        ButterKnife.bind(this);
        gelenid = getIntent().getStringExtra(getResources().getString(R.string.userid));
        gelendilturkecemi = getIntent().getBooleanExtra(getResources().getString(R.string.dil),true);
        gelenParaDizi = getIntent().getFloatArrayExtra(getResources().getString(R.string.paracevirdizi));
        gelentarih = getIntent().getStringExtra(getResources().getString(R.string.tarih));
        _txtodemecumle = findViewById(R.id.txtodemecumle);
        media = MediaPlayer.create(this,R.raw.cash);
        tick = MediaPlayer.create(this,R.raw.tick);
        paraSinirCek();
        //reklam başlangıç
        MobileAds.initialize(this, getResources().getString(R.string.admobappid));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.carkcevirgecisreklamid));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mAdView = findViewById(R.id.odemealadview);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //reklam bitiş

    }
    //<<<<<<<<<<<<<<<<<<<<<< Tıklamalar >>>>>>>>>>>>>>>>>>
    @OnClick(R.id.crdbank) void crdbankClick(){
        tick.start();
        if (wifiControl()){
            if (userparadeger >=banka){
                odemePopup(getResources().getString(R.string.banka));
            } else {
                Toast.makeText(this,getResources().getString(R.string.yetersizbakiye) +banka,Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this,getResources().getString(R.string.internetbaglantihatasi),Toast.LENGTH_SHORT).show();
        }
    }
    @OnClick(R.id.crdpapara) void crdpaparaClick(){
        tick.start();
        if (wifiControl()){
            if (userparadeger >=papara){
                odemePopup(getResources().getString(R.string.papara));
            } else {
                Toast.makeText(this,getResources().getString(R.string.yetersizbakiye) +papara,Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this,getResources().getString(R.string.internetbaglantihatasi),Toast.LENGTH_SHORT).show();
        }
    }
    @OnClick(R.id.crdininal) void crdininalClick(){
        tick.start();
        if (wifiControl()){
            if (userparadeger >=ininal){
                odemePopup(getResources().getString(R.string.ininal));
            } else {
                Toast.makeText(this,getResources().getString(R.string.yetersizbakiye) +ininal,Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this,getResources().getString(R.string.internetbaglantihatasi),Toast.LENGTH_SHORT).show();
        }
    }
    @OnClick(R.id.crdgooglekart) void crdgooglekartClick(){
        tick.start();
        if (wifiControl()){
            if (userparadeger >=googleplay){
                odemePopup(getResources().getString(R.string.googleplay));
            } else {
                Toast.makeText(this,getResources().getString(R.string.yetersizbakiye) +googleplay,Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this,getResources().getString(R.string.internetbaglantihatasi),Toast.LENGTH_SHORT).show();
        }
    }
    @OnClick(R.id.crdceptl) void crdceptlClick(){
        tick.start();
        if (wifiControl()){
            if (userparadeger >=kontor){
                odemePopup(getResources().getString(R.string.kontor));
            } else {
                Toast.makeText(this,getResources().getString(R.string.yetersizbakiye) +kontor,Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this,getResources().getString(R.string.internetbaglantihatasi),Toast.LENGTH_SHORT).show();
        }
    }
    @OnClick(R.id.crdbtc) void crdbtcClick(){
        tick.start();
        if (wifiControl()){
            if (userparadeger >=bitcoin){
                odemePopup(getResources().getString(R.string.bitcoin));
            } else {
                Toast.makeText(this,getResources().getString(R.string.yetersizbakiye) +bitcoin,Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this,getResources().getString(R.string.internetbaglantihatasi),Toast.LENGTH_SHORT).show();
        }
    }
    //<<<<<<<<<<<<<<<<<<<<<< Tıklamalar Bitiş >>>>>>>>>>>>>>>>>>

    private void paraSinirCek(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.oyunkontrol)).child(getResources().getString(R.string.odemesinir));
        reference.addValueEventListener(new ValueEventListener() {
            MOdemeLimit mOdemeLimit;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                girisDialog(getResources().getString(R.string.bekleyiniz));
                mOdemeLimit = dataSnapshot.getValue(MOdemeLimit.class);
                if (mOdemeLimit!=null){
                    banka=mOdemeLimit.getBanka();
                    bitcoin = mOdemeLimit.getBitcoin();
                    googleplay = mOdemeLimit.getGoogleplay();
                    ininal = mOdemeLimit.getIninal();
                    kontor = mOdemeLimit.getKontor();
                    papara = mOdemeLimit.getPapara();
                    profilcek();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                    usermail = user.getMail();
                    useradres = user.getAdres();
                    strpara = puanParaCevir(_txtodemecumle.getText().toString(),gelendilturkecemi);
                    userparadeger = (int) Float.parseFloat(strpara);
                    //Toast.makeText(getApplicationContext(),strpara,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dialog.dismiss();
    }
    private String  puanParaCevir(String a, boolean c){
        if (a.equals("Para Alma Yönteminizi Seçin")|| c){
            String b = String.valueOf(userPuan*gelenParaDizi[1]/gelenParaDizi[0]);
            if (b.length()>5){
                return  ""+b.substring(0,5);
            }
            return ""+b;
        } else {
            String b = String.valueOf(userPuan*gelenParaDizi[3]/gelenParaDizi[2]);
            if (b.length()>5){
                return  ""+b.substring(0,5);
            }
            return ""+b;
        }
    }
    private void userPuanDus(String odemeturu){
        gelenTarihVarmi();
        kayitOlustur(odemeturu);
        int a = (int) sinirPuanCevir(odemeturu);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(gelenid).child(getResources().getString(R.string.puan));
        reference.setValue(userPuan-a);
        Toast.makeText(getApplicationContext(),getResources().getString(R.string.siparisgerceklesti),Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
    private float sinirPuanCevir(String odemeturu) {
        switch (odemeturu) {
            case "banka":
                if (_txtodemecumle.getText().toString().equals("Para Alma Yönteminizi Seçin") || gelendilturkecemi) {
                    return gelenParaDizi[0] * banka / gelenParaDizi[1];
                } else {
                    return gelenParaDizi[2] * banka / gelenParaDizi[3];
                }
            case "bitcoin":
                if (_txtodemecumle.getText().toString().equals("Para Alma Yönteminizi Seçin") || gelendilturkecemi) {
                    return gelenParaDizi[0] * bitcoin / gelenParaDizi[1];
                } else {
                    return gelenParaDizi[2] * bitcoin / gelenParaDizi[3];
                }
            case "papara":
                if (_txtodemecumle.getText().toString().equals("Para Alma Yönteminizi Seçin") || gelendilturkecemi) {
                    return gelenParaDizi[0] * papara / gelenParaDizi[1];
                } else {
                    return gelenParaDizi[2] * papara / gelenParaDizi[3];
                }
            case "ininal":
                if (_txtodemecumle.getText().toString().equals("Para Alma Yönteminizi Seçin") || gelendilturkecemi) {
                    return gelenParaDizi[0] * ininal / gelenParaDizi[1];
                } else {
                    return gelenParaDizi[2] * ininal / gelenParaDizi[3];
                }
            case "kontor":
                if (_txtodemecumle.getText().toString().equals("Para Alma Yönteminizi Seçin") || gelendilturkecemi) {
                    return gelenParaDizi[0] * kontor / gelenParaDizi[1];
                } else {
                    return gelenParaDizi[2] * kontor / gelenParaDizi[3];
                }
            default:
                if (_txtodemecumle.getText().toString().equals("Para Alma Yönteminizi Seçin") || gelendilturkecemi) {
                    return gelenParaDizi[0] * googleplay / gelenParaDizi[1];
                } else {
                    return gelenParaDizi[2] * googleplay / gelenParaDizi[3];
                }
        }
    }
    private void kayitOlustur(String odemeturu) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.siparisler));
        String key = reference.push().getKey();
        HashMap<String,Object> m = new HashMap<>();
        m.put("gonderilecekhesap",topluHesapYaz(odemeturu));
        m.put("tarih",gelentarih);
        m.put("usermail",user.getEmail());
        m.put("username",user.getDisplayName());
        m.put("userid",user.getUid());
        m.put("kanitimg","-");
        m.put("odendigitarih","-");
        m.put("durum",0);
        m.put("oncekipuan",userPuan);
        m.put("fiyat",hesapTurPara(odemeturu));
        assert key != null;
        reference.child(key).setValue(m);
    }
    private String topluHesapYaz(String tur){
        int hesappara = hesapTurPara(tur);
        return "Hesap Türü: "+tur+"-Odenecek Para:"+hesappara+" Adres:"+ useradres+"- Telefon No: "+
                _edtodemetel.getText().toString()+"- Gönderilecek kişi: "+_edtodemeisim.getText().toString()+
                "Hesap No:"+_edtodemehesapno.getText().toString()+"- Özel Not: "+_edtodemeozelnot.getText().toString();
    }
    private int hesapTurPara(String odemeturu){
        switch (odemeturu){
            case "banka":
                return banka;
            case "bitcoin":
                return bitcoin;
            case "ininal":
                return ininal;
            case "papara":
                return papara;
            case "kontor":
                return kontor;
            default:
                return googleplay;
        }
    }
    private void girisDialog(String a){
        dialog = new SpotsDialog.Builder().setContext(this)
                .setMessage(a)
                .setCancelable(false)
                .setTheme(R.style.Custom)
                .build();
        dialog.show();
    }
    protected boolean wifiControl() {
        // TODO Auto-generated method stub
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    private void odemePopup(final String odemeturu){
        odemepopup = new Dialog(this);
        odemepopup.setContentView(R.layout.odemepopup);
        _txtodemetur = odemepopup.findViewById(R.id.txtodemetur);
        _edtodemeisim = odemepopup.findViewById(R.id.edtodemeisim);
        _edtodemetel = odemepopup.findViewById(R.id.edtodemetel);
        _edtodemehesapno = odemepopup.findViewById(R.id.edtodemehesapno);
        _edtodemeozelnot = odemepopup.findViewById(R.id.edtodemeozelnot);
        _btnodemeonay = odemepopup.findViewById(R.id.btnodemeonay);
        _txtodemetur.setText(odemeturu);
        _btnodemeonay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wifiControl()){
                    if (odemeturu.equals(getResources().getString(R.string.kontor))){
                        if(_edtodemeisim.getText().toString().length()>3||_edtodemetel.getText().toString().length()>7){
                            siparisDialog(odemeturu);
                        } else {
                            Toast.makeText(OdemeAl.this,getResources().getString(R.string.herhangibirindeeksikvar),Toast.LENGTH_SHORT).show();
                        }
                    } else if(_edtodemeisim.getText().toString().length()>3||_edtodemehesapno.getText().toString().length()>6){
                        siparisDialog(odemeturu);
                    } else {
                        Toast.makeText(OdemeAl.this,getResources().getString(R.string.herhangibirindeeksikvar),Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OdemeAl.this,getResources().getString(R.string.internetbaglantihatasi),Toast.LENGTH_SHORT).show();
                }
            }
        });
        Objects.requireNonNull(odemepopup.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        odemepopup.show();
    }
    private void siparisDialog(final String tur){
        final AlertDialog.Builder builder = new AlertDialog.Builder(OdemeAl.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
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
                timeStart();
                userPuanDus(tur);
                odemepopup.dismiss();
            }
        });
        builder.show();
    }
    private void gelenTarihVarmi(){
        if (gelentarih.length()<3){
            @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("d MMM yyyy, HH:mm");
            gelentarih = df.format(Calendar.getInstance().getTime());
        }
    }
    private void timeStart(){
        girisDialog(getResources().getString(R.string.sipariskontrolediliyor));
        new CountDownTimer(3700, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                dialog.dismiss();
                if(mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                    mInterstitialAd.setAdListener(new AdListener(){
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            Toast.makeText(getApplicationContext(),getResources().getString(R.string.siparisgerceklesti),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }.start();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
