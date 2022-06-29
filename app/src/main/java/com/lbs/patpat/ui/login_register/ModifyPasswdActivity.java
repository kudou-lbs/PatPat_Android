package com.lbs.patpat.ui.login_register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.lbs.patpat.MainActivity;
import com.lbs.patpat.R;
import com.lbs.patpat.databinding.ActivityModifyPasswdBinding;
import com.lbs.patpat.databinding.ActivityPersonalBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ModifyPasswdActivity extends AppCompatActivity {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private InputMethodManager keyBoard;
    private ActivityModifyPasswdBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModifyPasswdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        keyBoard = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
    }

    private  void initView(){
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
                if ((binding.confirmPassword.getText().toString().equals(binding.password.getText().toString()))
                        && (binding.confirmPassword.getText().toString().length() > 5))
                    binding.register.setEnabled(true);

            }
        };

        binding.confirmPassword.addTextChangedListener(afterTextChangedListener);
        binding.password.addTextChangedListener(afterTextChangedListener);

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (keyBoard != null)
                    keyBoard.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                binding.progressBar.setVisibility(View.VISIBLE);
                register(binding.confirmPassword.getText().toString(), binding.password.getText().toString());
            }
        });

        binding.goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.confirmPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (keyBoard != null)
                        keyBoard.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    register(binding.confirmPassword.getText().toString(), binding.password.getText().toString());
                }
                return false;
            }
        });


    }

    private void register(String toString, String toString1) {
        JSONObject json = new JSONObject();
        try {
            json.put("password",toString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(1500, TimeUnit.MILLISECONDS)
                        .build();
                RequestBody infoBody = RequestBody.create(String.valueOf(json), JSON);
                Request infoRequest = new Request.Builder()
                        .url(getString(R.string.server_ip) + "/user/" + MainActivity.getUid())
                        .addHeader("token", MainActivity.getToken())
                        .put(infoBody)
                        .build();

                try {
                    client.newCall(infoRequest).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        Toast.makeText(getApplicationContext(), "成功修改密码", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }
}
