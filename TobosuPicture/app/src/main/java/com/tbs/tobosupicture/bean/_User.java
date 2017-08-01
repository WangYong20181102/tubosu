package com.tbs.tobosupicture.bean;

/**
 * Created by Mr.Lin on 2017/6/30 10:01.
 */

public class _User{

    /**
     * nick : 飞鱼装饰
     * icon : http://cdn111.dev.tobosu.com/company_logo/2017-07-10/thumb_59631dcb76aa5.jpg
     * personal_signature :
     * uid : 23109
     * user_type : 3
     */

    private String nick;//用户的昵称
    private String icon;//用户头像
    private String personal_signature;//用户个性签名
    private String uid;//用户的uid
    private String user_type;//用户的类型 1：业主； 3：装修公司

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
}
