package com.tbs.tobosupicture.bean;

import java.util.ArrayList;

/**
 * 效果图 json实体类
 * Created by Lie on 2017/8/3.
 */

public class XiaoGuoTuJsonEntity {


    /**
     * status : 200
     * msg : success
     * data : [{"id":"4092","img_url":"https://opic.tbscache.com/manage/case/2015/05-25/small/17f4a3ae-20de-abc6-c4d8-1fcaef3c680f.jpg","title":"新中式风格大户型家装案例鉴赏","designer_id":"272371","image_count":"4","plan_price":"14","click_count":"298","designer_icon":"http://opic.tbscache.com/users/sjs/logo/2015/06-19/small/4230c1e1-37c8-d2dd-9478-c071e0fd0ba6.jpg","city_name":"武汉","area_name":"120~180㎡"}]
     */

    private int status;
    private String msg;
    private ArrayList<XiaoGuoTu> data;

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

    public ArrayList<XiaoGuoTu> getData() {
        return data;
    }

    public void setData(ArrayList<XiaoGuoTu> data) {
        this.data = data;
    }

    public static class XiaoGuoTu {
        /**
         * id : 4092
         * img_url : https://opic.tbscache.com/manage/case/2015/05-25/small/17f4a3ae-20de-abc6-c4d8-1fcaef3c680f.jpg
         * title : 新中式风格大户型家装案例鉴赏
         * designer_id : 272371
         * image_count : 4
         * plan_price : 14
         * click_count : 298
         * designer_icon : http://opic.tbscache.com/users/sjs/logo/2015/06-19/small/4230c1e1-37c8-d2dd-9478-c071e0fd0ba6.jpg
         * city_name : 武汉
         * area_name : 120~180㎡
         */

        private String id;
        private String img_url;
        private String title;
        private String designer_id;
        private String image_count;
        private String plan_price;
        private String click_count;
        private String designer_icon;
        private String city_name;
        private String area_name;

        // 叫昭仲给我补上的
        private String style_name;  //风格名称
        private String layout_name;  //户型

        public String getStyle_name() {
            return style_name;
        }

        public void setStyle_name(String style_name) {
            this.style_name = style_name;
        }

        public String getLayout() {
            return layout_name;
        }

        public void setLayout(String layout) {
            this.layout_name = layout;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesigner_id() {
            return designer_id;
        }

        public void setDesigner_id(String designer_id) {
            this.designer_id = designer_id;
        }

        public String getImage_count() {
            return image_count;
        }

        public void setImage_count(String image_count) {
            this.image_count = image_count;
        }

        public String getPlan_price() {
            return plan_price;
        }

        public void setPlan_price(String plan_price) {
            this.plan_price = plan_price;
        }

        public String getClick_count() {
            return click_count;
        }

        public void setClick_count(String click_count) {
            this.click_count = click_count;
        }

        public String getDesigner_icon() {
            return designer_icon;
        }

        public void setDesigner_icon(String designer_icon) {
            this.designer_icon = designer_icon;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public String getArea_name() {
            return area_name;
        }

        public void setArea_name(String area_name) {
            this.area_name = area_name;
        }
    }
}
