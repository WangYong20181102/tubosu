package com.tbs.tobosupicture.bean;

/**
 * Created by Mr.Lin on 2017/10/16 17:22.
 * 回复评论的接口 评论成功返回的玩意儿
 */

public class _ReturnContent {

    /**
     * uid : 23109
     * user_type : 2
     * nick : 飞鱼装饰
     * icon : https://pic.tbscache.com/company_logo/2017-09-05/thumb_59ae0077cdf30.png
     * content : 被控
     * add_time : 10-16 17:15
     */


    private String id;
    private String uid;
    private String user_type;
    private String nick;
    private String icon;
    private String content;
    private String add_time;

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

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
}
