package com.tbs.tobosutype.bean;

/**
 * Created by Lie on 2017/11/11.
 */

public class MyFavItem {
    /**
     * single_counts : 8
     * suite_counts : 9
     * company_counts : 0
     */
    private String single_counts;
    private String suite_counts;
    private String company_counts;

    public String getSingle_counts() {
        return single_counts;
    }

    public void setSingle_counts(String single_counts) {
        this.single_counts = single_counts;
    }

    public String getSuite_counts() {
        return suite_counts;
    }

    public void setSuite_counts(String suite_counts) {
        this.suite_counts = suite_counts;
    }

    public String getCompany_counts() {
        return company_counts;
    }

    public void setCompany_counts(String company_counts) {
        this.company_counts = company_counts;
    }
}
