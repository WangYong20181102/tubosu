package com.tbs.tobosutype.bean;

/**
 * Created by Mr.Wang on 2018/11/14 11:11.
 */
public class AskListBean {
    private String answer_comment_id;   //评论ID号
    private String answer_id;   //答案ID号
    private String comment_uid;   //评论人ID号
    private String comment_id;
    private String message;   //评论内容
    private String recomment_uid;   //评论对象ID号
    private String add_time;   //答案添加时间
    private String icon;   //回答者头像
    private int is_agree;   //是否点赞：1、是 2、否
    private String agree_count = ""; //点赞总数
    private String comment_name;    //评论人
    private String recomment_name;  //被评论人

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_name() {
        return comment_name;
    }

    public void setComment_name(String comment_name) {
        this.comment_name = comment_name;
    }

    public String getRecomment_name() {
        return recomment_name;
    }

    public void setRecomment_name(String recomment_name) {
        this.recomment_name = recomment_name;
    }

    public String getAgree_count() {
        return agree_count;
    }

    public void setAgree_count(String agree_count) {
        this.agree_count = agree_count;
    }

    public String getAnswer_comment_id() {
        return answer_comment_id;
    }

    public void setAnswer_comment_id(String answer_comment_id) {
        this.answer_comment_id = answer_comment_id;
    }

    public String getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
    }

    public String getComment_uid() {
        return comment_uid;
    }

    public void setComment_uid(String comment_uid) {
        this.comment_uid = comment_uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecomment_uid() {
        return recomment_uid;
    }

    public void setRecomment_uid(String recomment_uid) {
        this.recomment_uid = recomment_uid;
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
        return "AskListBean{" +
                "answer_comment_id='" + answer_comment_id + '\'' +
                ", answer_id='" + answer_id + '\'' +
                ", comment_uid='" + comment_uid + '\'' +
                ", message='" + message + '\'' +
                ", recomment_uid='" + recomment_uid + '\'' +
                ", add_time='" + add_time + '\'' +
                ", icon='" + icon + '\'' +
                ", is_agree=" + is_agree +
                '}';
    }
}
