package com.tbs.tobosutype.bean;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Lin on 2017/4/21 08:56.
 */

public class _ImageDetail {
    private String Id;
    private String indexImageUrl;
    private String createUser;
    private String village;
    private String layoutId;
    private String areId;
    private String planPrice;
    private String designer;
    private String designerName;
    private String styleId;
    private String idNext;
    private String idPrev;
    private comInfo comInfo;
    private List<String> singleMap = new ArrayList<String>();
    private String fengxiangUrl;
    private List<Relate> relates = new ArrayList<Relate>();
    private String havFav;

    public _ImageDetail() {
    }

    public _ImageDetail(JSONObject jsonObject) {
        try {
            this.Id = jsonObject.getString("id");
            this.indexImageUrl = jsonObject.getString("index_image_url");
            this.createUser = jsonObject.getString("create_user");
            this.village = jsonObject.getString("village");
            this.layoutId = jsonObject.getString("layout_id");
            this.areId = jsonObject.getString("area_id");
            this.planPrice = jsonObject.getString("plan_price");
            this.designer = jsonObject.getString("designer");
            this.designerName = jsonObject.getString("designer_name");
            this.styleId = jsonObject.getString("style_id");
            this.idNext = jsonObject.getString("id_next");
            this.idPrev = jsonObject.getString("id_prev");
            if (jsonObject.getJSONObject("com_info").equals("false") || jsonObject.getJSONObject("com_info").equals("null")) {
                this.comInfo = new comInfo();
            } else {
                this.comInfo = new comInfo(jsonObject.getJSONObject("com_info"));
            }
            JSONArray jsonArray = jsonObject.getJSONArray("single_map");
            for (int i = 0; i < jsonArray.length(); i++) {
                this.singleMap.add(new String(jsonArray.get(i).toString()));
            }
            this.fengxiangUrl = jsonObject.getString("fenxiang_url");
            JSONArray jsonArray1 = jsonObject.getJSONArray("relate");
            for (int i = 0; i < jsonArray1.length(); i++) {
                this.relates.add(new Relate(jsonArray1.get(i).toString()));
            }
            this.havFav = jsonObject.getString("hav_fav");

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

    public String getIndexImageUrl() {
        return indexImageUrl;
    }

    public void setIndexImageUrl(String indexImageUrl) {
        this.indexImageUrl = indexImageUrl;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(String layoutId) {
        this.layoutId = layoutId;
    }

    public String getAreId() {
        return areId;
    }

    public void setAreId(String areId) {
        this.areId = areId;
    }

    public String getIdNext() {
        return idNext;
    }

    public void setIdNext(String idNext) {
        this.idNext = idNext;
    }

    public String getIdPrev() {
        return idPrev;
    }

    public void setIdPrev(String idPrev) {
        this.idPrev = idPrev;
    }

    public _ImageDetail.comInfo getComInfo() {
        return comInfo;
    }

    public void setComInfo(_ImageDetail.comInfo comInfo) {
        this.comInfo = comInfo;
    }

    public List<String> getSingleMap() {
        return singleMap;
    }

    public void setSingleMap(List<String> singleMap) {
        this.singleMap = singleMap;
    }

    public String getFengxiangUrl() {
        return fengxiangUrl;
    }

    public void setFengxiangUrl(String fengxiangUrl) {
        this.fengxiangUrl = fengxiangUrl;
    }

    public List<Relate> getRelates() {
        return relates;
    }

    public void setRelates(List<Relate> relates) {
        this.relates = relates;
    }

    public String getHavFav() {
        return havFav;
    }

    public void setHavFav(String havFav) {
        this.havFav = havFav;
    }

    public String getPlanPrice() {
        return planPrice;
    }

    public void setPlanPrice(String planPrice) {
        this.planPrice = planPrice;
    }

    public String getDesigner() {
        return designer;
    }

    public void setDesigner(String designer) {
        this.designer = designer;
    }

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public String getDesignerName() {
        return designerName;
    }

    public void setDesignerName(String designerName) {
        this.designerName = designerName;
    }

    public class comInfo {
        private String comId;
        private String comSimpName;
        private String logoSmall;

        public comInfo() {
        }

        public comInfo(JSONObject jsonObject) {
            try {
                this.comId = jsonObject.getString("comid");
                this.comSimpName = jsonObject.getString("comsimpname");
                this.logoSmall = jsonObject.getString("logosmall");
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
    }

    public class Relate {
        private String relateId;
        private String relatedIndexImageUrl;
        private String des;

        public Relate(String json) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                this.relateId = jsonObject.getString("relateid");
                this.relatedIndexImageUrl = jsonObject.getString("related_index_image_url");
                this.des = jsonObject.getString("des");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getRelateId() {
            return relateId;
        }

        public void setRelateId(String relateId) {
            this.relateId = relateId;
        }

        public String getRelatedIndexImageUrl() {
            return relatedIndexImageUrl;
        }

        public void setRelatedIndexImageUrl(String relatedIndexImageUrl) {
            this.relatedIndexImageUrl = relatedIndexImageUrl;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }
    }
}
