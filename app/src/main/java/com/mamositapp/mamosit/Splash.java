package com.mamositapp.mamosit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mamositapp.mamosit.activity.Home;
import com.mamositapp.mamosit.activity.ReferansBelirle;
import com.mamositapp.mamosit.activity.Register;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class Splash extends AppCompatActivity {
    @BindView(R.id.splashicon)
    ImageView _splashicon;
    Animation animationZ;
    FirebaseUser user;
    CountDownTimer countDownTimer;
    SharedPreferences zamantutucu;
    SharedPreferences.Editor zamantutueditor;
    String tarihcekjsoup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        animationZ = AnimationUtils.loadAnimation(this,R.anim.zoomin);
        _splashicon.startAnimation(animationZ);
        user = FirebaseAuth.getInstance().getCurrentUser();
        zamantutucu = this.getSharedPreferences("zaman",Context.MODE_PRIVATE);
        if (wifiControl()){
            new TarihGetir().execute();
        } else {
            wifiDialog();
        }
    }
    protected boolean wifiControl() {
        // TODO Auto-generated method stub
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    private void timeStart(){
        countDownTimer = new CountDownTimer(2000, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if (user != null) {
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), Register.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                }
            }
        }.start();
    }
    private void wifiDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
    private class TarihGetir extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            zamantutueditor = zamantutucu.edit();
            zamantutueditor.putString("sitezaman",tarihcekjsoup);
            zamantutueditor.apply();
            //Toast.makeText(Splash.this,zamantutucu.getString("sitezaman","-") + "a",Toast.LENGTH_SHORT).show();
            timeStart();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(getResources().getString(R.string.sitetarihadres)).timeout(30*1000).get();
                Elements _tarih = doc.select("span");
                tarihcekjsoup = _tarih.get(0).text();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }
    }

}
