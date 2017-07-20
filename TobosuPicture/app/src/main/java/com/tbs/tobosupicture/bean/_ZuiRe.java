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
         * image_url : http://pic.tbscache.com/ke_file/2016-07-09/m_5780c0283aa98.jpg
         * image_url2 : http://pic.tbscache.com/ke_file/2016-07-09/m_5780c05d2f04f.jpg
         * image_url3 : http://pic.tbscache.com/ke_file/2016-07-09/m_5780c06855c8e.jpg
         * image_url4 : http://pic.tbscache.com/ke_file/2016-07-09/m_5780c03dc0858.jpg
         * image_url5 : http://pic.tbscache.com/ke_file/2016-07-09/m_5780c0455bcb1.jpg
         * image_url6 : http://pic.tbscache.com/ke_file/2016-07-09/m_5780c075ce844.jpg
         * image_url7 : http://pic.tbscache.com/ke_file/2016-07-09/m_5780c07c7df50.jpg
         * image_url8 : http://pic.tbscache.com/ke_file/2016-07-09/m_5780c082a22a7.jpg
         * image_url9 : https://pic.tbscache.com/impress_pic/2016-07-07/p_577df18da0db9.jpg
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
        private String image_url;//第一张图片
        private String image_url2;//第二张图片
        private String image_url3;//第三张图片
        private String image_url4;
        private String image_url5;
        private String image_url6;
        private String image_url7;
        private String image_url8;
        private String image_url9;
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

        public void setIs_virtual_user(String is_virtual_user) {
            this.is_virtual_user = is_virtual_user;
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

        public String getImage_url2() {
            return image_url2;
        }

        public void setImage_url2(String image_url2) {
            this.image_url2 = image_url2;
        }

        public String getImage_url3() {
            return image_url3;
        }

        public void setImage_url3(String image_url3) {
            this.image_url3 = image_url3;
        }

        public String getImage_url4() {
            return image_url4;
        }

        public void setImage_url4(String image_url4) {
            this.image_url4 = image_url4;
        }

        public String getImage_url5() {
            return image_url5;
        }

        public void setImage_url5(String image_url5) {
            this.image_url5 = image_url5;
        }

        public String getImage_url6() {
            return image_url6;
        }

        public void setImage_url6(String image_url6) {
            this.image_url6 = image_url6;
        }

        public String getImage_url7() {
            return image_url7;
        }

        public void setImage_url7(String image_url7) {
            this.image_url7 = image_url7;
        }

        public String getImage_url8() {
            return image_url8;
        }

        public void setImage_url8(String image_url8) {
            this.image_url8 = image_url8;
        }

        public String getImage_url9() {
            return image_url9;
        }

        public void setImage_url9(String image_url9) {
            this.image_url9 = image_url9;
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
