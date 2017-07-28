package com.tbs.tobosupicture.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Mr.Lin on 2017/7/27 13:51.
 * 查看动态详情的适配器
 */

public class PhotoFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;

    public PhotoFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragmentList = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
