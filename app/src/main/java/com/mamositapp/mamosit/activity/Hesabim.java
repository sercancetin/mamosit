package com.mamositapp.mamosit.activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mamositapp.mamosit.R;
import com.mamositapp.mamosit.model.MHesapFirebase;
import com.mamositapp.mamosit.model.User;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class Hesabim extends AppCompatActivity {
    @BindView(R.id.oturumisim)
    TextView _oturumisim;
    @BindView(R.id.profilimgoturum)
    ImageView _profilimgoturum;
    @BindView(R.id.txthesabimpuan)
    TextView _txthesabimpuan;
    @BindView(R.id.txthesabimhesabbakiye)
    TextView _txthesabimhesabbakiye;
    @BindView(R.id.txthesabimrefadi)
    TextView _txthesabimrefadi;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Dialog dialog;
    String gelenPara,contactmail;
    int refpuan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hesabim);
        ButterKnife.bind(this);
        gelenPara = getIntent().getStringExtra(getResources().getString(R.string.para));
        gelenleriYerlestir();
        fireBilgi();
    }
    /*<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Tıklamalar >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */
    @OnClick(R.id.rlthesaptakim) void rlthesaptakimClick(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        // i.setPackage("com.whatsapp"); whatsapp için
        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.mamositapp));
        intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.refgonder1)+refpuan+getResources().getString(R.string.refgonder2)+"\n" +
                getResources().getString(R.string.refgonder3)+ _txthesabimrefadi.getText().toString()
                +getResources().getString(R.string.refgonder4)+"\n"+" "+getResources().getString(R.string.appplaylinkuzun));
        startActivity(Intent.createChooser(intent, getResources().getString(R.string.refgonder5)));
    }
    @OnClick(R.id.lythesapoyla) void lythesapoylaClick(){
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.appplaykisalink))));
        }catch (ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.appplaylinkuzun))));
        }
    }
    @OnClick(R.id.rlthesapmesaj) void rlthesapmesajClick(){
        try {
            Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse(getResources().getString(R.string.mailto) + contactmail));
            intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.mamosithakkinda));
            intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.kullanici)+user.getUid()+"\n"+ getResources().getString(R.string.adi)+user.getDisplayName()+"\n"+getResources().getString(R.string.telmodel)+ Build.MODEL);
            startActivity(intent);
        }catch (ActivityNotFoundException e){

        }
    }


    /*<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Tıklamalar Bitiş >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
    private void gelenleriYerlestir(){
        girisDialog();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(user.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            User user1;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user1 = dataSnapshot.getValue(User.class);
                if (user1!=null){
                    _oturumisim.setText(user1.getMail());
                    Picasso.get().load(user.getPhotoUrl()).into(_profilimgoturum);
                    _txthesabimpuan.setText(String.valueOf(user1.getPuan()));
                    _txthesabimrefadi.setText(user1.getUser_name());
                    _txthesabimhesabbakiye.setText(gelenPara);
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void girisDialog(){
        dialog = new SpotsDialog.Builder().setContext(this)
                .setMessage(getResources().getString(R.string.bekleyiniz))
                .setCancelable(false)
                .setTheme(R.style.Custom)
                .build();
        dialog.show();
    }
    private void fireBilgi(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.oyunkontrol)).child(getResources().getString(R.string.tekliveriler));
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            MHesapFirebase mHesapFirebase;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mHesapFirebase = dataSnapshot.getValue(MHesapFirebase.class);
                if (mHesapFirebase!=null){
                    contactmail = mHesapFirebase.getContactmail();
                    refpuan = mHesapFirebase.getReferansbaslarkenkipuan();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
