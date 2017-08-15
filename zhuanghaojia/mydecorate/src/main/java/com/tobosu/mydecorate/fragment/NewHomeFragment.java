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
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.activity.CalculaterActivity;
import com.tobosu.mydecorate.activity.FreeActivity;
import com.tobosu.mydecorate.activity.GetPriceActivity;
import com.tobosu.mydecorate.activity.PopOrderActivity;
import com.tobosu.mydecorate.activity.SmartDesignActivity;
import com.tobosu.mydecorate.adapter.AuthorAdapter;
import com.tobosu.mydecorate.adapter.DecorateTitleAdapter;
import com.tobosu.mydecorate.application.MyApplication;
import com.tobosu.mydecorate.entity._HomePage;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.util.GlideUtils;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.view.ScrollViewExtend;
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
    private _HomePage homePage;

    private ImageView nhfFadan;//发单按钮

    private RollPagerView nhfViewPage;//轮播图

    private LinearLayout nhfMianfeisheji;//免费设计的浮层
    private LinearLayout nhfZhinengbaojia;//智能报价的浮层
    private LinearLayout nhfZhuangxiujisuanqi;//装修计算器的浮层

    private LinearLayout nhfGengduozhuangxiutoutiao;//更多装修头条
    private int fadingHeight = 400;
    /*-----------------------------------*/

//    private LinearLayout nhfFirstText;//第一篇文章 用于点击进入文章的详情
//    private LinearLayout nhfSecondText;//第二篇文章 用于点击进入文章的详情
//    private LinearLayout nhfThirdText;//第三篇文章 用于点击进入文章的详情
//    //装修头条的文章 封面图
//    private ImageView nhfTextImg1;//第一篇文章的图片
//    private ImageView nhfTextImg2;//第二篇文章的图片
//    private ImageView nhfTextImg3;//第三篇文章的图片
//    //装修头条文章的人气值
//    private TextView nhfTextRenqi1;//第一篇文章的人气值
//    private TextView nhfTextRenqi2;//第二篇文章的人气值
//    private TextView nhfTextRenqi3;//第三篇文章的人气值
//    //装修头条文章的标题
//    private TextView nhfTextTitle1;//第一篇文章的标题
//    private TextView nhfTextTitle2;//第二篇文章的标题
//    private TextView nhfTextTitle3;//第三篇文章的标题
//
//    //活跃作者
//    private LinearLayout nhfAuthor1;//活跃第一的作者
//    private LinearLayout nhfAuthor2;//活跃第二的作者
//    private LinearLayout nhfAuthor3;//活跃第三的作者
//    //活跃作者的头像
//    private ImageView nhfAuthorIcon1;//第一个作者的头像
//    private ImageView nhfAuthorIcon2;//第二个作者的头像
//    private ImageView nhfAuthorIcon3;//第三个作者的头像
//    //活跃作者的名字
//    private TextView nhfAuthorName1;//第一个作者名字
//    private TextView nhfAuthorName2;//第二个作者名字
//    private TextView nhfAuthorName3;//第三个作者名字
//    //活跃作者的浏览量
//    private TextView nhfLiulan1;
//    private TextView nhfLiulan2;
//    private TextView nhfLiulan3;
//    //活跃作者文章数
//    private TextView nhfWenzhang1;
//    private TextView nhfWenzhang2;
//    private TextView nhfWenzhang3;

    private List<_HomePage.Carousel> carouselList = new ArrayList<>();//轮播图集合


    private RecyclerView recyclerViewDecorateTitle; // 装修头条
    private LinearLayoutManager linearManager;
    private RecyclerView recyclerViewAuthor; // 作者排行榜
    private LinearLayoutManager linearManager1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
