package com.tbs.tobosutype.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Lin on 2018/1/26 13:36.
 */

public class _UpdateInfo {

    /**
     * type : 2
     * content : ["1.测试android","2.测试android2","3.测试android3"]
     * apk_url : http://cdn111.dev.tobosu.com/app_version/2018-01-04/5a6998da71d74.apk
     * is_update : 1
     */

    private String type;
    private String apk_url;
    private String is_update;
    private ArrayList<String> content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getApk_url() {
        return apk_url;
    }

    public void setApk_url(String apk_url) {
        this.apk_url = apk_url;
    }

    public String getIs_update() {
        return is_update;
    }

    public void setIs_update(String is_update) {
        this.is_update = is_update;
    }

    public ArrayList<String> getContent() {
        return content;
    }

    public void setContent(ArrayList<String> content) {
        this.content = content;
    }
}
