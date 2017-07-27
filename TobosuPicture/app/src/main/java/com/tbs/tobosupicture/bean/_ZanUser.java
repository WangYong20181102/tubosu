package com.tbs.tobosupicture.bean;

import java.util.List;

/**
 * Created by Mr.Lin on 2017/7/25 10:10.
 * 点赞用户的实体类
 */

public class _ZanUser {

    /**
     * count : 3
     * praise_user : [{"uid":"102946","id":"1301","nick":"wang123","icon":"http://imagei.tobosu.com/small/hf2012061435595.jpg","type":"1","personal_signature":""},{"uid":"102944","id":"1291","nick":"cb870125","icon":"http://member.tobosu.com/public/img/icon/icon2.jpg","type":"1","personal_signature":""},{"uid":"23109","id":"33573","nick":"飞鱼装饰","icon":"http://cdn111.dev.tobosu.com/company_logo/2017-07-10/thumb_59631dcb76aa5.jpg","type":"3","personal_signature":""}]
     */

    private String count;//总数
    private List<PraiseUser> praise_user;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<PraiseUser> getPraise_user() {
        return praise_user;
    }

    public void setPraise_user(List<PraiseUser> praise_user) {
        this.praise_user = praise_user;
    }

    public static class PraiseUser {
        /**
         * uid : 102946
         * id : 1301
         * nick : wang123
         * icon : http://imagei.tobosu.com/small/hf2012061435595.jpg
         * type : 1
         * personal_signature :
         */

        private String uid;
        private String id;
        private String nick;
        private String icon;
        private String type;
        private String personal_signature;

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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPersonal_signature() {
            return personal_signature;
        }

        public void setPersonal_signature(String personal_signature) {
            this.personal_signature = personal_signature;
        }
    }
}