//        Log.e(TAG, "获取缓存信息====" + CacheManager.getHomeFragCache(mContext));
//        homePage = new _HomePage(CacheManager.getHomeFragCache(mContext));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_home, null);
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
                        //将data储存在sp中 方便在后续取得数据
                        homePage = new _HomePage(data);
                        Log.e(TAG, "首页数据请求成功====" + homePage.getAuthorList().get(0).getView_count());
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
            if(y<=2) {
                relTransparenbt.setAlpha(0);
            }

            if (y > fadingHeight) {
                y = fadingHeight;
            }

            if (y>0 && y<=fadingHeight){
                relTransparenbt.setAlpha(y/4);
                Util.setErrorLog("NewHomeFragment", y/4 + "");
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
        nhfViewPage = (RollPagerView) rootView.findViewById(R.id.nhf_roll_pagerview);
        nhfFadan = (ImageView) rootView.findViewById(R.id.nhf_fadan);
        nhfMianfeisheji = (LinearLayout) rootView.findViewById(R.id.nhf_mianfeisheji);
        nhfZhinengbaojia = (LinearLayout) rootView.findViewById(R.id.nhf_zhinengbaojia);
        nhfZhuangxiujisuanqi = (LinearLayout) rootView.findViewById(R.id.nhf_zhuangxiujisuanqi);
        nhfGengduozhuangxiutoutiao = (LinearLayout) rootView.findViewById(R.id.nhf_gengduozhuangxiutoutiao);
//        drawable = getResources().getDrawable(R.drawable.color_first_head);
//        drawable.setAlpha(START_ALPHA);
//        rel_homebar.setBackgroundDrawable(drawable);
        scrollViewExtend.setOnScrollChangedListener(scrollChangedListener);
//        nhfFirstText = (LinearLayout) rootView.findViewById(R.id.nhf_first_text);
//        nhfSecondText = (LinearLayout) rootView.findViewById(R.id.nhf_second_text);
//        nhfThirdText = (LinearLayout) rootView.findViewById(R.id.nhf_third_text);
//
//        nhfTextImg1 = (ImageView) rootView.findViewById(R.id.nhf_img_text1);
//        nhfTextImg2 = (ImageView) rootView.findViewById(R.id.nhf_img_text2);
//        nhfTextImg3 = (ImageView) rootView.findViewById(R.id.nhf_img_text3);
//
//        nhfTextRenqi1 = (TextView) rootView.findViewById(R.id.nhf_renqi1);
//        nhfTextRenqi2 = (TextView) rootView.findViewById(R.id.nhf_renqi2);
//        nhfTextRenqi3 = (TextView) rootView.findViewById(R.id.nhf_renqi3);
//
//        nhfTextTitle1 = (TextView) rootView.findViewById(R.id.nhf_text_title1);
//        nhfTextTitle2 = (TextView) rootView.findViewById(R.id.nhf_text_title2);
//        nhfTextTitle3 = (TextView) rootView.findViewById(R.id.nhf_text_title3);
//
//        nhfAuthor1 = (LinearLayout) rootView.findViewById(R.id.nhf_author1);
//        nhfAuthor2 = (LinearLayout) rootView.findViewById(R.id.nhf_author2);
//        nhfAuthor3 = (LinearLayout) rootView.findViewById(R.id.nhf_author3);
//
//        nhfAuthorIcon1 = (ImageView) rootView.findViewById(R.id.nhf_author_icon1);
//        nhfAuthorIcon2 = (ImageView) rootView.findViewById(R.id.nhf_author_icon2);
//        nhfAuthorIcon3 = (ImageView) rootView.findViewById(R.id.nhf_author_icon3);
//
//        nhfAuthorName1 = (TextView) rootView.findViewById(R.id.nhf_author_name1);
//        nhfAuthorName2 = (TextView) rootView.findViewById(R.id.nhf_author_name2);
//        nhfAuthorName3 = (TextView) rootView.findViewById(R.id.nhf_author_name3);
//
//        nhfLiulan1 = (TextView) rootView.findViewById(R.id.nhf_liulang1);
//        nhfLiulan2 = (TextView) rootView.findViewById(R.id.nhf_liulang2);
//        nhfLiulan3 = (TextView) rootView.findViewById(R.id.nhf_liulang3);
//
//        nhfWenzhang1 = (TextView) rootView.findViewById(R.id.nhf_wenzhang1);
//        nhfWenzhang2 = (TextView) rootView.findViewById(R.id.nhf_wenzhang2);
//        nhfWenzhang3 = (TextView) rootView.findViewById(R.id.nhf_wenzhang3);

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

//        nhfFirstText.setOnClickListener(occl);
//        nhfSecondText.setOnClickListener(occl);
//        nhfThirdText.setOnClickListener(occl);
//
//        nhfAuthor1.setOnClickListener(occl);
//        nhfAuthor2.setOnClickListener(occl);
//        nhfAuthor3.setOnClickListener(occl);

        carouselList.addAll(homePage.getCarouselList());
        nhfViewPage.setAdapter(new MyPageAdapter(nhfViewPage));
        nhfViewPage.setOnItemClickListener(onItemClick);
//        String url = "http://img0.bdstatic.com/img/image/shouye/xiaoxiao/%E5%B0%8F%E6%B8%85%E6%96%B0614.jpg";
//        GlideUtils.glideLoader(mContext, url, 0, 0, nhfAuthorIcon1, GlideUtils.CIRCLE_IMAGE);
//        Log.e(TAG, "处理后的Token===" + Util.getDateToken());
    }

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

