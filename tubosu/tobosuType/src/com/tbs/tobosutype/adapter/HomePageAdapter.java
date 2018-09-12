package com.tbs.tobosutype.adapter;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.DImageLookingActivity;
import com.tbs.tobosutype.activity.DecorationCaseActivity;
import com.tbs.tobosutype.activity.DecorationCaseDetailActivity;
import com.tbs.tobosutype.activity.LearnRenovationActivity;
import com.tbs.tobosutype.activity.NewWebViewActivity;
import com.tbs.tobosutype.activity.TopicDetailActivity;
import com.tbs.tobosutype.bean._HomePage;
import com.tbs.tobosutype.customview.OnlyPointIndicator;
import com.tbs.tobosutype.customview.ScaleTransitionPagerTitleView;
import com.tbs.tobosutype.customview.ShadowTransformer;
import com.tbs.tobosutype.fragment.HomePageArticleFragment;
import com.tbs.tobosutype.model.FastBlur;
import com.tbs.tobosutype.utils.GlideUtils;
import com.tbs.tobosutype.utils.SpUtil;
import com.tbs.tobosutype.utils.Util;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2018/8/31 14:15.
 * 土拨鼠App4.0 新首页的适配器
 */
public class HomePageAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {

    private Context mContext;
    private String TAG = "HomePageAdapter";
    private _HomePage mHomePage;
    private CaseCardViewAdapter mCaseCardViewAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private ArrayList<Fragment> mFragmentArrayList;
    private MyFragmentPagerStateAdapter myFragmentPagerStateAdapter;
    private Gson mGson;
    private FragmentManager mFragmentManager;


    //单项点击事件
    private static interface OnHomePageItemClickLister {
        void onHomePageItemClick(View view, int position);
    }

    private OnHomePageItemClickLister onHomePageItemClickLister = null;

    public void setOnHomePageItemClickLister(OnHomePageItemClickLister onHomePageItemClickLister) {
        this.onHomePageItemClickLister = onHomePageItemClickLister;
    }

    /**
     * todo  初始化  这个地方还要添加 _HomePage 的文章项集合
     * todo 初始的文章集合进行拆分 不变项有一部分 填充项有一部分 填充到后面不断添加的list中
     */

    public HomePageAdapter(Context context, _HomePage homePage, FragmentManager fragmentManager) {
        this.mContext = context;
        this.mHomePage = homePage;
        this.mFragmentManager = fragmentManager;
        mGson = new Gson();
    }

