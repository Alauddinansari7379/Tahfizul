package com.amtech.tahfizulquranonline.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.adapter.WalidFeedbackAdapter;
import com.amtech.tahfizulquranonline.models.FeedBackModel;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.amtech.tahfizulquranonline.utils.AppConstants.getMaulimFeedbackUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.getWalidFeedbackUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.maulimID;
import static com.amtech.tahfizulquranonline.utils.AppConstants.maulimToken;
import static com.amtech.tahfizulquranonline.utils.AppConstants.walidID;
import static com.amtech.tahfizulquranonline.utils.AppConstants.walidToken;

public class MaulimFeedbackFragment extends Fragment {

    private static final String TAG = "MaulimFeedbackFragment";
    private Context mContext;
    private RecyclerView recView;
    private WalidFeedbackAdapter adapter;
    private List<FeedBackModel> list = new ArrayList<>();
    private ProgressDialog progressDialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maulim_feedback, container, false);
        initView(view);
        initRec();
        getMaulimFeedbackAction();
        return view;
    }
    private void initView(View view)
    {
        recView = (RecyclerView)view.findViewById(R.id.rec_view);
    }
    private void initRec()
    {
        adapter = new WalidFeedbackAdapter(mContext, list);
        recView.setLayoutManager(new LinearLayoutManager(mContext));
        recView.setHasFixedSize(true);
        recView.setAdapter(adapter);
    }
    private void getMaulimFeedbackAction()
    {
        progressDialog = ProgressDialog.show(mContext, "Please wait", "Fetching all feedbacks...");
        StringRequest request = new StringRequest(Request.Method.GET, getMaulimFeedbackUrl + maulimID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String res = response.trim();
                Log.i(TAG, "onResponse: "+res);
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    boolean status = jsonObject.getBoolean("status");
                    if(status)
                    {
                        list.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for(int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            FeedBackModel model = new FeedBackModel();
                            model.setFeedBack(jsonObject1.optString("feedback"));
                            model.setRating(jsonObject1.optString("rating"));
                            model.setName(jsonObject1.optString("walid_name"));
                            list.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        //no record found

                        Toast.makeText(mContext, "No Record Found!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                    Log.i(TAG, "json exception : "+e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);
    }



}
