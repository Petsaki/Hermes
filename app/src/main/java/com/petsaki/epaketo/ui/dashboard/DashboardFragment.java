package com.petsaki.epaketo.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.petsaki.epaketo.FetchData;
import com.petsaki.epaketo.HelperAdapter;
import com.petsaki.epaketo.ItemActivity;
import com.petsaki.epaketo.Item_2_Activity;
import com.petsaki.epaketo.R;
import com.petsaki.epaketo.Settings_2_Activity;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements HelperAdapter.SelectedPaketo{

    View DashboardFragment;
    private DashboardViewModel dashboardViewModel;
    private Toolbar toolbar;

    RecyclerView recyclerView;
    List<FetchData> fetchData;
    HelperAdapter adapter;
    DatabaseReference reference;

    String last_key="",last_node="";
    boolean isMaxData=false,isScrolling=false;
    int ITEM_LOAD_COUNT= 15;
    int currentitems,tottalitems,scrolledoutitems;
    ProgressBar progressBar;
    LinearLayoutManager manager;

    private SwipeRefreshLayout refreshLayout;
    private ImageView emptyBox;
    private TextView emptyView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);


        DashboardFragment=inflater.inflate(R.layout.fragment_dashboard,container,false);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
        toolbar=(Toolbar)DashboardFragment.findViewById(R.id.toolbar);
        recyclerView = (RecyclerView)DashboardFragment.findViewById(R.id.recyclerView);
        refreshLayout = (SwipeRefreshLayout)DashboardFragment.findViewById(R.id.swipeRefresh);
        progressBar=(ProgressBar)DashboardFragment.findViewById(R.id.progressBar);
        emptyBox=(ImageView)DashboardFragment.findViewById(R.id.emptyBox);
        emptyView=(TextView)DashboardFragment.findViewById(R.id.emptyView);

//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                //recyclerView.setAdapter(null);
                last_node="";
                isMaxData=false;
                //currentitems=tottalitems=scrolledoutitems=0;
                getLastKeyFromFirebase();
                resetRecycle();
//                recyclerView.setAdapter(adapter);
//                recyclerView.setLayoutManager(manager);
                getPaketa();

            }

        });

        getLastKeyFromFirebase();
        resetRecycle();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                getPaketa();
            }
        }, 280);




        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState){

                super.onScrollStateChanged(recyclerView, newState);

                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling=true;
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);

                currentitems=manager.getChildCount();
                tottalitems=manager.getItemCount();
                scrolledoutitems=manager.findFirstVisibleItemPosition();

                if( isScrolling && currentitems + scrolledoutitems == tottalitems){
                    //  Toast.makeText(getContext(), "fetch data", Toast.LENGTH_SHORT).show();
                    isScrolling=false;
                    //fetch data
                    progressBar.setVisibility(View.VISIBLE);
                    getPaketa();

                }

            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tune:
                        Intent intent = new Intent(getActivity(), Settings_2_Activity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_up,R.anim.dont_move);
                        break;
                }
                return false;
            }
        });

        return DashboardFragment;
    }

    private void getPaketa()
    {
        if(!isMaxData)
        {
            Query query;

            if (TextUtils.isEmpty(last_node)) {
                query = FirebaseDatabase.getInstance().getReference()
                        .child("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ProsParadwsh")
                        .orderByChild("hmerominia")
                        .limitToFirst(ITEM_LOAD_COUNT);

            }else {
                query = FirebaseDatabase.getInstance().getReference()
                        .child("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ProsParadwsh")
                        .orderByChild("hmerominia")
                        .startAt(last_node)
                        .limitToFirst(ITEM_LOAD_COUNT);
            }
            query.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    if(snapshot.hasChildren())
                    {

                        List<FetchData> newFetchData = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren())
                        {
                            newFetchData.add(ds.getValue(FetchData.class));
                        }

                        last_node =newFetchData.get(newFetchData.size()-1).getHmerominia();
//                        Toast.makeText(getContext(), "last_node = "+ last_node, Toast.LENGTH_SHORT).show();
                        if(!last_node.equals(last_key)) {
                            newFetchData.remove(newFetchData.size() - 1);
                        }else {
                            last_node = "end";
                            //Toast.makeText(getContext(), "Bghkaaa", Toast.LENGTH_SHORT).show();
                        }
//                         Toast.makeText(getContext(), "last_node = "+last_node, Toast.LENGTH_SHORT).show();

                        adapter.addAll(newFetchData);
                        adapter.notifyDataSetChanged();

                        if (recyclerView.getAdapter().getItemCount()==0){
                            emptyBox.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.VISIBLE);
                        }else{
                            emptyBox.setVisibility(View.GONE);
                            emptyView.setVisibility(View.GONE);
                        }


                    }
                    else
                    {
                        isMaxData=true;
                    }
                    refreshLayout.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {

                }
            });


        }

        else
        {
            refreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE); //if data end
        }


    }

    private void getLastKeyFromFirebase(){
        Query getLastKey= FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ProsParadwsh").orderByChild("hmerominia").limitToLast(1);

        getLastKey.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                for(DataSnapshot lastkey : snapshot.getChildren()) {
                    last_key = lastkey.child("hmerominia").getValue().toString();
                    //Toast.makeText(getContext(), "last_key = " + last_key, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error){
                //Toast.makeText(getContext(), "Can not get last key", Toast.LENGTH_SHORT).show();
            }
        });


    }
    @Override
    public void selectedPaketo(FetchData fetchData) {
        startActivity(new Intent(getActivity(), Item_2_Activity.class).putExtra("data",fetchData));
        getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.corner_up_left);
    }

    public void resetRecycle(){
        manager = new LinearLayoutManager(getContext());
        //SOS MALLON EDW LATHOS MARIEEEEEE KOITA EDW SE AYTH THN GRAMMH - Onclick tutorial
        adapter=new HelperAdapter(getContext(),this::selectedPaketo);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        
    }
}