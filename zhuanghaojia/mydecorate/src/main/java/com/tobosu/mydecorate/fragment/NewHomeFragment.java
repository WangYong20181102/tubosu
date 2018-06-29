package com.tobosu.mydecorate.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.activity.CalculaterActivity;
import com.tobosu.mydecorate.activity.NewWebViewActivity;
import com.tobosu.mydecorate.activity.PopOrderActivity;
import com.tobosu.mydecorate.adapter.AuthorAdapter;
import com.tobosu.mydecorate.adapter.DecorateTitleAdapter;
import com.tobosu.mydecorate.application.MyApplication;
import com.tobosu.mydecorate.entity._NewHomePage;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.pagclass.GlideImageLoader;
import com.tobosu.mydecorate.util.SpUtil;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.view.ScrollViewExtend;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mr.Lin on 2017/5/23 16:06.
 */

public class NewHomeFragment extends Fragment {
    private String TAG = "NewHomeFragment";
    private Context mContext;//关联的上下文  nhf==new home fragment
    private ScrollViewExtend scrollViewExtend;
    //        private _HomePage homePage;
    private _NewHomePage mNewHomePage;
    private Gson mGson;

    private ImageView nhfFadan;//发单按钮

    //    private RollPagerView nhfViewPage;//轮播图
    private Banner myBanner;//轮播图

    private LinearLayout nhfMianfeisheji;//免费设计的浮层
    private LinearLayout nhfZhinengbaojia;//智能报价的浮层
    private LinearLayout nhfZhuangxiujisuanqi;//装修计算器的浮层

    private LinearLayout nhfGengduozhuangxiutoutiao;//更多装修头条
    private int fadingHeight = 400;

    //    private List<_HomePage.Carousel> carouselList = new ArrayList<>();//轮播图集合
    private List<_NewHomePage.CarouselBean> carouselList = new ArrayList<>();//轮播图集合
    private List<String> mImageList = new ArrayList<>();//图片集合

