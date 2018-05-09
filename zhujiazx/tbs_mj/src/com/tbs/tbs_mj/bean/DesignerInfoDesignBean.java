package com.tbs.tbs_mj.bean;

import java.util.List;

public class DesignerInfoDesignBean {

    private String id;            //": "11962",  设计
    private String cover_url;     //": "https://back.tobosu.com/impress_pic/2015-10-05/small/p_561227b78e579.jpg",
    private String title;         //": "现代简约/大户型/120~180平",
    private List<String> sub_imagesList;    //

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getSub_imagesList() {
        return sub_imagesList;
    }

    public void setSub_imagesList(List<String> sub_imagesList) {
        this.sub_imagesList = sub_imagesList;
    }
}
