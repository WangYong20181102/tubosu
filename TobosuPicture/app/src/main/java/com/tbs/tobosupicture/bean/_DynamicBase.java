package com.tbs.tobosupicture.bean;

import java.util.List;

/**
 * Created by Mr.Lin on 2017/8/2 10:56.
 */

public class _DynamicBase {

    /**
     * id : 5181
     * uid : 2864
     * is_virtual_user : 1
     * title : 中式私家小庭院设计,尽显优雅从容
     * image_url : ["https://pic.tbscache.com/ke_file/2017-05-31/m_592e99665a69a.jpg","https://pic.tbscache.com/impress_pic/2016-07-14/p_5787364a90774.jpg","https://pic.tbscache.com/impress_pic/2016-03-24/p_56f360ebf0011.jpg","https://pic.tbscache.com/impress_pic/2015-08-18/small/p_55d299340c86a.jpg","https://pic.tbscache.com/impress_pic/2016-08-15/p_57b1210145f1e.jpg","https://pic.tbscache.com/impress_pic/2016-08-23/p_57bb972ea2cfd.jpg","https://pic.tbscache.com/ke_file/2017-05-31/m_592e9af44806c.jpg","https://pic.tbscache.com/impress_pic/2016-05-27/p_574858e8795ee.jpg","https://pic.tbscache.com/ke_file/2017-05-31/m_592e9a334c9df.jpg"]
     * view_count : 12
     * praise_count : 0
     * comment_count : 0
     * add_time : 2月前
     * nick : 郁春媛c26c155
     * icon : http://member.tobosu.com/public/img/icon/icon6.jpg
     * is_praise : 0
     * is_comment : 0
     */

    private String id;//动态id号
    private String uid;//用户Id
    private String is_virtual_user;//是否是虚拟用户1--是 2---不是
    private String title;//标题
    private String view_count;//浏览数
    private String praise_count;//点赞数
    private String comment_count;//评论数
    private String add_time;//发布时间
    private String nick;//用户昵称
    private String icon;//用户的头像
    private String is_praise;//是否点赞过
    private String is_comment;//是否回复过
    private List<String> image_url;//图集

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

    public String getIs_virtual_user() {
        return is_virtual_user;
    }

    public void setIs_virtual_user(String is_virtual_user) {
        this.is_virtual_user = is_virtual_user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getView_count() {
        return view_count;
    }

    public void setView_count(String view_count) {
        this.view_count = view_count;
    }

    public String getPraise_count() {
        return praise_count;
    }

    public void setPraise_count(String praise_count) {
        this.praise_count = praise_count;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
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

    public String getIs_praise() {
        return is_praise;
    }

    public void setIs_praise(String is_praise) {
        this.is_praise = is_praise;
    }

    public String getIs_comment() {
        return is_comment;
    }

    public void setIs_comment(String is_comment) {
        this.is_comment = is_comment;
    }

    public List<String> getImage_url() {
        return image_url;
    }

    public void setImage_url(List<String> image_url) {
        this.image_url = image_url;
    }
}
