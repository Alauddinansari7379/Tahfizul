package com.amtech.tahfizulquranonline.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.utils.Helper;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import static com.amtech.tahfizulquranonline.utils.AppConstants.getWalidProfileUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.walidID;
import static com.amtech.tahfizulquranonline.utils.AppConstants.walidToken;

/**
 * Created by Shourav Paul on 10-02-2022.
 **/
public class WalidProfileFragment extends Fragment {
    
    private TextView nameTxt, emailTxt, phoneTxt, MadarsaNameTxt, walidAddTxt;
    private ProgressDialog progressDialog;
    private Context mContext;
    private static final String TAG = "WalidProfileFragment";
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_walid_profile, container, false);
        initView(view);
        getProfile();
        return view;
    }
    private void initView(View v)
    {
        nameTxt = (TextView)v.findViewById(R.id.name_edtxt);
        emailTxt = (TextView)v.findViewById(R.id.email_edtxt);
        phoneTxt = (TextView)v.findViewById(R.id.mobile_edtxt);
        MadarsaNameTxt = (TextView)v.findViewById(R.id.madarsa_name_edtxt);
        walidAddTxt = (TextView)v.findViewById(R.id.walid_add_txt);
    }
    private void getProfile()
    {
        progressDialog = ProgressDialog.show(mContext, "", "Please wait...");
        StringRequest request = new StringRequest(Request.Method.GET, getWalidProfileUrl+walidID, new Response.Listener<String>() {
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
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        nameTxt.setText(Helper.checkIfNull(jsonObject1.optString("name")));
                        phoneTxt.setText(Helper.checkIfNull(jsonObject1.optString("mobile")));
                        emailTxt.setText(Helper.checkIfNull(jsonObject1.optString("email")));
                        walidAddTxt.setText(Helper.checkIfNull(jsonObject1.optString("address")));
                        MadarsaNameTxt.setText(Helper.checkIfNull(jsonObject1.optString("madarsa_id")));
                    }
                    else
                    {
                        //no record found
                        Toast.makeText(mContext, "No Record Found!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "json exception : "+e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: "+error.getMessage());
            }
        }){

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "bearer "+walidToken);
                //headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);
    }
}
