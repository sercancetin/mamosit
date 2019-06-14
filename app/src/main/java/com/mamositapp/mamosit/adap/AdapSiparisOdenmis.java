package com.mamositapp.mamosit.adap;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mamositapp.mamosit.R;
import com.mamositapp.mamosit.model.MSiparis;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class AdapSiparisOdenmis extends RecyclerView.Adapter<AdapSiparisOdenmis.MyViewHolder>{
    private Context context;
    private Intent intent;
    private ArrayList<MSiparis> list;
    public AdapSiparisOdenmis(Context context, ArrayList<MSiparis> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.single_odenmis,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder h, int i) {
        h.txtodemeisim1.setText(list.get(i).getUsername());
        h.txtodemefiyat1.setText(String.valueOf(list.get(i).getFiyat()));
        h.kaniturl = list.get(i).getKanitimg();
        h.txtodemetarih1.setText(list.get(i).getOdendigitarih());
        h.durum1 = list.get(i).getDurum();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtodemeisim1, txtodemetarih1, txtodemefiyat1;
        String kaniturl;
        Button btnodeme1;
        ImageView kanitimg1;
        private int durum1;
        Dialog dialog;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtodemeisim1 = itemView.findViewById(R.id.txtodemeisim1);
            txtodemetarih1 = itemView.findViewById(R.id.txtodemetarih1);
            txtodemefiyat1 = itemView.findViewById(R.id.txtodemefiyat1);
            btnodeme1 = itemView.findViewById(R.id.btnodeme1);
            btnodeme1.setOnClickListener(new View.OnClickListener() {
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
            kanitimg1 = dialog.findViewById(R.id.kanitimg);
            Picasso.get().load(kaniturl).into(kanitimg1);
            dialog.setCancelable(true);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
        private void butonKontrol(){
            if(kaniturl.length()>5){
                btnodeme1.setBackground(context.getResources().getDrawable(R.drawable.answerbggreen));
            }
        }
    }

}
