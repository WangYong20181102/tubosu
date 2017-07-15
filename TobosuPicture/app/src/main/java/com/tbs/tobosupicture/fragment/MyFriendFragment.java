package com.tbs.tobosupicture.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.MyFriendAdapter;
import com.tbs.tobosupicture.base.BaseFragment;
import com.tbs.tobosupicture.bean._MyFriend;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Mr.Lin on 2017/7/15 16:22.
 * 我的图友 Fragment
 */

public class MyFriendFragment extends BaseFragment {
    @BindView(R.id.myfriend_fragment_recyclelist)
    RecyclerView myfriendFragmentRecyclelist;
    Unbinder unbinder;
    @BindView(R.id.myfriend_swip_refresh)
    SwipeRefreshLayout myfriendSwipRefresh;

    private Context mContext;
    private String TAG = "MyFriendFragment";
    private MyFriendAdapter myFriendAdapter;
    private ArrayList<_MyFriend> myFriendArrayList = new ArrayList<>();//正式布局用的集合
    private ArrayList<_MyFriend> tempMyFriendArrayList = new ArrayList<>();//临时装箱集合

    public MyFriendFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myfriend, null);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        initView();
        return view;
    }

    private void initView() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
