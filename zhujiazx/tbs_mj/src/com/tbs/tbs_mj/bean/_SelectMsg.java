package com.tbs.tbs_mj.bean;

/**
 * Created by Mr.Lin on 2017/11/13 09:59.
 */

public class _SelectMsg {
    private String id;//选择条件的id
    private String name;//选择条件的名称
    private String value;//颜色类型

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
