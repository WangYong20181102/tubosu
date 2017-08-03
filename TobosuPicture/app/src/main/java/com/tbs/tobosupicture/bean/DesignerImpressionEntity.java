package com.tbs.tobosupicture.bean;

/**
 * Created by Lie on 2017/7/18.
 */

public class DesignerImpressionEntity {
    /**
     * id : 4092
     * img_url : https://opic.tbscache.com/manage/case/2015/05-25/small/17f4a3ae-20de-abc6-c4d8-1fcaef3c680f.jpg
     * image_count : 4
     * class_id : 0
     * plan_price : 14
     * collect_count:   收藏数
     * class_name :
     * style_name : 新中式
     * layout_name : 大户型
     */

    private String id;                               // 1  "id":"6040",
    private String img_url;                          // 1  "img_url":"https://opic.tbscache.com/manage/case/2015/06-27/small/e70e2ebc-8ed4-f84a-d3d8-1dc721394cfe.jpg",
    private String image_count;                      //   "title":"港式奢华风格新世界花园160平大户型装修案例图 ",
    private String class_id;                         //   "designer_id":"187899",
    private String plan_price;                       // 1  "image_count":"7",
    private String class_name;                       // 1  "plan_price":"60",
    private String collect_count;                    //   "click_count":"300",
    private String style_name;                       //   "designer_icon":"http://opic.tbscache.com/users/sjs/logo/2015/06-27/cut_0648f67e-0c28-2072-d196-97608289b5c8.jpg",
    private String is_collect;                       //   "city_name":"沈阳",
    private String layout_name;                      //   "area_name":"120~180㎡"

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

    public String getImage_count() {
        return image_count;
    }

    public void setImage_count(String image_count) {
        this.image_count = image_count;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getPlan_price() {
        return plan_price;
    }

    public void setPlan_price(String plan_price) {
        this.plan_price = plan_price;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getStyle_name() {
        return style_name;
    }

    public void setStyle_name(String style_name) {
        this.style_name = style_name;
    }

    public String getLayout_name() {
        return layout_name;
    }

    public void setLayout_name(String layout_name) {
        this.layout_name = layout_name;
    }

    public String getCollect_count() {
        return collect_count;
    }

    public String getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(String is_collect) {
        this.is_collect = is_collect;
    }

    public void setCollect_count(String collect_count) {
        this.collect_count = collect_count;
    }
}
