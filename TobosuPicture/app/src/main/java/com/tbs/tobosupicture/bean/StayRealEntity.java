package com.tbs.tobosupicture.bean;

import java.util.ArrayList;

/**
 * 案例详情 之 入住场景
 * Created by Lie on 2017/8/1.
 */

public class StayRealEntity {

    private String id;                    //	undefined	入住场景ID号
    private ArrayList<String> img_url;    //	undefined	图片（数组格式）
    private String description;           //	undefined	入住场景描述
    private String img_count;             //


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getImg_url() {
        return img_url;
    }

    public void setImg_url(ArrayList<String> img_url) {
        this.img_url = img_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg_count() {
        return img_count;
    }

    public void setImg_count(String img_count) {
        this.img_count = img_count;
    }
}
