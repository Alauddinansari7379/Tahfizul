package com.amtech.tahfizulquranonline.v2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.amtech.tahfizulquranonline.AppDelegate;
import com.amtech.tahfizulquranonline.LoadingDialog;
import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.V2Data.ApiClient;
import com.amtech.tahfizulquranonline.V2Data.ApiInterface;
import com.amtech.tahfizulquranonline.V2Data.CommonResponse;
import com.amtech.tahfizulquranonline.V2Data.countryModel.CountryModel;
import com.amtech.tahfizulquranonline.V2Data.plan.PlanItem;
import com.amtech.tahfizulquranonline.V2Data.plan.PlanResponse;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscriptionActivity extends AppCompatActivity {
    ViewPagerAdapter viewPagerAdapter;
    PageIndicatorView pageIndicatorView;
    LoadingDialog loadingDialog;
    List<PlanItem> planItemList = new ArrayList<>();
    ViewPager viewPager;
    TextView select;
    CreateModel createModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        createModel = AppDelegate.instance.getCreateModel();
        loadingDialog = new LoadingDialog(this);
        viewPager = findViewById(R.id.introViewPager);
        ImageView backBtn = findViewById(R.id.backBtn);
        select = findViewById(R.id.submit_btn);
        pageIndicatorView = findViewById(R.id.pageIndicatorView);
        getCountry();
        backBtn.setOnClickListener(v -> {
            finish();
        });
        select.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() == 0) {
                createModel.amount = planItemList.get(0).getPrice();
                createModel.madarsa_id = String.valueOf(planItemList.get(0).getMadarshaId());
                createModel.sub_id = String.valueOf(planItemList.get(0).getId());
                AppDelegate.getInstance().setCreateModel(createModel);
            }
            if (viewPager.getCurrentItem() == 1) {
                createModel.amount = planItemList.get(1).getPrice();
                createModel.madarsa_id = String.valueOf(planItemList.get(1).getMadarshaId());
                createModel.sub_id = String.valueOf(planItemList.get(1).getId());
                AppDelegate.getInstance().setCreateModel(createModel);
            }
            if (viewPager.getCurrentItem() == 2) {
                createModel.amount = planItemList.get(2).getPrice();
                createModel.madarsa_id = String.valueOf(planItemList.get(2).getMadarshaId());
                createModel.sub_id = String.valueOf(planItemList.get(2).getId());
                AppDelegate.getInstance().setCreateModel(createModel);
            }
            startActivity(new Intent(SubscriptionActivity.this, PaymentActivity.class));
        });
        // setting up the adapter

    }

    private void getCountry() {
        loadingDialog.show();
        ApiInterface service = ApiClient.createService(ApiInterface.class);
        Call<PlanResponse> call = service.getPlan();
        call.enqueue(new Callback<PlanResponse>() {
            @Override
            public void onResponse(Call<PlanResponse> call, Response<PlanResponse> response) {
                PlanResponse planResponse = response.body();
                if (planResponse != null) {
                    planItemList.addAll(planResponse.getData());
                    viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                    if (planItemList.size() == 1) {
                        viewPagerAdapter.add(new FirstSubscriptionFragment(planItemList.get(0)), "Page 1");
                    }
                    if (planItemList.size() == 2) {
                        viewPagerAdapter.add(new FirstSubscriptionFragment(planItemList.get(0)), "Page 1");
                        viewPagerAdapter.add(new FirstSubscriptionFragment(planItemList.get(1)), "Page 2");
                    }
                    if (planItemList.size() == 3) {
                        viewPagerAdapter.add(new FirstSubscriptionFragment(planItemList.get(0)), "Page 1");
                        viewPagerAdapter.add(new FirstSubscriptionFragment(planItemList.get(1)), "Page 2");
                        viewPagerAdapter.add(new FirstSubscriptionFragment(planItemList.get(2)), "Page 3");
                    }
                    if (planItemList.size() == 4
                    ) {
                        viewPagerAdapter.add(new FirstSubscriptionFragment(planItemList.get(0)), "Page 1");
                        viewPagerAdapter.add(new FirstSubscriptionFragment(planItemList.get(1)), "Page 2");
                        viewPagerAdapter.add(new FirstSubscriptionFragment(planItemList.get(2)), "Page 3");
                        viewPagerAdapter.add(new FirstSubscriptionFragment(planItemList.get(3)), "Page 3");
                    }
//                    if (planItemList.size() == 5
//                    ) {
//                        viewPagerAdapter.add(new FirstSubscriptionFragment(planItemList.get(0)), "Page 1");
//                        viewPagerAdapter.add(new FirstSubscriptionFragment(planItemList.get(1)), "Page 2");
//                        viewPagerAdapter.add(new FirstSubscriptionFragment(planItemList.get(2)), "Page 3");
//                        viewPagerAdapter.add(new FirstSubscriptionFragment(planItemList.get(3)), "Page 4");
//                        viewPagerAdapter.add(new FirstSubscriptionFragment(planItemList.get(4)), "Page 5");
//                    }
//                    if (planItemList.size() == 6
//                    ) {
//                        viewPagerAdapter.add(new FirstSubscriptionFragment(planItemList.get(0)), "Page 1");
//                        viewPagerAdapter.add(new FirstSubscriptionFragment(planItemList.get(1)), "Page 2");
//                        viewPagerAdapter.add(new FirstSubscriptionFragment(planItemList.get(2)), "Page 3");
//                        viewPagerAdapter.add(new FirstSubscriptionFragment(planItemList.get(3)), "Page 4");
//                        viewPagerAdapter.add(new FirstSubscriptionFragment(planItemList.get(4)), "Page 5");
//                        viewPagerAdapter.add(new FirstSubscriptionFragment(planItemList.get(5)), "Page 6");
//                    }
                    pageIndicatorView.setViewPager(viewPager);
                    viewPager.setAdapter(viewPagerAdapter);
                    pageIndicatorView.setVisibility(View.VISIBLE);
                    select.setVisibility(View.VISIBLE);
                } else {
                    Log.d("TAG", "onResponseSSS: ");
                }
                loadingDialog.hide();
            }

            @Override
            public void onFailure(Call<PlanResponse> call, Throwable t) {
                Log.d("TAG", "onResponseSSS: " + t.getMessage());
                loadingDialog.hide();
            }
        });
    }
//
//    private void login() {
//        loadingDialog.show();
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("email", createModel.email);
//        params.put("mobile", createModel.mobile);
//        params.put("madarsha_id", createModel.madarsha_id);
//        params.put("fname", createModel.fname);
//        params.put("sub_id", createModel.sub_id);
//        params.put("name", createModel.name);
//        params.put("talib_ilm_type", createModel.talib_ilm_type);
//        params.put("course_id", createModel.course_id);
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
//        ApiClient apiClient = new ApiClient();
//        ApiInterface service = apiClient.createService(ApiInterface.class);
//        Call<RegisterSuccess> call = service.userRegister(params);
//        call.enqueue(new Callback<RegisterSuccess>() {
//            @Override
//            public void onResponse(Call<RegisterSuccess> call, Response<RegisterSuccess> response) {
//
//                loadingDialog.hide();
//            }
//
//            @Override
//            public void onFailure(Call<RegisterSuccess> call, Throwable t) {
//                loadingDialog.hide();
//                Toast.makeText(SubscriptionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    @Override
//    public void onBackPressed() {
//        Toast.makeText(this, "You are already registered. Complete the payment to continue", Toast.LENGTH_SHORT).show();
//    }
}