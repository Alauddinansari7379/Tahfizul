package com.amtech.tahfizulquranonline.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.activity.MainActivity;
import com.amtech.tahfizulquranonline.activity.MualemHomeActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.amtech.tahfizulquranonline.utils.AppConstants.MAULIM;
import static com.amtech.tahfizulquranonline.utils.AppConstants.loginUser;
import static com.amtech.tahfizulquranonline.utils.AppConstants.maulimID;
import static com.amtech.tahfizulquranonline.utils.AppConstants.maulimToken;
import static com.amtech.tahfizulquranonline.utils.AppConstants.newMaulimLoginUrl;

/**
 * Created by Shourav Paul on 01-12-2021.
 **/
public class MualemLoginFragment extends Fragment {

    private EditText phoneEdTxt, passEdTxt;
    private AppCompatButton loginBtn;
    private ProgressDialog progressDialog;
    private String mobileStr, passStr;
    private static final String TAG = "MualemLoginFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mualem_login, container, false);
        initView(view);
        setListener();
        return view;
    }
    private void initView(View view)
    {
        phoneEdTxt = (EditText)view.findViewById(R.id.mobile_no_edtxt);
        passEdTxt = (EditText)view.findViewById(R.id.pass_edtxt);
        loginBtn = (AppCompatButton)view.findViewById(R.id.mualim_login_btn);
    }
    private void setListener()
    {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                {
                    //loginAction();
                    newloginAction();
                }
            }
        });
    }
    private boolean validate()
    {
        if(phoneEdTxt.getText().toString().isEmpty())
        {
            Toast.makeText(getContext(), "Enter phone number!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(passEdTxt.getText().toString().isEmpty())
        {
            Toast.makeText(getContext(), "Enter password !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            mobileStr = phoneEdTxt.getText().toString().trim();
            passStr = passEdTxt.getText().toString().trim();
        }
        return true;
    }
    private void newloginAction()
    {
        progressDialog = ProgressDialog.show(getContext(), "", "Please wait...");
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("mobile", mobileStr);
        postParam.put("password", passStr);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, newMaulimLoginUrl, new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.i(TAG, "onResponse: login response-> "+response.toString());
                try {
                    Log.e("login", response.getString("message"));
                    JSONObject jsonObject = response;
                    boolean status = jsonObject.getBoolean("status");
                    if (status == true) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        maulimID = jsonObject1.getString("mualem_id");
                        maulimToken = jsonObject1.getString("token");
                        loginUser = MAULIM;
                        saveInSharedPref();
                        startActivity(new Intent(getContext(), MainActivity.class));
                        getActivity().finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), R.string.common_error+":"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: "+error.getMessage());
                Toast.makeText(getContext(), R.string.common_error, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(jsonObjectRequest);
    }
    private void loginAction()
    {
        progressDialog = ProgressDialog.show(getContext(), "", "Please wait...");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, newMaulimLoginUrl, response -> {
            progressDialog.dismiss();
            String s = response.trim();
            Log.e("login", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                boolean status = jsonObject.getBoolean("status");
                if (status == true) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    maulimID = jsonObject1.getString("mualem_id");
                   //saveInSharedPref();
//                    editor.putString("phone", edtMobile.getText().toString().trim());
//                    editor.putString("pass", edtPassword.getText().toString().trim());
//                    if(checkBox.isChecked())
//                    {
//                        editor.putString("remember", "YES");
//                    }
//                    else
//                    {
//                        editor.putString("remember", "NO");
//                    }


                    startActivity(new Intent(getContext(), MualemHomeActivity.class));
                    getActivity().finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(getContext(), R.string.common_error+":"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", mobileStr);
                params.put("password", passStr);
                Log.e("login", params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    private void saveInSharedPref()
    {
        SharedPreferences pref = getContext().getSharedPreferences("Tahfizul", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("login", "1");
        editor.putString("maulimId", maulimID);
        editor.putString("maulimToken", maulimToken);
        editor.apply();
    }
}
