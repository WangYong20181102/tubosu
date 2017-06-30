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
 * Created by Mr.Lin on 2017/6/29 11:10.
 * 以图会友
 */

public class ImageToFriendFragment extends BaseFragment {
    public ImageToFriendFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_tofriend, null);
        return view;
    }
}
