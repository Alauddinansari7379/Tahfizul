package com.amtech.tahfizulquranonline.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.adapter.VPAdapter;
import com.amtech.tahfizulquranonline.fragment.MualemLoginFragment;
import com.amtech.tahfizulquranonline.fragment.TalibLoginFragment;
import com.amtech.tahfizulquranonline.fragment.WalidainLoginFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class LoginActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private VPAdapter vpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

    }

    private void initView() {
        viewPager2 = (ViewPager2) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        vpAdapter = new VPAdapter(getSupportFragmentManager(), getLifecycle());
        vpAdapter.addFragment(new WalidainLoginFragment(), "Walidain");
        vpAdapter.addFragment(new MualemLoginFragment(), "Mualem");
        vpAdapter.addFragment(new TalibLoginFragment(), "Talib");
        viewPager2.setAdapter(vpAdapter);
        viewPager2.setOffscreenPageLimit(1);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            tab.setText(vpAdapter.fragmentTitle.get(position));
        }).attach();
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TextView tv = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabLayout.getTabAt(i).setCustomView(tv);
        }
        if (getIntent().getBooleanExtra("isRegister", false)) {
            TabLayout.Tab tab = tabLayout.getTabAt(2);
            tab.select();
        }
    }
}