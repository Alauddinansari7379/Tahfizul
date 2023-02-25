package com.amtech.tahfizulquranonline.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.models.ServerResponse;
import com.amtech.tahfizulquranonline.utils.ApiConfig;
import com.amtech.tahfizulquranonline.utils.AppConfig;
import com.amtech.tahfizulquranonline.utils.AppConstants;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.amtech.tahfizulquranonline.utils.AppConstants.mainUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.talibAssignmentResponseUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.talibID;

public class TalibPendingAssignmentDetails extends AppCompatActivity {
    private String fileName;
    private String id, assignmentName, name, assignDate, assignDesc, fileLink;
    private TextView assignmentNameTxt, assignNameTxt, assignDateTxt, nameTxt, assignDescTxt, listenAudioBtn, audAttachLabel;
    private MediaPlayer mediaPlayer;
    private EditText assignResponseEdTxt;
    private TextView mediaPicker, uploadBtn, mediaLinkTxt, infoBtn;
    private AppCompatButton submitBtn;
    private ProgressDialog progressDialog, uploadDialog;
    private String assignNote;
    private String mediaPath;
    private String mediaFileLink;
    private MediaRecorder recorder;
    private MediaPlayer player;
    private String type;
    private static final String TAG = "TalibPendingAssignmentD";
    private Dialog dialog;
    TextView record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talib_pending_assignment_details);
        type = getIntent().getStringExtra("type");
        assignmentName = getIntent().getStringExtra("assign_name");
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        assignDate = getIntent().getStringExtra("assign_date");
        assignDesc = getIntent().getStringExtra("assign_des");
        fileLink = getIntent().getStringExtra("file_link");
        try {
            initMediaPlayer();
        } catch (Exception e) {

        }
        initView();
        setListener();
        findViewById(R.id.backBtn).setOnClickListener(v -> {
            onBackPressed();
        });
        setData();
        if (type == null) {
            disableViews();
        }
        initInfoDialog();
    }

    private void initView() {
        assignmentNameTxt = (TextView) findViewById(R.id.assignment_name_txt);
        assignNameTxt = (TextView) findViewById(R.id.assign_name_txt);
        assignDateTxt = (TextView) findViewById(R.id.assign_date);
        nameTxt = (TextView) findViewById(R.id.name_txt);
        assignDescTxt = (TextView) findViewById(R.id.assign_desc_txt);
        listenAudioBtn = (TextView) findViewById(R.id.listen_audio_btn);
        mediaPicker = (TextView) findViewById(R.id.media_picker);
        mediaLinkTxt = (TextView) findViewById(R.id.media_link);
        uploadBtn = (TextView) findViewById(R.id.upload_btn);
        submitBtn = (AppCompatButton) findViewById(R.id.submit_btn);
        assignResponseEdTxt = (EditText) findViewById(R.id.assign_note);
        audAttachLabel = (TextView) findViewById(R.id.aud_attach_label);
        infoBtn = (TextView) findViewById(R.id.support_aud_note);
        record = findViewById(R.id.record);
        record.setOnClickListener(v -> {
            showDialogFail();
        });
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
                    uploadFile(mediaPath);
                } else {
                    Toast.makeText(TalibPendingAssignmentDetails.this, "Select an audio file!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    submitAssignment();
                }
            }
        });
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    private void initInfoDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.info_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.bground));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        TextView okBtn = dialog.findViewById(R.id.ok_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void setData() {
        assignmentNameTxt.setText(assignmentName);
        assignNameTxt.setText(assignmentName);
        assignDateTxt.setText(assignDate);
        nameTxt.setText(name);
        assignDescTxt.setText(assignDesc);
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
    }

    private boolean validate() {
        if (assignResponseEdTxt.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Additional Assignment notes!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (AppConstants.mediaFileLink == null) {
            Toast.makeText(this, "Upload Audio File!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            assignNote = assignResponseEdTxt.getText().toString().trim();
        }
        return true;
    }

    //    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        try {
//            if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
//
//                // Get the Video from data
//                Uri selectedVideo = data.getData();
//                String[] filePathColumn = {MediaStore.Audio.Media.DATA};
//
//                Cursor cursor = getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
//                assert cursor != null;
//                cursor.moveToFirst();
//
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//
//                mediaPath = cursor.getString(columnIndex);
//                mediaLinkTxt.setText(mediaPath);
//                mediaFileLink = null;
//                // Set the Video Thumb in ImageView Previewing the Media
////                imgView.setImageBitmap(getThumbnailPathForLocalFile(DemoActivity.this, selectedVideo));
//                cursor.close();
//
//            } else {
//                Toast.makeText(TalibPendingAssignmentDetails.this, "You haven't picked Media1", Toast.LENGTH_LONG).show();
//            }
//        } catch (Exception e) {
//            Toast.makeText(TalibPendingAssignmentDetails.this, "Something went wrong", Toast.LENGTH_LONG).show();
//        }
//
//    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null) {
            if (requestCode == 1 && resultCode == RESULT_OK) {
                Log.i(TAG, "onActivityResult: " + data.getData());
                uploadDialog = ProgressDialog.show(TalibPendingAssignmentDetails.this, "Please Wait ", "Uploading file...");
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //File f = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                        Uri uri = data.getData();
                        String path = getAudioPath(uri);
                        Log.i(TAG, "run: getPath : " + path);
                        File f = new File(path);
                        String ext = path.substring(path.lastIndexOf(".") + 1);
                        String contentType = getMimeType(ext);
                        Log.i(TAG, "run: getMimeType : " + contentType);
                        String filePath = f.getAbsolutePath();
                        Log.i(TAG, "run: getAbsolutePath : " + filePath);
                        OkHttpClient client = new OkHttpClient();
                        RequestBody fileBody = RequestBody.create(MediaType.parse(contentType), f);

                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("file", filePath.substring(filePath.lastIndexOf("/") + 1), fileBody)
                                //.addFormDataPart("type", contentType)
                                //.addFormDataPart("uploaded_file", filePath.substring(filePath.lastIndexOf("/")+1), fileBody)
                                .build();

                        okhttp3.Request request = new okhttp3.Request.Builder()
                                //.url("http://192.168.1.7/paulwicky/audioupload/save_file.php")
                                .url(mainUrl + "fileupload")
                                .post(requestBody)
                                .build();
                        try {
                            okhttp3.Response response = client.newCall(request).execute();
                            if (response.isSuccessful()) {
                                Gson gson = new Gson();
                                ResponseBody responseBody = response.body();
                                ServerResponse entity = gson.fromJson(responseBody.string(), ServerResponse.class);
//                                    Assert.assertNotNull(entity);
//                                    Assert.assertEquals(sampleResponse.getName(), entity.getName());
                                if (entity.getStatus()) {
                                    AppConstants.mediaFileLink = "uploads/" + entity.getFilePath();
                                    Log.i(TAG, "run: uploaded file link : " + AppConstants.mediaFileLink);
                                    uploadDialog.dismiss();
                                    Handler handler = new Handler(Looper.getMainLooper());
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            showSuccessMsg("UPLOADED SUCCESSFULLY");
                                        }
                                    });
                                }
                            } else {
                                throw new IOException("Error : " + response);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                t.start();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getMimeType(String ext) {
        //String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
    }

    private String getAudioPath(Uri uri) {
        String[] data = {MediaStore.Audio.Media.DATA};
//        CursorLoader loader = new CursorLoader(getContext(), uri, data, null, null, null);
//        Cursor cursor = loader.loadInBackground();
        Cursor cursor = getContentResolver().query(uri, data, null, null, null);
        assert cursor != null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void showSuccessMsg(String msg) {
        Toast.makeText(TalibPendingAssignmentDetails.this, "" + msg, Toast.LENGTH_SHORT).show();
    }

    private void uploadFile(String mediaPathS) {
        progressDialog = new ProgressDialog(TalibPendingAssignmentDetails.this);
        progressDialog.setMessage("Uploading...");
        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(mediaPathS);

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
                        Toast.makeText(TalibPendingAssignmentDetails.this, "Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                        AppConstants.mediaFileLink = "uploads/" + serverResponse.getFilePath();
                        Log.i(TAG, "onResponse:" + serverResponse.getFilePath());
                    } else {
                        Toast.makeText(TalibPendingAssignmentDetails.this, serverResponse.getFilePath(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    assert serverResponse != null;
                    Log.v("Response", serverResponse.toString());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Toast.makeText(TalibPendingAssignmentDetails.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitAssignment() {
        progressDialog = ProgressDialog.show(TalibPendingAssignmentDetails.this, "", "Please wait...");
        StringRequest request = new StringRequest(Request.Method.POST, talibAssignmentResponseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String res = response.trim();
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        AppConstants.mediaFileLink = null;
                        Toast.makeText(TalibPendingAssignmentDetails.this, "Assignment Submitted Successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(TalibPendingAssignmentDetails.this, TalibMainActivity.class);
                        intent.putExtra("frag", "pending_assignment");
                        startActivity(intent);
                        finish();
                    } else {
                        //no record found
                        Toast.makeText(TalibPendingAssignmentDetails.this, "Failed to submit!", Toast.LENGTH_SHORT).show();
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
                params.put("talib_ilm_id", talibID);
                params.put("description", assignNote);
                params.put("assignment_file", AppConstants.mediaFileLink);
                Log.i(TAG, "getParams: " + params.toString());
                return params;

            }
        };
        RequestQueue queue = Volley.newRequestQueue(TalibPendingAssignmentDetails.this);
        queue.add(request);
    }

    private void disableViews() {
        assignResponseEdTxt.setVisibility(View.GONE);
        submitBtn.setVisibility(View.GONE);
        mediaPicker.setVisibility(View.GONE);
        record.setVisibility(View.GONE);
        uploadBtn.setVisibility(View.GONE);
        mediaLinkTxt.setVisibility(View.GONE);
        audAttachLabel.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {

        if (type != null) {
            startActivity(new Intent(this, TalibMainActivity.class));
            finish();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onPause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }

        super.onPause();
    }


    private void startRecording() {
        String uuid = UUID.randomUUID().toString();
        fileName = getFilesDir().getPath() + "/" + uuid + ".mp3";
        Log.i(MainActivity.class.getSimpleName(), fileName);
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(MainActivity.class.getSimpleName() + ":startRecording()", "prepare() failed");
        }
        recorder.start();
    }

    private void stopRecording() {
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
    }

    private void playRecording() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    stopPlaying();
                }
            });
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(MainActivity.class.getSimpleName() + ":playRecording()", "prepare() failed");
        }
    }

    private void stopPlaying() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    public void showDialogFail() {
        Dialog dialog = new Dialog(this, R.style.SheetDialog);
        dialog.setContentView(R.layout.record_dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LottieAnimationView lottieAnimationView = dialog.findViewById(R.id.animation);
        lottieAnimationView.pauseAnimation();
        dialog.findViewById(R.id.play).setOnClickListener(v -> {
            if (player != null) {
                player.pause();
            }
            startRecording();
            dialog.findViewById(R.id.play).setVisibility(View.INVISIBLE);
            dialog.findViewById(R.id.pause).setVisibility(View.VISIBLE);
            dialog.findViewById(R.id.save).setVisibility(View.INVISIBLE);
            lottieAnimationView.playAnimation();
        });
        dialog.findViewById(R.id.pauseBtn).setOnClickListener(v -> {
            if (player != null) {
                player.pause();
            }
            dialog.findViewById(R.id.playBtn).setVisibility(View.VISIBLE);

            dialog.findViewById(R.id.pauseBtn).setVisibility(View.INVISIBLE);
        });
        dialog.findViewById(R.id.pause).setOnClickListener(v -> {
            if (player != null) {
                player.pause();
            }
            stopRecording();
            Toast.makeText(this, "Audio Recorded", Toast.LENGTH_SHORT).show();
            dialog.findViewById(R.id.playBtn).setVisibility(View.VISIBLE);
            dialog.findViewById(R.id.save).setVisibility(View.VISIBLE);
            dialog.findViewById(R.id.pause).setVisibility(View.INVISIBLE);
            lottieAnimationView.pauseAnimation();
        });
        dialog.findViewById(R.id.playBtn).setOnClickListener(v -> {
            if (player != null) {
                player.pause();
            }
            playRecording();
            dialog.findViewById(R.id.playBtn).setVisibility(View.INVISIBLE);
            dialog.findViewById(R.id.pauseBtn).setVisibility(View.VISIBLE);
        });
        dialog.findViewById(R.id.save).setOnClickListener(v -> {
            dialog.dismiss();
            uploadFile(fileName);
        });
        dialog.findViewById(R.id.closeBtn).setOnClickListener(v -> {
            if (player != null) {
                player.pause();
            }
            dialog.dismiss();
        });
        dialog.show();
    }
}