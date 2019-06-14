package com.mamositapp.mamosit.activity;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mamositapp.mamosit.R;
import com.mamositapp.mamosit.activity.pager.SiparislerPagerAdapter;
import com.mamositapp.mamosit.activity.ui.FragOdenmemisler;
import com.mamositapp.mamosit.activity.ui.FragOdenmisler;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Siparis extends AppCompatActivity {
    //@BindView(R.id.barlytsiparis) AppBarLayout _barlytsiparis;
    @BindView(R.id.tablayout) TabLayout _tablayout;
    @BindView(R.id.siparisviewpager) ViewPager _siparisviewpager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siparis);
        ButterKnife.bind(this);
        SiparislerPagerAdapter siparislerPagerAdapter = new SiparislerPagerAdapter(getSupportFragmentManager());

        //add fragment
        siparislerPagerAdapter.addFragment(new FragOdenmemisler(),getResources().getString(R.string.odemebekleyenler));
        siparislerPagerAdapter.addFragment(new FragOdenmisler(),getResources().getString(R.string.odemesiyapilanlar));
        //fragment setup
        _siparisviewpager.setAdapter(siparislerPagerAdapter);
        _tablayout.setupWithViewPager(_siparisviewpager);
    }
}
