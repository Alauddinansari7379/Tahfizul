package com.amtech.tahfizulquranonline.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shourav Paul on 01-01-2022.
 **/
public class ServerResponse {
    // variable name should be same as in the json response from php
    @SerializedName("status")
    boolean status;
    @SerializedName("file_path")
    String file_path;
    public String getFilePath() {
        return file_path;
    }
    public boolean getStatus() {
        return status;
    }
}