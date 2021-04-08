package com.petsaki.epaketo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.ArrayList;
import java.util.List;


public class HelperAdapter extends RecyclerView.Adapter<HelperAdapter.NewViewHolder>{
    static List<FetchData> paketaList;
    static Context context;
    static SelectedPaketo selectedPaketo;

    public HelperAdapter( Context context, SelectedPaketo selectedPaketo){
        this.paketaList = new ArrayList<>();
        this.context = context;
        this.selectedPaketo = selectedPaketo;
    }

    public void addAll(List<FetchData> newFetchData){
        int initsize=paketaList.size();
        paketaList.addAll(newFetchData);
        notifyItemRangeChanged(initsize,newFetchData.size());
    }


//    @Override
//    public void onViewRecycled(@NonNull NewViewHolder holder) {
//        if (holder.map != null)
//        {
//            holder.map.clear();
//            holder.map.setMapType(GoogleMap.MAP_TYPE_NONE);
//        }
//    }

    public static class NewViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        TextView odos,baros,onoma_etairias,odos_magaziou,megethos;
        MapView mapView;
        GoogleMap map;
        int height = 65;
        int width = 65;
        Marker adr1;
        Marker adr2;
        String odos_m,odos_p;


        public NewViewHolder(@NonNull View itemView){
            super(itemView);
            odos=itemView.findViewById(R.id.odos);
            baros=itemView.findViewById(R.id.baros);
            onoma_etairias=itemView.findViewById(R.id.onoma_etairias);
            odos_magaziou=itemView.findViewById(R.id.odos_magaziou);
            megethos=itemView.findViewById(R.id.megethos);

            mapView = itemView.findViewById(R.id.mapView);
            mapView.setClickable(false);
            if (mapView != null)
            {
                mapView.onCreate(null);
                mapView.onResume();
                mapView.getMapAsync(this);
            }


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPaketo.selectedPaketo(paketaList.get(getAdapterPosition()));
                }
            });
        }


        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(context);

            googleMap.getUiSettings().setAllGesturesEnabled(false);
            googleMap.getUiSettings().setMapToolbarEnabled(false);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            BitmapDrawable bitmapdraw = (BitmapDrawable)context.getResources().getDrawable(R.drawable.icons8_package_48);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

            BitmapDrawable bitmapdraw2 = (BitmapDrawable)context.getResources().getDrawable(R.drawable.icons8_house_48);
            Bitmap b2 = bitmapdraw2.getBitmap();
            Bitmap smallMarker2 = Bitmap.createScaledBitmap(b2, width, height, false);

            //ΜΑΡΙΕ ΒΓΑΛΕ ΤΟ ΙF ΑΠΟ ΕΔΩ!!!
//            LatLng address = getLocationFromAddress(context, null);
//            if (address==null){
            LatLng address= getLocationFromAddress(context, odos_p);
//            }
            googleMap.addMarker(new MarkerOptions().position(address).icon(BitmapDescriptorFactory.fromBitmap(smallMarker2)));

            LatLng address2 = getLocationFromAddress(context, odos_m);

            googleMap.addMarker(new MarkerOptions().position(address2).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(address);
            builder.include(address2);
            LatLngBounds bounds = builder.build();
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));

//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(address2));
//            googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            //googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 1, null);
            //googleMap.setMyLocationEnabled(true);
        }
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

    @NonNull
    @Override
    public NewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView= LayoutInflater.from(context).inflate(R.layout.item_layout_v2,parent,false);
        return new NewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewViewHolder holder, int position){
        holder.odos.setText(paketaList.get(position).getOdos());
        holder.baros.setText(paketaList.get(position).getBaros());
        holder.onoma_etairias.setText(paketaList.get(position).getOnoma_etairias());
        holder.odos_magaziou.setText(paketaList.get(position).getOdos_magaziou());
        holder.megethos.setText(paketaList.get(position).getMegethos());
        holder.odos_p=paketaList.get(position).getOdos();
        holder.odos_m=paketaList.get(position).getOdos_magaziou();
    }

    @Override
    public int getItemCount() {
        return paketaList.size();
    }

    public interface SelectedPaketo{
        void selectedPaketo(FetchData fetchData);
    }

}
