package com.tobosu.mydecorate.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mr.Lin on 2017/6/9 08:55.
 */

public class _UserInfo {
    String name;//用户的账户名
    String gender;//用户的性别
    String decorate_type;//用户的装修阶段
    String icon;//用户的头像
    String city;//用户的所在的城市
    String uid;//用户的id

    public _UserInfo(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            this.name = jsonObject.getString("name");
            this.gender = jsonObject.getString("gender");
            this.decorate_type = jsonObject.getString("decorate_type");
            this.icon = jsonObject.getString("icon");
            this.city = jsonObject.getString("city");
            this.uid = jsonObject.getString("uid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDecorate_type() {
        return decorate_type;
    }

    public void setDecorate_type(String decorate_type) {
        this.decorate_type = decorate_type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
