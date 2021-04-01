package com.petsaki.epaketo.ui.home;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.petsaki.epaketo.FetchData;
import com.petsaki.epaketo.HelperAdapter;
import com.petsaki.epaketo.R;
import com.petsaki.epaketo.Settings_1_Activity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Toolbar toolbar;
    RecyclerView recyclerView;
    List<FetchData> fetchData;
    HelperAdapter helperAdapter;
    DatabaseReference reference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        toolbar=(Toolbar)root.findViewById(R.id.toolbar);
        recyclerView=(RecyclerView)root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fetchData= new ArrayList<>();
        final TextView textView = root.findViewById(R.id.text_home);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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
                        Intent intent = new Intent(getActivity(), Settings_1_Activity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_up,R.anim.dont_move);
                        break;
                }
                return false;
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("testing");//.child("Speedex").child("Magazi_1").child("Paketa");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    FetchData data = ds.getValue(FetchData.class);
                    fetchData.add(data);
                }
                helperAdapter= new HelperAdapter(fetchData);
                recyclerView.setAdapter(helperAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }
}