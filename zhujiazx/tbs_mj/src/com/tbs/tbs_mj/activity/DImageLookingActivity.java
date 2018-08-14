package com.tbs.tbs_mj.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.adapter.SImageLookingAdapter;
import com.tbs.tbs_mj.base.*;
import com.tbs.tbs_mj.bean.EC;
import com.tbs.tbs_mj.bean.Event;
import com.tbs.tbs_mj.bean._ImageD;
import com.tbs.tbs_mj.fragment.DImageLookFragment;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.global.OKHttpUtil;
import com.tbs.tbs_mj.utils.AppInfoUtil;
import com.tbs.tbs_mj.utils.EventBusUtil;
import com.tbs.tbs_mj.utils.SpUtil;
import com.tbs.tbs_mj.utils.Util;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * create by lin
 * 套图图片查看器
 */

public class DImageLookingActivity extends com.tbs.tbs_mj.base.BaseActivity {

    @BindView(R.id.d_img_look_viewpager)
    ViewPager dImgLookViewpager;
    @BindView(R.id.d_img_look_title_back)
    LinearLayout dImgLookTitleBack;
    @BindView(R.id.d_img_look_shoucan)
    ImageView dImgLookShoucan;
    @BindView(R.id.d_img_look_shoucan_ll)
    LinearLayout dImgLookShoucanLl;
    @BindView(R.id.d_img_look_share)
    ImageView dImgLookShare;
    @BindView(R.id.d_img_look_share_ll)
    LinearLayout dImgLookShareLl;
    @BindView(R.id.d_img_look_title_bar_rl)
    RelativeLayout dImgLookTitleBarRl;
    @BindView(R.id.d_img_look_title)
    TextView dImgLookTitle;
    @BindView(R.id.d_img_look_img_position)
    TextView dImgLookImgPosition;
    @BindView(R.id.d_img_look_img_list_size)
    TextView dImgLookImgListSize;
    @BindView(R.id.d_img_look_dec_rl)
    RelativeLayout dImgLookDecRl;
    @BindView(R.id.d_img_look_btn_fadan)
    LinearLayout dImgLookBtnFadan;
    @BindView(R.id.d_img_look_frist_into_anim)
    ImageView dImgLookFristIntoAnim;
    @BindView(R.id.d_img_look_i_know)
    TextView dImgLookIKnow;
    @BindView(R.id.d_img_look_frist_into_rl)
    RelativeLayout dImgLookFristIntoRl;
    @BindView(R.id.d_img_look_fadan_img)
    ImageView dImgLookFadanImg;
    @BindView(R.id.d_img_look_fadan_close)
    ImageView dImgLookFadanClose;
    @BindView(R.id.d_img_look_fadan_rl)
    RelativeLayout dImgLookFadanRl;
    @BindView(R.id.d_img_look_btn_fadan_rl)
    RelativeLayout dImgLookBtnFadanRl;

    private String TAG = "DImageLookingActivity";
    private Context mContext;
    private Gson mGson;
    private Intent mIntent;
    private ArrayList<_ImageD> mImageDArrayList = new ArrayList<>();//上界面的组数据
    private ArrayList<String> mImageUrlArrayList = new ArrayList<>();//图片url数据
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();//成像Fragment数据集
    private SImageLookingAdapter mSImageLookingAdapter;
    private String mDImageListJson = "";//从上个界面传来的数据json
    private int mArrayListPosition = 0;//上一个界面传来列表的位置信息
    private int mViewPagerPosition = 0;//viewpager所在的位置信息
    private int mItemPosition = 1;//当前套图子项的位置  进来时默认为0
    private int mItemSize = 0;//当前套图子项的长度
    private String mItemTitle = "";//当前套图的标题
    private String mShareUrl = "";

    //判断viewpager滑动情况
    private int lastValue = -1;
    private boolean isLeft = true;
    private int currentPosition = 0;

