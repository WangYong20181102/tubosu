package com.tbs.tbsbusiness.activity;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tbs.tbsbusiness.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends TabActivity {

    //    @BindView(R.id.main_tab_content_fl)
//    FrameLayout mainTabContentFl;
    @BindView(R.id.main_tab_dingdang_iv)
    ImageView mainTabDingdangIv;
    @BindView(R.id.main_tab_dingdang_tv)
    TextView mainTabDingdangTv;
    @BindView(R.id.main_tab_dingdang_rl)
    RelativeLayout mainTabDingdangRl;
    @BindView(R.id.main_tab_message_iv)
    ImageView mainTabMessageIv;
    @BindView(R.id.main_tab_message_tv)
    TextView mainTabMessageTv;
    @BindView(R.id.main_tab_message_rl)
    RelativeLayout mainTabMessageRl;
    @BindView(R.id.main_tab_mine_iv)
    ImageView mainTabMineIv;
    @BindView(R.id.main_tab_mine_tv)
    TextView mainTabMineTv;
    @BindView(R.id.main_tab_mine_rl)
    RelativeLayout mainTabMineRl;
    private Context mContext;

    private static TabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        //初始化基本事务
        initEvent();
        //检测更新
    }

    private void initEvent() {
        mTabHost = this.getTabHost();
        TabHost.TabSpec spec;
        //订单页面
        Intent mIntent = new Intent(mContext, OrderActivity.class);
        spec = mTabHost.newTabSpec("one").setIndicator("订单").setContent(mIntent);
        mTabHost.addTab(spec);
        //消息页面
        mIntent = new Intent(mContext, MessageActivity.class);
        spec = mTabHost.newTabSpec("two").setIndicator("消息").setContent(mIntent);
        mTabHost.addTab(spec);
        //我的界面
        mIntent = new Intent(mContext, MineActivity.class);
        spec = mTabHost.newTabSpec("three").setIndicator("我的").setContent(mIntent);
        mTabHost.addTab(spec);
        //初始选择
        selectTab(0);
    }

    private void selectTab(int position) {
        switch (position) {
            case 0:
                //订单
                mTabHost.setCurrentTab(position);
                mTabHost.setCurrentTabByTag("one");
                tabIconChange(position);
                break;
            case 1:
                //消息
                mTabHost.setCurrentTab(position);
                mTabHost.setCurrentTabByTag("two");
                tabIconChange(position);
                break;
            case 2:
                //我的
                mTabHost.setCurrentTab(position);
                mTabHost.setCurrentTabByTag("three");
                tabIconChange(position);
                break;
        }
    }

    private void tabIconChange(int position) {
        switch (position) {
            case 0:
                //按钮的的变化
                Glide.with(mContext).load(R.drawable.tab_dingdang_click).asBitmap().into(mainTabDingdangIv);
                Glide.with(mContext).load(R.drawable.tab_message).asBitmap().into(mainTabMessageIv);
                Glide.with(mContext).load(R.drawable.tab_mine).asBitmap().into(mainTabMineIv);
                //文字变化
                mainTabDingdangTv.setTextColor(Color.parseColor("#ff6c20"));
                mainTabMessageTv.setTextColor(Color.parseColor("#999999"));
                mainTabMineTv.setTextColor(Color.parseColor("#999999"));
                break;
            case 1:
                //按钮的的变化
                Glide.with(mContext).load(R.drawable.tab_dingdang).asBitmap().into(mainTabDingdangIv);
                Glide.with(mContext).load(R.drawable.tab_message_click).asBitmap().into(mainTabMessageIv);
                Glide.with(mContext).load(R.drawable.tab_mine).asBitmap().into(mainTabMineIv);
                //文字变化
                mainTabDingdangTv.setTextColor(Color.parseColor("#999999"));
                mainTabMessageTv.setTextColor(Color.parseColor("#ff6c20"));
                mainTabMineTv.setTextColor(Color.parseColor("#999999"));
                break;
            case 2:
                //按钮的的变化
                Glide.with(mContext).load(R.drawable.tab_dingdang).asBitmap().into(mainTabDingdangIv);
                Glide.with(mContext).load(R.drawable.tab_message).asBitmap().into(mainTabMessageIv);
                Glide.with(mContext).load(R.drawable.tab_mine_click).asBitmap().into(mainTabMineIv);
                //文字变化
                mainTabDingdangTv.setTextColor(Color.parseColor("#999999"));
                mainTabMessageTv.setTextColor(Color.parseColor("#999999"));
                mainTabMineTv.setTextColor(Color.parseColor("#ff6c20"));
                break;
        }
    }

    @OnClick({R.id.main_tab_dingdang_rl,
            R.id.main_tab_message_rl, R.id.main_tab_mine_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_tab_dingdang_rl:
                //点击了订单模块
                mTabHost.setCurrentTabByTag("one");
                selectTab(0);
                break;
            case R.id.main_tab_message_rl:
                //消息模块
                mTabHost.setCurrentTabByTag("two");
                selectTab(1);
                break;
            case R.id.main_tab_mine_rl:
                //我的模块
                mTabHost.setCurrentTabByTag("three");
                selectTab(2);
                break;
        }
    }
}
