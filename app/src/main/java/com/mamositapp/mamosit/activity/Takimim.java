package com.mamositapp.mamosit.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mamositapp.mamosit.R;
import com.mamositapp.mamosit.adap.AdapTakimim;
import com.mamositapp.mamosit.model.MReferensPersonel;
import com.mamositapp.mamosit.model.MTakimim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import dmax.dialog.SpotsDialog;

public class Takimim extends AppCompatActivity {
    RecyclerView myRecycler;
    ArrayList<MTakimim> list;
    AdapTakimim adapter;
    Context context = this;
    String gelenuserid;
    TextView _txttakimimyok;
    int kasadakipuan,gercekpuan,gecmistoplamkasa;
    TextView _txtKasacoin,_txtanapuan;
    Button _btntakimimbilgi;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takimim);
        gelenuserid = getIntent().getStringExtra(getResources().getString(R.string.userid));
        myRecycler = findViewById(R.id.myRecycler);
        _txtKasacoin = findViewById(R.id.txtKasacoin);
        _btntakimimbilgi= findViewById(R.id.btntakimimbilgi);
        _txtanapuan = findViewById(R.id.txtanapuan);
        myRecycler.setLayoutManager(new LinearLayoutManager(context));
        _txttakimimyok = findViewById(R.id.txttakimimyok);
        userBilgiCek();
        list = new ArrayList<MTakimim>();
        listele();
        _btntakimimbilgi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bilgi();
            }
        });
    }
    private void listele(){
        girisDialog(getResources().getString(R.string.bekleyiniz));
        Query query = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).orderByChild(getResources().getString(R.string.kimgetirdi)).equalTo(gelenuserid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            MTakimim mTakimim;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    if (dataSnapshot1.exists()){
                        mTakimim = dataSnapshot1.getValue(MTakimim.class);
                        assert mTakimim != null;
                        list.add(mTakimim);
                    }
                }
                adapter = new AdapTakimim(context,list);
                myRecycler.setAdapter(adapter);
                dialog.dismiss();
                if (list.size()>0){
                    _txttakimimyok.setVisibility(View.INVISIBLE);
                }
                kasayiKontrolEt();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void kasayiKontrolEt(){
        if (kasadakipuan>=1000){
            girisDialog(getResources().getString(R.string.kasadakileraktariliyor));
            Random random = new Random();
            int rnd = (int) random.nextInt(4)+2;
            degistir(rnd);
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
    private void degistir(int gelenzaman){
        new CountDownTimer(gelenzaman*1000,1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                kasaSifirla();
                puanEkle();
                gecmisKasayaEkle();
                dialog.dismiss();
            }
        }.start();
    }

    private void puanEkle() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(gelenuserid).child(getResources().getString(R.string.puan));
        reference.setValue(gercekpuan+kasadakipuan);
        _txtanapuan.setText(String.valueOf(gercekpuan+kasadakipuan));
    }
    private void gecmisKasayaEkle() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(gelenuserid).child(getResources().getString(R.string.gecmistoplamkasa));
        reference.setValue(gecmistoplamkasa+kasadakipuan);
    }
    private void kasaSifirla() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(gelenuserid).child(getResources().getString(R.string.kasa));
        reference.setValue(0);
        _txtKasacoin.setText("0");
    }
    private void userBilgiCek(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(gelenuserid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            MReferensPersonel mReferensPersonel;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mReferensPersonel = dataSnapshot.getValue(MReferensPersonel.class);
                if (mReferensPersonel!=null){
                    kasadakipuan = mReferensPersonel.getKasa();
                    gercekpuan = mReferensPersonel.getPuan();
                    gecmistoplamkasa = mReferensPersonel.getGecmistoplamkasa();
                    _txtKasacoin.setText(String.valueOf(kasadakipuan));
                    _txtanapuan.setText(String.valueOf(gercekpuan));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void bilgi(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Referans Nedir?");
        builder.setMessage(getResources().getString(R.string.kasapuaninedir)+"\n"+getResources().getString(R.string.referanslarininsana)+"\n" +
                getResources().getString(R.string.kasadakipuna));
        builder.setCancelable(false);
    /*builder.setNegativeButton("İPTAL", new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog, int id) {

        //İptal butonuna basılınca yapılacaklar.Sadece kapanması isteniyorsa boş bırakılacak

      }
    }); */
        builder.setPositiveButton(getResources().getString(R.string.tamam), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.show();
    }
    private void status(String status){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(gelenuserid);
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
