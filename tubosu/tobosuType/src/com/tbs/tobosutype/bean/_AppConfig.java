package com.tbs.tobosutype.bean;

/**
 * Created by Mr.Lin on 2018/1/20 17:25.
 */

public class _AppConfig {

    /**
     * cellphone_partern : ^((12[4,6])|(13[0-9])|(15[0-3])|(15[5-9])|(16[0-9])|(17[0-9])|(14[5,7])|(18[0-9])|(199))\d{8}$
     */

    private String cellphone_partern;

    public String getCellphone_partern() {
        return cellphone_partern;
    }

    public void setCellphone_partern(String cellphone_partern) {
        this.cellphone_partern = cellphone_partern;
    }
}
