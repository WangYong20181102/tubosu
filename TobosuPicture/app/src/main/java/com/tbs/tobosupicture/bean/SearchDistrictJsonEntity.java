package com.tbs.tobosupicture.bean;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 搜索记录json
 * Created by Lie on 2017/7/21.
 */

public class SearchDistrictJsonEntity {


    /**
     * status : 200
     * msg : success
     * data : [{"id":"238618","name":"联投龙湾","image_url":"https://pic.tbscache.com/village_img/2017-05-31/small/p_592e785684120.jpg"},{"id":"238617","name":"连图龙湾","image_url":"https://pic.tbscache.com/village_img/2017-05-31/small/p_592e761760155.jpg"},{"id":"238616","name":"远洲玫瑰园","image_url":"https://pic.tbscache.com/village_img/2017-05-31/small/p_592e3d17256d9.jpg"},{"id":"238615","name":"鼎元别苑","image_url":"https://pic.tbscache.com/village_img/2017-05-31/small/p_592e3aff2c580.jpg"},{"id":"238614","name":"金榈园","image_url":"https://pic.tbscache.com/village_img/2017-05-31/small/p_592e384559139.jpg"},{"id":"238613","name":"碧榆园","image_url":"https://pic.tbscache.com/village_img/2017-05-31/small/p_592e294c7d499.jpg"},{"id":"238612","name":"君泰·中央公園","image_url":"https://pic.tbscache.com/village_img/2017-05-30/small/p_592cd2ef25f8b.jpg"},{"id":"238611","name":"京投发展·檀香府","image_url":"https://pic.tbscache.com/village_img/2017-05-29/small/p_592b9234be4c8.jpg"},{"id":"238610","name":"京投发展·檀香府","image_url":"https://pic.tbscache.com/village_img/2017-05-29/small/p_592b910e8fab9.jpg"},{"id":"238609","name":"公园伍号","image_url":"https://pic.tbscache.com/village_img/2017-05-29/small/p_592b788945cf5.jpg"}]
     */

    private int status;
    private String msg;
    private JSONArray districtArr;
    private ArrayList<DistrictEntity> districtEntityList;


    public SearchDistrictJsonEntity(){

    }


    public SearchDistrictJsonEntity(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            this.status = jsonObject.getInt("status");
            this.msg = jsonObject.getString("msg");
            if(this.status == 200){
                this.districtArr = jsonObject.getJSONArray("data");
                int len = this.districtArr.length();
                districtEntityList = new  ArrayList<DistrictEntity>();
                for(int i=0;i<len;i++){
                    DistrictEntity entity = new DistrictEntity(
                            districtArr.getJSONObject(i).getString("id"),
                            districtArr.getJSONObject(i).getString("name"),
                            districtArr.getJSONObject(i).getString("image_url")
                    );
                    districtEntityList.add(entity);
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

    public ArrayList<DistrictEntity> getDistrictEntityList() {
        return districtEntityList;
    }

    public void setDistrictEntityList(ArrayList<DistrictEntity> districtEntityList) {
        this.districtEntityList = districtEntityList;
    }

    public JSONArray getDistrictArr() {
        return districtArr;
    }

    public void setDistrictArr(JSONArray districtArr) {
        this.districtArr = districtArr;
    }
}
