package com.tbs.tobosutype.bean;

import java.util.List;

/**
 * Created by Mr.Wang on 2018/11/14 14:32.
 */
public class AskListDataBean {
    /**
     * 评论总数
     */
    private String commentCount;
    /**
     * 评论内容集合
     */
    private List<AskListBean> commentList;

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public List<AskListBean> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<AskListBean> commentList) {
        this.commentList = commentList;
    }
}
