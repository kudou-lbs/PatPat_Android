package com.lbs.patpat;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Parcel;

import androidx.annotation.Nullable;

import com.previewlibrary.enitity.IThumbViewInfo;


public class UserViewInfo implements IThumbViewInfo {
    private String url;  //图片地址
    private Rect mBounds; // 记录坐标
    private String user;//
    private String videoUrl;//视频链接 //不为空是视频

    public UserViewInfo(String url) {
        this.url = url;
    }

    protected UserViewInfo(Parcel source) {
        this.url = source.readString();
    }

    @Override
    public String getUrl() {//将你的图片地址字段返回
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public Rect getBounds() {//将你的图片显示坐标字段返回
        return mBounds;
    }

    @Nullable
    @Override
    public String getVideoUrl() {
        return null;
    }

    public void setBounds(Rect bounds) {
        mBounds = bounds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.url);
    }
    public static Creator<UserViewInfo> CREATOR = new Creator<UserViewInfo>() {
        @Override
        public UserViewInfo createFromParcel(Parcel source) {
            return new UserViewInfo(source);
        }

        @Override
        public UserViewInfo[] newArray(int size) {
            return new UserViewInfo[size];
        }
    };
}
