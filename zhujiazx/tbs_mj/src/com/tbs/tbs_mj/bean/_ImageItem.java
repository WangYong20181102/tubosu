package com.tbs.tbs_mj.bean;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mr.Lin on 2017/4/19 10:50.
 * 逛图库对象 用于图库列表
 * 用到该类的时候直接将JSON数据转为对象
 */

public class _ImageItem {
    public _ImageItem() {

    }

    public _ImageItem(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            this.id = jsonObject.getString("id");
            this.indexImageUrl = jsonObject.getString("index_image_url");
            this.checkTime = jsonObject.getString("check_time");
            this.styleId = jsonObject.getString("style_id");
            this.planPrice = jsonObject.getString("plan_price");
            this.havFav = jsonObject.getString("check_time");
            this.title = jsonObject.getString("title");
            this.title2 = jsonObject.getString("title2");
            this.img1 = jsonObject.getString("img1");
            this.img2 = jsonObject.getString("img2");
            this.indexImageWidth = jsonObject.getInt("index_image_width");
            this.indexImageHeight = jsonObject.getInt("index_image_height");
            this.comInfo = new comInfo(jsonObject.getJSONObject("com_info"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String id;//图册的id
    private String indexImageUrl;
    private String checkTime;
    private String styleId;
    private String planPrice;
    private String havFav;
    private String title;
    private String title2;
    private String img1;
    private String img2;
    private comInfo comInfo;
    private int indexImageWidth;
    private int indexImageHeight;

    public int getIndexImageWidth() {
        return indexImageWidth;
    }

    public void setIndexImageWidth(int indexImageWidth) {
        this.indexImageWidth = indexImageWidth;
    }

    public int getIndexImageHeight() {
        return indexImageHeight;
    }

    public void setIndexImageHeight(int indexImageHeight) {
        this.indexImageHeight = indexImageHeight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIndexImageUrl() {
        return indexImageUrl;
    }

    public void setIndexImageUrl(String indexImageUrl) {
        this.indexImageUrl = indexImageUrl;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getHavFav() {
        return havFav;
    }

    public void setHavFav(String havFav) {
        this.havFav = havFav;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public _ImageItem.comInfo getComInfo() {
        return comInfo;
    }

    public void setComInfo(_ImageItem.comInfo comInfo) {
        this.comInfo = comInfo;
    }

    public String getStyleId() {
        return styleId;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public String getPlanPrice() {
        return planPrice;
    }

    public void setPlanPrice(String planPrice) {
        this.planPrice = planPrice;
    }

    public class comInfo {
        private String comId;
        private String comSimpName;
        private String logoSmall;
        private String certifiCation;
        private String jjbLogo;

        public comInfo(JSONObject jsonObject) {
            try {
                this.comId = jsonObject.getString("comid");
                this.comSimpName = jsonObject.getString("comsimpname");
                this.logoSmall = jsonObject.getString("logosmall");
                this.certifiCation = jsonObject.getString("certification");
                this.jjbLogo = jsonObject.getString("jjb_logo");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getComId() {
            return comId;
        }

        public void setComId(String comId) {
            this.comId = comId;
        }

        public String getComSimpName() {
            return comSimpName;
        }

        public void setComSimpName(String comSimpName) {
            this.comSimpName = comSimpName;
        }

        public String getLogoSmall() {
            return logoSmall;
        }

        public void setLogoSmall(String logoSmall) {
            this.logoSmall = logoSmall;
        }

        public String getCertifiCation() {
            return certifiCation;
        }

        public void setCertifiCation(String certifiCation) {
            this.certifiCation = certifiCation;
        }

        public String getJjbLogo() {
            return jjbLogo;
        }

        public void setJjbLogo(String jjbLogo) {
            this.jjbLogo = jjbLogo;
        }


    }
}
