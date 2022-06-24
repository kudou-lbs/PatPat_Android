package com.lbs.patpat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebViewClient;

import com.lbs.patpat.databinding.ActivityWebViewBinding;

/**
 * 纯纯的webView，还需要考虑点击返回等
 * */
public class webViewActivity extends AppCompatActivity {

    private String url;
    private ActivityWebViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intent=getIntent();
        url=intent.getStringExtra("url");

    }

    @Override
    protected void onStart() {
        super.onStart();

        binding.wvGameOfficialWebsites.getSettings().setJavaScriptEnabled(true);
        binding.wvGameOfficialWebsites.setWebViewClient(new WebViewClient());
        binding.wvGameOfficialWebsites.loadUrl(url);
    }
}