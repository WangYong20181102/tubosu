package com.tobosu.mydecorate.entity;

import java.io.Serializable;

/**
 * Created by dec on 2016/10/19.
 */

public class HomeFragmentData implements Serializable {
    private String name;
    private String aid;
    private String uid;
    private String title;
    private String type_id;
    private String tup_count;
    private String collect_count;
    private String show_count;
    private String image_url;
    private String time;
    private String time_unit;
    private String view_count;
//    private String system_code;
    private String c_title;
    private String url;

    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
    //    private String headerPicUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getTup_count() {
        return tup_count;
    }

    public void setTup_count(String tup_count) {
        this.tup_count = tup_count;
    }

    public String getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(String collect_count) {
        this.collect_count = collect_count;
    }

    public String getShow_count() {
        return show_count;
    }

    public void setShow_count(String show_count) {
        this.show_count = show_count;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime_unit() {
        return time_unit;
    }

    public void setTime_unit(String time_unit) {
        this.time_unit = time_unit;
    }

    public String getView_count() {
        return view_count;
    }

    public void setView_count(String view_count) {
        this.view_count = view_count;
    }

//    public String getSystem_code() {
//        return system_code;
//    }
//
//    public void setSystem_code(String system_code) {
//        this.system_code = system_code;
//    }

    public String getC_title() {
        return c_title;
    }

    public void setC_title(String c_title) {
        this.c_title = c_title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(!(o instanceof HomeFragmentData)){
            return false;
        }
        final HomeFragmentData other = (HomeFragmentData)o;
        if(this.name.equals(other.getName()) && this.aid.equals(other.getAid()) &&
                this.uid.equals(other.getUid()) && this.title.equals(other.getTitle()) &&
                this.type_id.equals(other.getType_id()) && this.tup_count.equals(other.getTup_count()) &&
                this.collect_count.equals(other.getCollect_count()) && this.show_count.equals(other.getShow_count()) &&
                this.image_url.equals(other.getImage_url())&&
                this.time.equals(other.getTime()) && this.time_unit.equals(other.getTime_unit()) &&
                this.view_count.equals(other.getView_count()) &&
                this.c_title.equals(other.getC_title()) && this.url.equals(other.getUrl()))
        {
            return true;
        }
        else{
            return false;
        }
    }

    public int hashCode(){
        int result;
        result = (name == null?0:name.hashCode());
        result = 37*result + (aid == null?0:aid.hashCode());
        result = 37*result + (uid == null?0:uid.hashCode());
        result = 37*result + (title == null?0:title.hashCode());
        result = 37*result + (type_id == null?0:type_id.hashCode());
        result = 37*result + (tup_count == null?0:tup_count.hashCode());
        result = 37*result + (collect_count == null?0:collect_count.hashCode());
        result = 37*result + (show_count == null?0:show_count.hashCode());
        result = 37*result + (image_url == null?0:image_url.hashCode());
        result = 37*result + (time == null?0:time.hashCode());
        result = 37*result + (time_unit == null?0:time_unit.hashCode());
        result = 37*result + (c_title == null?0:c_title.hashCode());
        result = 37*result + (url == null?0:url.hashCode());
        return result;
    }

}
