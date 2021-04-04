package com.petsaki.epaketo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PerigrafhFragments extends Fragment {

    private TextView odosMagaziou,odos_paralhpth,megethos,baros,etairia;
    public static PerigrafhFragments getInstance(){
        PerigrafhFragments perigrafhFragments = new PerigrafhFragments();
        return perigrafhFragments;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.perigrafh, container, false);
        odosMagaziou=(TextView)view.findViewById(R.id.odos_magaziou_id);
        odos_paralhpth=(TextView)view.findViewById(R.id.odos_paralhpth_id);
        megethos=(TextView)view.findViewById(R.id.megethos_id);
        baros=(TextView)view.findViewById(R.id.baros_id);
        etairia=(TextView)view.findViewById(R.id.etairia_id);

        Intent intent = getActivity().getIntent();
        if(intent.getExtras()!= null){
            FetchData fetchData = (FetchData) intent.getSerializableExtra("data");
            odosMagaziou.setText(fetchData.getOdos_magaziou());
            odos_paralhpth.setText(fetchData.getOdos());
            megethos.setText(fetchData.getMegethos());
            baros.setText(fetchData.getBaros());
            etairia.setText(fetchData.getOnoma_etairias());
        }


        return view;
    }
}
