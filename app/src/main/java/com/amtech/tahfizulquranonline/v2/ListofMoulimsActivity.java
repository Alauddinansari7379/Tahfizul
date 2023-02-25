package com.amtech.tahfizulquranonline.v2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amtech.tahfizulquranonline.LoadingDialog;
import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.V2Data.ApiClient;
import com.amtech.tahfizulquranonline.V2Data.ApiInterface;
import com.amtech.tahfizulquranonline.adapter.MoulimListAdapter;
import com.amtech.tahfizulquranonline.databinding.ActivityListofMoulimsBinding;
import com.amtech.tahfizulquranonline.models.AllPaymentModel;
import com.amtech.tahfizulquranonline.v2.M.MoulimsItems;
import com.amtech.tahfizulquranonline.v2.M.GetMoulimsResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListofMoulimsActivity extends AppCompatActivity {
    ActivityListofMoulimsBinding binding;
    ApiClient apiClient;
    MoulimListAdapter allPaymentAdapter;
    public ApiInterface service;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_listof_moulims);
        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });
        loadingDialog = new LoadingDialog(this);
        getData();
    }

    private void getData() {
        loadingDialog.show();
        apiClient = new ApiClient();
        service = ApiClient.createService(ApiInterface.class);
        Call<GetMoulimsResponse> call = service.getMoulims(String.valueOf(25));
        call.enqueue(new Callback<GetMoulimsResponse>() {
            @Override
            public void onResponse(Call<GetMoulimsResponse> call, Response<GetMoulimsResponse> response) {
                if (response.body() != null) {
                    GetMoulimsResponse allPaymentModel = response.body();
                    setRecycler(allPaymentModel.getData());
                } else {
                    loadingDialog.hide();
                    Toast.makeText(ListofMoulimsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<GetMoulimsResponse> call, Throwable t) {
                loadingDialog.hide();
            }
        });
    }


    private void setRecycler(List<MoulimsItems> list) {
        allPaymentAdapter = new MoulimListAdapter(this, list);
        binding.moulimListRc.setLayoutManager(new LinearLayoutManager(this));
        binding.moulimListRc.setAdapter(allPaymentAdapter);
        loadingDialog.hide();
    }
}