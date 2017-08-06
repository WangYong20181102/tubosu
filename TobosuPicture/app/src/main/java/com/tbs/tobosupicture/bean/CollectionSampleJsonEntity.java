package com.tbs.tobosupicture.bean;

import java.util.ArrayList;

/**
 * Created by Lie on 2017/8/4.
 */

public class CollectionSampleJsonEntity {


    /**
     * status : 200
     * msg : success
     * data : [{"title":"保利中央公馆现代简约时尚三居室家装设计图","id":"5632","img_url":"https://opic.tbscache.com/manage/case/2015/06-19/small/836beffe-1b68-dc1a-288b-9b567d836883.jpg","village":"保利中央公馆","designer_id":"272810","click_count":"299","plan_price":"8","designer_icon":"http://opic.tbscache.com/users/sjs/logo/2015/06-18/ebf7fcb5-35a4-e97d-ce36-5a60b2611395.jpg","area_name":"100㎡","city_name":"佛山"},{"title":"现代简约三居室","id":"4478","img_url":"https://opic.tbscache.com/manage/case/2015/05-31/small/3fa9b71d-75dc-2a50-c76f-2ad1c99cfd21.jpg","village":"0","designer_id":"271567","click_count":"281","plan_price":"28","designer_icon":"http://opic.tbscache.com/users/sjs/logo/2015/04-14/cut_83169aa1-f0b9-dbaf-2484-ae46725e414b.jpg","area_name":"","city_name":"广州"},{"title":"港式奢华风格新世界花园160平大户型装修案例图 ","id":"6040","img_url":"https://opic.tbscache.com/manage/case/2015/06-27/small/e70e2ebc-8ed4-f84a-d3d8-1dc721394cfe.jpg","village":"新世界花园","designer_id":"187899","click_count":"300","plan_price":"60","designer_icon":"http://opic.tbscache.com/users/sjs/logo/2015/06-27/cut_0648f67e-0c28-2072-d196-97608289b5c8.jpg","area_name":"120~180㎡","city_name":"沈阳"},{"title":"C Park 天悦城三居室122.6简欧装修设计效果图","id":"71080","img_url":"https://pic.tbscache.com/impress_pic/2017-05-25/p_59269ff7a7045.jpg","village":"C Park 天悦城","designer_id":"213266","click_count":"0","plan_price":"11","designer_icon":"http://opic.tbscache.com/users/sjs/logo/2014/04-17/small/831513f9-f6b4-5044-96ad-cd91beb183bf.jpg","area_name":"120~180㎡","city_name":"长沙"},{"title":"荣悦台欧式风格三居室装修设计效果图","id":"71010","img_url":"https://pic.tbscache.com/impress_pic/2017-05-25/p_592642e4a1bf6.jpg","village":"荣悦台","designer_id":"213266","click_count":"0","plan_price":"12","designer_icon":"http://opic.tbscache.com/users/sjs/logo/2014/04-17/small/831513f9-f6b4-5044-96ad-cd91beb183bf.jpg","area_name":"110㎡","city_name":"长沙"},{"title":"北欧风格复式装修实景案例","id":"70004","img_url":"https://pic.tbscache.com/impress_pic/2017-05-13/p_5916b72b71a09.jpg","village":"澳海澜庭","designer_id":"283167","click_count":"0","plan_price":"15","designer_icon":"https://pic.tbscache.com/company_logo/2017-05-13/thumb_5916a2e3a6613.png","area_name":"120~180㎡","city_name":"长沙"},{"title":"铂翠湾小区126平方港式现代风格四居室装修效果图","id":"70752","img_url":"https://pic.tbscache.com/impress_pic/2017-05-22/p_5922910dce8bd.jpg","village":"","designer_id":"283247","click_count":"0","plan_price":"14","designer_icon":"https://pic.tbscache.com/company_logo/2017-05-19/thumb_591eb4bae2ec0.jpg","area_name":"120~180㎡","city_name":"宁波"},{"title":"西堤阳光效果图","id":"52895","img_url":"https://pic.tbscache.com/impress_pic/2016-10-22/p_580af5b951081.jpg","village":"西堤阳光","designer_id":"276011","click_count":"0","plan_price":"11","designer_icon":"http://pic.tbscache.com/company_logo/2016-10-14/thumb_580041be54619.jpg","area_name":"120~180㎡","city_name":"宁波"},{"title":"盛世天城120平米简欧风格二居室装修效果图","id":"70715","img_url":"https://pic.tbscache.com/impress_pic/2017-05-21/p_592156d881ea8.jpg","village":"盛世 天城","designer_id":"283229","click_count":"0","plan_price":"25","designer_icon":"https://pic.tbscache.com/company_logo/2017-05-19/thumb_591e48778c4ac.jpg","area_name":"120~180㎡","city_name":"宁波"},{"title":"印象外滩新古典风格三居室装修效果图","id":"70709","img_url":"https://pic.tbscache.com/impress_pic/2017-05-21/p_592152b358ecc.jpg","village":"印象外滩","designer_id":"283221","click_count":"0","plan_price":"22","designer_icon":"https://pic.tbscache.com/company_logo/2017-05-18/thumb_591d662cba738.jpg","area_name":"120~180㎡","city_name":"宁波"}]
     */

    private int status;
    private String msg;
    private ArrayList<CollectionSampleImg> data;

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

    public ArrayList<CollectionSampleImg> getData() {
        return data;
    }

    public void setData(ArrayList<CollectionSampleImg> data) {
        this.data = data;
    }

    public static class CollectionSampleImg {
        /**
         * title : 保利中央公馆现代简约时尚三居室家装设计图
         * id : 5632
         * img_url : https://opic.tbscache.com/manage/case/2015/06-19/small/836beffe-1b68-dc1a-288b-9b567d836883.jpg
         * village : 保利中央公馆
         * designer_id : 272810
         * click_count : 299
         * plan_price : 8
         * designer_icon : http://opic.tbscache.com/users/sjs/logo/2015/06-18/ebf7fcb5-35a4-e97d-ce36-5a60b2611395.jpg
         * area_name : 100㎡
         * city_name : 佛山
         */

        private String title;
        private String id;
        private String img_url;
        private String village;
        private String designer_id;
        private String click_count;
        private String plan_price;
        private String designer_icon;
        private String area_name;
        private String city_name;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getVillage() {
            return village;
        }

        public void setVillage(String village) {
            this.village = village;
        }

        public String getDesigner_id() {
            return designer_id;
        }

        public void setDesigner_id(String designer_id) {
            this.designer_id = designer_id;
        }

        public String getClick_count() {
            return click_count;
        }

        public void setClick_count(String click_count) {
            this.click_count = click_count;
        }

        public String getPlan_price() {
            return plan_price;
        }

        public void setPlan_price(String plan_price) {
            this.plan_price = plan_price;
        }

        public String getDesigner_icon() {
            return designer_icon;
        }

        public void setDesigner_icon(String designer_icon) {
            this.designer_icon = designer_icon;
        }

        public String getArea_name() {
            return area_name;
        }

        public void setArea_name(String area_name) {
            this.area_name = area_name;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }
    }
}
