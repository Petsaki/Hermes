package com.petsaki.epaketo.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.petsaki.epaketo.FetchData;
import com.petsaki.epaketo.HelperAdapter;
import com.petsaki.epaketo.HomeActivityViewModel;
import com.petsaki.epaketo.ItemActivity;
import com.petsaki.epaketo.R;
import com.petsaki.epaketo.Settings_1_Activity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HelperAdapter.SelectedPaketo {

    View HomeFragment;
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
    int setscrollY,getscrollY;
    private HomeActivityViewModel homeActivityViewModel;
    private SwipeRefreshLayout refreshLayout;
    private Parcelable savedRecyclerLayoutState;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeActivityViewModel = new ViewModelProvider(getActivity()).get(HomeActivityViewModel.class);
        HomeFragment=inflater.inflate(R.layout.fragment_home,container,false);
        toolbar=(Toolbar)HomeFragment.findViewById(R.id.toolbar);
        recyclerView = (RecyclerView)HomeFragment.findViewById(R.id.recyclerView);
        refreshLayout = (SwipeRefreshLayout)HomeFragment.findViewById(R.id.swipeRefresh);


        //APOTIXIMENH PROSPATHIA GIA NA KRATAW TO SCROLL POTISION TOU RECYCLER VIEW
//        homeActivityViewModel.getRecyler_main_Y().observe(getViewLifecycleOwner(), new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer s) {
//                getscrollY=s;
//
//            }
//        });
//        HomeFragment.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//
//                recyclerView.scrollTo(0,getscrollY);
//            }
//        });

        //Arxikopoiw oti exei na kanei me to recycler view
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                last_node="";
                isMaxData=false;
                getLastKeyFromFirebase();
                resetRecycle();
                getPaketa();
            }
        });

        progressBar=(ProgressBar)HomeFragment.findViewById(R.id.progressBar);
        getLastKeyFromFirebase();

        resetRecycle();



        getPaketa();

        //ScrollListener gia na ckerw ama eftase sto telos tou recycler view
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

        //Gia to toolbar gia na anoickei ta filtra
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tune:
                        Intent intent = new Intent(getActivity(), Settings_1_Activity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_up,R.anim.dont_move);
                        break;
                    case R.id.action_search:
//                        Toast.makeText(getActivity(), "Search!",
//                                Toast.LENGTH_LONG).show();
                }
                return false;
            }




        });

        return HomeFragment;
    }

    //Trabaw data apo thn bash
    private void getPaketa()
    {
        if(!isMaxData)
        {
            Query query;

            //last_node einai to teleutaio paketo apo thn partida poy trabicke apo thn bash
            if (TextUtils.isEmpty(last_node))
                query = FirebaseDatabase.getInstance().getReference()
                        .child("Paketa")
                        .orderByChild("hmerominia")
                        .limitToFirst(ITEM_LOAD_COUNT);
            else
                query = FirebaseDatabase.getInstance().getReference()
                        .child("Paketa")
                        .orderByChild("hmerominia")
                        .startAt(last_node)
                        .limitToFirst(ITEM_LOAD_COUNT);

            query.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    if(snapshot.hasChildren())
                    {

                        fetchData = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren())
                        {
                            fetchData.add(ds.getValue(FetchData.class));
                        }

                        last_node =fetchData.get(fetchData.size()-1).getHmerominia();
                        if(!last_node.equals(last_key)) {
                            fetchData.remove(fetchData.size() - 1);
                        }else {
                            last_node = "end";
                        }

                        //kalo apo thn HelperAdapter thn addAll methodo
                        adapter.addAll(fetchData);
                        adapter.notifyDataSetChanged();


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

    //Pairnw to id tou teleutaiou paketou me orderBy thn hmerominia
    private void getLastKeyFromFirebase(){
        Query getLastKey= FirebaseDatabase.getInstance().getReference().child("Paketa").orderByChild("hmerominia").limitToLast(1);

        getLastKey.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                for(DataSnapshot lastkey : snapshot.getChildren()) {
                    last_key = lastkey.child("hmerominia").getValue().toString();
//                    Toast.makeText(getContext(), "last_key = " + last_key, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error){
                //Toast.makeText(getContext(), "Can not get last key", Toast.LENGTH_SHORT).show();
            }
        });


    }

    //Stelnw ta data tou paketou pou patithike kai anoigw kainourgio activity
    @Override
    public void selectedPaketo(FetchData fetchData) {
            startActivity(new Intent(getActivity(), ItemActivity.class).putExtra("data",fetchData));
            getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.corner_up_left);
    }

    //Apotiximenh prospathia gia na krataw to scroll potision tou recycler
//    @Override
//    public void onPause() {
//        super.onPause();
//        setscrollY= recyclerView.getScrollY();
//
//        Toast.makeText(getActivity(), "EFYGAAA",Toast.LENGTH_SHORT).show();
//
//        homeActivityViewModel.setRecyler_main_Y(setscrollY);
//    }

    //Oute egw den ckerw akribws ti kanei :)
    public void resetRecycle(){
        manager = new LinearLayoutManager(getContext());
        //SOS MALLON EDW LATHOS MARIEEEEEE KOITA EDW SE AYTH THN GRAMMH - Onclick tutorial
        adapter=new HelperAdapter(getContext(),this::selectedPaketo);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
    }


    //GIA TO SEARCH ALLA DEN DOULEYEI
//    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
//        MenuInflater inflater= getActivity().getMenuInflater();
//        inflater.inflate(R.menu.toolbar_2_menu,menu);
//
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//        Toast.makeText(getActivity(), "eeeeep", Toast.LENGTH_SHORT).show();
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                filter(newText);
//                return false;
//            }
//        });
//        return true;
//    }


    //OLOS O PARAKATW KWDIKAS HTAN GIA TO SEARCH ALLA DEN DOULEYEI
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.toolbar_2_menu,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        Toast.makeText(getActivity(), "eeeeep", Toast.LENGTH_SHORT).show();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

    }


    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<FetchData> filteredlist = new ArrayList<>();
        Toast.makeText(getActivity(), "GEIA SOY", Toast.LENGTH_SHORT).show();

        // running a for loop to compare elements.
        for (FetchData item : fetchData) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getOdos_magaziou().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(getActivity(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
    }

    public void setSavedRecyclerLayoutState(Parcelable savedRecyclerLayoutState) {
        this.savedRecyclerLayoutState = savedRecyclerLayoutState;
    }
}