package com.tbs.tobosupicture.bean;

import java.util.ArrayList;

/**
 * 案例详情 之 在线图库
 * Created by Lie on 2017/8/1.
 */

public class OnlineDiagram {

    private String id;                    //  undefined	在线工地ID号
    private String stage;                 //  undefined	阶段：1水电；2泥木；3油漆；4竣工
    private String description;           //  undefined	工地阶段描述
    private ArrayList<String> img_url;    //  undefined	图片（数组格式）
    private String img_count;             //

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getImg_url() {
        return img_url;
    }

    public void setImg_url(ArrayList<String> img_url) {
        this.img_url = img_url;
    }

    public String getImg_count() {
        return img_count;
    }

    public void setImg_count(String img_count) {
        this.img_count = img_count;
    }
}
