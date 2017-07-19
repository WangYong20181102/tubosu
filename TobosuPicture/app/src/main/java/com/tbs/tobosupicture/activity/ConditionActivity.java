package com.tbs.tobosupicture.activity;

import android.os.Bundle;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseActivity;

/**
 * Created by Lie on 2017/7/19.
 */

public class ConditionActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = "ConditionActivity";
        mContext = ConditionActivity.this;
        setContentView(R.layout.activity_condition);

    }
}
