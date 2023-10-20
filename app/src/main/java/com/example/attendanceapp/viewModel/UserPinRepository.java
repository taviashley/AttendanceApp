package com.example.attendanceapp.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.example.attendanceapp.configs.ApiGateway;
import com.example.attendanceapp.models.RequestPinResetResponse;
import com.example.attendanceapp.models.ResetPinAndRegisterCallBackRequest;
import com.example.attendanceapp.service.RequestCallBackService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class UserPinRepository {

    private RequestCallBackService requestCallBackService;
    MutableLiveData<RequestPinResetResponse> userPinResponseMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> apiErrorMutableLiveData = new MutableLiveData<>();

    public UserPinRepository() {
        requestCallBackService = ApiGateway.callMeBackCampaign();
    }

    public MutableLiveData<RequestPinResetResponse> getUserPinLiveData() {
        return userPinResponseMutableLiveData;
    }

    public MutableLiveData<String> getUserPinError() {
        return apiErrorMutableLiveData;
    }

    public void clearDown() {
        userPinResponseMutableLiveData = new MutableLiveData<>();
        apiErrorMutableLiveData = new MutableLiveData<>();
    }

    public void requestPin(int pin) {

        clearDown();
        userPinResponseMutableLiveData.postValue(new RequestPinResetResponse());

        ResetPinAndRegisterCallBackRequest resetPinAndRegisterCallBackRequest = new ResetPinAndRegisterCallBackRequest();

        resetPinAndRegisterCallBackRequest.setPin(pin);

        Call<RequestPinResetResponse> apiCall = requestCallBackService.verify(resetPinAndRegisterCallBackRequest);

        apiCall.enqueue(new Callback<RequestPinResetResponse>() {
            @Override
            public void onResponse(Call<RequestPinResetResponse> call, Response<RequestPinResetResponse> response) {
                if (response.isSuccessful()) {
                    userPinResponseMutableLiveData.postValue(response.body());
                } else {
                    Timber.i("error: %s", "fail");
                }
                Timber.i("received response from: %d", response.code());
            }

            @Override
            public void onFailure(Call<RequestPinResetResponse> call, Throwable t) {
                Timber.e("500 %s", t.getMessage());
                apiErrorMutableLiveData.postValue("error");
            }
        });
    }
}

//transform in kotlin