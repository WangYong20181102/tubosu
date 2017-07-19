package com.tbs.tobosupicture.fragment;

import android.content.Context;
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

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseFragment;

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
        ft.commit();
        mIndex = indexSelect;
    }

    @OnClick({R.id.imgtofriend_sousuo, R.id.imgtofriend_fabu, R.id.imgtofriend_zuire,
            R.id.imgtofriend_zuixin, R.id.imgtofriend_youguanyuwo})
    public void onViewClickedInImgToFriendFragment(View view) {
        switch (view.getId()) {
            case R.id.imgtofriend_sousuo:
                //跳转到搜索页
                break;
            case R.id.imgtofriend_fabu:
                //调用相机或者图册进行动态发布
                break;
            case R.id.imgtofriend_zuire:
                //切换至最热
                setIndexSelect(0);
                break;
            case R.id.imgtofriend_zuixin:
                //切换至最新
                setIndexSelect(1);
                break;
            case R.id.imgtofriend_youguanyuwo:
                //切换至有关于我
                setIndexSelect(2);
                break;
        }
    }
}
