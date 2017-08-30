package com.tbs.tobosupicture.bean;

import java.util.List;

/**
 * Created by Mr.Lin on 2017/8/7 09:50.
 */

public class _MyDynamic {

    /**
     * id : 5184
     * uid : 23109
     * is_virtual_user : 2
     * title : 不是我说你，你跟钟老板都是垃圾！
     * praise_count : 1
     * comment_count : 1
     * distance_time : 4天前
     * month : 8月
     * day : 03
     * type : 发起
     * participate_type : 3
     * image_detail : ["https://pic.tbscache.com/impress_pic/2016-10-02/p_57f0650958f5e.jpg","https://pic.tbscache.com/impress_pic/2016-08-23/p_57bbfac0f2a3a.jpg","https://pic.tbscache.com/impress_pic/2016-09-28/p_57eb6b1363b92.jpg","https://pic.tbscache.com/impress_pic/2016-09-15/p_57da3c1c855a3.jpg"]
     */

    private String id;
    private String uid;
    private String is_virtual_user;
    private String title;
    private String praise_count;
    private String comment_count;
    private String distance_time;
    private String month;
    private String day;
    private String type;
    private String is_praise;//是否点赞
    private String is_comment;//是否回复
    private List<String> image_detail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIs_virtual_user() {
        return is_virtual_user;
    }

    public void setIs_virtual_user(String is_virtual_user) {
        this.is_virtual_user = is_virtual_user;
    }

    public String getTitle() {
        return title;
    }
    public String getIs_praise() {
        return is_praise;
    }

    public void setIs_praise(String is_praise) {
        this.is_praise = is_praise;
    }

    public String getIs_comment() {
        return is_comment;
    }

    public void setIs_comment(String is_comment) {
        this.is_comment = is_comment;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPraise_count() {
        return praise_count;
    }

    public void setPraise_count(String praise_count) {
        this.praise_count = praise_count;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getDistance_time() {
        return distance_time;
    }

    public void setDistance_time(String distance_time) {
        this.distance_time = distance_time;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getImage_detail() {
        return image_detail;
    }

    public void setImage_detail(List<String> image_detail) {
        this.image_detail = image_detail;
    }
}
