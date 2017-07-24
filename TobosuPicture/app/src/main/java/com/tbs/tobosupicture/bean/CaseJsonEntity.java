package com.tbs.tobosupicture.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Lie on 2017/7/19.
 */

public class CaseJsonEntity {

    /**
     * status : 0
     * msg : success
     * data : [{"caseid":"15473","img_url":"https://pic.tbscache.com/impress_pic/2017-05-25/p_592690f450fcf.jpg","owner_name":"王先生","price":"3","designer_id":"0","desmethod":"半包","shi":"","ting":"","wei":"","area":"105","district_name":"荣盛香榭兰庭","style_name":"","designer_name":"","designer_icon":""},{"caseid":"15466","img_url":"https://pic.tbscache.com/impress_pic/2017-05-23/p_5923f812846be.jpg","owner_name":"","price":"11","designer_id":"0","desmethod":"全包","shi":"","ting":"","wei":"","area":"95","district_name":"金科中央御院","style_name":"","designer_name":"","designer_icon":""},{"caseid":"15282","img_url":"https://pic.tbscache.com/impress_pic/2017-05-04/p_590aec0a1d9f9.jpg","owner_name":"陈先生","price":"11","designer_id":"0","desmethod":"全包","shi":"","ting":"","wei":"","area":"100","district_name":"金地澜菲溪岸","style_name":"","designer_name":"","designer_icon":""},{"caseid":"15195","img_url":"https://pic.tbscache.com/impress_pic/2017-05-03/p_59099d20e9e27.jpg","owner_name":"刘先生","price":"12","designer_id":"0","desmethod":"全包","shi":"","ting":"","wei":"","area":"107","district_name":"招商公园1872","style_name":"","designer_name":"","designer_icon":""},{"caseid":"15037","img_url":"https://pic.tbscache.com/impress_pic/2017-04-21/p_58f9c7fd9dd06.jpg","owner_name":"潘女士","price":"27","designer_id":"0","desmethod":"半包","shi":"","ting":"","wei":"","area":"310","district_name":"武汉碧桂园别墅","style_name":"","designer_name":"","designer_icon":""},{"caseid":"14976","img_url":"https://pic.tbscache.com/impress_pic/2017-04-12/p_58edd6c5e4cb6.jpg","owner_name":"刘先生","price":"8","designer_id":"0","desmethod":"全包","shi":"","ting":"","wei":"","area":"84","district_name":"招商公园1872","style_name":"","designer_name":"","designer_icon":""},{"caseid":"14954","img_url":"https://pic.tbscache.com/impress_pic/2017-04-06/p_58e5f2b391e71.jpg","owner_name":"聂先生","price":"12","designer_id":"0","desmethod":"全包","shi":"","ting":"","wei":"","area":"109","district_name":"新城璟悦城","style_name":"","designer_name":"","designer_icon":""},{"caseid":"14911","img_url":"https://pic.tbscache.com/impress_pic/2017-04-06/p_58e5f3ddd5c42.jpg","owner_name":"刘女士","price":"10","designer_id":"0","desmethod":"全包","shi":"","ting":"","wei":"","area":"92","district_name":"世茂锦绣长江","style_name":"","designer_name":"","designer_icon":""},{"caseid":"14880","img_url":"https://pic.tbscache.com/impress_pic/2017-04-06/p_58e5f2b391e71.jpg","owner_name":"杜女士","price":"8","designer_id":"0","desmethod":"全包","shi":"","ting":"","wei":"","area":"85","district_name":"海伦春天","style_name":"","designer_name":"","designer_icon":""},{"caseid":"14878","img_url":"https://pic.tbscache.com/impress_pic/2017-04-06/p_58e5f97c64169.jpg","owner_name":"韩先生","price":"8","designer_id":"0","desmethod":"全包","shi":"","ting":"","wei":"","area":"89","district_name":"金色港湾（一至四期）","style_name":"","designer_name":"","designer_icon":""}]
     */

    private int status;
    private String msg;
    private JSONArray jsonArray;
    private ArrayList<CaseEntity> dataList;

