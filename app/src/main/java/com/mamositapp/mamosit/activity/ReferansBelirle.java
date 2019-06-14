package com.mamositapp.mamosit.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mamositapp.mamosit.R;
import com.mamositapp.mamosit.model.User;

import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class ReferansBelirle extends AppCompatActivity {
    @BindView(R.id.imgreferanslogo)
    ImageView _imgreferanslogo;
    @BindView(R.id.lytreferanswrap)
    LinearLayout _lytreferanswrap;
    @BindView(R.id.edtnickname)
    EditText _edtnickname;
    @BindView(R.id.btnnicknameonay)
    Button _btnnicknameonay;
    Context context = this;
    AlertDialog dialog;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    SharedPreferences sharedPreferences;
    String time;
    String gelenReferansNo,gelenadres;
    Animation anim1,anim2;
    boolean nickvarmi = true;
    int gelenPuan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referans_belirle);
        ButterKnife.bind(this);
        sharedPreferences = this.getSharedPreferences(getResources().getString(R.string.zaman),Context.MODE_PRIVATE);
        time = sharedPreferences.getString(getResources().getString(R.string.sitezaman),"-");
        gelenReferansNo = getIntent().getStringExtra(getResources().getString(R.string.referansno));
        gelenPuan = getIntent().getIntExtra(getResources().getString(R.string.baslangicpuan),0);
        gelenadres = getIntent().getStringExtra(getResources().getString(R.string.adres));
        anim();
    }
    @OnClick(R.id.btnnicknameonay) void btnnickonayClick(){
        girisDialog();
        if (nickyaziKontrol()){
            nickVarmiKontrol();
        }
    }
    private void uyelikcek(){
        //user oturum açmışmı başlangıç
        if (user != null) {
            profilCek();
        } else {
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.girisbasarisiz),Toast.LENGTH_LONG).show();
        }
        //user oturum açmışmı bitiş
    }

    private void profilCek() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).child(user.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            User user;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (user!=null){
                    if (!user.getUser_name().equals("-")){
                        startActivity(new Intent(context,Home.class));
                        finish();
                    } else {
                        _edtnickname.setHint("nick_name");
                        dialog.dismiss();
                    }
                } else {
                    profilCreat();
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void profilCreat(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(getResources().getString(R.string.user)).child(user.getUid());
        HashMap<String,Object> m = new HashMap<>();
        if (refaransKontrol(gelenReferansNo).length()>3){
            m.put(getResources().getString(R.string.puan),gelenPuan);
        } else {
            m.put(getResources().getString(R.string.puan),0);
        }
        m.put(getResources().getString(R.string.kasa),0);
        m.put(getResources().getString(R.string.name),user.getDisplayName());
        m.put(getResources().getString(R.string.ban),0);
        m.put(getResources().getString(R.string.kimgetirdi),refaransKontrol(gelenReferansNo));
        m.put(getResources().getString(R.string.mail),user.getEmail());
        m.put(getResources().getString(R.string.songiris),"-");
        m.put(getResources().getString(R.string.user_name),"-");
        m.put(getResources().getString(R.string.refverdigitoplampuan),0);
        m.put(getResources().getString(R.string.adres),gelenadres);
        m.put(getResources().getString(R.string.useretkinlik),1);
        m.put(getResources().getString(R.string.status),"off");
        m.put(getResources().getString(R.string.google_id),user.getUid());
        m.put(getResources().getString(R.string.gecmistoplamkasa),0);
        databaseReference.setValue(m);
    }
    private void nicknamekayit(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(getResources().getString(R.string.user)).child(user.getUid()).child(getResources().getString(R.string.user_name));
        databaseReference.setValue(_edtnickname.getText().toString());
    }
    private void girisDialog(){
        dialog = new SpotsDialog.Builder().setContext(context)
                .setMessage(getResources().getString(R.string.bekleyiniz))
                .setCancelable(false)
                .setTheme(R.style.Custom)
                .build();
        dialog.show();
    }
    private void anim(){
        anim1 =  AnimationUtils.loadAnimation(this,R.anim.fromtop1);
        anim2 =  AnimationUtils.loadAnimation(this,R.anim.frombottom);
        _lytreferanswrap.startAnimation(anim2);
        _imgreferanslogo.startAnimation(anim1);
        anim1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                girisDialog();
                uyelikcek();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private boolean nickyaziKontrol(){
        String a = _edtnickname.getText().toString();
        int len = a.length();
        if (len>3){
            if (!a.contains(" ")){
                if(len<12){
                    return true;
                } else {
                    Toast.makeText(context,getResources().getString(R.string.onbirkaraktergecmemeli),Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context,getResources().getString(R.string.boslukicermemeli),Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context,getResources().getString(R.string.dortkarakterolmali),Toast.LENGTH_SHORT).show();
        }
        dialog.dismiss();
        return false;
    }
    private String refaransKontrol(String a){
        int len = a.length();
        if (len>3){
            if (!a.contains(" ")){
                return a;
            }
        }
        return "-";
    }
    private void nickVarmiKontrol(){
        Query query = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).orderByChild(getResources().getString(R.string.user_name)).equalTo(_edtnickname.getText().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            User user;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                nickvarmi = user != null;
                timeStart();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void timeStart(){
        new CountDownTimer(2000, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if (nickvarmi) {
                    Toast.makeText(context, getResources().getString(R.string.boylenickvar), Toast.LENGTH_SHORT).show();
                } else {
                    nicknamekayit();
                    Intent intent = new Intent(getApplicationContext(),Home.class);
                    startActivity(intent);
                    finish();
                }
                dialog.dismiss();
            }
        }.start();
    }
}
