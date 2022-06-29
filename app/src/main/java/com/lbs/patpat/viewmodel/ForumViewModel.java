package com.lbs.patpat.viewmodel;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lbs.patpat.MainActivity;
import com.lbs.patpat.R;
import com.lbs.patpat.global.MyApplication;
import com.lbs.patpat.model.ForumDetailModel;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ForumViewModel extends ViewModel {

    String fid;
    MutableLiveData<Boolean> followed;
    MutableLiveData<ForumDetailModel> forumDetailModelMutableLiveData;

    public ForumViewModel(String fid) {
        this.fid = fid;
        followed=new MutableLiveData<>();
        followed.setValue(false);
        forumDetailModelMutableLiveData=new MutableLiveData<>();
        makeForumDetailApiCall();
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
                    String url=MyApplication.getContext().getString(R.string.server_ip)+"/forum/"+fid+"?"+"uid="+MainActivity.getUid();
                    Request request=new Request.Builder()
                            .url(url)
                            .header("token",MainActivity.getToken())
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
                            data.getString("fid"),
                            data.getBoolean("isLike"));
                    followed.postValue(tmpViewModel.isLike());
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
                String tmpUrl=MyApplication.getContext().getString(R.string.server_ip)+"/"
                        +"forum/like?uid="+ MainActivity.getUid()
                        +"&fid="+fid;
                if(Boolean.TRUE.equals(followed.getValue())){
                    Request request=new Request.Builder()
                            .url(tmpUrl)
                            .post(RequestBody.create( "",null))
                            .build();
                    try {
                        new OkHttpClient().newCall(request).execute();
                        forumDetailModelMutableLiveData.getValue().setLike(followed.getValue());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Request request=new Request.Builder()
                            .url(tmpUrl)
                            .delete()
                            .build();
                    try {
                        new OkHttpClient().newCall(request).execute();
                        forumDetailModelMutableLiveData.getValue().setLike(followed.getValue());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("关注信息修改为：",String.valueOf(followed.getValue()));
            }
        }).start();
    }

    /**
     * 获取当前服务端关注信息
     * */
}
