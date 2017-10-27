package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.LookPhotoAdapter;
import com.tbs.tobosutype.base.*;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.bean._DecoCaseDetail;
import com.tbs.tobosutype.fragment.LookPhotoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * creat by lin 2017/10/24 16:39
 * 图片查看器
 * 采用Viewpager的形式展现
 */

public class LookPhotoActivity extends com.tbs.tobosutype.base.BaseActivity {

    @BindView(R.id.look_photo_viewpager)
    ViewPager lookPhotoViewpager;
    @BindView(R.id.look_photo_back)
    LinearLayout lookPhotoBack;
    @BindView(R.id.look_photo_title)
    TextView lookPhotoTitle;
    @BindView(R.id.look_photo_num)
    TextView lookPhotoNum;
    @BindView(R.id.look_photo_find_price)
    TextView lookPhotoFindPrice;
    @BindView(R.id.look_photo_find_price_rl)
    RelativeLayout lookPhotoFindPriceRl;

    private Context mContext;
    private String TAG = "LookPhotoActivity";
    private Gson mGson;
    private Intent mIntent;//
    private String lookPhotoJson = "";
    private ArrayList<_DecoCaseDetail.SpaceInfoBean> spaceInfoBeanArrayList;
    private List<Fragment> fragmentList = new ArrayList<>();
    private _DecoCaseDetail mDecoCaseDetail;
    private LookPhotoAdapter mLookPhotoAdapter;
    private int mPosition = 0;//图片选中的位置
    private String photoDesc = "";//图片的描述
    private String photoPositionDesc = "";//图片位置的描述
    private boolean isShowingBanner = true;//是否隐藏banner等 默认显示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_photo);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
        mIntent = getIntent();
        mGson = new Gson();
        spaceInfoBeanArrayList = new ArrayList<>();
        //获取上一个页面的数据
        lookPhotoJson = mIntent.getStringExtra("lookPhotoJson");
        mPosition = mIntent.getIntExtra("position", 0);
        photoDesc = mIntent.getStringExtra("photoDesc");
        photoPositionDesc = mIntent.getStringExtra("positionDesc");
        //设置从上一个页面来的数据
        lookPhotoTitle.setText(photoDesc);
        lookPhotoNum.setText(photoPositionDesc);
        //解析数据
        mDecoCaseDetail = mGson.fromJson(lookPhotoJson, _DecoCaseDetail.class);
        //设置页面滑动监听
        lookPhotoViewpager.addOnPageChangeListener(onPageChangeListener);
        //设置数据源
        spaceInfoBeanArrayList.addAll(mDecoCaseDetail.getSpace_info());
        for (int i = 0; i < spaceInfoBeanArrayList.size(); i++) {
            fragmentList.add(LookPhotoFragment.newInstance(spaceInfoBeanArrayList.get(i)));
        }
        mLookPhotoAdapter = new LookPhotoAdapter(getSupportFragmentManager(), fragmentList);
        lookPhotoViewpager.setAdapter(mLookPhotoAdapter);
        //设置选中的位置
        lookPhotoViewpager.setCurrentItem(mPosition);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.CLICK_IMAGE_IN_LOOK_PHOTO:
                Log.e(TAG, "收到点击事件===============");
                if (isShowingBanner) {
                    //正在显示 让它隐藏
                    lookPhotoFindPriceRl.setVisibility(View.GONE);
                    lookPhotoBack.setVisibility(View.GONE);
                    isShowingBanner = false;
                } else {
                    //未显示 让它显示
                    lookPhotoFindPriceRl.setVisibility(View.VISIBLE);
                    lookPhotoBack.setVisibility(View.VISIBLE);
                    isShowingBanner = true;
                }
                break;
        }
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
            int position = lookPhotoViewpager.getCurrentItem();
            if (state == 2) {
                //修改下边的文字说明
                lookPhotoTitle.setText(mDecoCaseDetail.getSpace_info().get(position).getSpace_name());
                lookPhotoNum.setText((position + 1) + "/" + mDecoCaseDetail.getSpace_info().size());
            }
        }
    };

    @OnClick({R.id.look_photo_back, R.id.look_photo_find_price, R.id.look_photo_find_price_rl})
    public void onViewClickedOnLookPhotoActivity(View view) {
        switch (view.getId()) {
            case R.id.look_photo_back:
                finish();
                break;
            case R.id.look_photo_find_price:
            case R.id.look_photo_find_price_rl:
                /// TODO: 2017/10/26 into fadan
                break;
        }
    }
}
