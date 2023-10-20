package com.example.attendanceapp.viewModel;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.attendanceapp.models.RequestPinResetResponse;

public class UserPinViewModel extends ViewModel {

    private UserPinRepository userPinRepository;

    public UserPinViewModel() {
        userPinRepository = new UserPinRepository();
    }

    public MutableLiveData<RequestPinResetResponse> getUserPinResponse() {
        return userPinRepository.getUserPinLiveData();
    }

    public MutableLiveData<String> getUserPinError() {
        return userPinRepository.getUserPinError();
    }

    public void requestPin(int pin) {
        userPinRepository.requestPin(pin);
    }

    public void clearData(){
        userPinRepository.clearDown();
    }
}
