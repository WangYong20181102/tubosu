package com.tbs.tobosutype.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.MyReplyPagerAdapter;
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.fragment.AskQuestionFragment;
import com.tbs.tobosutype.fragment.ReplyFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mr.Wang on 2018/12/3 13:52.
 */
public class MyReplyActivity extends BaseActivity {
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.my_reply_tab)
    TabLayout myReplyTab;
    @BindView(R.id.my_reply_viewpager)
    ViewPager myReplyViewpager;

    private List<Fragment> fragmentList;
    private MyReplyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myreply_layout);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new ReplyFragment());
        fragmentList.add(new AskQuestionFragment());
        myReplyTab.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(myReplyTab,66,66);
            }
        });
        adapter = new MyReplyPagerAdapter(getSupportFragmentManager(),this,fragmentList);
        myReplyViewpager.setAdapter(adapter);
        //将tab和viewpager绑定
        myReplyTab.setupWithViewPager(myReplyViewpager);
        myReplyTab.getTabAt(0).setText("提问");
        myReplyTab.getTabAt(1).setText("回答");


    }
    //通过反射 改变tab的指示器的长度
    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        tabs.setTabGravity(Gravity.CENTER);
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());
        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.leftMargin = left / 2;
            params.rightMargin = right / 2;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
    @OnClick({R.id.rl_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
        }
    }
}
