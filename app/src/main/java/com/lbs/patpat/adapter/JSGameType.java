package com.lbs.patpat.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.webkit.JavascriptInterface;

import androidx.fragment.app.Fragment;

import com.lbs.patpat.R;
import com.lbs.patpat.ui.discovery.DiscoveryFragment;
import com.lbs.patpat.webViewActivity;

public class JSGameType implements JSGameTypeInterface {

    Activity context;
    Fragment fragment;

    public JSGameType(Fragment fragment) {
        this.fragment = fragment;
        context=fragment.getActivity();
    }

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

    @Override
    @JavascriptInterface
    public void goToGameList(String type) {
        if(fragment instanceof DiscoveryFragment){
            Message message=new Message();
            message.what=0;
            ((DiscoveryFragment) fragment).setType(type);
            ((DiscoveryFragment) fragment).getHandler().sendMessage(message);
        }
    }

    @Override
    @JavascriptInterface
    public void backToGameList() {
        if(fragment instanceof DiscoveryFragment){
            Message message=new Message();
            message.what=1;
            ((DiscoveryFragment) fragment).getHandler().sendMessage(message);
        }
    }
}
