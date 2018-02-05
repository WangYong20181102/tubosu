package com.tbs.tobosutype.bean;

import java.util.List;

/**
 * Created by Mr.Lin on 2018/2/4 11:21.
 */

public class _AppEvent {

    /**
     * ct :
     * uid :
     * ci :
     * pn :
     * pv :
     * di :
     * si :
     * ua :
     * ip :
     * sr :
     * dm :
     * lng :
     * lat :
     * mac :
     * imei :
     * ani :
     * idfa :
     * idfv :
     * ev : [{"ref":"","url":"","pt":"","et":"","cp":"","vt":"","lt":""}]
     */

    private String ct;//生成数据的时间
    private String uid;//用户的id
    private String pn;//产品的名称
    private String pv;//产品的版本
    private String di;//设备的id
    private String si;
    private String sr;
    private String dm;
    private String sv;
    private String lng;
    private String lat;
    private String mac;
    private String imei;
    private String ani;
    private String no;
    private List<EvBean> ev;

    public String getSv() {
        return sv;
    }

    public void setSv(String sv) {
        this.sv = sv;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getPn() {
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }

    public String getPv() {
        return pv;
    }

    public void setPv(String pv) {
        this.pv = pv;
    }

    public String getDi() {
        return di;
    }

    public void setDi(String di) {
        this.di = di;
    }

    public String getSi() {
        return si;
    }

    public void setSi(String si) {
        this.si = si;
    }

    public String getSr() {
        return sr;
    }

    public void setSr(String sr) {
        this.sr = sr;
    }

    public String getDm() {
        return dm;
    }

    public void setDm(String dm) {
        this.dm = dm;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getAni() {
        return ani;
    }

    public void setAni(String ani) {
        this.ani = ani;
    }

    public List<EvBean> getEv() {
        return ev;
    }

    public void setEv(List<EvBean> ev) {
        this.ev = ev;
    }

    public static class EvBean {
        /**
         * ref :
         * url :
         * pt :
         * et :
         * cp :
         * vt :
         * lt :
         */

        private String ref;
        private String url;
        private String pt;
        private String et;
        private String cp;
        private String vt;
        private String lt;

        public String getRef() {
            return ref;
        }

        public void setRef(String ref) {
            this.ref = ref;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPt() {
            return pt;
        }

        public void setPt(String pt) {
            this.pt = pt;
        }

        public String getEt() {
            return et;
        }

        public void setEt(String et) {
            this.et = et;
        }

        public String getCp() {
            return cp;
        }

        public void setCp(String cp) {
            this.cp = cp;
        }

        public String getVt() {
            return vt;
        }

        public void setVt(String vt) {
            this.vt = vt;
        }

        public String getLt() {
            return lt;
        }

        public void setLt(String lt) {
            this.lt = lt;
        }
    }
}
