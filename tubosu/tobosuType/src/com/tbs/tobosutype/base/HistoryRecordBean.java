package com.tbs.tobosutype.base;

/**
 * Created by Mr.Wang on 2019/1/2 14:21.
 */
public class HistoryRecordBean {
    private String data;    //日期
    private String num; //数量
    private String value;//价格
    private boolean isCheck;    //选中状态

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
