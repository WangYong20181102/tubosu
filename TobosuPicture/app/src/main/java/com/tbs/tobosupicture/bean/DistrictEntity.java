package com.tbs.tobosupicture.bean;

/**
 * 小区 实体类
 * Created by Lie on 2017/7/31.
 */

public class DistrictEntity {

    private String id;
    private String name;
    private String image_url;

    public DistrictEntity(){

    }

    public DistrictEntity(String id,String name,String image_url){
        this.id = id;
        this.name = name;
        this.image_url = image_url;
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

}
