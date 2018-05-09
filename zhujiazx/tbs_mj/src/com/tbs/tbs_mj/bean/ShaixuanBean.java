package com.tbs.tbs_mj.bean;


public class ShaixuanBean {
    private String id;
    private String name;

    public ShaixuanBean() {
    }
    public ShaixuanBean(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
