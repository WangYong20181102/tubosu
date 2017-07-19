package com.tbs.tobosupicture.bean;

import java.util.ArrayList;

/**
 * Created by Lie on 2017/7/17.
 */

public class FactoryStyrlBean {
    /**
     * id : 5
     * class_name : 工装单图
     * child_data : []
     */

    public FactoryStyrlBean() {
    }

    public FactoryStyrlBean(String id, String class_name, ArrayList<ChildData> child_data) {
        this.id = id;
        this.class_name = class_name;
        this.child_data = child_data;
    }

    private String id;
    private String class_name;
    private ArrayList<ChildData> child_data = new ArrayList<ChildData>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public ArrayList<ChildData> getChild_data() {
        return child_data;
    }

    public void setChild_data(ArrayList<ChildData> child_data) {
        this.child_data = child_data;
    }
}