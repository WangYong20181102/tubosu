package com.tbs.tobosutype.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mr.Lin on 2017/11/10 14:54.
 * 套图的数据模型
 * 作用于逛图库的套图模式
 */

public class _ImageD  implements Serializable {

    /**
     * id : 83438
     * cover_url : http://cdn111.dev.tobosu.com/impress_pic/2017-11-17/small/p_5a0e54afe295b.jpg
     * image_width : 800
     * image_height : 600
     * title :
     * designer_name : 陈小姐
     * designer_icon : http://opic.tbscache.com/users/sjs/logo/2013/12-30/11d11b27-e1ab-778b-8c90-e60d3c8d5a8c.jpeg
     * collect_count : 1
     * is_collect : 0
     * sub_images : ["http://cdn111.dev.tobosu.com/impress_pic/2017-11-17/p_5a0e54ab9bf45.jpg","http://cdn111.dev.tobosu.com/impress_pic/2017-11-17/p_5a0e54ad87d5f.jpg","http://cdn111.dev.tobosu.com/impress_pic/2017-11-17/p_5a0e54afe295b.jpg","http://cdn111.dev.tobosu.com/impress_pic/2017-11-17/p_5a0e54b2153b9.png"]
     */

    private String id;
    private String cover_url;
    private int image_width;
    private int image_height;
    private String title;
    private String designer_name;
    private String designer_icon;
    private String collect_count;
    private String is_collect;
    private String share_url;
    private List<String> sub_images;
    private String collect_id;

    public String getCollect_id() {
        return collect_id;
    }
    public void setCollect_id(String collect_id) {
        this.collect_id = collect_id;
    }

    private boolean seleteStatus;
    public boolean isSeleteStatus() {
        return seleteStatus;
    }
    public void setSeleteStatus(boolean seleteStatus) {
        this.seleteStatus = seleteStatus;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesigner_name() {
        return designer_name;
    }

    public void setDesigner_name(String designer_name) {
        this.designer_name = designer_name;
    }

    public String getDesigner_icon() {
        return designer_icon;
    }

    public void setDesigner_icon(String designer_icon) {
        this.designer_icon = designer_icon;
    }

    public String getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(String collect_count) {
        this.collect_count = collect_count;
    }

    public String getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(String is_collect) {
        this.is_collect = is_collect;
    }

    public List<String> getSub_images() {
        return sub_images;
    }

    public void setSub_images(List<String> sub_images) {
        this.sub_images = sub_images;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }
}
