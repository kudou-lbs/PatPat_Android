package com.lbs.patpat.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lbs.patpat.model.ForumModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用recyclerView展示搜索结果等，包括社区、用户
 * */
public class ListViewModel extends ViewModel {

    //社区信息列表
    private MutableLiveData<List<ForumModel>> forumsList;

    public ListViewModel() {
        forumsList=new MutableLiveData<>();
        forumsList.setValue(new ArrayList<>());
    }

    public MutableLiveData<List<ForumModel>> getForumsList() {
        return forumsList;
    }

    //调用API服务更新展示列表信息
    public void makeApiCall(){
        //使用okHttp请求信息，具体逻辑在network相关类里写好再调用

        //test
        for(int i=0;i<10;++i){
            forumsList.getValue().add(new ForumModel("Patpat","123","sad",12,13));
        }
    }
}