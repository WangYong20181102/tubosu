package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.Intent;
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
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.SImageLookingAdapter;
import com.tbs.tobosutype.base.*;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.bean.TaotuEntity;
import com.tbs.tobosutype.bean._ImageS;
import com.tbs.tobosutype.fragment.SImageLookFragment;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.EventBusUtil;
import com.tbs.tobosutype.utils.SpUtil;
import com.tbs.tobosutype.utils.Util;
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
 * 单图查看器
 * creat by lin
 * 传入该页面 位置
 * 页面的数据通过Sp中转
 */
public class SImageLookingActivity extends com.tbs.tobosutype.base.BaseActivity {

    @BindView(R.id.s_img_look_viewpager)
    ViewPager sImgLookViewpager;
    @BindView(R.id.s_img_look_title_back)
    LinearLayout sImgLookTitleBack;
    @BindView(R.id.s_img_look_shoucan)
    ImageView sImgLookShoucan;
    @BindView(R.id.s_img_look_share)
    ImageView sImgLookShare;
    @BindView(R.id.s_img_look_title_bar_rl)
    RelativeLayout sImgLookTitleBarRl;
    @BindView(R.id.s_img_look_btn_fadan)
    LinearLayout sImgLookBtnFadan;
    @BindView(R.id.s_img_look_shoucan_ll)
    LinearLayout sImgLookShoucanLl;
    @BindView(R.id.s_img_look_share_ll)
    LinearLayout sImgLookShareLl;
    @BindView(R.id.s_img_look_frist_into_anim)
    ImageView sImgLookFristIntoAnim;
    @BindView(R.id.s_img_look_i_know)
    TextView sImgLookIKnow;
    @BindView(R.id.s_img_look_frist_into_rl)
    RelativeLayout sImgLookFristIntoRl;
    @BindView(R.id.s_img_look_fadan_img)
    ImageView sImgLookFadanImg;
    @BindView(R.id.s_img_look_fadan_close)
    ImageView sImgLookFadanClose;
    @BindView(R.id.s_img_look_fadan_rl)
    RelativeLayout sImgLookFadanRl;

