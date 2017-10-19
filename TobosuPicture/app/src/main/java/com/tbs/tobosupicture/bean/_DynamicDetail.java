package com.tbs.tobosupicture.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Lin on 2017/7/18 16:30.
 * 动态详情实体类
 */

public class _DynamicDetail {

    /**
     * dynamic : {"id":"17443","uid":"340062","is_virtual_user":"2","title":"📱","image_url":"http://cdn111.dev.tobosu.com/app/2017-10-11/59dd729871889.jpg","image_url2":"","image_url3":"","image_url4":"","image_url5":"","image_url6":"","image_url7":"","image_url8":"","image_url9":"","praise_count":"2","comment_count":"3","add_time":"1天前","is_praise":"0","is_comment":"0","nick":"手机用户1101","icon":"http://aliyun.tbscache.com/res/common/images/icon/icon6.jpg"}
     * praise_user : [{"uid":"23109","icon":"https://pic.tbscache.com/company_logo/2017-09-05/thumb_59ae0077cdf30.png","type":"3"},{"uid":"340062","icon":"http://aliyun.tbscache.com/res/common/images/icon/icon6.jpg","type":"1"}]
     * praise_count : 2
     * comment : [{"id":"1797","uid":"340065","is_virtual_user":"2","content":"死iOS佬！","praise_count":"0","add_time":"10-12 10:10","nick":"手机用户1104","icon":"http://aliyun.tbscache.com/res/common/images/icon/icon19.jpg","is_praise":"0","reply_count":"1","child_comment":[{"uid":"23109","user_type":"2","commented_uid":"340065","is_virtual_user":"2","content":"考虑考虑","nick":"飞鱼装饰","icon":"https://pic.tbscache.com/company_logo/2017-09-05/thumb_59ae0077cdf30.png","c_nick":"手机用户1104","c_icon":"http://aliyun.tbscache.com/res/common/images/icon/icon19.jpg"}]},{"id":"1796","uid":"340064","is_virtual_user":"2","content":"死安卓佬！","praise_count":"0","add_time":"10-12 10:10","nick":"手机用户1103","icon":"http://aliyun.tbscache.com/res/common/images/icon/icon27.jpg","is_praise":"0","reply_count":"0","child_comment":[]},{"id":"1795","uid":"340063","is_virtual_user":"2","content":"傻叼，你是做iOS的吧！","praise_count":"0","add_time":"10-12 10:09","nick":"手机用户1102","icon":"http://aliyun.tbscache.com/res/common/images/icon/icon4.jpg","is_praise":"0","reply_count":"2","child_comment":[{"uid":"340062","user_type":"2","commented_uid":"340063","is_virtual_user":"2","content":"你给我闭嘴，吵什么吵","nick":"手机用户1101","icon":"http://aliyun.tbscache.com/res/common/images/icon/icon6.jpg","c_nick":"手机用户1102","c_icon":"http://aliyun.tbscache.com/res/common/images/icon/icon4.jpg"},{"uid":"340062","user_type":"2","commented_uid":"340063","is_virtual_user":"2","content":"你给我闭嘴，吵什么吵","nick":"手机用户1101","icon":"http://aliyun.tbscache.com/res/common/images/icon/icon6.jpg","c_nick":"手机用户1102","c_icon":"http://aliyun.tbscache.com/res/common/images/icon/icon4.jpg"}]}]
     */

    private Dynamic dynamic;
    private String praise_count;
    private List<PraiseUser> praise_user;
    private List<Comment> comment;

    public Dynamic getDynamic() {
        return dynamic;
    }

    public void setDynamic(Dynamic dynamic) {
        this.dynamic = dynamic;
    }

    public String getPraise_count() {
        return praise_count;
    }

    public void setPraise_count(String praise_count) {
        this.praise_count = praise_count;
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

    public static class Dynamic {
        /**
         * id : 17443
         * uid : 340062
         * is_virtual_user : 2
         * title : 📱
         * image_url : http://cdn111.dev.tobosu.com/app/2017-10-11/59dd729871889.jpg
         * image_url2 :
         * image_url3 :
         * image_url4 :
         * image_url5 :
         * image_url6 :
         * image_url7 :
         * image_url8 :
         * image_url9 :
         * praise_count : 2
         * comment_count : 3
         * add_time : 1天前
         * is_praise : 0
         * is_comment : 0
         * nick : 手机用户1101
         * icon : http://aliyun.tbscache.com/res/common/images/icon/icon6.jpg
         */

        private String id;
        private String uid;
        private String is_virtual_user;
        private String title;
        private String image_url;
        private String image_url2;
        private String image_url3;
        private String image_url4;
        private String image_url5;
        private String image_url6;
        private String image_url7;
        private String image_url8;
        private String image_url9;
        private String praise_count;
        private String comment_count;
        private String add_time;
        private String is_praise;
        private String is_comment;
        private String nick;
        private String icon;

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

    public static class PraiseUser {
        /**
         * uid : 23109
         * icon : https://pic.tbscache.com/company_logo/2017-09-05/thumb_59ae0077cdf30.png
         * type : 3
         */

        private String uid;
        private String icon;
        private String type;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class Comment {
        /**
         * id : 1797
         * uid : 340065
         * is_virtual_user : 2
         * content : 死iOS佬！
         * praise_count : 0
         * add_time : 10-12 10:10
         * nick : 手机用户1104
         * icon : http://aliyun.tbscache.com/res/common/images/icon/icon19.jpg
         * is_praise : 0
         * reply_count : 1
         * child_comment : [{"uid":"23109","user_type":"2","commented_uid":"340065","is_virtual_user":"2","content":"考虑考虑","nick":"飞鱼装饰","icon":"https://pic.tbscache.com/company_logo/2017-09-05/thumb_59ae0077cdf30.png","c_nick":"手机用户1104","c_icon":"http://aliyun.tbscache.com/res/common/images/icon/icon19.jpg"}]
         */

        private String id;
        private String uid;
        private String is_virtual_user;
        private String content;
        private String praise_count;
        private String add_time;
        private String nick;
        private String icon;
        private String is_praise;
        private String reply_count;
        private ArrayList<ChildComment> child_comment;

        public Comment() {
        }

        public Comment(String id, String uid, String is_virtual_user,
                       String content, String praise_count, String add_time,
                       String nick, String icon, String is_praise, String reply_count, ArrayList<ChildComment> child_comment) {
            this.id = id;
            this.uid = uid;
            this.is_virtual_user = is_virtual_user;
            this.content = content;
            this.praise_count = praise_count;
            this.add_time = add_time;
            this.nick = nick;
            this.icon = icon;
            this.is_praise = is_praise;
            this.reply_count = reply_count;
            this.child_comment = child_comment;

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

        public String getReply_count() {
            return reply_count;
        }

        public void setReply_count(String reply_count) {
            this.reply_count = reply_count;
        }

        public ArrayList<ChildComment> getChild_comment() {
            return child_comment;
        }

        public void setChild_comment(ArrayList<ChildComment> child_comment) {
            this.child_comment = child_comment;
        }

        public static class ChildComment {
            /**
             * content : 考虑考虑
             * nick : 飞鱼装饰
             * c_nick : 手机用户1125
             */

            private String content;
            private String nick;
            private String c_nick;

            public ChildComment() {

            }

            public ChildComment(String content, String nick, String c_nick) {
                this.content = content;
                this.nick = nick;
                this.c_nick = c_nick;
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

            public String getC_nick() {
                return c_nick;
            }

            public void setC_nick(String c_nick) {
                this.c_nick = c_nick;
            }
        }
    }
}
