package com.lbs.patpat.model;
/**
 * 社区model
 * */
public class ForumModel {
    private String forumName;
    private String forumFid;
    private String forumIcon;
    private int forumPostNum;
    private int forumFollowNum;


    public ForumModel(String forumName, String forumFid, String forumIcon, int forumPostNum, int forumFollowNum){
        this.forumName=forumName;
        this.forumFid=forumFid;
        this.forumIcon=forumIcon;
        this.forumPostNum=forumPostNum;
        this.forumFollowNum=forumFollowNum;
    }

    //以下为getter & setter
    public String getForumName() {
        return forumName;
    }

    public void setForumName(String forumName) {
        this.forumName = forumName;
    }

    public String getForumFid() {
        return forumFid;
    }

    public void setForumFid(String forumFid) {
        this.forumFid = forumFid;
    }

    public String getForumIcon() {
        return forumIcon;
    }

    public void setForumIcon(String forumIcon) {
        this.forumIcon = forumIcon;
    }

    public int getForumPostNum() {
        return forumPostNum;
    }

    public void setForumPostNum(int forumPostNum) {
        this.forumPostNum = forumPostNum;
    }

    public int getForumFollowNum() {
        return forumFollowNum;
    }

    public void setForumFollowNum(int forumFollowNum) {
        this.forumFollowNum = forumFollowNum;
    }
}
