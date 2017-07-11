package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jauker.widget.BadgeView;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean.EC;
import com.tbs.tobosupicture.bean.Event;
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
    @BindView(R.id.mian_show_num3)
    View mianShowNum3;

    private Fragment[] mFragments;//fragment集合
    private int mIndex;//数据下标
    private String TAG = "MainActivity";
    private Context mContext;
    private BadgeView badgeView;

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
        //将fragment添加至数组中
        mFragments = new Fragment[]{templateFragment, decorationCaseFragment, imageToFriendFragment, mineFragment};
        //处理相关的事务
        FragmentTransaction ft =
                getSupportFragmentManager().beginTransaction();
        //添加首页
        ft.add(R.id.main_frameLayout, templateFragment).commit();
        //默认设置第0个
        setIndexSelect(0);
        //红点提示
        badgeView = new BadgeView(mContext);
        //红点提示框的圆形角度以及背景颜色
        badgeView.setBackground(8, Color.parseColor("#FFF10606"));
        //设置依附在那个控件上
        badgeView.setTargetView(mianShowNum3);
        //依附的位置
        badgeView.setBadgeGravity(Gravity.RIGHT | Gravity.TOP);
        //显示数量
        badgeView.setBadgeCount(5);
        badgeView.setVisibility(View.VISIBLE);
    }

    //选择所在页面
    private void setIndexSelect(int index) {
        if (mIndex == index) {
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //隐藏不必要的布局
        ft.hide(mFragments[mIndex]);
        //处理逻辑 如果没有添加则添加 添加了则显示
        if (!mFragments[index].isAdded()) {
            ft.add(R.id.main_frameLayout, mFragments[index]).show(mFragments[index]);
        } else {
            ft.show(mFragments[index]);
        }
        ft.commit();
        mIndex = index;
    }

    @OnClick({R.id.rb_first, R.id.rb_second, R.id.rb_third, R.id.rb_fourth})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_first:
                //点击第一个选项 显示样板图
                Log.e(TAG, "点击了样板图");
//                EventBusUtil.sendEvent(new Event(EC.EventCode.SHOW_TEMPLATE_FRAGMENT));
                setIndexSelect(0);
                break;
            case R.id.rb_second:
                //点击第二个选项 显示案例
                setIndexSelect(1);
                break;
            case R.id.rb_third:
                //点击第三个选项 显示以图会友
                setIndexSelect(2);
                break;
            case R.id.rb_fourth:
                //点击第四个选项 显示我的  其中我的界面要分情况考虑
                setIndexSelect(3);
                break;
        }
    }

    //是否使用Eventbus
    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    //使用Eventbus处理回来显示
    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.SHOW_TEMPLATE_FRAGMENT:
                //样板图
                rbFirst.performClick();
                setIndexSelect(0);
                break;
            case EC.EventCode.SHOW_DECORATIONCASE_FRAGMENT:
                //装修案例
                Log.e(TAG, "收到案例相关消息");
//                rbSecond.performClick();
//                setIndexSelect(1);
                break;
            case EC.EventCode.SHOW_IMAGETOFRIEND_FRAGMENT:
                //以图会友
                rbThird.performClick();
                setIndexSelect(2);
                break;
            case EC.EventCode.SHOW_MINE_FRAGMENT:
                //显示我的
                rbFourth.performClick();
                setIndexSelect(3);
                break;
        }
    }
    //重新返回按钮

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("你确定要退出吗？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.this.finish();
                }
            }).setNegativeButton("再看看", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }
}
