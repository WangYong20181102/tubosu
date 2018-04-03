package com.tbs.tobosutype.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.androidkun.xtablayout.XTabLayout;
import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.MyFragmentPagerStateAdapter;
import com.tbs.tobosutype.base.*;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.bean._AllOrderTab;
import com.tbs.tobosutype.fragment.OrderFragment;
import com.tbs.tobosutype.utils.EventBusUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 全部订单页面
 * 3.7.2版本新增
 */
public class AllOrderActivity extends com.tbs.tobosutype.base.BaseActivity {
    @BindView(R.id.all_order_back_rl)
    RelativeLayout allOrderBackRl;
    @BindView(R.id.all_order_search_rl)
    RelativeLayout allOrderSearchRl;
    @BindView(R.id.all_order_banner_rl)
    RelativeLayout allOrderBannerRl;
    @BindView(R.id.all_order_tab_layout)
    XTabLayout allOrderTabLayout;
    @BindView(R.id.all_order_viewpager)
    ViewPager allOrderViewpager;
    private String TAG = "AllOrderActivity";
    private Context mContext;
    private Gson mGson;
    private Intent mIntent;
    private String mIndex;
    //标签集合
    private ArrayList<_AllOrderTab> allOrderTabArrayList;
    //fragment 数据集合
    private ArrayList<Fragment> mFragmentArrayList = new ArrayList<>();
    //viewPage适配器
    private MyFragmentPagerStateAdapter myFragmentPagerStateAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_order);
        ButterKnife.bind(this);
        mContext = this;
        initEvent();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    private void initEvent() {
        mGson = new Gson();
        mIntent = getIntent();
        mIndex = mIntent.getStringExtra("mIndex");
        initAllOrederTabList();
        initViewEvent();
        if (mIndex != null && !TextUtils.isEmpty(mIndex)) {
            allOrderViewpager.setCurrentItem(Integer.parseInt(mIndex));
        }
    }

    private void initAllOrederTabList() {
        allOrderTabArrayList = new ArrayList<>();
        //全部
        _AllOrderTab orderTab0 = new _AllOrderTab("全部", "0");
        allOrderTabArrayList.add(orderTab0);
        //新订单
        _AllOrderTab orderTab1 = new _AllOrderTab("新订单", "1");
        allOrderTabArrayList.add(orderTab1);
        //未量房
        _AllOrderTab orderTab2 = new _AllOrderTab("未量房", "2");
        allOrderTabArrayList.add(orderTab2);
        //已量房
        _AllOrderTab orderTab3 = new _AllOrderTab("已量房", "3");
        allOrderTabArrayList.add(orderTab3);
        //已签单
        _AllOrderTab orderTab4 = new _AllOrderTab("已签单", "4");
        allOrderTabArrayList.add(orderTab4);
        //未签单
        _AllOrderTab orderTab5 = new _AllOrderTab("未签单", "5");
        allOrderTabArrayList.add(orderTab5);
    }

    //初始化页面的相关事件
    private void initViewEvent() {
        for (int i = 0; i < allOrderTabArrayList.size(); i++) {
            mFragmentArrayList.add(OrderFragment.newInstance(allOrderTabArrayList.get(i).getOrderType()));
        }
        myFragmentPagerStateAdapter = new MyFragmentPagerStateAdapter(getSupportFragmentManager(), mFragmentArrayList);
        allOrderViewpager.setAdapter(myFragmentPagerStateAdapter);
        allOrderTabLayout.setupWithViewPager(allOrderViewpager);
        allOrderViewpager.setOffscreenPageLimit(allOrderTabArrayList.size());
        allOrderViewpager.addOnPageChangeListener(onPageChangeListener);
        myFragmentPagerStateAdapter.notifyDataSetChanged();
        for (int i = 0; i < allOrderTabArrayList.size(); i++) {
            allOrderTabLayout.getTabAt(i).setText("" + allOrderTabArrayList.get(i).getOrderTabName());
        }
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            // TODO: 2018/3/22 页面切换发送通知更新数据

            EventBusUtil.sendEvent(new Event(EC.EventCode.NOTE_ORDER_FRAGMENT_UPDATE_DATA));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @OnClick({R.id.all_order_back_rl, R.id.all_order_search_rl})
    public void onViewClickedOnAllOrderActivity(View view) {
        switch (view.getId()) {
            case R.id.all_order_back_rl:
                finish();
                break;
            case R.id.all_order_search_rl:
                // TODO: 2018/3/22 点击搜索按钮，进入搜索页面
                startActivity(new Intent(mContext, OrderSeacherActivity.class));
                break;
        }
    }
}
