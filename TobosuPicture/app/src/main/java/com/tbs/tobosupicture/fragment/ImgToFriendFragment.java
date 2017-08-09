package com.tbs.tobosupicture.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.ImgToFriendSeachActivity;
import com.tbs.tobosupicture.base.BaseFragment;
import com.tbs.tobosupicture.bean.EC;
import com.tbs.tobosupicture.bean.Event;
import com.tbs.tobosupicture.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Mr.Lin on 2017/7/17 14:14.
 */

public class ImgToFriendFragment extends BaseFragment {
    @BindView(R.id.imgtofriend_sousuo)
    RelativeLayout imgtofriendSousuo;
    @BindView(R.id.imgtofriend_fabu)
    RelativeLayout imgtofriendFabu;
    @BindView(R.id.imgtofriend_zuire)
    LinearLayout imgtofriendZuire;
    @BindView(R.id.imgtofriend_zuixin)
    LinearLayout imgtofriendZuixin;
    @BindView(R.id.imgtofriend_youguanyuwo)
    LinearLayout imgtofriendYouguanyuwo;
    @BindView(R.id.imgtofriend_framelayout)
    FrameLayout imgtofriendFramelayout;
    Unbinder unbinder;
    @BindView(R.id.text_zuire)
    TextView textZuire;
    @BindView(R.id.text_zuixin)
    TextView textZuixin;
    @BindView(R.id.text_youguanyuwo)
    TextView textYouguanyuwo;

    private Context mContext;
    private String TAG = "ImgToFriendFragment";
    private Fragment[] mFragments;
    private int mIndex;//fragment数据下标

    public ImgToFriendFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_img_tofriend, null);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        initFragment();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initFragment() {
        //最热
        ZuiReFragment zuiReFragment = new ZuiReFragment();
        //最新
        ZuiXinFragment zuiXinFragment = new ZuiXinFragment();
        //有关于我
        AboutMeFragment aboutMeFragment = new AboutMeFragment();

        mFragments = new Fragment[]{zuiReFragment, zuiXinFragment, aboutMeFragment};
        //fragment切换事务
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.imgtofriend_framelayout, zuiReFragment).commit();
        //默认显示最热
        setIndexSelect(0);
        clcikChange(0);
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
            ft.add(R.id.imgtofriend_framelayout, mFragments[indexSelect]).show(mFragments[indexSelect]);
        } else {
            ft.show(mFragments[indexSelect]);
        }
        ft.commitAllowingStateLoss();
        mIndex = indexSelect;
    }

    @OnClick({R.id.imgtofriend_sousuo, R.id.imgtofriend_fabu, R.id.imgtofriend_zuire,
            R.id.imgtofriend_zuixin, R.id.imgtofriend_youguanyuwo})
    public void onViewClickedInImgToFriendFragment(View view) {
        switch (view.getId()) {
            case R.id.imgtofriend_sousuo:
                //跳转到搜索页
                Intent intent = new Intent(mContext, ImgToFriendSeachActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.imgtofriend_fabu:
                //调用相机或者图册进行动态发布
                break;
            case R.id.imgtofriend_zuire:
                //切换至最热
                setIndexSelect(0);
                clcikChange(0);
                break;
            case R.id.imgtofriend_zuixin:
                //切换至最新
                setIndexSelect(1);
                clcikChange(1);
                break;
            case R.id.imgtofriend_youguanyuwo:
                //切换至有关于我
                if (Utils.userIsLogin(mContext)) {
                    setIndexSelect(2);
                    clcikChange(2);
                } else {
                    Utils.gotoLogin(mContext);
                }
                break;
        }
    }

    //切换文字的改变
    private void clcikChange(int position) {
        if (position == 0) {
            textZuire.setTextColor(Color.parseColor("#202124"));
            textZuixin.setTextColor(Color.parseColor("#8a8f99"));
            textYouguanyuwo.setTextColor(Color.parseColor("#8a8f99"));
        } else if (position == 1) {
            textZuire.setTextColor(Color.parseColor("#8a8f99"));
            textZuixin.setTextColor(Color.parseColor("#202124"));
            textYouguanyuwo.setTextColor(Color.parseColor("#8a8f99"));
        } else if (position == 2) {
            textZuire.setTextColor(Color.parseColor("#8a8f99"));
            textZuixin.setTextColor(Color.parseColor("#8a8f99"));
            textYouguanyuwo.setTextColor(Color.parseColor("#202124"));
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }
    //接收EventBus消息事件

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.LOGIN_OUT:
                //用户已经登出
                setIndexSelect(0);
                clcikChange(0);
                break;
        }
    }
}
