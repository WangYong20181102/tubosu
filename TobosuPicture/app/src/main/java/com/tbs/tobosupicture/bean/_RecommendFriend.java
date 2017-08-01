package com.tbs.tobosupicture.bean;

/**
 * Created by Mr.Lin on 2017/7/17 09:53.
 * 推荐关注好友的列表页 用于RecommendFragment
 */

public class _RecommendFriend {

    /**
     * uid : 102949
     * nick : zfsoft
     * icon : http://member.tobosu.com/public/img/icon/icon18.jpg
     * personal_signature :
     * user_type : 2
     */
    private String uid;
    private String nick;
    private String icon;
    private String is_friends;//是否互为好友哦  1--是  2--否
    private String personal_signature;
    private String user_type;


    public String getIs_friends() {
        return is_friends;
    }

    public void setIs_friends(String is_friends) {
        this.is_friends = is_friends;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
