package com.amtech.tahfizulquranonline.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amtech.tahfizulquranonline.AppDelegate;
import com.amtech.tahfizulquranonline.Constant;
import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.activity.LoginActivity;
import com.amtech.tahfizulquranonline.activity.MainActivity;
import com.amtech.tahfizulquranonline.activity.TalibMainActivity;
import com.amtech.tahfizulquranonline.adapter.MaulemSlotsAdapter;
import com.amtech.tahfizulquranonline.adapter.TalibSlotAdapter;
import com.amtech.tahfizulquranonline.models.SlotItem;
import com.amtech.tahfizulquranonline.utils.MyTimeCalculator;
import com.amtech.tahfizulquranonline.utils.TimeDiffFinder;
import com.amtech.tahfizulquranonline.v2.CreateModel;
import com.amtech.tahfizulquranonline.v2.RegisterSuccess;
import com.amtech.tahfizulquranonline.v2.RenewPaymentActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.amtech.tahfizulquranonline.utils.AppConstants.currentUserName;
import static com.amtech.tahfizulquranonline.utils.AppConstants.getProfileUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.talibClassSlotsUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.talibID;
import static com.amtech.tahfizulquranonline.utils.AppConstants.talibToken;

/**
 * Created by Shourav Paul on 16-12-2021.
 **/
