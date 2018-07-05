package com.tbs.tbs_mj.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.adapter.MyFragmentPagerAdapter;
import com.tbs.tbs_mj.base.*;
import com.tbs.tbs_mj.bean.EC;
import com.tbs.tbs_mj.bean.Event;
import com.tbs.tbs_mj.bean._AppEvent;
import com.tbs.tbs_mj.fragment.NewLoginFragmentAccount;
import com.tbs.tbs_mj.fragment.NewLoginFragmentPhone;
import com.tbs.tbs_mj.utils.AppInfoUtil;
import com.tbs.tbs_mj.utils.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新的登录页面  3.7版本新增
 * 宿主承载页面：承载账号登录以及微信快捷登录
 */

public class NewLoginActivity extends com.tbs.tbs_mj.base.BaseActivity{
    @BindView(R.id.new_login_close)
    RelativeLayout newLoginClose;
    @BindView(R.id.new_login_left_sanjiao)
    ImageView newLoginLeftSanjiao;
    @BindView(R.id.new_login_right_sanjiao)
    ImageView newLoginRightSanjiao;
    @BindView(R.id.new_login_viewpager)
    ViewPager newLoginViewpager;
    @BindView(R.id.new_login_with_phone)
    RelativeLayout newLoginWithPhone;
    @BindView(R.id.new_login_with_account)
    RelativeLayout newLoginWithAccount;
    private String TAG = "NewLoginActivity";
    private Gson mGson;
    private Context mContext;
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private Intent mIntent;
    private String mWhereComeFrom = "";

    // TODO: 2018/2/27 点击流相关的属性
    private String mFrom = "";//从那个界面进来的
    private String mNowActivity = "NewLoginActivity";//当前页面的名称
    private String mTo = "";// TODO: 2018/2/27  要去的界面 作用：在另一个界面回退时 mTo转换成mFrom 使得回退时可以知道从哪回到这个界面的


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
        Log.e(TAG, "执行onCreate========");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "执行onStart========");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "执行onRestart========");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "执行onResume========");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "执行onPause========");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "执行onStop========");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "执行onDestroy========");
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.CLOSE_NEW_LOGIN_ACTIVITY:
            case EC.EventCode.BAND_PHONE_SUCCESS:
                //处理在两个fragment中用户登录之后将这个页面关闭
                if (!TextUtils.isEmpty(mWhereComeFrom) && mWhereComeFrom.equals("NoneLoginOfMineActivity")) {
                    AppInfoUtil.ISJUSTLOGIN = true;
                    Log.e(TAG, "处理登录==============将重置登录属性==========");
                }
                Log.e(TAG, "处理登录==============关闭登录页面==========");
                this.finish();
                break;
        }
    }

    private void initViewEvent() {
        mIntent = getIntent();
        mWhereComeFrom = mIntent.getStringExtra("mWhereComeFrom");
        //设置显示的两个布局
        fragmentArrayList.add(new NewLoginFragmentPhone());
        fragmentArrayList.add(new NewLoginFragmentAccount());
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentArrayList);
        newLoginViewpager.setAdapter(myFragmentPagerAdapter);
        newLoginViewpager.setCurrentItem(0);
        newLoginViewpager.addOnPageChangeListener(onPageChangeListener);
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                newLoginLeftSanjiao.setVisibility(View.VISIBLE);
                newLoginRightSanjiao.setVisibility(View.INVISIBLE);
            } else {
                newLoginLeftSanjiao.setVisibility(View.INVISIBLE);
                newLoginRightSanjiao.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @OnClick({R.id.new_login_with_phone, R.id.new_login_with_account, R.id.new_login_close})
    public void onViewClickedInNewLoginActivity(View view) {
        switch (view.getId()) {
            case R.id.new_login_with_phone:
                //点击了手机登录滑块
                newLoginLeftSanjiao.setVisibility(View.VISIBLE);
                newLoginRightSanjiao.setVisibility(View.INVISIBLE);
                newLoginViewpager.setCurrentItem(0);
                break;
            case R.id.new_login_with_account:
                //点击账号登录的滑块
                newLoginLeftSanjiao.setVisibility(View.INVISIBLE);
                newLoginRightSanjiao.setVisibility(View.VISIBLE);
                newLoginViewpager.setCurrentItem(1);
                break;
            case R.id.new_login_close:
                Log.e(TAG, "执行==点击关闭按钮====");
                // TODO: 2018/2/27 设置点击了关闭按钮事件码
                finish();
                break;
        }
    }
}
