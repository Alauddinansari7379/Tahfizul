package com.amtech.tahfizulquranonline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.models.ServerResponse;
import com.amtech.tahfizulquranonline.utils.ApiConfig;
import com.amtech.tahfizulquranonline.utils.AppConfig;
import com.amtech.tahfizulquranonline.utils.MyTimeCalculator;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.amtech.tahfizulquranonline.utils.AppConstants.maulimCompleteAssignmentUrl;

public class MaulimCompleteAssignmentActivity extends AppCompatActivity {

    private String id, assignmentName, name, assignDate, assignDesc, fileLink, talibFileLink, talibResponse, submissionDate, maulimRemark;
    private TextView assignmentNameTxt, assignNameTxt, assignDateTxt, nameTxt, assignDescTxt, listenAudioBtn, headerStatusTxt, listenAudioTalibBTn, talibResponseTxt, submissionDateTxt;
    private MediaPlayer mediaPlayer, tMediaPlayer;
    private EditText assignResponseEdTxt, maulimRemarkEdTxt;
    private TextView mediaPicker, uploadBtn, mediaLinkTxt;
    private AppCompatButton submitBtn;
    private ProgressDialog progressDialog;
    private String assignNote;
    private String mediaPath;
    private String mediaFileLink;
    private static final String TAG = "MaulimCompleteAssignmen";
    private String type;
    private TextView maulimRemarkTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maulim_complete_assignment);
        assignmentName = getIntent().getStringExtra("assign_name");
        type = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        assignDate = getIntent().getStringExtra("assign_date");
        assignDesc = getIntent().getStringExtra("assign_des");
        fileLink = getIntent().getStringExtra("file_link");
        talibFileLink = getIntent().getStringExtra("talib_file_link");
        Log.i(TAG, "onCreate: talib file link" + talibFileLink);
        submissionDate = getIntent().getStringExtra("submission_date");
        talibResponse = getIntent().getStringExtra("talib_response");
        initMediaPlayer();
        initView();
        setListener();
        setData();
        findViewById(R.id.backBtn).setOnClickListener(v -> {
            onBackPressed();
        });
        if (type == null) {
            assignResponseEdTxt.setVisibility(View.GONE);
            submitBtn.setVisibility(View.GONE);
            headerStatusTxt.setText("STATUS : COMPLETED");
            maulimRemarkTxt.setText("Maulim Remark : " + getIntent().getStringExtra("maulim_remark_on_comp"));
        } else {
            maulimRemarkTxt.setVisibility(View.GONE);
        }
    }

    private void initView() {
        assignmentNameTxt = (TextView) findViewById(R.id.assignment_name_txt);
        assignNameTxt = (TextView) findViewById(R.id.assign_name_txt);
        assignDateTxt = (TextView) findViewById(R.id.assign_date);
        nameTxt = (TextView) findViewById(R.id.name_txt);
        assignDescTxt = (TextView) findViewById(R.id.assign_desc_txt);
        listenAudioBtn = (TextView) findViewById(R.id.listen_audio_btn);
        listenAudioTalibBTn = (TextView) findViewById(R.id.listen_talib_audio_btn);
        headerStatusTxt = (TextView) findViewById(R.id.header_status);
        mediaPicker = (TextView) findViewById(R.id.media_picker);
        mediaLinkTxt = (TextView) findViewById(R.id.media_link);
        uploadBtn = (TextView) findViewById(R.id.upload_btn);
        submitBtn = (AppCompatButton) findViewById(R.id.submit_btn);
        assignResponseEdTxt = (EditText) findViewById(R.id.maulim_remark_ed_txt);
        talibResponseTxt = (TextView) findViewById(R.id.talib_response_txt);
        submissionDateTxt = (TextView) findViewById(R.id.submission_txt);
        maulimRemarkTxt = (TextView) findViewById(R.id.moulim_remark_txt);

    }

    private void setListener() {
        listenAudioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                } else {
                    mediaPlayer.pause();
                }
            }
        });
        listenAudioTalibBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tMediaPlayer.isPlaying()) {
                    tMediaPlayer.start();
                } else {
                    tMediaPlayer.pause();
                }
            }
        });
        mediaPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1);
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mediaLinkTxt.getText().equals("")) {
                    uploadFile();
                } else {
                    Toast.makeText(MaulimCompleteAssignmentActivity.this, "Select an audio file!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    completeAssignment();
                }
            }
        });
    }

    private void setData() {
        assignmentNameTxt.setText(assignmentName);
        assignNameTxt.setText(assignmentName);
        assignDateTxt.setText(assignDate);
        nameTxt.setText(name);
        assignDescTxt.setText(assignDesc);
        talibResponseTxt.setText(talibResponse);
        submissionDateTxt.setText("Submitted On : " + submissionDate);
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(fileLink);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tMediaPlayer = new MediaPlayer();
        tMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            tMediaPlayer.setDataSource(talibFileLink);
            tMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validate() {
        if (assignResponseEdTxt.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Remarks for Assignment!", Toast.LENGTH_SHORT).show();
            return false;
        }
//        else if(mediaFileLink == null)
//        {
//            Toast.makeText(this, "Upload the selected audio file!", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        else {
            assignNote = assignResponseEdTxt.getText().toString().trim();
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == RESULT_OK && null != data) {

                // Get the Video from data
                Uri selectedVideo = data.getData();
                String[] filePathColumn = {MediaStore.Audio.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                mediaPath = cursor.getString(columnIndex);
                mediaLinkTxt.setText(mediaPath);
                mediaFileLink = null;
                // Set the Video Thumb in ImageView Previewing the Media
//                imgView.setImageBitmap(getThumbnailPathForLocalFile(DemoActivity.this, selectedVideo));
                cursor.close();

            } else {
                Toast.makeText(MaulimCompleteAssignmentActivity.this, "You haven't picked Media1", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(MaulimCompleteAssignmentActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }

    private void uploadFile() {
        progressDialog = new ProgressDialog(MaulimCompleteAssignmentActivity.this);
        progressDialog.setMessage("Uploading...");
        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(mediaPath);

        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<ServerResponse> call = getResponse.uploadFile(fileToUpload, filename);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                Log.i(TAG, "onResponse: " + response.body().toString());
                if (serverResponse != null) {
                    if (serverResponse.getStatus()) {
                        Toast.makeText(MaulimCompleteAssignmentActivity.this, "Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                        mediaFileLink = "uploads/" + serverResponse.getFilePath();
                        Log.i(TAG, "onResponse:" + serverResponse.getFilePath());
                    } else {
                        Toast.makeText(MaulimCompleteAssignmentActivity.this, serverResponse.getFilePath(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    assert serverResponse != null;
                    Log.v("Response", serverResponse.toString());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Toast.makeText(MaulimCompleteAssignmentActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void completeAssignment() {
        progressDialog = ProgressDialog.show(MaulimCompleteAssignmentActivity.this, "", "Please wait...");
        StringRequest request = new StringRequest(Request.Method.POST, maulimCompleteAssignmentUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String res = response.trim();
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        Toast.makeText(MaulimCompleteAssignmentActivity.this, "Assignment Completed Successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MaulimCompleteAssignmentActivity.this, MainActivity.class);
                        intent.putExtra("frag", "complete_assignment");
                        startActivity(intent);
                        finish();
                    } else {
                        //no record found
                        Toast.makeText(MaulimCompleteAssignmentActivity.this, "Failed to complete!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("assignment_id", id);
                params.put("comments", assignNote);
                params.put("completedDate", new MyTimeCalculator().getAddedDateInStr(0));
                Log.i(TAG, "getParams: " + params.toString());
                return params;

            }
        };
        RequestQueue queue = Volley.newRequestQueue(MaulimCompleteAssignmentActivity.this);
        queue.add(request);
    }

    @Override
    protected void onPause() {
        if (mediaPlayer!=null)
        {
            mediaPlayer.pause();
        }

        if (tMediaPlayer!=null)
        {
            tMediaPlayer.pause();
        }
        super.onPause();
    }
}