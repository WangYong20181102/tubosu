package com.tbs.tbsbusiness.activity;

import android.content.Context;
import android.content.Intent;
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
import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.adapter.MyFragmentPagerAdapter;
import com.tbs.tbsbusiness.base.BaseActivity;
import com.tbs.tbsbusiness.bean.EC;
import com.tbs.tbsbusiness.bean.Event;
import com.tbs.tbsbusiness.config.MyApplication;
import com.tbs.tbsbusiness.fragment.LoginFragmentAccount;
import com.tbs.tbsbusiness.fragment.LoginFragmentPhone;
import com.tbs.tbsbusiness.util.SpUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * 登录页面
 * 分别为手机登录和账号
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.new_login_with_phone)
    RelativeLayout newLoginWithPhone;
    @BindView(R.id.new_login_with_account)
    RelativeLayout newLoginWithAccount;
    @BindView(R.id.new_login_left_sanjiao)
    ImageView newLoginLeftSanjiao;
    @BindView(R.id.new_login_right_sanjiao)
    ImageView newLoginRightSanjiao;
    @BindView(R.id.new_login_viewpager)
    ViewPager newLoginViewpager;
    private String TAG = "NewLoginActivity";
    private Gson mGson;
    private Context mContext;
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private Intent mIntent;
    private String mWhereComeFrom = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.NOTIC_LOGINACTIVITY_FNISHI:
                finish();
                break;
        }
    }

    //初始化相关事件
    private void initViewEvent() {
        mIntent = getIntent();
        mWhereComeFrom = mIntent.getStringExtra("mWhereComeFrom");
        //获取推送的唯一设备信息  防止在初期没有拿到
        initJpush();
        //设置显示的两个布局
        fragmentArrayList.add(new LoginFragmentPhone());
        fragmentArrayList.add(new LoginFragmentAccount());
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentArrayList);
        newLoginViewpager.setAdapter(myFragmentPagerAdapter);
        newLoginViewpager.setCurrentItem(0);
        newLoginViewpager.addOnPageChangeListener(onPageChangeListener);
    }

    private void initJpush() {
        //防护措施  防止在welcome页面中没有获取到id  保证一定能获取到推送唯一id
        if (TextUtils.isEmpty(SpUtil.getPushRegisterId(mContext))) {
            JPushInterface.init(mContext);
            SpUtil.setPushRegisterId(MyApplication.getContext(), JPushInterface.getRegistrationID(MyApplication.getContext()));
            Log.e(TAG, "初始化推送=============" + SpUtil.getPushRegisterId(mContext));
        }
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


    @OnClick({R.id.new_login_with_phone, R.id.new_login_with_account})
    public void onViewClicked(View view) {
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
        }
    }
}
