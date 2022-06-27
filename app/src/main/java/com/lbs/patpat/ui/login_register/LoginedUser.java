package com.lbs.patpat.ui.login_register;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.RoomDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

@Entity
public class LoginedUser {

    public String username;
    public String nickname;
    public String intro;
    public int gender;
    public String avatar;
    public String background;
    public int fansNum;
    public int followNum;
    public String token;

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    @PrimaryKey
    @NonNull
    private String uid;


    public LoginedUser(String uid, String username, String nickname, String intro,int gender,
                       String avatar, String background, int fansNum, int followNum, String token) {
        this.uid = uid;
        this.username = username;
        this.nickname = nickname;
        this.intro = intro;
        this.gender = gender;
        this.avatar = avatar;
        this.background = background;
        this.fansNum = fansNum;
        this.followNum = followNum;
        this.token = token;
    }

    @Ignore
    public LoginedUser(JSONObject userData) {
        try {
            token = userData.getString("token");
            JSONObject userInfo = new JSONObject(userData.getString("user"));
            this.uid = userInfo.getString("uid");
            this.username = userInfo.getString("username");
            this.nickname = userInfo.getString("nickname");
            this.gender = Integer.parseInt(userInfo.getString("gender"));
            this.avatar = userInfo.getString("avatar");
            this.background = userInfo.getString("background");
            this.fansNum = Integer.parseInt(userInfo.getString("fansNum"));
            this.followNum = Integer.parseInt(userInfo.getString("followNum"));
            this.intro = userInfo.getString("intro");

        } catch (JSONException e) {
            Log.d("TEST", "LoginedUser: JSON error");
            e.printStackTrace();
        }
    }
}

