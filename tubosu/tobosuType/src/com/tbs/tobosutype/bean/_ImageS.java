package com.tbs.tobosutype.bean;

import java.io.Serializable;

/**
 * Created by Mr.Lin on 2017/11/14 10:14.
 * 单图的数据模型
 * 作用于逛图库的单图模式
 */

public class _ImageS implements Serializable {

    /**
     * id : 825994
     * cover_url : http://cdn111.dev.tobosu.com/impress_pic/2017-11-17/small/p_5a0e54b2153b9.png
     * image_width : 960
     * image_height : 540
     * style_id : 0
     * space_id : 0
     * image_url : http://cdn111.dev.tobosu.com/impress_pic/2017-11-17/p_5a0e54b2153b9.png
     * title : 装修效果图_土拨鼠装修
     * is_collect : 0
     * share_url : http://m.tobosu.com/pic/mt/825994.html
     */

    private String id;
    private String cover_url;
    private int image_width;
    private int image_height;
    private String style_id;
    private String space_id;
    private String image_url;
    private String title;
    private String is_collect;
    private String share_url;

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

    public String getStyle_id() {
        return style_id;
    }

    public void setStyle_id(String style_id) {
        this.style_id = style_id;
    }

    public String getSpace_id() {
        return space_id;
    }

    public void setSpace_id(String space_id) {
        this.space_id = space_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(String is_collect) {
        this.is_collect = is_collect;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }
}
