package com.amtech.tahfizulquranonline.fragment;

import static com.amtech.tahfizulquranonline.utils.AppConstants.TALIB;
import static com.amtech.tahfizulquranonline.utils.AppConstants.loginUser;
import static com.amtech.tahfizulquranonline.utils.AppConstants.newTalibLoginUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.talibID;
import static com.amtech.tahfizulquranonline.utils.AppConstants.talibToken;
import static com.amtech.tahfizulquranonline.utils.AppConstants.talibloginUrl;

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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.activity.TalibHomeActivity;
import com.amtech.tahfizulquranonline.activity.TalibMainActivity;
import com.amtech.tahfizulquranonline.v2.TalibRegisterFragment;
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

/**
 * Created by Shourav Paul on 01-12-2021.
 **/
public class TalibLoginFragment extends Fragment {

    private EditText usrNameEdTxt, passEdTxt;
    private TextView register, forgot;
    private AppCompatButton loginBtn;
    private ProgressDialog progressDialog;
    private String usernameStr, passStr;
    public static ScrollView login;
    public static FragmentContainerView registerV;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_talib_login, container, false);
        initView(view);
        setListener();
        setMyFragment(new TalibRegisterFragment(), "TalibRegisterFragment");
        return view;
    }

    private void initView(View view) {
        usrNameEdTxt = (EditText) view.findViewById(R.id.usrname_edtxt);
        register = (TextView) view.findViewById(R.id.register);
        forgot = (TextView) view.findViewById(R.id.forgotPassWord);
        passEdTxt = (EditText) view.findViewById(R.id.pass_edtxt);

        registerV = (FragmentContainerView) view.findViewById(R.id.fragmentContain);
        login = (ScrollView) view.findViewById(R.id.loginLY);
        loginBtn = (AppCompatButton) view.findViewById(R.id.talib_login_btn);
    }

    private void setListener() {
        loginBtn.setOnClickListener(v -> {
            if (validate()) {
                newloginAction();
            }
        });
        register.setOnClickListener(v ->
        {
            login.setVisibility(View.GONE);

            registerV.setVisibility(View.VISIBLE);
        });
    }

    private boolean validate() {
        if (usrNameEdTxt.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Enter Username !", Toast.LENGTH_SHORT).show();
            return false;
        } else if (passEdTxt.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Enter password !", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            usernameStr = usrNameEdTxt.getText().toString().trim();
            passStr = passEdTxt.getText().toString().trim();
        }
        return true;
    }

    private void newloginAction() {
        progressDialog = ProgressDialog.show(getContext(), "", "Please wait...");
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("username", usernameStr);
        postParam.put("password", passStr);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, newTalibLoginUrl, new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();

                try {
                    Log.e("login", response.getString("message"));
                    JSONObject jsonObject = response;
                    boolean status = jsonObject.getBoolean("status");
                    if (status == true) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        talibID = jsonObject1.getString("talib_ilm_id");
                        talibToken = jsonObject1.getString("token");
                        loginUser = TALIB;
                        saveInSharedPref();
                        startActivity(new Intent(getContext(), TalibMainActivity.class));
                        getActivity().finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), R.string.common_error + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void loginAction() {
        progressDialog = ProgressDialog.show(getContext(), "", "Please wait...");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, talibloginUrl, response -> {
            progressDialog.dismiss();
            String s = response.trim();
            Log.e("login", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String status = jsonObject.getString("status");
                if (status.equals("true")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    talibID = jsonObject1.getString("id");
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


                    startActivity(new Intent(getContext(), TalibHomeActivity.class));
                    getActivity().finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(getContext(), R.string.common_error, Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", usernameStr);
                params.put("password", passStr);
                Log.e("login", params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void setMyFragment(Fragment fragment, String tag) {
        //get current fragment manager
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        //get fragment transaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //set new fragment in fragment_container (FrameLayout)
        fragmentTransaction.replace(R.id.fragmentContain, fragment, tag);
        fragmentTransaction.commit();
    }


    private void saveInSharedPref() {
        SharedPreferences pref = getContext().getSharedPreferences("Tahfizul", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("login", "2");
        editor.putString("talibId", talibID);
        editor.putString("talibToken", talibToken);
        editor.apply();
    }
}
