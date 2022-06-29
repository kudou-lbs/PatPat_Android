package com.lbs.patpat.viewmodel;

import static com.lbs.patpat.global.MyApplication.getContext;
import static com.lbs.patpat.global.MyApplication.urlPrefix;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lbs.patpat.global.MyApplication;
import com.lbs.patpat.model.ForumDetailModel;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForumViewModel extends ViewModel {

    String fid;
    MutableLiveData<Boolean> followed;
    MutableLiveData<ForumDetailModel> forumDetailModelMutableLiveData;

    public ForumViewModel(String fid) {
        this.fid = fid;
        followed=new MutableLiveData<>();
        forumDetailModelMutableLiveData=new MutableLiveData<>();
        makeForumDetailApiCall();
        makeForumFollowInfoApiCall();
    }

    public void onClickFollow(){
        followed.setValue(!followed.getValue());
        renewFollowInfo();
    }

    public MutableLiveData<Boolean> getFollowed() {
        return followed;
    }
    public MutableLiveData<ForumDetailModel> getForumDetailModelMutableLiveData() {
        return forumDetailModelMutableLiveData;
    }
    //获取当前社区详信息
    public void makeForumDetailApiCall(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String url=urlPrefix+"forum/"+fid;
                    Request request=new Request.Builder()
                            .url(url)
                            .build();
                    OkHttpClient client=new OkHttpClient();
                    Response response=client.newCall(request).execute();
                    String responseData=response.body().string();
                    JSONObject data=new JSONObject(responseData).getJSONObject("data");
                    ForumDetailModel tmpViewModel=new ForumDetailModel(
                            data.getString("name"),
                            data.getString("intro"),
                            data.getString("icon"),
                            data.getString("followNum"),
                            data.getString("postNum"),
                            data.getString("fid"));
                    forumDetailModelMutableLiveData.postValue(tmpViewModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 根据当前followed更新服务器关注信息
     * */
    public void renewFollowInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("点击关注","asdasd");
            }
        }).start();
    }

    /**
     * 获取当前服务端关注信息
     * */
    public void makeForumFollowInfoApiCall(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                followed.postValue(true);
            }
        }).start();
    }
}