public class TalibDashboardFragment extends Fragment {
    //transfer the code from Activity to Fragment after demo
    private RecyclerView recyclerView;
    private TalibSlotAdapter adapter;
    private List<SlotItem> slotList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private static final String TAG = "DashboardFragment";
    private TextView testCallBtn, talibNameTxt;
    private Handler handler;
    private TextView refreshBtn;
    private Context mContext;
    CreateModel createModel;
    TextView noDataFound;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_talib_dashboard, container, false);
        createModel = new CreateModel();
        testCallBtn = (TextView) view.findViewById(R.id.test_call_btn);
        refreshBtn = (TextView) view.findViewById(R.id.refresh_btn);
        noDataFound = view.findViewById(R.id.noDataTxt);
        NavigationView navigationView = getActivity().findViewById(R.id.navigation_view);
        View view1 = navigationView.getHeaderView(0);
        talibNameTxt = view1.findViewById(R.id.talib_name_txt);
        initRecycler(view);
        setListener();
        getProfile();
        return view;
    }

    private void initRecycler(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rec_slots);
        adapter = new TalibSlotAdapter(mContext, slotList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void setListener() {
//        refreshBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                adapter.notifyDataSetChanged();
//            }
//        });
    }

    private void fetchClassSlots() {

        StringRequest request = new StringRequest(Request.Method.GET, talibClassSlotsUrl + talibID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                //Toast.makeText(TalibHomeActivity.this, "Slot List", Toast.LENGTH_SHORT).show();
                String res = response.trim();
                slotList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                SlotItem model = new SlotItem();
                                model.setId(jsonObject1.getString("id"));
                                model.setMualimId(jsonObject1.getString("mualem_id"));
                                model.setDesc(jsonObject1.getString("description"));
                                model.setMadarsaId(jsonObject1.getString("madarsa_id"));
                                model.setStatus(jsonObject1.getString("status"));
                                model.setManageTimeId(jsonObject1.getString("manage_time_id"));
                                model.setDate(jsonObject1.getString("date"));
                                model.setTimeSlotId(jsonObject1.getString("time_slot_id"));
                                model.setStartTime(jsonObject1.getString("start_time"));
                                model.setEndTime(jsonObject1.getString("end_time"));
                                model.setSlotType(jsonObject1.getString("slot_type"));
                                slotList.add(model);
                                noDataFound.setText("No Record Found!");
                                noDataFound.setVisibility(View.GONE);
                            }
                            checkForNextActiveSlot();
                        } else {
                            //no record found
                            noDataFound.setText("No Record Found!");
                            noDataFound.setVisibility(View.VISIBLE);
                        }
                    } else {
                        //no record found
                        noDataFound.setText("No Classes Found!");
                        noDataFound.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, R.string.common_error, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onErrorResponse: " + error.getMessage());
                noDataFound.setText("No Record Found!");
                noDataFound.setVisibility(View.VISIBLE);
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "bearer " + talibToken);
                //headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);
    }

    private void checkForNextActiveSlot() {
        for (int i = 0; i < slotList.size(); i++) {
            SlotItem item = slotList.get(i);
            String strDate = item.getDate();
            String unFormatttedStTime = item.getStartTime();
            String formattedStTime = new MyTimeCalculator().formatTime(unFormatttedStTime);
            long diff = new TimeDiffFinder().getTimeDiff(strDate, formattedStTime);
            int secLeft = (int) (diff / 1000.0f);
            Log.i(TAG, "checkForNextActiveSlot: startTime: " + formattedStTime + " secleft = " + secLeft);
            if (secLeft > 0) {
                startActiveTimer(secLeft);
                return;
            }
        }
    }

    private void startActiveTimer(int secLeft) {
        secLeft = secLeft + 1;
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "run: activated");
                adapter.notifyDataSetChanged();
                checkForNextActiveSlot();
            }
        }, secLeft * 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeMessages(0);
        }
    }

    private void getProfile() {
        progressDialog = ProgressDialog.show(mContext, "", "Please wait...");
        StringRequest request = new StringRequest(Request.Method.GET, getProfileUrl + talibID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                fetchClassSlots();
                String res = response.trim();
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    boolean status = jsonObject.getBoolean("status");
                    JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                    JSONObject jsonObject12 = jsonArray1.getJSONObject(0);
                    Constant.changeName.chane(jsonObject12.optString("name"));

                    if (status) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        talibNameTxt.setText(jsonObject1.optString("name"));
                        String payment = jsonObject1.getString("pay_status");
                        String mobile = jsonObject1.getString("mobile");
                        if (payment.equals("null") || payment.equals("pending") || payment.equals("Pending")) {
                            createModel.setName(jsonObject1.getString("name"));
                            Constant.changeName.chane(createModel.getName());
                            createModel.setMobile(mobile);
                            createModel.setEmail(jsonObject1.getString("email"));
                            createModel.setMadarsa_id("1");
                            createModel.setTalib_ilm_type("1");
                            createModel.setTx_id("0");
                            createModel.setPay_status("Pending");
                            createModel.setPay_type("0");
                            createModel.setTrx_date("0");
                            createModel.setMadarsha_id("1");
                            createModel.setAmount("0");
                            AppDelegate.getInstance().setCreateModel(createModel);
                            RegisterSuccess
                                    registerSuccess
                                    =
                                    new RegisterSuccess();
                            registerSuccess.setUserId(jsonObject1.getInt("id"));
                            AppDelegate.getInstance().setRegisterSuccess(registerSuccess);
                            showDialogForPayment();
                        }
                    } else {
                        //no record found
                                 String message = jsonObject.getString("message");

//                        Toast.makeText(mContext, "No Record Found!", Toast.LENGTH_SHORT).show();
                        if (message.equalsIgnoreCase("Your plan is expire")) {
                            JSONObject jsonObject1 = jsonObject.getJSONArray("data").getJSONObject(0);
                            talibNameTxt.setText(jsonObject1.optString("name"));
                            // String payment = jsonObject1.getString("pay_status");
                            String mobile = jsonObject1.getString("mobile");

                            createModel.setName(jsonObject1.getString("name"));
                            Constant.changeName.chane(createModel.getName());
                            createModel.setMobile(mobile);
                            createModel.setEmail(jsonObject1.getString("email"));
                            createModel.setMadarsa_id("1");
                            createModel.setTalib_ilm_type("1");
                            createModel.setTx_id("0");
                            createModel.setPay_status("Pending");
                            createModel.setPay_type("0");
                            createModel.setTrx_date("0");
                            createModel.setMadarsha_id("1");
                            createModel.setAmount("0");
                            AppDelegate.getInstance().setCreateModel(createModel);
                            RegisterSuccess
                                    registerSuccess
                                    =
                                    new RegisterSuccess();
                            registerSuccess.setUserId(jsonObject1.getInt("id"));
                            AppDelegate.getInstance().setRegisterSuccess(registerSuccess);
                            showDialogForPaymentForRenew();


                        }
                        noDataFound.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "json exception : " + e.getMessage());
                }
                //  TalibMainActivity.changeName(createModel.getName());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: " + error.getMessage());
            }
        });
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);
    }

    public void showDialogForPayment() {
        Dialog dialog = new Dialog(getActivity(), R.style.SheetDialog);
        dialog.setContentView(R.layout.show_pending_payment_dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.findViewById(R.id.logout).setOnClickListener(v -> {
            dialog.dismiss();
            SharedPreferences pref = getActivity().getSharedPreferences("Tahfizul", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
            dialog.dismiss();
            currentUserName = "";
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });
        dialog.findViewById(R.id.payment).setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(getActivity(), RenewPaymentActivity.class));
            getActivity().finish();
        });
        dialog.show();
    }

    public void showDialogForPaymentForRenew() {
        Dialog dialog = new Dialog(getActivity(), R.style.SheetDialog);
        dialog.setContentView(R.layout.show_pending_payment_dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.findViewById(R.id.logout).setOnClickListener(v -> {
            dialog.dismiss();
            SharedPreferences pref = getActivity().getSharedPreferences("Tahfizul", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
            dialog.dismiss();
            currentUserName = "";
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });
        TextView textView = dialog.findViewById(R.id.message);
        textView.setText("Your Plan is Expired!\n Please Renew the subscription for continue");
        dialog.findViewById(R.id.payment).setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(getActivity(), RenewPaymentActivity.class));
            getActivity().finish();
        });
        dialog.show();
    }
}
