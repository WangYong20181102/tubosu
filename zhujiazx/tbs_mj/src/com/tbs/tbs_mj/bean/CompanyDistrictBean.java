package com.tbs.tbs_mj.bean;


public class CompanyDistrictBean {

    private String district_id;
    private String district_name;

    public CompanyDistrictBean() {
    }

    public CompanyDistrictBean(String district_id, String district_name) {
        this.district_id = district_id;
        this.district_name = district_name;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }
}
