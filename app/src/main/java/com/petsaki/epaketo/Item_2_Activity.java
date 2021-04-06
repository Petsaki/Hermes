package com.petsaki.epaketo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Item_2_Activity extends AppCompatActivity {

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

    private Button oloklhrwsh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_2_);
        mAuth = FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        tabLayout= findViewById(R.id.tabLayout2);
        viewPager = findViewById(R.id.viewPager);
        oloklhrwsh=findViewById(R.id.button3);

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

    public void oloklhrwshFun(View view){
        oloklhrwsh.setEnabled(false);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query query = ref.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("ProsParadwsh").orderByChild("hmerominia").equalTo(paketoHmer);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    if (ds.getValue()!=null){
                        ds.getRef().removeValue();
                    }
                }
                    Toast.makeText(Item_2_Activity.this, "Πακέτο παραδώθηκε!", Toast.LENGTH_SHORT).show();
                oloklhrwsh.setEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        Toast.makeText(this, "ID: "+ paketoHmer, Toast.LENGTH_SHORT).show();
    }

    public void showToast(String string) {
        if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
            toast = Toast.makeText(Item_2_Activity.this, string, Toast.LENGTH_LONG);
            toast.show();
        }
    }

}