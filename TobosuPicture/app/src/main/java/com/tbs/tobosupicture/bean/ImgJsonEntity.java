package com.tbs.tobosupicture.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 看大图图片json实体类
 * Created by Lie on 2017/7/27.
 */

public class ImgJsonEntity {

    /**
     * status : 200
     * msg : success
     * data : [{"id":"243767","title":"新中式风格鉴赏","img_url":"https://opic.tbscache.com/manage/case/2015/05-25/small/17f4a3ae-20de-abc6-c4d8-1fcaef3c680f.jpg","space_name":"客厅","part_name":"","design_concept":"结合了现代人的生活方式，更多的装修风格从复杂变得简单，让生活方式改变了装修的手法，突破了传统的中式风","plan_price":"14"},{"id":"243768","title":"新中式风格鉴赏","img_url":"https://opic.tbscache.com/manage/case/2015/05-25/small/43599cce-cf1d-dbe9-e4b1-1a245652088c.jpg","space_name":"客厅","part_name":"背景墙","design_concept":"结合了现代人的生活方式，更多的装修风格从复杂变得简单，让生活方式改变了装修的手法，突破了传统的中式风","plan_price":"14"},{"id":"243769","title":"新中式风格鉴赏","img_url":"https://opic.tbscache.com/manage/case/2015/05-25/small/3093cb41-0fea-3f3c-9df5-0fad5d0da80b.jpg","space_name":"餐厅","part_name":"","design_concept":"结合了现代人的生活方式，更多的装修风格从复杂变得简单，让生活方式改变了装修的手法，突破了传统的中式风","plan_price":"14"},{"id":"243770","title":"新中式风格鉴赏","img_url":"https://opic.tbscache.com/manage/case/2015/05-25/small/bfd02639-e958-189e-dd5a-d8634f6c0adf.jpg","space_name":"卧室","part_name":"","design_concept":"结合了现代人的生活方式，更多的装修风格从复杂变得简单，让生活方式改变了装修的手法，突破了传统的中式风","plan_price":"14"}]
     */

    private int status;
    private String msg;
    private ArrayList<ImgEntity> imgEntityList;
    private JSONArray dataArr;

    public ImgJsonEntity(){}
    public ImgJsonEntity(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            this.status = jsonObject.getInt("status");
            this.msg = jsonObject.getString("msg");
            if(this.status == 200){
                imgEntityList = new ArrayList<ImgEntity>();
                this.dataArr = jsonObject.getJSONArray("data");
                int len = this.dataArr.length();
                for(int i=0;i<len;i++){
                    ImgEntity entity = new ImgEntity();
                    entity.setId(dataArr.getJSONObject(i).getString("id"));
                    entity.setTitle(dataArr.getJSONObject(i).getString("title"));
                    entity.setImg_url(dataArr.getJSONObject(i).getString("img_url"));
                    entity.setSpace_name(dataArr.getJSONObject(i).getString("space_name"));
                    entity.setPart_name(dataArr.getJSONObject(i).getString("part_name"));
                    entity.setDesign_concept(dataArr.getJSONObject(i).getString("design_concept"));
                    entity.setPlan_price(dataArr.getJSONObject(i).getString("plan_price"));
                    imgEntityList.add(entity);
                }
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

    public ArrayList<ImgEntity> getImgEntityList() {
        return imgEntityList;
    }

    public void setImgEntityList(ArrayList<ImgEntity> imgEntityList) {
        imgEntityList = imgEntityList;
    }
}
