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
import com.amtech.tahfizulquranonline.adapter.PendingAssignAdapter;
import com.amtech.tahfizulquranonline.models.AssignmentModel;
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
import java.util.List;
import static com.amtech.tahfizulquranonline.utils.AppConstants.getTalibAssignmentByStatusUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.talibID;

/**
 * Created by Shourav Paul on 28-12-2021.
 **/
public class TalibPendingAssignment extends Fragment {

    private ProgressDialog progressDialog;
    private Context mContext;
    private static final String TAG = "TalibPendingAssignment";
    private List<AssignmentModel> pendingList = new ArrayList<>();
    private PendingAssignAdapter adapter;
    private RecyclerView pendingRec;
    //2/api

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_talib_pending_assign, container, false);
        initView(view);
        setListener();
        initRecycler(view);
        getPendingAssignmentList();
        return view;
    }
    private void initView(View view)
    {

    }
    private void setListener()
    {

    }
    private void initRecycler(View view)
    {
        pendingRec = (RecyclerView)view.findViewById(R.id.pending_recycler);
        adapter = new PendingAssignAdapter(mContext,pendingList);
        pendingRec.setHasFixedSize(true);
        pendingRec.setLayoutManager(new LinearLayoutManager(getContext()));
        pendingRec.setAdapter(adapter);
    }
    private void getPendingAssignmentList()
    {
        progressDialog = ProgressDialog.show(mContext, "", "Please wait...");
        StringRequest request = new StringRequest(Request.Method.GET, getTalibAssignmentByStatusUrl+talibID+"/"+"2"+"/api", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String res = response.trim();
                Log.i(TAG, "onResponse: "+res);
                pendingList.clear();
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
                                AssignmentModel model = new AssignmentModel();
                                model.setId(jsonObject1.getString("id"));
                                model.setName(jsonObject1.getString("mualem_name"));
                                model.setMadarsaId(jsonObject1.getString("madarsa_id"));
                                model.setMualemId(jsonObject1.getString("mualem_id"));
                                model.setFile("https://www.tahfizulquranonline.com/public/"+jsonObject1.getString("file"));
                                String[] arr = jsonObject1.getString("assigned_date").split(" ");
                                model.setAssignDate(arr[0]);
                                model.setDesc(jsonObject1.getString("description"));
                                model.setDeadLine(jsonObject1.getString("deadline"));
                                //model.setAssignedMualemId(jsonObject1.getString("assigned_mualem_id"));
                                pendingList.add(model);
                            }
                        }
                        else
                        {
                            //no record found
                            Toast.makeText(mContext, "No Assignment Found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        //no record found
                        Toast.makeText(mContext, "No Assignment Found!", Toast.LENGTH_SHORT).show();
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
                pendingList.clear();
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);
    }
}
