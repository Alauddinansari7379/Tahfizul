package com.amtech.tahfizulquranonline.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.amtech.tahfizulquranonline.BuildConfig;
import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.utils.GeneralInternet;
import com.amtech.tahfizulquranonline.v2.SubscriptionActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;

import static com.amtech.tahfizulquranonline.utils.AppConstants.getPlaystoreLatestVersionUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.maulimID;
import static com.amtech.tahfizulquranonline.utils.AppConstants.maulimToken;
import static com.amtech.tahfizulquranonline.utils.AppConstants.talibID;
import static com.amtech.tahfizulquranonline.utils.AppConstants.talibToken;
import static com.amtech.tahfizulquranonline.utils.AppConstants.walidID;
import static com.amtech.tahfizulquranonline.utils.AppConstants.walidToken;
import static com.android.volley.VolleyLog.TAG;

public class SplashActivity extends AppCompatActivity {

    private int delayTime = 3000;
    private Dialog dialog;
    private String mCurrentVersion, mLatestVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initDialog();
        startLoginActivity();
//        if (checkInternet()) {
//            //Get latest version from playstore
//            dialog.dismiss();
//         //   checkIfAppIsUpToDate();
//            //new GetLatestVersion().execute();
//        } else {
//            dialog.show();
//        }
    }

    private void startLoginActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref = getSharedPreferences("Tahfizul", Context.MODE_PRIVATE);
                String isLogin = pref.getString("login", null);
                if (isLogin != null) {
                    if (isLogin.equals("1")) {
                        maulimID = pref.getString("maulimId", null);
                        maulimToken = pref.getString("maulimToken", null);
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));

                    } else if (isLogin.equals("2")) {
                        talibID = pref.getString("talibId", null);
                        talibToken = pref.getString("talibToken", null);
                        startActivity(new Intent(SplashActivity.this, TalibMainActivity.class));
                    } else if (isLogin.equals("3")) {
                        walidID = pref.getString("walidId", null);
                        walidToken = pref.getString("walidToken", null);
                        startActivity(new Intent(SplashActivity.this, WalidMainActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, delayTime);
    }

    public void initDialog() {
        dialog = new Dialog(SplashActivity.this);
        dialog.setContentView(R.layout.no_internet_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.no_internet_bg));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        Button btnTryAgain = dialog.findViewById(R.id.btn_try);
        Button btnExit = dialog.findViewById(R.id.btn_exit);

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkInternet()) {
                    //Get latest version from playstore
                    dialog.dismiss();
                    checkIfAppIsUpToDate();
                    //new GetLatestVersion().execute();//old version fetching api
                    //new ForceUpdateAsync(BuildConfig.VERSION_NAME, getApplicationContext()).execute();
                } else {
                    dialog.show();
                }
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
    }

    public boolean checkInternet() {
        if (GeneralInternet.checkInternet(this)) {
            return true;
        }
        return false;
    }

    public void updateAlertDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage("Please Update to Continue");
        builder.setCancelable(false);

        //
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //open playstore
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                finish();
            }
        });
        builder.show();
    }

    private class GetLatestVersion extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                mLatestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + getPackageName())
                        .timeout(30000)
                        .get()
                        .select("div.hAyfc:nth-child(4)>" +
                                "span:nth-child(2) > div:nth-child(1)" +
                                "> span:nth-child(1)")
                        .first()
                        .ownText();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mLatestVersion;
        }

        @Override
        protected void onPostExecute(String s) {
            //Get current version
            mCurrentVersion = BuildConfig.VERSION_NAME;
            if (mLatestVersion != null) {
                float cVersion = Float.parseFloat(mCurrentVersion);
                float lVersion = Float.parseFloat(mLatestVersion);
                if (lVersion > cVersion) {
                    updateAlertDialog();
                } else {
                    startLoginActivity();
                }

            }

        }
    }

    public class ForceUpdateAsync extends AsyncTask<String, String, JSONObject> {

        private String latestVersion;
        private String currentVersion;
        private Context context;

        public ForceUpdateAsync(String currentVersion, Context context) {
            this.currentVersion = currentVersion;
            this.context = context;
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            try {
                latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + context.getPackageName() + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div.hAyfc:nth-child(3) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                        .first()
                        .ownText();
                Log.e("latestversion", "---" + latestVersion);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (latestVersion != null) {
                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    updateAlertDialog();
                    // Toast.makeText(context,"update is available.",Toast.LENGTH_LONG).show();
//                    if (!(context instanceof SplashActivity)) {
//                        if (!((Activity) context).isFinishing()) {
//                            showForceUpdateDialog();
//                        }
//                    }
                }
                else
                    {
                        startLoginActivity();
                    }
            }
            super.onPostExecute(jsonObject);
        }
    }
    private void checkIfAppIsUpToDate()
    {
        StringRequest request = new StringRequest(Request.Method.GET, getPlaystoreLatestVersionUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("success");
                    if (status) {
                        int latestVersionNo = jsonObject.getInt("current_version");
                        int currentVersionNo = BuildConfig.VERSION_CODE;

                        if(latestVersionNo > currentVersionNo)
                        {
                            updateAlertDialog();
                        }
                        else
                        {
                            startLoginActivity();
                        }


                    }
                    else {
                        Log.i(TAG, "onResponse: status is false");
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "onResponse: json exception ->"+e.getMessage());
                    finish();
                }
            }
        }, error -> Log.i(TAG, "get Latest playstore version: "+error.getMessage()));
        RequestQueue queue = Volley.newRequestQueue(SplashActivity.this);
        queue.add(request);
    }
}