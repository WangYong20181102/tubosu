package com.tobosu.mydecorate.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2017/6/3 15:47.
 */

public class _ArticleDetail {
    private String aid;//文章id
    private String type_id;//文章分类id
    private String title;//文章标题
    private String tup_count;//点赞量
    private String image_url;//文章封面图
    private String info;//文章基本描述
    private String time;//距离当前时间的发布时间
    private JSONArray content;//文章内容
    private String author_id;//作者id
    private String author_name;//作者名字
    private String author_img;//作者头像
    private String is_follow;//是否已经关注该作者
    private String is_collect;//是否收藏该文章
    private String is_tup;//是否点赞该文章  1--点赞  0--未点赞
    private String share_url;//分享地址

    public _ArticleDetail(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            this.aid = jsonObject.getString("aid");
            this.type_id = jsonObject.getString("type_id");
            this.title = jsonObject.getString("title");
            this.tup_count = jsonObject.getString("tup_count");
            this.image_url = jsonObject.getString("image_url");
            this.info = jsonObject.getString("info");
            this.time = jsonObject.getString("time");
            this.content = jsonObject.getJSONArray("content");
            this.author_id = jsonObject.getString("author_id");
            this.author_name = jsonObject.getString("author_name");
            this.author_img = jsonObject.getString("author_img");
            this.is_follow = jsonObject.getString("is_follow");
            this.is_collect = jsonObject.getString("is_collect");
            this.is_tup = jsonObject.getString("is_tup");
            this.share_url = jsonObject.getString("share_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTup_count() {
        return tup_count;
    }

    public void setTup_count(String tup_count) {
        this.tup_count = tup_count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public JSONArray getContent() {
        return content;
    }

    public void setContent(JSONArray content) {
        this.content = content;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAuthor_img() {
        return author_img;
    }

    public void setAuthor_img(String author_img) {
        this.author_img = author_img;
    }

    public String getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(String is_follow) {
        this.is_follow = is_follow;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getIs_collect() {
        return is_collect;
    }

    public String getIs_tup() {
        return is_tup;
    }

    public void setIs_tup(String is_tup) {
        this.is_tup = is_tup;
    }

    public void setIs_collect(String is_collect) {
        this.is_collect = is_collect;
    }
}
