package com.amtech.tahfizulquranonline.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.amtech.tahfizulquranonline.R;

public class TalibDetailsActivity extends AppCompatActivity {

    private String name, username, address, email, madarsaId;
    private TextView nameEdTxt, usernameEdTxt, addressEdTxt, emailEdTxt, madarsaIdEdTxt;
    private static final String TAG = "TalibDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talib_details);
        name = getIntent().getStringExtra("name");
        username = getIntent().getStringExtra("username");
        address = getIntent().getStringExtra("address");
        email = getIntent().getStringExtra("email");
        madarsaId = getIntent().getStringExtra("madarsaId");
        initView();
        setValues();
    }
    private void initView()
    {
        nameEdTxt = (TextView)findViewById(R.id.name_edtxt);
        usernameEdTxt = (TextView)findViewById(R.id.name_edtxt);
        addressEdTxt = (TextView)findViewById(R.id.address_edtxt);
        emailEdTxt = (TextView)findViewById(R.id.email_edtxt);
        madarsaIdEdTxt = (TextView)findViewById(R.id.madarsa_name_edtxt);
        findViewById(R.id.backBtn).setOnClickListener(v -> {
            onBackPressed();
        });
    }
    private void setValues()
    {
        nameEdTxt.setText(name);
        usernameEdTxt.setText(username);
        addressEdTxt.setText(address);
        emailEdTxt.setText(email);
        madarsaIdEdTxt.setText(madarsaId);
        Log.i(TAG, "setValues: "+name+username+address+email+madarsaId);
    }
}