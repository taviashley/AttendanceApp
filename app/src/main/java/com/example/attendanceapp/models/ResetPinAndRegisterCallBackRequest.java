package com.example.attendanceapp.models;


import com.squareup.moshi.Json;

public class ResetPinAndRegisterCallBackRequest {

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    @Json(name = "pin")
    private int pin;


    public ResetPinAndRegisterCallBackRequest() {
    }

    public ResetPinAndRegisterCallBackRequest(int UDID) {
        this.pin = UDID;
    }
}
