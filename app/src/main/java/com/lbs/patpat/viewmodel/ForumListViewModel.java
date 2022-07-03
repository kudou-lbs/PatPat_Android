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

public class ForumListViewModel extends ViewModel {
    MutableLiveData<List<ForumModel>> followForumList, allForumList;
    int pageSize=10;
    int followOffset=0;
    int allOffset=0;
    static final int TYPE_ALL=0;
    static final int TYPE_FOLLOW=1;

    public ForumListViewModel() {
        followForumList=new MutableLiveData<>();
        allForumList=new MutableLiveData<>();
        followForumList.setValue(new ArrayList<>());
        allForumList.setValue(new ArrayList<>());
    }

    public MutableLiveData<List<ForumModel>> getFollowForumList() {
        return followForumList;
    }

    public MutableLiveData<List<ForumModel>> getAllForumList() {
        return allForumList;
    }

    public void makeFollowApiCall(){
        makeForumListApiCall(TYPE_FOLLOW);
    }
    public void makeAllApiCall(){
        makeForumListApiCall(TYPE_ALL);
    }
    private void makeForumListApiCall(int type){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String tmpUrl= MyApplication.getContext().getString(R.string.server_ip)+"/forum";
                    switch (type){
                        case TYPE_FOLLOW:
                            tmpUrl=tmpUrl
                                    +"/like?uid="+ MainActivity.getUid()
                                    +"&offset="+followOffset
                                    +"&pageSize="+pageSize;
                            break;
                        case TYPE_ALL:
                            tmpUrl=tmpUrl
                                    +"?pageSize="+pageSize
                                    +"&offset="+allOffset;
                            break;
                        default:
                            break;
                    }
                    Log.d("lbs_dynamic_forum","url is "+tmpUrl);
                    Request.Builder builder=new Request.Builder().url(tmpUrl);
                    if(type==TYPE_FOLLOW){
                        builder.header("token",MainActivity.getToken());
                    }
                    Request request=builder.build();

                    Response response=new OkHttpClient().newCall(request).execute();
                    String responseData=response.body().string();
                    JSONObject jsonObject=new JSONObject(responseData);

                    //根据返回结果进行数据更新
                    List<ForumModel> tmpList=new ArrayList<>();
                    if(jsonObject.getString("message").equals("OK")){
                        switch (type){
                            case TYPE_ALL:
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
                                allForumList.postValue(tmpList);
                                allOffset+=pageSize;
                                break;
                            case TYPE_FOLLOW:
                                JSONArray jsonArray1=jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    JSONObject tmpObject=jsonArray1.getJSONObject(i);
                                    ForumModel tmpForumModel=new ForumModel(tmpObject.getString("name"),
                                            tmpObject.getString("fid"),
                                            tmpObject.getString("icon"),
                                            tmpObject.getString("lastTitle"));
                                    tmpList.add(tmpForumModel);
                                    Log.d("lbs",String.valueOf(i)+tmpObject.toString());
                                }
                                followForumList.postValue(tmpList);
                                followOffset+=pageSize;
                                break;
                            default:
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
