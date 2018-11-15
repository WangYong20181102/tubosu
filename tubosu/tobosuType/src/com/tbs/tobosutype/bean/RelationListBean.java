package com.tbs.tobosutype.bean;

/**
 * Created by Mr.Wang on 2018/11/14 10:22.
 */
public class RelationListBean {
    private String question_id;     //相关问题ID
    private String title;   //相关问题标题

    @Override
    public String toString() {
        return "RelationListBean{" +
                "question_id='" + question_id + '\'' +
                ", title='" + title + '\'' +
                '}';
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
}
