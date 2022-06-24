package com.lbs.patpat.model;

public class UserModel {
    private String uid;
    private String nickname;
    private String avatar;
    private int fans_num;
    private boolean followed;

    public UserModel(String uid, String nickname, String avatar, int fans_num, boolean followed) {
        this.uid = uid;
        this.nickname = nickname;
        this.avatar = avatar;
        this.fans_num = fans_num;
        this.followed = followed;
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

    public int getFans_num() {
        return fans_num;
    }

    public void setFans_num(int fans_num) {
        this.fans_num = fans_num;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }
}
