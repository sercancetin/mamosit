package com.mamositapp.mamosit.adap;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
import com.mamositapp.mamosit.model.MTakimim;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapGorev extends RecyclerView.Adapter<AdapGorev.MyViewHolder>{
    private Context context;
    private Intent intent;
    private ArrayList<MGorev> list;

    public AdapGorev(Context context, ArrayList<MGorev> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.gorev_line,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder h, int i) {
        h.txtgorevbaslik.setText(String.valueOf(list.get(i).getBaslik()));
        h.txtgorevaciklama.setText(list.get(i).getAciklama());
        Picasso.get().load(list.get(i).getImg()).into(h.imggorev);
        h.link = list.get(i).getLink();
        h.mail = list.get(i).getMail();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtgorevbaslik,txtgorevaciklama;
        ImageView imggorev;
        Button btngorevmail,btngorevlink;
        String link,mail;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtgorevbaslik = itemView.findViewById(R.id.txtgorevbaslik);
            txtgorevaciklama = itemView.findViewById(R.id.txtgorevaciklama);
            imggorev = itemView.findViewById(R.id.imggorev);
            btngorevmail = itemView.findViewById(R.id.btngorevmail);
            btngorevlink = itemView.findViewById(R.id.btngorevlink);
            btngorevlink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
                    }catch (ActivityNotFoundException e){
                        //context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/sorubilhediyekazan/")));
                    }
                }
            });
            btngorevmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + mail));
                        intent.putExtra(Intent.EXTRA_SUBJECT, txtgorevbaslik.getText().toString());
                        intent.putExtra(Intent.EXTRA_TEXT, "Kullanıcı mail ve görüntü kesinlikle atmalısınız.");
                        context.startActivity(intent);
                    }catch (ActivityNotFoundException e){

                    }
                }
            });
        }
    }

}
