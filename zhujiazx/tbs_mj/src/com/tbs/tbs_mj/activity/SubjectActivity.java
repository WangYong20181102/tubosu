package com.tbs.tbs_mj.activity;

import android.os.Bundle;

import com.tbs.tbs_mj.R;

/**
 * create by lin
 * 专题详情
 * 采用列表的形式展示
 * 列表中 头部的显示 包含了标题 简介
 * 列表中 图文详情排版 图-文 图-文  数据为空的情况下不显示
 */
public class SubjectActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
    }
}
