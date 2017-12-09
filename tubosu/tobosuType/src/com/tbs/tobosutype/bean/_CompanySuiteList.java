package com.tbs.tobosutype.bean;

import java.util.List;

/**
 * Created by Mr.Lin on 2017/12/5 15:29.
 * 设计方案列表子项
 */

public class _CompanySuiteList {

    /**
     * id : 55948
     * cover_url : https://back.tobosu.com/impress_pic/2016-11-20/p_58313d7e63b86.jpg
     * designer_id : 276806
     * title : 欧式 / 公寓 / 120~180㎡ / 5.7万
     * designer_name : 杨威
     * designer_icon : http://back.tobosu.com/company_logo/2016-04-27/thumb_57201b2b2dfd6.jpg
     * company_name : 常州天叶设计
     * is_collect : 0
     * share_url : http://m.dev.tobosu.com/pic/tc/55948.html
     * sub_images : ["https://back.tobosu.com/impress_pic/2016-11-20/p_58313d73e1cdb.jpg","https://back.tobosu.com/impress_pic/2016-11-20/p_58313d77808cf.jpg","https://back.tobosu.com/impress_pic/2016-11-20/p_58313d7b21b66.jpg","https://back.tobosu.com/impress_pic/2016-11-20/p_58313d7e63b86.jpg"]
     */

    private String id;
    private String cover_url;
    private String designer_id;
    private String title;
    private String designer_name;
    private String designer_icon;
    private String company_name;
    private String is_collect;
    private String share_url;
    private List<String> sub_images;

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

    public String getDesigner_id() {
        return designer_id;
    }

    public void setDesigner_id(String designer_id) {
        this.designer_id = designer_id;
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

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
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

    public List<String> getSub_images() {
        return sub_images;
    }

    public void setSub_images(List<String> sub_images) {
        this.sub_images = sub_images;
    }
}
