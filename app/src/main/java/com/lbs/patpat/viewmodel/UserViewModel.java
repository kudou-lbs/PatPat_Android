package com.lbs.patpat.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lbs.patpat.ui.login_register.LoginedUser;
import com.lbs.patpat.ui.login_register.UserDatabase;

import java.util.List;

public class UserViewModel extends AndroidViewModel{

    private UserDatabase userDatabase;
    private LiveData<List<LoginedUser>> loginedUser;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userDatabase = UserDatabase.getInstance(application);
        loginedUser = userDatabase.userDao().getUser();
    }

    public LiveData<List<LoginedUser>> getLoginedUser() {
        return loginedUser;
    }

}