    private RecyclerView recyclerViewDecorateTitle; // 装修头条
    private LinearLayoutManager linearManager;
    private RecyclerView recyclerViewAuthor; // 作者排行榜
    private LinearLayoutManager linearManager1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_home, null);
        mGson = new Gson();
        bindView(rootView);//绑定布局的控件
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        httpGetHomeFragMessage();
    }

    private void httpGetHomeFragMessage() {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", Util.getDateToken());
        hashMap.put("is_default", "4");
        okHttpUtil.post(Constant.HOME_FRAGMENT_URL, hashMap, new OKHttpUtil.BaseCallBack() {

            @Override
            public void onSuccess(Response response, String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        String data = jsonObject.getString("data");
                        Log.e(TAG, "请求数据json结果=============" + data);
                        //将data储存在sp中 方便在后续取得数据
                        mNewHomePage = mGson.fromJson(data, _NewHomePage.class);

                        Log.e(TAG, "首页数据请求成功====" + mNewHomePage.getAuthor().get(0).getIcon());
                        initView();
                        initViewEvent();
                    } else {
                        Toast.makeText(mContext, "服务器错误！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Request request, IOException e) {
                Util.setErrorLog(TAG, "------请求失败->>" + request.toString());
            }

            @Override
            public void onError(Response response, int code) {
                Util.setErrorLog(TAG, "-----请求错误->>" + response.message());
            }
        });
    }

    /**
     * 顶部标题栏的透明度监控
     */
    private ScrollViewExtend.OnScrollChangedListener scrollChangedListener = new ScrollViewExtend.OnScrollChangedListener() {

        @Override
        public void onScrollChanged(ScrollView who, int x, int y, int oldx, int oldy) {
            if (y <= 2) {
                relTransparenbt.setAlpha(0);
            }

            if (y > fadingHeight) {
                y = fadingHeight;
            }

            if (y > 0 && y <= fadingHeight) {
                relTransparenbt.setAlpha(y / 4);
//                Util.setErrorLog("NewHomeFragment", y / 4 + "");
            }
        }
    };

    private Drawable drawable;
    private TextView home_textView;
    private RelativeLayout relTransparenbt;

    private void bindView(View rootView) {
        scrollViewExtend = (ScrollViewExtend) rootView.findViewById(R.id.scrollViewExtend);
        scrollViewExtend.scrollTo(0, 0);
        home_textView = (TextView) rootView.findViewById(R.id.home_textView);

        relTransparenbt = (RelativeLayout) rootView.findViewById(R.id.relTransparenbt);
        relTransparenbt.setAlpha(0);
//        nhfViewPage = (RollPagerView) rootView.findViewById(R.id.nhf_roll_pagerview);
        myBanner = rootView.findViewById(R.id.new_home_banner);
        nhfFadan = (ImageView) rootView.findViewById(R.id.nhf_fadan);
        nhfMianfeisheji = (LinearLayout) rootView.findViewById(R.id.nhf_mianfeisheji);
        nhfZhinengbaojia = (LinearLayout) rootView.findViewById(R.id.nhf_zhinengbaojia);
        nhfZhuangxiujisuanqi = (LinearLayout) rootView.findViewById(R.id.nhf_zhuangxiujisuanqi);
        nhfGengduozhuangxiutoutiao = (LinearLayout) rootView.findViewById(R.id.nhf_gengduozhuangxiutoutiao);

        scrollViewExtend.setOnScrollChangedListener(scrollChangedListener);


        recyclerViewDecorateTitle = (RecyclerView) rootView.findViewById(R.id.recyclerviewDecorateTitle);
        recyclerViewAuthor = (RecyclerView) rootView.findViewById(R.id.recyclerviewAuthor);
        linearManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewDecorateTitle.setLayoutManager(linearManager);
        linearManager1 = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewAuthor.setLayoutManager(linearManager1);
    }

    /**
     * 控件相关的事件
     */
    private void initViewEvent() {
        if (!carouselList.isEmpty()) {
            carouselList.clear();
        }
        nhfFadan.setOnClickListener(occl);
        nhfMianfeisheji.setOnClickListener(occl);
        nhfZhinengbaojia.setOnClickListener(occl);
        nhfZhuangxiujisuanqi.setOnClickListener(occl);
        nhfGengduozhuangxiutoutiao.setOnClickListener(occl);

        // TODO: 2018/3/15 初始化banner
        carouselList.addAll(mNewHomePage.getCarousel());
        if (!mImageList.isEmpty()) {
            mImageList.clear();
        }
        //初始化图片集合
        for (int i = 0; i < carouselList.size(); i++) {
            mImageList.add(carouselList.get(i).getImg_url());
        }
        //设置banner样式
        myBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片的加载器
        myBanner.setImageLoader(new GlideImageLoader());
        //设置图片的集合
        myBanner.setImages(mImageList);
        //设置banner动画的效果
        myBanner.setBannerAnimation(Transformer.Default);
        //设置标题集合
//        myBanner.setBannerTitles(mTitleList);
        //设置是否自动轮播
        myBanner.isAutoPlay(true);
        //设置轮播时间
        myBanner.setDelayTime(3000);
        //设置轮播的指示器
        myBanner.setIndicatorGravity(BannerConfig.RIGHT);
        //设置点击事件
        myBanner.setOnBannerListener(onBannerListener);
        //开始轮播
        myBanner.start();

    }

    private OnBannerListener onBannerListener = new OnBannerListener() {
        @Override
        public void OnBannerClick(int position) {
            Intent intent = new Intent(mContext, NewWebViewActivity.class);
            intent.putExtra("mLoadingUrl", mNewHomePage.getCarousel().get(position).getContent_url());
            startActivity(intent);
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;//mCtx 是成员变量，上下文引用
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }


    //初始化UI的显示
    private void initView() {
        //设置装修头条 标题
        initRecyclerViewAdapter(mContext);

    }


    private void initRecyclerViewAdapter(Context context) {
        DecorateTitleAdapter adapter = new DecorateTitleAdapter(MyApplication.getApp(), mNewHomePage.getArticle());
        recyclerViewDecorateTitle.setAdapter(adapter);
        AuthorAdapter authorAdapter = new AuthorAdapter(MyApplication.getApp(), mNewHomePage.getAuthor());
        recyclerViewAuthor.setAdapter(authorAdapter);
        adapter.notifyDataSetChanged();
        authorAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener occl = new View.OnClickListener() {
        Intent intent;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.nhf_fadan:
                    //发单跳转到引导发单页面
                    intent = new Intent(mContext, PopOrderActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nhf_mianfeisheji:
                    //进入免费设计
                    intent = new Intent(mContext, NewWebViewActivity.class);
                    intent.putExtra("mLoadingUrl", SpUtil.getzhjaj01(mContext));
                    startActivity(intent);
                    break;
                case R.id.nhf_zhinengbaojia:
                    //进入装修报价
                    intent = new Intent(mContext, NewWebViewActivity.class);
                    intent.putExtra("mLoadingUrl", SpUtil.getzhjaj02(mContext));
                    startActivity(intent);
                    break;
                case R.id.nhf_zhuangxiujisuanqi:
                    //进入装修计算器
                    intent = new Intent(mContext, CalculaterActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nhf_gengduozhuangxiutoutiao:
                    //更多进入装修宝典
                    Intent intent = new Intent(Constant.GO_DECORATE_BIBLE_ACTION);
                    mContext.sendBroadcast(intent);
                    break;
            }
        }
    };

}
