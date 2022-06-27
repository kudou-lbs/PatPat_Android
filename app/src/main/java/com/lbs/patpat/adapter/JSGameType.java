package com.lbs.patpat.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.webkit.JavascriptInterface;

import androidx.fragment.app.Fragment;

import com.lbs.patpat.R;
import com.lbs.patpat.ui.discovery.DiscoveryFragment;
import com.lbs.patpat.webViewActivity;

/**
 * 发现页专用，goToGameList和backToGameList用于控制toolbar的展示样式
 * */
public class JSGameType extends JSBasic {

    Activity context;
    Fragment fragment;

    public JSGameType(Fragment fragment) {
        super(fragment);
        this.fragment = fragment;
        context=fragment.getActivity();
    }

    @JavascriptInterface
    public void goToGameList(String type) {
        if(fragment instanceof DiscoveryFragment){
            Message message=new Message();
            message.what=0;
            ((DiscoveryFragment) fragment).setType(type);
            ((DiscoveryFragment) fragment).getHandler().sendMessage(message);
        }
    }

    @JavascriptInterface
    public void backToGameList() {
        if(fragment instanceof DiscoveryFragment){
            Message message=new Message();
            message.what=1;
            ((DiscoveryFragment) fragment).getHandler().sendMessage(message);
        }
    }
}
