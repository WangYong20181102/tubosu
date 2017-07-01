package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.utils.SpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 用户第一次使用我们的App时的启动页
 * 主要放置一些滑动切片 利用fragment 作为切片的载体
 */
public class FristActivity extends AppCompatActivity {
    @BindView(R.id.frist_viewpager)
    ViewPager fristViewpager;
    @BindView(R.id.frist_into_main)
    TextView fristIntoMain;
    private String TAG = "FristActivity";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frist);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    private void initView() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.frist_into_main)
    public void onFristActivityClick() {
        SpUtils.setUserIsFristLogin(mContext, "alreadyLogin");
        startActivity(new Intent(FristActivity.this, MainActivity.class));
        FristActivity.this.finish();
    }
}
