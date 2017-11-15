package com.tbs.tobosutype.bean;

/**
 * Created by Mr.Lin on 2017/11/14 10:14.
 * 单图的数据模型
 * 作用于逛图库的单图模式
 */

public class _ImageS {

    /**
     * id : 825969
     * cover_url : http://cdn111.dev.tobosu.com/impress_pic/2017-11-02/small/p_59fadeb229e47.png
     * image_width : 400
     * image_height : 400
     * image_url : http://cdn111.dev.tobosu.com/impress_pic/2017-11-02/p_59fadeb229e47.png
     */

    private String id;//单图的id
    private String cover_url;//封面图
    private int image_width;//图片的额宽度
    private int image_height;//图片的高度
    private String image_url;//大图的地址

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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
