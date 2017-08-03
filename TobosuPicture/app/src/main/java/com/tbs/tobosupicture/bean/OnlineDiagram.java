package com.tbs.tobosupicture.bean;

/**
 * 案例详情 之 在线图库
 * Created by Lie on 2017/8/1.
 */

public class OnlineDiagram {

    private String id; //	         undefined	在线工地ID号
    private String stageid; //	     undefined	阶段：1水电；2泥木；3油漆；4竣工
    private String descriptionid; // undefined	工地阶段描述
    private String img_urlid; //	 undefined	图片（数组格式）
    private String img_countid; //



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStageid() {
        return stageid;
    }

    public void setStageid(String stageid) {
        this.stageid = stageid;
    }

    public String getDescriptionid() {
        return descriptionid;
    }

    public void setDescriptionid(String descriptionid) {
        this.descriptionid = descriptionid;
    }

    public String getImg_urlid() {
        return img_urlid;
    }

    public void setImg_urlid(String img_urlid) {
        this.img_urlid = img_urlid;
    }

    public String getImg_countid() {
        return img_countid;
    }

    public void setImg_countid(String img_countid) {
        this.img_countid = img_countid;
    }
}
