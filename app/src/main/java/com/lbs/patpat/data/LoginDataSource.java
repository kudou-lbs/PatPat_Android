package com.lbs.patpat.data;

import android.util.JsonReader;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.lbs.patpat.data.model.LoggedInUser;
import com.lbs.patpat.ui.login.LoginResult;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private static final String TAG = "TEST";
    private MutableLiveData<LoginResult> loginResultMutableLiveData;

    public LoginDataSource(MutableLiveData<LoginResult> loginResult){
        this.loginResultMutableLiveData = loginResult;
    }

    public void login(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username,password,loginResultMutableLiveData);
        new Thread(loginRequest).start();

//        while (loginRequest.getResponseCode()==-1);
//        //Log.d(TAG, responseMsg +String.valueOf(responseCode));
//        if (loginRequest.getResponseCode()==0) {
//            Log.d(TAG, "登陆成功,Username is " + username);
//            return new Result.Success<>(new LoggedInUser(username, "patpat"));
//        }
//        Log.d(TAG, "登陆失败");
//        return new Result.Error(loginRequest.getResponseMsg());


    }

    public void logout() {
        // TODO: revoke authentication
    }

    static class LoginRequest implements Runnable {

        static private String usrname;
        static private String passwd;
        private MutableLiveData<LoginResult> loginResult;

        LoginRequest(String name, String pd,MutableLiveData<LoginResult> loginResult) {
            this.usrname = name;
            this.passwd = pd;
            this.loginResult = loginResult;

        }

        @Override
        public void run() {
            String loginUrl = "http://172.21.140.162/user/login?username=" + this.usrname + "&password=" + this.passwd;
            //loginUrl="http://110.64.89.131:8199/login";
            //loginUrl="http://110.64.89.131:8199/wrongPasswd";
            //loginUrl="http://110.64.89.131:8199/noUser";
            Log.d(TAG, "url:"+loginUrl);
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(loginUrl)
                        .build();
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);
                Log.d(TAG, responseBody);
                int responseCode = jsonObject.getInt("code");
                String responseMsg = jsonObject.getString("message");
                Log.d(TAG, responseMsg +String.valueOf(responseCode));
                if (responseCode == 0) {
                    JSONObject userData = new JSONObject(jsonObject.getString("data"));
                    loginResult.postValue(new LoginResult(userData));
                }
                else{
                    loginResult.postValue((new LoginResult(responseMsg)));
                }


            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "登录不了就滚！");
            }
        }
    }
}