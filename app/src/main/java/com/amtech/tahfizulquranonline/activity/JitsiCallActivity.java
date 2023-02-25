package com.amtech.tahfizulquranonline.activity;

/**
 * Created by Shourav Paul on 15-12-2021.
 **/
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.facebook.react.modules.core.PermissionListener;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate;
import org.jitsi.meet.sdk.JitsiMeetActivityInterface;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetView;
import org.jitsi.meet.sdk.JitsiMeetViewListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static com.amtech.tahfizulquranonline.utils.AppConstants.MAULIM;
import static com.amtech.tahfizulquranonline.utils.AppConstants.loginUser;


/**
 * Created by Aftab Alam on 25-02-2021.
 **/
public class JitsiCallActivity extends FragmentActivity implements JitsiMeetActivityInterface, JitsiMeetViewListener {

    private JitsiMeetView jitView = null;
    private String talibId, maulimId;
    private AlertDialog alertDialog;
    private Handler handler, handler2;
    private int sessionTimeInSec = 60*20;
    private int timeLeft;
    private  boolean hasCtrStarted = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        talibId = getIntent().getStringExtra("talib_id");
        maulimId = getIntent().getStringExtra("maulim_id");
        timeLeft = getIntent().getIntExtra("time_left", 0);
        jitView = new JitsiMeetView(this);
        URL serverURL;
        try {
            serverURL = new URL("https://ka-nnect.com/new");//https://meet.amtechgcc.com
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid server URL!");
        }
        JitsiMeetConferenceOptions defaultOptions
                = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                .setWelcomePageEnabled(false)
                .build();
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);

        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                .setRoom("Call_tfl_" + talibId + "connectwith" + maulimId)
                .setSubject("")
                .setFeatureFlag("chat.enabled", false)
                .setFeatureFlag("invite.enabled", false)
                .setFeatureFlag("calendar.enabled", false)
                .setWelcomePageEnabled(false)
                .build();
//        JitsiMeetActivity.launch(JitsiCallActivity.this, options);
        jitView.join(options);

        setContentView(jitView);
        jitView.setListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        JitsiMeetActivityDelegate.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void requestPermissions(String[] strings, int i, PermissionListener permissionListener) {

    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        JitsiMeetActivityDelegate.onNewIntent(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        JitsiMeetActivityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Disconnect Call to go back!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConferenceJoined(Map<String, Object> map) {
        startSessionCounter();
        hasCtrStarted = true;
    }

    @Override
    public void onConferenceTerminated(Map<String, Object> map) {
        Log.d("TAG", "onConferenceTerminated: "+map);
        if(hasCtrStarted)
        {
            handler.removeMessages(0);
        }
        Intent intent;
        if(loginUser == MAULIM)
        {
            finish();
        }
        else
        {
            finish();
        }


    }

    @Override
    public void onConferenceWillJoin(Map<String, Object> map) {

    }

    public void startSessionCounter() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog = new AlertDialog.Builder(JitsiCallActivity.this)
                        .setCancelable(false)
                        .setMessage("Your Session is Over!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                                handler2.removeMessages(0);
                                jitView.leave();
                            }
                        })
                        .show();
                startAutoDisconnectHandler();

            }
        }, timeLeft * 1000);
    }
    private void startAutoDisconnectHandler()
    {
        handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {

                alertDialog.dismiss();
                jitView.leave();
            }
        }, 5000);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        jitView.leave();
    }
}
