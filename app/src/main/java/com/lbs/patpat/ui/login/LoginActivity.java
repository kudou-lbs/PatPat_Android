package com.lbs.patpat.ui.login;

import static com.lbs.patpat.R.string.saved_user_account_key;

import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lbs.patpat.R;
import com.lbs.patpat.databinding.ActivityLoginBinding;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "TEST";
    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    private EditText accountEditText;
    private EditText passwordEditText;
    private SharedPreferences sharedPref;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = LoginActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        accountEditText = binding.account;
        passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;
        final Button goBackButton = binding.goBack;
        final TextView toRegister = binding.toRegister;
        final CheckBox protocolAgreement = binding.checkBox;
        InputMethodManager keyBoard = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        protocolAgreement.setText(Html.fromHtml("勾选即代表同意<font color=#F1A6A6>《服务协议》</font>" +
                "和<font color=#F1A6A6>《隐私政策》</font>",0));

        accountEditText.setText(sharedPref.getString(getString(saved_user_account_key), ""));

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @SuppressLint("ResourceType")
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {

                    updateUiWithUser(loginResult.getSuccess());
                    setResult(Activity.RESULT_OK, new Intent());
                    finish();
                }


            }
        });

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
                loginViewModel.loginDataChanged(accountEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        accountEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (keyBoard != null)
                        keyBoard.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    if (!protocolAgreement.isChecked()) {
                        Toast.makeText(getApplicationContext(), "请先同意《服务协议》和《隐私政策》", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    loginViewModel.login(accountEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (keyBoard != null)
                    keyBoard.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                if (!protocolAgreement.isChecked()) {
                    Toast.makeText(getApplicationContext(), "请先同意《服务协议》和《隐私政策》", Toast.LENGTH_SHORT).show();
                    return;
                }

                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(accountEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);

            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(saved_user_account_key), accountEditText.getText().toString());
        editor.apply();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_SHORT).show();
    }


    private void showLoginFailed(String errorString) {
        Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();
    }


}