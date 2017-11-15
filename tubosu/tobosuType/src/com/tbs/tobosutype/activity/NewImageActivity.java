package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.NewImagePagerAdapter;
import com.tbs.tobosutype.base.*;
import com.tbs.tobosutype.customview.CustomDialog;
import com.tbs.tobosutype.fragment.NewImageDFragment;
import com.tbs.tobosutype.fragment.NewImageSFragment;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 新的逛图库(采用瀑布流的展示方式)
 * 土拨鼠3.5
 * creat by lin 2017 11 09
 */
public class NewImageActivity extends com.tbs.tobosutype.base.BaseActivity {
    @BindView(R.id.new_image_tab)
    TabLayout newImageTab;
    @BindView(R.id.new_image_viewpager)
    ViewPager newImageViewpager;
    @BindView(R.id.new_image_free_design_img)
    ImageView newImageFreeDesignImg;
    @BindView(R.id.new_image_free_design_img_close)
    ImageView newImageFreeDesignImgClose;
    @BindView(R.id.new_image_gif)
    ImageView newImageGif;
    @BindView(R.id.new_image_free_design_img_rl)
    RelativeLayout newImageFreeDesignImgRl;
    private Context mContext;
    private String TAG = "NewImageActivity";
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private NewImagePagerAdapter mNewImagePagerAdapter;
    //缩放动画
    private ScaleAnimation scaleAnimation;


    private int mTime = 0;//倒计时时间
    private boolean isAddTime = false;//是否在倒计时

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_image);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
        //加载gif
        Glide.with(mContext).load(R.drawable.image_free_design_gif)
                .placeholder(R.drawable.image_free_design_gif)
                .error(R.drawable.image_free_design_gif)
                .into(newImageGif);
        //初始化ViewPager
        fragmentArrayList.add(new NewImageDFragment());
        fragmentArrayList.add(new NewImageSFragment());
        mNewImagePagerAdapter = new NewImagePagerAdapter(getSupportFragmentManager(), fragmentArrayList);
        newImageViewpager.setAdapter(mNewImagePagerAdapter);
        //将tab和Viewpager绑定
        newImageTab.setupWithViewPager(newImageViewpager);
        //关联之后设置tab的名称在这之前要完成Viewpager的实例化不然会报空指针异常
        newImageTab.getTabAt(0).setText("套图");
        newImageTab.getTabAt(1).setText("单图");
    }

    //通过反射 改变tab的指示器的长度
//    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
//        Class<?> tabLayout = tabs.getClass();
//        Field tabStrip = null;
//        try {
//            tabStrip = tabLayout.getDeclaredField("mTabStrip");
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
//
//        tabStrip.setAccessible(true);
//        LinearLayout llTab = null;
//        try {
//            llTab = (LinearLayout) tabStrip.get(tabs);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
//        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());
//
//        for (int i = 0; i < llTab.getChildCount(); i++) {
//            View child = llTab.getChildAt(i);
//            child.setPadding(0, 0, 0, 0);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
//            params.leftMargin = left;
//            params.rightMargin = right;
//            child.setLayoutParams(params);
//            child.invalidate();
//        }
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            CustomDialog.Builder builder = new CustomDialog.Builder(this);
            builder.setMessage("你确定退出吗？")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    finish();
                                    System.exit(0);
                                }
                            })
                    .setNegativeButton("再看看",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            builder.create().show();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.new_image_free_design_img, R.id.new_image_free_design_img_close})
    public void onViewClickedInNewImageActivity(View view) {
        switch (view.getId()) {
            case R.id.new_image_free_design_img:
                //跳转到发单页
                Log.e(TAG, "点击了发单按钮===================");
                break;
            case R.id.new_image_free_design_img_close:
                //关闭界面弹出的界面 处理关闭时的动画
                Log.e(TAG, "点击了关闭的按钮======");
                //整合动画
                AnimationSet animationSet = new AnimationSet(false);
                ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                        Animation.RELATIVE_TO_SELF, 1.5f, Animation.RELATIVE_TO_SELF, 1.5f);
                TranslateAnimation translateAnimation = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 1.8f,
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 1.8f);
                scaleAnimation.setDuration(1500);
                translateAnimation.setDuration(1800);
                animationSet.addAnimation(translateAnimation);
                animationSet.addAnimation(scaleAnimation);
                animationSet.setFillAfter(true);
//                //背景由不透明变透明
//                AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 0.0f);
//                alphaAnimation.setFillAfter(true);
//                newImageFreeDesignImgRl.startAnimation(alphaAnimation);
                animationSet.setAnimationListener(animationListener);
                newImageFreeDesignImg.startAnimation(animationSet);
                newImageFreeDesignImgClose.setVisibility(View.GONE);
                break;
        }
    }

    //动画监听事件
    private Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Log.e(TAG, "动画结束==============");
            //动画结束时弹出右侧的GIF动图
            newImageFreeDesignImgRl.setVisibility(View.GONE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    //展示右侧的帧动画
    private void showRightAnim() {
        newImageGif.setImageResource(R.drawable.anim_free_design_right);
        AnimationDrawable animationDrawable = (AnimationDrawable) newImageGif.getDrawable();
        animationDrawable.start();
    }

    //定时显示弹窗
    private void showImageDesignDialog() {
        if (AppInfoUtil.getImageListDateToken(mContext).equals(Util.getDateToken() + "list")) {
            //日期数据相同 说明当日已经弹出了对话框 不做处理
            Log.e(TAG, "弹窗已经出现过===============!!!!!!");
        } else {
            //存储的日期数据不相同说明没有弹出对话框  开始计时弹出
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (isAddTime) {
                            Thread.sleep(1000);
                            mTime++;
                            Log.e(TAG, "倒计时时间================" + mTime);
                            if (mTime >= 10) {
                                //设置出现弹窗
                                Log.e(TAG, "出现弹窗==================!!!!!!!");
                                isAddTime = false;
//                                AppInfoUtil.setImageListDateToken(mContext, Util.getDateToken() + "list");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        newImageFreeDesignImgRl.setVisibility(View.VISIBLE);
                                    }
                                });

                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    protected void onResume() {
        Log.e(TAG, "===========onResume 开始 ====");
        isAddTime = true;
//        showImageDesignDialog();
        showRightAnim();
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "===========onPause 暂停 离开页面时");
        if (isAddTime) {
            isAddTime = false;
            mTime = 0;
        }
        super.onPause();
    }
}
