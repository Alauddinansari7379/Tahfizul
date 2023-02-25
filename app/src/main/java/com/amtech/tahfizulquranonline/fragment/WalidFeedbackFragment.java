package com.amtech.tahfizulquranonline.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.activity.WalidMainActivity;
import com.amtech.tahfizulquranonline.models.StudentModel;
import com.amtech.tahfizulquranonline.utils.AppConstants;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

import static com.amtech.tahfizulquranonline.utils.AppConstants.WALID;
import static com.amtech.tahfizulquranonline.utils.AppConstants.loginUser;
import static com.amtech.tahfizulquranonline.utils.AppConstants.maulimID;
import static com.amtech.tahfizulquranonline.utils.AppConstants.myTalibList;
import static com.amtech.tahfizulquranonline.utils.AppConstants.newWalidLoginUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.sendWalidFeedbackUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.talibGetMyMaulimListUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.walidID;
import static com.amtech.tahfizulquranonline.utils.AppConstants.walidToken;

/**
 * Created by Shourav Paul on 11-02-2022.
 **/
public class WalidFeedbackFragment extends Fragment {

    private static final String TAG = "WalidFeedbackFragment";
    private Context mContext;
    private ProgressDialog progressDialog;
    private MaterialSpinner talibSpinner, maulimSpinner;
    private List<StudentModel> maulimModels = new ArrayList<>();
    private List<String> maulimList = new ArrayList<>();
    private List<String> talibList = new ArrayList<>();
    private String talibId, maulimId, madarsaId, ratingPoint="1";
    private EditText feedbackEdTxt;
    private String feedbackStr;
    private TextView sendBtn;
    private RatingBar ratingBar;
    private LinearLayout maulimSpinnerBody;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_walid_feedback, container, false);
        initView(view);
        initTalibSpinner(view);
        initMaulimSpinner(view);
        setListener();
        return view;
    }
    private void initView(View view)
    {
        feedbackEdTxt = (EditText)view.findViewById(R.id.feedback_edtxt);
        sendBtn = (TextView)view.findViewById(R.id.send_btn);
        ratingBar = (RatingBar)view.findViewById(R.id.rating_bar);
        maulimSpinnerBody = (LinearLayout)view.findViewById(R.id.maulim_spinner_body);
    }
    private void initTalibSpinner(View view)
    {
        talibList.add("Select Talib Ilm");
        for(int i=0; i<myTalibList.size(); i++)
        {
            talibList.add(myTalibList.get(i).getName());
        }
        talibSpinner = (MaterialSpinner)view.findViewById(R.id.talib_spinner);
        talibSpinner.setItems(talibList);
        talibSpinner.setSelectedIndex(0);
        talibSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(position != 0)
                {
                    talibId = myTalibList.get(position-1).getId();
                    getMyMaulimList(myTalibList.get(position-1).getId());
                    maulimSpinnerBody.setVisibility(View.VISIBLE);
                }
                else
                {
                    maulimSpinnerBody.setVisibility(View.GONE);
                    maulimList.clear();
                    maulimList.add("Select Maulim");
                    maulimSpinner.setItems(maulimList);
                    maulimSpinner.setSelectedIndex(0);
                }

            }
        });
    }
    private void initMaulimSpinner(View view)
    {
        maulimList.add("Select Maulim");
        maulimSpinner = (MaterialSpinner)view.findViewById(R.id.maulim_spinner);
        maulimSpinner.setItems(maulimList);
        maulimSpinner.setSelectedIndex(0);
        maulimSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(position != 0)
                {
                    maulimId = maulimModels.get(position-1).getId();
                    madarsaId = maulimModels.get(position-1).getMadarsaId();
                }
            }
        });
    }
    private void setListener()
    {
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                {
                    sendFeedBack();
                }
            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingPoint = String.valueOf(rating);
            }
        });
    }
    private boolean validate()
    {
        if(feedbackEdTxt.getText().toString().isEmpty())
        {
            showErrorMsg("Enter feedback!!");
            return false;
        }
        else if(talibSpinner.getSelectedIndex() == 0)
        {
            showErrorMsg("Select Talib Ilm");
            return false;
        }
        else if(maulimSpinner.getSelectedIndex() == 0)
        {
            showErrorMsg("Select Maulim");
            return false;
        }
        else
        {
            feedbackStr = feedbackEdTxt.getText().toString().trim();
        }
        return true;
    }
    private void showErrorMsg(String msg)
    {
        Toast.makeText(mContext, ""+msg, Toast.LENGTH_SHORT).show();
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
                maulimModels.clear();
                maulimList.clear();
                maulimList.add("Select Maulim");
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
                                maulimModels.add(model);
                                maulimList.add(jsonObject1.getString("name"));
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
                maulimSpinner.setItems(maulimList);
                maulimSpinner.setSelectedIndex(0);


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
    private void sendFeedBack()
    {
        progressDialog = ProgressDialog.show(mContext, "", "Please wait...");
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("madarsa_id", madarsaId);
        postParam.put("mualem_id", maulimId);
        postParam.put("walidain_id", walidID);
        postParam.put("rating", ratingPoint);
        postParam.put("feedback", feedbackStr);
        postParam.put("username", talibId);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, sendWalidFeedbackUrl, new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();

                try {
                    Log.e("feedback response : ", response.getString("message"));
                    JSONObject jsonObject = response;
                    boolean status = jsonObject.getBoolean("status");
                    if (status == true) {
                        Toast.makeText(mContext, "Feed Sent Successfully!", Toast.LENGTH_SHORT).show();
                        reset();

                    } else {
                        Toast.makeText(mContext, "Feed Could not be sent!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
        queue.add(jsonObjectRequest);
    }
    private void reset()
    {
        talibSpinner.setSelectedIndex(0);
        maulimSpinnerBody.setVisibility(View.GONE);
        maulimList.clear();
        maulimList.add("Select Maulim");
        maulimSpinner.setItems(maulimList);
        maulimSpinner.setSelectedIndex(0);
        feedbackStr = null;
        feedbackEdTxt.getText().clear();
        ratingBar.setRating(1);
        ratingPoint = "1";
    }
}
