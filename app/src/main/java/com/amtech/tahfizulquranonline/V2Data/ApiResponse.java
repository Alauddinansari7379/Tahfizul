package com.amtech.tahfizulquranonline.V2Data;

import java.lang.reflect.Array;

public class ApiResponse {
    String status;
    Array data;

    public ApiResponse(Array data) {
        this.data = data;
    }

    public Array getData() {
        return data;
    }

    public void setData(Array data) {
        this.data = data;
    }

    public ApiResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
