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
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.adapter.CompletedAssignmentAdapter;
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

import static com.amtech.tahfizulquranonline.utils.AppConstants.getMaulimAssignmentByStatusUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.maulimID;

/**
 * Created by Shourav Paul on 25-12-2021.
 **/
public class CompletedAssignFragment extends Fragment {

    private static final String TAG = "CompletedAssignFragment";
    private AppCompatButton approveBtn, unApproveBtn;
    private ProgressDialog progressDialog;
    private Context mContext;
    private List<AssignmentModel> completedList = new ArrayList<>();
    private CompletedAssignmentAdapter adapter;
    private RecyclerView pendingRec;
    public static String appListStatus = "3";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comp_assignment, container, false);
        initView(view);
        setListener();
        initRecycler(view);
        getAssignmentListByStatus("3");
        return view;
    }
    private void initView(View view)
    {
        approveBtn = (AppCompatButton)view.findViewById(R.id.approve_btn);
        unApproveBtn = (AppCompatButton)view.findViewById(R.id.unapprove_btn);
    }
    private void setListener()
    {
        approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAssignmentListByStatus("1");
                appListStatus = "1";
            }
        });
        unApproveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAssignmentListByStatus("3");
                appListStatus = "3";
            }
        });
    }
    private void initRecycler(View view)
    {
        pendingRec = (RecyclerView)view.findViewById(R.id.complete_recycler);
        adapter = new CompletedAssignmentAdapter(mContext,completedList);
        pendingRec.setHasFixedSize(true);
        pendingRec.setLayoutManager(new LinearLayoutManager(getContext()));
        pendingRec.setAdapter(adapter);
    }
    private void getAssignmentListByStatus(String status)
    {
        progressDialog = ProgressDialog.show(mContext, "", "Please wait...");
        StringRequest request = new StringRequest(Request.Method.GET, getMaulimAssignmentByStatusUrl+maulimID+"/"+status+"/api", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String res = response.trim();
                Log.i(TAG, "onResponse: "+res);
                completedList.clear();
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
                                model.setTalibId(jsonObject1.getString("assigned_talib_ilm_id"));
                                model.setFile("https://www.tahfizulquranonline.com/public/"+jsonObject1.getString("file"));
                                String[] arrDate = jsonObject1.getString("assigned_date").split(" ");
                                model.setAssignDate(arrDate[0]);
                                model.setDesc(jsonObject1.getString("description"));
                                model.setDeadLine(jsonObject1.getString("deadline"));
                                JSONArray jsonArray1 = jsonObject1.getJSONArray("talibilmresponse");
                                JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
                                model.setTalibResponse(jsonObject2.getString("description"));
                                model.setTalibFile("https://www.tahfizulquranonline.com/public/"+jsonObject2.getString("assignment_file"));
                                String[] arr = jsonObject2.getString("updated_at").split("T");
                                model.setSubmissionDate(arr[0]);
                                model.setMaulimRemarkOnComp(jsonObject1.getString("comments"));
                                //model.setAssignedMualemId(jsonObject1.getString("assigned_mualem_id"));
                                completedList.add(model);
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
                completedList.clear();
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);
    }
}
