package com.tobosu.mydecorate.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by dec on 2017/2/16.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG = BaseActivity.class.getSimpleName();
    protected Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLayout();
        initView();
        initNetData();
        setClickListener();
    }

    protected abstract void setLayout();
    protected abstract void initView();
    protected abstract void initNetData();
    protected abstract void setClickListener() ;
}
