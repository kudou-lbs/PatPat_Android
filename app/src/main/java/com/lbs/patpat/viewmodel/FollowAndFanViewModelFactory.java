package com.lbs.patpat.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class FollowAndFanViewModelFactory implements ViewModelProvider.Factory {

    private String uid;

    public FollowAndFanViewModelFactory() {
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FollowAndFanViewModel();
    }
}
