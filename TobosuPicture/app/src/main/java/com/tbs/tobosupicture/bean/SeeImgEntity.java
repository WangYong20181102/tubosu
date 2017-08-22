package com.tbs.tobosupicture.bean;

import java.util.ArrayList;

/**
 * Created by Lie on 2017/7/27.
 */

public class SeeImgEntity {
    private String suite_id;
    private String is_collect;
    private String share_title;
    private String share_image;
    private String share_desc;
    private String share_url;

    public String getShare_title() {
        return share_title;
    }

    public void setShare_title(String share_title) {
        this.share_title = share_title;
    }

    public String getShare_image() {
        return share_image;
    }

    public void setShare_image(String share_image) {
        this.share_image = share_image;
    }

    public String getShare_desc() {
        return share_desc;
    }

    public void setShare_desc(String share_desc) {
        this.share_desc = share_desc;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    private ArrayList<ImgEntity> ImgEntityList;

    public String getSuite_id() {
        return suite_id;
    }

    public void setSuite_id(String suite_id) {
        this.suite_id = suite_id;
    }

    public String getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(String is_collect) {
        this.is_collect = is_collect;
    }

    public ArrayList<ImgEntity> getImgEntityList() {
        return ImgEntityList;
    }

    public void setImgEntityList(ArrayList<ImgEntity> imgEntityList) {
        ImgEntityList = imgEntityList;
    }
}
