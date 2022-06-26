package com.lbs.patpat.global;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static Context mContext;

    public static String getTest() {
        return test;
    }

    private static String test = "123456";

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=getApplicationContext();
    }

    public static Context getInstance(){
        return mContext;
    }
}
