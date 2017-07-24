package com.tbs.tobosupicture.bean;

import java.util.ArrayList;

/**搜索记录对象
 * Created by Lie on 2017/7/21.
 */

public class SearchDataEntity {
    private ArrayList<SearchRecordBean> search_record;  //搜索记录


    public ArrayList<SearchRecordBean> getSearch_record() {
        return search_record;
    }

    public void setSearch_record(ArrayList<SearchRecordBean> search_record) {
        this.search_record = search_record;
    }
}
