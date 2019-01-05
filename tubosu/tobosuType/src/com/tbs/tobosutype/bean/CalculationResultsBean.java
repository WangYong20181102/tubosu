package com.tbs.tobosutype.bean;

import java.io.Serializable;

/**
 * Created by Mr.Wang on 2019/1/4 16:32.
 * 装修工具计算结果
 */
public class CalculationResultsBean implements Serializable {
    private String number;//数量
    private String total_price = "";//价格
    private String record_id = "";

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    @Override
    public String toString() {
        return "CalculationResultsBean{" +
                "number='" + number + '\'' +
                ", total_price='" + total_price + '\'' +
                ", record_id='" + record_id + '\'' +
                '}';
    }
}
