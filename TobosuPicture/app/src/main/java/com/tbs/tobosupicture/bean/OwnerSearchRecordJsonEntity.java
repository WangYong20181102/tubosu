package com.tbs.tobosupicture.bean;

import java.util.List;

/**
 * 用户搜索记录
 * Created by Lie on 2017/8/16.
 */

public class OwnerSearchRecordJsonEntity {


    /**
     * status : 200
     * msg : success
     * data : [{"id":"81","uid":"332598","new_case_count":"0","add_time":"2017-08-04","search_condition":"深圳 60~90㎡ 2室 5~10万"},{"id":"80","uid":"332598","new_case_count":"0","add_time":"2017-08-04","search_condition":"深圳 60~90㎡ 2室"},{"id":"79","uid":"332598","new_case_count":"0","add_time":"2017-08-04","search_condition":"深圳 2室 现代简约"},{"id":"78","uid":"332598","new_case_count":"0","add_time":"2017-08-04","search_condition":"深圳 2室 简约"},{"id":"77","uid":"332598","new_case_count":"0","add_time":"2017-08-04","search_condition":"深圳 2室"},{"id":"76","uid":"332598","new_case_count":"0","add_time":"2017-08-04","search_condition":"深圳"}]
     */

    private int status;
    private String msg;
    private List<OwnerSearchRecordEntity> data;

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

    public List<OwnerSearchRecordEntity> getData() {
        return data;
    }

    public void setData(List<OwnerSearchRecordEntity> data) {
        this.data = data;
    }

    public static class OwnerSearchRecordEntity {
        /**
         * id : 81
         * uid : 332598
         * new_case_count : 0
         * add_time : 2017-08-04
         * search_condition : 深圳 60~90㎡ 2室 5~10万
         */

        private String id;
        private String uid;
        private String new_case_count;
        private String add_time;
        private String search_condition;

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

        public String getNew_case_count() {
            return new_case_count;
        }

        public void setNew_case_count(String new_case_count) {
            this.new_case_count = new_case_count;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getSearch_condition() {
            return search_condition;
        }

        public void setSearch_condition(String search_condition) {
            this.search_condition = search_condition;
        }
    }
}
