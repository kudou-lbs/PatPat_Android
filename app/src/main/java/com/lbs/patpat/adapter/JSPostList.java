package com.lbs.patpat.adapter;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import androidx.fragment.app.Fragment;

import com.lbs.patpat.R;
import com.lbs.patpat.webViewActivity;

public class JSPostList extends JSBasic{

    Activity context;
    Fragment fragment;

    public JSPostList(Fragment fragment) {
        super(fragment);
        this.fragment = fragment;
        context=fragment.getActivity();
    }

    /**
     * 打开帖子详情页面
    * */
    @JavascriptInterface
    public void goToPost(int pid) {
        Intent intent=new Intent(context, webViewActivity.class);
        intent.putExtra(context.getString(R.string.intent_post_pid),String.valueOf(pid));
        context.startActivity(intent);
    }
}
