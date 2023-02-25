package com.amtech.tahfizulquranonline.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.adapter.MaulemSlotsAdapter;
import com.amtech.tahfizulquranonline.models.SlotItem;
import com.amtech.tahfizulquranonline.record.MainActivity;
import com.amtech.tahfizulquranonline.utils.MyTimeCalculator;
import com.amtech.tahfizulquranonline.utils.TimeDiffFinder;
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
import static com.amtech.tahfizulquranonline.utils.AppConstants.getMaulimProfileUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.maulimClassSlotsUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.maulimID;
import static com.amtech.tahfizulquranonline.utils.AppConstants.maulimToken;

/**
 * Created by Shourav Paul on 16-12-2021.
 **/
public class DashboardFragment extends Fragment {

    //transfer the code from Activity to Fragment after demo
    private RecyclerView recyclerView;
    private MaulemSlotsAdapter adapter;
    private List<SlotItem> slotList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private static final String TAG = "DashboardFragment";
    private TextView testCallBtn, maulimNameTxt;
    private Handler handler;
    private TextView refreshBtn;
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        testCallBtn = (TextView) view.findViewById(R.id.test_call_btn);
        refreshBtn = (TextView) view.findViewById(R.id.refresh_btn);
        NavigationView navigationView = getActivity().findViewById(R.id.navigation_view);
        View view1 = navigationView.getHeaderView(0);
        maulimNameTxt = view1.findViewById(R.id.maulim_name_txt);
        initRecycler(view);
        setListener();
        getProfile();
        return view;
    }

    private void initRecycler(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rec_slots);
        adapter = new MaulemSlotsAdapter(mContext, slotList);
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
        StringRequest request = new StringRequest(Request.Method.GET, maulimClassSlotsUrl + maulimID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                //Toast.makeText(MualemHomeActivity.this, "Slot List", Toast.LENGTH_SHORT).show();
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
                                model.setTalibIlmId(jsonObject1.getString("talib_ilm_id"));
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
                            }
                            checkForNextActiveSlot();
                        } else {
                            //no record found
                            Toast.makeText(mContext, "No Record Found!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //no record found
                        Toast.makeText(mContext, "No Classes Found!", Toast.LENGTH_SHORT).show();
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
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "bearer " + maulimToken);
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
        StringRequest request = new StringRequest(Request.Method.GET, getMaulimProfileUrl + maulimID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                fetchClassSlots();
                String res = response.trim();
                Log.i(TAG, "onResponse: " + res);
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        currentUserName = jsonObject1.optString("name");
                        maulimNameTxt.setText(currentUserName);
                    } else {
                        //no record found
                        Toast.makeText(mContext, "No Record Found!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "json exception : " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: " + error.getMessage());
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "bearer " + maulimToken);
                //headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);
    }
}
