package com.tbs.tobosupicture.bean;

import java.util.List;

/**
 * Created by Lie on 2017/8/16.
 */

public class CompanySearchRecordJsonEntity {


    /**
     * status : 200
     * msg : success
     * data : [{"id":"94","uid":"102942","add_time":"2017-08-16","nick":"zhong","icon":"http://aliyun.tbscache.com/res/common/images/icon/icon24.jpg","is_read":"1","search_condition":"深圳 30~60㎡","city":"深圳"},{"id":"93","uid":"102942","add_time":"2017-08-16","nick":"zhong","icon":"http://aliyun.tbscache.com/res/common/images/icon/icon24.jpg","is_read":"1","search_condition":"深圳 30㎡以下","city":"深圳"},{"id":"92","uid":"102942","add_time":"2017-08-16","nick":"zhong","icon":"http://aliyun.tbscache.com/res/common/images/icon/icon24.jpg","is_read":"1","search_condition":"深圳 30㎡以下 简约","city":"深圳"},{"id":"91","uid":"102942","add_time":"2017-08-16","nick":"zhong","icon":"http://aliyun.tbscache.com/res/common/images/icon/icon24.jpg","is_read":"1","search_condition":"深圳 30㎡以下 2室 简约","city":"深圳"},{"id":"90","uid":"102942","add_time":"2017-08-16","nick":"zhong","icon":"http://aliyun.tbscache.com/res/common/images/icon/icon24.jpg","is_read":"1","search_condition":"深圳 30㎡以下 2室","city":"深圳"},{"id":"89","uid":"102942","add_time":"2017-08-16","nick":"zhong","icon":"http://aliyun.tbscache.com/res/common/images/icon/icon24.jpg","is_read":"1","search_condition":"深圳 30㎡以下 2室 5万以下","city":"深圳"},{"id":"82","uid":"102943","add_time":"2017-08-16","nick":"wangmengyang","icon":"http://aliyun.tbscache.com/res/common/images/icon/icon27.jpg","is_read":"1","search_condition":"深圳","city":"深圳福田"},{"id":"81","uid":"332598","add_time":"2017-08-16","nick":"tbs_o_a_VyWeKJnXQL","icon":"http://aliyun.tbscache.com/res/common/images/icon/icon13.jpg","is_read":"1","search_condition":"深圳 60~90㎡ 2室 5~10万","city":"深圳"},{"id":"80","uid":"332598","add_time":"2017-08-16","nick":"tbs_o_a_VyWeKJnXQL","icon":"http://aliyun.tbscache.com/res/common/images/icon/icon13.jpg","is_read":"1","search_condition":"深圳 60~90㎡ 2室","city":"深圳"},{"id":"79","uid":"332598","add_time":"2017-08-16","nick":"tbs_o_a_VyWeKJnXQL","icon":"http://aliyun.tbscache.com/res/common/images/icon/icon13.jpg","is_read":"1","search_condition":"深圳 2室 现代简约","city":"深圳"}]
     */

    private int status;
    private String msg;
    private List<CompanySearchRecordEntity> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<CompanySearchRecordEntity> getData() {
        return data;
    }

    public void setData(List<CompanySearchRecordEntity> data) {
        this.data = data;
    }

    public static class CompanySearchRecordEntity {
        /**
         * id : 94
         * uid : 102942
         * add_time : 2017-08-16
         * nick : zhong
         * icon : http://aliyun.tbscache.com/res/common/images/icon/icon24.jpg
         * is_read : 1
         * search_condition : 深圳 30~60㎡
         * city : 深圳
         */

        private String id;
        private String uid;
        private String add_time;
        private String nick;
        private String icon;
        private String is_read;
        private String search_condition;
        private String city;

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

        public String getIs_read() {
            return is_read;
        }

        public void setIs_read(String is_read) {
            this.is_read = is_read;
        }

        public String getSearch_condition() {
            return search_condition;
        }

        public void setSearch_condition(String search_condition) {
            this.search_condition = search_condition;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }
}
