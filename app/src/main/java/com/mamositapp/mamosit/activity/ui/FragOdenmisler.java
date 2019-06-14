package com.mamositapp.mamosit.activity.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mamositapp.mamosit.R;
import com.mamositapp.mamosit.adap.AdapSiparis;
import com.mamositapp.mamosit.adap.AdapSiparisOdenmis;
import com.mamositapp.mamosit.model.MSiparis;

import java.util.ArrayList;
import java.util.Collections;

public class FragOdenmisler extends Fragment {
    View view;
    RecyclerView myRecycler;
    ArrayList<MSiparis> list;
    AdapSiparisOdenmis adapter;
    TextView txtodenmisbilgi;
    public FragOdenmisler() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.odenmisfragment,container,false);
        myRecycler = view.findViewById(R.id.rcyodenmis);
        txtodenmisbilgi = view.findViewById(R.id.txtodenmisbilgi);
        myRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        listele();
        return view;
    }
    private void listele(){
        Query query = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.siparisler)).orderByChild(getResources().getString(R.string.durum)).equalTo(1).limitToLast(150);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            MSiparis mSiparis;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    mSiparis = dataSnapshot1.getValue(MSiparis.class);
                    if (mSiparis!=null){
                        list.add(mSiparis);
                    }
                }
                TersCevir(list);
                adapter = new AdapSiparisOdenmis(getContext(),list);
                myRecycler.setAdapter(adapter);
                if (list.size()>0){
                    txtodenmisbilgi.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void TersCevir(ArrayList<MSiparis> liste){
        int azs=liste.size()-1;
        int as =0;
        while (as<azs+1){
            Collections.swap(list,as,azs--);
            as++;
        }
    }
}
