package com.lbs.patpat.model;

public class ForumDetailModel {

    String name;
    String intro;
    String icon;
    String followNum;
    String postNum;
    String fid;

    public ForumDetailModel(String name, String intro, String icon, String followNum, String postNum, String fid) {
        this.name = name;
        this.intro = intro;
        this.icon = icon;
        this.followNum = followNum;
        this.postNum = postNum;
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getFollowNum() {
        return followNum;
    }

    public void setFollowNum(String followNum) {
        this.followNum = followNum;
    }

    public String getPostNum() {
        return postNum;
    }

    public void setPostNum(String postNum) {
        this.postNum = postNum;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }
}
