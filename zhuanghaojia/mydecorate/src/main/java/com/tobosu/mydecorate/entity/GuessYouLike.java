package com.tobosu.mydecorate.entity;

import java.util.List;

/**
 * 猜你喜欢实体类
 * Created by Lie on 2017/6/2.
 */

public class GuessYouLike {


    /**
     * status : 200
     * msg : success
     * data : [{"key_word":"厨房"},{"key_word":"家居生活"},{"key_word":"家装嘻嘻"},{"key_word":"家装"},{"key_word":"装修宝典"},{"key_word":"装好家"},{"key_word":"格调"},{"key_word":"家居"},{"key_word":"饮食"}]
     */

    private int status;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * key_word : 厨房
         */

        private String key_word;

        public String getKey_word() {
            return key_word;
        }

        public void setKey_word(String key_word) {
            this.key_word = key_word;
        }
    }
}
