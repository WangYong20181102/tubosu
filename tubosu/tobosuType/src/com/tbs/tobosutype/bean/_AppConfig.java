package com.tbs.tobosutype.bean;

import java.util.List;

/**
 * Created by Mr.Lin on 2018/1/20 17:25.
 * 获取后台的配置信息
 * 1.基本的信息配置
 * 2.获取报价相关的信息
 */

public class _AppConfig {

    /**
     * cellphone_partern : XigoMTJbNCw2XSl8KDEzWzAtOV0pfCgxNVswLTNdKXwoMTVbNS05XSl8KDE2WzAtOV0pfCgxN1swLTldKXwoMTRbNSw3XSl8KDE4WzAtOV0pfCgxOTkpKVxkezh9JA==
     * custom_service_tel : 400-696-2221
     * custom_service_qq : 4006062221
     * applets_name : 土拨鼠查订单
     * official_accounts : itobosu
     * order_links : [{"code":"tbsaj01","url":""},{"code":"tbsaj02","url":""},{"code":"tbsaj03","url":""},{"code":"tbsaj04","url":""},{"code":"tbsaj05","url":""},{"code":"tbsaj06","url":""},{"code":"tbsaj07","url":""},{"code":"tbsaj08","url":""},{"code":"tbsaj09","url":""},{"code":"tbsaj10","url":""},{"code":"tbsaj11","url":""},{"code":"tbsaj12","url":""},{"code":"tbsaj13","url":""},{"code":"tbsaj14","url":""},{"code":"tbsaj15","url":""},{"code":"tbsaj16","url":""},{"code":"tbsaj17","url":""},{"code":"tbsaj18","url":""},{"code":"tbsaj19","url":""},{"code":"tbsaj20","url":""},{"code":"tbsaj21","url":""},{"code":"tbsaj22","url":""},{"code":"tbsaj23","url":""},{"code":"tbsaj24","url":""},{"code":"tbsaj25","url":""},{"code":"tbsaj26","url":""},{"code":"tbsaj27","url":""},{"code":"tbsaj28","url":""},{"code":"tbsaj29","url":""},{"code":"tbsaj30","url":""},{"code":"tbsaj31","url":"http://www.google.com?channel=app&subchannel=android&chcode=ali&app_type=1"},{"code":"tbsaj32","url":"http://www.baidu.com?channel=app&subchannel=android&chcode=ali&app_type=1"},{"code":"tbsaj33","url":""}]
     */

    private String cellphone_partern;
    private String custom_service_tel;
    private String custom_service_qq;
    private String applets_name;
    private String official_accounts;
    private List<OrderLinksBean> order_links;

    public String getCellphone_partern() {
        return cellphone_partern;
    }

    public void setCellphone_partern(String cellphone_partern) {
        this.cellphone_partern = cellphone_partern;
    }

    public String getCustom_service_tel() {
        return custom_service_tel;
    }

    public void setCustom_service_tel(String custom_service_tel) {
        this.custom_service_tel = custom_service_tel;
    }

    public String getCustom_service_qq() {
        return custom_service_qq;
    }

    public void setCustom_service_qq(String custom_service_qq) {
        this.custom_service_qq = custom_service_qq;
    }

    public String getApplets_name() {
        return applets_name;
    }

    public void setApplets_name(String applets_name) {
        this.applets_name = applets_name;
    }

    public String getOfficial_accounts() {
        return official_accounts;
    }

    public void setOfficial_accounts(String official_accounts) {
        this.official_accounts = official_accounts;
    }

    public List<OrderLinksBean> getOrder_links() {
        return order_links;
    }

    public void setOrder_links(List<OrderLinksBean> order_links) {
        this.order_links = order_links;
    }

    public static class OrderLinksBean {
        /**
         * code : tbsaj01
         * url :
         */

        private String code;
        private String url;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