    private boolean isShowingBanner = true;//是否显示banner  默认显示
    private PopupWindow mDownLoadImagePopWindow;//长按显示下载的pop
    private View mDownLoadImageView;//承载pop的view
    private boolean isAddTime = false;//是否在倒计时
    private int mTime = 0;//计时时间
    private String mWhereFrom = "";//从哪一个界面进入

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dimage_looking);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected boolean havePageId() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.CLICK_DIMAGE_IN_LOOK_PHOTO:
                //单击收起
                if (isShowingBanner) {
                    //正在显示 让它隐藏
                    isShowingBanner = false;
                    //回退按钮的动画
                    TranslateAnimation mAnimBc = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, -1.0f);
                    mAnimBc.setDuration(500);
                    //发单页动画
                    TranslateAnimation mAnimBanner = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 1.0f);
                    mAnimBanner.setDuration(500);

                    mAnimBanner.setFillAfter(true);
                    dImgLookTitleBarRl.setAnimation(mAnimBc);
                    dImgLookBtnFadan.setAnimation(mAnimBanner);
                    dImgLookDecRl.setAnimation(mAnimBanner);
                    dImgLookBtnFadan.setVisibility(View.GONE);
                    dImgLookTitleBarRl.setVisibility(View.GONE);
                } else {
                    //未显示 让它显示
                    isShowingBanner = true;
                    TranslateAnimation mAnimBc = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, -1.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f);
                    mAnimBc.setDuration(500);
                    //发单页动画
                    TranslateAnimation mAnimBanner = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f);
                    mAnimBanner.setDuration(500);

                    dImgLookTitleBarRl.setAnimation(mAnimBc);
                    dImgLookBtnFadan.setAnimation(mAnimBanner);
                    dImgLookDecRl.setAnimation(mAnimBanner);
                    dImgLookBtnFadan.setVisibility(View.VISIBLE);
                    dImgLookTitleBarRl.setVisibility(View.VISIBLE);

                }
                break;
            case EC.EventCode.LOOG_CLICK_DIMAGE_IN_LOOK_PHOTO:
                //长按下载
                showDownLoadImagePopWindow((String) event.getData());
                break;
        }
    }

    //长按事件弹出下载弹框
    private void showDownLoadImagePopWindow(final String downloadUrl) {
        mDownLoadImageView = View.inflate(mContext, R.layout.popwindow_image_download, null);
        TextView pop_img_dw_ok = (TextView) mDownLoadImageView.findViewById(R.id.pop_img_dw_ok);
        TextView pop_img_dw_no = (TextView) mDownLoadImageView.findViewById(R.id.pop_img_dw_no);
        RelativeLayout pop_image_download_rl = (RelativeLayout) mDownLoadImageView.findViewById(R.id.pop_image_download_rl);
        mDownLoadImagePopWindow = new PopupWindow(mDownLoadImageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mDownLoadImagePopWindow.setFocusable(true);
        mDownLoadImagePopWindow.setOutsideTouchable(true);
        mDownLoadImagePopWindow.update();
        //确定下载
        pop_img_dw_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDownLoadImagePopWindow.dismiss();
                //创建文件夹
                File dirFile = new File(Constant.DOWNLOAD_IMG_PATH);
                if (!dirFile.exists()) {
                    dirFile.mkdir();
                }
                String fileName = System.currentTimeMillis() + ".jpg";
                OKHttpUtil.downFile(mContext, downloadUrl, dirFile.getPath(), fileName);
                if (Util.isNetAvailable(mContext)) {
                    Toast.makeText(mContext, "图片下载成功!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "图片下载失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //取消
        pop_img_dw_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDownLoadImagePopWindow.dismiss();
            }
        });
        //点击空白取消
        pop_image_download_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDownLoadImagePopWindow.dismiss();
            }
        });
        mDownLoadImagePopWindow.showAtLocation(mDownLoadImageView, Gravity.CENTER, 0, 0);
    }

    private void initView() {
        mGson = new Gson();
        mIntent = getIntent();
        //获取上一个界面的json数据
        mDImageListJson = SpUtil.getDoubleImageListJson(mContext);
        //获取上一个界面传来的位置信息
        mArrayListPosition = mIntent.getIntExtra("mPosition", 0);
        mWhereFrom = mIntent.getStringExtra("mWhereFrom");
        dImgLookBtnFadanRl.setBackgroundColor(Color.parseColor("#ffffff"));
        //根据json处理数据
        if (TextUtils.isEmpty(mDImageListJson)) {
            Toast.makeText(mContext, "数据获取失败！", Toast.LENGTH_SHORT).show();
        } else {
            //有数据时进行数据的处理
            initDataList(mDImageListJson);
            //处理页面数据
            for (int i = 0; i < mImageUrlArrayList.size(); i++) {
                fragmentArrayList.add(DImageLookFragment.newInstance(mImageUrlArrayList.get(i)));
            }
            mSImageLookingAdapter = new SImageLookingAdapter(getSupportFragmentManager(), fragmentArrayList);
            dImgLookViewpager.setAdapter(mSImageLookingAdapter);
            dImgLookViewpager.setCurrentItem(mViewPagerPosition);
            dImgLookViewpager.addOnPageChangeListener(onPageChangeListener);

            //显示收藏的状态
            if (mImageDArrayList.get(mArrayListPosition).getIs_collect().equals("1")) {
                //已收藏
                dImgLookShoucan.setImageResource(R.drawable.shoucang_after);
            } else {
                //未收藏
                dImgLookShoucan.setImageResource(R.drawable.shoucang_detail_befor);
            }
            SpUtil.setStatisticsEventPageId(mContext, mImageDArrayList.get(mArrayListPosition).getId());
            //设置标题以及左侧的标签
            setTitleAndPosition();
            //展示动画
            ShowFristIntoAnim();
        }

    }

    private void setTitleAndPosition() {
        mItemTitle = mImageDArrayList.get(mArrayListPosition).getTitle();
        mItemSize = mImageDArrayList.get(mArrayListPosition).getSub_images().size();
        dImgLookTitle.setText(mItemTitle);//标题
        dImgLookImgListSize.setText("" + mItemSize);//子项的总长
        dImgLookImgPosition.setText("" + mItemPosition + "/");//当前子项所处的位置
        mShareUrl = mImageDArrayList.get(mArrayListPosition).getShare_url();
    }

    // 加载滑动演示动画
    private void ShowFristIntoAnim() {
        if (TextUtils.isEmpty(SpUtil.getFristIntoImageLook(mContext))) {
            SpUtil.setFristIntoImageLook(mContext, "已经显示");
            dImgLookFristIntoRl.setVisibility(View.VISIBLE);
            dImgLookFristIntoAnim.setImageResource(R.drawable.anim_frist_into_look_img);
            AnimationDrawable animationDrawable = (AnimationDrawable) dImgLookFristIntoAnim.getDrawable();
            animationDrawable.start();
        } else {
            dImgLookFristIntoRl.setVisibility(View.GONE);
        }
    }

    //页面滑动监听事件
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            Log.e(TAG, "滑动==========position=========" + position);
            if (positionOffset != 0) {
                if (lastValue >= positionOffsetPixels) {
                    //右滑
                    isLeft = false;
                } else if (lastValue < positionOffsetPixels) {
                    //左滑
                    isLeft = true;
                }
            }
            lastValue = positionOffsetPixels;
