package com.example.attendanceapp.service;

import com.example.attendanceapp.models.RequestPinResetResponse;
import com.example.attendanceapp.models.ResetPinAndRegisterCallBackRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestCallBackService {

    @POST("attendance")
    Call<RequestPinResetResponse> verify(@Body ResetPinAndRegisterCallBackRequest body);
}
