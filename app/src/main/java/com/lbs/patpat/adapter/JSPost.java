package com.lbs.patpat.adapter;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import androidx.fragment.app.Fragment;

import com.lbs.patpat.R;
import com.lbs.patpat.webViewActivity;

public class JSPost implements JSPostAdapter{

    Activity context;
    Fragment fragment;

    public JSPost(Fragment fragment) {
        this.fragment = fragment;
        context=fragment.getActivity();
    }

    /**
     * 打开游戏详情
     * */
    @Override
    @JavascriptInterface
    public void goToUrl(String url) {
        Intent intent=new Intent(context, webViewActivity.class);
        intent.putExtra(context.getString(R.string.intent_url_name),url);
        context.startActivity(intent);
    }

    /**
     * 关闭当前活动
    * */
    @Override
    @JavascriptInterface
    public void finishCurrentActivity() {
        context.finish();
    }

    /**
     * 打开帖子详情页面
    * */
    @Override
    @JavascriptInterface
    public void goToPost(int pid) {
        Intent intent=new Intent(context, webViewActivity.class);
        intent.putExtra(context.getString(R.string.intent_post_pid),String.valueOf(pid));
        context.startActivity(intent);
    }
}
