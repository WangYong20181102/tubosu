package com.tbs.tobosutype.bean;

import java.util.List;

/**
 * Created by Mr.Wang on 2018/11/14 13:57.
 */
public class AskDetailDataBean {
    private AskQuestionBean questionList;
    private List<AnswerListBean> answerList;
    private List<RelationListBean> relationList;
    private List<AdvertBean> advert;
    private String share_url;//分享url

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public List<AdvertBean> getAdvert() {
        return advert;
    }

    public void setAdvert(List<AdvertBean> advert) {
        this.advert = advert;
    }

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
