package com.lbs.patpat.adapter;

import android.webkit.JavascriptInterface;

//打开WebView时需要新建此类的对象并做好映射,第二个参数为映射后给JavaScript使用的对象名称
//Android：myWebView.addJavascriptInterface(new JSAdapter(),"jsa");
//JavaScript: jsa.getType()

public class JSAdapter {
    @JavascriptInterface
    public void goToUrl(String url){
        //打开官网
    }

    @JavascriptInterface
    public void goToGameList(String type){
        //从发现打开某个类型的游戏列表
    }

    @JavascriptInterface
    public void goToPost(int pid){
        //打开某个帖子的详细页
    }

    @JavascriptInterface
    public void getType(){
        //打开某一类型的游戏列表时需要提供GetType给JavaScript调用获取Type
        //return type
    }

    @JavascriptInterface
    public void getPostId(){
        //打开帖子时需要提供GetPostId给JavaScript调用获取pid
        //return pid
    }
}
