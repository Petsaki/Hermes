package com.petsaki.epaketo.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.petsaki.epaketo.R;
import com.petsaki.epaketo.Settings_2_Activity;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private Toolbar toolbar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        toolbar=(Toolbar)root.findViewById(R.id.toolbar);

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tune:
                        Toast.makeText(getActivity(), "Settings!",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), Settings_2_Activity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_up,R.anim.dont_move);
                        break;
                }
                return false;
            }
        });

        return root;
    }
}