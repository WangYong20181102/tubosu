package com.tbs.tobosupicture.bean;

import java.util.ArrayList;

/**
 * Created by Lie on 2017/7/18.
 */

public class DesignerEntity {
    private DesignerInfoEntity designerInfoEntity; // 设计师类
    private ArrayList<DesignerImpressionEntity> designerImpressionEntityList; // 样板图
    private ArrayList<DesignerCaseEntity> designerCaseEntityList; //  案例图

    public DesignerInfoEntity getDesignerInfoEntity() {
        return designerInfoEntity;
    }

    public void setDesignerInfoEntity(DesignerInfoEntity designerInfoEntity) {
        this.designerInfoEntity = designerInfoEntity;
    }

    public ArrayList<DesignerImpressionEntity> getDesignerImpressionEntityList() {
        return designerImpressionEntityList;
    }

    public void setDesignerImpressionEntityList(ArrayList<DesignerImpressionEntity> designerImpressionEntityList) {
        this.designerImpressionEntityList = designerImpressionEntityList;
    }

    public ArrayList<DesignerCaseEntity> getDesignerCaseEntityList() {
        return designerCaseEntityList;
    }

    public void setDesignerCaseEntityList(ArrayList<DesignerCaseEntity> designerCaseEntityList) {
        this.designerCaseEntityList = designerCaseEntityList;
    }
}
