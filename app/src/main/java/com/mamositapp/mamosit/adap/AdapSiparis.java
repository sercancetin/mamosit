package com.mamositapp.mamosit.adap;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mamositapp.mamosit.R;
import com.mamositapp.mamosit.model.MGorev;
import com.mamositapp.mamosit.model.MSiparis;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class AdapSiparis extends RecyclerView.Adapter<AdapSiparis.MyViewHolder>{
    private Context context;
    private Intent intent;
    private ArrayList<MSiparis> list;
    public AdapSiparis(Context context, ArrayList<MSiparis> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.odenmemis_single,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder h, int i) {
        h.txtodemeisim.setText(list.get(i).getUsername());
        h.txtodemefiyat.setText(String.valueOf(list.get(i).getFiyat()));
        h.kaniturl = list.get(i).getKanitimg();
        h.txtodemetarih.setText(list.get(i).getTarih());
        h.durum1 = list.get(i).getDurum();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtodemeisim,txtodemetarih,txtodemefiyat;
        String kaniturl;
        Button btnodeme;
        ImageView kanitimg;
        private int durum1;
        Dialog dialog;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtodemeisim = itemView.findViewById(R.id.txtodemeisim);
            txtodemetarih = itemView.findViewById(R.id.txtodemetarih);
            txtodemefiyat = itemView.findViewById(R.id.txtodemefiyat);
            btnodeme = itemView.findViewById(R.id.btnodeme);
            btnodeme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (durum1==1){
                        //btnodeme1.setBackground(context.getResources().getDrawable(R.drawable.answerbggreen));
                        setDialog();
                    }
                }
            });
        }
        private void setDialog(){
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.odeme_kanit);
            kanitimg = dialog.findViewById(R.id.kanitimg);
            Picasso.get().load(kaniturl).into(kanitimg);
            dialog.setCancelable(true);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
        private void butonKontrol(){
            if(kaniturl.length()>5){
                btnodeme.setBackground(context.getResources().getDrawable(R.drawable.answerbggreen));
            }
        }
    }

}
