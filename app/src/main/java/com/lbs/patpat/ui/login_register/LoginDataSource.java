package com.lbs.patpat.ui.login_register;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.lbs.patpat.R;
import com.lbs.patpat.global.MyApplication;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private static final String TAG = "TEST";
    private MutableLiveData<LoginResult> loginResultMutableLiveData;

    public LoginDataSource(MutableLiveData<LoginResult> loginResult) {
        this.loginResultMutableLiveData = loginResult;
    }

    public void login(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password, loginResultMutableLiveData);
        new Thread(loginRequest).start();

    }


    static class LoginRequest implements Runnable {

        static private String usrname;
        static private String passwd;
        private MutableLiveData<LoginResult> loginResult;

        LoginRequest(String name, String pd, MutableLiveData<LoginResult> loginResult) {
            this.usrname = name;
            this.passwd = pd;
            this.loginResult = loginResult;

        }

        @Override
        public void run() {
            String loginUrl = MyApplication.getContext().getString(R.string.server_ip)+
                    "/user/login?username=" + this.usrname + "&password=" + this.passwd;

            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(loginUrl)
                        .build();
                Response response = client.newCall(request).execute();


                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                int responseCode = jsonObject.getInt("code");
                String responseMsg = jsonObject.getString("message");
                if (responseCode == 0) {
                    JSONObject userData = new JSONObject(jsonObject.getString("data"));
                    loginResult.postValue(new LoginResult(userData));
                } else {
                    loginResult.postValue((new LoginResult(responseMsg)));
                }


            } catch (Exception e) {
                Log.d(TAG, "run: 响应失败");
                loginResult.postValue((new LoginResult("网络异常")));
                e.printStackTrace();
            }
        }
    }
}