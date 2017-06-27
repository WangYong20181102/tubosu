package com.tbs.tobosupicture.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mr.Lin on 2017/5/10 16:50.
 */

public class LbPicture {
    private String Id;
    private String ImgUrl;
    private String ContentUrl;

    public LbPicture(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            this.Id = jsonObject.getString("id");
            this.ImgUrl = jsonObject.getString("img_url");
            this.ContentUrl = jsonObject.getString("content_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public String getContentUrl() {
        return ContentUrl;
    }

    public void setContentUrl(String contentUrl) {
        ContentUrl = contentUrl;
    }
}
