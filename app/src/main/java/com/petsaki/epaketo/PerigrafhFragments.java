package com.petsaki.epaketo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PerigrafhFragments extends Fragment {

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
        return view;
    }
}
