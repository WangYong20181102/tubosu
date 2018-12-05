package com.tbs.tobosutype.bean;

import java.io.Serializable;

/**
 * Created by Mr.Wang on 2018/11/13 11:03.
 * 问答首页
 */
public class AskQuestionBean implements Serializable {
    private String question_id; //问题id
    private String title; //问题标题
    private String category_id; //问题分类id
    private String content; //问题内容
    private String answer_count; //问题答案数量
    private String add_time; //问题添加时间
    private String id; //广告id
    private String jump_url; //广告跳转链接
    private String[] img_urls; //问题图片url
    private String icon; //回答者头像
    private String published_uid; //提问者ID
    private String name;
    private String view_count;  //浏览量

    public String getView_count() {
        return view_count;
    }

    public void setView_count(String view_count) {
        this.view_count = view_count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //    private String answer_count; //问题答案数量


    public String[] getImg_urls() {
        return img_urls;
    }

    public void setImg_urls(String[] img_urls) {
        this.img_urls = img_urls;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getAnswer_count() {
        return answer_count;
    }

    public void setAnswer_count(String answer_count) {
        this.answer_count = answer_count;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJump_url() {
        return jump_url;
    }

    public void setJump_url(String jump_url) {
        this.jump_url = jump_url;
    }

    public String getIcon()  {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPublished_uid() {
        return published_uid;
    }

    public void setPublished_uid(String published_uid) {
        this.published_uid = published_uid;
    }

    @Override
    public String toString() {
        return "AskQuestionBean{" +
                "question_id='" + question_id + '\'' +
                ", title='" + title + '\'' +
                ", category_id='" + category_id + '\'' +
                ", content='" + content + '\'' +
                ", answer_count='" + answer_count + '\'' +
                ", add_time='" + add_time + '\'' +
                ", id='" + id + '\'' +
                ", jump_url='" + jump_url + '\'' +
                ", icon='" + icon + '\'' +
                ", published_uid='" + published_uid + '\'' +
                '}';
    }
}
