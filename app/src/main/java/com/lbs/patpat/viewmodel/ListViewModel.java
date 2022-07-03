package com.lbs.patpat.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lbs.patpat.MainActivity;
import com.lbs.patpat.R;
import com.lbs.patpat.global.MyApplication;
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
    private int forumOffset, forumPageSize;
    private int userOffset, userPageSize;
    private  MutableLiveData<List<ForumModel>> forumsList;
    private MutableLiveData<List<UserModel>> userList;
    private OkHttpClient client;

    public static final String TYPE_FORUM="forum";
    public static final String TYPE_USER="user";

    public ListViewModel() {
        forumOffset =0;
        forumPageSize =10;
        userOffset=0;
        userPageSize=10;

        forumsList=new MutableLiveData<>();
        forumsList.setValue(new ArrayList<>());
        userList=new MutableLiveData<>();
        userList.setValue(new ArrayList<>());

        client=new OkHttpClient();
    }

    public void backToForumStart(){
        forumOffset =0;
    }

    public void backToUserStart(){
        userOffset=0;
    }

    public MutableLiveData<List<ForumModel>> getForumsList() {
        return forumsList;
    }

    public MutableLiveData<List<UserModel>> getUserList() {
        return userList;
    }

    //更新搜索论坛
    public void makeForumApiCall(String key){
        makeSearchApiCall(TYPE_FORUM,key);
    }
    //更新搜索用户
    public void makeUserApiCall(String key){
        makeSearchApiCall(TYPE_USER,key);
    }
    private void makeSearchApiCall(String type,String key){
        //这个清除似乎没有意义
        //if(forumsList!=null)forumsList.getValue().clear();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String tmpUrl= MyApplication.getContext().getString(R.string.server_ip)+"/"
                            +"search?type="+type
                            +"&key="+key
                            +"&pageSize="+ forumPageSize
                            +"&offset="+ forumOffset;
                    Log.d("lbs_search_forum","url is "+tmpUrl);
                    Request request=new Request.Builder()
                            .url(tmpUrl)
                            .build();
                    Response response=client.newCall(request).execute();
                    String responseData=response.body().string();
                    JSONObject jsonObject=new JSONObject(responseData);

                    //根据返回结果进行数据更新
                    switch (type){
                        case TYPE_FORUM:
                            List<ForumModel> tmpList=new ArrayList<>();
                            if(jsonObject.getString("message").equals("OK")){
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject tmpObject=jsonArray.getJSONObject(i);
                                    ForumModel tmpForumModel=new ForumModel(tmpObject.getString("name"),
                                            tmpObject.getString("fid"),
                                            tmpObject.getString("icon"),
                                            tmpObject.getInt("postNum"),
                                            tmpObject.getInt("followNum"));
                                    tmpList.add(tmpForumModel);
                                    Log.d("lbs",String.valueOf(i)+tmpObject.toString());
                                }
                                forumsList.postValue(tmpList);
                                forumOffset += forumPageSize;
                            }
                            break;
                        case TYPE_USER:
                            List<UserModel> tmpUserList=new ArrayList<>();
                            if(jsonObject.getString("message").equals("OK")){
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject tmpObject=jsonArray.getJSONObject(i);
                                    UserModel tmpUserModel=new UserModel(
                                            tmpObject.getString("uid"),
                                            tmpObject.getString("nickname"),
                                            tmpObject.getString("avatar"),
                                            tmpObject.getInt("fansNum"),
                                            tmpObject.getString("intro")
                                    );
                                    tmpUserList.add(tmpUserModel);
                                    Log.d("lbs",String.valueOf(i)+tmpObject.toString());
                                }
                                userList.postValue(tmpUserList);
                                userOffset += userPageSize;
                            }
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //获取关注论坛列表，这里无意义
    public void makeFollowForumApiCall(){
        if(forumsList!=null)forumsList.getValue().clear();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String tmpUrl= MyApplication.getContext().getString(R.string.server_ip)+"/"
                            +"forum/like?uid="+ MainActivity.getUid()
                            +"&pageSize="+ forumPageSize
                            +"&offset=0"+ forumOffset;
                    //Log.d("用户关注论坛",String.valueOf(forumOffset)+String.valueOf(forumPageSize));
                    Request request=new Request.Builder()
                            .url(tmpUrl)
                            .header("token",MainActivity.getToken())
                            .build();
                    Response response=client.newCall(request).execute();
                    String responseData=response.body().string();
                    JSONObject jsonObject=new JSONObject(responseData);
                    List<ForumModel> tmpList=new ArrayList<>();
                    if(jsonObject.getString("message").equals("OK")){
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        Log.d("获取成功",String.valueOf(jsonArray.length()));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject tmpObject=jsonArray.getJSONObject(i);
                            /*String icon;
                            if(tmpObject.getString("icon").equals("null")){
                                icon="https://upload.wikimedia.org/wikipedia/zh/9/94/Genshin_Impact.jpg";
                            }else{
                                icon=MyApplication.getContext().getString(R.string.server_ip)+"/"
                                        +tmpObject.getString("icon");
                            }*/

                            ForumModel tmpForumModel=new ForumModel(tmpObject.getString("name"),
                                    tmpObject.getString("fid"),
                                    tmpObject.getString("icon"),
                                    0,
                                    0);
                            tmpList.add(tmpForumModel);
                            Log.d("lbs",String.valueOf(i)+tmpObject.toString());
                        }
                        forumsList.postValue(tmpList);
                        forumOffset += forumPageSize;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}