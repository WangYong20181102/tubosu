package com.tbs.tobosupicture.bean;

import com.tbs.tobosupicture.adapter.CompanySearchRecordAdapter;
import com.tbs.tobosupicture.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Lie on 2017/8/1.
 */

public class CaseDetailJsonEntity2 {

    private String TAG = "*CaseDetailJsonEntity1*";

    /**
     * status : 200
     * msg : success
     * data : {"case_data":{"caseid":"10982","shi":"5","ting":"3","wei":"3","area":"87","desmethod":"半包","designer_id":"273125","price":"9","description":"现代风格主要强调的是兼容并蓄的立场，简约而不简单，沉稳而又大气的装修效果,没有过于复杂的设计，讲究的是软装搭配的相互呼应","owner_name":"李先生","city_name":"南京市","district_name":"平治东苑","style_name":"新古典","designer_name":"侯向阳","designer_icon":"http://opic.tbscache.com/users/sjs/logo/2015/07-10/79daa798-065c-b019-8e6b-cf09533f364a.jpg","designer_position":"设计总监","layout_url":"http://pic.tbscache.com/apartment/2016-08-10/small/p_57aa973ccacdd.png"},"online_diagram":[{"id":"177","stage":"1","description":"热镀锌铁管。灵敏度高抗屏蔽，硬度大安全系数高。工艺横平竖直，电路改造的标准工艺。","img_url":["http://pic.tbscache.com/building/2016-08-10/small/p_57aada036040d.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aada1e6d3ea.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aada27a2351.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aada2f1962f.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aada53b0c34.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aada5d9ea03.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aada73df424.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aada89cf90f.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aada950d855.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aadaa0de10d.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aadab41e3d4.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aadabf9cede.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aadace7b7c6.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aadadd4bfe1.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aadaea2a99b.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aadaf320a85.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aadb6b523a4.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aadb8dad2d9.jpg"],"img_count":18},{"id":"178","stage":"2","description":"先地砖后墙砖的铺砖，做到防水一次性成型，降低瓷砖空鼓率。另防水小坝，很好的将水气当在卫生间内部，防止木门受潮腐烂。","img_url":["http://pic.tbscache.com/building/2016-08-10/small/p_57aadbc3d9d87.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aadbc9dd5a6.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aadbcfbb6ba.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aadbd7a803d.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aadbe186933.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aadbeb84d48.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aadbf9cc4cc.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aadc1174e52.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aadc193127a.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aadc2cdee73.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aadc360126e.jpg","http://pic.tbscache.com/building/2016-08-10/small/p_57aadc4669dbc.jpg"],"img_count":12},{"id":"586","stage":"3","description":"合建工艺，三遍腻子，两遍底漆，一遍面漆。确保墙面的光滑，均匀，无色差；让你在不管在任何角度，亦或是光线明暗，还是逆光看上去的效果都是很好！","img_url":["http://pic.tbscache.com/building/2016-10-19/small/p_580738da8bf6c.jpg","http://pic.tbscache.com/building/2016-10-19/small/p_580738dad53f8.jpg","http://pic.tbscache.com/building/2016-10-19/small/p_580738db30850.jpg","http://pic.tbscache.com/building/2016-10-19/small/p_580738db7dc28.jpg","http://pic.tbscache.com/building/2016-10-19/small/p_580738dbc4f1f.jpg"],"img_count":5},{"id":"587","stage":"4","description":"简约风格，看上去干干净净，整齐大方。色彩偏淡，给人一种清新脱俗，春风拂面的感觉！！！棒棒的","img_url":["http://pic.tbscache.com/building/2016-10-19/small/p_58073d6e5fb13.jpg","http://pic.tbscache.com/building/2016-10-19/small/p_58073d6ea51ea.jpg","http://pic.tbscache.com/building/2016-10-19/small/p_58073d6f510b8.jpg","http://pic.tbscache.com/building/2016-10-19/small/p_58073d6f9716c.jpg","http://pic.tbscache.com/building/2016-10-19/small/p_58073d6fe4035.jpg","http://pic.tbscache.com/building/2016-10-19/small/p_58073d7050270.jpg","http://pic.tbscache.com/building/2016-10-19/small/p_58073d709aa2a.jpg","http://pic.tbscache.com/building/2016-10-19/small/p_58073d70ec666.jpg","http://pic.tbscache.com/building/2016-10-19/small/p_58073d714b5af.jpg","http://pic.tbscache.com/building/2016-10-19/small/p_58073d718f5ff.jpg","http://pic.tbscache.com/building/2016-10-19/small/p_58073d71d9466.jpg"],"img_count":11}],"stay_real":[{"id":"2873","img_url":["http://pic.tbscache.com/case_pic/2016-05-25/small/p_57450ceae72b7.jpg","http://pic.tbscache.com/case_pic/2016-05-25/small/p_57450cf543b5c.jpg","http://pic.tbscache.com/case_pic/2016-05-25/small/p_57450cff9d4b4.jpg","http://pic.tbscache.com/case_pic/2016-05-25/small/p_57450d0a42e0e.jpg","http://pic.tbscache.com/case_pic/2016-05-25/small/p_57450d12c1dab.jpg","http://pic.tbscache.com/case_pic/2016-05-25/small/p_57450d200cb67.jpg"],"description":",,,,,","img_count":6}],"suite":[{"img_url":"https://pic.tbscache.com/impress_pic/2016-03-15/p_56e77d1ace62d.jpg","space_name":"餐厅"},{"img_url":"https://pic.tbscache.com/impress_pic/2016-03-15/p_56e77d28eff95.jpg","space_name":"客厅"},{"img_url":"https://pic.tbscache.com/impress_pic/2016-03-15/p_56e77d373d254.jpg","space_name":"卧室"},{"img_url":"https://pic.tbscache.com/impress_pic/2016-03-15/p_56e7a782f0772.jpg","space_name":"厨房"}]}
     */

