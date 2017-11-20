package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.NewImagePagerAdapter;
import com.tbs.tobosutype.base.*;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.customview.CustomDialog;
import com.tbs.tobosutype.fragment.NewImageDFragment;
import com.tbs.tobosutype.fragment.NewImageSFragment;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.EventBusUtil;
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
    @BindView(R.id.tab_view_pager_rl)
    RelativeLayout tabViewPagerRl;//包含了tab和viewpage
    private Context mContext;
    private String TAG = "NewImageActivity";
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private NewImagePagerAdapter mNewImagePagerAdapter;

    private int mTime = 0;//计时时间
    private boolean isAddTime = false;//是否在计时

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_image);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    private void initViewEvent() {
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

    @OnClick({R.id.new_image_free_design_img, R.id.new_image_free_design_img_close, R.id.new_image_gif, R.id.new_image_free_design_img_rl})
    public void onViewClickedInNewImageActivity(View view) {
        switch (view.getId()) {
            case R.id.new_image_free_design_img_rl:

                break;
            case R.id.new_image_gif:
                //右侧gif点击进发单页
                Intent intent = new Intent(mContext, NewWebViewActivity.class);
                intent.putExtra("mLoadingUrl", Constant.IAMGE_LIST_RIGHT_GIF);
                startActivity(intent);
                break;
            case R.id.new_image_free_design_img:
                //跳转到发单页
                Log.e(TAG, "点击了发单按钮===================");
                Intent intent2 = new Intent(mContext, NewWebViewActivity.class);
                intent2.putExtra("mLoadingUrl", Constant.IAMGE_LIST_DIALOG);
                startActivity(intent2);
                dismissTanChuang();
                break;
            case R.id.new_image_free_design_img_close:
                //关闭界面弹出的界面 处理关闭时的动画
                Log.e(TAG, "点击了关闭的按钮======");
                //整合动画
                AnimationSet animationSet = new AnimationSet(false);
                ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                        Animation.RELATIVE_TO_SELF, 1.5f,
                        Animation.RELATIVE_TO_SELF, 1.6f);
                TranslateAnimation translateAnimation = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 1.9f,
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 2.7f);
                scaleAnimation.setDuration(1000);
                translateAnimation.setDuration(1200);
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
            dismissTanChuang();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    //结束弹窗
    private void dismissTanChuang() {
        //动画结束时弹出右侧的GIF动图
        newImageFreeDesignImgRl.setVisibility(View.GONE);
        AppInfoUtil.setImageListDateToken(mContext, Util.getDateToken() + "list");
        ShowRightGifAnim();
    }

    //加载右侧的帧动画
    private void LoadingRightGifAnim() {
        newImageGif.setImageResource(R.drawable.anim_free_design_right);
        AnimationDrawable animationDrawable = (AnimationDrawable) newImageGif.getDrawable();
        animationDrawable.start();
    }

    //弹出右侧的Gif动画
    private void ShowRightGifAnim() {
        newImageGif.setVisibility(View.VISIBLE);
        TranslateAnimation mAnimShowGif = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        mAnimShowGif.setDuration(500);
        newImageGif.startAnimation(mAnimShowGif);
    }

    //定时显示弹窗
    private void showImageDesignDialog() {
        if (AppInfoUtil.getImageListDateToken(mContext).equals(Util.getDateToken() + "list")) {
            //日期数据相同 说明当日已经弹出了对话框
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
                                EventBusUtil.sendEvent(new Event(EC.EventCode.CLOSE_POP_WINDOW_IN_NEW_IMAGE_ACTIVITY));
                                isAddTime = false;
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
        LoadingRightGifAnim();//加载动画
        if (AppInfoUtil.getImageListDateToken(mContext).equals(Util.getDateToken() + "list")) {
            //比对成功  1.显示Gif图  2.倒计时开关isAddtime=false
            newImageGif.setVisibility(View.VISIBLE);
            isAddTime = false;//倒计时开关关闭
        } else {
            //比对失败当天未开启弹窗 1.隐藏Gif  2.倒计时开关 isAddtime=true 3窗口出现
            isAddTime = true;
            showImageDesignDialog();
        }


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
