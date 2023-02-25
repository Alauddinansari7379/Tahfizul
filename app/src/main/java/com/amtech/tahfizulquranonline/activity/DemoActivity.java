package com.amtech.tahfizulquranonline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.models.ServerResponse;
import com.amtech.tahfizulquranonline.utils.ApiConfig;
import com.amtech.tahfizulquranonline.utils.AppConfig;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DemoActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private AppCompatButton uploadBtn;
    private TextView audioPickerBtn;
    private String mediaPath;
    private static final String TAG = "DemoActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        initView();
        setListener();
    }
    private void initView()
    {
        audioPickerBtn = (TextView)findViewById(R.id.pick_audio);
        uploadBtn = (AppCompatButton)findViewById(R.id.upload_btn);
    }
    private void setListener()
    {
        audioPickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1);
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                audioPickerBtn.setText(mediaPath);
                // Set the Video Thumb in ImageView Previewing the Media
//                imgView.setImageBitmap(getThumbnailPathForLocalFile(DemoActivity.this, selectedVideo));
                cursor.close();

            } else {
                Toast.makeText(this, "You haven't picked Image/Video", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }
//    // Providing Thumbnail For Selected Image
//    public Bitmap getThumbnailPathForLocalFile(Activity context, Uri fileUri) {
//        long fileId = getFileId(context, fileUri);
//        return MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(),
//                fileId, MediaStore.Video.Thumbnails.MICRO_KIND, null);
//    }

//    // Getting Selected File ID
//    public long getFileId(Activity context, Uri fileUri) {
//        Cursor cursor = context.managedQuery(fileUri, mediaColumns, null, null, null);
//        if (cursor.moveToFirst()) {
//            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
//            return cursor.getInt(columnIndex);
//        }
//        return 0;
//    }

    private void uploadFile() {
        progressDialog = new ProgressDialog(this);
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
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                Log.i(TAG, "onResponse: "+response.body().toString());
                if (serverResponse != null) {
                    if (serverResponse.getStatus()) {
                        Toast.makeText(getApplicationContext(), serverResponse.getFilePath(),Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onResponse:"+serverResponse.getFilePath());
                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getFilePath(),Toast.LENGTH_SHORT).show();
                    }
                } else {
                    assert serverResponse != null;
                    Log.v("Response", serverResponse.toString());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Toast.makeText(DemoActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}