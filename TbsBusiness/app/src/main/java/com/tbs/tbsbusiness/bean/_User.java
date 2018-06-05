package com.tbs.tbsbusiness.bean;

/**
 * Created by Mr.Lin on 2018/6/4 14:07.
 * 用户的基本属性
 */
public class _User {

    /**
     * uid : 23109
     * company_id : 33573
     * cellphone : 17520479595
     * cellphone_check : 1
     * nickname : 飞鱼装饰hh
     * icon : https://back.tobosu.com/app/2018-05-04/5aebd96ae5143.jpg
     * order_count : 146
     * grade : 0
     * city_name : 菏泽
     * province_name : 山东
     * new_order_count : 0
     * not_lf_order_count : 0
     * lf_order_count : 0
     * is_new_sms : 0
     */

    private String uid;
    private String company_id;
    private String cellphone;
    private String cellphone_check;
    private String nickname;
    private String icon;
    private int order_count;
    private int grade;
    private String city_name;
    private String province_name;
    private int new_order_count;
    private int not_lf_order_count;
    private int lf_order_count;
    private int is_new_sms;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getCellphone_check() {
        return cellphone_check;
    }

    public void setCellphone_check(String cellphone_check) {
        this.cellphone_check = cellphone_check;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getOrder_count() {
        return order_count;
    }

    public void setOrder_count(int order_count) {
        this.order_count = order_count;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public int getNew_order_count() {
        return new_order_count;
    }

    public void setNew_order_count(int new_order_count) {
        this.new_order_count = new_order_count;
    }

    public int getNot_lf_order_count() {
        return not_lf_order_count;
    }

    public void setNot_lf_order_count(int not_lf_order_count) {
        this.not_lf_order_count = not_lf_order_count;
    }

    public int getLf_order_count() {
        return lf_order_count;
    }

    public void setLf_order_count(int lf_order_count) {
        this.lf_order_count = lf_order_count;
    }

    public int getIs_new_sms() {
        return is_new_sms;
    }

    public void setIs_new_sms(int is_new_sms) {
        this.is_new_sms = is_new_sms;
    }
}
