package com.lbs.patpat.viewmodel;

import static com.lbs.patpat.global.MyApplication.uid;
import static com.lbs.patpat.global.MyApplication.urlPrefix;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lbs.patpat.model.ForumModel;
import com.lbs.patpat.model.UserModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 使用recyclerView展示搜索结果等，包括社区、用户
 * */
public class ListViewModel extends ViewModel {

    //社区信息列表
    private  MutableLiveData<List<ForumModel>> forumsList;
    private MutableLiveData<List<UserModel>> userList;
    private OkHttpClient client;

    public ListViewModel() {
        forumsList=new MutableLiveData<>();
        forumsList.setValue(new ArrayList<>());
        userList=new MutableLiveData<>();
        userList.setValue(new ArrayList<>());

        client=new OkHttpClient();
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
        if(forumsList!=null)forumsList.getValue().clear();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String tmpUrl=urlPrefix+"forum?uid="+String.valueOf(uid)
                            +"&pageSize=10"
                            +"&offset=0";
                    Request request=new Request.Builder()
                            .url(tmpUrl)
                            .build();
                    Response response=client.newCall(request).execute();
                    String responseData=response.body().string();
                    JSONObject jsonObject=new JSONObject(responseData);
                    List<ForumModel> tmpList=new ArrayList<>();
                    if(jsonObject.getString("message").equals("OK")){
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject tmpObject=jsonArray.getJSONObject(i);
                            tmpObject.getString("icon");
                            String icon;
                            if(tmpObject.getString("icon").equals("null")){
                                icon="https://upload.wikimedia.org/wikipedia/zh/9/94/Genshin_Impact.jpg";
                            }else{
                                icon=urlPrefix+tmpObject.getString("icon");
                            }

                            ForumModel tmpForumModel=new ForumModel(tmpObject.getString("name"),
                                    tmpObject.getString("fid"),
                                    icon,
                                    tmpObject.getInt("postNum"),
                                    tmpObject.getInt("followNum"));
                            tmpList.add(tmpForumModel);
                            Log.d("lbs",String.valueOf(i)+tmpObject.toString());
                        }
                        forumsList.postValue(tmpList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void makeUserApiCall(){
        //同上请求
        if(userList!=null)userList.getValue().clear();
        for(int i=0;i<10;++i){
            userList.getValue().add(new UserModel("123","原神-西风驿站","192.168.0.1/avatar",9999,true));
        }
    }
}