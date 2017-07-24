package com.tbs.tobosupicture.bean;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 搜索记录json
 * Created by Lie on 2017/7/21.
 */

public class SearchRecordJsonEntity {

    /**
     * status : 200
     * msg : success
     * data : {"search_record":[{"city_id":"199","city_name":"深圳市","area_key":"1","area_value":"30㎡以下","layout_key":"2","layout_value":"2室","price_key":"3","price_value":"10~15万","style_id":"38","style_name":"现代简约"},{"city_id":"199","city_name":"深圳市","area_key":"3","area_value":"60~90㎡","layout_key":"3","layout_value":"3室","price_key":"3","price_value":"10~15万","style_id":"38","style_name":"现代简约"},{"city_id":"199","city_name":"深圳市","area_key":"3","area_value":"60~90㎡","layout_key":"3","layout_value":"3室","price_key":"3","price_value":"10~15万","style_id":"36","style_name":"简约"},{"city_id":"199","city_name":"深圳市","area_key":"0","area_value":"","layout_key":"3","layout_value":"3室","price_key":"3","price_value":"10~15万","style_id":"36","style_name":"简约"},{"city_id":"199","city_name":"深圳市","area_key":"0","area_value":"","layout_key":"3","layout_value":"3室","price_key":"3","price_value":"10~15万","style_id":"0","style_name":""}],"area":[{"key":"1","value":"30㎡以下"},{"key":"2","value":"30~60㎡"},{"key":"3","value":"60~90㎡"},{"key":"4","value":"90~120㎡"},{"key":"5","value":"120~140㎡"},{"key":"6","value":"140㎡以上"}],"layout":[{"key":"1","value":"1室"},{"key":"2","value":"2室"},{"key":"3","value":"3室"},{"key":"4","value":"4室"},{"key":"5","value":"4室以上"}],"price":[{"key":"1","value":"5万以下"},{"key":"2","value":"5~10万"},{"key":"3","value":"10~15万"},{"key":"4","value":"15~30万"},{"key":"5","value":"30~50万"},{"key":"6","value":"50万以上"}],"style":[{"id":"33","class_name":"简欧"},{"id":"34","class_name":"欧式"},{"id":"35","class_name":"田园"},{"id":"36","class_name":"简约"},{"id":"37","class_name":"中式"},{"id":"38","class_name":"现代简约"},{"id":"39","class_name":"美式"},{"id":"40","class_name":"新中式"},{"id":"41","class_name":"韩式"},{"id":"42","class_name":"现代"},{"id":"43","class_name":"新古典"},{"id":"44","class_name":"简单"},{"id":"45","class_name":"简中"},{"id":"46","class_name":"北欧"},{"id":"47","class_name":"混搭"},{"id":"48","class_name":"地中海"},{"id":"49","class_name":"欧式田园"},{"id":"50","class_name":"法式"},{"id":"51","class_name":"日式"},{"id":"52","class_name":"现代欧式"},{"id":"53","class_name":"中式古典"},{"id":"54","class_name":"经典"},{"id":"55","class_name":"古典"},{"id":"56","class_name":"现代中式"},{"id":"57","class_name":"后现代"},{"id":"58","class_name":"东南亚"},{"id":"950","class_name":"洛可可"}]}
     */

    private int status;
    private String msg;
    private JSONObject data;
    private SearchDataEntity searchDataEntity;
    private SearchCaseConditionEntity searchCaseConditionEntity;

    public SearchRecordJsonEntity(String  json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            this.status = jsonObject.getInt("status");
            this.msg = jsonObject.getString("msg");

            if(this.status==200){
                searchDataEntity = new SearchDataEntity();
                this.data = jsonObject.getJSONObject("data");
                JSONArray searchRecordArr = data.getJSONArray("search_record");
                int recordLen = searchRecordArr.length();
                ArrayList<SearchRecordBean> searchRecordList = new ArrayList<SearchRecordBean>();
                if(recordLen>0){
                    for(int i=0;i<recordLen;i++){
                        SearchRecordBean bean = new SearchRecordBean();
                        bean.setCity_id(searchRecordArr.getJSONObject(i).getString("city_id"));
                        bean.setCity_name(searchRecordArr.getJSONObject(i).getString("city_name"));
                        bean.setArea_key(searchRecordArr.getJSONObject(i).getString("area_key"));
                        bean.setArea_value(searchRecordArr.getJSONObject(i).getString("area_value"));
                        bean.setLayout_key(searchRecordArr.getJSONObject(i).getString("layout_key"));
                        bean.setLayout_value(searchRecordArr.getJSONObject(i).getString("layout_value"));
                        bean.setPrice_key(searchRecordArr.getJSONObject(i).getString("price_key"));
                        bean.setPrice_value(searchRecordArr.getJSONObject(i).getString("price_value"));
                        bean.setStyle_id(searchRecordArr.getJSONObject(i).getString("style_id"));
                        bean.setStyle_name(searchRecordArr.getJSONObject(i).getString("style_name"));
                        searchRecordList.add(bean);
                    }
                }
                this.searchDataEntity.setSearch_record(searchRecordList);


                // 条件
                searchCaseConditionEntity = new SearchCaseConditionEntity();
                JSONArray conditionArr = data.getJSONArray("condition");
                int arrLen = conditionArr.length();
                ArrayList<CaseConditionType> caseConditionTypeList = new ArrayList<CaseConditionType>();
                if(arrLen>0) {
                    for (int i = 0; i < arrLen; i++) {
                        CaseConditionType type = new CaseConditionType();
                        type.setType(conditionArr.getJSONObject(i).getString("type"));
                        JSONArray childArr = conditionArr.getJSONObject(i).getJSONArray("child_data");
                        ArrayList<CaseTypeChild> caseChildList = new ArrayList<CaseTypeChild>();
                        int childLen = childArr.length();
                        if(childLen>0){
                            for(int j =0;j <childLen;j++){
                                CaseTypeChild child = new CaseTypeChild();
                                child.setId(childArr.getJSONObject(j).getString("id"));
                                child.setValue(childArr.getJSONObject(j).getString("value"));
                                caseChildList.add(child);
                            }
                            type.setCaseTypeChildList(caseChildList);
                        }
                        caseConditionTypeList.add(type);
                    }
                }
                this.searchCaseConditionEntity.setConditionTypeList(caseConditionTypeList);

            }else{

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


    public SearchDataEntity getSearchDataEntity() {
        return searchDataEntity;
    }

    public void setSearchDataEntity(SearchDataEntity searchDataEntity) {
        this.searchDataEntity = searchDataEntity;
    }

    public SearchCaseConditionEntity getSearchCaseConditionEntity() {
        return searchCaseConditionEntity;
    }

    public void setSearchCaseConditionEntity(SearchCaseConditionEntity searchCaseConditionEntity) {
        this.searchCaseConditionEntity = searchCaseConditionEntity;
    }
}
