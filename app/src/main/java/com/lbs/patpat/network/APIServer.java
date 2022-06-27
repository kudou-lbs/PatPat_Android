package com.lbs.patpat.network;

import android.os.Handler;
import android.os.Message;

import com.lbs.patpat.model.ForumModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class APIServer {

    public static final int MESSAGE_FORUM_LIST =1;

    static OkHttpClient okHttpClient=new OkHttpClient();
    //测试用token
    static String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJuaWNrbmFtZVwiOlwi55So5oi3YTZiOTQ0XCIsXCJpbnRyb1wiOlwi562-5ZCN5piv5LiA56eN5oCB5bqm77yM5oiR5oOz5oiR5Y-v5Lul5pu06YW3XCIsXCJnZW5kZXJcIjowLFwicmVnaXN0ZXJUaW1lXCI6XCIyMDIyLTA2LTI2IDE3OjMxOjU4XCIsXCJmYW5zTnVtXCI6MCxcImZvbGxvd051bVwiOjAsXCJhdmF0YXJcIjpudWxsLFwiYmFja2dyb3VuZFwiOm51bGwsXCJ1c2VybmFtZVwiOlwia3Vkb3VcIixcInBhc3N3b3JkXCI6XCIkMmEkMTIkLnlUdDFpTWlEY213cE9KMHEyM0lhLndObUJRVFV2UlBMSENJbDdRRGFZZGguaXhwWXNvLlNcIixcInVpZFwiOjExNDk5MDF9IiwidWlkIjoxMTQ5OTAxLCJleHAiOjE2NTYyNjQ4MjYsInVzZXJuYW1lIjoia3Vkb3UifQ.5CTQtcB1X5kBihGfAQ4HfZsuOQrtYS9A19GD2RB050Q";
    static String uid="1149901";
    static String urlPrefix="http://172.21.140.162/";

    static List<ForumModel> forumModelList;

    public static List<ForumModel> getForumModelList() {
        return forumModelList;
    }

    public static void getForumList(Handler handler, int pageSize, int offset){
        if (forumModelList==null){
            forumModelList=new ArrayList<>();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String tmpUrl=urlPrefix+"forum?uid="+String.valueOf(uid)
                            +"&pageSize="+String.valueOf(pageSize)
                            +"&offset="+String.valueOf(offset);
                    Request request=new Request.Builder()
                            .url(tmpUrl)
                            .build();
                    Response response=okHttpClient.newCall(request).execute();
                    String responseData=response.body().string();
                    JSONObject jsonObject=new JSONObject(responseData);
                    if(jsonObject.getString("message").equals("OK")){
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject tmpObject=jsonArray.getJSONObject(i);
                            ForumModel tmpForumModel=new ForumModel(tmpObject.getString("name"),
                                    tmpObject.getString("fid"),
                                    "https://upload.wikimedia.org/wikipedia/zh/9/94/Genshin_Impact.jpg",
                                    tmpObject.getInt("postNum"),
                                    tmpObject.getInt("isLike"));
                            forumModelList.add(tmpForumModel);
                        }
                    }
                    Message message=new Message();
                    message.what=MESSAGE_FORUM_LIST;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
