package com.tbs.tobosutype.bean;

/**
 * Created by Mr.Lin on 2018/3/22 11:38.
 * 订单的子项
 */

public class _OrderItem {

    /**
     * id : 497446
     * order_id : 2278513
     * add_time : 2018-03-16
     * state : 1
     * is_give : 0
     * district_name : 福田区
     * house_type : 1
     * layout_name : 5室2厅2卫
     * area : 155㎡
     * community_name : 长泰花园
     */

    private String id;
    private String order_id;
    private String add_time;
    private String state;
    private String is_give;
    private String district_name;
    private String house_type;
    private String layout_name;
    private String area;
    private String community_name;
    private String cellphone;

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

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getHouse_type() {
        return house_type;
    }

    public void setHouse_type(String house_type) {
        this.house_type = house_type;
    }

    public String getLayout_name() {
        return layout_name;
    }

    public void setLayout_name(String layout_name) {
        this.layout_name = layout_name;
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

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }
}
