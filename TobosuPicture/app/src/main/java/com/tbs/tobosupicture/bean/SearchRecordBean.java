package com.tbs.tobosupicture.bean;

/**
 * 搜索记录实体类
 * Created by Lie on 2017/7/21.
 */

public class SearchRecordBean {
    /**
     * city_id : 199
     * city_name : 深圳市
     * area_key : 1
     * area_value : 30㎡以下
     * layout_key : 2
     * layout_value : 2室
     * price_key : 3
     * price_value : 10~15万
     * style_id : 38
     * style_name : 现代简约
     */

    private String city_id;
    private String city_name;
    private String area_key;
    private String area_value;
    private String layout_key;
    private String layout_value;
    private String price_key;
    private String price_value;
    private String style_id;
    private String style_name;

    private String district_id;     //: "1",
    private String district_name;   //": "东城区"

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

    public String getArea_key() {
        return area_key;
    }

    public void setArea_key(String area_key) {
        this.area_key = area_key;
    }

    public String getArea_value() {
        return area_value;
    }

    public void setArea_value(String area_value) {
        this.area_value = area_value;
    }

    public String getLayout_key() {
        return layout_key;
    }

    public void setLayout_key(String layout_key) {
        this.layout_key = layout_key;
    }

    public String getLayout_value() {
        return layout_value;
    }

    public void setLayout_value(String layout_value) {
        this.layout_value = layout_value;
    }

    public String getPrice_key() {
        return price_key;
    }

    public void setPrice_key(String price_key) {
        this.price_key = price_key;
    }

    public String getPrice_value() {
        return price_value;
    }

    public void setPrice_value(String price_value) {
        this.price_value = price_value;
    }

    public String getStyle_id() {
        return style_id;
    }

    public void setStyle_id(String style_id) {
        this.style_id = style_id;
    }

    public String getStyle_name() {
        return style_name;
    }

    public void setStyle_name(String style_name) {
        this.style_name = style_name;
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
