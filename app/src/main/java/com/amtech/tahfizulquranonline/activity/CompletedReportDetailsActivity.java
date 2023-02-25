package com.amtech.tahfizulquranonline.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.amtech.tahfizulquranonline.R;

public class CompletedReportDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_report_details);
        findViewById(R.id.backBtn).setOnClickListener(v -> {
            onBackPressed();
        });
    }
}