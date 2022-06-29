package com.lbs.patpat.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lbs.patpat.MainActivity;
import com.lbs.patpat.R;
import com.lbs.patpat.global.MyApplication;
import com.lbs.patpat.model.PersonalModel;

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

public class PersonalAboutViewModel extends ViewModel {
    private MutableLiveData<List<PersonalModel>> personalModelList;
    private String uid;

    public PersonalAboutViewModel(String uid) {
        personalModelList=new MutableLiveData<>();
        personalModelList.setValue(new ArrayList<>());
        this.uid=uid;
    }

    public MutableLiveData<List<PersonalModel>> getPersonalModelList() {
        return personalModelList;
    }
    public void makePersonalApiCall(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url= MyApplication.getContext().getString(R.string.server_ip)+"/user/"+uid;
                Request request=new Request.Builder()
                        .url(url)
                        .addHeader("token",MainActivity.getToken())
                        .build();
                new OkHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {}

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        try {
                            String s=response.body().string();
                            Log.d("reply",s);
                            JSONObject jsonObject=new JSONObject(s).getJSONObject("data");
                            List<PersonalModel> list=new ArrayList<>();
                            list.add(new PersonalModel("用户名",jsonObject.getString("username")));
                            list.add(new PersonalModel("注册时间",jsonObject.getString("registerTime")));
                            personalModelList.postValue(list);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }
}
