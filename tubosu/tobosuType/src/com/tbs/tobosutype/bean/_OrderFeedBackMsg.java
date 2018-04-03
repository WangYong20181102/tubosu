package com.tbs.tobosutype.bean;

/**
 * Created by Mr.Lin on 2018/3/30 11:41.
 * 订单反馈实体类
 * 作用于反馈列表页
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
