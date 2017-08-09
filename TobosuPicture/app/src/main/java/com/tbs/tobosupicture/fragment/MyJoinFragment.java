package com.tbs.tobosupicture.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseFragment;

/**
 * Created by Mr.Lin on 2017/8/3 11:10.
 * 我参与的fragment 作用于我的模块
 */

public class MyJoinFragment extends BaseFragment{
    private Context mContext;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_my_join,null);
        mContext=getActivity();
        return view;
    }
}
