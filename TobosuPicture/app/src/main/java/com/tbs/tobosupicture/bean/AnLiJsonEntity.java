package com.tbs.tobosupicture.bean;

import java.util.ArrayList;

/**
 * Created by Lie on 2017/8/3.
 */

public class AnLiJsonEntity {


    /**
     * status : 200
     * msg : success
     * data : [{"caseid":"10364","img_url":"","shi":"3","ting":"2","wei":"1","area":"112","desmethod":"半包","price":"14","collect_count":"1392","is_collect":"0"},{"caseid":"10358","img_url":"","shi":"5","ting":"2","wei":"2","area":"180","desmethod":"半包","price":"18","collect_count":"608","is_collect":"1"},{"caseid":"10079","img_url":"","shi":"4","ting":"2","wei":"2","area":"100","desmethod":"半包","price":"8","collect_count":"520","is_collect":"1"},{"caseid":"10066","img_url":"","shi":"5","ting":"2","wei":"2","area":"144","desmethod":"半包","price":"10","collect_count":"1407","is_collect":"1"},{"caseid":"10001","img_url":"","shi":"5","ting":"5","wei":"5","area":"180","desmethod":"半包","price":"15","collect_count":"1051","is_collect":"1"}]
     */

    private int status;
    private String msg;
    private ArrayList<AnLiEntity> data;

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

    public ArrayList<AnLiEntity> getData() {
        return data;
    }

    public void setData(ArrayList<AnLiEntity> data) {
        this.data = data;
    }

    public static class AnLiEntity {
        /**
         * caseid : 10364
         * img_url :
         * shi : 3
         * ting : 2
         * wei : 1
         * area : 112
         * desmethod : 半包
         * price : 14
         * collect_count : 1392
         * is_collect : 0
         */

        private String caseid;
        private String img_url;
        private String shi;
        private String ting;
        private String wei;
        private String area;
        private String desmethod;
        private String price;
        private String collect_count;
        private String is_collect;

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

        public String getDesmethod() {
            return desmethod;
        }

        public void setDesmethod(String desmethod) {
            this.desmethod = desmethod;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCollect_count() {
            return collect_count;
        }

        public void setCollect_count(String collect_count) {
            this.collect_count = collect_count;
        }

        public String getIs_collect() {
            return is_collect;
        }

        public void setIs_collect(String is_collect) {
            this.is_collect = is_collect;
        }
    }
}
