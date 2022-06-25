package com.lbs.patpat.adapter;

import android.content.Context;
import android.webkit.JavascriptInterface;

//打开WebView时需要新建此类的对象并做好映射,第二个参数为映射后给JavaScript使用的对象名称
//Android：myWebView.addJavascriptInterface(new JSInterface(),"jsa");
//JavaScript: jsa.getType()

public interface JSInterface {

    /**
     * 打开游戏官网
     * */
    @JavascriptInterface
    public void goToUrl( String url);

    /**
     * 关闭当前活动，一般情况不要使用
     * */
    @JavascriptInterface
    public void finishCurrentActivity();

}
