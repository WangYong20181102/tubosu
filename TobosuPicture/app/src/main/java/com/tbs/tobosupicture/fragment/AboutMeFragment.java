package com.tbs.tobosupicture.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jauker.widget.BadgeView;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseFragment;
import com.tbs.tobosupicture.bean.EC;
import com.tbs.tobosupicture.bean.Event;
import com.tbs.tobosupicture.utils.EventBusUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Mr.Lin on 2017/7/17 15:52.
 * 以图会友有关于我的fragment
 */

public class AboutMeFragment extends BaseFragment {

    @BindView(R.id.about_me_my_orgin)
    TextView aboutMeMyOrgin;
    @BindView(R.id.about_me_my_join)
    TextView aboutMeMyJoin;
    @BindView(R.id.about_me_my_friend)
    TextView aboutMeMyFriend;
    @BindView(R.id.about_me_framelayout)
    FrameLayout aboutMeFramelayout;
    @BindView(R.id.about_me_my_orgin_tag)
    View aboutMeMyOrginTag;
    @BindView(R.id.about_me_my_join_tag)
    View aboutMeMyJoinTag;
    @BindView(R.id.about_me_my_friend_tag)
    View aboutMeMyFriendTag;
    Unbinder unbinder;
    private Context mContext;
    private String TAG = "AboutMeFragment";
    private Fragment[] mFragments;//fragment集合
    private int mIndex;//fragment数据下标
    private List<TextView> textViewList = new ArrayList<>();//按钮集合
    private List<View> tagViewList = new ArrayList<>();//标签集合
    private BadgeView mMyOrginTag;//我的发起
    private BadgeView mMyJoinTag;//我的参与
    private MyOrginFragment myOrginFragment;
    private MyJoinFragment myJoinFragment;
    private MyFriendDynamicFragment myFriendDynamicFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_me, null);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        initFragment();
        InitShowDot();
        return view;
    }

    //注册事件通知
    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    //接收相关通知
    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
//            case EC.EventCode.LOGIN_INITDATA:
//                Log.e(TAG, "重置数据=========");
//                initFragment();
//                break;
            case EC.EventCode.MY_JOIN_NUM:
                //我参与的数量
                if ((int) event.getData() >= 100) {
                    mMyJoinTag.setText("99+");
                } else {
                    mMyJoinTag.setBadgeCount((int) event.getData());
                }
                break;
            case EC.EventCode.MY_ORGIN_NUM:
                //我发起的数量
                if ((int) event.getData() >= 100) {
                    mMyOrginTag.setText("99+");
                } else {
                    mMyOrginTag.setBadgeCount((int) event.getData());
                }
                break;
            case EC.EventCode.CHECK_ABOUTME_MYORG_HAS_MSG:
                //监测是否有消息
