package com.lbs.patpat.model;

public class UserModel {
    private String uid;
    private String nickname;
    private String avatar;
    private int fansNum;
    private String intro;
    private boolean followed;

    public String getIntro() {
        return intro;
    }

    public UserModel(String uid, String nickname, String avatar, int fansNum, boolean followed) {
        this.uid = uid;
        this.nickname = nickname;
        this.avatar = avatar;
        this.fansNum = fansNum;
        this.followed = followed;
    }

    //针对列表返回的信息
    public UserModel(String uid, String nickname, String avatar, int fansNum, String intro) {
        this.uid = uid;
        this.nickname = nickname;
        this.avatar = avatar;
        this.fansNum = fansNum;
        this.intro=intro;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getFansNum() {
        return fansNum;
    }

    public void setFansNum(int fansNum) {
        this.fansNum = fansNum;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }
}
