package com.tbs.tobosupicture.bean;

/**
 * Created by Mr.Lin on 2017/7/13 11:48.
 * 我的图谜实体类  含有添加为图友的按钮
 */

public class _MyFans {

    /**
     * id : 51
     * uid : 102949
     * user_type : 2
     * is_friends : 2
     * nick : zfsoft
     * icon : http://member.tobosu.com/public/img/icon/icon18.jpg
     * personal_signature :
     */

    private String id;
    private String uid;
    private String user_type;//用户类型
    private String is_friends;//是否互为好友哦  1--是  2--否
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
