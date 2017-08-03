package com.tbs.tobosupicture.bean;

/**
 * Created by Lie on 2017/7/18.
 */

public class DesignerCaseEntity {
    /**
     * caseid : 4092
     * img_url : https://opic.tbscache.com/manage/case/2015/05-25/small/17f4a3ae-20de-abc6-c4d8-1fcaef3c680f.jpg
     * shi : 4
     * ting : 0
     * wei : 14
     * area :
     * desmethod : 新中式
     * price : 大户型
     */

    private String caseid;                              //   "id":"6040",
    private String img_url;                             //   "img_url":"https://opic.tbsc
    private String shi;                                 //   "title":"港式奢华风格新世界花园160平大户型装
    private String ting;                                //   "designer_id":"187899",
    private String wei;                                 //   "image_count":"7",
    private String area;                                //   "plan_price":"60",
    private String is_collect;                          //   "click_count":"300",
    private String collect_count;                       //   "designer_icon":"http://opic
    private String desmethod;                           //   "city_name":"沈阳",
    private String price;                               //   "area_name":"120~180㎡"

    public String getCaseid() {
        return caseid;
    }

    public void setCaseid(String caseid) {
        this.caseid = caseid;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
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

    public String getDesmethod() {
        return desmethod;
    }

    public void setDesmethod(String desmethod) {
        this.desmethod = desmethod;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
