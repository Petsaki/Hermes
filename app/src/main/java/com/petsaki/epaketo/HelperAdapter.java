package com.petsaki.epaketo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HelperAdapter extends RecyclerView.Adapter {

    List<FetchData> fetchDataList;

    public HelperAdapter(List<FetchData> fetchDataList) {
        this.fetchDataList = fetchDataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);

        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolderClass viewHolderClass=(ViewHolderClass)holder;
        FetchData fetchData=fetchDataList.get(position);
        viewHolderClass.baros.setText(fetchData.getBaros().toString());
        viewHolderClass.odos.setText(fetchData.getOdos());

    }

    @Override
    public int getItemCount() {

        return fetchDataList.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder{

        TextView odos,baros;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            baros= (TextView)itemView.findViewById(R.id.baros);
            odos= (TextView)itemView.findViewById(R.id.odos);
        }
    }
}
