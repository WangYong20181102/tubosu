package com.tbs.tobosupicture.bean;

import com.tbs.tobosupicture.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/** 样板图 实体类
 * Created by Lie on 2017/7/15.
 */

public class SamplePicBeanEntity {

    private String id;
    private String img_url;
    private String designer_id;
    private String image_count;
    private String plan_price;
    //    private String village;
    private String click_count;
    private String designer_icon;
    private String city_name;
    private String area_name;
    private String title;

    /**
     * id : 6040
     * img_url : https://opic.tbscache.com/manage/case/2015/06-27/small/e70e2ebc-8ed4-f84a-d3d8-1dc721394cfe.jpg
     * designer_id : 187899
     * image_count : 7
     * plan_price : 60
     * village : 新世界花园
     * click_count : 300
     * designer_icon : http://opic.tbscache.com/users/sjs/logo/2015/06-27/cut_0648f67e-0c28-2072-d196-97608289b5c8.jpg
     * city_name : 沈阳
     * area_name : 120~180㎡
     * title : 新世界花园现代欧式大户型
     */

    public SamplePicBeanEntity(){}

    public SamplePicBeanEntity(JSONObject object){
        try {
            this.id = object.getString("id");                         //  "title": "现代简约三居室",
            this.img_url = object.getString("img_url");               //  "id": "4478",
            this.designer_id = object.getString("designer_id");       //  "img_url": "https://opic.tbscache.com/manage/case/2015/05-31/small/3fa9b71d-75dc-2a50-c76f-2ad1c99cfd21.jpg",
            this.image_count = object.getString("image_count");       //  "village": "0",
            this.plan_price = object.getString("plan_price");         //  "designer_id": "271567",
//            this.village = object.getString("village");             //  "click_count": "281",
            this.click_count = object.getString("click_count");       //  "plan_price": "28",
            this.designer_icon = object.getString("designer_icon");   //  "designer_icon": "http://opic.tbscache.com/users/sjs/logo/2015/04-14/cut_83169aa1-f0b9-dbaf-2484-ae46725e414b.jpg",
            this.city_name = object.getString("city_name");           //  "area_name": "",
            this.area_name = object.getString("area_name");
            this.title = object.getString("title");

//            Utils.setErrorLog("============>>", this.area_name);

        } catch (JSONException e) {
            e.printStackTrace();
        }
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

//    public String getVillage() {
//        return village;
//    }
//
//    public void setVillage(String village) {
//        this.village = village;
//    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
