package com.tbs.tobosupicture.bean;

/**
 * Created by Mr.Lin on 2017/7/15 16:59.
 * 我的图友实体类  作用与我的图友列表的显示
 */

public class _MyFriend {

    /**
     * id : 74
     * uid : 102942
     * user_type : 2
     * is_friends : 1
     * nick : zhong
     * icon : http://member.tobosu.com/public/img/icon/icon21.jpg
     * personal_signature : 承文是傻逼
     */

    private String id;
    private String uid;
    private String user_type;
    private String is_friends;
    private String nick;
    private String icon;
    private String personal_signature;

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

    public String getIs_friends() {
        return is_friends;
    }

    public void setIs_friends(String is_friends) {
        this.is_friends = is_friends;
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

    public String getPersonal_signature() {
        return personal_signature;
    }

    public void setPersonal_signature(String personal_signature) {
        this.personal_signature = personal_signature;
    }
}