    private String TAG = "SImageLookingActivity";
    private Context mContext;
    private Gson mGson;
    private Intent mIntent;
    private String mSImageListJson = "";//从上个界面传来的数据json
    private ArrayList<_ImageS> mImageSArrayList = new ArrayList<>();
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private SImageLookingAdapter mSImageLookingAdapter;
    private int mPosition = 0;//上一个界面传来的位置信息
    private String mWhereFrom="";//上一个界面传来用
    private boolean isShowingBanner = true;//是否显示banner  默认显示
    private PopupWindow mDownLoadImagePopWindow;//长按显示下载的pop
    private View mDownLoadImageView;//承载pop的view
    private boolean isAddTime = false;//是否在倒计时
    private int mTime = 0;//计时时间
    //判断viewpager滑动情况
    private int lastValue = -1;
    private boolean isLeft = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simage_looking);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    @Override
    protected boolean havePageId() {
        return true;
    }

    private void initView() {
        mGson = new Gson();
        mIntent = getIntent();
        //获取上一个界面传来的json数据
        mSImageListJson = SpUtil.getSingImageListJson(mContext);
        //获取上一个界面传来的位置信息
        mPosition = mIntent.getIntExtra("mPosition", 0);
        mWhereFrom = mIntent.getStringExtra("mWhereFrom");
        //根据json处理数据
        if (TextUtils.isEmpty(mSImageListJson)) {
            Toast.makeText(mContext, "数据获取失败！", Toast.LENGTH_SHORT).show();
        } else {
            //有数据时进行数据的处理
            initDataList(mSImageListJson);
            //处理页面数据
            for (int i = 0; i < mImageSArrayList.size(); i++) {
                fragmentArrayList.add(SImageLookFragment.newInstance(mImageSArrayList.get(i)));
            }
            mSImageLookingAdapter = new SImageLookingAdapter(getSupportFragmentManager(), fragmentArrayList);
            sImgLookViewpager.setAdapter(mSImageLookingAdapter);
            sImgLookViewpager.setCurrentItem(mPosition);
            sImgLookViewpager.addOnPageChangeListener(onPageChangeListener);
            //显示收藏的状态
            if (mImageSArrayList.get(mPosition).getIs_collect().equals("1")) {
                //已收藏
                sImgLookShoucan.setImageResource(R.drawable.shoucang_after);
            } else {
                //未收藏
                sImgLookShoucan.setImageResource(R.drawable.shoucang_detail_befor);
            }
            //设置统计的页面id
            SpUtil.setStatisticsEventPageId(mContext,mImageSArrayList.get(mPosition).getId());
        }
        //展示动画
        ShowFristIntoAnim();
    }

    // 加载滑动演示动画
    private void ShowFristIntoAnim() {
        if (TextUtils.isEmpty(SpUtil.getFristIntoImageLook(mContext))) {
            SpUtil.setFristIntoImageLook(mContext, "已经显示");
            sImgLookFristIntoRl.setVisibility(View.VISIBLE);
            sImgLookFristIntoAnim.setImageResource(R.drawable.anim_frist_into_look_img);
            AnimationDrawable animationDrawable = (AnimationDrawable) sImgLookFristIntoAnim.getDrawable();
            animationDrawable.start();
        } else {
            sImgLookFristIntoRl.setVisibility(View.GONE);
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
                                        sImgLookFadanRl.setVisibility(View.VISIBLE);
                                        sImgLookFristIntoRl.setVisibility(View.GONE);
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

    //页面滑动监听事件
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            Log.e(TAG, "单图查看器ViewPager====onPageScrolled===position====" + position+"====positionOffset===="+positionOffset+"====positionOffsetPixels==="+positionOffsetPixels);
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
        }

        @Override
        public void onPageSelected(int position) {
            //滑动时的位置切换
//            Log.e(TAG, "单图查看器ViewPager====onPageSelected===position====" + position);
            mPosition = position;
            //改变收藏的状态
            if (mImageSArrayList.get(mPosition).getIs_collect().equals("1")) {
                sImgLookShoucan.setImageResource(R.drawable.shoucang_after);
            } else {
                sImgLookShoucan.setImageResource(R.drawable.shoucang_detail_befor);
            }
            if (isLeft){
                Log.e(TAG,"--->左划==============="+position);
            }else {
                Log.e(TAG,"--->右划==============="+position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
//            Log.e(TAG, "单图查看器ViewPager====onPageScrollStateChanged===state====" + state);
        }
    };

    //初始化事件
    private void initDataList(String mSImageListJson) {
        try {
            JSONArray jsonArray = new JSONArray(mSImageListJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                _ImageS imageS = mGson.fromJson(jsonArray.get(i).toString(), _ImageS.class);
                mImageSArrayList.add(imageS);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.CLICK_SIMAGE_IN_LOOK_PHOTO:
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
                    sImgLookTitleBarRl.setAnimation(mAnimBc);
                    sImgLookBtnFadan.setAnimation(mAnimBanner);

                    sImgLookBtnFadan.setVisibility(View.GONE);
                    sImgLookTitleBarRl.setVisibility(View.GONE);
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

                    sImgLookTitleBarRl.setAnimation(mAnimBc);
                    sImgLookBtnFadan.setAnimation(mAnimBanner);

                    sImgLookBtnFadan.setVisibility(View.VISIBLE);
                    sImgLookTitleBarRl.setVisibility(View.VISIBLE);

                }
                break;
            case EC.EventCode.LOOG_CLICK_SIMAGE_IN_LOOK_PHOTO:
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

    @OnClick({R.id.s_img_look_title_back, R.id.s_img_look_shoucan,
            R.id.s_img_look_share, R.id.s_img_look_btn_fadan,
            R.id.s_img_look_shoucan_ll, R.id.s_img_look_share_ll,
            R.id.s_img_look_frist_into_rl, R.id.s_img_look_i_know,
            R.id.s_img_look_fadan_img, R.id.s_img_look_fadan_close,
            R.id.s_img_look_fadan_rl})
    public void onViewClickedInSImageLookingActivity(View view) {
        switch (view.getId()) {
            case R.id.s_img_look_title_back:
                //返回按钮
                finish();
                break;
            case R.id.s_img_look_shoucan:
            case R.id.s_img_look_shoucan_ll:
                //收藏按钮
                if (TextUtils.isEmpty(AppInfoUtil.getUserid(mContext))) {
                    //用户未登录 跳转到登录页面
                    Toast.makeText(mContext, "您还没有登陆,请登陆后再来收藏!", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(mContext, LoginActivity.class);
                    Intent intent = new Intent(mContext, NewLoginActivity.class);
//                    intent.putExtra("isFav", true);
                    startActivityForResult(intent, 0);
                } else {
                    HttpShouCang(mImageSArrayList.get(mPosition).getId(), mImageSArrayList.get(mPosition).getIs_collect());
                }
                break;
            case R.id.s_img_look_share:
            case R.id.s_img_look_share_ll:
                //分享按钮
                //分享
                UMWeb umWeb = new UMWeb(mImageSArrayList.get(mPosition).getShare_url() + "&channel=app&subchannel=android&chcode=" + AppInfoUtil.getChannType(mContext));
                umWeb.setDescription("这里的图片很不错，分享给你哦");
                umWeb.setTitle(mImageSArrayList.get(mPosition).getTitle());
                umWeb.setThumb(new UMImage(mContext, mImageSArrayList.get(mPosition).getCover_url()));
                new ShareAction(SImageLookingActivity.this)
                        .setDisplayList(SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ)
                        .withMedia(umWeb).open();
                break;
            case R.id.s_img_look_btn_fadan:
                //底部的发单按钮
                Intent intent = new Intent(mContext, NewWebViewActivity.class);
                intent.putExtra("mLoadingUrl", Constant.IAMGE_DETAIL_BUTTON);
                mContext.startActivity(intent);
                break;
            case R.id.s_img_look_i_know:
                //点击我知道按钮 动画消失
                sImgLookFristIntoRl.setVisibility(View.GONE);
                break;
            case R.id.s_img_look_frist_into_rl:
                //初次进入的图层 不做任何处理  只是设置点击事件 防止触摸透传
                break;
            case R.id.s_img_look_fadan_img:
                //发单的图片 跳转到发单的页面
                Log.e(TAG, "点击了发单按钮===================");
                sImgLookFadanRl.setVisibility(View.GONE);
                Intent intent2 = new Intent(mContext, NewWebViewActivity.class);
                intent2.putExtra("mLoadingUrl", Constant.IAMGE_DETAIL_DIALOG);
                startActivity(intent2);
                break;
            case R.id.s_img_look_fadan_close:
                //关闭发单的按钮
                sImgLookFadanRl.setVisibility(View.GONE);
                break;
            case R.id.s_img_look_fadan_rl:
                //不做任何处理
                break;
        }
    }

    //收藏事件
    private void HttpShouCang(String id, String state) {
        HashMap<String, Object> param = new HashMap<>();
        String _id = mContext.getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("id", "");
        param.put("token", Util.getDateToken());
        param.put("state", state);
        param.put("id", id);
        param.put("uid", _id); // 以前是AppInfoUtil.getUserid(mContext) 昭仲要求改成
        param.put("user_type", AppInfoUtil.getTypeid(mContext));
        param.put("type", "1");


        Util.setErrorLog(TAG, "昭仲要传的参数 state=" + state + "   id=" + id + "   uid=" +_id + "  user_type=" + AppInfoUtil.getTypeid(mContext) + " type=1");
        OKHttpUtil.post(Constant.IMAGE_COLLECT, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "收藏链接失败=======" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Util.setErrorLog(TAG, "昭仲返回的结果："+json);
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
                                    sImgLookShoucan.setImageResource(R.drawable.shoucang_after);
                                    mImageSArrayList.get(mPosition).setIs_collect("1");
                                    Toast.makeText(mContext, "收藏成功", Toast.LENGTH_SHORT).show();
                                    if(mWhereFrom.equals("NewImageSFragment")){
                                        EventBusUtil.sendEvent(new Event(EC.EventCode.NOTIF_SHOUCANG_DATA_CHANGE_IS_COLLECT, mPosition));
                                    }
                                } else {
                                    //取消收藏  同时通知外部的数据改变
                                    sImgLookShoucan.setImageResource(R.drawable.shoucang_detail_befor);
                                    mImageSArrayList.get(mPosition).setIs_collect("0");
                                    Toast.makeText(mContext, "取消收藏成功", Toast.LENGTH_SHORT).show();
                                    if(mWhereFrom.equals("NewImageSFragment")){
                                        // TODO: 2017/11/20
                                        EventBusUtil.sendEvent(new Event(EC.EventCode.NOTIF_SHOUCANG_DATA_CHANGE_IS_NOT_COLLECT, mPosition));
                                    }else if(mWhereFrom.equals("DanTuActivity")){
                                        EventBusUtil.sendEvent(new Event(EC.EventCode.DELETE_DANTU_LIST_CODE, mPosition));
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
