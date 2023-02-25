package com.amtech.tahfizulquranonline.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

/**
 * Created by Shourav Paul on 01-12-2021.
 **/
public class VPAdapter extends FragmentStateAdapter {

    public final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    public final ArrayList<String> fragmentTitle = new ArrayList<>();

    public VPAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentArrayList.size();
    }
    public void addFragment(Fragment fragment, String title)
    {
        fragmentArrayList.add(fragment);
        fragmentTitle.add(title);
    }
}
