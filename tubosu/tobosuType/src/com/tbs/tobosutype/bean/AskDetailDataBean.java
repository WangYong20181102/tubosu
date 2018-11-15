package com.tbs.tobosutype.bean;

import java.util.List;

/**
 * Created by Mr.Wang on 2018/11/14 13:57.
 */
public class AskDetailDataBean {
    private AskQuestionBean questionList;
    private List<AnswerListBean> answerList;
    private List<RelationListBean> relationList;

    public AskQuestionBean getQuestionList() {
        return questionList;
    }

    public void setQuestionList(AskQuestionBean questionList) {
        this.questionList = questionList;
    }

    public List<AnswerListBean> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<AnswerListBean> answerList) {
        this.answerList = answerList;
    }

    public List<RelationListBean> getRelationList() {
        return relationList;
    }

    public void setRelationList(List<RelationListBean> relationList) {
        this.relationList = relationList;
    }
}
