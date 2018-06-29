package com.tobosu.mydecorate.bean;

import com.tobosu.mydecorate.application.MyApplication;
import com.tobosu.mydecorate.util.AppInfoUtil;
import com.tobosu.mydecorate.util.MacUtils;
import com.tobosu.mydecorate.util.Util;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2018/6/25 11:17.
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

    private long ct;//生成数据的时间 unix时间戳
    private String uid;//用户的id
    private String pn;//产品的名称
    private String pv;//产品的版本
    private String di;//设备的id   Android:md5(ani+imei+mac)
    private String si;//会话id 生成规则：md5(di+current_time+random_str)
    private String sr;//手机的分辨率
    //    private String ip;//当前网络的ip
    private String dm;//设备的型号
    private String sv;//设备的系统版本
    private String lng;//经度
    private String lat;//纬度
    private String mac;//MAC地址
    private String imei;//手机的imei
    private String ani;//Android id 等同于Api号码
    private String no;//网络运营商
    private ArrayList<EvBean> ev;

    //初始化初始的信息
    public _AppEvent(ArrayList<EvBean> evBeanArrayList) {
        this.ct = Util.getUnixTime();
        this.uid = Util.getUserId(MyApplication.getContexts());
        this.pn = "zhj_and";
        this.pv = AppInfoUtil.getAppVersionName(MyApplication.getContexts()) + "/" + AppInfoUtil.getNewChannType(MyApplication.getContexts());
        this.di = Util.getDeviceID();
        this.si = Util.getSessionID();
        this.sr = Util.getPixels();
        this.dm = Util.getDeviceModel();
        this.sv = Util.getSystemVersion();
        this.lng = AppInfoUtil.getLng(MyApplication.getContexts());
        this.lat = AppInfoUtil.getLat(MyApplication.getContexts());
        this.mac = MacUtils.getMobileMAC(MyApplication.getContexts());
        this.imei = Util.getIMEI();
        this.ani = Util.getAni();
        this.no = Util.getOperator();
//        this.ip = Util.getIp(MyApplication.getContext());
        this.ev = evBeanArrayList;
    }

    public _AppEvent() {
        this.ct = Util.getUnixTime();
        this.uid = AppInfoUtil.getUserid(MyApplication.getContexts());
        this.pn = "zhj_and";
        this.pv = AppInfoUtil.getAppVersionName(MyApplication.getContexts())+ "/" + AppInfoUtil.getNewChannType(MyApplication.getContexts());
        this.di = Util.getDeviceID();
        this.si = Util.getSessionID();
        this.sr = Util.getPixels();
        this.dm = Util.getDeviceModel();
        this.sv = Util.getSystemVersion();
        this.lng = AppInfoUtil.getLng(MyApplication.getContexts());
        this.lat = AppInfoUtil.getLat(MyApplication.getContexts());
        this.mac = MacUtils.getMobileMAC(MyApplication.getContexts());
        this.imei = Util.getIMEI();
        this.ani = Util.getAni();
        this.no = Util.getOperator();
//        this.ip = Util.getIp(MyApplication.getContexts());
    }

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

    public long getCt() {
        return ct;
    }

    public void setCt(long ct) {
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

    public ArrayList<EvBean> getEv() {
        return ev;
    }

    public void setEv(ArrayList<EvBean> ev) {
        this.ev = ev;
    }

//    public String getIp() {
//        return ip;
//    }
//
//    public void setIp(String ip) {
//        this.ip = ip;
//    }

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

        private String ref;//访问来源
        private String url;//当前的访问页面
        private String pt;//页面的元素
        private String et;//事件类型
        private String cp;//自定义参数
        private long vt;//访问页面的时间
        private long lt;//离开页面的时间


        public EvBean(String from, String nowActivity, String eventCode, long vistTime, long leaveTime, String eventType) {
            this.ref = from;
            this.url = nowActivity;
            this.pt = eventCode;
            this.vt = vistTime;
            this.lt = leaveTime;
            // TODO: 2018/3/1 事件处理的方式  0-访问（页面加载进入算一次访问事件）  1-点击（类似按钮的点击事件）
            this.et = eventType;//传入事件的类型
        }

        public EvBean() {

        }

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

        public long getVt() {
            return vt;
        }

        public void setVt(long vt) {
            this.vt = vt;
        }

        public long getLt() {
            return lt;
        }

        public void setLt(long lt) {
            this.lt = lt;
        }
    }
}
