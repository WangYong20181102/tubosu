package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.os.Bundle;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.fragment.DecorationCaseFragment;
import com.tbs.tobosupicture.fragment.ImageToFriendFragment;
import com.tbs.tobosupicture.fragment.MineFragment;
import com.tbs.tobosupicture.fragment.TemplateFragment;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    private String TAG = "MainActivity";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        initFragment();
    }

    private void initFragment() {
        //样板图
        TemplateFragment templateFragment = new TemplateFragment();
        //案例
        DecorationCaseFragment decorationCaseFragment = new DecorationCaseFragment();
        //以图会友
        ImageToFriendFragment imageToFriendFragment = new ImageToFriendFragment();
        //我的
        MineFragment mineFragment=new MineFragment();
    }
}
