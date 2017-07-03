package com.tbs.tobosupicture.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseFragment;

/**
 * Created by Mr.Lin on 2017/6/29 11:11.
 * 我的 fragment
 */

public class MineFragment extends BaseFragment {

    public MineFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, null);
        return view;
    }
}
