package com.amtech.tahfizulquranonline.v2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.amtech.tahfizulquranonline.AppDelegate;
import com.amtech.tahfizulquranonline.LoadingDialog;
import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.V2Data.ApiClient;
import com.amtech.tahfizulquranonline.V2Data.ApiInterface;
import com.amtech.tahfizulquranonline.adapter.MoulimListAdapter;
import com.amtech.tahfizulquranonline.databinding.ActivityMoulimProfileactivityBinding;
import com.amtech.tahfizulquranonline.v2.M.GetMoulimsResponse;
import com.amtech.tahfizulquranonline.v2.M.MoulimsItems;
import com.amtech.tahfizulquranonline.v2.moulimProfile.GetMoulimProfileResponse;
import com.amtech.tahfizulquranonline.v2.moulimProfile.MProfileItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoulimProfileActivity extends AppCompatActivity {
    ActivityMoulimProfileactivityBinding binding;
    ApiClient apiClient;
    MoulimProfileAdapter allPaymentAdapter;
    public ApiInterface service;
    String ivImage;
    LoadingDialog loadingDialog;
    CreateModel createModel;
    int TeacherID, CourceID = -1, TIMEID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_moulim_profileactivity);
        createModel = AppDelegate.getInstance().getCreateModel();
        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });
        loadingDialog = new LoadingDialog(this);
        binding.moulimNamelb.setText(getIntent().getStringExtra("name"));
        ivImage = getIntent().getStringExtra("profile");
        TeacherID = getIntent().getIntExtra("id", -1);
        binding.aboutEdtxt.setText(getIntent().getStringExtra("about"));
        binding.experienceTxt.setText(getIntent().getStringExtra("exp"));
        binding.experienceTxt.setText(getIntent().getStringExtra("exp"));
        RequestOptions reqOpt = RequestOptions.fitCenterTransform().transform(new RoundedCorners(5)).diskCacheStrategy(DiskCacheStrategy.ALL) // It will cache your image after loaded for first time
                ;
        Glide.with(this).load("https://www.tahfizulquranonline.com/uploads/" + ivImage).thumbnail(/*sizeMultiplier=*/ 0.25f).apply(reqOpt).placeholder(R.drawable.moulim).error(R.drawable.moulim).into(binding.moulimProfileImg);
        binding.submitBtn.setOnClickListener(v -> {
            if (CourceID == -1) {
                Toast.makeText(this, "Please Select Course", Toast.LENGTH_SHORT).show();
            } else {
                login();
            }
        });
        getData();
    }

    private void getData() {
        loadingDialog.show();
        apiClient = new ApiClient();
        service = ApiClient.createService(ApiInterface.class);
        Call<GetMoulimProfileResponse> call = service.getMoulimsProfile(String.valueOf("25") , String.valueOf(TeacherID));
        call.enqueue(new Callback<GetMoulimProfileResponse>() {
            @Override
            public void onResponse(Call<GetMoulimProfileResponse> call, Response<GetMoulimProfileResponse> response) {
                if (response.body() != null) {
                    GetMoulimProfileResponse allPaymentModel = response.body();
                    setRecycler(allPaymentModel.getData());
                } else {
                    loadingDialog.hide();
                    Toast.makeText(MoulimProfileActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GetMoulimProfileResponse> call, Throwable t) {
                loadingDialog.hide();
            }
        });
    }

    public void setSelected(int id, int time) {
        CourceID = id;
        TIMEID = time;

    }

    private void setRecycler(List<MProfileItem> list) {
        if (list.size() == 0) {
            binding.nodata.setVisibility(View.VISIBLE);
            binding.submitBtn.setVisibility(View.GONE);
        }
        allPaymentAdapter = new MoulimProfileAdapter(this, list, this);
        binding.moulimListRc.setLayoutManager(new GridLayoutManager(this, 2));
        binding.moulimListRc.setAdapter(allPaymentAdapter);
        loadingDialog.hide();
    }

    private void login() {
//
//      //  loadingDialog.show();
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("email", createModel.email);
//        params.put("mobile", createModel.mobile);
//        params.put("madarsha_id", createModel.madarsha_id);
//        params.put("fname", createModel.fname);
//        params.put("sub_id", createModel.sub_id);
//        params.put("name", createModel.name);
//        params.put("talib_ilm_type", createModel.talib_ilm_type);
//        params.put("course_id", String.valueOf(CourceID));
//        params.put("country_id", createModel.country_id);
//        params.put("state_id", createModel.state_id);
//        params.put("city_id", createModel.city_id);
//        params.put("address", createModel.address);
//        params.put("password", createModel.password);
//        params.put("madarsa_id", createModel.madarsa_id);
//        params.put("aadhar_no", createModel.aadhar_no);
//        params.put("dob", createModel.dob);
//        params.put("tx_id", createModel.tx_id);
//        params.put("pay_status", createModel.pay_status);
//        params.put("amount", createModel.amount);
//        params.put("trx_date", createModel.trx_date);
//        params.put("pay_type", createModel.pay_type);
//        params.put("teacher_id", String.valueOf(TeacherID));
//        params.put("timing_id", String.valueOf(TIMEID));

        createModel.setCountry_id(String.valueOf(CourceID));
        createModel.setTeacherID(String.valueOf(TeacherID));
        createModel.setTimeID(String.valueOf(TIMEID));

        AppDelegate.getInstance().setCreateModel(createModel);
        startActivity(new Intent(MoulimProfileActivity.this, SubscriptionActivity.class));
//        ApiClient apiClient = new ApiClient();
//        ApiInterface service = apiClient.createService(ApiInterface.class);
//        Call<RegisterSuccess> call = service.userRegister(params);
//        call.enqueue(new Callback<RegisterSuccess>() {
//            @Override
//            public void onResponse(Call<RegisterSuccess> call, Response<RegisterSuccess> response) {
//                if (response != null) {
//                    RegisterSuccess registerSuccess = response.body();
//                    if (registerSuccess != null && registerSuccess.getMessage().equalsIgnoreCase("Success")) {
//                        AppDelegate.getInstance().setCreateModel(createModel);
//                        AppDelegate.getInstance().setRegisterSuccess(registerSuccess);
//                        startActivity(new Intent(MoulimProfileActivity.this, SubscriptionActivity.class));
//                    } else {
//                        Toast.makeText(MoulimProfileActivity.this, "Something Wend Wrong", Toast.LENGTH_SHORT).show();
//                    }
//                    loadingDialog.hide();
//                } else {
//                    Toast.makeText(MoulimProfileActivity.this, "Something Wend Wrong", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RegisterSuccess> call, Throwable t) {
//                loadingDialog.hide();
//                Toast.makeText(MoulimProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}