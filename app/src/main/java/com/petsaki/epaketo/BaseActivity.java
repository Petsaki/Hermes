package com.petsaki.epaketo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayDeque;
import java.util.Deque;

public class BaseActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Deque<Integer> integerDeque = new ArrayDeque<>(3);
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Epaketo);
        setContentView(R.layout.activity_base);

        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navi);

        integerDeque.push(R.id.bn_home);
        loadFragment(new OldHomeFragment());

        bottomNavigationView.setSelectedItemId(R.id.bn_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();
                        if (integerDeque.contains(id)){


                            if(integerDeque.getFirst()== id){
                                Toast.makeText(BaseActivity.this, "SAME", Toast.LENGTH_LONG).show();
                                return false;
                            }
                            integerDeque.remove(id);
                        }

                        integerDeque.push(id);
                        loadFragment(getFragment(item.getItemId()));

                        return true;
                    }
                }
        );
    }

    private Fragment getFragment(int itemId) {
        switch (itemId){
            case R.id.bn_home:
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                return new OldHomeFragment();
            case R.id.bn_paketa:
                bottomNavigationView.getMenu().getItem(1).setChecked(true);
                return new PaketaFragment();
            case R.id.bn_profile:
                bottomNavigationView.getMenu().getItem(2).setChecked(true);
                return new ProfileFragment();
        }
        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        return new OldHomeFragment();
    }

    private void loadFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.basefragment, fragment,fragment.getClass().getSimpleName())
                .commit();
    }

    @Override
    public void onBackPressed() {
        integerDeque.pop();
        if (!integerDeque.isEmpty()){
            loadFragment(getFragment(integerDeque.peek()));
        }else {
            finish();
        }
    }

/*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, bottomNavigationView);
        super.onSaveInstanceState(outState);
    }*/
}