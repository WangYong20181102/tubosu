package com.tbs.tobosupicture.bean;

import java.util.ArrayList;

/**
 * Created by Lie on 2017/7/27.
 */

public class SeeImgEntity {
    private String suite_id;
    private String is_collect;
    private ArrayList<ImgEntity> ImgEntityList;

    public String getSuite_id() {
        return suite_id;
    }

    public void setSuite_id(String suite_id) {
        this.suite_id = suite_id;
    }

    public String getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(String is_collect) {
        this.is_collect = is_collect;
    }

    public ArrayList<ImgEntity> getImgEntityList() {
        return ImgEntityList;
    }

    public void setImgEntityList(ArrayList<ImgEntity> imgEntityList) {
        ImgEntityList = imgEntityList;
    }
}
