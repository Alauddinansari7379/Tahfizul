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
import com.amtech.tahfizulquranonline.activity.MaulimDetailsActivity;
import com.amtech.tahfizulquranonline.adapter.StudentAdapter;
import com.amtech.tahfizulquranonline.models.StudentModel;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.amtech.tahfizulquranonline.utils.AppConstants.talibGetMyMaulimListUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.talibID;
import static com.amtech.tahfizulquranonline.utils.AppConstants.walidGetMyTalibListUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.walidID;
import static com.amtech.tahfizulquranonline.utils.AppConstants.walidToken;

/**
 * Created by Shourav Paul on 10-02-2022.
 **/
public class WalidMaulimFragment extends Fragment {

    private static final String TAG = "WalidMaulimFragment";
    private Context mContext;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private List<StudentModel> maulimList = new ArrayList<>();
    private MaterialSpinner talibSpinner;
    private List<String> talibList = new ArrayList<>();
    private List<String> talibId = new ArrayList<>();
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_walid_maulim, container, false);
        initRec();
        getMyTalibIlmList();
        return view;
    }
    private void initRec()
    {
        recyclerView = (RecyclerView)view.findViewById(R.id.maulim_recycler);
        adapter = new StudentAdapter(maulimList, mContext, getListener());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
    }
    private void initSpinner()
    {
        talibSpinner = (MaterialSpinner)view.findViewById(R.id.talib_spinner);
        talibSpinner.setItems(talibList);
        talibSpinner.setSelectedIndex(0);
        talibSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                getMyMaulimList(talibId.get(position));
            }
        });
    }
    private StudentAdapter.OnStudentItemClickListener getListener()
    {
        return new StudentAdapter.OnStudentItemClickListener() {
            @Override
            public void onClick(int pos) {
                Intent intent = new Intent(mContext, MaulimDetailsActivity.class);
                intent.putExtra("name", maulimList.get(pos).getName());
                intent.putExtra("about", maulimList.get(pos).getAbout());
                intent.putExtra("address", maulimList.get(pos).getAddress());
                intent.putExtra("email", maulimList.get(pos).getEmail());
                intent.putExtra("madarsaId", maulimList.get(pos).getMadarsaId());
                intent.putExtra("mobile", maulimList.get(pos).getPhoneNo());
                startActivity(intent);
            }
        };
    }
    private void getMyMaulimList(String talibId)
    {
        progressDialog = ProgressDialog.show(mContext, "", "Please wait...");
        StringRequest request = new StringRequest(Request.Method.GET, talibGetMyMaulimListUrl+talibId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String res = response.trim();
                Log.i(TAG, "onResponse: maulim list : "+res);
                maulimList.clear();
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
                                model.setPhoneNo(jsonObject1.getString("mobile"));
                                model.setMadarsaId(jsonObject1.getString("madarsa_id"));
                                model.setAddress(jsonObject1.getString("address"));
                                model.setEmail(jsonObject1.getString("email"));
                                model.setAbout(jsonObject1.getString("about"));
                                maulimList.add(model);
                            }
                        }
                        else
                        {
                            //no record found
                            Toast.makeText(mContext, "No Maulim Found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        //no record found
                        Toast.makeText(mContext, "No Maulim Found!", Toast.LENGTH_SHORT).show();
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
        });
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);
    }
    private void getMyTalibIlmList()
    {
        //progressDialog = ProgressDialog.show(mContext, "", "Please wait...");
        StringRequest request = new StringRequest(Request.Method.GET, walidGetMyTalibListUrl+walidID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //progressDialog.dismiss();
                String res = response.trim();
                Log.i(TAG, "onResponse: "+res);
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
                                talibId.add(String.valueOf(jsonObject1.getInt("id")));
                                talibList.add(jsonObject1.getString("name"));
                            }
                            if(talibList.size() > 0)
                            {
                                initSpinner();
                                Log.i(TAG, "onResponse: talib Id -> "+talibId.get(0));
                                getMyMaulimList(talibId.get(0));
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
                    Log.i(TAG, "onResponse: jsonException : "+e.getMessage());
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
