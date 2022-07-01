package com.lbs.patpat.global;

import android.app.Application;
import android.content.Context;
import com.lbs.patpat.ui.login_register.UserDatabase;


public class MyApplication extends Application {

    public static final String EXTRA_MESSAGE = "INTENT_EXTRA";
    private static Context mContext;

    private static UserDatabase userDatabase;

    public static Context getInstance() {
        return mContext;
    }


    @Override
    public void onCreate( ) {
        super.onCreate();
        mContext = getApplicationContext();
        userDatabase =  UserDatabase.getInstance(this);

    }

    public static Context getContext() {
        return mContext;
    }

    public static UserDatabase getUserDatabase() {
        return userDatabase;
    }

}
