package com.tbs.tbs_mj.bean;

/**
 * Created by Lie on 2017/10/16.
 */
public class DantuEntity {

//    "id": "825900",
//    "collect_id": "12557",
//    "cover_url": "https://st.hzcdn.com/simgs/0b11743c072b5803_9-1700/modern-living-room.jpg",
//    "image_width": 600,
//    "image_height": 600

    private String id;
    private String collect_id;
    private String cover_url;
    private int image_width;
    private int image_height;
    private boolean isSelected;

//    private String id;         0
//    private String cover_url;  0
//    private int image_width;   0
//    private int image_height;  0
//    private String style_id;   1
//    private String space_id;   1
//    private String image_url;  1
//    private String title;      1
//    private String is_collect; 0
//    private String share_url;  1


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

    public boolean getSeleteStatus() {
        return isSelected;
    }

    public void setSeleteStatus(boolean selected) {
        isSelected = selected;
    }
}
