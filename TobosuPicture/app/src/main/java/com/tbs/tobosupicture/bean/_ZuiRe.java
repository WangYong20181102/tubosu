package com.tbs.tobosupicture.bean;

import java.util.List;

/**
 * Created by Mr.Lin on 2017/7/17 16:23.
 * 最热实体类
 */

public class _ZuiRe {

    private List<ActiveUser> active_user;
    private List<Dynamic> dynamic;

    public List<ActiveUser> getActive_user() {
        return active_user;
    }

    public void setActive_user(List<ActiveUser> active_user) {
        this.active_user = active_user;
    }

    public List<Dynamic> getDynamic() {
        return dynamic;
    }

    public void setDynamic(List<Dynamic> dynamic) {
        this.dynamic = dynamic;
    }

    //最热人气榜
    public static class ActiveUser {
        /**
         * uid : 102942
         * nick : HTC123
         * icon : http://member.tobosu.com/public/img/icon/icon21.jpg
         * is_virtual_user : 2
         */

        private String uid;//用户的id
        private String nick;//用户的昵称
        private String icon;//用户的头像
        private String is_virtual_user;//是否是虚拟用户

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

        public String getIs_virtual_user() {
            return is_virtual_user;
        }

        public void setIs_virtual_user(String is_virtual_user) {
            this.is_virtual_user = is_virtual_user;
        }
    }

    //用户发布的动态
    public static class Dynamic {
        /**
         * id : 316
         * uid : 3592
         * is_virtual_user : 1
         * title : 10平米小单间布置图片,出门在外的暖心小窝
         * view_count : 22640
         * praise_count : 0
         * comment_count : 0
         * nick : 谷族滕4e17d2d
         * is_praise;//是否点赞
         * is_comment;//是否回复
         * icon : http://wx.qlogo.cn/mmopen/ktZ0nuwFpAarW8QrDj78oKGHUl0Omq0tUHVFMgsKYmGPCpv7t3qlnjdr9PucrY3g2SgnhMMcuY5ic0Yc4NMgZckEiagktIw2dE/0
         */

        private String id;//动态id
        private String uid;//用户id
        private String is_virtual_user;//是否是虚拟用户
        private String title;//标题
        //        private String image_url;//第一张图片
//        private String image_url2;//第二张图片
//        private String image_url3;//第三张图片
//        private String image_url4;
//        private String image_url5;
//        private String image_url6;
//        private String image_url7;
//        private String image_url8;
//        private String image_url9;
        private List<String> image_url;
        private String view_count;//浏览数
        private String praise_count;//点赞数
        private String comment_count;//评论数
        private String nick;//用户的昵称
        private String icon;//用户的头像
        private String is_praise;//是否点赞
        private String is_comment;//是否回复

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
        public List<String> getImage_url() {
            return image_url;
        }

        public void setImage_url(List<String> image_url) {
            this.image_url = image_url;
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
    }
}
