package com.lbs.patpat.adapter;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import androidx.fragment.app.Fragment;

import com.lbs.patpat.ForumActivity;
import com.lbs.patpat.PersonalActivity;
import com.lbs.patpat.R;
import com.lbs.patpat.global.MyApplication;
import com.lbs.patpat.ui.login_register.UserDao;
import com.lbs.patpat.webViewActivity;

/**
 * 打开游戏详情，基础功能
 * */
public class JSBasic implements JSInterface{

    Activity context;
    Fragment fragment;

    public JSBasic(Fragment fragment) {
        this.fragment = fragment;
        context=fragment.getActivity();
    }

    public JSBasic(Activity context) {
        this.context = context;
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

    /**
     * 销毁当前活动
     * */
    @Override
    @JavascriptInterface
    public void finishCurrentActivity() {
        context.finish();
    }

    /**
     * 未登录则返回null
    * */
    @JavascriptInterface
    public String getToken(){
        UserDao userDao= MyApplication.getUserDatabase().userDao();
        if(userDao.getCount()<1)return null;

        return userDao.getToken()[0];
    }

    /**
     * 当uid为-1则未登录
     * */
    @JavascriptInterface
    public int getUid(){
        UserDao userDao= MyApplication.getUserDatabase().userDao();
        if(userDao.getCount()<1)return -1;

        return userDao.getUID()[0];
    }

    /**
     * 打开论坛
     * */
    @JavascriptInterface
    public void startForumActivity(String fid){
        Intent intent=new Intent(context, ForumActivity.class);
        intent.putExtra("fid",fid);
        context.startActivity(intent);
    }

    /**
     * 打开用户主页
     * */
    @JavascriptInterface
    public void goToUser(String uid){
        Intent intent=new Intent(context, PersonalActivity.class);
        intent.putExtra("uid",uid);
        context.startActivity(intent);
    }

    /**
     * 根据pid打开帖子
     * */
    @JavascriptInterface
    public void goToPost(String pid){

    }

    /**
     * 打开评论详情
     * */
    @JavascriptInterface
    public void goToReply(String pid, String rid){

    }
}
