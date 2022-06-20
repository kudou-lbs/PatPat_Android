package com.lbs.patpat.ui.dynamic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DynamicViewModel extends ViewModel {

    private MutableLiveData<String[]> tabItems;

    public DynamicViewModel() {
        tabItems = new MutableLiveData<>();
        tabItems.setValue(new String[]{"关注","推荐","论坛"});
    }

    public LiveData<String[]> getTabItems() {
        return tabItems;
    }
}