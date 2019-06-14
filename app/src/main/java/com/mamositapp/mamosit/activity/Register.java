package com.mamositapp.mamosit.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mamositapp.mamosit.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mamositapp.mamosit.model.User;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class Register extends AppCompatActivity {
    @BindView(R.id.registericon)
            ImageView _registericon;
    @BindView(R.id.btnsignin)
            SignInButton _btnsignin;
    @BindView(R.id.txtgizlilik)
            TextView _txtgizlilik;
    @BindView(R.id.edtregisterreferans)
            EditText _edtregisterreferans;
    @BindView(R.id.registerreftanitim)
            Button _registerreftanitim;
    @BindView(R.id.btnregisterkonum)
            Button _btnregisterkonum;
    Animation animationZ;
    Animation as;
    FirebaseAuth auth;
    FirebaseUser user;
    private int RC_SIGN_IN=1;
    GoogleSignInClient mGoogleSignInClient;
    private boolean nickvarmi;
    private android.app.AlertDialog dialog;
    private String yazilannick,referansinidsi;
    int refbaslangicpuan = 0;
    //Konum Bilgisi alma
    LocationManager locationManager;
    String lattitude,longitude;
    Geocoder geocoder;
    List<Address> addresses;
    private String fulladress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        //konum
        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);
        geocoder = new Geocoder(this, Locale.getDefault());
        //konumbitis
        _txtgizlilik.setPaintFlags(_txtgizlilik.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        animSetup();
        auth = FirebaseAuth.getInstance();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.googlegirisid))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        refBaslangicPuanCek();
        _btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_edtregisterreferans.getText().toString().length()>0){
                    girisDialog(getResources().getString(R.string.refkontrolediliyor));
                    if (nickyaziKontrol()){
                        nickVarmiKontrol();
                        timeStart();
                    }
                } else {
                    //yazilannick = "-";
                    referansinidsi = "-";
                    sigin();
                }
            }
        });
    }
    @OnClick(R.id.txtgizlilik) void txtgizliliClick(){
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.gizlilik))));
        }catch (ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.gizlilik))));
        }
    }
    @OnClick(R.id.registerreftanitim) void registerreftanitimClick(){
        refBilgi();
    }
    @OnClick(R.id.btnregisterkonum) void btnregisterkonumClick(){
        locationControl();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately

                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent i = new Intent(getApplicationContext(),ReferansBelirle.class);
                            i.putExtra(getResources().getString(R.string.referansno),referansinidsi);
                            i.putExtra(getResources().getString(R.string.baslangicpuan),refbaslangicpuan);
                            i.putExtra(getResources().getString(R.string.adres),fulladress);
                            startActivity(i);
                            Toast.makeText(getApplicationContext(),getResources().getString(R.string.girisbasarili),Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(),getResources().getString(R.string.girisbasarisiz), Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });
    }
    void animSetup(){
        animationZ = AnimationUtils.loadAnimation(this,R.anim.fromtop1);
        //as = AnimationUtils.loadAnimation(this,R.anim.lefttoright);
        _registericon.startAnimation(animationZ);
        //_btnsignin.startAnimation(as);
    }
    void sigin(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void refBilgi(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Referans Nedir?");
        builder.setMessage(getResources().getString(R.string.arkadasininbirhediyesivar)
        +String.valueOf(refbaslangicpuan));
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
    private void nickVarmiKontrol(){
        Query query = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.user)).orderByChild(getResources().getString(R.string.user_name)).equalTo(yazilannick);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            User user;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    user = dataSnapshot1.getValue(User.class);
                    if (user!=null){
                        nickvarmi = true;
                        referansinidsi = user.getGoogle_id();
                        if (referansinidsi.length()<5){
                            referansinidsi="-";
                        }
                    } else {
                        nickvarmi = false;
                        Toast.makeText(getApplicationContext(),"Hata: R100",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private boolean nickyaziKontrol(){
        yazilannick = _edtregisterreferans.getText().toString();
        int len = yazilannick.length();
        if (len>3){
            if (!yazilannick.contains(" ")){
                if(len<12){
                    return true;
                } else {
                    Toast.makeText(this,getResources().getString(R.string.onbirkaraktergecmemeli),Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this,getResources().getString(R.string.boslukicermemeli),Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this,getResources().getString(R.string.dortkarakterolmali),Toast.LENGTH_SHORT).show();
        }
        dialog.dismiss();
        return false;
    }
    private void girisDialog(String a){
        dialog = new SpotsDialog.Builder().setContext(this)
                .setMessage(a)
                .setCancelable(false)
                .setTheme(R.style.Custom)
                .build();
        dialog.show();
    }
    private void timeStart(){
        new CountDownTimer(4000, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if (nickvarmi){
                    sigin();
                } else {
                    Toast.makeText(getApplicationContext(),"Böyle bir referans yok",Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
        }.start();
    }
    private void refBaslangicPuanCek(){
        girisDialog(getResources().getString(R.string.bekleyiniz));
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.oyunkontrol)).child(getResources().getString(R.string.tekliveriler))
                .child(getResources().getString(R.string.referansbaslarkenkipuan));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                refbaslangicpuan = Integer.valueOf(String.valueOf(dataSnapshot.getValue()));
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void locationControl(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            buildAlertMessageNoGps();
        } else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            getLocation();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null){
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                //Toast.makeText(this,lattitude + " "+ longitude,Toast.LENGTH_SHORT).show();
                adresCek(latti,longi);
            } else {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(getResources().getString(R.string.googlemapapp));
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                } else {
                    Intent launchIntent1 = getPackageManager().getLaunchIntentForPackage(getResources().getString(R.string.yandexmapapp));
                    startActivity(launchIntent1);//null pointer check in case package name was not found
                }
                Toast.makeText(this,getResources().getString(R.string.konumbilgisialinamadi),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void buildAlertMessageNoGps() {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Register.this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setTitle(getResources().getString(R.string.gpsbaglantisi));
        builder.setMessage(getResources().getString(R.string.lütfengpsbaglantisi));
        builder.setCancelable(false);
        builder.setNegativeButton(getResources().getString(R.string.hayir), new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.evet), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.show();
    }
    private void adresCek(Double a,Double b){
        try {
            addresses = geocoder.getFromLocation(a,b,1);
            String address = addresses.get(0).getAddressLine(0);
            //String city = addresses.get(0).getAdminArea();
            String  country = addresses.get(0).getCountryName();
            //String postalcode = addresses.get(0).getPostalCode();
            fulladress = address+"-"+country;
            //Toast.makeText(this,fulladress,Toast.LENGTH_SHORT).show();
            _btnregisterkonum.setVisibility(View.INVISIBLE);
            _btnsignin.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
