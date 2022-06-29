package com.lbs.patpat.ui.login_register;

import static com.lbs.patpat.R.string.saved_user_account_key;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lbs.patpat.R;
import com.lbs.patpat.databinding.ActivityRegisterBinding;
import com.lbs.patpat.global.MyActivity;
import com.lbs.patpat.global.MyApplication;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends MyActivity {
    private ActivityRegisterBinding binding;
    private EditText accountEditText, passwordEditText, confirmEditText;
    private Button registerButton, goBackButton;
    private SharedPreferences sharedPref;
    private CheckBox protocolAgreement;
    private InputMethodManager keyBoard;
    private  UserDao userDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userDao = MyApplication.getUserDatabase().userDao();
        sharedPref = RegisterActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        keyBoard = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        accountEditText = binding.account;
        passwordEditText = binding.password;
        confirmEditText = binding.confirmPassword;
        registerButton = binding.register;
        goBackButton = binding.goBack;
        protocolAgreement = binding.checkBox;

        initView();


    }

    private void initView() {

        protocolAgreement.setText(Html.fromHtml("勾选即代表同意<font color=#F1A6A6>《服务协议》" +
                "</font>和<font color=#F1A6A6>《隐私政策》</font>"));
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((confirmEditText.getText().toString().equals(passwordEditText.getText().toString()))
                        && (confirmEditText.getText().toString().length() > 5))
                    registerButton.setEnabled(true);

            }
        };

        confirmEditText.addTextChangedListener(afterTextChangedListener);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (keyBoard != null)
                    keyBoard.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                register(accountEditText.getText().toString(), passwordEditText.getText().toString(), confirmEditText.getText().toString());
            }
        });

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirmEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (keyBoard != null)
                        keyBoard.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    register(accountEditText.getText().toString(), passwordEditText.getText().toString(), confirmEditText.getText().toString());
                }
                return false;
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            Toast.makeText(goBackButton.getContext(), "成功登陆", Toast.LENGTH_SHORT).show();
    }

    private void register(String account, String passwd, String confirmPasswd) {
        if (!protocolAgreement.isChecked()) {
            Toast.makeText(getApplicationContext(), "请先同意《服务协议》和《隐私政策》", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isAccountValid()) {
            Toast.makeText(getApplicationContext(), R.string.account_form, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isPassswdValid()) {
            Toast.makeText(getApplicationContext(), R.string.passwd_form, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isPasswdConfirm()) {
            Toast.makeText(getApplicationContext(), R.string.passwd_confirm, Toast.LENGTH_SHORT).show();
            return;
        }
        binding.progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String registerUrl = "http://172.21.140.162/user/register";
                final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                try {
                    Looper.prepare();
                    Map<String, Object> bodyDta = new HashMap<>();
                    bodyDta.put("username",account);
                    bodyDta.put("password",passwd);
                    String json = new Gson().toJson(bodyDta);
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .post(body)
                            .url(registerUrl)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseBody = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseBody);
                    int responseCode = jsonObject.getInt("code");
                    String responseMsg = jsonObject.getString("message");
                    if (responseCode == 0){ //注册成功
                        try {
                            String loginUrl = "http://172.21.140.162/user/login?username=" + account + "&password=" + passwd;
                            request = new Request.Builder()
                                    .url(loginUrl)
                                    .build();
                            response = client.newCall(request).execute();
                            responseBody = response.body().string();
                            jsonObject = new JSONObject(responseBody);
                            int loginCode = jsonObject.getInt("code");
                            String loginMsg = jsonObject.getString("message");
                            if (loginCode == 0) {
                                userDao.deleteUser();
                                JSONObject data = new JSONObject(jsonObject.getString("data"));
                                userDao.insertUser(new LoginedUser(data));
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString(getString(saved_user_account_key), accountEditText.getText().toString());
                                editor.apply();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        setResult(Activity.RESULT_OK, new Intent());
                                        RegisterActivity.this.finish();
                                        Log.d(getString(R.string.log_tag), "finish");
                                    }
                                });
                                Toast.makeText(RegisterActivity.this, "注册成功并自动登录", Toast.LENGTH_SHORT).show();
                                Looper.loop();

                            }
                            else {
                                Toast.makeText(RegisterActivity.this, loginMsg, Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                    else{
                        Toast.makeText(RegisterActivity.this, responseMsg, Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }




                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(getString(R.string.log_tag), "注册不了就滚！");
                }
            }
        }).start();



    }

    private boolean isPasswdConfirm() {
        if (confirmEditText.getText().toString().equals(passwordEditText.getText().toString()))
            return true;
        else
            return false;
    }

    private boolean isPassswdValid() {
        String passwd = passwordEditText.getText().toString();
        Log.d(getString(R.string.log_tag), passwd);
        if (Pattern.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$",passwd)&&passwd.length()>5){

            return true;
        }


        else
            return false;
    }

    private boolean isAccountValid() {
        if (accountEditText.getText().toString().length() == 0)
            return false;
        else
            return true;
    }
}