package com.tbs.tobosupicture.bean;

import java.util.List;

/**
 * Created by Mr.Lin on 2017/8/2 18:06.
 */

public class _ZuiXin {

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

    public static class ActiveUser {
        /**
         * uid : 102942
         * nick : zhong
         * icon : http://member.tobosu.com/public/img/icon/icon21.jpg
         * is_virtual_user : 2
         */

        private String uid;
        private String nick;
        private String icon;
        private String is_virtual_user;

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

    public static class Dynamic {
        /**
         * id : 5183
         * uid : 23109
         * is_virtual_user : 2
         * title :
         * view_count : 0
         * praise_count : 0
         * comment_count : 0
         * add_time : 4天前
         * nick : 飞鱼装饰
         * icon : http://cdn111.dev.tobosu.com/company_logo/2017-07-10/thumb_59631dcb76aa5.jpg
         * is_praise : 0
         * is_comment : 0
         */

        private String id;
        private String uid;
        private String is_virtual_user;
        private String title;

        //        private String image_url;
//        private String image_url2;
//        private String image_url3;
//        private String image_url4;
//        private String image_url5;
//        private String image_url6;
//        private String image_url7;
//        private String image_url8;
//        private String image_url9;
        private List<String> image_url;
        private String view_count;
        private String praise_count;
        private String comment_count;
        private String add_time;
        private String nick;
        private String icon;
        private String is_praise;
        private String is_comment;

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

        public List<String> getImage_url() {
            return image_url;
        }

        public void setImage_url(List<String> image_url) {
            this.image_url = image_url;
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
    }
}
