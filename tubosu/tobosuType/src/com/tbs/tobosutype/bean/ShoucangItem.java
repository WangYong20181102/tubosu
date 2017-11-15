package com.tbs.tobosutype.bean;

/**
 * Created by Lie on 2017/10/9.
 */

public class ShoucangItem {
    private String name;
    private String count;
    private String icon;


    public ShoucangItem(String name, String count, String icon) {
        this.name = name;
        this.count = count;
        this.icon = icon;
    }

    public ShoucangItem() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
