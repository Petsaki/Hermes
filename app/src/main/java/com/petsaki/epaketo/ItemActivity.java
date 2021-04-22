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

    private Button epiloghButton;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    FetchData fetchDataPaketo;
    private String paketoHmer;
    boolean checking;
    private Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        mAuth = FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        tabLayout= findViewById(R.id.tabLayout2);
        viewPager = findViewById(R.id.viewPager);
        epiloghButton=findViewById(R.id.button4);
        setTitle("Πληροφορίες Πακέτου");
        getSupportActionBar().setElevation(0);

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


    public void akyrwshFun(View view){
        onBackPressed();
    }

    public void epiloghFun(View view){
        epiloghButton.setEnabled(false);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query query = ref.child("Paketa").orderByChild("hmerominia").equalTo(paketoHmer);
        checking=true;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    if (ds.getValue()!=null){

                        String keyPaketou=ds.getKey();
                        showToast("Το πακέτο αυτό πλέον βρίσκετε στην καρτέλα Προς Παράδοση!");


                        DatabaseReference frompath = FirebaseDatabase.getInstance().getReference().child("Paketa").child(keyPaketou);
                        DatabaseReference topath = FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ProsParadwsh").child(keyPaketou);

                        movePaketo(frompath,topath);
                        checking=false;
                    }
                }
                if(checking){
                    showToast("Φαίνεται ότι κάποιος άλλος πρόλαβε αυτό το πακέτο.");
                    epiloghButton.setEnabled(true);
                }
          }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void showToast(String string) {
        if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
            toast = Toast.makeText(ItemActivity.this, string, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    //Apo thn bash, kanw cut to paketo apo ta paketa kai to bazw ston xrhsth
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