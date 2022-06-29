package com.lbs.patpat.viewmodel;

import static com.lbs.patpat.global.MyApplication.urlPrefix;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lbs.patpat.FollowAndFansActivity;
import com.lbs.patpat.MainActivity;
import com.lbs.patpat.R;
import com.lbs.patpat.fragment.ListFragment;
import com.lbs.patpat.global.MyApplication;
import com.lbs.patpat.model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FollowAndFanViewModel extends ViewModel {
    private MutableLiveData<List<UserModel>> followUsers,fanUsers;
    private String uid;
    private Handler handler;
    private JSONArray responseData;

    int followOffset=0;
    int fanOffset=0;
    int pageSize=100;
    //lbsDebug这里还需要添加读取到末尾刷新

    public FollowAndFanViewModel() {
        followUsers=new MutableLiveData<>();
        followUsers.setValue(new ArrayList<>());
        fanUsers=new MutableLiveData<>();
        fanUsers.setValue(new ArrayList<>());

        uid= FollowAndFansActivity.uid;
        initHandler();
    }

    private void initHandler() {
        handler=new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                List<UserModel> tmpList=new ArrayList<>();
                try {
                    Log.d("length",String.valueOf(responseData.length()));
                    for (int i = 0; i < responseData.length(); ++i) {
                        JSONObject tmpObject = responseData.getJSONObject(i);
                        tmpList.add(new UserModel(
                                tmpObject.getString("uid"),
                                tmpObject.getString("nickname"),
                                tmpObject.getString("avatar"),
                                tmpObject.getInt("fansNum"),
                                tmpObject.getString("intro"))
                        );
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                switch (msg.what){
                    case ListFragment.PERSONAL_FOLLOW:
                        followUsers.setValue(tmpList);
                        followOffset+=pageSize;
                        break;
                    case ListFragment.PERSONAL_FAN:
                        fanUsers.setValue(tmpList);
                        fanOffset+=pageSize;
                        break;
                }
            }
        };
    }

    public MutableLiveData<List<UserModel>> getFollowUsers() {
        return followUsers;
    }

    public MutableLiveData<List<UserModel>> getFanUsers() {
        return fanUsers;
    }

    //调用API更新数据
    public void makeFollowUserCall(){
        getUserList(ListFragment.PERSONAL_FOLLOW);
    }
    public void makeFanUserCall(){
        getUserList(ListFragment.PERSONAL_FAN);
    }

    private void getUserList(int type){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String urlPrefix=MyApplication.getContext().getString(R.string.server_ip)+"/user/";
                String url;
                /*if(type==ListFragment.PERSONAL_FOLLOW) url="http://172.21.140.162/user/9/follow?offset=0&pageSize=2";
                else url="http://172.21.140.162/user/9/fan?offset=0&pageSize=2";*/
                if(type==ListFragment.PERSONAL_FOLLOW){
                    url=urlPrefix+uid+"/follow?offset="+followOffset+"&pageSize="+pageSize;
                }else{
                    url=urlPrefix+uid+"/fan?offset="+fanOffset+"&pageSize="+pageSize;
                }
                Request request=new Request.Builder()
                        .url(url)
                        .addHeader("token", MainActivity.getToken())
                        .build();
                new OkHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.d("请求关注列表失败：",e.toString());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        try {
                            JSONObject tmpResponse=new JSONObject(response.body().string());
                            Log.d("wtf",tmpResponse.toString());
                            responseData=tmpResponse.getJSONArray("data");
                            Message message=new Message();
                            message.what=type;
                            handler.sendMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }
}
