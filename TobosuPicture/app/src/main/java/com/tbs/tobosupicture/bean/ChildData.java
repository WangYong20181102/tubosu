package com.tbs.tobosupicture.bean;

/**
 * Created by Lie on 2017/7/17.
 */

public class ChildData {
    private String id;
    private String parent_id;
    private String class_name;
    private String event_name;

    public ChildData(){}
    public ChildData(String id, String parent_id,String class_name, String event_name){
        this.id = id;
        this.class_name = class_name;
        this.parent_id = parent_id;
        this.event_name = event_name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }
}
