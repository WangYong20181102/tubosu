package com.tbs.tobosutype.bean;

import java.util.ArrayList;

public class CompanyCityBean {

    private String city_id;
    private String city_name;
    private ArrayList<CompanyDistrictBean> disctBeanList;

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public ArrayList<CompanyDistrictBean> getDisctBeanList() {
        return disctBeanList;
    }

    public void setDisctBeanList(ArrayList<CompanyDistrictBean> disctBeanList) {
        this.disctBeanList = disctBeanList;
    }
}
