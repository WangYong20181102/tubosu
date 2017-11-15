package com.tbs.tobosutype.bean;

/**
 * Created by Mr.Lin on 2017/11/10 14:54.
 * 套图的数据模型
 * 作用于逛图库的套图模式
 */

public class _ImageD {

    /**
     * id : 83433
     * cover_url : http://cdn111.dev.tobosu.com/impress_pic/2017-11-06/p_59fffc03e549e.jpg
     * image_width : 864
     * image_height : 600
     * title : 混搭小户型
     * designer_name : 今天星期一
     * designer_icon : http://aliyun.tbscache.com/res/common/images/icon/icon8.jpg
     * collect_count : 8
     * is_collect : 1
     */

    private String id;
    private String cover_url;//封面图
    private int image_width;//图片的宽高
    private int image_height;
    private String title;
    private String designer_name;
    private String designer_icon;
    private String collect_count;
    private String is_collect;

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
}
