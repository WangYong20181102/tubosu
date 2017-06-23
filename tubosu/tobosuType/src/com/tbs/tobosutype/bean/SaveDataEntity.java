package com.tbs.tobosutype.bean;

/**
 * Created by Lie on 2017/6/23.
 */

public class SaveDataEntity {
    private String name;
    private String money;
    private String time;
    private String content;
    private String id;

    public SaveDataEntity(){}

    public SaveDataEntity(String name, String money, String time, String content, String id) {
        this.name = name;
        this.money = money;
        this.time = time;
        this.content = content;
        this.id = id;
    }

    public String[] getDataArray(){
        return new String[]{name, money, time, content, id};
    }
}
