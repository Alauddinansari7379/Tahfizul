package com.amtech.tahfizulquranonline.utils;

import com.amtech.tahfizulquranonline.models.ServerResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Shourav Paul on 01-01-2022.
 **/
public interface ApiConfig {
    @Multipart
    @POST("fileupload")
    Call<ServerResponse> uploadFile(@Part MultipartBody.Part file,
                                    @Part("file") RequestBody name);

    @Multipart
    @POST("retrofit_example/upload_multiple_files.php")
    Call<ServerResponse> uploadMulFile(@Part MultipartBody.Part file1,
                                       @Part MultipartBody.Part file2);
}
