package com.lbs.patpat.viewmodel;

import static com.lbs.patpat.global.MyApplication.urlPrefix;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lbs.patpat.model.ForumDetailModel;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForumViewModel extends ViewModel {

    String fid;
    MutableLiveData<ForumDetailModel> forumDetailModelMutableLiveData;

    public ForumViewModel(String fid) {
        this.fid = fid;
        forumDetailModelMutableLiveData=new MutableLiveData<>();
        makeForumDetailApiCall();
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

                    /*if(forumDetailModelMutableLiveData.getValue()==null){
                        //copy out
                    }else {
                        ForumDetailModel detailModel=forumDetailModelMutableLiveData.getValue();
                        detailModel.setName(data.getString("name"));
                        detailModel.setIntro(data.getString("intro"));
                        detailModel.setIcon(data.getString("icon"));
                        detailModel.setFollowNum(data.getString("followNum"));
                        detailModel.setPostNum(data.getString("postNum"));
                        detailModel.setFid(data.getString("fid"));
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