    private int status;
    private String msg;
    private JSONObject data;

    private CompanyEntity companyEntity;                            //  案例详情 之 公司类
    private CompanyCaseEntity caseInfoEntity;                       //  案例详情 之 公司案例信息
    private ArrayList<OnlineDiagram> onlineDiagramDataList;         //  案例详情 之 在线图
    private ArrayList<String> stayRealImgList;                      //  案例详情 之 入住场景
    private ArrayList<SuiteEntiy> suiteEntiyDataList;               //  案例详情 之 套图
    private String allImgDataString;


    public CaseDetailJsonEntity2(String json){

        try {
            JSONObject jsonObject = new JSONObject(json);
            this.status = jsonObject.getInt("status");
            this.msg = jsonObject.getString("msg");
            if(this.status == 200){
                this.data = jsonObject.getJSONObject("data");
                // 案例信息
                this.caseInfoEntity = new CompanyCaseEntity();
                JSONObject caseDataJsonObject = this.data.getJSONObject("case_data");
                this.caseInfoEntity.setCaseid(caseDataJsonObject.getString("caseid")); // caseid;	//undefined	案例ID号
                this.caseInfoEntity.setTitle(caseDataJsonObject.getString("title"));
                this.caseInfoEntity.setShi(caseDataJsonObject.getString("shi")); // shi;	//undefined	室：没有则为空字符串
                this.caseInfoEntity.setTing(caseDataJsonObject.getString("ting")); // ting;	//undefined	厅：没有则为空字符串
                this.caseInfoEntity.setWei(caseDataJsonObject.getString("wei")); // wei;	//undefined	卫：没有则为空字符串
                this.caseInfoEntity.setArea(caseDataJsonObject.getString("area")); // area;	//undefined	面积
                this.caseInfoEntity.setDesmethod(caseDataJsonObject.getString("desmethod")); // desmethod;	//undefined	装修方式：半包/全包
                this.caseInfoEntity.setDesigner_id(caseDataJsonObject.getString("designer_id")); // designer_id;	//undefined	设计师ID号
                this.caseInfoEntity.setPrice(caseDataJsonObject.getString("price")); // price;	//undefined	价格
                this.caseInfoEntity.setDescription(caseDataJsonObject.getString("description")); // description;	//undefined	案例简介
                this.caseInfoEntity.setOwner_name(caseDataJsonObject.getString("owner_name")); // owner_name;	//undefined	业主名称
                this.caseInfoEntity.setCity_name(caseDataJsonObject.getString("city_name")); // city_name;	//undefined	城市名称
                this.caseInfoEntity.setDistrict_name(caseDataJsonObject.getString("district_name")); // district_name;	//undefined	小区名称
                this.caseInfoEntity.setStyle_name(caseDataJsonObject.getString("style_name")); // style_name;	//undefined	风格名称
                this.caseInfoEntity.setDesigner_name(caseDataJsonObject.getString("designer_name")); // designer_name;	//undefined	设计师名称
                this.caseInfoEntity.setDesigner_icon(caseDataJsonObject.getString("designer_icon")); // designer_icon;	//undefined	设计师头像
                this.caseInfoEntity.setDesigner_position(caseDataJsonObject.getString("designer_position")); // designer_position;	//undefined	设计师职称
                this.caseInfoEntity.setLayout_url(caseDataJsonObject.getString("layout_url")); // layout_url;
                this.caseInfoEntity.setIs_collect(caseDataJsonObject.getString("is_collect"));
                this.caseInfoEntity.setCover_url(caseDataJsonObject.getString("cover_url"));
                this.caseInfoEntity.setShare_url(caseDataJsonObject.getString("share_url"));

                // 在线工地
                JSONArray onlineDiagramArr = this.data.getJSONArray("online_diagram");
                int onlineArrLen = onlineDiagramArr.length();
                if(onlineArrLen>0){
                    onlineDiagramDataList = new ArrayList<OnlineDiagram>();
                    for(int i=0;i<onlineArrLen;i++){
                        OnlineDiagram onlineDiagram = new OnlineDiagram();
                        onlineDiagram.setId(onlineDiagramArr.getJSONObject(i).getString("id"));
                        onlineDiagram.setDescription(onlineDiagramArr.getJSONObject(i).getString("description"));
                        onlineDiagram.setImg_count(onlineDiagramArr.getJSONObject(i).getString("img_count"));
                        onlineDiagram.setStage(onlineDiagramArr.getJSONObject(i).getString("stage"));
//                        JSONArray imgArr = onlineDiagramArr.getJSONObject(i).getJSONArray("img_url");
                        String arrString = onlineDiagramArr.getJSONObject(i).getJSONArray("img_url").toString();
                        Utils.setErrorLog(TAG, "在线工地 原字符串 " + arrString);
                        arrString = arrString.replace(",", "#").replace("[", "").replace("]", "").replaceAll("\"", "");
                        Utils.setErrorLog(TAG, "replace之后" + arrString);
                        ArrayList<String> imgList = new ArrayList<String>();
                        String[] tempArr = arrString.split("#");
                        for(int j=0; j<tempArr.length;j++){
                            String temp = tempArr[j];
                            if(!"".equals(temp)){
                                imgList.add(temp);
                                Utils.setErrorLog("在线工地  分割之后 ", temp);
                            }else{
                                Utils.setErrorLog("发生什么事1 >>>>>>", "发生什么事1");
                            }
                        }
                        onlineDiagram.setImg_url(imgList);
                        onlineDiagramDataList.add(onlineDiagram);
                    }
                }


                // 入住实景
                JSONArray stayRealArr = this.data.getJSONArray("stay_real");
                int stayRealArrLen = stayRealArr.length();
                if(stayRealArrLen>0){
//                    stayRealEntityDataList = new ArrayList<StayRealEntity>();
                    stayRealImgList = new ArrayList<String>();
                    String arrString = stayRealArr.toString();
                    Utils.setErrorLog(TAG, "入住实景 原字符串 " + arrString);
                    arrString = arrString.replace(",", "#").replace("[", "").replace("]", "").replaceAll("\"", "");
                    String[] tempArr = arrString.split("#");
                    for(int j=0; j<tempArr.length;j++){
                        String temp = tempArr[j];
                        if(!"".equals(temp)){
                            stayRealImgList.add(temp);
                            Utils.setErrorLog("入住实景 分割之后 ", temp);
                        }else{
                            Utils.setErrorLog("发生什么事2 >>>>>>", "发生什么事2");
                        }
                    }
                }

                JSONArray suiteArr = this.data.getJSONArray("suite");
                int suiteArrLen = suiteArr.length();
                if(suiteArrLen>0){
                    suiteEntiyDataList = new ArrayList<SuiteEntiy>();
                    for(int i=0;i<suiteArrLen;i++){
                        SuiteEntiy entiy = new SuiteEntiy();
                        entiy.setImg_url(suiteArr.getJSONObject(i).getString("img_url"));
                        entiy.setSpace_name(suiteArr.getJSONObject(i).getString("space_name"));
                        suiteEntiyDataList.add(entiy);
                    }
                }

                JSONArray allImg = this.data.getJSONArray("img_urls");
                allImgDataString = allImg.toString();
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

    public CompanyEntity getCompanyEntity() {
        return companyEntity;
    }

    public void setCompanyEntity(CompanyEntity companyEntity) {
        this.companyEntity = companyEntity;
    }

    public CompanyCaseEntity getCaseInfoEntity() {
        return caseInfoEntity;
    }

    public void setCaseInfoEntity(CompanyCaseEntity caseInfoEntity) {
        this.caseInfoEntity = caseInfoEntity;
    }

    public ArrayList<OnlineDiagram> getOnlineDiagramDataList() {
        return onlineDiagramDataList;
    }

    public void setOnlineDiagramDataList(ArrayList<OnlineDiagram> onlineDiagramDataList) {
        this.onlineDiagramDataList = onlineDiagramDataList;
    }

    public ArrayList<String> getStayRealImgList() {
        return stayRealImgList;
    }

    public void setStayRealImgList(ArrayList<String> stayRealImgList) {
        this.stayRealImgList = stayRealImgList;
    }

    public ArrayList<SuiteEntiy> getSuiteEntiyDataList() {
        return suiteEntiyDataList;
    }

    public void setSuiteEntiyDataList(ArrayList<SuiteEntiy> suiteEntiyDataList) {
        this.suiteEntiyDataList = suiteEntiyDataList;
    }

    public String getAllImgDataString() {
        return allImgDataString;
    }

    public void setAllImgDataString(String allImgDataString) {
        this.allImgDataString = allImgDataString;
    }
}
