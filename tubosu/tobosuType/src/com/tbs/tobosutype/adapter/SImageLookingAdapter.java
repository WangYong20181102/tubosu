package com.tbs.tobosutype.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;


import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2017/11/16 17:43.
 * 查看单图详情适配器
 */

public class SImageLookingAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> fragmentArrayList;

    public SImageLookingAdapter(FragmentManager fm, ArrayList<Fragment> fragmentArrayList) {
        super(fm);
        this.fragmentArrayList = fragmentArrayList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }
}
