package com.tobosu.mydecorate.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mr.Lin on 2017/6/7 09:22.
 */

public class _MyAttention {
    private String id;//关注Id号
    private String aid;//作者Id
    private String nick;//作者昵称
    private String article_count;//文章总数
    private String time;//文章时间
    private String title;//文章标题
    private String type_name;//文章类型
    private String icon;//作者的头像

    public _MyAttention(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            this.id = jsonObject.getString("id");
            this.aid = jsonObject.getString("aid");
            this.nick = jsonObject.getString("nick");
            this.article_count = jsonObject.getString("article_count");
            this.time = jsonObject.getString("time");
            this.title = jsonObject.getString("title");
            this.type_name = jsonObject.getString("type_name");
            this.icon = jsonObject.getString("icon");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getArticle_count() {
        return article_count;
    }

    public void setArticle_count(String article_count) {
        this.article_count = article_count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
