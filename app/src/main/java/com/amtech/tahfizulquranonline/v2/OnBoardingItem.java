package com.amtech.tahfizulquranonline.v2;

import androidx.fragment.app.Fragment;

public class OnBoardingItem {
    Fragment fragment;

    public OnBoardingItem(Fragment fragment) {
        this.fragment = fragment;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
