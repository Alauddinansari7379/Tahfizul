package com.amtech.tahfizulquranonline.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.amtech.tahfizulquranonline.LoadingDialog;
import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.activity.MainActivity;
import com.amtech.tahfizulquranonline.adapter.StudentAdapter;
import com.amtech.tahfizulquranonline.models.ServerResponse;
import com.amtech.tahfizulquranonline.models.StudentModel;
import com.amtech.tahfizulquranonline.utils.ApiConfig;
import com.amtech.tahfizulquranonline.utils.AppConfig;
import com.amtech.tahfizulquranonline.utils.AppConstants;
import com.amtech.tahfizulquranonline.utils.Constant;
import com.amtech.tahfizulquranonline.utils.MyTimeCalculator;
import com.amtech.tahfizulquranonline.utils.Util;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
//import com.nbsp.materialfilepicker.MaterialFilePicker;
//import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.amtech.tahfizulquranonline.utils.AppConstants.mainUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.maulimCreateAssignmentUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.maulimGetMyTalibListUrl;
import static com.amtech.tahfizulquranonline.utils.AppConstants.maulimID;

/**
 * Created by Shourav Paul on 24-12-2021.
 **/
public class NewAssignment extends Fragment implements View.OnClickListener {
    private MediaRecorder recorder;
    private MediaPlayer player;
    private String fileName;
    private LinearLayout assignTypePicker, assignType, stdntPicker, dailyAssignItem, weeklyAssignItem;
    private TextView assignTypeTxt, mediaPicker, uploadBtn, submitBtn, studentNameTxt, mediaLinkTxt, infoBtn;
    private EditText aboutEdTxt;
    private StudentAdapter adapter;
    private RecyclerView studentRecycler;
    private List<StudentModel> studentList = new ArrayList<>();
    private ProgressDialog progressDialog, uploadDialog;
    private Context mContext;
    private String mediaPath;
    private static final String TAG = "NewAssignment";
    private String talibId, madarsaId, assignedMaulimId, name;
    private String about;
    private String assignmentType;
    //public static String mediaFileLink;
    private String assignedDate, deadLine;
    private Dialog dialog;
    TextView record;
    LoadingDialog loadingDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_assignment, container, false);
        player = new MediaPlayer();
        loadingDialog = new LoadingDialog(mContext);
        initView(view);
        setListener();
        initRecycler(view);
        getMyStudents();
        initInfoDialog();
        return view;
    }

    private void initView(View view) {
        record = view.findViewById(R.id.record);
        assignTypePicker = (LinearLayout) view.findViewById(R.id.assign_type_picker);
        assignType = (LinearLayout) view.findViewById(R.id.assignment_type);
        stdntPicker = (LinearLayout) view.findViewById(R.id.student_picker);
        assignTypeTxt = (TextView) view.findViewById(R.id.assign_type_txt);
        dailyAssignItem = (LinearLayout) view.findViewById(R.id.daily_assign);
        weeklyAssignItem = (LinearLayout) view.findViewById(R.id.weekly_assign);
        studentNameTxt = (TextView) view.findViewById(R.id.student_name_txt);
        aboutEdTxt = (EditText) view.findViewById(R.id.about_edtxt);
        mediaPicker = (TextView) view.findViewById(R.id.media_picker);
        mediaLinkTxt = (TextView) view.findViewById(R.id.media_link);
        uploadBtn = (TextView) view.findViewById(R.id.upload_btn);
        submitBtn = (TextView) view.findViewById(R.id.submit_btn);
        infoBtn = (TextView) view.findViewById(R.id.support_aud_note);
        record.setOnClickListener(v -> {
            showDialogFail();
        });
        ((EditText) view.findViewById(R.id.about_edtxt)).setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH
                                || actionId == EditorInfo.IME_ACTION_DONE
                                || event.getAction() == KeyEvent.ACTION_DOWN
                                && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            return true;
                        }
                        // Return true if you have consumed the action, else false.
                        return false;
                    }
                });
    }

    private void setListener() {
        assignTypePicker.setOnClickListener(this);
        stdntPicker.setOnClickListener(this);
        dailyAssignItem.setOnClickListener(this);
        weeklyAssignItem.setOnClickListener(this);
        mediaPicker.setOnClickListener(this);
        uploadBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        infoBtn.setOnClickListener(this);
    }

    private void initRecycler(View view) {
        studentRecycler = (RecyclerView) view.findViewById(R.id.student_rec);
        adapter = new StudentAdapter(studentList, mContext, getListener());
        studentRecycler.setHasFixedSize(true);
        studentRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        studentRecycler.setAdapter(adapter);
    }

    private void initInfoDialog() {
        dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.info_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(mContext.getDrawable(R.drawable.bground));
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

    private StudentAdapter.OnStudentItemClickListener getListener() {
        return new StudentAdapter.OnStudentItemClickListener() {
            @Override
            public void onClick(int pos) {
                studentNameTxt.setText(studentList.get(pos).getName());
                talibId = studentList.get(pos).getId();
                name = studentList.get(pos).getName();
                madarsaId = studentList.get(pos).getMadarsaId();
                assignedMaulimId = studentList.get(pos).getAssignedMualemId();
                studentRecycler.setVisibility(View.GONE);
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.assign_type_picker:
                assignmentPickerAction();
                break;
            case R.id.student_picker:
                studentPickerAction();
                break;
            case R.id.daily_assign: {
                assignTypeTxt.setText("DAILY ASSIGNMENT");
                assignType.setVisibility(View.GONE);
                assignmentType = "1";
            }
            break;
            case R.id.weekly_assign: {
                assignTypeTxt.setText("WEEKLY ASSIGNMENT");
                assignType.setVisibility(View.GONE);
                assignmentType = "2";
            }
            break;
            case R.id.media_picker: {
//                    new MaterialFilePicker()
//                            .withActivity(getActivity())
//                            .withRequestCode(1)
//                            .start();
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(galleryIntent, "Pick"), 1);
                //result uri
                //content://media/external/audio/media/1951
//                    Intent intent_upload = new Intent(Intent.ACTION_GET_CONTENT);
//                    intent_upload.setType("audio/*");
//                    startActivityForResult(intent_upload,1);
                //result uri
                //content://com.android.providers.media.documents/document/audio%3A1951
            }
            break;
            case R.id.support_aud_note:
                dialog.show();
            case R.id.upload_btn:
//                if(!mediaLinkTxt.getText().equals(""))
//                {
//                    //uploadFile();
//                    uploadDialog = ProgressDialog.show(mContext, "Please Wait", "Uploading File...");
//                    new UploadMediaFile(mContext, mediaPath, new UploadMediaFile.OnUploadListener() {
//                        @Override
//                        public void onUpload(boolean success) {
//                            if(success)
//                            {
//                                uploadDialog.dismiss();
//                            }
//                            else
//                            {
//                                uploadDialog.dismiss();
//                                Toast.makeText(mContext, "failed to upload!", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }).execute();
//                }
//                else
//                {
//                    Toast.makeText(mContext, "Select an audio file!", Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.submit_btn:
                if (validate()) {
                    createAssignment();
                }

        }
    }

    private void assignmentPickerAction() {
        if (assignType.getVisibility() == View.GONE) {
            assignType.setVisibility(View.VISIBLE);
        } else {
            assignType.setVisibility(View.GONE);
        }
    }

    private void studentPickerAction() {
        if (studentRecycler.getVisibility() == View.GONE) {
            studentRecycler.setVisibility(View.VISIBLE);
        } else {
            studentRecycler.setVisibility(View.GONE);
        }
    }

    private void itemPickerAction() {

    }

    private void getMyStudents() {
        progressDialog = ProgressDialog.show(mContext, "", "Please wait...");
        StringRequest request = new StringRequest(Request.Method.GET, maulimGetMyTalibListUrl + maulimID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                //Toast.makeText(MualemHomeActivity.this, "Slot List", Toast.LENGTH_SHORT).show();
                String res = response.trim();
                studentList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                StudentModel model = new StudentModel();
                                model.setId(jsonObject1.getString("id"));
                                model.setName(jsonObject1.getString("name"));
                                model.setMadarsaId(jsonObject1.getString("madarsa_id"));
                                model.setAssignedMualemId(jsonObject1.getString("assigned_mualem_id"));
                                studentList.add(model);
                            }
                        } else {
                            //no record found
                            Toast.makeText(mContext, "No Talib Found!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //no record found
                        Toast.makeText(mContext, "No Talib Found!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: " + error.getMessage());
            }
        });
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);
    }

    private void createAssignment() {
//        Calendar calendar = Calendar.getInstance();
//        String assignedDate = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH);

        if (assignmentType.equals("1")) {
            assignedDate = new MyTimeCalculator().getAddedDateInStr(1);
            deadLine = new MyTimeCalculator().getAddedDateInStr(1);
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                LocalDate currentDate = LocalDate.now();
//                LocalDate result = currentDate.plus(1, DAYS);
//                assignedDate = result.toString();
//                deadLine = result.toString();
//            }

        } else {
            assignedDate = new MyTimeCalculator().getAddedDateInStr(7);
            deadLine = new MyTimeCalculator().getAddedDateInStr(7);
        }
        progressDialog = ProgressDialog.show(mContext, "", "Please wait...");
        StringRequest request = new StringRequest(Request.Method.POST, maulimCreateAssignmentUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                //Toast.makeText(MualemHomeActivity.this, "Slot List", Toast.LENGTH_SHORT).show();
                String res = response.trim();
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        Toast.makeText(mContext, "Assignment Created Successfully!", Toast.LENGTH_SHORT).show();
                        if (Constant.mainActivity != null) {
                            Constant.mainActivity.viewSelect();
                        }
                        //   resetValues();
                    } else {
                        //no record found
                        Toast.makeText(mContext, "Failed to create!", Toast.LENGTH_SHORT).show();
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
                params.put("madarsa_id", madarsaId);
                params.put("mualem_id", maulimID);
                params.put("assigned_talib_ilm_id", talibId);
                params.put("para_id", "1");
                params.put("surah_id", "1");
                params.put("ruku", "1");
                params.put("assignment_type_id", assignmentType);
                params.put("name", name);
                params.put("description", about);
                params.put("deadline", deadLine);
                params.put("assigned_date", assignedDate);
                params.put("file", AppConstants.mediaFileLink);
                Log.i(TAG, "getParams: " + params.toString());
                return params;

            }
        };
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);
    }

    private boolean validate() {
        if (assignTypeTxt.getText().toString().trim().equals("ASSIGNMENT TYPE")) {
            Toast.makeText(mContext, "Select Assignment Type!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (studentNameTxt.getText().toString().trim().equals("STUDENT NAME")) {
            Toast.makeText(mContext, "Select Student!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (aboutEdTxt.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "Assignment description cannot be empty!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (AppConstants.mediaFileLink == null) {
            Toast.makeText(mContext, "Upload the selected audio file!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            about = aboutEdTxt.getText().toString().trim();
        }
        return true;
    }

    private void resetValues() {
        assignTypeTxt.setText("ASSIGNMENT TYPE");
        studentNameTxt.setText("STUDENT NAME");
        aboutEdTxt.getText().clear();
        AppConstants.mediaFileLink = null;
        mediaLinkTxt.setText("");
    }
    /////
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if(data != null)
//        {
//            if(requestCode == 1 && resultCode == RESULT_OK)
//            {
//                Uri uri = data.getData();
//                Log.i(TAG, "onActivityResult: Uri => "+uri);
//                try {
//                    String uriString = uri.toString();
//                    File myFile = new File(uriString);
//                    //    String path = myFile.getAbsolutePath();
//                    String displayName = null;
//                    String path2 = getAudioPath(uri);
//                    File f = new File(path2);
//                    Log.i(TAG, "onActivityResult: AudioPath => "+path2);
//                    long fileSizeInBytes = f.length();
//                    long fileSizeInKB = fileSizeInBytes / 1024;
//                    long fileSizeInMB = fileSizeInKB / 1024;
//                    if (fileSizeInMB > 8) {
//                        //customAlterDialog("Can't Upload ", "sorry file size is large");
//                        Toast.makeText(mContext, "Cannot Upload any file of large size!", Toast.LENGTH_SHORT).show();
//                    } else {
//                        mediaPath = path2;
//                        mediaLinkTxt.setText(mediaPath);
//                        AppConstants.mediaFileLink = null;
//                    }
//                } catch (Exception e) {
//                    //handle exception
//                    Toast.makeText(getContext(), "Unable to process,try again"+e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//
//                //--
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//        //Old audio file selecting from storage code
////        try {
////            if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
////
////                // Get the Video from data
////                Uri selectedVideo = data.getData();
////                String[] filePathColumn = {MediaStore.Audio.Media.DATA};
////
////                Cursor cursor = mContext.getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
////                assert cursor != null;
////                cursor.moveToFirst();
////
////                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
////
////                mediaPath = cursor.getString(columnIndex);
////                mediaLinkTxt.setText(mediaPath);
////                mediaFileLink = null;
////                // Set the Video Thumb in ImageView Previewing the Media
//////                imgView.setImageBitmap(getThumbnailPathForLocalFile(DemoActivity.this, selectedVideo));
////                cursor.close();
////
////            } else {
////                Toast.makeText(mContext, "You haven't picked Media1", Toast.LENGTH_LONG).show();
////            }
////        } catch (Exception e) {
////            Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_LONG).show();
////        }
//
//    }


    private String getMimeType(String ext) {
        //String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
    }

    private String getAudioPath(Uri uri) {
        String[] data = {MediaStore.Audio.Media.DATA};
//        CursorLoader loader = new CursorLoader(getContext(), uri, data, null, null, null);
//        Cursor cursor = loader.loadInBackground();
        Cursor cursor = mContext.getContentResolver().query(uri, data, null, null, null);
        assert cursor != null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void uploadFile() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        File file = new File(fileName);
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
                        Toast.makeText(mContext, "Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                        AppConstants.mediaFileLink = "uploads/" + serverResponse.getFilePath();
                        Log.i(TAG, "onResponse:" + serverResponse.getFilePath());
                    } else {
                        Toast.makeText(mContext, serverResponse.getFilePath(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    assert serverResponse != null;
                    Log.v("Response", serverResponse.toString());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Toast.makeText(mContext, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSuccessMsg(String msg) {
        Toast.makeText(mContext, "" + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 1 && resultCode == RESULT_OK) {
                Log.i(TAG, "onActivityResult: " + data.getData());
                uploadDialog = ProgressDialog.show(mContext, "Please Wait ", "Uploading file...");
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
//                                throw new IOException("Error : "+response);
                                Log.i(TAG, "all error : failure response : " + response);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.i(TAG, "all error : IOException : " + e.getMessage());
                        }
                    }
                });
                t.start();
            }
        }
    }

    private void startRecording() {
        String uuid = UUID.randomUUID().toString();
        fileName = getActivity().getFilesDir().getPath() + "/" + uuid + ".mp3";
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
        Dialog dialog = new Dialog(requireActivity(), R.style.SheetDialog);
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
            Toast.makeText(mContext, "Audio Recorded", Toast.LENGTH_SHORT).show();
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
            uploadFile();
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
