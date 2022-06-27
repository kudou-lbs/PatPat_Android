package com.lbs.patpat.global;

import android.app.Application;
import android.content.Context;

import com.lbs.patpat.ui.login_register.UserDatabase;

public class MyApplication extends Application {

    private static Context mContext;

    private static UserDatabase userDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=getApplicationContext();
        userDatabase = UserDatabase.getInstance(this);
    }

    public static Context getInstance(){
        return mContext;
    }

    public static UserDatabase getUserDatabase() {
        return userDatabase;
    }
}
