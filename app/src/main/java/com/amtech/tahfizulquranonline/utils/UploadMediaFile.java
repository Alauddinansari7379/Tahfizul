package com.amtech.tahfizulquranonline.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.amtech.tahfizulquranonline.models.ServerResponse;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Shourav Paul on 08-01-2022.
 **/
public class UploadMediaFile extends AsyncTask<String, Void, String> {

    private ProgressDialog progressDialog;
    private Context mContext;
    private String mediaPath;
    private static final String TAG = "UploadMediaFile";
    private OnUploadListener mListener;

    public UploadMediaFile(Context mContext, String mediaPath, OnUploadListener mListener) {
        this.mContext = mContext;
        this.mediaPath = mediaPath;
        this.mListener = mListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        uploadFile();
        return null;
    }

//    @Override
//    protected void onProgressUpdate(Void... values) {
//        super.onProgressUpdate(values);
//    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
    private void uploadFile() {

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
                //Log.i(TAG, "onResponse: "+response.body().toString());
                if (serverResponse != null) {
                    if (serverResponse.getStatus()) {
                        Toast.makeText(mContext, "Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                        AppConstants.mediaFileLink = "uploads/"+serverResponse.getFilePath();
                        mListener.onUpload(true);
                        Log.i(TAG, "onResponse:"+serverResponse.getFilePath());
                    } else {
                        Toast.makeText(mContext, serverResponse.getFilePath(),Toast.LENGTH_SHORT).show();
                        mListener.onUpload(false);
                    }
                } else {
                    mListener.onUpload(false);
//                    assert serverResponse != null;
//                    Log.v("Response", serverResponse.toString());
                }
                //progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                mListener.onUpload(false);
            }
        });
    }
    public interface OnUploadListener
    {
        void onUpload(boolean success);
    }
}
