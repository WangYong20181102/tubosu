package com.tbs.tobosupicture.bean;

/**
 * Created by Mr.Lin on 2017/8/14 18:03.
 */

public class _SendDynamic {
    private String mImageUri;//图片地址
    private String mTitle;//标题

    public _SendDynamic(){

    }

    public _SendDynamic(String mImageUri, String mTitle) {
        this.mImageUri = mImageUri;
        this.mTitle = mTitle;
    }

    public String getmImageUri() {
        return mImageUri;
    }

    public void setmImageUri(String mImageUri) {
        this.mImageUri = mImageUri;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
