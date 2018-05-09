package com.tbs.tbs_mj.bean;

/**
 * Created by Mr.Lin on 2018/1/20 17:25.
 */

public class _AppConfig {

    /**
     * cellphone_partern : XigoMTJbNCw2XSl8KDEzWzAtOV0pfCgxNVswLTNdKXwoMTVbNS05XSl8KDE2WzAtOV0pfCgxN1swLTldKXwoMTRbNSw3XSl8KDE4WzAtOV0pfCgxOTkpKVxkezh9JA==
     * custom_service_tel : 400-696-2221
     * custom_service_qq : 4006062221
     * applets_name : 土拨鼠查订单
     * public_number : itobosu
     */

    private String cellphone_partern;
    private String custom_service_tel;
    private String custom_service_qq;
    private String applets_name;
    private String official_accounts;

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

    public String getPublic_number() {
        return official_accounts;
    }

    public void setPublic_number(String public_number) {
        this.official_accounts = public_number;
    }
}
