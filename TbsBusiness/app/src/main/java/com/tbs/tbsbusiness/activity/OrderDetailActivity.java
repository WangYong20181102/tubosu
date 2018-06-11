package com.tbs.tbsbusiness.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.adapter.MyFragmentPagerAdapter;
import com.tbs.tbsbusiness.base.BaseActivity;
import com.tbs.tbsbusiness.bean.EC;
import com.tbs.tbsbusiness.bean.Event;
import com.tbs.tbsbusiness.fragment.OrderDetailFragment;
import com.tbs.tbsbusiness.fragment.OrderFeedBackFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订单详情页
 */
public class OrderDetailActivity extends BaseActivity {

    @BindView(R.id.new_order_detail_back_rl)
    RelativeLayout newOrderDetailBackRl;
    @BindView(R.id.new_order_id_tv)
    TextView newOrderIdTv;
    @BindView(R.id.new_order_detail_tab_tv)
    TextView newOrderDetailTabTv;
    @BindView(R.id.new_order_detail_tab_sign_view)
    View newOrderDetailTabSignView;
    @BindView(R.id.new_order_detail_tab_rl)
    RelativeLayout newOrderDetailTabRl;
    @BindView(R.id.new_order_detail_feedback_tab_tv)
    TextView newOrderDetailFeedbackTabTv;
    @BindView(R.id.new_order_detail_feedback_tab_sign_view)
    View newOrderDetailFeedbackTabSignView;
    @BindView(R.id.new_order_detail_feedback_tab_rl)
    RelativeLayout newOrderDetailFeedbackTabRl;
    @BindView(R.id.new_order_detail_viewpager)
    ViewPager newOrderDetailViewpager;
    private Context mContext;
    private String TAG = "NewOrderDetailActivity";
    private String mOrderId = "";//从上一个界面进来的订单id
    private String mShowingOrderId = "";//从上一个界面进来的用于展示的订单id
    private String mViewPagerPosition = "";
    private Intent mIntent;
    private Gson mGson;
    private ArrayList<Fragment> mFragmentArrayList = new ArrayList<>();
    private MyFragmentPagerAdapter myFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
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
            case EC.EventCode.CHANGE_ORDER_DETAIL_ACTIVITY_TO_FEEDBACK_VIEW:
                newOrderDetailTabTv.setTextColor(Color.parseColor("#666666"));
                newOrderDetailFeedbackTabTv.setTextColor(Color.parseColor("#ff6c14"));
                newOrderDetailTabSignView.setVisibility(View.GONE);
                newOrderDetailFeedbackTabSignView.setVisibility(View.VISIBLE);
                newOrderDetailViewpager.setCurrentItem(1);
                break;
        }
    }

    private void initViewEvent() {
        mIntent = getIntent();
        mGson = new Gson();
        //获取从上一个界面传来的订单id
        mOrderId = mIntent.getStringExtra("mOrderId");
        //获取从上一个界面传来的订单id用于展示
        mShowingOrderId = mIntent.getStringExtra("mShowingOrderId");
        //获取切换的界面
        mViewPagerPosition = mIntent.getStringExtra("mViewPagerPosition");
        Log.e(TAG, "进入订单详情页面=============" + mOrderId + "============" + mShowingOrderId + "============" + mViewPagerPosition);
        //显示订单号码
        newOrderIdTv.setText("" + "订单 " + mShowingOrderId);
        mFragmentArrayList.add(OrderDetailFragment.newInstance(mOrderId));
        mFragmentArrayList.add(OrderFeedBackFragment.newInstance(mOrderId));
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragmentArrayList);
        newOrderDetailViewpager.setAdapter(myFragmentPagerAdapter);
        newOrderDetailViewpager.setCurrentItem(0);
        newOrderDetailViewpager.addOnPageChangeListener(onPageChangeListener);
        if (mViewPagerPosition != null && mViewPagerPosition.equals("1")) {
            //进入了反馈页面
            newOrderDetailViewpager.setCurrentItem(1);
            newOrderDetailTabTv.setTextColor(Color.parseColor("#666666"));
            newOrderDetailFeedbackTabTv.setTextColor(Color.parseColor("#ff6c14"));
            newOrderDetailTabSignView.setVisibility(View.GONE);
            newOrderDetailFeedbackTabSignView.setVisibility(View.VISIBLE);
        }
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                //显示
                newOrderDetailTabTv.setTextColor(Color.parseColor("#ff6c14"));
                newOrderDetailFeedbackTabTv.setTextColor(Color.parseColor("#666666"));
                newOrderDetailTabSignView.setVisibility(View.VISIBLE);
                newOrderDetailFeedbackTabSignView.setVisibility(View.GONE);
            } else {
                newOrderDetailTabTv.setTextColor(Color.parseColor("#666666"));
                newOrderDetailFeedbackTabTv.setTextColor(Color.parseColor("#ff6c14"));
                newOrderDetailTabSignView.setVisibility(View.GONE);
                newOrderDetailFeedbackTabSignView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @OnClick({R.id.new_order_detail_back_rl,
            R.id.new_order_detail_tab_rl,
            R.id.new_order_detail_feedback_tab_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.new_order_detail_back_rl:
                finish();
                break;
            case R.id.new_order_detail_tab_rl:
                newOrderDetailTabTv.setTextColor(Color.parseColor("#ff6c14"));
                newOrderDetailFeedbackTabTv.setTextColor(Color.parseColor("#666666"));
                newOrderDetailTabSignView.setVisibility(View.VISIBLE);
                newOrderDetailFeedbackTabSignView.setVisibility(View.GONE);
                newOrderDetailViewpager.setCurrentItem(0);
                break;
            case R.id.new_order_detail_feedback_tab_rl:
                newOrderDetailTabTv.setTextColor(Color.parseColor("#666666"));
                newOrderDetailFeedbackTabTv.setTextColor(Color.parseColor("#ff6c14"));
                newOrderDetailTabSignView.setVisibility(View.GONE);
                newOrderDetailFeedbackTabSignView.setVisibility(View.VISIBLE);
                newOrderDetailViewpager.setCurrentItem(1);
                break;
        }
    }
}
