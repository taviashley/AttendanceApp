package com.example.attendanceapp.models;

import com.squareup.moshi.Json;

public class RequestPinResetResponse {

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Json(name = "success")
    private boolean success;
}
