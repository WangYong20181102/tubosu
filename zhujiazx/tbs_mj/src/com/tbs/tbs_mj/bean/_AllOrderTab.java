package com.tbs.tbs_mj.bean;

/**
 * Created by Mr.Lin on 2018/3/22 10:47.
 */

public class _AllOrderTab {
    private String OrderTabName;//标题
    private String OrderType;//tab对应的数字


    public _AllOrderTab() {

    }

    public _AllOrderTab(String OrderTabName, String OrderType) {
        this.OrderTabName = OrderTabName;
        this.OrderType = OrderType;
    }

    public String getOrderTabName() {
        return OrderTabName;
    }

    public void setOrderTabName(String orderTabName) {
        OrderTabName = orderTabName;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }
}
