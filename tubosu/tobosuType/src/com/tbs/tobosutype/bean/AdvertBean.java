package com.tbs.tobosutype.bean;

/**
 * Created by Mr.Wang on 2018/11/23 09:53.
 */
public class AdvertBean {
    private String id;
    private String img_url;
    private String jump_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getJump_url() {
        return jump_url;
    }

    public void setJump_url(String jump_url) {
        this.jump_url = jump_url;
    }

    @Override
    public String toString() {
        return "AdvertBean{" +
                "id='" + id + '\'' +
                ", img_url='" + img_url + '\'' +
                ", jump_url='" + jump_url + '\'' +
                '}';
    }
}
