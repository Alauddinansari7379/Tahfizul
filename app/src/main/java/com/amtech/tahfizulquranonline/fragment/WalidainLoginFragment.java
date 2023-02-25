package com.amtech.tahfizulquranonline.fragment;

import android.app.Activity;
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
import com.amtech.tahfizulquranonline.activity.WalidMainActivity;
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
import static com.amtech.tahfizulquranonline.utils.AppConstants.WALID;
import static com.amtech.tahfizulquranonline.utils.AppConstants.loginUser;
import static com.amtech.tahfizulquranonline.utils.AppConstants.maulimID;
import static com.amtech.tahfizulquranonline.utils.AppConstants.maulimToken;
import static com.amtech.tahfizulquranonline.utils.AppConstants.newMaulimLoginUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.newWalidLoginUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.walidID;
import static com.amtech.tahfizulquranonline.utils.AppConstants.walidToken;

/**
 * Created by Shourav Paul on 01-12-2021.
 **/
public class WalidainLoginFragment extends Fragment {

    private AppCompatButton walidLoginBtn;
    private Context mContext;
    private EditText phoneEdTxt, passEdTxt;
    private ProgressDialog progressDialog;
    private String mobileStr, passStr;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_walidain_login, container, false);
        initView(view);
        setListener();
        return view;
    }
    private void initView(View view)
    {
        walidLoginBtn = (AppCompatButton)view.findViewById(R.id.walid_login_btn);
        phoneEdTxt = (EditText)view.findViewById(R.id.mobile_no_edtxt);
        passEdTxt = (EditText)view.findViewById(R.id.pass_edtxt);
    }
    private void setListener()
    {
        walidLoginBtn.setOnClickListener(new View.OnClickListener() {
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
            Toast.makeText(getContext(), "Enter password!", Toast.LENGTH_SHORT).show();
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, newWalidLoginUrl, new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();

                try {
                    Log.e("login", response.getString("message"));
                    JSONObject jsonObject = response;
                    boolean status = jsonObject.getBoolean("status");
                    if (status == true) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        walidID = jsonObject1.getString("walidain_id");
                        walidToken = jsonObject1.getString("token");
                        loginUser = WALID;
                        saveInSharedPref();
                        startActivity(new Intent(getContext(), WalidMainActivity.class));
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
                Toast.makeText(getContext(), R.string.common_error, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(jsonObjectRequest);
    }
    private void saveInSharedPref()
    {
        SharedPreferences pref = getContext().getSharedPreferences("Tahfizul", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("login", String.valueOf(loginUser));
        editor.putString("walidId", walidID);
        editor.putString("walidToken", walidToken);
        editor.apply();
    }
}
