package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.LookPhotoAdapter;
import com.tbs.tobosutype.base.*;
import com.tbs.tobosutype.bean._CompanyDetail;
import com.tbs.tobosutype.fragment.CoLookPhotoFragment;
import com.tbs.tobosutype.fragment.LookPhotoFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * creat by lin  作用于3.6版本的公司资质
 * 公司资质大图查看页面
 */
public class ZizhiPhotoLookingActivity extends com.tbs.tobosutype.base.BaseActivity {

    @BindView(R.id.zizhi_look_photo_viewpager)
    ViewPager zizhiLookPhotoViewpager;
    @BindView(R.id.zizhi_look_photo_back)
    LinearLayout zizhiLookPhotoBack;
    @BindView(R.id.zizhi_look_photo_num)
    TextView zizhiLookPhotoNum;
    @BindView(R.id.zizhi_look_photo_title_rl)
    RelativeLayout zizhiLookPhotoTitleRl;

    private Context mContext;
    private String TAG = "LookPhotoActivity";
    private Gson mGson;
    private Intent mIntent;//
    private String mLookPhotoJson = "";
    private ArrayList<_CompanyDetail.QualificationBean> qualificationBeanArrayList;
    private List<Fragment> fragmentList = new ArrayList<>();
    private LookPhotoAdapter mLookPhotoAdapter;
    private int mPosition = 0;//图片选中的位置
    private String photoPositionDesc = "";//图片位置的描述

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zizhi_photo_looking);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
        mIntent = getIntent();
        mGson = new Gson();
        qualificationBeanArrayList = new ArrayList<>();
        //获取上一个界面的数据
        mLookPhotoJson = mIntent.getStringExtra("qualificationJson");
        mPosition = mIntent.getIntExtra("position", 0);
        photoPositionDesc = mIntent.getStringExtra("positionDesc");
        //设置数据
        zizhiLookPhotoNum.setText("" + photoPositionDesc);
        //解析数据 填充数据
        try {
            JSONArray jsonArray = new JSONArray(mLookPhotoJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                _CompanyDetail.QualificationBean qualificationBean = mGson.fromJson(jsonArray.get(i).toString(), _CompanyDetail.QualificationBean.class);
                qualificationBeanArrayList.add(qualificationBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //设置数据源
        for (int i = 0; i < qualificationBeanArrayList.size(); i++) {
            fragmentList.add(CoLookPhotoFragment.newInstance(qualificationBeanArrayList.get(i)));
        }
        mLookPhotoAdapter = new LookPhotoAdapter(getSupportFragmentManager(), fragmentList);
        zizhiLookPhotoViewpager.setAdapter(mLookPhotoAdapter);
        //设置选中位置
        zizhiLookPhotoViewpager.setCurrentItem(mPosition);
        zizhiLookPhotoViewpager.addOnPageChangeListener(onPageChangeListener);
    }

    @OnClick(R.id.zizhi_look_photo_back)
    public void onViewClickedInZiahiPhotoLookActivity() {
        finish();
    }

    //页面滑动监听事件
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            int position = zizhiLookPhotoViewpager.getCurrentItem();
            if (state == 2) {
                //修改下边的文字说明
                zizhiLookPhotoNum.setText((position + 1) + "/" + qualificationBeanArrayList.size());
            }
        }
    };
}