    public CaseJsonEntity(String json){
        try {
            JSONObject object = new JSONObject(json);
            this.status = object.getInt("status");
            this.msg = object.getString("msg");
            this.msg = object.getString("msg");
            if(this.status==200){
                this.jsonArray = object.getJSONArray("data");
                int len = this.jsonArray.length();
                this.dataList = new ArrayList<CaseEntity>();
                if(len>0){
                    for(int i=0;i<len;i++){
                        CaseEntity entity = new CaseEntity();
                        entity.setCaseid(jsonArray.getJSONObject(i).getString("caseid"));
                        entity.setImg_url(jsonArray.getJSONObject(i).getString("img_url"));
                        entity.setOwner_name(jsonArray.getJSONObject(i).getString("owner_name"));
                        entity.setPrice(jsonArray.getJSONObject(i).getString("price"));
                        entity.setDesigner_id(jsonArray.getJSONObject(i).getString("designer_id"));
                        entity.setDesmethod(jsonArray.getJSONObject(i).getString("desmethod"));
                        entity.setShi(jsonArray.getJSONObject(i).getString("shi"));
                        entity.setTing(jsonArray.getJSONObject(i).getString("ting"));
                        entity.setWei(jsonArray.getJSONObject(i).getString("wei"));
                        entity.setArea(jsonArray.getJSONObject(i).getString("area"));
                        entity.setDistrict_name(jsonArray.getJSONObject(i).getString("district_name"));
                        entity.setStyle_name(jsonArray.getJSONObject(i).getString("style_name"));
                        entity.setDesigner_name(jsonArray.getJSONObject(i).getString("designer_name"));
                        entity.setDesigner_icon(jsonArray.getJSONObject(i).getString("designer_icon"));
                        dataList.add(entity);
                    }
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

    public ArrayList<CaseEntity> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<CaseEntity> dataList) {
        this.dataList = dataList;
    }

    public static class CaseEntity {
        /**
         * caseid : 15473
         * img_url : https://pic.tbscache.com/impress_pic/2017-05-25/p_592690f450fcf.jpg
         * owner_name : 王先生
         * price : 3
         * designer_id : 0
         * desmethod : 半包
         * shi :
         * ting :
         * wei :
         * area : 105
         * district_name : 荣盛香榭兰庭
         * style_name :
         * designer_name :
         * designer_icon :
         */

        private String caseid;
        private String img_url;
        private String owner_name;
        private String price;
        private String designer_id;
        private String desmethod;
        private String shi;
        private String ting;
        private String wei;
        private String area;
        private String district_name;
        private String style_name;
        private String designer_name;
        private String designer_icon;

        public String getCaseid() {
            return caseid;
        }

        public void setCaseid(String caseid) {
            this.caseid = caseid;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getOwner_name() {
            return owner_name;
        }

        public void setOwner_name(String owner_name) {
            this.owner_name = owner_name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getDesigner_id() {
            return designer_id;
        }

        public void setDesigner_id(String designer_id) {
            this.designer_id = designer_id;
        }

        public String getDesmethod() {
            return desmethod;
        }

        public void setDesmethod(String desmethod) {
            this.desmethod = desmethod;
        }

        public String getShi() {
            return shi;
        }

        public void setShi(String shi) {
            this.shi = shi;
        }

        public String getTing() {
            return ting;
        }

        public void setTing(String ting) {
            this.ting = ting;
        }

        public String getWei() {
            return wei;
        }

        public void setWei(String wei) {
            this.wei = wei;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getDistrict_name() {
            return district_name;
        }

        public void setDistrict_name(String district_name) {
            this.district_name = district_name;
        }

        public String getStyle_name() {
            return style_name;
        }

        public void setStyle_name(String style_name) {
            this.style_name = style_name;
        }

        public String getDesigner_name() {
            return designer_name;
        }

        public void setDesigner_name(String designer_name) {
            this.designer_name = designer_name;
        }

        public String getDesigner_icon() {
            return designer_icon;
        }

        public void setDesigner_icon(String designer_icon) {
            this.designer_icon = designer_icon;
        }
    }
}
