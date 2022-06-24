package com.lbs.patpat.adapter;

import android.content.Context;
import android.webkit.JavascriptInterface;

public interface JSGameTypeAdapter extends JSAdapter {
    //传入游戏类型，将bar改为游戏类型
    @JavascriptInterface
    public void goToGameList(String type);

    //返回发现页
    @JavascriptInterface
    public void backToGameList();
}
