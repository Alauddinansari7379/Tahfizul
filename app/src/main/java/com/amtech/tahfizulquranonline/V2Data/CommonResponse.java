package com.amtech.tahfizulquranonline.V2Data;

import com.google.gson.annotations.SerializedName;

public class CommonResponse {
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private Boolean status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
