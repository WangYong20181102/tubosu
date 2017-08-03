package com.tbs.tobosupicture.bean;

import java.util.List;

/**
 * Created by Mr.Lin on 2017/7/25 16:21.
 * 回复评论列表的实体类
 */

public class _Reply {

    /**
     * commented : {"id":"325","uid":"102942","user_type":"2","content":"和我国和和个哦个","praise_count":"2","add_time":"07-24 13:37","nick":"HTC123","icon":"http://member.tobosu.com/public/img/icon/icon21.jpg","is_praise":"0"}
     * praise_user : [{"uid":"102941","id":"81","nick":"18606601814","icon":"http://member.tobosu.com/public/img/icon/icon21.jpg"},{"uid":"102946","id":"1301","nick":"wang123","icon":"http://imagei.tobosu.com/small/hf2012061435595.jpg"}]
     * comment : [{"id":"336","uid":"102943","user_type":"2","content":"个好饿猴哥猴哥额","add_time":"01-01 08:00","nick":"wangmengyang","icon":"http://member.tobosu.com/public/img/icon/icon1.jpg","is_praise":"2","praise_count":"0"},{"id":"337","uid":"102946","user_type":"2","content":"个人火热如何","add_time":"01-01 08:00","nick":"wang123","icon":"http://imagei.tobosu.com/small/hf2012061435595.jpg","is_praise":"2","praise_count":"0"}]
     */

    private Commented commented;
    private List<PraiseUser> praise_user;
    private List<Comment> comment;

    public Commented getCommented() {
        return commented;
    }

    public void setCommented(Commented commented) {
        this.commented = commented;
    }

    public List<PraiseUser> getPraise_user() {
        return praise_user;
    }

    public void setPraise_user(List<PraiseUser> praise_user) {
        this.praise_user = praise_user;
    }

    public List<Comment> getComment() {
        return comment;
    }

    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }

    public static class Commented {
        /**
         * id : 325
         * uid : 102942
         * user_type : 2
         * content : 和我国和和个哦个
         * praise_count : 2
         * add_time : 07-24 13:37
         * nick : HTC123
         * icon : http://member.tobosu.com/public/img/icon/icon21.jpg
         * is_praise : 0
         */

        private String id;
        private String uid;
        private String user_type;
        private String content;
        private String praise_count;
        private String add_time;
        private String nick;
        private String icon;
        private String is_praise;

        public String getComment_count() {
            return comment_count;
        }

        public void setComment_count(String comment_count) {
            this.comment_count = comment_count;
        }

        private String comment_count;


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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPraise_count() {
            return praise_count;
        }

        public void setPraise_count(String praise_count) {
            this.praise_count = praise_count;
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
    }

    public static class PraiseUser {
        /**
         * uid : 102941
         * id : 81
         * nick : 18606601814
         * icon : http://member.tobosu.com/public/img/icon/icon21.jpg
         */

        private String uid;
        private String id;
        private String nick;
        private String icon;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

    public static class Comment {
        /**
         * id : 336
         * uid : 102943
         * user_type : 2
         * content : 个好饿猴哥猴哥额
         * add_time : 01-01 08:00
         * nick : wangmengyang
         * icon : http://member.tobosu.com/public/img/icon/icon1.jpg
         * is_praise : 2
         * praise_count : 0
         */

        private String id;
        private String uid;
        private String user_type;
        private String content;
        private String add_time;
        private String nick;
        private String icon;
        private String is_praise;
        private String praise_count;

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public String getPraise_count() {
            return praise_count;
        }

        public void setPraise_count(String praise_count) {
            this.praise_count = praise_count;
        }
    }
}
