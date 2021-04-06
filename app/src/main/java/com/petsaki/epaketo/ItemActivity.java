package com.petsaki.epaketo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ItemActivity extends AppCompatActivity{ //implements OnMapReadyCallback {

//    private MapView mapView;
    private Polyline polyline;
    private Button akyrwshButton,epiloghButton;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    FetchData fetchDataPaketo;
    private String paketoHmer;
    boolean checking;
    private Toast toast;

    private Button buttonA,buttonE;

//    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        mAuth = FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        tabLayout= findViewById(R.id.tabLayout2);
        viewPager = findViewById(R.id.viewPager);
        akyrwshButton=findViewById(R.id.button3);
        epiloghButton=findViewById(R.id.button4);

        //Trabaw ta dedomena apo to antikeimeno pou pathsa
        Intent intent = getIntent();
        if(intent.getExtras()!= null){
            FetchData fetchData = (FetchData) intent.getSerializableExtra("data");
            fetchDataPaketo = fetchData;
            paketoHmer=fetchData.getHmerominia();

        }


        //Pernaw to paketo sta fragments mou
        Bundle bundle = new Bundle();
        bundle.putSerializable("data",fetchDataPaketo);
        // set Fragmentclass Arguments
        PerigrafhFragments perigrafhFragments = new PerigrafhFragments();
        perigrafhFragments.setArguments(bundle);
        XarthsFragment xarthsFragment = new XarthsFragment();
        xarthsFragment.setArguments(bundle);



        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
        }



        getTabs();

        //Google maps things :/
//        Bundle mapViewBundle = null;
//        if (savedInstanceState !=null){
//            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
//        }
//        mapView = (MapView) findViewById(R.id.mapView);
//        mapView.onCreate(mapViewBundle);
//        mapView.getMapAsync(this);


    }

    public void getTabs(){
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                viewPagerAdapter.addFragment(XarthsFragment.getInstance(),"Χάρτης");
                viewPagerAdapter.addFragment(PerigrafhFragments.getInstance(),"Περιγραφή");
                viewPager.setAdapter(viewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        this.finish();
        overridePendingTransition(R.anim.corner_down_right,R.anim.slide_out_right);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.anim.corner_down_right,R.anim.slide_out_right);
    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        if (ActivityCompat.checkSelfPermission(ItemActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(ItemActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        LatLng address = getLocationFromAddress(this, "Κολοκοτρώνη 20, Τριάδι, 57001");
//        googleMap.addMarker(new MarkerOptions().position(address).title("Παράδωση"));
//
//        LatLng address2 = getLocationFromAddress(this, "Εγνατία, Θεσσαλονίκη");
//        googleMap.addMarker(new MarkerOptions().position(address2).title("Πακέτο"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(address2));
//        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
//        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
//        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
//        googleMap.setMyLocationEnabled(true);
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
//        if (mapViewBundle == null) {
//            mapViewBundle = new Bundle();
//            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
//        }
//
//        mapView.onSaveInstanceState(mapViewBundle);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mapView.onResume();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        mapView.onStart();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        mapView.onStop();
//    }
//
//
//    @Override
//    public void onPause() {
//        mapView.onPause();
//        super.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        mapView.onDestroy();
//        super.onDestroy();
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mapView.onLowMemory();
//    }
//
//    public LatLng getLocationFromAddress(Context context, String strAddress)
//    {
//        Geocoder coder= new Geocoder(context);
//        List<Address> address;
//        LatLng p1 = null;
//
//        try
//        {
//            address = coder.getFromLocationName(strAddress, 5);
//            if(address==null)
//            {
//                return null;
//            }
//            Address location = address.get(0);
//            location.getLatitude();
//            location.getLongitude();
//
//            p1 = new LatLng(location.getLatitude(), location.getLongitude());
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        return p1;
//
//    }


    public void akyrwshFun(View view){
        onBackPressed();
    }

    public void epiloghFun(View view){
        epiloghButton.setEnabled(false);
//        reference = FirebaseDatabase.getInstance().getReference().child("Paketa").child(paketoID);
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.getValue()==null){
//                    Toast.makeText(ItemActivity.this, "Den Yparxeis gamhmeno paketo", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(ItemActivity.this, "Yparxeis kai se thelw", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query query = ref.child("Paketa").orderByChild("hmerominia").equalTo(paketoHmer);
        checking=true;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    if (ds.getValue()!=null){
                        String keyPaketou=ds.getKey();
                        Toast.makeText(ItemActivity.this, "Yparxeis kai se thelw: "+keyPaketou, Toast.LENGTH_SHORT).show();


//                        String userID=user.getUid();
//                        Query query = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

                        DatabaseReference frompath = FirebaseDatabase.getInstance().getReference().child("Paketa").child(keyPaketou);
                        DatabaseReference topath = FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ProsParadwsh").child(keyPaketou);

                        movePaketo(frompath,topath);
//                        FirebaseDatabase.getInstance().getReference("Users")
//                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ProsParadwsh")
//                                .setValue(dataSnapshot.getValue());


//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    showToast("User has been singed up successfully!");
//
//                                } else {
//                                    showToast("Failed to singed up. Try again!");
//                                }
////                                progressBar.setVisibility(View.GONE);
//                            }
//                        });


//                        ds.getRef().removeValue();
                        checking=false;
                    }
                }
                if(checking){
                    Toast.makeText(ItemActivity.this, "Φαίνεται ότι καποιος έχει πάρει ήδη το πακέτο αυτό.", Toast.LENGTH_SHORT).show();
                    epiloghButton.setEnabled(true);
                }
          }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        Toast.makeText(this, "ID: "+ paketoHmer, Toast.LENGTH_SHORT).show();
    }

    public void showToast(String string) {
        if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
            toast = Toast.makeText(ItemActivity.this, string, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void movePaketo(final DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                        if (firebaseError != null) {
                            System.out.println("Copy failed");
                        } else {
                            fromPath.removeValue();

                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}