//                if (!mMyOrginTag.getBadgeCount().equals("0")) {
//                    EventBusUtil.sendEvent(new Event(EC.EventCode.REFRESH_MY_ORGIN_NUM));
//                }
                break;
        }
    }

    private void initFragment() {
        //我的发起
        myOrginFragment = new MyOrginFragment();
        //我的参与
        myJoinFragment = new MyJoinFragment();
        //我的好友动态
        myFriendDynamicFragment = new MyFriendDynamicFragment();
        //加入集合
        mFragments = new Fragment[]{myOrginFragment, myJoinFragment, myFriendDynamicFragment};
        //fragment任务切换
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.about_me_framelayout, myOrginFragment).commit();
        //设置默认显示
        setIndexSelect(0);
        //将按钮加入集合
        textViewList.add(aboutMeMyOrgin);
        textViewList.add(aboutMeMyJoin);
        textViewList.add(aboutMeMyFriend);
        //将标签加入集合
        tagViewList.add(aboutMeMyOrginTag);
        tagViewList.add(aboutMeMyJoinTag);
        tagViewList.add(aboutMeMyFriendTag);
        //默认选中
        clickChange(0);
    }

    //红点提示  这个是要在接收到具体消息之后才会有提示
    private void InitShowDot() {
        //我的发起
        mMyOrginTag = new BadgeView(mContext);
        mMyOrginTag.setBackground(8, Color.parseColor("#FFF10606"));
        mMyOrginTag.setTargetView(aboutMeMyOrgin);
        mMyOrginTag.setBadgeGravity(Gravity.RIGHT | Gravity.TOP);
        mMyOrginTag.setBadgeMargin(0, 7, 13, 0);
        //我参加的
        mMyJoinTag = new BadgeView(mContext);
        mMyJoinTag.setBackground(8, Color.parseColor("#FFF10606"));
        mMyJoinTag.setTargetView(aboutMeMyJoin);
        mMyJoinTag.setBadgeGravity(Gravity.RIGHT | Gravity.TOP);
        mMyJoinTag.setBadgeMargin(0, 7, 13, 0);
    }

    //切换选择
    private void setIndexSelect(int indexSelect) {
        if (mIndex == indexSelect) {
            return;
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.hide(mFragments[mIndex]);
        if (!mFragments[indexSelect].isAdded()) {
            ft.add(R.id.about_me_framelayout, mFragments[indexSelect]).show(mFragments[indexSelect]);
        } else {
            ft.show(mFragments[indexSelect]);
        }
        ft.commitAllowingStateLoss();
        mIndex = indexSelect;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.about_me_my_orgin, R.id.about_me_my_join, R.id.about_me_my_friend})
    public void onViewClickedInAboutMeFragment(View view) {
        switch (view.getId()) {
            case R.id.about_me_my_orgin:
                //我的发起
                setIndexSelect(0);
                clickChange(0);
                if (!mMyOrginTag.getBadgeCount().equals(0)) {
                    //我的发起消息数量不为0时通知刷新界面
                    if (myOrginFragment != null) {
                        EventBusUtil.sendEvent(new Event(EC.EventCode.REFRESH_MY_ORGIN_NUM));
                    }
                }
                break;
            case R.id.about_me_my_join:
                setIndexSelect(1);
                clickChange(1);
                if (!mMyJoinTag.getBadgeCount().equals(0)) {
                    //消息数量不为0时通知刷新界面
                    if (myJoinFragment != null) {
                        EventBusUtil.sendEvent(new Event(EC.EventCode.REFRESH_MY_JOIN_NUM));
                    }
                }
                break;
            case R.id.about_me_my_friend:
                setIndexSelect(2);
                clickChange(2);
                if (myFriendDynamicFragment != null) {
                    EventBusUtil.sendEvent(new Event(EC.EventCode.INIT_MY_FRIEND_DYNAMIC));
                }
                break;
        }
    }

    //字体颜色和底部下划线的改变
    private void clickChange(int position) {
        if (position == 0) {
            textViewList.get(0).setTextColor(Color.parseColor("#2e3033"));
            tagViewList.get(0).setVisibility(View.VISIBLE);
            textViewList.get(1).setTextColor(Color.parseColor("#8a8f99"));
            tagViewList.get(1).setVisibility(View.GONE);
            textViewList.get(2).setTextColor(Color.parseColor("#8a8f99"));
            tagViewList.get(2).setVisibility(View.GONE);
        }
        if (position == 1) {
            textViewList.get(1).setTextColor(Color.parseColor("#2e3033"));
            tagViewList.get(1).setVisibility(View.VISIBLE);
            textViewList.get(0).setTextColor(Color.parseColor("#8a8f99"));
            tagViewList.get(0).setVisibility(View.GONE);
            textViewList.get(2).setTextColor(Color.parseColor("#8a8f99"));
            tagViewList.get(2).setVisibility(View.GONE);
        }
        if (position == 2) {
            textViewList.get(2).setTextColor(Color.parseColor("#2e3033"));
            tagViewList.get(2).setVisibility(View.VISIBLE);
            textViewList.get(1).setTextColor(Color.parseColor("#8a8f99"));
            tagViewList.get(1).setVisibility(View.GONE);
            textViewList.get(0).setTextColor(Color.parseColor("#8a8f99"));
            tagViewList.get(0).setVisibility(View.GONE);
        }
    }
}
