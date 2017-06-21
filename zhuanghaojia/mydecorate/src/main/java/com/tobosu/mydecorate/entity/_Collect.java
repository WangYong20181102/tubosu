package com.tobosu.mydecorate.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mr.Lin on 2017/6/2 10:58.
 */

public class _Collect {
    String id;//收藏Id
    String aid;//文章id
    String author_id;//作者id
    String nick;//作者昵称
    String icon;//作者头像
    String title;//文章标题
    String image_url;//文章封面
    String time;//时间

    public _Collect(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            this.id = jsonObject.getString("id");
            this.aid = jsonObject.getString("aid");
            this.author_id = jsonObject.getString("author_id");
            this.nick = jsonObject.getString("nick");
            this.icon = jsonObject.getString("icon");
            this.title = jsonObject.getString("title");
            this.image_url = jsonObject.getString("image_url");
            this.time = jsonObject.getString("time");
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

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
