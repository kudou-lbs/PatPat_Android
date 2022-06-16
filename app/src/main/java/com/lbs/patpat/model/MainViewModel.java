package com.lbs.patpat.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MainViewModel{

    private MutableLiveData<AppInfo> appInfoMutableLiveData;

    public MainViewModel() {
        appInfoMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<AppInfo> getAppInfoMutableLiveData() {
        return appInfoMutableLiveData;
    }
}
