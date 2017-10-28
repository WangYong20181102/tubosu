package com.tbs.tobosutype.activity;

import android.content.Context;
import android.os.Bundle;
import com.tbs.tobosutype.R;
import cn.jpush.android.api.InstrumentedActivity;

/**
 * Created by Lie on 2017/10/28.
 */

public class JPushMessageActivity extends InstrumentedActivity {
    private Context mContext;
    private String TAG = "JPushMessageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = JPushMessageActivity.this;
        TAG = JPushMessageActivity.class.getSimpleName();
        setContentView(R.layout.activity_jpush_message);
    }
}
