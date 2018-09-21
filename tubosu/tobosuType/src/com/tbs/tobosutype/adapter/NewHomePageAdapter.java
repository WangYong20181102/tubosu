package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
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
import android.widget.Toast;

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
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
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
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Lin on 2018/9/5 15:38.
 * 首页的布显示排布
 * 第一项：banner
 * 第二项：选设计 看案例 找装修 学装修
 * 第三项装修大礼包
 * 第四项 免费设计 急速报价 和专业量房
 * 第五项 选设计
 * 第六项 看案例
 * 第七项 第一个广告banner
 * 第八项学装修
 * 第九项 装修专题模块
 */
public class NewHomePageAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private String TAG = "NewHomePageAdapter";
    private _HomePage mHomePage;
    private CaseCardViewAdapter mCaseCardViewAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private ArrayList<Fragment> mFragmentArrayList;
    private MyFragmentPagerStateAdapter myFragmentPagerStateAdapter;
    private Gson mGson;
    private FragmentManager mFragmentManager;
    private int mSelectPosition = 0;

    public NewHomePageAdapter(Context context, _HomePage homePage, FragmentManager fragmentManager) {
        this.mContext = context;
        this.mHomePage = homePage;
        this.mFragmentManager = fragmentManager;
        mGson = new Gson();
    }

    /**
     * Created by Mr.Lin on 2018/9/5 15:38.
     * 首页的布显示排布
     * 第0项：banner
     * 第1项：选设计 看案例 找装修 学装修
     * 第2项装修大礼包
     * 第3项 免费设计 急速报价 和专业量房
     * 第4项 选设计
     * 第5项 看案例
     * 第6项 第一个广告banner
     * 第7项学装修
     * 第8项 装修专题模块
     */

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            //返回banner项
            return 0;
        } else if (position == 1) {
            //返回选设计项
            return 1;
        } else if (position == 2) {
            //返回装修大礼包
            return 2;
        } else if (position == 3) {
            //返回免费设计 急速报价 专业量房
            return 3;
        } else if (position == 4) {
            //返回选设计
            return 4;
        } else if (position == 5) {
            //返回看案例
            return 5;
        } else if (position == 6) {
            //返回第一个广告
            return 6;
        } else if (position == 7) {
            //返回学装修模块
            return 7;
        } else {
            //装修专题
            return 8;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            //返回banner
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_new_hp_banner, parent, false);
            BannerViewHolder bannerViewHolder = new BannerViewHolder(view);
            return bannerViewHolder;
        } else if (viewType == 1) {
            //返回选设计 看案例 找装修
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_new_hp_xuansheji_andother, parent, false);
            XuanShejiBannerViewHolder xuanShejiBannerViewHolder = new XuanShejiBannerViewHolder(view);
            return xuanShejiBannerViewHolder;
        } else if (viewType == 2) {
            //返回装修大礼包
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_new_hp_zhuangxiudalibao, parent, false);
            ZhuangxiuDalibaoViewHolder zhuangxiuDalibaoViewHolder = new ZhuangxiuDalibaoViewHolder(view);
            return zhuangxiuDalibaoViewHolder;
        } else if (viewType == 3) {
            //返回急速报价等模块
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_new_hp_jisubaojia_andother, parent, false);
            JisuBaoJiaViewHolder jisuBaoJiaViewHolder = new JisuBaoJiaViewHolder(view);
            return jisuBaoJiaViewHolder;
        } else if (viewType == 4) {
            //选设计模块 图层
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_new_hp_xuansheji_img, parent, false);
            XuansheJiViewHolder xuansheJiViewHolder = new XuansheJiViewHolder(view);
            return xuansheJiViewHolder;
        } else if (viewType == 5) {
            //返回看案例模块
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_new_hp_kananli, parent, false);
            KanAnliViewHolder kanAnliViewHolder = new KanAnliViewHolder(view);
            return kanAnliViewHolder;
        } else if (viewType == 6) {
            //第一个广告模块
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_new_hp_adv1, parent, false);
            Adv1ViewHolder adv1ViewHolder = new Adv1ViewHolder(view);
            return adv1ViewHolder;
        } else if (viewType == 7) {
            //学装修模块
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_new_hp_xuezhuanxiu, parent, false);
            XueZhuangXiuViewHolder xueZhuangXiuViewHolder = new XueZhuangXiuViewHolder(view);
            return xueZhuangXiuViewHolder;
        } else {
            //专题模块
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_page_topicle, parent, false);
            ZhuanTiViewHolder zhuanTiViewHolder = new ZhuanTiViewHolder(view);
            return zhuanTiViewHolder;
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof BannerViewHolder) {
            //展示轮播图信息
            if (mHomePage.getData().getBanner() != null
                    && !mHomePage.getData().getBanner().isEmpty()
                    && mHomePage.getData().getBanner().get(0).getBgcolor() != null
                    && !TextUtils.isEmpty(mHomePage.getData().getBanner().get(0).getBgcolor())) {
                ((BannerViewHolder) holder).new_ihph_banner_bg_view.setBackgroundColor(Color.parseColor(mHomePage.getData().getBanner().get(0).getBgcolor()));
            }

            //设置点击事件
            ((BannerViewHolder) holder).new_ihph_banner_mz_banner_view.setBannerPageClickListener(bannerPageClickListener);
            //设置页面滑动事件
            ((BannerViewHolder) holder).new_ihph_banner_mz_banner_view.addPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (
                            mHomePage.getData().getBanner() != null
                                    && mHomePage.getData().getBanner().size() >= position + 1
                                    && mHomePage.getData().getBanner().get(position).getBgcolor() != null
                                    && !TextUtils.isEmpty(mHomePage.getData().getBanner().get(position).getBgcolor())) {
                        ((BannerViewHolder) holder).new_ihph_banner_bg_view.setBackgroundColor(Color.parseColor(mHomePage.getData().getBanner().get(position).getBgcolor()));
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            //设置适配器
            ((BannerViewHolder) holder).new_ihph_banner_mz_banner_view.setPages(mHomePage.getData().getBanner(), new MZHolderCreator<MZBannerViewHolder>() {
                @Override
                public MZBannerViewHolder createViewHolder() {
                    return new MZBannerViewHolder();
                }
            });

            if (!mHomePage.getData().getBanner().isEmpty()) {
                if (mHomePage.getData().getBanner().size() > 1) {
                    ((BannerViewHolder) holder).new_ihph_banner_mz_banner_view.setCanLoop(true);
                    ((BannerViewHolder) holder).new_ihph_banner_mz_banner_view.start();
                } else {
                    ((BannerViewHolder) holder).new_ihph_banner_mz_banner_view.setCanLoop(false);
                    ((BannerViewHolder) holder).new_ihph_banner_mz_banner_view.pause();
                }
            }

        } else if (holder instanceof XuanShejiBannerViewHolder) {
            //绑定选设计等的数据
            //学装修
            ((XuanShejiBannerViewHolder) holder).newihph_xuansheji_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent("goto_activity_xiaoguotu");
                    it.putExtra("position", 1);
                    mContext.sendBroadcast(it);
                }
            });
            //看案例
            ((XuanShejiBannerViewHolder) holder).newihph_kananli_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, DecorationCaseActivity.class));

                }
            });
            //找装修
            ((XuanShejiBannerViewHolder) holder).newihph_zhaozhuangxiu_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent("goto_activity_zhuangxiu");
                    it.putExtra("position", 2);
                    mContext.sendBroadcast(it);
                }
            });
            //学装修
            ((XuanShejiBannerViewHolder) holder).newihph_xuezhuangxiu_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent gotoLearnActivity = new Intent(mContext, LearnRenovationActivity.class);
                    mContext.startActivity(gotoLearnActivity);
                }
            });
        } else if (holder instanceof ZhuangxiuDalibaoViewHolder) {
            //点击事件
            ((ZhuangxiuDalibaoViewHolder) holder).newihph_zhuangxiudalibao_cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(mContext, NewWebViewActivity.class);
                    intent1.putExtra("mLoadingUrl", SpUtil.getTbsAj35(mContext));
                    mContext.startActivity(intent1);
                }
            });
            //设置图片
            if (!TextUtils.isEmpty(SpUtil.getNewHomeXianshihaoliImgUrl(mContext))) {
                if (SpUtil.getNewHomeXianshihaoliImgUrl(mContext).contains(".gif")) {
                    Glide.with(mContext)
                            .load(SpUtil.getNewHomeXianshihaoliImgUrl(mContext))
                            .asGif()
                            .into(((ZhuangxiuDalibaoViewHolder) holder)
                                    .newihph_zhuangxiudalibao_img);
                } else {
                    Glide.with(mContext)
                            .load(SpUtil.getNewHomeXianshihaoliImgUrl(mContext))
                            .into(((ZhuangxiuDalibaoViewHolder) holder)
                                    .newihph_zhuangxiudalibao_img);
                }
            }
        } else if (holder instanceof JisuBaoJiaViewHolder) {
            //绑定急速报价deng数据
            //免费设计
            ((JisuBaoJiaViewHolder) holder).ihph_mianfeisheji_cv_new.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(mContext, NewWebViewActivity.class);
                    intent1.putExtra("mLoadingUrl", SpUtil.getTbsAj36(mContext));
                    mContext.startActivity(intent1);
                }
            });
            //急速报价
            ((JisuBaoJiaViewHolder) holder).ihph_jisubaojia_cv_new.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(mContext, NewWebViewActivity.class);
                    intent1.putExtra("mLoadingUrl", SpUtil.getTbsAj37(mContext));
                    mContext.startActivity(intent1);
                }
            });
            //专业量房
            ((JisuBaoJiaViewHolder) holder).ihph_zhuanyeliangfang_cv_new.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(mContext, NewWebViewActivity.class);
                    intent1.putExtra("mLoadingUrl", SpUtil.getTbsAj38(mContext));
                    mContext.startActivity(intent1);
                }
            });
            //设置图片
            if (!TextUtils.isEmpty(SpUtil.getNewHomeMianfeishejiImgUrl(mContext))) {
                //设置免费设计
                if (SpUtil.getNewHomeMianfeishejiImgUrl(mContext).contains(".gif")) {
                    Glide.with(mContext)
                            .load(SpUtil.getNewHomeMianfeishejiImgUrl(mContext))
                            .asGif()
                            .into(((JisuBaoJiaViewHolder) holder)
                                    .ihph_mianfeisheji_img_new);
                } else {
                    Glide.with(mContext)
                            .load(SpUtil.getNewHomeMianfeishejiImgUrl(mContext))
                            .into(((JisuBaoJiaViewHolder) holder)
                                    .ihph_mianfeisheji_img_new);
                }

            }

            if (!TextUtils.isEmpty(SpUtil.getNewHomeJisubaojiaImgUrl(mContext))) {
                //急速报价
                if (SpUtil.getNewHomeJisubaojiaImgUrl(mContext).contains(".gif")) {
                    Glide.with(mContext)
                            .load(SpUtil.getNewHomeJisubaojiaImgUrl(mContext))
                            .asGif()
                            .into(((JisuBaoJiaViewHolder) holder)
                                    .ihph_jisubaojia_img_new);
                } else {
                    Glide.with(mContext)
                            .load(SpUtil.getNewHomeJisubaojiaImgUrl(mContext))
                            .into(((JisuBaoJiaViewHolder) holder)
                                    .ihph_jisubaojia_img_new);
                }

            }
            if (!TextUtils.isEmpty(SpUtil.getNewHomeZhuanyeliangfangImgUrl(mContext))) {
                //专业量房
                if (SpUtil.getNewHomeZhuanyeliangfangImgUrl(mContext).contains(".gif")) {
                    Glide.with(mContext)
                            .load(SpUtil.getNewHomeZhuanyeliangfangImgUrl(mContext))
                            .asGif()
                            .into(((JisuBaoJiaViewHolder) holder)
                                    .ihph_zhuanyeliangfang_img_new);
                } else {
                    Glide.with(mContext)
                            .load(SpUtil.getNewHomeZhuanyeliangfangImgUrl(mContext))
                            .into(((JisuBaoJiaViewHolder) holder)
                                    .ihph_zhuanyeliangfang_img_new);
                }

            }
        } else if (holder instanceof XuansheJiViewHolder) {
            // TODO: 2018/9/4 设置选设计模块
            ((XuansheJiViewHolder) holder).ihph_more_design_style_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // TODO: 2018/9/5 进入效果图模块
                    Intent it = new Intent("goto_activity_xiaoguotu");
                    it.putExtra("position", 1);
                    mContext.sendBroadcast(it);
                }
            });
            for (int i = 0; i < mHomePage.getData().getImpression().size(); i++) {
                if (mHomePage.getData().getImpression().get(i).getType().equals("jianyue")) {
                    //设置简约
                    Glide.with(mContext)
                            .load(mHomePage.getData().getImpression().get(i).getCover_url())
                            .into(((XuansheJiViewHolder) holder).ihph_jianyue_img);

                    //点击事件
                    final int finalI = i;
                    ((XuansheJiViewHolder) holder).ihph_jianyue_cv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String DImageJson = new Gson().toJson(mHomePage.getData().getImpression());
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
                            .into(((XuansheJiViewHolder) holder).ihph_tianyuan_img);

                    //点击事件
                    ((XuansheJiViewHolder) holder).ihph_tianyuan_cv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String DImageJson = new Gson().toJson(mHomePage.getData().getImpression());
                            Util.setErrorLog(TAG, DImageJson);
                            SpUtil.setDoubleImageListJson(mContext, DImageJson);
                            Intent intent = new Intent(mContext, DImageLookingActivity.class);
                            intent.putExtra("mPosition", 1);
                            intent.putExtra("mWhereFrom", "NewhomeActivity");
                            mContext.startActivity(intent);
                        }
                    });
                }
                if (mHomePage.getData().getImpression().get(i).getType().equals("xiandai")) {
                    //设置现代
                    Glide.with(mContext)
                            .load(mHomePage.getData().getImpression().get(i).getCover_url())
                            .into(((XuansheJiViewHolder) holder).ihph_xiandai_img);
                    //点击事件
                    ((XuansheJiViewHolder) holder).ihph_xiandai_cv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String DImageJson = new Gson().toJson(mHomePage.getData().getImpression());
                            Util.setErrorLog(TAG, DImageJson);
                            SpUtil.setDoubleImageListJson(mContext, DImageJson);
                            Intent intent = new Intent(mContext, DImageLookingActivity.class);
                            intent.putExtra("mPosition", 2);
                            intent.putExtra("mWhereFrom", "NewhomeActivity");
                            mContext.startActivity(intent);
                        }
                    });
                }
                if (mHomePage.getData().getImpression().get(i).getType().equals("zhongshi")) {
                    //设置中式
                    Glide.with(mContext)
                            .load(mHomePage.getData().getImpression().get(i).getCover_url())
                            .into(((XuansheJiViewHolder) holder).ihph_zhongshi_img);

                    //点击事件

                    ((XuansheJiViewHolder) holder).ihph_zhongshi_cv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String DImageJson = new Gson().toJson(mHomePage.getData().getImpression());
                            Util.setErrorLog(TAG, DImageJson);
                            SpUtil.setDoubleImageListJson(mContext, DImageJson);
                            Intent intent = new Intent(mContext, DImageLookingActivity.class);
                            intent.putExtra("mPosition", 3);
                            intent.putExtra("mWhereFrom", "NewhomeActivity");
                            mContext.startActivity(intent);
                        }
                    });
                }
                if (mHomePage.getData().getImpression().get(i).getType().equals("meishi")) {
                    //设置美式
                    Glide.with(mContext)
                            .load(mHomePage.getData().getImpression().get(i).getCover_url())
                            .into(((XuansheJiViewHolder) holder).ihph_meishi_img);

                    //点击事件
                    ((XuansheJiViewHolder) holder).ihph_meishi_cv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String DImageJson = new Gson().toJson(mHomePage.getData().getImpression());
                            Util.setErrorLog(TAG, DImageJson);
                            SpUtil.setDoubleImageListJson(mContext, DImageJson);
                            Intent intent = new Intent(mContext, DImageLookingActivity.class);
                            intent.putExtra("mPosition", 4);
                            intent.putExtra("mWhereFrom", "NewhomeActivity");
                            mContext.startActivity(intent);
                        }
                    });
                }
            }
        } else if (holder instanceof KanAnliViewHolder) {
            //绑定看案例的数据
            // TODO: 2018/9/4 设置案例模块
            //设置初始的背景
            if (mHomePage.getData().getCases() != null
                    && !mHomePage.getData().getCases().isEmpty()
                    && mHomePage.getData().getCases().get(0).getCover_url() != null) {
                Glide.with(mContext)
                        .load(mHomePage.getData().getCases().get(0).getCover_url())
                        .transform(new FastBlur(mContext, 100))
                        .into(((KanAnliViewHolder) holder).ihph_case_bg_img);
            }
            mCaseCardViewAdapter = new CaseCardViewAdapter(mContext, mHomePage.getData().getCases());
            mCardShadowTransformer = new ShadowTransformer(((KanAnliViewHolder) holder).ihph_case_viewpager, mCaseCardViewAdapter);
            ((KanAnliViewHolder) holder).ihph_case_viewpager.setAdapter(mCaseCardViewAdapter);
            ((KanAnliViewHolder) holder).ihph_case_viewpager.setPageTransformer(false, mCardShadowTransformer);
            ((KanAnliViewHolder) holder).ihph_case_viewpager.setOffscreenPageLimit(3);
            ((KanAnliViewHolder) holder).ihph_case_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if (position == mHomePage.getData().getCases().size() - 1 && positionOffset == 0) {
                        ((KanAnliViewHolder) holder).ihph_more_case_ll.setVisibility(View.VISIBLE);
                    } else {
                        ((KanAnliViewHolder) holder).ihph_more_case_ll.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onPageSelected(int position) {
                    //滑动时模糊处理
                    Glide.with(mContext)
                            .load(mHomePage.getData().getCases().get(position).getCover_url())
                            .transform(new FastBlur(mContext, 100))
                            .into(((KanAnliViewHolder) holder).ihph_case_bg_img);

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
            ((KanAnliViewHolder) holder).ihph_more_case_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, DecorationCaseActivity.class));
                }
            });
        } else if (holder instanceof Adv1ViewHolder) {
            //第一处广告处
            if (mHomePage.getData().getIndex_advert_1() == null
                    || mHomePage.getData().getIndex_advert_1().isEmpty()) {
                //没有时不显示
                ((Adv1ViewHolder) holder).ihph_adv1_rl.setVisibility(View.GONE);
                ((Adv1ViewHolder) holder).ihph_adv1_mzbanner.setVisibility(View.GONE);
                ((Adv1ViewHolder) holder).ihph_adv1_rl_2.setVisibility(View.GONE);
            } else {
                ((Adv1ViewHolder) holder).ihph_adv1_rl.setVisibility(View.VISIBLE);
                ((Adv1ViewHolder) holder).ihph_adv1_mzbanner.setVisibility(View.VISIBLE);
                ((Adv1ViewHolder) holder).ihph_adv1_rl_2.setVisibility(View.VISIBLE);
                //设置点击事件
                ((Adv1ViewHolder) holder).ihph_adv1_mzbanner.setBannerPageClickListener(Adv1bannerPageClickListener);
                //设置适配器
                ((Adv1ViewHolder) holder).ihph_adv1_mzbanner.setPages(mHomePage.getData().getIndex_advert_1(), new MZHolderCreator<ADV1MZBannerViewHolder>() {
                    @Override
                    public ADV1MZBannerViewHolder createViewHolder() {
                        return new ADV1MZBannerViewHolder();
                    }
                });
                ((Adv1ViewHolder) holder).ihph_adv1_mzbanner.setIndicatorVisible(false);
                if (mHomePage.getData().getIndex_advert_1().size() > 1) {
                    //大于1项的时候要进行轮播
                    ((Adv1ViewHolder) holder).ihph_adv1_mzbanner.setCanLoop(true);
                    ((Adv1ViewHolder) holder).ihph_adv1_mzbanner.start();
                } else {
                    ((Adv1ViewHolder) holder).ihph_adv1_mzbanner.setCanLoop(false);
                    ((Adv1ViewHolder) holder).ihph_adv1_mzbanner.pause();
                }


            }
        } else if (holder instanceof XueZhuangXiuViewHolder) {
            //设置学装修数据
            // TODO: 2018/9/5 学装修的列表页
            //将数据注入

            mFragmentArrayList = new ArrayList<>();
            for (int i = 0; i < mHomePage.getData().getArticle().size(); i++) {
                String homepageArticleJson = mGson.toJson(mHomePage.getData().getArticle().get(i));
                mFragmentArrayList.add(HomePageArticleFragment.newInstance(homepageArticleJson));
            }
            myFragmentPagerStateAdapter = new MyFragmentPagerStateAdapter(mFragmentManager, mFragmentArrayList);
            ((XueZhuangXiuViewHolder) holder).ihph_learnzx_viewpager.setAdapter(myFragmentPagerStateAdapter);
            ((XueZhuangXiuViewHolder) holder).ihph_learnzx_viewpager.setOffscreenPageLimit(mHomePage.getData().getArticle().size());
            myFragmentPagerStateAdapter.notifyDataSetChanged();
            ((XueZhuangXiuViewHolder) holder).ihph_learnzx_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mSelectPosition = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            //顶部的滑块
            ((XueZhuangXiuViewHolder) holder).ihph_learnzx_indicator.setBackgroundColor(Color.WHITE);
            CommonNavigator commonNavigator = new CommonNavigator(mContext);
            commonNavigator.setAdapter(new CommonNavigatorAdapter() {
                @Override
                public int getCount() {
                    return mHomePage.getData().getArticle_type() == null ? 0 : mHomePage.getData().getArticle_type().size();
                }

                @Override
                public IPagerTitleView getTitleView(Context context, final int i) {
                    SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                    simplePagerTitleView.setText("" + mHomePage.getData().getArticle_type().get(i).getTitle());
                    simplePagerTitleView.setTextSize(13);
                    simplePagerTitleView.setNormalColor(Color.parseColor("#999999"));
                    simplePagerTitleView.setSelectedColor(Color.parseColor("#ff882e"));
                    simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((XueZhuangXiuViewHolder) holder).ihph_learnzx_viewpager.setCurrentItem(i);
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
            ((XueZhuangXiuViewHolder) holder).ihph_learnzx_indicator.setNavigator(commonNavigator);
            ViewPagerHelper.bind(((XueZhuangXiuViewHolder) holder).ihph_learnzx_indicator,
                    ((XueZhuangXiuViewHolder) holder).ihph_learnzx_viewpager);

            //中间的广告banner层
            if (mHomePage.getData().getIndex_advert_2() == null
                    || mHomePage.getData().getIndex_advert_2().isEmpty()) {
                //隐藏该层
                ((XueZhuangXiuViewHolder) holder).ihph_adv2_mzbanner.setVisibility(View.GONE);
            } else {
                //显示该层
                ((XueZhuangXiuViewHolder) holder).ihph_adv2_mzbanner.setVisibility(View.VISIBLE);
                ((XueZhuangXiuViewHolder) holder).ihph_adv2_mzbanner.setBannerPageClickListener(Adv2bannerPageClickListener);
                //设置适配器
                ((XueZhuangXiuViewHolder) holder).ihph_adv2_mzbanner.setPages(mHomePage.getData().getIndex_advert_2(), new MZHolderCreator<ADV2MZBannerViewHolder>() {
                    @Override
                    public ADV2MZBannerViewHolder createViewHolder() {
                        return new ADV2MZBannerViewHolder();
                    }
                });
                ((XueZhuangXiuViewHolder) holder).ihph_adv2_mzbanner.setIndicatorVisible(false);

                if (mHomePage.getData().getIndex_advert_2().size() > 1) {
                    ((XueZhuangXiuViewHolder) holder).ihph_adv2_mzbanner.setCanLoop(true);
                    ((XueZhuangXiuViewHolder) holder).ihph_adv2_mzbanner.start();
                } else {
                    ((XueZhuangXiuViewHolder) holder).ihph_adv2_mzbanner.setCanLoop(false);
                    ((XueZhuangXiuViewHolder) holder).ihph_adv2_mzbanner.pause();
                }
            }
            //更多学装修
            ((XueZhuangXiuViewHolder) holder).ihph_more_learnzx_lr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, LearnRenovationActivity.class);
//                    intent.putExtra("mIndex", dataSource.getArticle_type().get(position).getIndex());
                    intent.putExtra("mIndex", mHomePage.getData().getArticle_type().get(mSelectPosition).getIndex());
                    mContext.startActivity(intent);
                }
            });
        } else if (holder instanceof ZhuanTiViewHolder) {
            //绑定专题数据
            ((ZhuanTiViewHolder) holder).item_hp_foot_time_tv.setText("" + mHomePage.getData().getTopic().get(position - 8).getAdd_time());
            ((ZhuanTiViewHolder) holder).item_hp_foot_title_tv.setText("" + mHomePage.getData().getTopic().get(position - 8).getTitle());
            if (mHomePage.getData().getTopic().get(position - 8).getDesc() != null && mHomePage.getData().getTopic().get(position - 8).getDesc().length() > 35) {
                String dec = mHomePage.getData().getTopic().get(position - 8).getDesc().substring(0, 34);
                ((ZhuanTiViewHolder) holder).item_hp_foot_dec_tv.setText("" + dec + "...");
            } else {
                ((ZhuanTiViewHolder) holder).item_hp_foot_dec_tv.setText("" + mHomePage.getData().getTopic().get(position - 8).getDesc());
            }

            GlideUtils.glideLoader(mContext, mHomePage.getData().getTopic().get(position - 8).getCover_url(), ((ZhuanTiViewHolder) holder).item_hp_foot_image_img);
            //点击事件
            ((ZhuanTiViewHolder) holder).item_hp_foot_cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击进入
                    Intent it = new Intent(mContext, TopicDetailActivity.class);
                    it.putExtra("mTopicId", mHomePage.getData().getTopic().get(position - 8).getId());
                    mContext.startActivity(it);
                }
            });
        }
    }


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
            httpClick(mHomePage.getData().getIndex_advert_2().get(i).getId());
            //点击进入
            Intent webIntent = new Intent(mContext, NewWebViewActivity.class);
            webIntent.putExtra("mLoadingUrl", mHomePage.getData().getIndex_advert_2().get(i).getJump_url());
            mContext.startActivity(webIntent);
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
            httpClick(mHomePage.getData().getIndex_advert_1().get(i).getId());
            //点击进入
            Intent webIntent = new Intent(mContext, NewWebViewActivity.class);
            webIntent.putExtra("mLoadingUrl", mHomePage.getData().getIndex_advert_1().get(i).getJump_url());
            mContext.startActivity(webIntent);
        }
    };

    //todo  banner轮播图的适配器
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
            Log.e(TAG, "点击事件");
            setClickRequest(mHomePage.getData().getBanner().get(i).getId());
            Intent webIntent = new Intent(mContext, NewWebViewActivity.class);
            webIntent.putExtra("mLoadingUrl", mHomePage.getData().getBanner().get(i).getContent_url());
            mContext.startActivity(webIntent);
        }
    };


    @Override
    public int getItemCount() {
        return mHomePage.getData().getTopic() != null ? mHomePage.getData().getTopic().size() + 7 : 7;
    }

    private void setClickRequest(String id) {
        if (Util.isNetAvailable(mContext)) {
            HashMap<String, Object> para = new HashMap<String, Object>();
            para.put("token", Util.getDateToken());
            para.put("id", id);
            OKHttpUtil.post(Constant.CLICKURL_BANNER, para, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                }
            });
        }
    }

    //首页banner的viewHolder
    private class BannerViewHolder extends RecyclerView.ViewHolder {
        private View new_ihph_banner_bg_view;//轮播图背景
        private MZBannerView new_ihph_banner_mz_banner_view;//轮播图

        public BannerViewHolder(View itemView) {
            super(itemView);
            new_ihph_banner_bg_view = itemView.findViewById(R.id.new_ihph_banner_bg_view);
            new_ihph_banner_mz_banner_view = itemView.findViewById(R.id.new_ihph_banner_mz_banner_view);
        }
    }

    //选设计和其他的选项
    private class XuanShejiBannerViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout newihph_xuansheji_rl;//选设计
        private RelativeLayout newihph_kananli_rl;//看案例
        private RelativeLayout newihph_zhaozhuangxiu_rl;//找装修
        private RelativeLayout newihph_xuezhuangxiu_rl;//学装修

        public XuanShejiBannerViewHolder(View itemView) {
            super(itemView);
            newihph_xuansheji_rl = itemView.findViewById(R.id.newihph_xuansheji_rl);
            newihph_kananli_rl = itemView.findViewById(R.id.newihph_kananli_rl);
            newihph_zhaozhuangxiu_rl = itemView.findViewById(R.id.newihph_zhaozhuangxiu_rl);
            newihph_xuezhuangxiu_rl = itemView.findViewById(R.id.newihph_xuezhuangxiu_rl);
        }
    }

    //装修大礼包的
    private class ZhuangxiuDalibaoViewHolder extends RecyclerView.ViewHolder {
        private CardView newihph_zhuangxiudalibao_cv;//装修大礼包
        private ImageView newihph_zhuangxiudalibao_img;//装修大礼包图片

        public ZhuangxiuDalibaoViewHolder(View itemView) {
            super(itemView);
            newihph_zhuangxiudalibao_cv = itemView.findViewById(R.id.newihph_zhuangxiudalibao_cv);
            newihph_zhuangxiudalibao_img = itemView.findViewById(R.id.newihph_zhuangxiudalibao_img);
        }
    }

    //极速报价等
    private class JisuBaoJiaViewHolder extends RecyclerView.ViewHolder {
        private CardView ihph_mianfeisheji_cv_new;//免费设计
        private ImageView ihph_mianfeisheji_img_new;//免费设计图片
        private CardView ihph_jisubaojia_cv_new;//急速报价
        private ImageView ihph_jisubaojia_img_new;//极速报价图片
        private CardView ihph_zhuanyeliangfang_cv_new;//专业量房
        private ImageView ihph_zhuanyeliangfang_img_new;//专业量房图片

        public JisuBaoJiaViewHolder(View itemView) {
            super(itemView);
            ihph_mianfeisheji_cv_new = itemView.findViewById(R.id.ihph_mianfeisheji_cv_new);
            ihph_mianfeisheji_img_new = itemView.findViewById(R.id.ihph_mianfeisheji_img_new);
            ihph_jisubaojia_cv_new = itemView.findViewById(R.id.ihph_jisubaojia_cv_new);
            ihph_jisubaojia_img_new = itemView.findViewById(R.id.ihph_jisubaojia_img_new);
            ihph_zhuanyeliangfang_cv_new = itemView.findViewById(R.id.ihph_zhuanyeliangfang_cv_new);
            ihph_zhuanyeliangfang_img_new = itemView.findViewById(R.id.ihph_zhuanyeliangfang_img_new);
        }
    }

    //选设计
    private class XuansheJiViewHolder extends RecyclerView.ViewHolder {
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

        public XuansheJiViewHolder(View itemView) {
            super(itemView);
            ihph_jianyue_cv = itemView.findViewById(R.id.ihph_jianyue_cv_new);
            ihph_jianyue_img = itemView.findViewById(R.id.ihph_jianyue_img_new);
            ihph_tianyuan_cv = itemView.findViewById(R.id.ihph_tianyuan_cv_new);
            ihph_tianyuan_img = itemView.findViewById(R.id.ihph_tianyuan_img_new);
            ihph_xiandai_cv = itemView.findViewById(R.id.ihph_xiandai_cv_new);
            ihph_xiandai_img = itemView.findViewById(R.id.ihph_xiandai_img_new);
            ihph_zhongshi_cv = itemView.findViewById(R.id.ihph_zhongshi_cv_new);
            ihph_zhongshi_img = itemView.findViewById(R.id.ihph_zhongshi_img_new);
            ihph_meishi_cv = itemView.findViewById(R.id.ihph_meishi_cv_new);
            ihph_meishi_img = itemView.findViewById(R.id.ihph_meishi_img_new);
            ihph_more_design_style_rl = itemView.findViewById(R.id.ihph_more_design_style_rl_new);

        }
    }

    //看案例
    private class KanAnliViewHolder extends RecyclerView.ViewHolder {
        private ImageView ihph_case_bg_img;//看案例背景图片 做模糊处理
        private ViewPager ihph_case_viewpager;//看案例背景图片 滑块
        private LinearLayout ihph_more_case_ll;//更多案例

        public KanAnliViewHolder(View itemView) {
            super(itemView);
            ihph_more_case_ll = itemView.findViewById(R.id.ihph_more_case_ll_new);
            ihph_case_bg_img = itemView.findViewById(R.id.ihph_case_bg_img_new);
            ihph_case_viewpager = itemView.findViewById(R.id.ihph_case_viewpager_new);
        }
    }

    //第一处广告
    private class Adv1ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout ihph_adv1_rl;//第一处广告图
        private RelativeLayout ihph_adv1_rl_2;//第一处广告图
        private MZBannerView ihph_adv1_mzbanner;//第一处广告图

        public Adv1ViewHolder(View itemView) {
            super(itemView);
            ihph_adv1_rl = itemView.findViewById(R.id.ihph_adv1_rl_new);
            ihph_adv1_rl_2 = itemView.findViewById(R.id.ihph_adv1_rl_2_new);
            ihph_adv1_mzbanner = itemView.findViewById(R.id.ihph_adv1_mzbanner_new);
        }
    }

    //选装修模块
    private class XueZhuangXiuViewHolder extends RecyclerView.ViewHolder {
        private MagicIndicator ihph_learnzx_indicator;//学装修滑块
        private MZBannerView ihph_adv2_mzbanner;//第二处广告图
        private ViewPager ihph_learnzx_viewpager;//学装修viewpager
        private LinearLayout ihph_more_learnzx_lr;//更多学装修

        public XueZhuangXiuViewHolder(View itemView) {
            super(itemView);
            ihph_learnzx_indicator = itemView.findViewById(R.id.ihph_learnzx_indicator_new);
            ihph_adv2_mzbanner = itemView.findViewById(R.id.ihph_adv2_mzbanner_new);
            ihph_learnzx_viewpager = itemView.findViewById(R.id.ihph_learnzx_viewpager_new);
            ihph_more_learnzx_lr = itemView.findViewById(R.id.ihph_more_learnzx_lr_new);
        }
    }

    //专题模块
    private class ZhuanTiViewHolder extends RecyclerView.ViewHolder {
        private CardView item_hp_foot_cv;//整个布局
        private TextView item_hp_foot_time_tv;//时间
        private TextView item_hp_foot_title_tv;//标题
        private TextView item_hp_foot_dec_tv;//文本描述
        private ImageView item_hp_foot_image_img;//图片

        public ZhuanTiViewHolder(View itemView) {
            super(itemView);
            item_hp_foot_cv = itemView.findViewById(R.id.item_hp_foot_cv);
            item_hp_foot_time_tv = itemView.findViewById(R.id.item_hp_foot_time_tv);
            item_hp_foot_title_tv = itemView.findViewById(R.id.item_hp_foot_title_tv);
            item_hp_foot_dec_tv = itemView.findViewById(R.id.item_hp_foot_dec_tv);
            item_hp_foot_image_img = itemView.findViewById(R.id.item_hp_foot_image_img);
        }
    }

    //广告点击统计
    private void httpClick(String advId) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("id", advId);
        OKHttpUtil.post(Constant.ADV_CLICK_COUNT, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }
}
