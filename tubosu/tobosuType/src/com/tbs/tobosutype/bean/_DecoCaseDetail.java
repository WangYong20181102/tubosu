package com.tbs.tobosutype.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Lin on 2017/10/25 09:58.
 * 案例详情的实体类  作用于DecoCaseAdapter
 */

public class _DecoCaseDetail {

    /**
     * id : 15353
     * cover_url : https://pic.tbscache.com/impress_pic/2017-05-05/small/p_590bd676a49f7.jpg
     * city_name : 天津
     * desc : 简约主义的本质就是取消外表多余的浮华以显露家原本的特性，由简单的形象或符号来构筑空间;在结构上，简化结构体系，精简结构构件，讲究结构逻辑，使之产生没有屏障，或屏障极少的建筑空间
     * community_name : 大都会
     * owner_name :
     * price : 22
     * desmethod : 全包
     * area : 120
     * province_name : 天津
     * layout_name : 2室1厅1卫
     * style_name : 简约
     * designer_name : 刘长建
     * designer_icon : https://pic.tbscache.com/company_logo/2017-05-05/thumb_590bd58b5ddcc.jpg
     * designer_position : 设计师
     * space_info : [{"img_url":"https://pic.tbscache.com/apartment/2017-03-23/small/p_58d37ea0080af.jpg","space_name":"户型图","thumb_img_url":"https://pic.tbscache.com/apartment/2017-03-23/small/p_58d37ea0080af.jpg"},{"img_url":"https://pic.tbscache.com/impress_pic/2017-05-05/p_590bd67241c7a.jpg","space_name":"书房","thumb_img_url":"https://pic.tbscache.com/impress_pic/2017-05-05/small/p_590bd67241c7a.jpg"},{"img_url":"https://pic.tbscache.com/impress_pic/2017-05-05/p_590bd672a2046.jpg","space_name":"餐厅","thumb_img_url":"https://pic.tbscache.com/impress_pic/2017-05-05/small/p_590bd672a2046.jpg"},{"img_url":"https://pic.tbscache.com/impress_pic/2017-05-05/p_590bd6730cde3.jpg","space_name":"餐厅","thumb_img_url":"https://pic.tbscache.com/impress_pic/2017-05-05/small/p_590bd6730cde3.jpg"},{"img_url":"https://pic.tbscache.com/impress_pic/2017-05-05/p_590bd6736e390.jpg","space_name":"餐厅","thumb_img_url":"https://pic.tbscache.com/impress_pic/2017-05-05/small/p_590bd6736e390.jpg"},{"img_url":"https://pic.tbscache.com/impress_pic/2017-05-05/p_590bd6743b25b.jpg","space_name":"家装","thumb_img_url":"https://pic.tbscache.com/impress_pic/2017-05-05/small/p_590bd6743b25b.jpg"},{"img_url":"https://pic.tbscache.com/impress_pic/2017-05-05/p_590bd674975d7.jpg","space_name":"餐厅","thumb_img_url":"https://pic.tbscache.com/impress_pic/2017-05-05/small/p_590bd674975d7.jpg"},{"img_url":"https://pic.tbscache.com/impress_pic/2017-05-05/p_590bd6757936a.jpg","space_name":"客厅","thumb_img_url":"https://pic.tbscache.com/impress_pic/2017-05-05/small/p_590bd6757936a.jpg"},{"img_url":"https://pic.tbscache.com/impress_pic/2017-05-05/p_590bd675d86bb.jpg","space_name":"阳台","thumb_img_url":"https://pic.tbscache.com/impress_pic/2017-05-05/small/p_590bd675d86bb.jpg"},{"img_url":"https://pic.tbscache.com/impress_pic/2017-05-05/p_590bd6765227a.jpg","space_name":"书房","thumb_img_url":"https://pic.tbscache.com/impress_pic/2017-05-05/small/p_590bd6765227a.jpg"},{"img_url":"https://pic.tbscache.com/impress_pic/2017-05-05/p_590bd676a49f7.jpg","space_name":"客厅","thumb_img_url":"https://pic.tbscache.com/impress_pic/2017-05-05/small/p_590bd676a49f7.jpg"}]
     */

    private String id;
    private String cover_url;
    private String city_name;
    private String desc;
    private String community_name;
    private String owner_name;
    private String price;
    private String desmethod;
    private String area;
    private String province_name;
    private String layout_name;
    private String style_name;
    private String designer_name;
    private String designer_icon;
    private String designer_position;
    private ArrayList<SpaceInfoBean> space_info;

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

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCommunity_name() {
        return community_name;
    }

    public void setCommunity_name(String community_name) {
        this.community_name = community_name;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesmethod() {
        return desmethod;
    }

    public void setDesmethod(String desmethod) {
        this.desmethod = desmethod;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public String getLayout_name() {
        return layout_name;
    }

    public void setLayout_name(String layout_name) {
        this.layout_name = layout_name;
    }

    public String getStyle_name() {
        return style_name;
    }

    public void setStyle_name(String style_name) {
        this.style_name = style_name;
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

    public String getDesigner_position() {
        return designer_position;
    }

    public void setDesigner_position(String designer_position) {
        this.designer_position = designer_position;
    }

    public List<SpaceInfoBean> getSpace_info() {
        return space_info;
    }

    public void setSpace_info(ArrayList<SpaceInfoBean> space_info) {
        this.space_info = space_info;
    }

    public static class SpaceInfoBean {
        /**
         * img_url : https://pic.tbscache.com/apartment/2017-03-23/small/p_58d37ea0080af.jpg
         * space_name : 户型图
         * thumb_img_url : https://pic.tbscache.com/apartment/2017-03-23/small/p_58d37ea0080af.jpg
         */

        private String img_url;//大图
        private String space_name;
        private String thumb_img_url;//缩略图

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getSpace_name() {
            return space_name;
        }

        public void setSpace_name(String space_name) {
            this.space_name = space_name;
        }

        public String getThumb_img_url() {
            return thumb_img_url;
        }

        public void setThumb_img_url(String thumb_img_url) {
            this.thumb_img_url = thumb_img_url;
        }
    }
}
