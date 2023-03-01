package com.amtech.tahfizulquranonline.activity;

/**
 * Created by Shourav Paul on 15-12-2021.
 **/

import static com.amtech.tahfizulquranonline.record.hbrecorder.Constants.MAX_FILE_SIZE_REACHED_ERROR;
import static com.amtech.tahfizulquranonline.record.hbrecorder.Constants.SETTINGS_ERROR;
import static com.amtech.tahfizulquranonline.utils.AppConstants.MAULIM;
import static com.amtech.tahfizulquranonline.utils.AppConstants.loginUser;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.record.MainActivity;
import com.amtech.tahfizulquranonline.record.hbrecorder.HBRecorder;
import com.amtech.tahfizulquranonline.record.hbrecorder.HBRecorderListener;
import com.amtech.tahfizulquranonline.record.hbrecorder.ScreenRecordService;
import com.facebook.react.modules.core.PermissionListener;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate;
import org.jitsi.meet.sdk.JitsiMeetActivityInterface;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetView;
import org.jitsi.meet.sdk.JitsiMeetViewListener;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;


/**
 * Created by Aftab Alam on 25-02-2021.
 **/
public class JitsiCallActivity extends FragmentActivity implements JitsiMeetActivityInterface, JitsiMeetViewListener, HBRecorderListener {


    private JitsiMeetView jitView = null;
    private String talibId, maulimId;
    private AlertDialog alertDialog;
    private Handler handler, handler2;
    private int sessionTimeInSec = 60 * 20;
    private int timeLeft;
    private boolean hasCtrStarted = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        MainActivity.hbRecorder = new HBRecorder(this, this);

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
        callStart();
    }

    @Override
    public void onConferenceTerminated(Map<String, Object> map) {
        Log.d("TAG", "onConferenceTerminated: "+map);
        if(hasCtrStarted)
        {
            handler.removeMessages(0);
        }
        Intent intent;
        callEnd();
        finish();


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

    public void callStart() {
        if (getIntent() != null && getIntent().getBooleanExtra("is_maulim", false)) {
            Log.d("TAG", "callStart: ");
            Intent mResultData = getIntent().getParcelableExtra("data");
            MainActivity.hbRecorder.startScreenRecording(mResultData, getIntent().getIntExtra("resultCode", 0));
        }


    }

    public void callEnd() {
        if (getIntent() != null && getIntent().getBooleanExtra("is_maulim", false)) {
            Log.d("TAG", "callEnd: ");
            Intent service = new Intent(this, ScreenRecordService.class);
            stopService(service);
        }
    }

    @Override
    public void HBRecorderOnStart() {

    }

    @Override
    public void HBRecorderOnComplete() {
        //Update gallery depending on SDK Level
        if (MainActivity.hbRecorder.wasUriSet()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ) {
                updateGalleryUri();
            } else {
                refreshGalleryFile();
            }
        }else{
            refreshGalleryFile();
        }

    }
    ContentResolver resolver;
    ContentValues contentValues;
    Uri mUri;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setOutputPath() {
        String filename = generateFileName();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            resolver = getContentResolver();
            contentValues = new ContentValues();
            contentValues.put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/" + "HBRecorder");
            contentValues.put(MediaStore.Video.Media.TITLE, filename);
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, filename);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
            mUri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
            //FILE NAME SHOULD BE THE SAME
            MainActivity.hbRecorder.setFileName(filename);
            MainActivity.hbRecorder.setOutputUri(mUri);
        }else{
            createFolder();
            MainActivity.hbRecorder.setOutputPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) +"/HBRecorder");
        }
    }
    private void createFolder() {
        File f1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "HBRecorder");
        if (!f1.exists()) {
            if (f1.mkdirs()) {
                Log.i("Folder ", "created");
            }
        }
    }
    //Generate a timestamp to be used as a file name
    private String generateFileName() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate).replace(" ", "");
    }

    //Show Toast
    private void showLongToast(final String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    @Override
    public void HBRecorderOnError(int errorCode, String reason) {
        if (errorCode == SETTINGS_ERROR) {
            showLongToast(getString(R.string.settings_not_supported_message));
        } else if ( errorCode == MAX_FILE_SIZE_REACHED_ERROR) {
            showLongToast(getString(R.string.max_file_size_reached_message));
        } else {
            showLongToast(getString(R.string.general_recording_error_message));
            Log.e("HBRecorderOnError", reason);
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void updateGalleryUri(){
        contentValues.clear();
        contentValues.put(MediaStore.Video.Media.IS_PENDING, 0);
        getContentResolver().update(mUri, contentValues, null, null);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void refreshGalleryFile() {
        MediaScannerConnection.scanFile(this,
                new String[]{MainActivity.hbRecorder.getFilePath()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }
}