    @Override
    public int getItemViewType(int position) {
//        if (position == 0) {
        return 0;//显示头部  不变项
//        }
//        else {
//            return 1;//显示尾部 动态添加项
//        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            //展示头部
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_page_head, parent, false);
            HomePageHeadViewHolder homePageHeadViewHolder = new HomePageHeadViewHolder(view);
//            homePageHeadViewHolder.ihph_xuansheji_rl.setOnClickListener(this);
//            homePageHeadViewHolder.ihph_kananli_rl.setOnClickListener(this);
//            homePageHeadViewHolder.ihph_zhaozhuangxiu_rl.setOnClickListener(this);
//            homePageHeadViewHolder.ihph_xuezhuangxiu_rl.setOnClickListener(this);
//            homePageHeadViewHolder.ihph_zhuangxiudalibao_cv.setOnClickListener(this);
//            homePageHeadViewHolder.ihph_mianfeisheji_cv.setOnClickListener(this);
//            homePageHeadViewHolder.ihph_jisubaojia_cv.setOnClickListener(this);
//            homePageHeadViewHolder.ihph_zhuanyeliangfang_cv.setOnClickListener(this);
//            homePageHeadViewHolder.ihph_jianyue_cv.setOnClickListener(this);
//            homePageHeadViewHolder.ihph_tianyuan_cv.setOnClickListener(this);
//            homePageHeadViewHolder.ihph_xiandai_cv.setOnClickListener(this);
//            homePageHeadViewHolder.ihph_zhongshi_cv.setOnClickListener(this);
//            homePageHeadViewHolder.ihph_meishi_cv.setOnClickListener(this);
//            homePageHeadViewHolder.ihph_more_design_style_rl.setOnClickListener(this);
//            homePageHeadViewHolder.ihph_more_case_ll.setOnClickListener(this);
            return homePageHeadViewHolder;
        } else if (viewType == 1) {
            //展示文章项
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_page_topicle, parent, false);
            HomePageFootViewHolder homePageFootViewHolder = new HomePageFootViewHolder(view);
            return homePageFootViewHolder;
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HomePageHeadViewHolder) {
            //展示头部信息
            //todo 设置轮播图
            //设置点击事件
            ((HomePageHeadViewHolder) holder).ihph_banner_mz_banner_view.setBannerPageClickListener(bannerPageClickListener);
            //设置页面滑动事件
            ((HomePageHeadViewHolder) holder).ihph_banner_mz_banner_view.addPageChangeListener(onPageChangeListener);
            //设置适配器
            ((HomePageHeadViewHolder) holder).ihph_banner_mz_banner_view.setPages(mHomePage.getData().getBanner(), new MZHolderCreator<MZBannerViewHolder>() {
                @Override
                public MZBannerViewHolder createViewHolder() {
                    return new MZBannerViewHolder();
                }
            });
            // TODO: 2018/9/4 设置四个键的点击事件
            //学装修
            ((HomePageHeadViewHolder) holder).ihph_xuansheji_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent("goto_activity_xiaoguotu");
                    it.putExtra("position", 1);
                    mContext.sendBroadcast(it);
                }
            });
            //看案例
            ((HomePageHeadViewHolder) holder).ihph_kananli_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, DecorationCaseActivity.class));

                }
            });
            //找装修
            ((HomePageHeadViewHolder) holder).ihph_zhaozhuangxiu_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent("goto_activity_zhuangxiu");
                    it.putExtra("position", 2);
                    mContext.sendBroadcast(it);
                }
            });
            //学装修
            ((HomePageHeadViewHolder) holder).ihph_xuezhuangxiu_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent gotoLearnActivity = new Intent(mContext, LearnRenovationActivity.class);
                    mContext.startActivity(gotoLearnActivity);
                }
            });
            //装修大礼包
            ((HomePageHeadViewHolder) holder).ihph_zhuangxiudalibao_cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(mContext, NewWebViewActivity.class);
                    intent1.putExtra("mLoadingUrl", SpUtil.getTbsAj35(mContext));
                    mContext.startActivity(intent1);
                }
            });
            //免费设计
            ((HomePageHeadViewHolder) holder).ihph_mianfeisheji_cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(mContext, NewWebViewActivity.class);
                    intent1.putExtra("mLoadingUrl", SpUtil.getTbsAj36(mContext));
                    mContext.startActivity(intent1);
                }
            });
            //急速报价
            ((HomePageHeadViewHolder) holder).ihph_jisubaojia_cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(mContext, NewWebViewActivity.class);
                    intent1.putExtra("mLoadingUrl", SpUtil.getTbsAj37(mContext));
                    mContext.startActivity(intent1);
                }
            });
            //专业量房
            ((HomePageHeadViewHolder) holder).ihph_zhuanyeliangfang_cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(mContext, NewWebViewActivity.class);
                    intent1.putExtra("mLoadingUrl", SpUtil.getTbsAj38(mContext));
                    mContext.startActivity(intent1);
                }
            });
            // TODO: 2018/9/4 设置图片
            //设置装修大礼包
            if (!TextUtils.isEmpty(SpUtil.getNewHomeXianshihaoliImgUrl(mContext))) {
                if (SpUtil.getNewHomeXianshihaoliImgUrl(mContext).contains(".gif")) {
                    Glide.with(mContext)
                            .load(SpUtil.getNewHomeXianshihaoliImgUrl(mContext))
                            .asGif()
                            .into(((HomePageHeadViewHolder) holder)
                                    .ihph_zhuangxiudalibao_img);
                } else {
                    Glide.with(mContext)
                            .load(SpUtil.getNewHomeXianshihaoliImgUrl(mContext))
                            .into(((HomePageHeadViewHolder) holder)
                                    .ihph_zhuangxiudalibao_img);
                }
            }

            if (!TextUtils.isEmpty(SpUtil.getNewHomeMianfeishejiImgUrl(mContext))) {
                //设置免费设计
                if (SpUtil.getNewHomeMianfeishejiImgUrl(mContext).contains(".gif")) {
                    Glide.with(mContext)
                            .load(SpUtil.getNewHomeMianfeishejiImgUrl(mContext))
                            .asGif()
                            .into(((HomePageHeadViewHolder) holder)
                                    .ihph_mianfeisheji_img);
                } else {
                    Glide.with(mContext)
                            .load(SpUtil.getNewHomeMianfeishejiImgUrl(mContext))
                            .into(((HomePageHeadViewHolder) holder)
                                    .ihph_mianfeisheji_img);
                }

            }

            if (!TextUtils.isEmpty(SpUtil.getNewHomeJisubaojiaImgUrl(mContext))) {
                //急速报价
                if (SpUtil.getNewHomeJisubaojiaImgUrl(mContext).contains(".gif")) {
                    Glide.with(mContext)
                            .load(SpUtil.getNewHomeJisubaojiaImgUrl(mContext))
                            .asGif()
                            .into(((HomePageHeadViewHolder) holder)
                                    .ihph_jisubaojia_img);
                } else {
                    Glide.with(mContext)
                            .load(SpUtil.getNewHomeJisubaojiaImgUrl(mContext))
                            .into(((HomePageHeadViewHolder) holder)
                                    .ihph_jisubaojia_img);
                }

            }
            if (!TextUtils.isEmpty(SpUtil.getNewHomeZhuanyeliangfangImgUrl(mContext))) {
                //专业量房
                if (SpUtil.getNewHomeZhuanyeliangfangImgUrl(mContext).contains(".gif")) {
                    Glide.with(mContext)
                            .load(SpUtil.getNewHomeZhuanyeliangfangImgUrl(mContext))
                            .asGif()
                            .into(((HomePageHeadViewHolder) holder)
                                    .ihph_zhuanyeliangfang_img);
                } else {
                    Glide.with(mContext)
                            .load(SpUtil.getNewHomeZhuanyeliangfangImgUrl(mContext))
                            .into(((HomePageHeadViewHolder) holder)
                                    .ihph_zhuanyeliangfang_img);
                }

            }
            // TODO: 2018/9/4 设置选设计模块
            for (int i = 0; i < mHomePage.getData().getImpression().size(); i++) {
                if (mHomePage.getData().getImpression().get(i).getType().equals("jianyue")) {
                    //设置简约
                    Glide.with(mContext)
                            .load(mHomePage.getData().getImpression().get(i).getCover_url())
                            .into(((HomePageHeadViewHolder) holder).ihph_jianyue_img);

                    //点击事件
                    final int finalI = i;
                    ((HomePageHeadViewHolder) holder).ihph_jianyue_cv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String DImageJson = new Gson().toJson(mHomePage.getData().getImpression().get(finalI).getSub_images());
                            Util.setErrorLog(TAG, DImageJson);
                            SpUtil.setDoubleImageListJson(mContext, DImageJson);
                            Intent intent = new Intent(mContext, DImageLookingActivity.class);
                            intent.putExtra("mPosition", 0);
                            intent.putExtra("mWhereFrom", "NewhomeActivity");
                            mContext.startActivity(intent);
                        }
                    });

                }
                if (mHomePage.getData().getImpression().get(i).getType().equals("tianyuan")) {
                    //设置田园
                    Glide.with(mContext)
                            .load(mHomePage.getData().getImpression().get(i).getCover_url())
                            .into(((HomePageHeadViewHolder) holder).ihph_tianyuan_img);

                    //点击事件
                    final int finalI = i;
                    ((HomePageHeadViewHolder) holder).ihph_tianyuan_cv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String DImageJson = new Gson().toJson(mHomePage.getData().getImpression().get(finalI).getSub_images());
                            Util.setErrorLog(TAG, DImageJson);
                            SpUtil.setDoubleImageListJson(mContext, DImageJson);
                            Intent intent = new Intent(mContext, DImageLookingActivity.class);
                            intent.putExtra("mPosition", 0);
                            intent.putExtra("mWhereFrom", "NewhomeActivity");
                            mContext.startActivity(intent);
                        }
                    });
                }
                if (mHomePage.getData().getImpression().get(i).getType().equals("xiandai")) {
                    //设置现代
                    Glide.with(mContext)
                            .load(mHomePage.getData().getImpression().get(i).getCover_url())
                            .into(((HomePageHeadViewHolder) holder).ihph_xiandai_img);
                    //点击事件
                    final int finalI = i;
                    ((HomePageHeadViewHolder) holder).ihph_xiandai_cv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String DImageJson = new Gson().toJson(mHomePage.getData().getImpression().get(finalI).getSub_images());
                            Util.setErrorLog(TAG, DImageJson);
                            SpUtil.setDoubleImageListJson(mContext, DImageJson);
                            Intent intent = new Intent(mContext, DImageLookingActivity.class);
                            intent.putExtra("mPosition", 0);
                            intent.putExtra("mWhereFrom", "NewhomeActivity");
                            mContext.startActivity(intent);
                        }
                    });
                }
                if (mHomePage.getData().getImpression().get(i).getType().equals("zhongshi")) {
                    //设置中式
                    Glide.with(mContext)
                            .load(mHomePage.getData().getImpression().get(i).getCover_url())
                            .into(((HomePageHeadViewHolder) holder).ihph_zhongshi_img);

                    //点击事件
                    final int finalI = i;
                    ((HomePageHeadViewHolder) holder).ihph_zhongshi_cv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String DImageJson = new Gson().toJson(mHomePage.getData().getImpression().get(finalI).getSub_images());
                            Util.setErrorLog(TAG, DImageJson);
                            SpUtil.setDoubleImageListJson(mContext, DImageJson);
                            Intent intent = new Intent(mContext, DImageLookingActivity.class);
                            intent.putExtra("mPosition", 0);
                            intent.putExtra("mWhereFrom", "NewhomeActivity");
                            mContext.startActivity(intent);
                        }
                    });
                }
                if (mHomePage.getData().getImpression().get(i).getType().equals("meishi")) {
                    //设置美式
                    Glide.with(mContext)
                            .load(mHomePage.getData().getImpression().get(i).getCover_url())
                            .into(((HomePageHeadViewHolder) holder).ihph_meishi_img);

                    //点击事件
                    final int finalI = i;
                    ((HomePageHeadViewHolder) holder).ihph_meishi_cv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String DImageJson = new Gson().toJson(mHomePage.getData().getImpression().get(finalI).getSub_images());
                            Util.setErrorLog(TAG, DImageJson);
                            SpUtil.setDoubleImageListJson(mContext, DImageJson);
                            Intent intent = new Intent(mContext, DImageLookingActivity.class);
                            intent.putExtra("mPosition", 0);
                            intent.putExtra("mWhereFrom", "NewhomeActivity");
                            mContext.startActivity(intent);
                        }
                    });
                }
            }

            // TODO: 2018/9/4 设置案例模块
            //设置初始的背景
            if (mHomePage.getData().getCases() != null && !mHomePage.getData().getCases().isEmpty()) {
                Glide.with(mContext)
                        .load(mHomePage.getData().getCases().get(0).getCover_url())
                        .transform(new FastBlur(mContext, 100))
                        .into(((HomePageHeadViewHolder) holder).ihph_case_bg_img);
            }


            mCaseCardViewAdapter = new CaseCardViewAdapter(mContext, mHomePage.getData().getCases());
            mCardShadowTransformer = new ShadowTransformer(((HomePageHeadViewHolder) holder).ihph_case_viewpager, mCaseCardViewAdapter);
            ((HomePageHeadViewHolder) holder).ihph_case_viewpager.setAdapter(mCaseCardViewAdapter);
            ((HomePageHeadViewHolder) holder).ihph_case_viewpager.setPageTransformer(false, mCardShadowTransformer);
            ((HomePageHeadViewHolder) holder).ihph_case_viewpager.setOffscreenPageLimit(3);
            ((HomePageHeadViewHolder) holder).ihph_case_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if (position == mHomePage.getData().getCases().size() && positionOffset == 0) {
                        ((HomePageHeadViewHolder) holder).ihph_more_case_ll.setVisibility(View.VISIBLE);
                    } else {
                        ((HomePageHeadViewHolder) holder).ihph_more_case_ll.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onPageSelected(int position) {
                    //滑动时模糊处理
                    Glide.with(mContext)
                            .load(mHomePage.getData().getCases().get(position).getCover_url())
                            .transform(new FastBlur(mContext, 100))
                            .into(((HomePageHeadViewHolder) holder).ihph_case_bg_img);

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            mCaseCardViewAdapter.setOnItemClickLister(new CaseCardViewAdapter.OnItemClickLister() {
                @Override
                public void onItemClick(View view, int position) {
                    //点击进入案例详情
                    Intent intent = new Intent(mContext, DecorationCaseDetailActivity.class);
                    intent.putExtra("deco_case_id", mHomePage.getData().getCases().get(position).getId());
                    mContext.startActivity(intent);
                }
            });
            mCardShadowTransformer.enableScaling(true);
            //更多案例
            ((HomePageHeadViewHolder) holder).ihph_more_case_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, DecorationCaseActivity.class));
                }
            });

            // TODO: 2018/9/4 第一个广告标签图
            if (mHomePage.getData().getIndex_advert_1() == null
                    || mHomePage.getData().getIndex_advert_1().isEmpty()) {
                //没有时不显示
                ((HomePageHeadViewHolder) holder).ihph_adv1_rl.setVisibility(View.GONE);
            } else {
                //设置点击事件
                ((HomePageHeadViewHolder) holder).ihph_adv1_mzbanner.setBannerPageClickListener(Adv1bannerPageClickListener);
                //设置适配器
                ((HomePageHeadViewHolder) holder).ihph_adv1_mzbanner.setPages(mHomePage.getData().getIndex_advert_1(), new MZHolderCreator<ADV1MZBannerViewHolder>() {
                    @Override
                    public ADV1MZBannerViewHolder createViewHolder() {
                        return new ADV1MZBannerViewHolder();
                    }
                });
            }

            // TODO: 2018/9/4 设置学装修模块
            // TODO: 2018/9/5 学装修的列表页
            //将数据注入
            mFragmentArrayList = new ArrayList<>();
            for (int i = 0; i < mHomePage.getData().getArticle().size(); i++) {
                String homepageArticleJson = mGson.toJson(mHomePage.getData().getArticle().get(i));
                mFragmentArrayList.add(HomePageArticleFragment.newInstance(homepageArticleJson));
            }
            myFragmentPagerStateAdapter = new MyFragmentPagerStateAdapter(mFragmentManager, mFragmentArrayList);
            ((HomePageHeadViewHolder) holder).ihph_learnzx_viewpager.setAdapter(myFragmentPagerStateAdapter);
            ((HomePageHeadViewHolder) holder).ihph_learnzx_viewpager.setOffscreenPageLimit(mHomePage.getData().getArticle().size());
            myFragmentPagerStateAdapter.notifyDataSetChanged();

            //顶部的滑块
            ((HomePageHeadViewHolder) holder).ihph_learnzx_indicator.setBackgroundColor(Color.WHITE);
            CommonNavigator commonNavigator = new CommonNavigator(mContext);
            commonNavigator.setAdapter(new CommonNavigatorAdapter() {
                @Override
                public int getCount() {
                    return mHomePage.getData().getArticle_type() == null ? 0 : mHomePage.getData().getArticle_type().size();
                }

                @Override
                public IPagerTitleView getTitleView(Context context, final int i) {
                    SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                    simplePagerTitleView.setText("" + mHomePage.getData().getArticle_type().get(i).getTitle());
                    simplePagerTitleView.setTextSize(18);
                    simplePagerTitleView.setNormalColor(Color.parseColor("#999999"));
                    simplePagerTitleView.setSelectedColor(Color.parseColor("#ff882e"));
                    simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((HomePageHeadViewHolder) holder).ihph_learnzx_viewpager.setCurrentItem(i);
                        }
                    });
                    return simplePagerTitleView;
                }

                @Override
                public IPagerIndicator getIndicator(Context context) {
                    OnlyPointIndicator indicator = new OnlyPointIndicator(context);
                    indicator.setColors(Color.parseColor("#ff882e"));
                    return indicator;
                }
            });
            ((HomePageHeadViewHolder) holder).ihph_learnzx_indicator.setNavigator(commonNavigator);
            ViewPagerHelper.bind(((HomePageHeadViewHolder) holder).ihph_learnzx_indicator,
                    ((HomePageHeadViewHolder) holder).ihph_learnzx_viewpager);

            //中间的广告banner层
            if (mHomePage.getData().getIndex_advert_2() != null
                    || !mHomePage.getData().getIndex_advert_2().isEmpty()) {
                //显示该层
                ((HomePageHeadViewHolder) holder).ihph_adv2_mzbanner.setBannerPageClickListener(Adv2bannerPageClickListener);
                //设置适配器
                ((HomePageHeadViewHolder) holder).ihph_adv2_mzbanner.setPages(mHomePage.getData().getIndex_advert_2(), new MZHolderCreator<ADV2MZBannerViewHolder>() {
                    @Override
                    public ADV2MZBannerViewHolder createViewHolder() {
                        return new ADV2MZBannerViewHolder();
                    }
                });
            } else {
                //隐藏该层
                ((HomePageHeadViewHolder) holder).ihph_adv2_mzbanner.setVisibility(View.GONE);
            }
        } else if (holder instanceof HomePageFootViewHolder) {
            //设置相应的数据
//            ((HomePageFootViewHolder) holder).item_hp_foot_time_tv.setText("" + mHomePage.getData().getTopic().get(position - 1).getAdd_time());
//            ((HomePageFootViewHolder) holder).item_hp_foot_title_tv.setText("" + mHomePage.getData().getTopic().get(position - 1).getTitle());
//            ((HomePageFootViewHolder) holder).item_hp_foot_dec_tv.setText("" + mHomePage.getData().getTopic().get(position - 1).getDesc());
//            GlideUtils.glideLoader(mContext, mHomePage.getData().getTopic().get(position-1).getCover_url(), ((HomePageFootViewHolder) holder).item_hp_foot_image_img);
//            //点击事件
//            ((HomePageFootViewHolder) holder).item_hp_foot_cv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //点击进入
//                    Intent it = new Intent(mContext, TopicDetailActivity.class);
//                    it.putExtra("mTopicId", mHomePage.getData().getTopic().get(position - 1).getId());
//                    mContext.startActivity(it);
//                }
//            });
        }
    }


    @Override
    public int getItemCount() {
        //返回的数量级 是文章集合的size+1
        return mHomePage.getData().getTopic() != null ? mHomePage.getData().getTopic().size() + 1 : 0;
    }

    @Override
    public void onClick(View v) {
        if (onHomePageItemClickLister != null) {
            onHomePageItemClickLister.onHomePageItemClick(v, (int) v.getTag());
        }
    }


    //轮播图的适配器
    public static class MZBannerViewHolder implements MZViewHolder<_HomePage.DataBean.BannerBean> {

        private CardView item_home_page_mz_banner_cv;
        private ImageView item_home_page_mz_banner_img;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_home_page_mz_banner, null);
            item_home_page_mz_banner_cv = view.findViewById(R.id.item_home_page_mz_banner_cv);
            item_home_page_mz_banner_img = view.findViewById(R.id.item_home_page_mz_banner_img);
            return view;
        }

        @Override
        public void onBind(Context context, int i, _HomePage.DataBean.BannerBean bannerBean) {
            GlideUtils.glideLoader(context, bannerBean.getImg_url(), item_home_page_mz_banner_img);
        }
    }

    //banner图层点击事件
    private MZBannerView.BannerPageClickListener bannerPageClickListener = new MZBannerView.BannerPageClickListener() {
        @Override
        public void onPageClick(View view, int i) {
            //点击进入
            Intent webIntent = new Intent(mContext, NewWebViewActivity.class);
            webIntent.putExtra("mLoadingUrl", mHomePage.getData().getBanner().get(i).getContent_url());
            mContext.startActivity(webIntent);
        }
    };
    //todo banner 滑动事件 用于切换底部背景颜色
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    //  todo 第一个广告图的适配器
    //轮播图的适配器
    public static class ADV1MZBannerViewHolder implements MZViewHolder<_HomePage.DataBean.IndexAdvert1Bean> {

        private CardView item_home_page_adv1_mz_banner_cv;
        private ImageView item_home_page_adv1_mz_banner_img;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_adv1_banner, null);
            item_home_page_adv1_mz_banner_cv = view.findViewById(R.id.item_home_page_adv1_mz_banner_cv);
            item_home_page_adv1_mz_banner_img = view.findViewById(R.id.item_home_page_adv1_mz_banner_img);
            return view;
        }

        @Override
        public void onBind(Context context, int i, _HomePage.DataBean.IndexAdvert1Bean indexAdvert1Bean) {
            GlideUtils.glideLoader(context, indexAdvert1Bean.getImg_url(), item_home_page_adv1_mz_banner_img);
        }
    }

    //广告图层点击事件
    private MZBannerView.BannerPageClickListener Adv1bannerPageClickListener = new MZBannerView.BannerPageClickListener() {
        @Override
        public void onPageClick(View view, int i) {
            //点击进入
            Intent webIntent = new Intent(mContext, NewWebViewActivity.class);
            webIntent.putExtra("mLoadingUrl", mHomePage.getData().getIndex_advert_1().get(i).getJump_url());
            mContext.startActivity(webIntent);
        }
    };

    // TODO: 2018/9/5 第二个广告图层适配器
    public static class ADV2MZBannerViewHolder implements MZViewHolder<_HomePage.DataBean.IndexAdvert2Bean> {

        private CardView item_home_page_adv2_mz_banner_cv;
        private ImageView item_home_page_adv2_mz_banner_img;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_adv2_banner, null);
            item_home_page_adv2_mz_banner_cv = view.findViewById(R.id.item_home_page_adv2_mz_banner_cv);
            item_home_page_adv2_mz_banner_img = view.findViewById(R.id.item_home_page_adv2_mz_banner_img);
            return view;
        }

        @Override
        public void onBind(Context context, int i, _HomePage.DataBean.IndexAdvert2Bean indexAdvert2Bean) {
            GlideUtils.glideLoader(context, indexAdvert2Bean.getImg_url(), item_home_page_adv2_mz_banner_img);
        }
    }

    //广告图层点击事件
    private MZBannerView.BannerPageClickListener Adv2bannerPageClickListener = new MZBannerView.BannerPageClickListener() {
        @Override
        public void onPageClick(View view, int i) {
            //点击进入
            Intent webIntent = new Intent(mContext, NewWebViewActivity.class);
            webIntent.putExtra("mLoadingUrl", mHomePage.getData().getIndex_advert_2().get(i).getJump_url());
            mContext.startActivity(webIntent);
        }
    };


    //ViewHolder  头部
    class HomePageHeadViewHolder extends RecyclerView.ViewHolder {
        private View ihph_banner_bg_view;//轮播图背景
        private MZBannerView ihph_banner_mz_banner_view;//轮播图
        private RelativeLayout ihph_xuansheji_rl;//选设计
        private RelativeLayout ihph_kananli_rl;//看案例
        private RelativeLayout ihph_zhaozhuangxiu_rl;//找装修
        private RelativeLayout ihph_xuezhuangxiu_rl;//学装修
        private CardView ihph_zhuangxiudalibao_cv;//装修大礼包
        private ImageView ihph_zhuangxiudalibao_img;//装修大礼包图片
        private CardView ihph_mianfeisheji_cv;//免费设计
        private ImageView ihph_mianfeisheji_img;//免费设计图片
        private CardView ihph_jisubaojia_cv;//急速报价
        private ImageView ihph_jisubaojia_img;//极速报价图片
        private CardView ihph_zhuanyeliangfang_cv;//专业量房
        private ImageView ihph_zhuanyeliangfang_img;//专业量房图片
        private CardView ihph_jianyue_cv;//简约
        private ImageView ihph_jianyue_img;//简约图片
        private CardView ihph_tianyuan_cv;//田园
        private ImageView ihph_tianyuan_img;//田园图片
        private CardView ihph_xiandai_cv;//现代
        private ImageView ihph_xiandai_img;//现代图片
        private CardView ihph_zhongshi_cv;//中式
        private ImageView ihph_zhongshi_img;//中式
        private CardView ihph_meishi_cv;//美式
        private ImageView ihph_meishi_img;//美式图片
        private RelativeLayout ihph_more_design_style_rl;//更多设计风格
        private ImageView ihph_case_bg_img;//看案例背景图片 做模糊处理
        private ViewPager ihph_case_viewpager;//看案例背景图片 滑块
        private LinearLayout ihph_more_case_ll;//更多案例
        private RelativeLayout ihph_adv1_rl;//第一处广告图
        private MZBannerView ihph_adv1_mzbanner;//第一处广告图
        private MagicIndicator ihph_learnzx_indicator;//学装修滑块
        private MZBannerView ihph_adv2_mzbanner;//第二处广告图
        private ViewPager ihph_learnzx_viewpager;//学装修viewpager
        private LinearLayout ihph_more_learnzx_lr;//更多学装修


        public HomePageHeadViewHolder(View itemView) {
            super(itemView);
            ihph_banner_bg_view = itemView.findViewById(R.id.ihph_banner_bg_view);
            ihph_banner_mz_banner_view = itemView.findViewById(R.id.ihph_banner_mz_banner_view);
            ihph_xuansheji_rl = itemView.findViewById(R.id.ihph_xuansheji_rl);
            ihph_kananli_rl = itemView.findViewById(R.id.ihph_kananli_rl);
            ihph_zhaozhuangxiu_rl = itemView.findViewById(R.id.ihph_zhaozhuangxiu_rl);
            ihph_xuezhuangxiu_rl = itemView.findViewById(R.id.ihph_xuezhuangxiu_rl);
            ihph_zhuangxiudalibao_cv = itemView.findViewById(R.id.ihph_zhuangxiudalibao_cv);
            ihph_zhuangxiudalibao_img = itemView.findViewById(R.id.ihph_zhuangxiudalibao_img);
            ihph_mianfeisheji_cv = itemView.findViewById(R.id.ihph_mianfeisheji_cv);
            ihph_mianfeisheji_img = itemView.findViewById(R.id.ihph_mianfeisheji_img);
            ihph_jisubaojia_cv = itemView.findViewById(R.id.ihph_jisubaojia_cv);
            ihph_jisubaojia_img = itemView.findViewById(R.id.ihph_jisubaojia_img);
            ihph_zhuanyeliangfang_cv = itemView.findViewById(R.id.ihph_zhuanyeliangfang_cv);
            ihph_zhuanyeliangfang_img = itemView.findViewById(R.id.ihph_zhuanyeliangfang_img);
            ihph_jianyue_cv = itemView.findViewById(R.id.ihph_jianyue_cv);
            ihph_jianyue_img = itemView.findViewById(R.id.ihph_jianyue_img);
            ihph_tianyuan_cv = itemView.findViewById(R.id.ihph_tianyuan_cv);
            ihph_tianyuan_img = itemView.findViewById(R.id.ihph_tianyuan_img);
            ihph_xiandai_cv = itemView.findViewById(R.id.ihph_xiandai_cv);
            ihph_xiandai_img = itemView.findViewById(R.id.ihph_xiandai_img);
            ihph_zhongshi_cv = itemView.findViewById(R.id.ihph_zhongshi_cv);
            ihph_zhongshi_img = itemView.findViewById(R.id.ihph_zhongshi_img);
            ihph_meishi_cv = itemView.findViewById(R.id.ihph_meishi_cv);
            ihph_meishi_img = itemView.findViewById(R.id.ihph_meishi_img);
            ihph_more_design_style_rl = itemView.findViewById(R.id.ihph_more_design_style_rl);
            ihph_case_bg_img = itemView.findViewById(R.id.ihph_case_bg_img);
            ihph_case_viewpager = itemView.findViewById(R.id.ihph_case_viewpager);
            ihph_adv1_rl = itemView.findViewById(R.id.ihph_adv1_rl);
            ihph_adv1_mzbanner = itemView.findViewById(R.id.ihph_adv1_mzbanner);
            ihph_learnzx_indicator = itemView.findViewById(R.id.ihph_learnzx_indicator);
            ihph_adv2_mzbanner = itemView.findViewById(R.id.ihph_adv2_mzbanner);
            ihph_learnzx_viewpager = itemView.findViewById(R.id.ihph_learnzx_viewpager);
            ihph_more_learnzx_lr = itemView.findViewById(R.id.ihph_more_learnzx_lr);
            ihph_more_case_ll = itemView.findViewById(R.id.ihph_more_case_ll);
        }
    }

    //尾部文章 holder
    class HomePageFootViewHolder extends RecyclerView.ViewHolder {
        private CardView item_hp_foot_cv;//整个布局
        private TextView item_hp_foot_time_tv;//时间
        private TextView item_hp_foot_title_tv;//标题
        private TextView item_hp_foot_dec_tv;//文本描述
        private ImageView item_hp_foot_image_img;//图片

        public HomePageFootViewHolder(View itemView) {
            super(itemView);
            item_hp_foot_cv = itemView.findViewById(R.id.item_hp_foot_cv);
            item_hp_foot_time_tv = itemView.findViewById(R.id.item_hp_foot_time_tv);
            item_hp_foot_title_tv = itemView.findViewById(R.id.item_hp_foot_title_tv);
            item_hp_foot_dec_tv = itemView.findViewById(R.id.item_hp_foot_dec_tv);
            item_hp_foot_image_img = itemView.findViewById(R.id.item_hp_foot_image_img);
        }
    }
}
