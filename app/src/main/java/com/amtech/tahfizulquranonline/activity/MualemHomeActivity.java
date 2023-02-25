package com.amtech.tahfizulquranonline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.adapter.MaulemSlotsAdapter;
import com.amtech.tahfizulquranonline.models.SlotItem;
import com.amtech.tahfizulquranonline.utils.MyTimeCalculator;
import com.amtech.tahfizulquranonline.utils.TimeDiffFinder;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.amtech.tahfizulquranonline.utils.AppConstants.maulimClassSlotsUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.maulimID;
import static com.amtech.tahfizulquranonline.utils.AppConstants.maulimToken;

public class MualemHomeActivity extends AppCompatActivity {
    //transfer the code from Activity to Fragment after demo
    private RecyclerView recyclerView;
    private MaulemSlotsAdapter adapter;
    private List<SlotItem> slotList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private static final String TAG = "MualemHomeActivity";
    private TextView testCallBtn;
    private Handler handler;
    private TextView refreshBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mualem_home);
        testCallBtn = (TextView)findViewById(R.id.test_call_btn);
        refreshBtn = (TextView)findViewById(R.id.refresh_btn);
        initRecycler();
        setListener();
        fetchClassSlots();
    }
    private void initRecycler()
    {
        recyclerView = (RecyclerView)findViewById(R.id.rec_slots);
        adapter = new MaulemSlotsAdapter(MualemHomeActivity.this, slotList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }
    private void setListener()
    {
        testCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions("2021-12-16", "03:25:00", "03:30:00", maulimID, "8");
            }
        });
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
            }
        });
    }
    private void fetchClassSlots()
    {
        progressDialog = ProgressDialog.show(this, "", "Please wait...");
        StringRequest request = new StringRequest(Request.Method.GET, maulimClassSlotsUrl+maulimID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                //Toast.makeText(MualemHomeActivity.this, "Slot List", Toast.LENGTH_SHORT).show();
                String res = response.trim();
                slotList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    boolean status = jsonObject.getBoolean("status");
                    if(status)
                    {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if(jsonArray.length() > 0)
                        {
                            for(int i = 0; i < jsonArray.length(); i++)
                            {
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
                        }
                        else
                        {
                            //no record found
                            Toast.makeText(MualemHomeActivity.this, "No Record Found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        //no record found
                        Toast.makeText(MualemHomeActivity.this, "No Classes Found!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MualemHomeActivity.this, R.string.common_error, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onErrorResponse: "+error.getMessage());
            }
        }){

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "bearer "+maulimToken);
                //headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(MualemHomeActivity.this);
        queue.add(request);
    }
    private void checkForNextActiveSlot()
    {
        for(int i = 0; i < slotList.size(); i++)
        {
            SlotItem item = slotList.get(i);
            String strDate = item.getDate();
            String unFormatttedStTime = item.getStartTime();
            String formattedStTime = new MyTimeCalculator().formatTime(unFormatttedStTime);
            long diff = new TimeDiffFinder().getTimeDiff(strDate, formattedStTime);
            int secLeft = (int) (diff/1000.0f);
            Log.i(TAG, "checkForNextActiveSlot: startTime: "+formattedStTime+" secleft = "+secLeft);
            if(secLeft > 0)
            {
                startActiveTimer(secLeft);
                return;
            }
        }
    }
    private void startActiveTimer(int secLeft)
    {
        secLeft = secLeft+1;
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "run: activated");
                adapter.notifyDataSetChanged();
                checkForNextActiveSlot();
            }
        }, secLeft*1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(handler!=null)
        {
            handler.removeMessages(0);
        }
    }

    //test
    private void checkPermissions(String bookDate, String startTime, String endTime, String maulimId, String talibId)
    {
        Dexter.withActivity(MualemHomeActivity.this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        callAction(bookDate, startTime, endTime, maulimId, talibId);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    private void callAction(String strDate, String startTime, String endTime, String maulimId, String talibId)
    {
        if(new MyTimeCalculator().inTimeFrame(strDate, startTime, endTime))//check appointment date n time with current date n time
        {
            long diff = new TimeDiffFinder().getTimeDiff(strDate, endTime);
            int secLeft = (int) (diff/1000.0f);
            if(secLeft > 0)
            {
                Intent intent = new Intent(MualemHomeActivity.this, JitsiCallActivity.class);
                intent.putExtra("maulim_id", maulimId);
                intent.putExtra("talib_id", talibId);
                intent.putExtra("time_left", secLeft);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(MualemHomeActivity.this, "Your session is over!", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            long diff = new TimeDiffFinder().getTimeDiff(strDate, endTime);
            int secLeft = (int) (diff/1000.0f);
            if(secLeft > 0)
            {
                Toast.makeText(MualemHomeActivity.this, "Its not Class time!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(MualemHomeActivity.this, "Your Class is over!", Toast.LENGTH_SHORT).show();
            }
        }

        //////////////////////

    }
    //test
}