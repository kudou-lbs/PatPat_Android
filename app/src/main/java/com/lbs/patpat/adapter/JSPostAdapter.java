package com.lbs.patpat.adapter;

import android.webkit.JavascriptInterface;

public interface JSPostAdapter {
    //打开某个帖子的详细页
    @JavascriptInterface
    public void goToPost(int pid);
}
