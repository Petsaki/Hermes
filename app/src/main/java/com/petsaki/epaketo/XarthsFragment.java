package com.petsaki.epaketo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class XarthsFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private String odos_magaziou,odos_paralhpth;
    private int height = 100;
    private int width = 100;
    Bitmap smallMarker2;
    Bitmap smallMarker;

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    public static XarthsFragment getInstance(){
        XarthsFragment xarthsFragment = new XarthsFragment();
        return xarthsFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.xarths, container, false);


        Bundle mapViewBundle = null;
        if (savedInstanceState !=null){
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView=(MapView)view.findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
        Intent intent = getActivity().getIntent();
        if(intent.getExtras()!= null){
            FetchData fetchData = (FetchData) intent.getSerializableExtra("data");
            odos_magaziou = fetchData.getOdos_magaziou();
            odos_paralhpth = fetchData.getOdos();
        }

        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.icons8_package_48);
        Bitmap b = bitmapdraw.getBitmap();
        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        BitmapDrawable bitmapdraw2 = (BitmapDrawable)getResources().getDrawable(R.drawable.icons8_house_48);
        Bitmap b2 = bitmapdraw2.getBitmap();
        smallMarker2 = Bitmap.createScaledBitmap(b2, width, height, false);

        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LatLng address = getLocationFromAddress(getActivity(), odos_paralhpth);

        googleMap.addMarker(new MarkerOptions().position(address).title("Παράδωση").icon(BitmapDescriptorFactory.fromBitmap(smallMarker2)));

        LatLng address2 = getLocationFromAddress(getActivity(), odos_magaziou);

        googleMap.addMarker(new MarkerOptions().position(address2).title("Πακέτο").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(address);
        builder.include(address2);
        LatLngBounds bounds = builder.build();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));

        googleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }


    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    //Apo aples dieuthinseis tis kanei sintetagmenes
    public LatLng getLocationFromAddress(Context context, String strAddress)
    {
        Geocoder coder= new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try
        {
            address = coder.getFromLocationName(strAddress, 5);
            if(address==null)
            {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return p1;

    }
}
