package com.tbs.tobosupicture.bean;

import java.util.List;

/**
 * Created by Mr.Lin on 2017/7/18 16:30.
 * 动态详情实体类
 */

public class _DynamicDetail {

    /**
     * dynamic : {"id":"1","uid":"102942","is_virtual_user":"2","title":"致青春","image_url":"https://opic.tbscache.com/users/zsgs/case/2015/04-02/index/bbf29b55-84b1-e95e-b2f1-c5582fccb25e.jpg","image_url2":"https://opic.tbscache.com/users/zsgs/case/2015/04-02/index/2c6a82c8-a0c8-f3d4-6218-d80c61f81313.jpg","image_url3":"https://opic.tbscache.com/users/zsgs/case/2015/04-03/index/2510a1e0-76de-9983-3327-462076a96563.jpg","image_url4":"https://opic.tbscache.com/users/zsgs/case/2015/04-03/index/2b581c3e-4f95-aa53-81e1-90b098359322.jpg","image_url5":"https://opic.tbscache.com/users/zsgs/case/2015/04-03/index/1d500cae-2a15-3dd0-321b-8b8d7dfe4976.jpg","image_url6":"https://opic.tbscache.com/users/zsgs/case/2015/04-03/index/52aa8ab5-05a1-2a6c-ebee-8908ba5890da.jpg","image_url7":"https://opic.tbscache.com/users/zsgs/case/2015/04-03/index/d86a7984-75d3-b373-0028-16c949c0a471.jpg","image_url8":"https://opic.tbscache.com/users/zsgs/case/2015/04-03/index/1469394e-cd6d-6a8d-d57f-e5967c66eb85.jpg","image_url9":"https://opic.tbscache.com/users/zsgs/case/2015/04-03/index/1469394e-cd6d-6a8d-d57f-e5967c66eb85.jpg","praise_count":"0","comment_count":"4","add_time":"6天前","nick":"HTC123","icon":"http://member.tobosu.com/public/img/icon/icon21.jpg"}
     * praise_user : [{"uid":"102941","icon":"http://member.tobosu.com/public/img/icon/icon21.jpg","type":1},{"uid":"23109","icon":"http://cdn111.dev.tobosu.com/company_logo/2017-07-10/thumb_59631dcb76aa5.jpg","type":3},{"uid":"102943","icon":"http://member.tobosu.com/public/img/icon/icon1.jpg","type":1}]
     * praise_count : 3
     * comment : [{"id":"28","uid":"102949","is_virtual_user":"2","content":"房子挺好看的","praise_count":"0","add_time":"07-17 17:18","nick":"zfsoft","icon":"http://member.tobosu.com/public/img/icon/icon18.jpg","reply_count":"0"},{"id":"27","uid":"102946","is_virtual_user":"2","content":"房子还不错","praise_count":"0","add_time":"07-17 17:17","nick":"wang123","icon":"http://imagei.tobosu.com/small/hf2012061435595.jpg","reply_count":"1"},{"id":"26","uid":"102944","is_virtual_user":"2","content":"房子装修效果还不错","praise_count":"0","add_time":"07-17 17:17","nick":"cb870125","icon":"http://member.tobosu.com/public/img/icon/icon2.jpg","reply_count":"0"},{"id":"25","uid":"23109","is_virtual_user":"2","content":"你是猴子请来的逗比吗","praise_count":"0","add_time":"07-17 17:16","nick":"飞鱼装饰","icon":"http://cdn111.dev.tobosu.com/company_logo/2017-07-10/thumb_59631dcb76aa5.jpg","reply_count":"2"}]
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
         * id : 1
         * uid : 102942
         * is_virtual_user : 2
         * title : 致青春
         * image_url : https://opic.tbscache.com/users/zsgs/case/2015/04-02/index/bbf29b55-84b1-e95e-b2f1-c5582fccb25e.jpg
         * image_url2 : https://opic.tbscache.com/users/zsgs/case/2015/04-02/index/2c6a82c8-a0c8-f3d4-6218-d80c61f81313.jpg
         * image_url3 : https://opic.tbscache.com/users/zsgs/case/2015/04-03/index/2510a1e0-76de-9983-3327-462076a96563.jpg
         * image_url4 : https://opic.tbscache.com/users/zsgs/case/2015/04-03/index/2b581c3e-4f95-aa53-81e1-90b098359322.jpg
         * image_url5 : https://opic.tbscache.com/users/zsgs/case/2015/04-03/index/1d500cae-2a15-3dd0-321b-8b8d7dfe4976.jpg
         * image_url6 : https://opic.tbscache.com/users/zsgs/case/2015/04-03/index/52aa8ab5-05a1-2a6c-ebee-8908ba5890da.jpg
         * image_url7 : https://opic.tbscache.com/users/zsgs/case/2015/04-03/index/d86a7984-75d3-b373-0028-16c949c0a471.jpg
         * image_url8 : https://opic.tbscache.com/users/zsgs/case/2015/04-03/index/1469394e-cd6d-6a8d-d57f-e5967c66eb85.jpg
         * image_url9 : https://opic.tbscache.com/users/zsgs/case/2015/04-03/index/1469394e-cd6d-6a8d-d57f-e5967c66eb85.jpg
         * praise_count : 0
         * comment_count : 4
         * add_time : 6天前
         * is_praise: 0//是否点过赞
         * is_comment:0 //是否评论过
         * nick : HTC123
         * icon : http://member.tobosu.com/public/img/icon/icon21.jpg
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
    }

    public static class PraiseUser {
        /**
         * uid : 102941
         * icon : http://member.tobosu.com/public/img/icon/icon21.jpg
         * type : 1
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
         * id : 28
         * uid : 102949
         * is_virtual_user : 2
         * content : 房子挺好看的
         * praise_count : 0
         * add_time : 07-17 17:18
         * nick : zfsoft
         * icon : http://member.tobosu.com/public/img/icon/icon18.jpg
         * reply_count : 0
         */

        private String id;
        private String uid;
        private String is_virtual_user;
        private String content;
        private String praise_count;
        private String add_time;
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

        public String getReply_count() {
            return reply_count;
        }

        public void setReply_count(String reply_count) {
            this.reply_count = reply_count;
        }
    }
}
