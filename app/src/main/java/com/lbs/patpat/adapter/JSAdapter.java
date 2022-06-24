package com.lbs.patpat.adapter;

import android.content.Context;
import android.webkit.JavascriptInterface;

//打开WebView时需要新建此类的对象并做好映射,第二个参数为映射后给JavaScript使用的对象名称
//Android：myWebView.addJavascriptInterface(new JSAdapter(),"jsa");
//JavaScript: jsa.getType()

public interface JSAdapter {

    //打开官网
    @JavascriptInterface
    public void goToUrl( String url);

    //打开某个帖子的详细页
    @JavascriptInterface
    public void goToPost( int pid);

}
