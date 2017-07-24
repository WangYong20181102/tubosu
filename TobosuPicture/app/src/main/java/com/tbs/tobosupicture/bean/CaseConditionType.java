package com.tbs.tobosupicture.bean;

import java.util.ArrayList;

/**
 * Created by Lie on 2017/7/21.
 */

public class CaseConditionType {

    private String type;
    private ArrayList<CaseTypeChild> caseTypeChildList;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<CaseTypeChild> getCaseTypeChildList() {
        return caseTypeChildList;
    }

    public void setCaseTypeChildList(ArrayList<CaseTypeChild> caseTypeChildList) {
        this.caseTypeChildList = caseTypeChildList;
    }
}
