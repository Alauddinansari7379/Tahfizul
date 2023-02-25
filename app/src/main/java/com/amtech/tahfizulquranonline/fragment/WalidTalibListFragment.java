package com.amtech.tahfizulquranonline.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.amtech.tahfizulquranonline.activity.TalibDetailsActivity;
import com.amtech.tahfizulquranonline.adapter.StudentAdapter;
import com.amtech.tahfizulquranonline.models.StudentModel;
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

import static com.amtech.tahfizulquranonline.utils.AppConstants.walidGetMyTalibListUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.walidID;
import static com.amtech.tahfizulquranonline.utils.AppConstants.walidToken;

/**
 * Created by Shourav Paul on 10-02-2022.
 **/
public class WalidTalibListFragment extends Fragment {

    private static final String TAG = "WalidTalibListFragment";
    private Context mContext;
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private List<StudentModel> talibList = new ArrayList<>();
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_walid_talib, container, false);
        initRec(view);
        getMyTalibIlmList();
        return view;
    }
    private void initRec(View v)
    {
        recyclerView = (RecyclerView)v.findViewById(R.id.talib_recycler);
        adapter = new StudentAdapter(talibList, mContext, getListener());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
    }
    private StudentAdapter.OnStudentItemClickListener getListener()
    {
        return new StudentAdapter.OnStudentItemClickListener() {
            @Override
            public void onClick(int pos) {
                Intent intent = new Intent(mContext, TalibDetailsActivity.class);
                intent.putExtra("name", talibList.get(pos).getName());
                intent.putExtra("username", talibList.get(pos).getUsername());
                intent.putExtra("address", talibList.get(pos).getAddress());
                intent.putExtra("email", talibList.get(pos).getEmail());
                intent.putExtra("madarsaId", talibList.get(pos).getMadarsaId());
                startActivity(intent);
            }
        };
    }
    private void getMyTalibIlmList()
    {
        progressDialog = ProgressDialog.show(mContext, "", "Please wait...");
        StringRequest request = new StringRequest(Request.Method.GET, walidGetMyTalibListUrl+walidID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String res = response.trim();
                Log.i(TAG, "onResponse: "+res);
                talibList.clear();
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
                                StudentModel model = new StudentModel();
                                model.setId(jsonObject1.getString("id"));
                                model.setName(jsonObject1.getString("name"));
                                model.setUsername(jsonObject1.getString("username"));
                                model.setMadarsaId(jsonObject1.getString("madarsa_id"));
                                model.setAddress(jsonObject1.getString("address"));
                                model.setEmail(jsonObject1.getString("email"));
                                if(!jsonObject1.isNull("assigned_mualem_id"))
                                {
                                    model.setAssignedMualemId(jsonObject1.getString("assigned_mualem_id"));
                                }
                                else
                                {
                                    model.setAssignedMualemId("No Data");
                                }
                                talibList.add(model);
                            }
                        }
                        else
                        {
                            //no record found
                            Toast.makeText(mContext, "No Talib Found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        //no record found
                        Toast.makeText(mContext, "No Talib Found!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();

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
