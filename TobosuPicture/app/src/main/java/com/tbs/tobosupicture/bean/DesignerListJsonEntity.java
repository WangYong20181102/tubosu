package com.tbs.tobosupicture.bean;

import java.util.ArrayList;

/**
 * Created by Lie on 2017/8/4.
 */

public class DesignerListJsonEntity {

    /**
     * status : 200
     * msg : success
     * data : [{"id":"24","designer_id":"865","designer_name":"从庆鹏","icon":"http://opic.tbscache.com/sjsimages/Logo/small/hf2011112811465383682.jpg","position":"设计师","view_count":"0","fans_count":"2","impression_count":"0","case_count":"0"},{"id":"22","designer_id":"283408","designer_name":"孙浩","icon":"http://aliyun.tbscache.com/res/common/images/icon/icon2.jpg","position":"设计师","view_count":"0","fans_count":"2","impression_count":"0","case_count":"0"},{"id":"21","designer_id":"283410","designer_name":"关俏","icon":"http://aliyun.tbscache.com/res/common/images/icon/icon14.jpg","position":"设计师","view_count":"0","fans_count":"1","impression_count":"0","case_count":"0"},{"id":"20","designer_id":"264316","designer_name":"李春梅","icon":"http://opic.tbscache.com/users/sjs/logo/2014/07-09/afb95888-2919-67f2-da5f-ff79e99eedd1.jpg","position":"设计师","view_count":"7","fans_count":"6","impression_count":"77","case_count":"0"},{"id":"19","designer_id":"264321","designer_name":"蒋正雷","icon":"http://opic.tbscache.com/users/sjs/logo/2014/07-09/small/9f97488f-71b4-d0ee-1abc-9e2bfcf7b8c8.jpg","position":"设计师","view_count":"6","fans_count":"2","impression_count":"23","case_count":"0"},{"id":"18","designer_id":"278226","designer_name":"刘燕峰","icon":"http://pic.tbscache.com/company_logo/2016-06-29/thumb_577338cb42481.jpg","position":"设计师","view_count":"14","fans_count":"5","impression_count":"31","case_count":"0"},{"id":"17","designer_id":"283409","designer_name":"韦国涛","icon":"https://pic.tbscache.com/company_logo/2017-05-31/thumb_592e83ce8b039.jpg","position":"设计师","view_count":"0","fans_count":"1","impression_count":"0","case_count":"0"}]
     */

    private int status;
    private String msg;
    private ArrayList<MyDesigner> data;

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

    public ArrayList<MyDesigner> getData() {
        return data;
    }

    public void setData(ArrayList<MyDesigner> data) {
        this.data = data;
    }

    public static class MyDesigner {
        /**
         * id : 24
         * designer_id : 865
         * designer_name : 从庆鹏
         * icon : http://opic.tbscache.com/sjsimages/Logo/small/hf2011112811465383682.jpg
         * position : 设计师
         * view_count : 0
         * fans_count : 2
         * impression_count : 0
         * case_count : 0
         */

        private String id;
        private String designer_id;
        private String designer_name;
        private String icon;
        private String position;
        private String view_count;
        private String fans_count;
        private String impression_count;
        private String case_count;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDesigner_id() {
            return designer_id;
        }

        public void setDesigner_id(String designer_id) {
            this.designer_id = designer_id;
        }

        public String getDesigner_name() {
            return designer_name;
        }

        public void setDesigner_name(String designer_name) {
            this.designer_name = designer_name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getView_count() {
            return view_count;
        }

        public void setView_count(String view_count) {
            this.view_count = view_count;
        }

        public String getFans_count() {
            return fans_count;
        }

        public void setFans_count(String fans_count) {
            this.fans_count = fans_count;
        }

        public String getImpression_count() {
            return impression_count;
        }

        public void setImpression_count(String impression_count) {
            this.impression_count = impression_count;
        }

        public String getCase_count() {
            return case_count;
        }

        public void setCase_count(String case_count) {
            this.case_count = case_count;
        }
    }
}
