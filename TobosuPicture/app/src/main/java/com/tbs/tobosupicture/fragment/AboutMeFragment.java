package com.tbs.tobosupicture.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseFragment;

/**
 * Created by Mr.Lin on 2017/7/17 15:52.
 * 以图会友有关于我的fragment
 */

public class AboutMeFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_me, null);
        return view;
    }
}
