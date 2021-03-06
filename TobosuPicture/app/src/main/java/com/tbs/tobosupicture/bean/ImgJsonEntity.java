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
    private SeeImgEntity seeImgEntity;
    private JSONObject data;

    public ImgJsonEntity(){}
    public ImgJsonEntity(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            this.status = jsonObject.getInt("status");
            this.msg = jsonObject.getString("msg");
            if(this.status == 200){

                this.data = jsonObject.getJSONObject("data");
                seeImgEntity = new SeeImgEntity();
                seeImgEntity.setSuite_id(this.data.getString("suite_id"));
                seeImgEntity.setIs_collect(this.data.getString("is_collect"));
                seeImgEntity.setShare_desc(this.data.getString("share_title"));
                seeImgEntity.setShare_image(this.data.getString("share_image"));
                seeImgEntity.setShare_title(this.data.getString("share_desc"));
                seeImgEntity.setShare_url(this.data.getString("share_url"));


                JSONArray pictureArr = this.data.getJSONArray("picture");
                ArrayList<ImgEntity> imgEntityList = new ArrayList<ImgEntity>();
                int len = pictureArr.length();
                for(int i=0;i<len;i++){
                    ImgEntity entity = new ImgEntity();
                    entity.setId(pictureArr.getJSONObject(i).getString("id"));
                    entity.setTitle(pictureArr.getJSONObject(i).getString("title"));
                    entity.setImg_url(pictureArr.getJSONObject(i).getString("img_url"));
                    entity.setSpace_name(pictureArr.getJSONObject(i).getString("space_name"));
                    entity.setPart_name(pictureArr.getJSONObject(i).getString("part_name"));
                    entity.setDesign_concept(pictureArr.getJSONObject(i).getString("design_concept"));
                    entity.setPlan_price(pictureArr.getJSONObject(i).getString("plan_price"));
                    imgEntityList.add(entity);
                }
                seeImgEntity.setImgEntityList(imgEntityList);
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

    public SeeImgEntity getSeeImgEntity() {
        return seeImgEntity;
    }

    public void setSeeImgEntity(SeeImgEntity seeImgEntity) {
        this.seeImgEntity = seeImgEntity;
    }
}
