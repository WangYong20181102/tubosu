package com.tbs.tbs_mj.bean;

/**
 * Created by Mr.Lin on 2018/3/29 11:56.
 */
public class _OrderNotice {

    /**
     * id : 6
     * content : 贵公司有来自罗湖区的新订单，订单号：2301932，请点击查看，建议第一时间联系业主！为保证取得业主信任，请告知土拨鼠推荐，谢谢合作！
     * is_read : 1
     * order_id : 2301932
     * add_time : 2018-03-28
     * com_order_id : 497406
     */

    private String id;
    private String content;
    private String is_read;
    private String order_id;
    private String add_time;
    private String com_order_id;

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

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getCom_order_id() {
        return com_order_id;
    }

    public void setCom_order_id(String com_order_id) {
        this.com_order_id = com_order_id;
    }
}
