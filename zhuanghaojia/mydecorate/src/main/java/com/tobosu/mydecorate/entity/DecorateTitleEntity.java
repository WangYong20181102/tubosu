package com.tobosu.mydecorate.entity;

import java.util.List;

/**
 * 装修宝典 title
 * Created by Lie on 2017/6/3.
 */

public class DecorateTitleEntity {

    /**
     * status : 200
     * msg : success
     * data : [{"id":"11","name":"工装"},{"id":"10","name":"软装"},{"id":"9","name":"家电"},{"id":"8","name":"家具"},{"id":"7","name":"评测"},{"id":"6","name":"专题"},{"id":"5","name":"风水"},{"id":"4","name":"家居"},{"id":"3","name":"建材"},{"id":"2","name":"家装"},{"id":"1","name":"设计"}]
     */

    private int status;
    private String msg;
    private List<ChannelItem> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ChannelItem> getData() {
        return data;
    }

    public void setData(List<ChannelItem> data) {
        this.data = data;
    }

    public static class ChannelItem {
        /**
         * 栏目对应ID
         *  */
        public Integer id;
        /**
         * 栏目对应NAME
         *  */
        public String name;
        /**
         * 栏目在整体中的排序顺序  rank
         *  */
        public Integer orderId;
        /**
         * 栏目是否选中
         *  */
        public Integer selected;


        public ChannelItem() {

        }

        public ChannelItem(Integer id, String name, Integer orderId, Integer selected) {
            this.id = id;
            this.name = name;
            this.orderId = orderId;
            this.selected = selected;
        }


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getOrderId() {
            return orderId;
        }

        public void setOrderId(Integer orderId) {
            this.orderId = orderId;
        }

        public Integer getSelected() {
            return selected;
        }

        public void setSelected(Integer selected) {
            this.selected = selected;
        }
    }
}
