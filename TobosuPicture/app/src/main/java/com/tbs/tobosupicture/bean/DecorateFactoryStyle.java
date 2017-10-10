package com.tbs.tobosupicture.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * 工装类型实体类
 * Created by Lie on 2017/7/14.
 */

public class DecorateFactoryStyle {


    public DecorateFactoryStyle(){}

    public DecorateFactoryStyle(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            this.msg = jsonObject.getString("msg");
            this.status = jsonObject.getInt("status");
            this.data = jsonObject.getJSONArray("data");
            int len = this.data.length();
            for(int i=0;i<len;i++){
                FactoryStyrlBean bean = new FactoryStyrlBean();
                String id = this.data.getJSONObject(i).getString("id");
                String class_name = this.data.getJSONObject(i).getString("class_name");
                String event_name = this.data.getJSONObject(i).getString("event_name");
                JSONArray arr = this.data.getJSONObject(i).getJSONArray("child_data");
                int ss = arr.length();
                ArrayList<ChildData> childDataList = new ArrayList<ChildData>();
                if(ss>0){
                    for(int j=0;j<ss;j++){
                        String small_id = arr.getJSONObject(j).getString("id");
                        String small_parent_id = arr.getJSONObject(j).getString("parent_id");
                        String small_class_name = arr.getJSONObject(j).getString("class_name");
                        String small_event_name = arr.getJSONObject(j).getString("event_name");
                        ChildData childData = new ChildData(small_id,small_parent_id,small_class_name,small_event_name);
                        childDataList.add(childData);
                    }
                }

                bean.setId(id);
                bean.setEvent_name(event_name);
                bean.setClass_name(class_name);
                bean.setChild_data(childDataList);
                this.factoryStyrlBeanList.add(bean);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * status : 200
     * msg : success
     * data : [{"id":"5","class_name":"工装单图","child_data":[]},{"id":"6","class_name":"工装套图","child_data":[]},{"id":"13","class_name":"百货类","child_data":[{"id":"117","parent_id":"13","class_name":"饰品店"},{"id":"118","parent_id":"13","class_name":"店面"},{"id":"119","parent_id":"13","class_name":"花店"},{"id":"120","parent_id":"13","class_name":"鞋店"},{"id":"121","parent_id":"13","class_name":"化妆品店"},{"id":"122","parent_id":"13","class_name":"孕婴店"},{"id":"123","parent_id":"13","class_name":"便利店"},{"id":"124","parent_id":"13","class_name":"汽车店"},{"id":"125","parent_id":"13","class_name":"珠宝店"},{"id":"126","parent_id":"13","class_name":"药店"},{"id":"127","parent_id":"13","class_name":"数码店"},{"id":"128","parent_id":"13","class_name":"建材店"},{"id":"129","parent_id":"13","class_name":"家居店"}]},{"id":"14","class_name":"餐饮类","child_data":[{"id":"95","parent_id":"14","class_name":"饭店"},{"id":"96","parent_id":"14","class_name":"快餐店"},{"id":"97","parent_id":"14","class_name":"茶楼"},{"id":"98","parent_id":"14","class_name":"烧烤店"},{"id":"99","parent_id":"14","class_name":"咖啡厅"},{"id":"100","parent_id":"14","class_name":"火锅店"},{"id":"101","parent_id":"14","class_name":"餐馆"},{"id":"102","parent_id":"14","class_name":"饮品店"},{"id":"103","parent_id":"14","class_name":"食品店"},{"id":"943","parent_id":"14","class_name":"茶餐厅"}]},{"id":"15","class_name":"生活类","child_data":[{"id":"104","parent_id":"15","class_name":"美容院"},{"id":"105","parent_id":"15","class_name":"旅馆"},{"id":"106","parent_id":"15","class_name":"宾馆"},{"id":"107","parent_id":"15","class_name":"酒店"},{"id":"109","parent_id":"15","class_name":"超市"},{"id":"110","parent_id":"15","class_name":"足浴店"},{"id":"111","parent_id":"15","class_name":"商场"},{"id":"112","parent_id":"15","class_name":"医院"},{"id":"115","parent_id":"15","class_name":"美发店"},{"id":"116","parent_id":"15","class_name":"美甲店"},{"id":"946","parent_id":"15","class_name":"摄影店"}]},{"id":"16","class_name":"服饰类","child_data":[{"id":"114","parent_id":"16","class_name":"服装店"}]},{"id":"17","class_name":"娱乐类","child_data":[{"id":"92","parent_id":"17","class_name":"网吧"},{"id":"93","parent_id":"17","class_name":"ktv"},{"id":"94","parent_id":"17","class_name":"酒吧"},{"id":"941","parent_id":"17","class_name":"会所"}]},{"id":"18","class_name":"健身类","child_data":[{"id":"939","parent_id":"18","class_name":"健身房"}]},{"id":"19","class_name":"教育类","child_data":[{"id":"108","parent_id":"19","class_name":"幼儿园"},{"id":"113","parent_id":"19","class_name":"学校"},{"id":"944","parent_id":"19","class_name":"图书馆"}]},{"id":"20","class_name":"商业类","child_data":[{"id":"88","parent_id":"20","class_name":"办公室"},{"id":"89","parent_id":"20","class_name":"写字楼"},{"id":"90","parent_id":"20","class_name":"会议室"},{"id":"91","parent_id":"20","class_name":"公司"},{"id":"940","parent_id":"20","class_name":"展厅"}]},{"id":"21","class_name":"其它类","child_data":[{"id":"942","parent_id":"21","class_name":"酒庄"}]}]
     */

    private int status;
    private String msg;
    private JSONArray data;
    private ArrayList<FactoryStyrlBean> factoryStyrlBeanList = new ArrayList<FactoryStyrlBean>();

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

    public JSONArray getData() {
        return data;
    }

    public void setData(JSONArray data) {
        this.data = data;
    }

    public ArrayList<FactoryStyrlBean> getFactoryStyrlBeanList() {
        return factoryStyrlBeanList;
    }

    public void setFactoryStyrlBeanList(ArrayList<FactoryStyrlBean> factoryStyrlBeanList) {
        this.factoryStyrlBeanList = factoryStyrlBeanList;
    }
}
