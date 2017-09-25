package com.tbs.tobosutype.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.utils.ImageLoaderUtil;

public class TestActivity extends BaseActivity {
    private Context mContext;
    private ImageView test_imageview;
    private String imageUrl = "https://opic.tbscache.com/manage/case/2015/07-07/small/32856fb1-b7a3-eb64-c1b6-756a010ba703.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mContext = this;
        test_imageview = (ImageView) findViewById(R.id.test_imageview);
        initView();
    }

    private void initView() {
        ImageLoaderUtil.loadImage(mContext, test_imageview, imageUrl);
    }
}
