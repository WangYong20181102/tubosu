package com.tbs.tobosupicture.bean;

/**
 * Created by Mr.Lin on 2017/7/24 15:27.
 */

public class _HisFriends {
    /**
     * id : 29
     * uid : 102949
     * user_type : 2
     * nick : zfsoft
     * icon : http://member.tobosu.com/public/img/icon/icon18.jpg
     * personal_signature :
     */

    private String id;
    private String uid;
    private String user_type;//用户的类型 1--虚拟用户 2--不是虚拟用户
    private String nick;//用户的昵称
    private String icon;//用户的头像
    private String personal_signature;//用户的个性签名

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

    public String getPersonal_signature() {
        return personal_signature;
    }

    public void setPersonal_signature(String personal_signature) {
        this.personal_signature = personal_signature;
    }
}
