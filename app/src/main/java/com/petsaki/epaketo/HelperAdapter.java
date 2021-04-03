package com.petsaki.epaketo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HelperAdapter extends RecyclerView.Adapter<HelperAdapter.NewViewHolder>
{
    List<FetchData> paketaList;
    Context context;

    public HelperAdapter( Context context)
    {
        this.paketaList = new ArrayList<>();
        this.context = context;
    }

    public void addAll(List<FetchData> newFetchData)
    {
        int initsize=paketaList.size();
        paketaList.addAll(newFetchData);
        notifyItemRangeChanged(initsize,newFetchData.size());
    }


    public static class NewViewHolder extends RecyclerView.ViewHolder{
        TextView odos,baros,onoma_etairias,odos_magaziou,megethos;

        public NewViewHolder(@NonNull View itemView){
            super(itemView);
            odos=itemView.findViewById(R.id.odos);
            baros=itemView.findViewById(R.id.baros);
            onoma_etairias=itemView.findViewById(R.id.onoma_etairias);
            odos_magaziou=itemView.findViewById(R.id.odos_magaziou);
            megethos=itemView.findViewById(R.id.megethos);


        }
    }

    @NonNull
    @Override
    public NewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView= LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false);
        return new NewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewViewHolder holder, int position){
        holder.odos.setText(paketaList.get(position).getOdos());
        holder.baros.setText(paketaList.get(position).getBaros());
        holder.onoma_etairias.setText(paketaList.get(position).getOnoma_etairias());
        holder.odos_magaziou.setText(paketaList.get(position).getOdos_magaziou());
        holder.megethos.setText(paketaList.get(position).getMegethos());
    }

    @Override
    public int getItemCount() {
        return paketaList.size();
    }


}
