package com.tbs.tbs_mj.bean;

import java.util.List;

/**
 * Created by Mr.Lin on 2018/3/24 15:47.
 * 订单详情实体类
 * 作用于3.7.2版本新增的查看订单详情
 */

public class _OrderDetail {

    /**
     * id : 497412
     * order_id : 2303033
     * add_time : 2018-03-15
     * state : 4
     * is_give : 0
     * house_type : 普通住宅
     * area : 154㎡
     * community_name : 小区名字
     * owner_name : 先生
     * cellphone : 13882039626
     * address : 成都 蓝润ISC公寓 客户最近在考虑装修方面的东西，麻烦联系下
     * lf_time :
     * decorate_time : 2018-03-23
     * is_old_house : 0
     * is_get_key : 0
     * decorate_style : 请选择
     * decorate_type : 请选择
     * decorate_mode : 请选择
     * order_demand : 广东省深圳市罗湖区，请选择，普通住宅，新房装修，装修风格: 请选择，建筑面积154平米，1室1厅1卫，无钥匙，请选择装修，预算 3万以下，量房时间：2018-03-12 18:25:53，装修时间：2018-03-23 00:00:00，联系时请告知是土拨鼠推荐的。
     * budget : 1.5
     * district_name : 罗湖区
     * layout_name : 1室1厅1卫
     * company_list : [{"company_name":"发财猫","state":2},{"company_name":"测试小cy","state":2},{"company_name":"cc很任性","state":2}]
     * order_track : [{"title":"已签单","time":"2018-03-23 17:34:08"},{"title":"已量房","time":"2018-03-23 17:32:39"},{"title":"接收分单","time":"2018-03-23 16:58:21"},{"title":"分单时间","time":"2018-03-15 00:00:00"}]
     */

    private String id;
    private String order_id;
    private String add_time;
    private String state;
    private String is_give;
    private String house_type;
    private String area;
    private String community_name;
    private String owner_name;
    private String cellphone;
    private String address;
    private String lf_time;
    private String decorate_time;
    private String is_old_house;
    private String is_get_key;
    private String decorate_style;
    private String decorate_type;
    private String decorate_mode;
    private String order_demand;
    private String budget;
    private String district_name;
    private String layout_name;
    private List<CompanyListBean> company_list;
    private List<OrderTrackBean> order_track;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIs_give() {
        return is_give;
    }

    public void setIs_give(String is_give) {
        this.is_give = is_give;
    }

    public String getHouse_type() {
        return house_type;
    }

    public void setHouse_type(String house_type) {
        this.house_type = house_type;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCommunity_name() {
        return community_name;
    }

    public void setCommunity_name(String community_name) {
        this.community_name = community_name;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLf_time() {
        return lf_time;
    }

    public void setLf_time(String lf_time) {
        this.lf_time = lf_time;
    }

    public String getDecorate_time() {
        return decorate_time;
    }

    public void setDecorate_time(String decorate_time) {
        this.decorate_time = decorate_time;
    }

    public String getIs_old_house() {
        return is_old_house;
    }

    public void setIs_old_house(String is_old_house) {
        this.is_old_house = is_old_house;
    }

    public String getIs_get_key() {
        return is_get_key;
    }

    public void setIs_get_key(String is_get_key) {
        this.is_get_key = is_get_key;
    }

    public String getDecorate_style() {
        return decorate_style;
    }

    public void setDecorate_style(String decorate_style) {
        this.decorate_style = decorate_style;
    }

    public String getDecorate_type() {
        return decorate_type;
    }

    public void setDecorate_type(String decorate_type) {
        this.decorate_type = decorate_type;
    }

    public String getDecorate_mode() {
        return decorate_mode;
    }

    public void setDecorate_mode(String decorate_mode) {
        this.decorate_mode = decorate_mode;
    }

    public String getOrder_demand() {
        return order_demand;
    }

    public void setOrder_demand(String order_demand) {
        this.order_demand = order_demand;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getLayout_name() {
        return layout_name;
    }

    public void setLayout_name(String layout_name) {
        this.layout_name = layout_name;
    }

    public List<CompanyListBean> getCompany_list() {
        return company_list;
    }

    public void setCompany_list(List<CompanyListBean> company_list) {
        this.company_list = company_list;
    }

    public List<OrderTrackBean> getOrder_track() {
        return order_track;
    }

    public void setOrder_track(List<OrderTrackBean> order_track) {
        this.order_track = order_track;
    }

    public static class CompanyListBean {
        /**
         * company_name : 发财猫
         * state : 2
         */

        private String company_name;
        private int state;

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }

    public static class OrderTrackBean {
        /**
         * title : 已签单
         * time : 2018-03-23 17:34:08
         */

        private String title;
        private String time;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
