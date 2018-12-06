package com.tbs.tobosutype.bean;

/**
 * Created by Mr.Wang on 2018/12/6 10:17.
 */
public class MessageCenterBean {

    private String id;  //id
    private String title;  //标题
    private String question_id;
    private String content;//内容
    private String is_see;//是否查看
    private String add_time;    //日期
    private String icon;    //头像 (0查看，未查看)

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIs_see() {
        return is_see;
    }

    public void setIs_see(String is_see) {
        this.is_see = is_see;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
