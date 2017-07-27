package com.tbs.tobosupicture.bean;

/**
 * 设计师实体类
 * Created by Lie on 2017/7/18.
 */

public class DesignerInfoEntity {

    /**
     * desid : 272371
     * desname : 胡丹
     * icon : http://opic.tbscache.com/users/sjs/logo/2015/06-19/small/4230c1e1-37c8-d2dd-9478-c071e0fd0ba6.jpg
     * position : 设计师
     * area :
     * intro :
     * style :
     * impression_count : 1
     * case_count : 0
     */

    private String desid;
    private String desname;
    private String icon;
    private String position;
    private String area;
    private String intro;
    private String style;
    private int impression_count;
    private int case_count;
    private String fans_count;
    private String view_count;
    private String is_follow;


    public String getDesid() {
        return desid;
    }

    public void setDesid(String desid) {
        this.desid = desid;
    }

    public String getDesname() {
        return desname;
    }

    public void setDesname(String desname) {
        this.desname = desname;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public int getImpression_count() {
        return impression_count;
    }

    public void setImpression_count(int impression_count) {
        this.impression_count = impression_count;
    }

    public int getCase_count() {
        return case_count;
    }

    public void setCase_count(int case_count) {
        this.case_count = case_count;
    }


    public String getFans_count() {
        return fans_count;
    }

    public void setFans_count(String fans_count) {
        this.fans_count = fans_count;
    }

    public String getView_count() {
        return view_count;
    }

    public void setView_count(String view_count) {
        this.view_count = view_count;
    }

    public String getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(String is_follow) {
        this.is_follow = is_follow;
    }
}
