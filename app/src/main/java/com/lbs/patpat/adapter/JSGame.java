package com.lbs.patpat.adapter;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import androidx.fragment.app.Fragment;

import com.lbs.patpat.R;
import com.lbs.patpat.webViewActivity;

/**
 * 打开游戏详情，基础功能
 * */
public class JSGame implements JSInterface{

    Activity context;
    Fragment fragment;

    public JSGame(Fragment fragment) {
        this.fragment = fragment;
        context=fragment.getActivity();
    }

    /**
     * 输入官网url，打开新活动——游戏详情
     * */
    @Override
    @JavascriptInterface
    public void goToUrl(String url) {
        Intent intent=new Intent(context, webViewActivity.class);
        intent.putExtra(context.getString(R.string.intent_url_name),url);
        context.startActivity(intent);
    }

    @Override
    @JavascriptInterface
    public void finishCurrentActivity() {
        context.finish();
    }
}
