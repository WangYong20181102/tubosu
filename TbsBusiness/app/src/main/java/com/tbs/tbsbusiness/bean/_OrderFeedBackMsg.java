package com.tbs.tbsbusiness.bean;

/**
 * Created by Mr.Lin on 2018/6/6 10:14.
 */
public class _OrderFeedBackMsg {

    /**
     * send_type : 3  消息类型
     * content : 03月29日 16:09
     */

    private int send_type;
    private String content;

    public int getSend_type() {
        return send_type;
    }

    public void setSend_type(int send_type) {
        this.send_type = send_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
