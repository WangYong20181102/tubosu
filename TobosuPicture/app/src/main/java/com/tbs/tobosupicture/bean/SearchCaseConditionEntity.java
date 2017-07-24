package com.tbs.tobosupicture.bean;


import java.util.ArrayList;

/**
 * 搜索类型对象
 * Created by Lie on 2017/7/21.
 */

public class SearchCaseConditionEntity {

    private ArrayList<CaseConditionType> conditionTypeList;

    public ArrayList<CaseConditionType> getConditionTypeList() {
        return conditionTypeList;
    }

    public void setConditionTypeList(ArrayList<CaseConditionType> conditionTypeList) {
        this.conditionTypeList = conditionTypeList;
    }
}
