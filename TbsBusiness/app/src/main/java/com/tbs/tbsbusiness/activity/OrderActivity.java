package com.tbs.tbsbusiness.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;
import com.google.gson.Gson;
import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.adapter.MyFragmentPagerStateAdapter;
import com.tbs.tbsbusiness.base.BaseActivity;
import com.tbs.tbsbusiness.bean.EC;
import com.tbs.tbsbusiness.bean.Event;
import com.tbs.tbsbusiness.bean._AllOrderTab;
import com.tbs.tbsbusiness.bean._IsOpenShengming;
import com.tbs.tbsbusiness.config.Constant;
import com.tbs.tbsbusiness.customview.CustomDialog;
import com.tbs.tbsbusiness.fragment.OrderFragment;
import com.tbs.tbsbusiness.util.EventBusUtil;
import com.tbs.tbsbusiness.util.OKHttpUtil;
import com.tbs.tbsbusiness.util.SpUtil;
import com.tbs.tbsbusiness.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 订单页面
 */
public class OrderActivity extends BaseActivity {

    @BindView(R.id.all_order_banner_rl)
    RelativeLayout allOrderBannerRl;
    @BindView(R.id.all_order_tab_layout)
    XTabLayout allOrderTabLayout;
    @BindView(R.id.all_order_viewpager)
    ViewPager allOrderViewpager;
    @BindView(R.id.all_order_tishi_tv)
    TextView allOrderTishiTv;
    @BindView(R.id.all_order_tishi_down_img)
    ImageView allOrderTishiDownImg;
    @BindView(R.id.all_order_tishi_rl)
    RelativeLayout allOrderTishiRl;
    private Context mContext;
    private String TAG = "OrderActivity";
    private Gson mGson;

    //标签集合
    private ArrayList<_AllOrderTab> allOrderTabArrayList;
    //fragment 数据集合
    private ArrayList<Fragment> mFragmentArrayList = new ArrayList<>();
    //viewPage适配器
    private MyFragmentPagerStateAdapter myFragmentPagerStateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        mContext = this;
        Log.e(TAG, "==onCreate==");
        initView();
        initEvent();
    }

    private void initView() {
        String htmlText = "<font color='#ff6b14'>温馨提醒：</font><font color='#ffffff'>订单分配后48小时内没有反馈的视为有效订单；反馈信息与客服回访不符的视为有效订单，以客服核实业主内容为准！</font>";
        allOrderTishiTv.setText(Html.fromHtml(htmlText));
        HttpOpenShengming();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.USER_LOGIN_OUT:
                Log.e(TAG, "OrderActivity退出====");
                finish();
                break;
        }
    }

    //初始化相关的事务
    private void initEvent() {
        mGson = new Gson();
        initAllOrederTabList();
        initViewEvent();
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
        //已撤单
        _AllOrderTab orderTab7 = new _AllOrderTab("已撤单", "7");
        allOrderTabArrayList.add(orderTab7);
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

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "==onStart==");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "==onStop==");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "==onResume==");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "==onRestart==");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "==onPause==");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "==onDestroy==");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            CustomDialog.Builder builder = new CustomDialog.Builder(this);
            builder.setMessage("你确定退出吗？")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    //退出前将数据上传
                                    finish();
                                    System.exit(0);
                                }
                            })
                    .setNegativeButton("再看看",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            builder.create().show();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.all_order_banner_rl, R.id.all_order_tishi_down_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.all_order_banner_rl:
                startActivity(new Intent(mContext, OrderSeacherActivity.class));
                break;
            case R.id.all_order_tishi_down_img:
                //动画消失
                dimissAnimation();
                break;
        }
    }

    private void dimissAnimation() {
        TranslateAnimation mAnimBanner = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.5f);
        mAnimBanner.setDuration(600);
        allOrderTishiRl.setAnimation(mAnimBanner);
        allOrderTishiRl.setVisibility(View.GONE);
        HttpCloseShengming();
    }

    //关闭免责声明
    private void HttpCloseShengming() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("comid", SpUtil.getCompany_id(mContext));
        OKHttpUtil.post(Constant.CLOSE_SHENGMING, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    //是否开启免责申明
    private void HttpOpenShengming() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("comid", SpUtil.getCompany_id(mContext));
        OKHttpUtil.post(Constant.IS_OPEN_SHENGMING, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "是否开启免责声明==========" + json);
                final _IsOpenShengming isOpenShengming = mGson.fromJson(json, _IsOpenShengming.class);
                if (isOpenShengming.getStatus() == 200) {
                    //请求成功
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isOpenShengming.getData().getStatus() == 1) {
                                //开启
                                showAnimation();
                            }
                        }
                    });
                }
            }
        });
    }

    //显示动画
    private void showAnimation() {
        TranslateAnimation mAnimBanner = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        mAnimBanner.setDuration(600);
        allOrderTishiRl.setAnimation(mAnimBanner);
        allOrderTishiRl.setVisibility(View.VISIBLE);
    }
}
