package com.lbs.patpat.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lbs.patpat.model.AppInfo;

public class MainViewModel{

    private MutableLiveData<AppInfo> appInfoMutableLiveData;

    public MainViewModel() {
        appInfoMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<AppInfo> getAppInfoMutableLiveData() {
        return appInfoMutableLiveData;
    }
}
