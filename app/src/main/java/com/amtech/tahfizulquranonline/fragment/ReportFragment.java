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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.adapter.AssignmentForReportAdapter;
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

import static com.amtech.tahfizulquranonline.utils.AppConstants.currentUserName;
import static com.amtech.tahfizulquranonline.utils.AppConstants.getAssignmentForReportUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.maulimID;

/**
 * Created by Shourav Paul on 28-12-2021.
 **/
public class ReportFragment extends Fragment{
    private static final String TAG = "ReportFragment";
    private TextView repDoneBtn, repPendingBtn;
    private ProgressDialog progressDialog;
    private Context mContext;
    private List<AssignmentModel> reportList = new ArrayList<>();
    private AssignmentForReportAdapter adapter;
    private RecyclerView repRec;
    public static String reportListStatus = "0";
    private TextView statusHeaderTxt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        initView(view);
        setListener();
        initRecycler(view);
        getAssignmentListForReportByStatus("0");
        return view;
    }
    private void initView(View view)
    {
        repDoneBtn = (TextView)view.findViewById(R.id.comp_tab);
        repPendingBtn = (TextView)view.findViewById(R.id.pending_tab);
        statusHeaderTxt = (TextView)view.findViewById(R.id.status_header);
    }
    private void setListener()
    {
        repDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAssignmentListForReportByStatus("1");
                reportListStatus = "1";
                statusHeaderTxt.setText("COMPLETED");
            }
        });
        repPendingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAssignmentListForReportByStatus("0");
                reportListStatus = "0";
                statusHeaderTxt.setText("PENDING");
            }
        });
    }
    private void initRecycler(View view)
    {
        repRec = (RecyclerView)view.findViewById(R.id.report_rec);
        adapter = new AssignmentForReportAdapter(mContext,reportList);
        repRec.setHasFixedSize(true);
        repRec.setLayoutManager(new LinearLayoutManager(getContext()));
        repRec.setAdapter(adapter);
    }
    private void getAssignmentListForReportByStatus(String status)
    {
        progressDialog = ProgressDialog.show(mContext, "", "Please wait...");
        StringRequest request = new StringRequest(Request.Method.GET, getAssignmentForReportUrl+maulimID+"/"+status+"/api", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String res = response.trim();
                Log.i(TAG, "onResponse: "+res);
                reportList.clear();
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
                                model.setName(jsonObject1.getString("talibilm_name"));
                                model.setMaulimName(jsonObject1.getString("mualem_name"));
                                model.setMadarsaId(jsonObject1.getString("madarsa_id"));
                                model.setTalibId(jsonObject1.getString("assigned_talib_ilm_id"));
                                model.setFile("https://www.tahfizulquranonline.com/public/"+jsonObject1.getString("file"));
                                String[] arrDate = jsonObject1.getString("assigned_date").split(" ");
                                model.setAssignDate(arrDate[0]);
                                model.setDesc(jsonObject1.getString("description"));
                                model.setDeadLine(jsonObject1.getString("deadline"));
                                JSONArray jsonArray1 = jsonObject1.getJSONArray("talibilmresponse");
                                if(jsonArray1.length() > 0)
                                {
                                    JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
                                    model.setTalibResponse(jsonObject2.getString("description"));
                                    model.setTalibFile(jsonObject2.getString("assignment_file"));
                                    String[] arr = jsonObject2.getString("created_at").split("T");
                                    model.setSubmissionDate(arr[0]);
                                }
                                model.setMaulimRemarkOnComp(jsonObject1.getString("comments"));
                                model.setReportComment(jsonObject1.getString("reportremarks"));
                                if(jsonObject1.getString("status_id").equals("1"))
                                {
                                    model.setStatus("COMPLETED");
                                }
                                else if(jsonObject1.getString("status_id").equals("2"))
                                {
                                    model.setStatus("PENDING");
                                }
                                else
                                {
                                    model.setStatus("UNAPPROVED");
                                }
                                if(jsonObject1.getString("assignment_type_id").equals("1"))
                                {
                                    model.setAssignType("DAILY");
                                }
                                else
                                {
                                    model.setAssignType("WEEKLY");
                                }
                                //model.setAssignedMualemId(jsonObject1.getString("assigned_mualem_id"));
                                reportList.add(model);
                            }
                        }
                        else
                        {
                            //no record found
                            Toast.makeText(mContext, "No Report Found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        //no record found
                        Toast.makeText(mContext, "No Report Found!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "json exception : "+e.getMessage());
                }
                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: "+error.getMessage());
                reportList.clear();
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);
    }
}
