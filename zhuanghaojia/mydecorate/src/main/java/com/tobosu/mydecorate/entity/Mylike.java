package com.tobosu.mydecorate.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mr.Lin on 2017/6/11 15:42.
 */

public class Mylike {
    /**
     * "id": "383",
     * "aid": "273",
     * "author_id": "245970",
     * "nick": "石家庄生活家装饰",
     * "icon": "http://cdn01.tobosu.net/mt_company_logo/2016-11-18/582ea9b2186ac.jpg",
     * "title": "在这个美图秀秀猖狂的年代，我们用原汁原味的实景感动你",
     * "image_url": "http://cdn01.tobosu.net/mt_banner/2016-12-09/584a8fa5e5f37.jpg",
     * "tup_count": "2"
     */
    String id;//收藏ID
    String aid;//文章id
    String author_id;//作者id
    String nick;//昵称
    String icon;//头像
    String title;//标题
    String image_url;//封面图
    String tup_count;//点赞数

    public Mylike(String json) {
        try {
            JSONObject jsonObject=new JSONObject(json);
            this.id=jsonObject.getString("id");
            this.aid=jsonObject.getString("aid");
            this.author_id=jsonObject.getString("author_id");
            this.nick=jsonObject.getString("nick");
            this.title=jsonObject.getString("title");
            this.image_url=jsonObject.getString("image_url");
            this.tup_count=jsonObject.getString("tup_count");
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

    public String getTup_count() {
        return tup_count;
    }

    public void setTup_count(String tup_count) {
        this.tup_count = tup_count;
    }
}
