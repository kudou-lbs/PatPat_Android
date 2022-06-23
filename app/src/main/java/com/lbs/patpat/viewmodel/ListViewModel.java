package com.lbs.patpat.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lbs.patpat.model.ForumModel;
import com.lbs.patpat.model.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用recyclerView展示搜索结果等，包括社区、用户
 * */
public class ListViewModel extends ViewModel {

    //社区信息列表
    private MutableLiveData<List<ForumModel>> forumsList;
    private MutableLiveData<List<UserModel>> userList;

    public ListViewModel() {
        forumsList=new MutableLiveData<>();
        forumsList.setValue(new ArrayList<>());
        userList=new MutableLiveData<>();
        userList.setValue(new ArrayList<>());
    }

    public MutableLiveData<List<ForumModel>> getForumsList() {
        return forumsList;
    }

    public MutableLiveData<List<UserModel>> getUserList() {
        return userList;
    }

    //调用API服务更新展示列表信息
    public void makeForumApiCall(){
        //使用okHttp请求信息，具体逻辑在network相关类里写好再调用
        //test
        for(int i=0;i<10;++i){
            forumsList.getValue().add(new ForumModel("原神","123","sad",12,13));
        }
    }
    public void makeUserApiCall(){
        //同上请求
        for(int i=0;i<10;++i){
            userList.getValue().add(new UserModel("123","原神-西风驿站","192.168.0.1/avatar",9999,true));
        }
    }
}