package com.lbs.patpat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lbs.patpat.adapter.JSBasic;
import com.lbs.patpat.global.MyActivity;

/**
 * 纯纯的webView，还需要考虑点击返回等
 * */
public class webViewActivity extends MyActivity {

    private WebView webView;
    private String url; //打开游戏详情
    private String pid; //打开指定帖子

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView=findViewById(R.id.wv_game_official_websites);
        Intent intent=getIntent();
        url=intent.getStringExtra("url");
        pid=intent.getStringExtra("pid");
        if(url==null){
            url=getString(R.string.url_prefix)
                    +getString(R.string.url_suffix)
                    +getString(R.string.url_post)
                    +"/"+pid;
        }

        initView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView(){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient());

        webView.addJavascriptInterface(new JSBasic(webViewActivity.this),"jsAdapter");
        webView.loadUrl(url);
    }
}