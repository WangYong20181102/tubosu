package com.tobosu.mydecorate.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lie on 2017/6/5.
 */

public class BibleEntity {
    /**
     * aid : 314
     * title : fdsfsdfsdf
     * view_count : 20
     * collect_count : 0
     * tup_count : 0
     * author_name : 灌灌灌灌灌过
     * author_id : 23109
     * time : 2天前
     * img_url : ["/upload/mt_content/2017-04-11/p_58ec8e6be5b73.jpg","/upload/mt_content/2017-04-11/p_58ec9373107cf.jpg","/upload/mt_content/2017-04-11/p_58ec938be1d1d.jpg","/upload/mt_content/2017-04-11/p_58ec93d80a92e.jpg","/upload/mt_content/2017-04-11/p_58ec993c11a33.jpg"]
     * style : 2
     */

    private String aid;
    private String title;
    private String view_count;
    private String collect_count;
    private String tup_count;
    private String author_name;
    private String author_id;
    private String time;
    private String style;
    private List<String> img_url = new ArrayList<>();

    public BibleEntity(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            this.aid = jsonObject.getString("aid");
            this.title = jsonObject.getString("title");
            this.view_count = jsonObject.getString("view_count");
            this.collect_count = jsonObject.getString("collect_count");
            this.tup_count = jsonObject.getString("tup_count");
            this.author_name = jsonObject.getString("author_name");
            this.author_id = jsonObject.getString("author_id");
            this.time = jsonObject.getString("time");
            this.style = jsonObject.getString("style");
            JSONArray jsonArray = jsonObject.getJSONArray("img_url");
            for (int i = 0; i < jsonArray.length(); i++) {
                this.img_url.add(jsonArray.get(i).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<String> getImg_url() {
        return img_url;
    }

    public void setImg_url(List<String> img_url) {
        this.img_url = img_url;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getView_count() {
        return view_count;
    }

    public void setView_count(String view_count) {
        this.view_count = view_count;
    }

    public String getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(String collect_count) {
        this.collect_count = collect_count;
    }

    public String getTup_count() {
        return tup_count;
    }

    public void setTup_count(String tup_count) {
        this.tup_count = tup_count;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

}
