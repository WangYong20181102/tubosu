package com.tbs.tbs_mj.bean;
import java.util.ArrayList;

public class CompanyProvinceBean {

    private String province_id;
    private String province_name;
    private ArrayList<CompanyCityBean> cityBeanList;

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public ArrayList<CompanyCityBean> getCityBeanList() {
        return cityBeanList;
    }

    public void setCityBeanList(ArrayList<CompanyCityBean> cityBeanList) {
        this.cityBeanList = cityBeanList;
    }

}
