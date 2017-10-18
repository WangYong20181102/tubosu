package com.tbs.tobosupicture.bean;

/**
 * Created by Mr.Lin on 2017/8/10 10:20.
 */

public class _DynamicMsg {

    /**
     * uid : 272286
     * user_type : 2
     * dynamic_id : 5184
     * add_time : 11分钟前
     * content : 胡明明
     * nick : 呵呵^_^
     * icon : http://cdn111.dev.tobosu.com/head_file/2017-08-08/598976b6195bd.jpg
     * title : 不是我说你，你跟钟老板都是垃圾！
     * image_url : https://pic.tbscache.com/impress_pic/2016-10-02/p_57f0650958f5e.jpg
     * dynamic_uid : 23109
     * dynamic_user_type : 2
     */

    private String uid;
    private String user_type;
    private String dynamic_id;
    private String add_time;
    private String content;
    private String nick;
    private String icon;
    private String title;
    private String image_url;
    private String dynamic_uid;
    private String dynamic_user_type;

    private String page_level;//页面层级 是返回哪个页面 页面层级：1：评论列表页； 2：评论回复页
    private String msg_type;//是点赞还是评论  用这个参数去判断是否要定位 消息类型：1：点赞； 2：评论
    private String location_id;//如果需要定位的id
    private String comment_id;//如果是跳转到评论回复页所需的评论回复id

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

    public String getDynamic_id() {
        return dynamic_id;
    }

    public void setDynamic_id(String dynamic_id) {
        this.dynamic_id = dynamic_id;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getDynamic_uid() {
        return dynamic_uid;
    }

    public void setDynamic_uid(String dynamic_uid) {
        this.dynamic_uid = dynamic_uid;
    }

    public String getDynamic_user_type() {
        return dynamic_user_type;
    }

    public void setDynamic_user_type(String dynamic_user_type) {
        this.dynamic_user_type = dynamic_user_type;
    }

    public String getPage_level() {
        return page_level;
    }

    public void setPage_level(String page_level) {
        this.page_level = page_level;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }
}
