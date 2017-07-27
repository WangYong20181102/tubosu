package com.tbs.tobosupicture.bean;

/**
 * 看大图的图片实体类
 * Created by Lie on 2017/7/27.
 */

public class ImgEntity {

    private String id; //": "243767",
    private String title; //": "新中式风格鉴赏",
    private String img_url; //": "https://opic.tbscache.com/manage/case/2015/05-25/small/17f4a3ae-20de-abc6-c4d8-1fcaef3c680f.jpg",
    private String space_name; //": "客厅",
    private String part_name; //": "",
    private String design_concept; //": "结合了现代人的生活方式，更多的装修风格从复杂变得简单，让生活方式改变了装修的手法，突破了传统的中式风",
    private String plan_price; //": "14"

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getSpace_name() {
        return space_name;
    }

    public void setSpace_name(String space_name) {
        this.space_name = space_name;
    }

    public String getPart_name() {
        return part_name;
    }

    public void setPart_name(String part_name) {
        this.part_name = part_name;
    }

    public String getDesign_concept() {
        return design_concept;
    }

    public void setDesign_concept(String design_concept) {
        this.design_concept = design_concept;
    }

    public String getPlan_price() {
        return plan_price;
    }

    public void setPlan_price(String plan_price) {
        this.plan_price = plan_price;
    }
}
