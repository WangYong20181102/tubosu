package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.MyFragmentPagerAdapter;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.fragment.MyFriendFragment;
import com.tbs.tobosupicture.fragment.RecommendFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的图友 这个模块分为 我的图友 和推荐 图友
 * create by lin
 */
public class MyFriendsActivity extends BaseActivity {
    @BindView(R.id.my_friend_back)
    RelativeLayout myFriendBack;
    @BindView(R.id.my_friend_tablayout)
    TabLayout myFriendTablayout;
    @BindView(R.id.my_friend_viewpager)
    ViewPager myFriendViewpager;

    private Context mContext;
    private String TAG = "MyFriendsActivity";
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private MyFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    private void initView() {
        //将选项卡的指示器变短
        myFriendTablayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(myFriendTablayout, 65, 65);
            }
        });
        //初始化ViewPager
        fragmentArrayList.add(new RecommendFragment());
        fragmentArrayList.add(new MyFriendFragment());
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentArrayList);
        myFriendViewpager.setAdapter(adapter);
        //将tab和Viewpage绑定
        myFriendTablayout.setupWithViewPager(myFriendViewpager);
        //在tabLayout和Viewpage绑定时会移除tab文字 所以在关联之后写入
        myFriendTablayout.getTabAt(0).setText("推荐");
        myFriendTablayout.getTabAt(1).setText("我的");
    }

    @OnClick({R.id.my_friend_back, R.id.my_friend_tablayout, R.id.my_friend_viewpager})
    public void onViewClickedInMyFriendsActivity(View view) {
        switch (view.getId()) {
            case R.id.my_friend_back:
                finish();
                break;
        }
    }

    //通过反射 改变tab的指示器的长度
    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
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
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }
}
