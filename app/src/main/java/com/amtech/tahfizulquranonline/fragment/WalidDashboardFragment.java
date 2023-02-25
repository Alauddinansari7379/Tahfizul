package com.amtech.tahfizulquranonline.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.amtech.tahfizulquranonline.Constant;
import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.adapter.StudentAdapter;
import com.amtech.tahfizulquranonline.models.AssignmentModel;
import com.amtech.tahfizulquranonline.models.StudentModel;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.amtech.tahfizulquranonline.utils.AppConstants.currentUserName;
import static com.amtech.tahfizulquranonline.utils.AppConstants.getMaulimProfileUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.getTalibAssignmentByStatusUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.getTalibReportUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.getWalidProfileUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.maulimID;
import static com.amtech.tahfizulquranonline.utils.AppConstants.myTalibList;
import static com.amtech.tahfizulquranonline.utils.AppConstants.walidGetMyTalibListUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.walidID;
import static com.amtech.tahfizulquranonline.utils.AppConstants.walidToken;

/**
 * Created by Shourav Paul on 25-01-2022.
 **/
public class WalidDashboardFragment extends Fragment {

    private static final String TAG = "WalidDashboardFragment";
    private Context mContext;
    private ProgressDialog progressDialog;
    private List<String> talibList = new ArrayList<>();
    private MaterialSpinner talibSpinner;
    private View view;
    private TextView pendingTxt, compTxt;
    private TextView walidNameTxt, report;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_walid_dashboard, container, false);
        initView();
        NavigationView navigationView = getActivity().findViewById(R.id.navigation_view);
        View view1 = navigationView.getHeaderView(0);
        LinearLayout pending = view.findViewById(R.id.pending_assignments);
        LinearLayout report = view.findViewById(R.id.talib_report);
        LinearLayout complete = view.findViewById(R.id.completed_assignments);
        pending.setOnClickListener(v -> {
            Constant.changeWalidFragment.changeFragment(R.id.pending_assignments);
        });
        report.setOnClickListener(v -> {
            Constant.changeWalidFragment.changeFragment(R.id.walid_rep);
        });
        complete.setOnClickListener(v -> {
            Constant.changeWalidFragment.changeFragment(R.id.completed_assignments);
        });
        walidNameTxt = view1.findViewById(R.id.walid_name_txt);
        Log.i(TAG, "onCreateView: myTalibList size: " + myTalibList.size());
        if (myTalibList.size() < 1) {
            getProfile();
        } else {
            walidNameTxt.setText(currentUserName);
            for (int i = 0; i < myTalibList.size(); i++) {
                talibList.add(myTalibList.get(i).getName());
            }
            initTalibSpinner(view);
            getPendingAssignmentList(myTalibList.get(0).getId(), 0);
        }
        return view;
    }

    private void initView() {
        pendingTxt = (TextView) view.findViewById(R.id.pending_txt);
        compTxt = (TextView) view.findViewById(R.id.comp_txt);
        report = view.findViewById(R.id.report);
    }

    private void initTalibSpinner(View view) {
        talibSpinner = (MaterialSpinner) view.findViewById(R.id.talib_spinner);
        talibSpinner.setItems(talibList);
        talibSpinner.setSelectedIndex(0);
        talibSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                getPendingAssignmentList(myTalibList.get(position).getId(), position);
            }
        });
    }

    private void getMyTalibIlmList() {
        //progressDialog = ProgressDialog.show(mContext, "", "Please wait...");
        StringRequest request = new StringRequest(Request.Method.GET, walidGetMyTalibListUrl + walidID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //progressDialog.dismiss();
                String res = response.trim();
                Log.i(TAG, "onResponse: " + res);
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                StudentModel model = new StudentModel();
                                model.setId(String.valueOf(jsonObject1.getInt("id")));
                                model.setName(jsonObject1.getString("name"));
                                myTalibList.add(model);
                                talibList.add(jsonObject1.getString("name"));
                            }
                            initTalibSpinner(view);
                            //get pending assingment count..
                            getPendingAssignmentList(myTalibList.get(0).getId(), 0);

                        } else {
                            //no record found
                            Toast.makeText(mContext, "No Talib Found!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //no record found
                        Toast.makeText(mContext, "No Talib Found!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "onResponse: jsonException : " + e.getMessage());
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
                headers.put("Authorization", "bearer " + walidToken);
                //headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);
    }

    private void getPendingAssignmentList(String talibId, int position) {
        progressDialog = ProgressDialog.show(mContext, "", "Please wait...");
        StringRequest request = new StringRequest(Request.Method.GET, getTalibAssignmentByStatusUrl + talibId + "/" + "2" + "/api", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String res = response.trim();
                Log.i(TAG, "onResponse: " + res);
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            pendingTxt.setText(String.valueOf(jsonArray.length()));
                            //get completed assignment report...
                            getAssignmentListByStatus(myTalibList.get(position).getId(), "1");
                            getTalibReportByTalibId(myTalibList.get(position).getId());
                        }
                    } else {
                        //no record found
                        Log.i(TAG, "onResponse: No Assignment Found!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: " + error.getMessage());
                pendingTxt.setText("0");
                progressDialog.hide();

                getAssignmentListByStatus(myTalibList.get(0).getId(), "1");
            }
        });
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);
    }

    private void getAssignmentListByStatus(String talibId, String status) {
        StringRequest request = new StringRequest(Request.Method.GET, getTalibAssignmentByStatusUrl + talibId + "/" + status + "/api", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String res = response.trim();
                Log.i(TAG, "onResponse: " + res);
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            compTxt.setText(String.valueOf(jsonArray.length()));
                        }
                    } else {
                        //no record found
                        Log.i(TAG, "onResponse: No Assignment Found!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "onResponse: jsonexception : " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: " + error.getMessage());
                compTxt.setText("0");
                progressDialog.dismiss();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);
    }


    private void getTalibReportByTalibId(String talibId) {
        List<AssignmentModel> reportList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, getTalibReportUrl + talibId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String res = response.trim();
                Log.i(TAG, "onResponse: " + res);
                reportList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        report.setText(String.valueOf(jsonArray.length()));
                    } else {
                        //no record found
                        Toast.makeText(mContext, "No Assignment Found!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "json exception : " + e.getMessage());
                }
                Log.d(TAG, "onResponseSSS: " + reportList.size());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: " + error.getMessage());
                reportList.clear();
                progressDialog.dismiss();
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "bearer " + walidToken);
                //headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);
    }

    private void getProfile() {
        //progressDialog = ProgressDialog.show(mContext, "", "Please wait...");
        StringRequest request = new StringRequest(Request.Method.GET, getWalidProfileUrl + walidID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String res = response.trim();
                Log.i(TAG, "onResponse: " + res);
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        currentUserName = jsonObject1.optString("name");
                        walidNameTxt.setText(currentUserName);
                        getMyTalibIlmList();
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
                headers.put("Authorization", "bearer " + walidToken);
                //headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);
    }
}