//            Log.e(TAG, "==============lastValue=============" + lastValue);
            //另一种加载方式
//            if (position > currentPosition) {
//                isLeft = false;
//                currentPosition = position;
//            } else if (position < currentPosition) {
//                isLeft = true;
//                currentPosition = position;
//            }
        }

        @Override
        public void onPageSelected(int position) {
            Log.e(TAG, "onPageSelected==================" + position);
            if (position + 1 > currentPosition) {
                Log.e(TAG, "===左划===");
                mItemPosition++;
                if (mItemPosition > mItemSize) {
                    //套图的位置向后移动 n->n+1
                    mArrayListPosition = mArrayListPosition + 1;
                    //套图子项所在位置重新赋值
                    mItemPosition = 1;
                    //套图子项长度重新赋值
                    mItemSize = mImageDArrayList.get(mArrayListPosition).getSub_images().size();
                    //套图的标题
                    mItemTitle = mImageDArrayList.get(mArrayListPosition).getTitle();
                    //设置到对应的位置
                    dImgLookTitle.setText("" + mItemTitle);//标题
                    dImgLookImgListSize.setText("" + mItemSize);//子项的总长
                    dImgLookImgPosition.setText("" + mItemPosition + "/");//当前子项所处的位置
                    //用户的收藏状态
                    if (mImageDArrayList.get(mArrayListPosition).getIs_collect().equals("1")) {
                        //已收藏
                        dImgLookShoucan.setImageResource(R.drawable.shoucang_after);
                    } else {
                        //未收藏
                        dImgLookShoucan.setImageResource(R.drawable.shoucang_detail_befor);
                    }
                    //分享的Url
                    mShareUrl = mImageDArrayList.get(mArrayListPosition).getShare_url();
                }
                dImgLookImgPosition.setText("" + mItemPosition + "/");//当前子项所处的位置

                currentPosition = position + 1;
            } else if (position + 1 <= currentPosition) {
                Log.e(TAG, "===右划===");
                //右划  (n->n-1)
                mItemPosition--;
                if (mItemPosition < 1) {
                    //套图位置向前移动 n->n-1
                    mArrayListPosition = mArrayListPosition - 1;
                    //套图子项所在位置重新赋值
                    mItemPosition = mImageDArrayList.get(mArrayListPosition).getSub_images().size();
                    //套图子项长度重新赋值
                    mItemSize = mImageDArrayList.get(mArrayListPosition).getSub_images().size();
                    //套图的标题
                    mItemTitle = mImageDArrayList.get(mArrayListPosition).getTitle();
                    //设置到对应的位置
                    dImgLookTitle.setText("" + mItemTitle);//标题
                    dImgLookImgListSize.setText("" + mItemSize);//子项的总长
                    dImgLookImgPosition.setText("" + mItemPosition + "/");//当前子项所处的位置
                    //用户的收藏状态
                    if (mImageDArrayList.get(mArrayListPosition).getIs_collect().equals("1")) {
                        //已收藏
                        dImgLookShoucan.setImageResource(R.drawable.shoucang_after);
                    } else {
                        //未收藏
                        dImgLookShoucan.setImageResource(R.drawable.shoucang_detail_befor);
                    }
                    //分享的Url
                    mShareUrl = mImageDArrayList.get(mArrayListPosition).getShare_url();
                }
                dImgLookImgPosition.setText("" + mItemPosition + "/");//当前子项所处的位置
                currentPosition = position + 1;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
//            Log.e(TAG, "状态========state======" + state);
            if (state == 2) {
                if (isLeft) {

                } else {
//                    mItemPosition--;
//                    if (mItemPosition < 1) {
//                        //套图位置向前移动 n->n-1
//                        mArrayListPosition = mArrayListPosition - 1;
//                        //套图子项所在位置重新赋值
//                        if (mArrayListPosition <= -1) {
//                            //滑动太快引起的问题
//                            Log.e(TAG, "出现了位置变化========" + mArrayListPosition);
//                            mArrayListPosition = 0;
//                        } else {
//                            mItemPosition = mImageDArrayList.get(mArrayListPosition).getSub_images().size();
//                            //套图子项长度重新赋值
//                            mItemSize = mImageDArrayList.get(mArrayListPosition).getSub_images().size();
//                            //套图的标题
//                            mItemTitle = mImageDArrayList.get(mArrayListPosition).getTitle();
//                            //设置到对应的位置
//                            dImgLookTitle.setText("" + mItemTitle);//标题
//                            dImgLookImgListSize.setText("" + mItemSize);//子项的总长
//                            dImgLookImgPosition.setText("" + mItemPosition + "/");//当前子项所处的位置
//                            //用户的收藏状态
//                            if (mImageDArrayList.get(mArrayListPosition).getIs_collect().equals("1")) {
//                                //已收藏
//                                dImgLookShoucan.setImageResource(R.drawable.shoucang_after);
//                            } else {
//                                //未收藏
//                                dImgLookShoucan.setImageResource(R.drawable.shoucang_detail_befor);
//                            }
//                            //分享的Url
//                            mShareUrl = mImageDArrayList.get(mArrayListPosition).getShare_url();
//                        }
//                    }
//                    dImgLookImgPosition.setText("" + mItemPosition + "/");//当前子项所处的位置
                }
            }
        }
    };

    //初始化事件
    private void initDataList(String mSImageListJson) {
        try {
            JSONArray jsonArray = new JSONArray(mSImageListJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                _ImageD imageD = mGson.fromJson(jsonArray.get(i).toString(), _ImageD.class);
                //套图数据的集合
                mImageDArrayList.add(imageD);
                //图片地址的数据集
                mImageUrlArrayList.addAll(imageD.getSub_images());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //从上个界面点击进入时viewPgae所在位置
        for (int i = 0; i < mArrayListPosition; i++) {
            mViewPagerPosition = mViewPagerPosition + mImageDArrayList.get(i).getSub_images().size();
        }
        //判断滑动方向的数据
        currentPosition = mViewPagerPosition;
        //当前套图子项长度
        mItemSize = mImageDArrayList.get(mArrayListPosition).getSub_images().size();
    }

    @OnClick({R.id.d_img_look_title_back, R.id.d_img_look_shoucan,
            R.id.d_img_look_shoucan_ll, R.id.d_img_look_share,
            R.id.d_img_look_share_ll, R.id.d_img_look_btn_fadan,
            R.id.d_img_look_i_know, R.id.d_img_look_fadan_img,
            R.id.d_img_look_fadan_close, R.id.d_img_look_frist_into_rl, R.id.d_img_look_fadan_rl})
    public void onViewClickedInDImageLookActivity(View view) {
        switch (view.getId()) {
            case R.id.d_img_look_title_back:
                //返回按钮
                finish();
                break;
            case R.id.d_img_look_shoucan:
            case R.id.d_img_look_shoucan_ll:
                //点击收藏
                //收藏按钮
                if (TextUtils.isEmpty(AppInfoUtil.getUserid(mContext))) {
                    //用户未登录 跳转到登录页面
//                    Toast.makeText(mContext, "您还没有登陆,请登陆后再来收藏!", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(mContext, NewLoginActivity.class);
////                    intent.putExtra("isFav", true);
//                    startActivityForResult(intent, 0);
                } else {
                    HttpShouCang(mImageDArrayList.get(mArrayListPosition).getId(), mImageDArrayList.get(mArrayListPosition).getIs_collect());
                }
                break;
            case R.id.d_img_look_share:
            case R.id.d_img_look_share_ll:
                //分享
                UMWeb umWeb = new UMWeb(mImageDArrayList.get(mArrayListPosition).getShare_url() + "&channel=app&subchannel=android&chcode=" + AppInfoUtil.getChannType(mContext));
                umWeb.setDescription("这里的图片很不错，分享给你哦");
                umWeb.setTitle(mImageDArrayList.get(mArrayListPosition).getTitle());
                umWeb.setThumb(new UMImage(mContext, mImageDArrayList.get(mArrayListPosition).getCover_url()));
                new ShareAction(DImageLookingActivity.this)
                        .setDisplayList(SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ)
                        .withMedia(umWeb).open();
                break;
            case R.id.d_img_look_btn_fadan:
                //底部发单的条
                Intent intent = new Intent(mContext, NewWebViewActivity.class);
                intent.putExtra("mLoadingUrl", SpUtil.getzjzxaj12(mContext));
                mContext.startActivity(intent);
                break;
            case R.id.d_img_look_i_know:
                //第一次进来的操作指示关闭按钮
                dImgLookFristIntoRl.setVisibility(View.GONE);
                break;
            case R.id.d_img_look_fadan_img:
                //弹窗发单按钮
                Log.e(TAG, "点击了发单按钮===================");
                dImgLookFadanRl.setVisibility(View.GONE);
                Intent intent2 = new Intent(mContext, NewWebViewActivity.class);
                intent2.putExtra("mLoadingUrl", SpUtil.getzjzxaj10(mContext));
                startActivity(intent2);
                break;
            case R.id.d_img_look_fadan_close:
                //发单弹窗关闭按钮
                dImgLookFadanRl.setVisibility(View.GONE);
                break;
            case R.id.d_img_look_frist_into_rl:
                //防止层级间事件透传
                break;
            case R.id.d_img_look_fadan_rl:
                //防止层级间事件透传
                break;
        }
    }

    @Override
    protected void onResume() {
        if (SpUtil.getImageDetailDataToken(mContext).equals(Util.getDateToken() + "detail")) {
            //比对成功 当天已经弹窗过
            isAddTime = false;
        } else {
            isAddTime = true;
            ShowFadanTanChuang();
        }
        //装修公司屏蔽收藏按钮
        if (AppInfoUtil.getTypeid(mContext).equals("3")) {
            dImgLookShoucanLl.setVisibility(View.GONE);
        } else {
            dImgLookShoucanLl.setVisibility(View.GONE);
        }
        super.onResume();
    }

    //用户进入页面每天一次的弹窗
    private void ShowFadanTanChuang() {
        if (SpUtil.getImageDetailDataToken(mContext).equals(Util.getDateToken() + "detail")) {
            Log.e(TAG, "弹窗已经出现过！！！！！！！！============");
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (isAddTime) {
                            Thread.sleep(1000);
                            mTime++;
                            Log.e(TAG, "时间累积=============" + mTime);
                            if (mTime >= 45) {
                                //出现弹窗
                                isAddTime = false;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //显示弹窗
                                        SpUtil.setImageDetailDataToken(mContext, Util.getDateToken() + "detail");
                                        dImgLookFadanRl.setVisibility(View.VISIBLE);
                                        dImgLookFristIntoRl.setVisibility(View.GONE);
                                        if (mDownLoadImagePopWindow != null) {
                                            mDownLoadImagePopWindow.dismiss();
                                        }
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

    //收藏事件
    private void HttpShouCang(String id, String state) {
        HashMap<String, Object> param = new HashMap<>();
        String _id = getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("id", "");
        param.put("token", Util.getDateToken());
        param.put("state", state);
        param.put("id", id);// id
        param.put("uid", _id);// 以前是AppInfoUtil.getUserid(mContext)  昭仲要求改成 _id
        param.put("user_type", AppInfoUtil.getTypeid(mContext));
        param.put("type", "2");
        Util.setErrorLog(TAG, "昭仲要传的参数： state=" + state + "   图的id=" + id + "   人的id=" + _id + " user_type=" + AppInfoUtil.getTypeid(mContext) + " type=2");

        OKHttpUtil.post(Constant.IMAGE_COLLECT, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "收藏链接失败=======" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Util.setErrorLog(TAG, "昭仲返回的结果：" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int status = jsonObject.optInt("status");
                    if (status == 200) {
                        final String msg = jsonObject.optString("msg");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (msg.equals("收藏成功")) {
                                    //收藏成功将收藏的图标改变 将数据模型改变 同时通知外部的数据改变
                                    dImgLookShoucan.setImageResource(R.drawable.shoucang_after);
                                    mImageDArrayList.get(mArrayListPosition).setIs_collect("1");
                                    Toast.makeText(mContext, "收藏成功", Toast.LENGTH_SHORT).show();
                                    if (mWhereFrom.equals("NewImageDFragment")) {
                                        EventBusUtil.sendEvent(new Event(EC.EventCode.NOTIF_D_SHOUCANG_DATA_CHANGE_IS_COLLECT, mArrayListPosition));
                                    } else if (mWhereFrom.equals("DesignCaseActivity")) {
                                        EventBusUtil.sendEvent(new Event(EC.EventCode.NOTIF_DESIGN_CASE_ACTIVITY_MODE_IS_COLLECT, mArrayListPosition));
                                    } else if (mWhereFrom.equals("DecComActivity")) {
                                        EventBusUtil.sendEvent(new Event(EC.EventCode.NOTIF_DECCOMACTIVITY_MODE_IS_COLLECT, mArrayListPosition));
                                    }
                                } else {
                                    //取消收藏  同时通知外部的数据改变
                                    dImgLookShoucan.setImageResource(R.drawable.shoucang_detail_befor);
                                    mImageDArrayList.get(mArrayListPosition).setIs_collect("0");
                                    Toast.makeText(mContext, "取消收藏成功", Toast.LENGTH_SHORT).show();
                                    if (mWhereFrom.equals("NewImageDFragment")) {
                                        EventBusUtil.sendEvent(new Event(EC.EventCode.NOTIF_D_SHOUCANG_DATA_CHANGE_IS_NOT_COLLECT, mArrayListPosition));
                                    } else if (mWhereFrom.equals("TaotuActivity")) {
                                        // TODO: 2017/11/20
                                        EventBusUtil.sendEvent(new Event(EC.EventCode.DELETE_TAOTU_LIST_CODE, mArrayListPosition));
                                    } else if (mWhereFrom.equals("DesignCaseActivity")) {
                                        EventBusUtil.sendEvent(new Event(EC.EventCode.NOTIF_DESIGN_CASE_ACTIVITY_MODE_IS_NOT_COLLECT, mArrayListPosition));
                                    } else if (mWhereFrom.equals("DecComActivity")) {
                                        EventBusUtil.sendEvent(new Event(EC.EventCode.NOTIF_DECCOMACTIVITY_MODE_IS_NOT_COLLECT, mArrayListPosition));
                                    }
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
