package com.amtech.tahfizulquranonline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.V2Data.ApiClient;
import com.amtech.tahfizulquranonline.V2Data.ApiInterface;
import com.amtech.tahfizulquranonline.adapter.AllPaymentAdapter;
import com.amtech.tahfizulquranonline.databinding.ActivityAllPaymentBinding;
import com.amtech.tahfizulquranonline.utils.AppConstants;
import com.amtech.tahfizulquranonline.v2.payment.DataItem;
import com.amtech.tahfizulquranonline.v2.payment.PaymentResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllPaymentActivity extends AppCompatActivity {

    ActivityAllPaymentBinding binding;
    ApiClient apiClient;
    List<DataItem> list = new ArrayList<>();
    AllPaymentAdapter allPaymentAdapter;
    public ApiInterface service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_payment);
        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });
        getAllPayment();
    }

    private void getAllPayment() {
        apiClient = new ApiClient();
        service = ApiClient.createService(ApiInterface.class);
        Call<PaymentResponse> call = service.getAllPayment(String.valueOf(Integer.parseInt(AppConstants.talibID)));
        call.enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                if (response.body() != null) {
                    list = response.body().getData();
                    setRecycler(list);
                } else {
                    Toast.makeText(AllPaymentActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {

            }
        });
    }


    private void setRecycler(List<DataItem> list) {
        Log.d("TAG", "setRecyclerDAta: " + list);
        allPaymentAdapter = new AllPaymentAdapter(this, list);
        binding.allPaymentRc.setLayoutManager(new LinearLayoutManager(this));
        binding.allPaymentRc.setAdapter(allPaymentAdapter);
    }
}