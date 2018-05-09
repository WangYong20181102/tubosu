package com.tbs.tbs_mj.bean;

/**
 * Created by Lie on 2017/10/15.
 */

public class TaotuEntity  {

//    "id": "83418",
//    "collect_id": "12560",
//    "cover_url": "https://st.hzcdn.com/simgs/5a3106d3072b5809_9-1700/farmhouse-bedroom.jpg",
//    "image_width": 600,
//    "image_height": 600,
//    "title": "混搭小面积"

    private String id;
    private String collect_id;
    private String cover_url;
    private int image_width;
    private int image_height;
    private String title;
    private boolean seleteStatus;

    public TaotuEntity() {}

    public TaotuEntity(String id, String collect_id, String cover_url, int image_width, int image_height, String title, boolean seleteStatus) {
        this.id = id;
        this.collect_id = collect_id;
        this.cover_url = cover_url;
        this.image_width = image_width;
        this.image_height = image_height;
        this.title = title;
        this.seleteStatus = seleteStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCollect_id() {
        return collect_id;
    }

    public void setCollect_id(String collect_id) {
        this.collect_id = collect_id;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public int getImage_width() {
        return image_width;
    }

    public void setImage_width(int image_width) {
        this.image_width = image_width;
    }

    public int getImage_height() {
        return image_height;
    }

    public void setImage_height(int image_height) {
        this.image_height = image_height;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSeleteStatus() {
        return seleteStatus;
    }

    public void setSeleteStatus(boolean seleteStatus) {
        this.seleteStatus = seleteStatus;
    }
}
