package com.tbs.tobosupicture.bean;

import java.util.List;

/**
 * 用户搜索记录
 * Created by Lie on 2017/8/16.
 */

public class OwnerSearchRecordJsonEntity {


    /**
     * status : 200
     * msg : success
     * data : [{"id":"81","uid":"332598","new_case_count":"0","add_time":"2017-08-04","search_condition":"深圳 60~90㎡ 2室 5~10万"},{"id":"80","uid":"332598","new_case_count":"0","add_time":"2017-08-04","search_condition":"深圳 60~90㎡ 2室"},{"id":"79","uid":"332598","new_case_count":"0","add_time":"2017-08-04","search_condition":"深圳 2室 现代简约"},{"id":"78","uid":"332598","new_case_count":"0","add_time":"2017-08-04","search_condition":"深圳 2室 简约"},{"id":"77","uid":"332598","new_case_count":"0","add_time":"2017-08-04","search_condition":"深圳 2室"},{"id":"76","uid":"332598","new_case_count":"0","add_time":"2017-08-04","search_condition":"深圳"}]
     */

    private int status;
    private String msg;
    private List<OwnerSearchRecordEntity> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<OwnerSearchRecordEntity> getData() {
        return data;
    }

    public void setData(List<OwnerSearchRecordEntity> data) {
        this.data = data;
    }

}
