package com.tbs.tobosupicture.bean;

import java.util.ArrayList;

/**
 * Created by Lie on 2017/8/12.
 */

public class CollectCaseJsonEntity {


    /**
     * status : 200
     * msg : success
     * data : [{"title":"滨湖假日金榈园","img_url":"https://pic.tbscache.com/impress_pic/2017-05-31/p_592e3506b0d6b.jpg","caseid":"15573","owner_name":"王总","price":"58","designer_id":"0","desmethod":"全包","shi":"","ting":"","wei":"","area":"240","district_name":"金榈园","style_name":"","designer_name":"","designer_icon":"","spec_info":"240㎡"},{"title":"龙湖滟澜山","img_url":"https://pic.tbscache.com/impress_pic/2017-05-30/p_592d19a72f511.jpg","caseid":"15582","owner_name":"陈女士","price":"9","designer_id":"0","desmethod":"半包","shi":"","ting":"","wei":"","area":"125","district_name":"龙湖滟澜山","style_name":"","designer_name":"","designer_icon":"","spec_info":"125㎡"},{"title":"滨湖万达三期","img_url":"https://pic.tbscache.com/impress_pic/2017-05-28/p_592a31acaf485.jpg","caseid":"15584","owner_name":"叶女士","price":"7","designer_id":"0","desmethod":"全包","shi":"","ting":"","wei":"","area":"110","district_name":"万达城揽湖苑","style_name":"","designer_name":"","designer_icon":"","spec_info":"110㎡"}]
     */

    private int status;
    private String msg;
    private ArrayList<CollectCaseEntity> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<CollectCaseEntity> getData() {
        return data;
    }

    public void setData(ArrayList<CollectCaseEntity> data) {
        this.data = data;
    }

    public static class CollectCaseEntity {
        /**
         * title : 滨湖假日金榈园
         * img_url : https://pic.tbscache.com/impress_pic/2017-05-31/p_592e3506b0d6b.jpg
         * caseid : 15573
         * owner_name : 王总
         * price : 58
         * designer_id : 0
         * desmethod : 全包
         * shi :
         * ting :
         * wei :
         * area : 240
         * district_name : 金榈园
         * style_name :
         * designer_name :
         * designer_icon :
         * spec_info : 240㎡
         */

        private String title;
        private String img_url;
        private String caseid;
        private String owner_name;
        private String price;
        private String designer_id;
        private String desmethod;
        private String shi;
        private String ting;
        private String wei;
        private String area;
        private String district_name;
        private String style_name;
        private String designer_name;
        private String designer_icon;
        private String spec_info;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getCaseid() {
            return caseid;
        }

        public void setCaseid(String caseid) {
            this.caseid = caseid;
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

        public String getDesigner_id() {
            return designer_id;
        }

        public void setDesigner_id(String designer_id) {
            this.designer_id = designer_id;
        }

        public String getDesmethod() {
            return desmethod;
        }

        public void setDesmethod(String desmethod) {
            this.desmethod = desmethod;
        }

        public String getShi() {
            return shi;
        }

        public void setShi(String shi) {
            this.shi = shi;
        }

        public String getTing() {
            return ting;
        }

        public void setTing(String ting) {
            this.ting = ting;
        }

        public String getWei() {
            return wei;
        }

        public void setWei(String wei) {
            this.wei = wei;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getDistrict_name() {
            return district_name;
        }

        public void setDistrict_name(String district_name) {
            this.district_name = district_name;
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

        public String getSpec_info() {
            return spec_info;
        }

        public void setSpec_info(String spec_info) {
            this.spec_info = spec_info;
        }
    }
}
