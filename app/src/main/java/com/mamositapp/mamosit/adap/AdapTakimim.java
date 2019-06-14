package com.mamositapp.mamosit.adap;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mamositapp.mamosit.R;
import com.mamositapp.mamosit.model.MTakimim;

import java.util.ArrayList;

public class AdapTakimim extends RecyclerView.Adapter<AdapTakimim.MyViewHolder>{
    private Context context;
    private Intent intent;
    private ArrayList<MTakimim> list;

    public AdapTakimim(Context context, ArrayList<MTakimim> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.takimimsingle,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder h, int i) {
        h.txttakimsinglepuan.setText(String.valueOf(list.get(i).getRefverdigitoplampuan()));
        h.txttakimsingleisim.setText(list.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txttakimsinglepuan,txttakimsingleisim;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txttakimsingleisim = itemView.findViewById(R.id.txttakimsingleisim);
            txttakimsinglepuan = itemView.findViewById(R.id.txttakimsinglepuan);
        }
    }

}
