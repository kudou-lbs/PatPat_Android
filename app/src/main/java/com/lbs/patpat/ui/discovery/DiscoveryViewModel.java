package com.lbs.patpat.ui.discovery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DiscoveryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DiscoveryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is discovery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}