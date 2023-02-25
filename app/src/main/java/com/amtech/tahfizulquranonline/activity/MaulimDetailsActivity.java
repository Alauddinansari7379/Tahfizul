package com.amtech.tahfizulquranonline.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.amtech.tahfizulquranonline.R;

public class MaulimDetailsActivity extends AppCompatActivity {

    private String name, about, address, email, madarsaId, mobile;
    private TextView nameEdTxt, aboutEdTxt, addressEdTxt, emailEdTxt, madarsaIdEdTxt, mobileEdTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maulim_details);
        name = getIntent().getStringExtra("name");
        about = getIntent().getStringExtra("about");
        address = getIntent().getStringExtra("address");
        email = getIntent().getStringExtra("email");
        madarsaId = getIntent().getStringExtra("madarsaId");
        mobile = getIntent().getStringExtra("mobile");
        initView();
        setValues();
    }

    private void initView() {
        nameEdTxt = (TextView) findViewById(R.id.name_edtxt);
        aboutEdTxt = (TextView) findViewById(R.id.about_edtxt);
        addressEdTxt = (TextView) findViewById(R.id.address_edtxt);
        emailEdTxt = (TextView) findViewById(R.id.email_edtxt);
        mobileEdTxt = (TextView) findViewById(R.id.mobile_edtxt);
        madarsaIdEdTxt = (TextView) findViewById(R.id.madarsa_name_edtxt);
        findViewById(R.id.backBtn).setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void setValues() {
        nameEdTxt.setText(name);
        aboutEdTxt.setText(about);
        addressEdTxt.setText(address);
        emailEdTxt.setText(email);
        mobileEdTxt.setText(mobile);
        madarsaIdEdTxt.setText(madarsaId);
    }
}