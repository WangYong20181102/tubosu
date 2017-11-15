package com.tbs.tobosutype.bean;

import java.util.ArrayList;

/**
 * Created by Lie on 2017/11/14.
 */

public class CompanyJsonItem {

    /**
     * status : 200
     * msg : success
     * data : [{"id":"273920","collect_id":"12580","name":"湖南省淘家装饰设计工程有限公司","icon":"https://pic.tbscache.com/company_logo/2017-04-07/thumb_58e7330762361.jpg","is_certified":"1","district_name":"贵溪市","is_discount":"0","suite_count":"12","case_count":"4"},{"id":"273952","collect_id":"12579","name":"湖北名星全案装饰","icon":"https://pic.tbscache.com/company_logo/2017-05-31/thumb_592e8b05531e6.png","is_certified":"0","district_name":"和县","is_discount":"0","suite_count":"103","case_count":"39"},{"id":"276146","collect_id":"12574","name":"杭州品鑫装饰设计有限公司","icon":"https://pic.tbscache.com/company_logo/2017-09-18/thumb_59bf70000c0ba.jpg","is_certified":"1","district_name":"堆龙德庆县","is_discount":"0","suite_count":"21","case_count":"0"},{"id":"276774","collect_id":"12573","name":"南昌品宅装饰工程有限公司","icon":"https://pic.tbscache.com/company_logo/2017-11-07/thumb_5a01728cac9cd.jpg","is_certified":"1","district_name":"正宁县","is_discount":"0","suite_count":"0","case_count":"0"}]
     */

    private int status;
    private String msg;
    private ArrayList<CompanyBean> data;

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

    public ArrayList<CompanyBean> getData() {
        return data;
    }

    public void setData(ArrayList<CompanyBean> data) {
        this.data = data;
    }

    public static class CompanyBean {
        /**
         * id : 273920
         * collect_id : 12580
         * name : 湖南省淘家装饰设计工程有限公司
         * icon : https://pic.tbscache.com/company_logo/2017-04-07/thumb_58e7330762361.jpg
         * is_certified : 1
         * district_name : 贵溪市
         * is_discount : 0
         * suite_count : 12
         * case_count : 4
         */

        private String id;
        private String collect_id;
        private String name;
        private String icon;
        private String is_certified;
        private String district_name;
        private String is_discount;
        private String suite_count;
        private String case_count;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCollect_id() {
            return collect_id;
        }

        public void setCollect_id(String collect_id) {
            this.collect_id = collect_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getIs_certified() {
            return is_certified;
        }

        public void setIs_certified(String is_certified) {
            this.is_certified = is_certified;
        }

        public String getDistrict_name() {
            return district_name;
        }

        public void setDistrict_name(String district_name) {
            this.district_name = district_name;
        }

        public String getIs_discount() {
            return is_discount;
        }

        public void setIs_discount(String is_discount) {
            this.is_discount = is_discount;
        }

        public String getSuite_count() {
            return suite_count;
        }

        public void setSuite_count(String suite_count) {
            this.suite_count = suite_count;
        }

        public String getCase_count() {
            return case_count;
        }

        public void setCase_count(String case_count) {
            this.case_count = case_count;
        }
    }
}
