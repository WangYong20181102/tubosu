package com.tbs.tbs_mj.bean;

/**
 * Created by Mr.Lin on 2017/10/24 09:16.
 * 土拨鼠3.4版本新增 装修案例实体类
 */

public class _DecorationCaseItem {

    /**
     * id : 15591
     * cover_url : https://opic.tbscache.com/users/zsgs/case/2015/04-03/index/2510a1e0-76de-9983-3327-462076a96563.jpg
     * city_name : 深圳市
     * community_name : 金丽豪苑
     * owner_name : 钟老板
     * sub_title : 20㎡/3居/5万/半包
     */

    private String id;
    private String cover_url;
    private String city_name;
    private String community_name;
    private String owner_name;
    private String sub_title;

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

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }
}
