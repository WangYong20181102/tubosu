package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.fragment.DecorationCaseFragment;
import com.tbs.tobosupicture.fragment.ImageToFriendFragment;
import com.tbs.tobosupicture.fragment.MineFragment;
import com.tbs.tobosupicture.fragment.TemplateFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.main_frameLayout)
    FrameLayout mainFrameLayout;
    @BindView(R.id.rb_first)
    RadioButton rbFirst;
    @BindView(R.id.rb_second)
    RadioButton rbSecond;
    @BindView(R.id.rb_third)
    RadioButton rbThird;
    @BindView(R.id.rb_fourth)
    RadioButton rbFourth;
    @BindView(R.id.main_radioGroup)
    RadioGroup mainRadioGroup;

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
        MineFragment mineFragment = new MineFragment();
    }

    @OnClick({R.id.rb_first, R.id.rb_second, R.id.rb_third, R.id.rb_fourth})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_first:
                //点击第一个选项 显示样板图
                Log.e(TAG, "点击了样板图");
                break;
            case R.id.rb_second:
                //点击第二个选项 显示案例
                break;
            case R.id.rb_third:
                //点击第三个选项 显示以图会友
                break;
            case R.id.rb_fourth:
                //点击第四个选项 显示我的  其中我的界面要分情况考虑
                break;
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
