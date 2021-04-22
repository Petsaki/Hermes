package com.petsaki.epaketo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;


//EINAI GIA TA TABS APO TA ItemActivity kai Item_2_Activity. Den ckerw akribws ti kanei :)
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList=new ArrayList<>();
    private List<String> stringList=new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return stringList.get(position);
    }

    public void addFragment(Fragment fragment, String title){
        fragmentList.add(fragment);
        stringList.add(title);
    }
}
