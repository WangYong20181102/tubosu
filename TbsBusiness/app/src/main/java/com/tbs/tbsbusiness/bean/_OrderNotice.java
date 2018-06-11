package com.tbs.tbsbusiness.bean;

/**
 * Created by Mr.Lin on 2018/6/11 09:19.
 * 推送消息实体类
 */
public class _OrderNotice {
    private String id;//推送的id
    private String content;//推送的内容
    private String is_read;//是否已读
    private String com_order_id;//装修公司的分单id号
    private String notice_type;//通知类型
    private String add_time;//消息推送时间
    private String order_id;//订单id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    public String getCom_order_id() {
        return com_order_id;
    }

    public void setCom_order_id(String com_order_id) {
        this.com_order_id = com_order_id;
    }

    public String getNotice_type() {
        return notice_type;
    }

    public void setNotice_type(String notice_type) {
        this.notice_type = notice_type;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}
