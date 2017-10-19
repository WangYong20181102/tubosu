package com.tbs.tobosupicture.bean;

import java.util.List;

/**
 * Created by Mr.Lin on 2017/10/14 09:36.
 */

public class _NewReply {

    /**
     * comment : {"id":"1818","uid":"340073","user_type":"2","praise_count":"0","content":"红河二狗额饿哦饿哦","dynamic_id":"17452","add_time":"10-13 14:03","dynamic_uid":"340072","dynamic_user_type":"2","is_praise":"0","nick":"手机用户1112","icon":"http://aliyun.tbscache.com/res/common/images/icon/icon6.jpg","reply_count":"6"}
     * child_comment : [{"uid":"340072","user_type":"2","content":"个哦二哥猴儿哥","add_time":"10-13 15:58","commented_uid":"340073","is_virtual_user":"2","nick":"手机用户1111","icon":"http://aliyun.tbscache.com/res/common/images/icon/icon17.jpg","c_nick":"手机用户1112","c_icon":"http://aliyun.tbscache.com/res/common/images/icon/icon6.jpg"},{"uid":"340072","user_type":"2","content":"饿哦二狗饿哦鞥会","add_time":"10-13 15:58","commented_uid":"340073","is_virtual_user":"2","nick":"手机用户1111","icon":"http://aliyun.tbscache.com/res/common/images/icon/icon17.jpg","c_nick":"手机用户1112","c_icon":"http://aliyun.tbscache.com/res/common/images/icon/icon6.jpg"},{"uid":"340072","user_type":"2","content":"胸锁围观和","add_time":"10-13 15:58","commented_uid":"340073","is_virtual_user":"2","nick":"手机用户1111","icon":"http://aliyun.tbscache.com/res/common/images/icon/icon17.jpg","c_nick":"手机用户1112","c_icon":"http://aliyun.tbscache.com/res/common/images/icon/icon6.jpg"},{"uid":"340072","user_type":"2","content":"哥哥哥","add_time":"10-13 15:46","commented_uid":"340073","is_virtual_user":"2","nick":"手机用户1111","icon":"http://aliyun.tbscache.com/res/common/images/icon/icon17.jpg","c_nick":"手机用户1112","c_icon":"http://aliyun.tbscache.com/res/common/images/icon/icon6.jpg"},{"uid":"340072","user_type":"2","content":"各个和偶尔会个哦","add_time":"10-13 15:46","commented_uid":"340073","is_virtual_user":"2","nick":"手机用户1111","icon":"http://aliyun.tbscache.com/res/common/images/icon/icon17.jpg","c_nick":"手机用户1112","c_icon":"http://aliyun.tbscache.com/res/common/images/icon/icon6.jpg"},{"uid":"340072","user_type":"2","content":"嘻嘻嘻！","add_time":"10-13 15:45","commented_uid":"340073","is_virtual_user":"2","nick":"手机用户1111","icon":"http://aliyun.tbscache.com/res/common/images/icon/icon17.jpg","c_nick":"手机用户1112","c_icon":"http://aliyun.tbscache.com/res/common/images/icon/icon6.jpg"}]
     */

    private CommentBean comment;
    private List<ChildCommentBean> child_comment;

    public CommentBean getComment() {
        return comment;
    }

    public void setComment(CommentBean comment) {
        this.comment = comment;
    }

    public List<ChildCommentBean> getChild_comment() {
        return child_comment;
    }

    public void setChild_comment(List<ChildCommentBean> child_comment) {
        this.child_comment = child_comment;
    }

    public static class CommentBean {
        /**
         * id : 1818
         * uid : 340073
         * user_type : 2
         * praise_count : 0
         * content : 红河二狗额饿哦饿哦
         * dynamic_id : 17452
         * add_time : 10-13 14:03
         * dynamic_uid : 340072
         * dynamic_user_type : 2
         * is_praise : 0
         * nick : 手机用户1112
         * icon : http://aliyun.tbscache.com/res/common/images/icon/icon6.jpg
         * reply_count : 6
         */

        private String id;
        private String uid;
        private String user_type;
        private String praise_count;
        private String content;
        private String dynamic_id;
        private String add_time;
        private String dynamic_uid;
        private String dynamic_user_type;
        private String is_praise;
        private String nick;
        private String icon;
        private String reply_count;

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

        public String getPraise_count() {
            return praise_count;
        }

        public void setPraise_count(String praise_count) {
            this.praise_count = praise_count;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public String getIs_praise() {
            return is_praise;
        }

        public void setIs_praise(String is_praise) {
            this.is_praise = is_praise;
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

        public String getReply_count() {
            return reply_count;
        }

        public void setReply_count(String reply_count) {
            this.reply_count = reply_count;
        }
    }

    public static class ChildCommentBean {
        /**
         * uid : 340072
         * user_type : 2
         * content : 个哦二哥猴儿哥
         * add_time : 10-13 15:58
         * commented_uid : 340073
         * is_virtual_user : 2
         * nick : 手机用户1111
         * icon : http://aliyun.tbscache.com/res/common/images/icon/icon17.jpg
         * c_nick : 手机用户1112
         * c_icon : http://aliyun.tbscache.com/res/common/images/icon/icon6.jpg
         */
        private String id;
        private String uid;
        private String user_type;
        private String content;
        private String add_time;
        private String nick;
        private String icon;
        private String c_nick;

        public ChildCommentBean() {

        }

        public ChildCommentBean(String id, String uid, String user_type, String content, String add_time, String nick, String icon, String c_nick) {
            this.id = id;
            this.uid = uid;
            this.user_type = user_type;
            this.content = content;
            this.add_time = add_time;
            this.nick = nick;
            this.icon = icon;
            this.c_nick = c_nick;
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

        public String getC_nick() {
            return c_nick;
        }

        public void setC_nick(String c_nick) {
            this.c_nick = c_nick;
        }

    }
}
