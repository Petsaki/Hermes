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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
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
import java.util.Locale;

//EDW EINAI POU GINONTE OLA TA KOLPA GIA TO RECYCLER VIEW GIA NA TA DEIXNEI TA PAKETA

public class HelperAdapter extends RecyclerView.Adapter<HelperAdapter.NewViewHolder>{
    static List<FetchData> paketaList;
    static ArrayList<FetchData> copypaketaList;
    static Context context;
    static SelectedPaketo selectedPaketo;

    public HelperAdapter( Context context, SelectedPaketo selectedPaketo){
        this.paketaList = new ArrayList<>();
        this.context = context;
        this.selectedPaketo = selectedPaketo;
        copypaketaList =  new ArrayList<>();
        this.copypaketaList.addAll(paketaList);
    }

    //Bazei sto telos tis stibas ta kainoyrgia paketa
    public void addAll(List<FetchData> newFetchData){
        int initsize=paketaList.size();
        paketaList.addAll(newFetchData);
        notifyItemRangeChanged(initsize,newFetchData.size());
    }




    //Oti item exei scrollaristei kai den fenete sthn othonh na diagrafei ta dedomena apo tous xartes
    @Override
    public void onViewRecycled(@NonNull NewViewHolder holder) {
        if (holder.map != null)
        {
            holder.map.clear();
            holder.map.setMapType(GoogleMap.MAP_TYPE_NONE);
        }
    }

    public static class NewViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        TextView odos,baros,onoma_etairias,odos_magaziou,megethos;
        MapView mapView;
        GoogleMap map;
        int height = 65;
        int width = 65;
        Marker adr1;
        Marker adr2;
        String odos_m,odos_p;

        //To custom marker gia to xarth
        BitmapDrawable bitmapdraw = (BitmapDrawable)context.getResources().getDrawable(R.drawable.icons8_package_48);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        BitmapDrawable bitmapdraw2 = (BitmapDrawable)context.getResources().getDrawable(R.drawable.icons8_house_48);
        Bitmap b2 = bitmapdraw2.getBitmap();
        Bitmap smallMarker2 = Bitmap.createScaledBitmap(b2, width, height, false);


        //Kalite kathe fora gia ena neo antikeimeno pou mpainei sto recycler
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
                //mapView.onResume();
                mapView.getMapAsync(this);
            }


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPaketo.selectedPaketo(paketaList.get(getAdapterPosition()));
                }
            });
        }


        //Kalite kathe fora gia na arxikopoihsh toys xartes
        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(context);

            map= googleMap;
            map.getUiSettings().setAllGesturesEnabled(false);
            map.getUiSettings().setMapToolbarEnabled(false);

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            setMapLocation();
        }

        //Bazw tous markers kai deixnw tous xartes
        private void setMapLocation() {
            if (map == null) return;

            FetchData data = (FetchData) mapView.getTag();
            if (data == null) return;
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(data.location, 13f));
//            map.addMarker(new MarkerOptions().position(data.location));
            LatLng address= getLocationFromAddress(context,data.getOdos());
            map.addMarker(new MarkerOptions().position(address).icon(BitmapDescriptorFactory.fromBitmap(smallMarker2)));
            LatLng address2 = getLocationFromAddress(context, data.getOdos_magaziou());
            map.addMarker(new MarkerOptions().position(address2).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(address);
            builder.include(address2);
            LatLngBounds bounds = builder.build();
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,0));
            //map.moveCamera(CameraUpdateFactory.zoomTo(13f));


        }

        //Apo aples dieuthinseis tis kanei sintetagmenes
        public LatLng getLocationFromAddress(Context context, String strAddress)
        {
            Geocoder coder= new Geocoder(context);
            List<Address> address;
            LatLng p1 = null;

            try
            {
                address = coder.getFromLocationName(strAddress, 1);
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

        //oti lene ta sxolia vvv
        private void bindView(int pos) {
            FetchData item = paketaList.get(pos);
            // Store a reference of the ViewHolder object in the layout.
            itemView.setTag(this);
            // Store a reference to the item in the mapView's tag. We use it to get the
            // coordinate of a location, when setting the map location.
            mapView.setTag(item);
            setMapLocation();
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
        holder.odos.setText(paketaList.get(position).getOdos().substring(0,paketaList.get(position).getOdos().length()-6));
        holder.baros.setText(paketaList.get(position).getBaros());
        holder.onoma_etairias.setText(paketaList.get(position).getOnoma_etairias());
        holder.odos_magaziou.setText(paketaList.get(position).getOdos_magaziou().substring(0,paketaList.get(position).getOdos_magaziou().length()-6));
        holder.megethos.setText(paketaList.get(position).getMegethos());//easteregg:paidiki fili <3
        holder.odos_p=paketaList.get(position).getOdos();
        holder.odos_m=paketaList.get(position).getOdos_magaziou();

        if (holder == null) {
            return;
        }
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return paketaList.size();
    }

    public interface SelectedPaketo{
        void selectedPaketo(FetchData fetchData);
    }


    //O PARAKATW KWDIKAS HTAN GIA TO SEARCH. DEN EXEI KATI ALLO. MHN TO DINEIS SHMASIA
    // method for filtering our recyclerview items.
    public void filterList(ArrayList<FetchData> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        //paketaList = filterlist;

        int initsize=paketaList.size();
        paketaList.addAll(filterlist);
        notifyItemRangeChanged(initsize,filterlist.size());
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

//    public void filter(String characterText) {
//        characterText = characterText.toLowerCase(Locale.getDefault());
//        paketaList.clear();
//        if (characterText.length() == 0) {
//            paketaList.addAll(copypaketaList);
//        } else {
//            paketaList.clear();
//            for (FetchData fetchData: copypaketaList) {
//                if (fetchData.getOdos_magaziou().toLowerCase(Locale.getDefault()).contains(characterText)) {
//                    paketaList.add(fetchData);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }

}
