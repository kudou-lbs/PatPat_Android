package com.lbs.patpat.global;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.lbs.patpat.network.APIServer;

import com.google.gson.Gson;
import com.lbs.patpat.R;
import com.lbs.patpat.ui.login_register.UserDatabase;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyApplication extends Application {

    private static Context mContext;
    private APIServer apiServer;

    public static final String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJuaWNrbmFtZVwiOlwi55So5oi3YTZiOTQ0XCIsXCJpbnRyb1wiOlwi562-5ZCN5piv5LiA56eN5oCB5bqm77yM5oiR5oOz5oiR5Y-v5Lul5pu06YW3XCIsXCJnZW5kZXJcIjowLFwicmVnaXN0ZXJUaW1lXCI6XCIyMDIyLTA2LTI2IDE3OjMxOjU4XCIsXCJmYW5zTnVtXCI6MCxcImZvbGxvd051bVwiOjAsXCJhdmF0YXJcIjpudWxsLFwiYmFja2dyb3VuZFwiOm51bGwsXCJ1c2VybmFtZVwiOlwia3Vkb3VcIixcInBhc3N3b3JkXCI6XCIkMmEkMTIkLnlUdDFpTWlEY213cE9KMHEyM0lhLndObUJRVFV2UlBMSENJbDdRRGFZZGguaXhwWXNvLlNcIixcInVpZFwiOjExNDk5MDF9IiwidWlkIjoxMTQ5OTAxLCJleHAiOjE2NTYyNjQ4MjYsInVzZXJuYW1lIjoia3Vkb3UifQ.5CTQtcB1X5kBihGfAQ4HfZsuOQrtYS9A19GD2RB050Q";
    public static final String uid="1149901";
    public static final String urlPrefix="http://172.21.140.162/";

    private static UserDatabase userDatabase;

    public static Context getInstance() {
        return mContext;
    }


    @Override
    public void onCreate( ) {
        super.onCreate();
        mContext = getApplicationContext();
        userDatabase =  UserDatabase.getInstance(this);

        apiServer=new APIServer();
    }

    public static Context getContext() {
        return mContext;
    }

    public static UserDatabase getUserDatabase() {
        return userDatabase;
    }

    public APIServer getApiServer() {
        return apiServer;
    }
}
