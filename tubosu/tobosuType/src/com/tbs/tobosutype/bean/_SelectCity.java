package com.tbs.tobosutype.bean;

/**
 * Created by Mr.Lin on 2018/2/24 17:57.
 * 选择城市
 */

public class _SelectCity {
    private String cityName;
    private String cityId;

    public _SelectCity() {

    }

    public _SelectCity(String cityName, String cityId) {
        this.cityId = cityId;
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
}
