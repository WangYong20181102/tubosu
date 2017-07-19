package com.tbs.tobosupicture.bean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Lie on 2017/7/18.
 */

public class DesignerJsonEntity {


    private int status;
    private String msg;
    private JSONObject data;
    private DesignerEntity designerEntity;

    public DesignerJsonEntity(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            this.status = jsonObject.getInt("status");
            if(this.status==0){
                this.designerEntity = new DesignerEntity();
                JSONObject designerObject = jsonObject.getJSONObject("designer");
                DesignerInfoEntity designerInfoEntity = new DesignerInfoEntity();
                designerInfoEntity.setDesid(designerObject.getString("desid"));
                designerInfoEntity.setDesname(designerObject.getString("desname"));
                designerInfoEntity.setIcon(designerObject.getString("icon"));
                designerInfoEntity.setPosition(designerObject.getString("position"));
                designerInfoEntity.setArea(designerObject.getString("area"));
                designerInfoEntity.setIntro(designerObject.getString("intro"));
                designerInfoEntity.setStyle(designerObject.getString("style"));
                designerInfoEntity.setImpression_count(designerObject.getInt("impression_count"));
                designerInfoEntity.setCase_count(designerObject.getInt("case_count"));
                this.designerEntity.setDesignerInfoEntity(designerInfoEntity);

                JSONArray impressionArr = jsonObject.getJSONArray("impression");
                int impressLen = impressionArr.length();
                ArrayList<DesignerImpressionEntity> impressionList = new ArrayList<DesignerImpressionEntity>();
                if(impressLen>0){
                    for(int i=0;i<impressLen; i++){
                        DesignerImpressionEntity entity = new DesignerImpressionEntity();
                        entity.setId(impressionArr.getJSONObject(i).getString("id"));

                        entity.setId(impressionArr.getJSONObject(i).getString("id"));
                        entity.setImg_url(impressionArr.getJSONObject(i).getString("img_url"));
                        entity.setImage_count(impressionArr.getJSONObject(i).getString("image_count"));
                        entity.setClass_id(impressionArr.getJSONObject(i).getString("class_id"));
                        entity.setPlan_price(impressionArr.getJSONObject(i).getString("plan_price"));
                        entity.setClass_name(impressionArr.getJSONObject(i).getString("class_name"));
                        entity.setStyle_name(impressionArr.getJSONObject(i).getString("style_name"));
                        entity.setLayout_name(impressionArr.getJSONObject(i).getString("layout_name"));
                        impressionList.add(entity);
                    }
                    this.designerEntity.setDesignerImpressionEntityList(impressionList);
                }


                JSONArray caseArr = jsonObject.getJSONArray("case");
                int caseLen = caseArr.length();
                ArrayList<DesignerCaseEntity> caseList = new ArrayList<DesignerCaseEntity>();
                if(caseLen>0){
                    for(int i=0;i<caseLen;i++){
                        DesignerCaseEntity entity = new DesignerCaseEntity();
                        entity.setCaseid(caseArr.getJSONObject(i).getString("caseid"));
                        entity.setImg_url(caseArr.getJSONObject(i).getString("img_url"));
                        entity.setShi(caseArr.getJSONObject(i).getString("shi"));
                        entity.setTing(caseArr.getJSONObject(i).getString("ting"));
                        entity.setWei(caseArr.getJSONObject(i).getString("wei"));
                        entity.setArea(caseArr.getJSONObject(i).getString("area"));
                        entity.setDesmethod(caseArr.getJSONObject(i).getString("desmethod"));
                        entity.setPrice(caseArr.getJSONObject(i).getString("price"));
                        caseList.add(entity);
                    }
                    designerEntity.setDesignerCaseEntityList(caseList);
                }
            }else{
                this.designerEntity = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public DesignerEntity getDesignerEntity() {
        return designerEntity;
    }

    public void setDesignerEntity(DesignerEntity designerEntity) {
        this.designerEntity = designerEntity;
    }
}
