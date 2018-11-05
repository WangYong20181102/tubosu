package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Mr.Wang on 2018/11/5 11:47.
 */
public class DecorationQuestionViewPagerAdapter extends FragmentStatePagerAdapter{

    private Context context;
    private List<Fragment> fragmentList;
    public DecorationQuestionViewPagerAdapter(FragmentManager fm, Context context, List<Fragment> fragmentList){
        super(fm);
        this.context = context;
        this.fragmentList = fragmentList;
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
