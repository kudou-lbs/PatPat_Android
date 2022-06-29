package com.lbs.patpat.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {
    private MutableLiveData<String[]> hotSearch;
    private MutableLiveData<String[]> resultClassify;

    public SearchViewModel(){
        hotSearch=new MutableLiveData<>();
        hotSearch.setValue(new String[]{"超级马里奥","我的世界","王者荣耀","原神","炉石传说","明日方舟"});
        resultClassify=new MutableLiveData<>();
        resultClassify.setValue(new String[]{"帖子","游戏","论坛","用户"});
    }

    public MutableLiveData<String[]> getHotSearch() {
        return hotSearch;
    }

    //调用api更新热搜榜
    public void makeRenewHotApiCall(){

    }

    public MutableLiveData<String[]> getResultClassify() {
        return resultClassify;
    }
}
