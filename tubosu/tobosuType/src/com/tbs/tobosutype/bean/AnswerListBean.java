package com.tbs.tobosutype.bean;

/**
 * Created by Mr.Wang on 2018/11/14 09:31.
 * 问答列表类
 */
public class AnswerListBean {
    private String name;    //姓名
    private String answer_id;   //回答ID号
    private String question_id;     //问题ID号
    private String answer_content;  //回答的内容
    private String agree_count = ""; //赞同数
    private String answer_uid;  //回答者ID号
    private String comment_count;   //回答评论数
    private String[] img_urls; //回答内容图片url
    private String add_time;    //回答添加时间
    private String icon;    //回答者头像
    private int is_agree;    //是否点赞：1、是 2、否
    private String title;   //标题

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getAnswer_content() {
        return answer_content;
    }

    public void setAnswer_content(String answer_content) {
        this.answer_content = answer_content;
    }

    public String getAgree_count() {
        return agree_count;
    }

    public void setAgree_count(String agree_count) {
        this.agree_count = agree_count;
    }

    public String getAnswer_uid() {
        return answer_uid;
    }

    public void setAnswer_uid(String answer_uid) {
        this.answer_uid = answer_uid;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String[] getImg_urls() {
        return img_urls;
    }

    public void setImg_urls(String[] img_urls) {
        this.img_urls = img_urls;
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

    public int getIs_agree() {
        return is_agree;
    }

    public void setIs_agree(int is_agree) {
        this.is_agree = is_agree;
    }

    @Override
    public String toString() {
        return "AnswerListBean{" +
                "answer_id='" + answer_id + '\'' +
                ", question_id='" + question_id + '\'' +
                ", answer_content='" + answer_content + '\'' +
                ", agree_count='" + agree_count + '\'' +
                ", answer_uid='" + answer_uid + '\'' +
                ", comment_count='" + comment_count + '\'' +
                ", img_url='" + img_urls + '\'' +
                ", add_time='" + add_time + '\'' +
                ", icon='" + icon + '\'' +
                ", is_agree=" + is_agree +
                '}';
    }
}
