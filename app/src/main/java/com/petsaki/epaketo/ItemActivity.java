package com.petsaki.epaketo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class ItemActivity extends AppCompatActivity{ //implements OnMapReadyCallback {

    private TextView textview;
//    private MapView mapView;
    private Polyline polyline;

    private TabLayout tabLayout;
    private ViewPager viewPager;

//    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        textview = findViewById(R.id.textView6);
        tabLayout= findViewById(R.id.tabLayout2);
        viewPager = findViewById(R.id.viewPager);
        getTabs();


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
        }

        //Trabaw ta dedomena apo to antikeimeno pou pathsa
        Intent intent = getIntent();
        if(intent.getExtras()!= null){
             FetchData fetchData = (FetchData) intent.getSerializableExtra("data");

             textview.setText(fetchData.getOnoma_etairias());
        }

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
                viewPagerAdapter.addFragment(XarthsFragment.getInstance(),"XARTHS");
                viewPagerAdapter.addFragment(PerigrafhFragments.getInstance(),"PeRiGrAfH");
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

}