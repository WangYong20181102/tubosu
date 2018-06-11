package com.tbs.tbsbusiness.bean;

/**
 * Created by Mr.Lin on 2018/6/7 10:52.
 */
public class _ImageUpload {
    /**
     * status : 200
     * msg : success
     * data : {"url":"http://cdn111.dev.tobosu.com/app/2017-08-25/599fcd71af5f3.jpg"}
     */

    private int status;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * url : http://cdn111.dev.tobosu.com/app/2017-08-25/599fcd71af5f3.jpg
         */

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