//        if (homePage.getArticleList().get(0).getTitle().length() > 14) {
//            nhfTextTitle1.setText("" + homePage.getArticleList().get(0).getTitle().substring(0, 13) + "...");
//        } else {
//            nhfTextTitle1.setText("" + homePage.getArticleList().get(0).getTitle());
//        }
//        if (homePage.getArticleList().get(1).getTitle().length() > 14) {
//            nhfTextTitle2.setText("" + homePage.getArticleList().get(1).getTitle().substring(0, 13) + "...");
//        } else {
//            nhfTextTitle2.setText("" + homePage.getArticleList().get(1).getTitle());
//        }
//        if (homePage.getArticleList().get(2).getTitle().length() > 14) {
//            nhfTextTitle3.setText("" + homePage.getArticleList().get(2).getTitle().substring(0, 13) + "...");
//        } else {
//            nhfTextTitle3.setText("" + homePage.getArticleList().get(2).getTitle());
//        }
//        //设置装修头条 封面图
//        GlideUtils.glideLoader(mContext, homePage.getArticleList().get(0).getImage_url(), 0, R.mipmap.jiazai_loading, nhfTextImg1, GlideUtils.ROUND_IMAGE);
//        GlideUtils.glideLoader(mContext, homePage.getArticleList().get(1).getImage_url(), 0, R.mipmap.jiazai_loading, nhfTextImg2, GlideUtils.ROUND_IMAGE);
//        GlideUtils.glideLoader(mContext, homePage.getArticleList().get(2).getImage_url(), 0, R.mipmap.jiazai_loading, nhfTextImg3, GlideUtils.ROUND_IMAGE);
//        //设置人气值
//        nhfTextRenqi1.setText("" + homePage.getArticleList().get(0).getView_count());
//        nhfTextRenqi2.setText("" + homePage.getArticleList().get(1).getView_count());
//        nhfTextRenqi3.setText("" + homePage.getArticleList().get(2).getView_count());
//
//        //设置作者排行榜 头像
//        GlideUtils.glideLoader(mContext, homePage.getAuthorList().get(0).getIcon(), 0, R.mipmap.jiazai_loading, nhfAuthorIcon1, GlideUtils.CIRCLE_IMAGE);
//        GlideUtils.glideLoader(mContext, homePage.getAuthorList().get(1).getIcon(), 0, R.mipmap.jiazai_loading, nhfAuthorIcon2, GlideUtils.CIRCLE_IMAGE);
//        GlideUtils.glideLoader(mContext, homePage.getAuthorList().get(2).getIcon(), 0, R.mipmap.jiazai_loading, nhfAuthorIcon3, GlideUtils.CIRCLE_IMAGE);
//        //名称
//        if (homePage.getAuthorList().get(0).getNick().length() > 5) {
//            nhfAuthorName1.setText("" + homePage.getAuthorList().get(0).getNick().substring(0, 4) + "...");
//        } else {
//            nhfAuthorName1.setText("" + homePage.getAuthorList().get(0).getNick());
//        }
//        if (homePage.getAuthorList().get(1).getNick().length() > 5) {
//            nhfAuthorName2.setText("" + homePage.getAuthorList().get(1).getNick().substring(0, 4) + "...");
//        } else {
//            nhfAuthorName2.setText("" + homePage.getAuthorList().get(1).getNick());
//        }
//        if (homePage.getAuthorList().get(2).getNick().length() > 5) {
//            nhfAuthorName3.setText("" + homePage.getAuthorList().get(2).getNick().substring(0, 4) + "...");
//        } else {
//            nhfAuthorName3.setText("" + homePage.getAuthorList().get(2).getNick());
//        }
//        //浏览量
//        nhfLiulan1.setText("" + homePage.getAuthorList().get(0).getView_count());
//        nhfLiulan2.setText("" + homePage.getAuthorList().get(1).getView_count());
//        nhfLiulan3.setText("" + homePage.getAuthorList().get(2).getView_count());
//        //发布文章量
//        nhfWenzhang1.setText("" + homePage.getAuthorList().get(0).getArticle_count());
//        nhfWenzhang2.setText("" + homePage.getAuthorList().get(1).getArticle_count());
//        nhfWenzhang3.setText("" + homePage.getAuthorList().get(2).getArticle_count());
    }


    private void initRecyclerViewAdapter(Context context){
        DecorateTitleAdapter adapter = new DecorateTitleAdapter(MyApplication.getApp(), homePage.getArticleList());
        recyclerViewDecorateTitle.setAdapter(adapter);
        AuthorAdapter authorAdapter = new AuthorAdapter(MyApplication.getApp(), homePage.getAuthorList());
        recyclerViewAuthor.setAdapter(authorAdapter);
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
//                    startActivity(new Intent(mContext, FreeActivity.class));
                    startActivity(new Intent(mContext, GetPriceActivity.class));
                    break;
                case R.id.nhf_zhinengbaojia:
                    //进入装修报价
                    startActivity(new Intent(mContext, SmartDesignActivity.class));
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
//                case R.id.nhf_first_text:
//                    //进入第一篇文章详情
//                    intent = new Intent(mContext, NewArticleDetailActivity.class);
//                    intent.putExtra("id", homePage.getArticleList().get(0).getAid());
//                    intent.putExtra("author_id", homePage.getArticleList().get(0).getUid());
//                    startActivity(intent);
//                    break;
//                case R.id.nhf_second_text:
//                    //进入第二篇文章
//                    intent = new Intent(mContext, NewArticleDetailActivity.class);
//                    intent.putExtra("id", homePage.getArticleList().get(1).getAid());
//                    intent.putExtra("author_id", homePage.getArticleList().get(1).getUid());
//                    startActivity(intent);
//                    break;
//                case R.id.nhf_third_text:
//                    //进入第三篇文章
//                    intent = new Intent(mContext, NewArticleDetailActivity.class);
//                    intent.putExtra("id", homePage.getArticleList().get(2).getAid());
//                    intent.putExtra("author_id", homePage.getArticleList().get(2).getUid());
//                    startActivity(intent);
//                    break;
//                case R.id.nhf_author1:
//                    //进入第一个作者的详情
//                    Intent intent1 = new Intent(mContext, NewAuthorDetailActivity.class);
//                    intent1.putExtra("author_id", homePage.getAuthorList().get(0).getUid());
//                    intent1.putExtra("page_num", homePage.getAuthorList().get(0).getArticle_count());
//                    startActivity(intent1);
//                    break;
//                case R.id.nhf_author2:
//                    //进入第二个作者的详情
//                    Intent intent2 = new Intent(mContext, NewAuthorDetailActivity.class);
//                    intent2.putExtra("author_id", homePage.getAuthorList().get(1).getUid());
//                    intent2.putExtra("page_num", homePage.getAuthorList().get(1).getArticle_count());
//                    startActivity(intent2);
//                    break;
//                case R.id.nhf_author3:
//                    //进入第三个作者的详情
//                    Intent intent3 = new Intent(mContext, NewAuthorDetailActivity.class);
//                    intent3.putExtra("author_id", homePage.getAuthorList().get(2).getUid());
//                    intent3.putExtra("page_num", homePage.getAuthorList().get(2).getArticle_count());
//                    startActivity(intent3);
//                    break;
            }
        }
    };
    private OnItemClickListener onItemClick = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            if (homePage.getCarouselList().get(position).getContent_url() != null) {
                if(homePage.getCarouselList().get(position).getContent_url().contains("http://m.tobosu.com/app/pub?channel=seo")){
                    Intent it = new Intent(mContext, FreeActivity.class);
                    it.putExtra("urldata", homePage.getCarouselList().get(position).getContent_url());
                    startActivity(it);
                }else {
                    Uri uri = Uri.parse(homePage.getCarouselList().get(position).getContent_url());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        }
    };

    private class MyPageAdapter extends LoopPagerAdapter {
        public MyPageAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            GlideUtils.glideLoader(mContext, carouselList.get(position).getImg_url(), 0, R.mipmap.jiazai_loading, view);
            return view;
        }

        @Override
        public int getRealCount() {
            return carouselList.size();
        }
    }

}
