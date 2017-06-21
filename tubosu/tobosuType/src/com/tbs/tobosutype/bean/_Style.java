package com.tbs.tobosutype.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mr.Lin on 2017/4/25 11:16.
 */

public class _Style {
    public _Style() {
    }

    public _Style(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            this.parent_id = jsonObject.getString("parent_id");
            this.id = jsonObject.getString("id");
            this.class_name = jsonObject.getString("class_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String parent_id;
    private String id;
    private String class_name;

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

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
}
