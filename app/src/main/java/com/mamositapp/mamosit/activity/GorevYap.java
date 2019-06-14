package com.mamositapp.mamosit.activity;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mamositapp.mamosit.R;
import com.mamositapp.mamosit.adap.AdapGorev;
import com.mamositapp.mamosit.adap.AdapTakimim;
import com.mamositapp.mamosit.model.MGorev;
import com.mamositapp.mamosit.model.MTakimim;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class GorevYap extends AppCompatActivity {
    RecyclerView rcygorev;
    ArrayList<MGorev> list;
    AdapGorev adapter;
    Context context = this;
    Dialog dialog;
    TextView txtgorevyok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gorev_yap);
        rcygorev = findViewById(R.id.rcygorev);
        txtgorevyok = findViewById(R.id.txtgorevyok);
        rcygorev.setLayoutManager(new LinearLayoutManager(context));
        list = new ArrayList<MGorev>();
        listele();

    }
    private void listele(){
        girisDialog(getResources().getString(R.string.bekleyiniz));
        Query query = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.gorevler));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            MGorev mGorev;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    if (dataSnapshot1.exists()){
                        mGorev = dataSnapshot1.getValue(MGorev.class);
                        assert mGorev != null;
                        list.add(mGorev);
                    }
                }
                adapter = new AdapGorev(context,list);
                rcygorev.setAdapter(adapter);
                dialog.dismiss();
                if (list.size()>0){
                    txtgorevyok.setVisibility(View.INVISIBLE);
                }
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
}
