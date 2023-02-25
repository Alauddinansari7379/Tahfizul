package com.amtech.tahfizulquranonline.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.models.StudentModel;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import static com.amtech.tahfizulquranonline.utils.AppConstants.getMaulimProfileUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.getProfileUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.maulimID;
import static com.amtech.tahfizulquranonline.utils.AppConstants.maulimToken;
import static com.amtech.tahfizulquranonline.utils.AppConstants.walidToken;

/**
 * Created by Shourav Paul on 16-12-2021.
 **/
public class MaulimProfileFragment extends Fragment {

    private TextView nameEdTxt, mobileEdTxt, emailEdTxt, addressEdTxt, aboutEdTxt, madarsaEdTxt;
    private String name, mobile, email, address, about, madarsa;
    private AppCompatButton updateBtn;
    private ProgressDialog progressDialog;
    private Context mContext;
    private static final String TAG = "MaulimProfileFragment";
    private StudentModel model;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maulim_profile, container, false);
        initView(view);
        getProfile();
        return view;

    }
    private void initView(View view)
    {
        nameEdTxt = (TextView)view.findViewById(R.id.name_edtxt);
        mobileEdTxt = (TextView)view.findViewById(R.id.mobile_no_edtxt);
        emailEdTxt = (TextView)view.findViewById(R.id.email_edtxt);
        addressEdTxt = (TextView)view.findViewById(R.id.address_edtxt);
        aboutEdTxt = (TextView)view.findViewById(R.id.about_edtxt);
        madarsaEdTxt = (TextView)view.findViewById(R.id.madarsa_name_edtxt);
        //updateBtn = (AppCompatButton)view.findViewById(R.id.update_btn);

    }
    private void setListener(View view)
    {
//        updateBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(validate(view))
//                {
//                    updateAction();
//                }
//            }
//        });
    }
//    private boolean validate(View view)
//    {
//        if(nameEdTxt.getText().toString().trim().isEmpty())
//        {
//            Snackbar.make(view, "Enter Name!", Snackbar.LENGTH_SHORT).show();
//            return false;
//        }
//        else if(mobileEdTxt.getText().toString().trim().isEmpty())
//        {
//            Snackbar.make(view, "Enter Mobile Number!", Snackbar.LENGTH_SHORT).show();
//            return false;
//        }
//        else if(emailEdTxt.getText().toString().trim().isEmpty())
//        {
//            Snackbar.make(view, "Enter Email!", Snackbar.LENGTH_SHORT).show();
//            return false;
//        }
//        else if(addressEdTxt.getText().toString().trim().isEmpty())
//        {
//            Snackbar.make(view, "Enter Address!", Snackbar.LENGTH_SHORT).show();
//            return false;
//        }
//        else if(aboutEdTxt.getText().toString().trim().isEmpty())
//        {
//            Snackbar.make(view, "Enter about section!", Snackbar.LENGTH_SHORT).show();
//            return false;
//        }
//        else if(madarsaEdTxt.getText().toString().trim().isEmpty())
//        {
//            Snackbar.make(view, "Enter Madarsa name!", Snackbar.LENGTH_SHORT).show();
//            return false;
//        }
//        else
//        {
//            name = nameEdTxt.getText().toString().trim();
//            mobile = mobileEdTxt.getText().toString().trim();
//            email = emailEdTxt.getText().toString().trim();
//            address = addressEdTxt.getText().toString().trim();
//            about = aboutEdTxt.getText().toString().trim();
//            madarsa = madarsaEdTxt.getText().toString().trim();
//        }
//        return true;
//    }
//    private void updateAction()
//    {
//
//    }
    private void setValues()
    {
        if(!model.getName().isEmpty())
            nameEdTxt.setText(model.getName());
        if(!model.getPhoneNo().isEmpty())
            mobileEdTxt.setText(model.getPhoneNo());
        if(!model.getEmail().isEmpty())
            emailEdTxt.setText(model.getEmail());
        if(!model.getAddress().isEmpty())
            addressEdTxt.setText(model.getAddress());
        if(!model.getAbout().isEmpty())
            aboutEdTxt.setText(model.getAbout());
        if(!model.getMadarsaId().isEmpty())
            madarsaEdTxt.setText(model.getMadarsaId());
    }
    private void getProfile()
    {
        progressDialog = ProgressDialog.show(mContext, "", "Please wait...");
        StringRequest request = new StringRequest(Request.Method.GET, getMaulimProfileUrl+maulimID, new Response.Listener<String>() {
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
                        model = new StudentModel();
                        model.setName(jsonObject1.optString("name"));
                        model.setPhoneNo(jsonObject1.optString("mobile"));
                        model.setEmail(jsonObject1.optString("email"));
                        model.setAddress(jsonObject1.optString("address"));
                        model.setAbout(jsonObject1.optString("about"));
                        model.setMadarsaId(jsonObject1.optString("madarsa_id"));
                        setValues();

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
                headers.put("Authorization", "bearer "+maulimToken);
                //headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);
    }
}
