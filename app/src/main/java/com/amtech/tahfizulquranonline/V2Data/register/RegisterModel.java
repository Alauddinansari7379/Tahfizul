package com.amtech.tahfizulquranonline.V2Data.register;

import com.google.gson.annotations.SerializedName;

public class RegisterModel {
    @SerializedName("email")
    private String loginEmail;
    @SerializedName("password")
    private String loginPassword;
    @SerializedName("token")
    private String token;


}
