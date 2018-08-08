package com.tbs.tbs_mj.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.activity.ArticleWebViewActivity;
import com.tbs.tbs_mj.activity.DImageLookingActivity;
import com.tbs.tbs_mj.activity.DecorationCaseActivity;
import com.tbs.tbs_mj.activity.DecorationCaseDetailActivity;
import com.tbs.tbs_mj.activity.LearnRenovationActivity;
import com.tbs.tbs_mj.activity.NewWebViewActivity;
import com.tbs.tbs_mj.activity.TopicDetailActivity;
import com.tbs.tbs_mj.bean.NewHomeDataItem;
import com.tbs.tbs_mj.customview.BetterRecyclerView;
import com.tbs.tbs_mj.customview.CustomGridView;
import com.tbs.tbs_mj.customview.Marquee;
import com.tbs.tbs_mj.customview.MarqueeView;
import com.tbs.tbs_mj.customview.MyListView;
import com.tbs.tbs_mj.customview.MySwipeRefreshLayout;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.global.MyApplication;
import com.tbs.tbs_mj.global.OKHttpUtil;
import com.tbs.tbs_mj.utils.AppInfoUtil;
import com.tbs.tbs_mj.utils.EndlessRecyclerOnScrollListener;
import com.tbs.tbs_mj.utils.SpUtil;
import com.tbs.tbs_mj.utils.Util;
import com.tbs.tbs_mj.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import com.tbs.tbs_mj.bean._ImageD;
import com.tbs.tbs_mj.web.AcWebActivity;

/**
 * Created by Lie on 2017/04/23.
 */

public class NewHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = NewHomeAdapter.class.getSimpleName();
    private NewHomeDataItem.NewhomeDataBean dataSource;
    private ViewPager newhomeViewPager;
    private Context context;
    private LayoutInflater inflater;

    private static final int[] cheatImageArr = new int[]{R.drawable.cheat1, R.drawable.cheat2, R.drawable.cheat3, R.drawable.cheat4, R.drawable.cheat5, R.drawable.cheat6, R.drawable.cheat7, R.drawable.cheat8, R.drawable.cheat9, R.drawable.cheat10, R.drawable.cheat11, R.drawable.cheat12, R.drawable.cheat13, R.drawable.cheat14, R.drawable.cheat15, R.drawable.cheat16, R.drawable.cheat17, R.drawable.cheat18, R.drawable.cheat19, R.drawable.cheat20};
    private static String[] familyName = new String[]{"赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨", "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏", "陶", "姜", "戚", "谢", "邹", "章", "苏", "潘", "葛", "奚", "范", "彭", "鲁", "韦", "昌", "马", "苗", "凤", "花", "方", "俞", "袁", "柳", "酆", "鲍", "史", "唐", "费", "廉", "岑", "薛", "雷", "贺", "倪", "汤", "滕", "殷", "罗", "毕", "郝", "邬", "安", "常", "乐", "于", "齐", "黄", "萧", "尹", "姚", "祁", "宋", "熊", "舒", "屈", "项", "祝", "董", "梁", "杜", "席", "贾", "江", "郭", "林", "钟", "徐", "邱", "高", "田", "胡", "邓"};
    private static final String[] fares = new String[]{"领取了免费设计", "获得了免费报价", "获得了专业推荐", "领取了装修大礼包"};
    private static String[] citys = new String[]{"北京市", " 天津市", " 石家庄市", " 唐山市", " 秦皇岛市", " 太原市", " 大同市", " 长治市", "呼和浩特市", "包头市", "沈阳市", "大连市", "长春市", "哈尔滨市", "上海市", "南京市", "无锡市", "徐州市", "常州市", "苏州市", "南通市", "扬州市", "杭州市", "宁波市", "温州市", "嘉兴市", "湖州市", "绍兴市", "金华市", "台州市", "合肥市", "福州市", "厦门市", "南昌市", "济南市", "青岛市", "烟台市", "潍坊市", "威海市", "郑州市", "武汉市", "长沙市", "广州市", "深圳市", "珠海市", "佛山市", "东莞市", "中山市", "南宁市", "重庆市", "成都市", "贵阳市", "昆明市", "西安市", "兰州市", "江阴市", "宜兴市", "昆山市", "张家港市", "余姚市", "慈溪市"};


    private final int ADAPTER_ITEM_HEAD = 0;
    private final int ITEM_VIEW_TYPE_ZHUANTI = 9;
    private final int ADAPTER_ITEM_FOOT = 2;

    private View adapterHeadView;
    private View adapterItemViewZhuanti;
    private View adapterFootView;

    private ArrayList<_ImageD> shejiList;


    private boolean zhuantiMore = false;
    private List<NewHomeDataItem.NewhomeDataBean.TopicBean> topicList = new ArrayList<NewHomeDataItem.NewhomeDataBean.TopicBean>();
    private List<NewHomeDataItem.NewhomeDataBean.TopicBean> mTempTopicList = new ArrayList<NewHomeDataItem.NewhomeDataBean.TopicBean>();

    public NewHomeAdapter(Context context, NewHomeDataItem.NewhomeDataBean dataSource, ArrayList<_ImageD> shejiList, List<NewHomeDataItem.NewhomeDataBean.TopicBean> topList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dataSource = dataSource;
        if (!mTempTopicList.isEmpty()) {
            mTempTopicList.clear();
        }
        if (shejiList != null && shejiList.size() > 0) {
            this.shejiList = shejiList;
        }

        if (topList != null && topList.size() > 0) {
            this.topicList.addAll(topList);
        } else {
            this.topicList = dataSource.getTopic();
        }
        mTempTopicList.addAll(this.topicList);

    }

    public void setTopicData(List<NewHomeDataItem.NewhomeDataBean.TopicBean> topList) {
        if (topList != null && topList.size() > 0) {
            this.topicList.clear();
            this.topicList.addAll(mTempTopicList);
            this.topicList.addAll(topList);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ADAPTER_ITEM_HEAD;
        } else if (position + 1 == getItemCount()) {
            return ADAPTER_ITEM_FOOT;
        } else {
            return ITEM_VIEW_TYPE_ZHUANTI;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ADAPTER_ITEM_HEAD) {
            adapterHeadView = inflater.inflate(R.layout.layout_new_home_head, parent, false);
            NewHomeHead newHomeHead = new NewHomeHead(adapterHeadView);
            return newHomeHead;
        } else if (viewType == ITEM_VIEW_TYPE_ZHUANTI) {
            adapterItemViewZhuanti = inflater.inflate(R.layout.layout_zhuanti_item_adapter /*layout_newhome_zhuanti*/, parent, false);
            NewHomeZhuanti newHomeZhuanti = new NewHomeZhuanti(adapterItemViewZhuanti);
            return newHomeZhuanti;
        } else { // ==>>> (viewType == ADAPTER_ITEM_FOOT)
            adapterFootView = inflater.inflate(R.layout.layout_new_home_foot, parent, false);
            NewHomeFoot newHomeFoot = new NewHomeFoot(adapterFootView);
            return newHomeFoot;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof NewHomeHead) {
            NewHomeHead headHolder = (NewHomeHead) holder;
            newhomeViewPager.setFocusable(true);
            newhomeViewPager.setFocusableInTouchMode(true);
            newhomeViewPager.requestFocus();

            initBannerAdapter(newhomeViewPager, headHolder.layoutDot, dataSource.getBanner());

            initCheatText(headHolder.cheatText);
//
            // TODO: 2018/8/2
            //发单01
//            Log.e(TAG, "获取发单的图片地址========10086");
            Log.e(TAG, "获取免费报价url=======" + SpUtil.getNewHomeMianfeibaojiaImgUrl(context));
            if (SpUtil.getNewHomeMianfeibaojiaImgUrl(context) != null
                    && !TextUtils.isEmpty(SpUtil.getNewHomeMianfeibaojiaImgUrl(context))) {
                //设置网络图片
                //如果是gif图要单独处理
                if (SpUtil.getNewHomeMianfeibaojiaImgUrl(context).contains(".gif")) {
                    Glide.with(context)
                            .load(SpUtil.getNewHomeMianfeibaojiaImgUrl(context))
                            .asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                            .centerCrop()
                            .into(((NewHomeHead) holder).fa_dan_01_img);
                } else {
                    Glide.with(context)
                            .load(SpUtil.getNewHomeMianfeibaojiaImgUrl(context))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                            .centerCrop()
                            .into(((NewHomeHead) holder).fa_dan_01_img);
                }
            } else {
                //设置本地的图片
                Glide.with(context)
                        .load(R.drawable.fadan_01)
                        .into(((NewHomeHead) holder).fa_dan_01_img);
            }
            //发单02
            if (SpUtil.getNewHomeMianfeishejiImgUrl(context) != null
                    && !TextUtils.isEmpty(SpUtil.getNewHomeMianfeishejiImgUrl(context))) {
                //设置网络图片
                //如果是gif图要单独处理
                if (SpUtil.getNewHomeMianfeishejiImgUrl(context).contains(".gif")) {
                    Glide.with(context)
                            .load(SpUtil.getNewHomeMianfeishejiImgUrl(context))
                            .asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(((NewHomeHead) holder).fa_dan_02_img);
                } else {
                    Glide.with(context)
                            .load(SpUtil.getNewHomeMianfeishejiImgUrl(context))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(((NewHomeHead) holder).fa_dan_02_img);
                }
            } else {
                //设置本地的图片
                Glide.with(context)
                        .load(R.drawable.fadan_02)
                        .into(((NewHomeHead) holder).fa_dan_02_img);
            }
            //发单03
            if (SpUtil.getNewHomeZhuanyetuijianImgUrl(context) != null
                    && !TextUtils.isEmpty(SpUtil.getNewHomeZhuanyetuijianImgUrl(context))) {
                //设置网络图片
                //如果是gif图要单独处理
                if (SpUtil.getNewHomeZhuanyetuijianImgUrl(context).contains(".gif")) {
                    Glide.with(context)
                            .load(SpUtil.getNewHomeZhuanyetuijianImgUrl(context))
                            .asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(((NewHomeHead) holder).fa_dan_03_img);
                } else {
                    Glide.with(context)
                            .load(SpUtil.getNewHomeZhuanyetuijianImgUrl(context))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(((NewHomeHead) holder).fa_dan_03_img);
                }
            } else {
                //设置本地的图片
                Glide.with(context)
                        .load(R.drawable.fadan_03)
                        .into(((NewHomeHead) holder).fa_dan_03_img);
            }

            //发单04
            if (SpUtil.getNewHomeXianshihaoliImgUrl(context) != null
                    && !TextUtils.isEmpty(SpUtil.getNewHomeXianshihaoliImgUrl(context))) {
                //设置网络图片
                //如果是gif图要单独处理
                if (SpUtil.getNewHomeXianshihaoliImgUrl(context).contains(".gif")) {
                    Glide.with(context)
                            .load(SpUtil.getNewHomeXianshihaoliImgUrl(context))
                            .asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(((NewHomeHead) holder).fa_dan_04_img);
                } else {
                    Glide.with(context)
                            .load(SpUtil.getNewHomeXianshihaoliImgUrl(context))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(((NewHomeHead) holder).fa_dan_04_img);
                }
            } else {
                //设置本地的图片
                Glide.with(context)
                        .load(R.drawable.dalibao)
                        .into(((NewHomeHead) holder).fa_dan_04_img);
            }

            headHolder.relFreeLiangFang.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent webIntent = new Intent(context, NewWebViewActivity.class);
                    webIntent.putExtra("mLoadingUrl", Constant.COMPANY_GIFT);
                    context.startActivity(webIntent);
                }
            });


            headHolder.relXuanSheJi.setFocusable(false);
            headHolder.relXuanSheJi.setFocusableInTouchMode(false);
            headHolder.relXuanSheJi.clearFocus();

            headHolder.relXuanSheJi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent("goto_activity_xiaoguotu");
                    it.putExtra("position", 1);
                    context.sendBroadcast(it);
                }
            });
            headHolder.relKanAnLi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, DecorationCaseActivity.class));
                }
            });
            headHolder.relZhaoZhuangXiu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent("goto_activity_zhuangxiu");
                    it.putExtra("position", 2);
                    context.sendBroadcast(it);
                }
            });
            // 这个跳转改为学装修  跳转到学装列表
            headHolder.relZhuangXiuKetang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent webIntent = new Intent(context, NewWebViewActivity.class);
//                    webIntent.putExtra("mLoadingUrl", Constant.POP_URL + Constant.M_POP_PARAM + Constant.WANGJIANLIN);
//                    context.startActivity(webIntent);
                    Intent gotoLearnActivity = new Intent(context, LearnRenovationActivity.class);
                    context.startActivity(gotoLearnActivity);
                }
            });


            headHolder.relFreeBaojia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fadanClick("1009", Utils.getIp(context));
                    Intent webIntent = new Intent(context, NewWebViewActivity.class);
                    webIntent.putExtra("mLoadingUrl", SpUtil.getzjzxaj01(context));
                    context.startActivity(webIntent);
                }
            });
            headHolder.relFreeSheji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fadanClick("950", Utils.getIp(context));
                    Intent webIntent = new Intent(context, NewWebViewActivity.class);
                    webIntent.putExtra("mLoadingUrl", SpUtil.getzjzxaj02(context));
                    context.startActivity(webIntent);
                }
            });
            headHolder.relProfessalTuiJian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fadanClick("1010", Utils.getIp(context));
                    Intent webIntent = new Intent(context, NewWebViewActivity.class);
                    webIntent.putExtra("mLoadingUrl", SpUtil.getzjzxaj03(context));
                    context.startActivity(webIntent);
                }
            });
            headHolder.rel_cuiying.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fadanClick("1011", Utils.getIp(context));
                    Intent webIntent;
                    if (SpUtil.getzjzxaj04(context).contains(Constant.TEN_YEARS_ACTIVITY)) {
                        webIntent = new Intent(context, AcWebActivity.class);
                    } else {
                        webIntent = new Intent(context, NewWebViewActivity.class);
                    }
                    webIntent.putExtra("mLoadingUrl", SpUtil.getzjzxaj04(context));
                    context.startActivity(webIntent);
                }
            });


            // ==================案例================
            headHolder.relMoreAnli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, DecorationCaseActivity.class));
                }
            });

            NewhomeCasesGridAdapter caseAdapter = null;
            if (caseAdapter == null) {
                caseAdapter = new NewhomeCasesGridAdapter(context, dataSource.getCases());
                headHolder.newhomeGvAnli.setAdapter(caseAdapter);
//                caseAdapter.notifyDataSetChanged();
            }

            headHolder.newhomeGvAnli.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(context, DecorationCaseDetailActivity.class);
                    intent.putExtra("deco_case_id", dataSource.getCases().get(position).getId());
                    context.startActivity(intent);
                }
            });


            // ===============设计==============league
            headHolder.relMoreSheji.setFocusable(false);
            headHolder.relMoreSheji.setFocusableInTouchMode(false);
            headHolder.relMoreSheji.clearFocus();
            headHolder.relMoreSheji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent("goto_activity_xiaoguotu");
                    it.putExtra("position", 1);
                    context.sendBroadcast(it);
                }
            });

            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            headHolder.rvSheji.setFocusable(false);
            headHolder.rvSheji.setFocusableInTouchMode(false);
            headHolder.rvSheji.clearFocus();
            headHolder.rvSheji.setLayoutManager(linearLayoutManager);
            final NewhomeShejiAdapter shejiAdapter = new NewhomeShejiAdapter(context, shejiList);
            headHolder.rvSheji.setAdapter(shejiAdapter);
            shejiAdapter.notifyDataSetChanged();
            shejiAdapter.setOnItemClickListener(new NewhomeShejiAdapter.OnRecyclerViewItemClickListener() {

                @Override
                public void onRecyclerViewItemClick(View view, int position) {
                    // 3.5版本
                    String DImageJson = new Gson().toJson(shejiList);
                    Util.setErrorLog(TAG, DImageJson);
                    SpUtil.setDoubleImageListJson(context, DImageJson);
                    Intent intent = new Intent(context, DImageLookingActivity.class);
                    intent.putExtra("mPosition", position);
                    intent.putExtra("mWhereFrom", "NewhomeActivity");
                    context.startActivity(intent);

                }
            });

            headHolder.rvSheji.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
                @Override
                public void onLoadMore() {
                    shejiAdapter.showLoadMore(true);
                    Intent it = new Intent("goto_activity_xiaoguotu");
                    it.putExtra("position", 1);
                    context.sendBroadcast(it);
                }
            });
            shejiAdapter.notifyDataSetChanged();


            // 课堂
            headHolder.relMoreClass.setFocusable(false);
            headHolder.relMoreClass.setFocusableInTouchMode(false);
            headHolder.relMoreClass.clearFocus();
            // TODO: 2017/12/29  学装修更多按钮  点击进入学装修列表
            headHolder.relMoreClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent webIntent = new Intent(context, NewWebViewActivity.class);
//                    webIntent.putExtra("mLoadingUrl", dataSource.getCourse_list_url());
//                    context.startActivity(webIntent);
                    Intent gotoLearnActivity = new Intent(context, LearnRenovationActivity.class);
                    context.startActivity(gotoLearnActivity);
                }
            });

            LinearLayoutManager classManager = new LinearLayoutManager(context);
            classManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            headHolder.newhomeRecyclerviewClass.setLayoutManager(classManager);
            NewhomeDecorationClassAdapter classAdapter = new NewhomeDecorationClassAdapter(context/*, dataSource.getCourseType()*/);
            headHolder.newhomeRecyclerviewClass.setAdapter(classAdapter);
            classAdapter.notifyDataSetChanged();
            // TODO: 2017/12/29 首页的装修 学装修的选项卡 这个点击某一个选项进入学装修对应的页面
//            Log.e("NewHomeAdapter", "适配器的数据==========" + dataSource.getArticle_type().size());
            classAdapter.setOnItemClickListener(new NewhomeDecorationClassAdapter.OnRecyclerViewItemClickListener() {

                @Override
                public void onRecyclerViewItemClick(View view, int position) {
//                    Intent webIntent = new Intent(context, NewWebViewActivity.class);
//                    webIntent.putExtra("mLoadingUrl", dataSource.getCourse_type().get(position).getJump_url());
//                    context.startActivity(webIntent);
                    Util.HttpArticleClickCount(dataSource.getArticle_type().get(position).getId());
                    Intent intent = new Intent(context, LearnRenovationActivity.class);
                    intent.putExtra("mIndex", dataSource.getArticle_type().get(position).getIndex());
                    context.startActivity(intent);
                }
            });

            KeTangAdapter adapter = new KeTangAdapter(context, dataSource.getCourse());
            headHolder.newhomeRecyclerviewKetang.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            headHolder.newhomeRecyclerviewKetang.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent webIntent = new Intent(context, ArticleWebViewActivity.class);
                    webIntent.putExtra("mLoadingUrl", dataSource.getCourse().get(position).getJump_url() + "?app_type=1");
                    context.startActivity(webIntent);
                }
            });
        }

        if (holder instanceof NewHomeZhuanti) {
            NewHomeZhuanti newHomeZhuanti = (NewHomeZhuanti) holder;
            Glide.with(context).load(topicList.get(position - 1).getCover_url()).placeholder(R.drawable.new_home_loading).error(R.drawable.new_home_loading).into(newHomeZhuanti.iv);
            newHomeZhuanti.tvZhuantiTime.setText(topicList.get(position - 1).getAdd_time());
            newHomeZhuanti.tvZhuantiTitle.setText(topicList.get(position - 1).getTitle());
            newHomeZhuanti.tvZhuantiDesc.setText(topicList.get(position - 1).getDesc());
            newHomeZhuanti.itemView.setTag(position - 1);
            newHomeZhuanti.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(context, TopicDetailActivity.class);
                    it.putExtra("mTopicId", topicList.get(position - 1).getId());
                    context.startActivity(it);
                }
            });
        }


        if (holder instanceof NewHomeFoot) {
            NewHomeFoot footHolder = (NewHomeFoot) holder;
            if (zhuantiMore) {
                footHolder.bar.setVisibility(View.VISIBLE);
                footHolder.textLoadMore.setVisibility(View.VISIBLE);
                footHolder.textLoadMore.setText("加载更多...");
            } else {
                footHolder.bar.setVisibility(View.GONE);
                footHolder.textLoadMore.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return topicList == null ? 0 : topicList.size() + 2;
    }


    public void setLoadMoreFlag(boolean more) {
        this.zhuantiMore = more;
        notifyDataSetChanged();
    }


    /**
     * 头部类
     */
    class NewHomeHead extends RecyclerView.ViewHolder {
        // 上部
        LinearLayout layoutDot;
        MarqueeView cheatText;
        ImageView priceBackgroud;
        RelativeLayout relFreeLiangFang;
        RelativeLayout relXuanSheJi;
        RelativeLayout relKanAnLi;
        RelativeLayout relZhaoZhuangXiu;
        RelativeLayout relZhuangXiuKetang;
        RelativeLayout relFreeBaojia;
        RelativeLayout relFreeSheji;
        RelativeLayout relProfessalTuiJian;
        TextView tvGoGet;
        RelativeLayout rel_cuiying;

        // 案例
        RelativeLayout relMoreAnli;
        CustomGridView newhomeGvAnli;


        // 设计
        RelativeLayout relMoreSheji;
        BetterRecyclerView rvSheji;
        MySwipeRefreshLayout shejiSwipeRefreshLayout;


        // 课堂

        RelativeLayout relMoreClass;
        BetterRecyclerView newhomeRecyclerviewClass;
        MyListView newhomeRecyclerviewKetang;

        //对应的原始发单图片
        ImageView fa_dan_01_img;
        ImageView fa_dan_02_img;
        ImageView fa_dan_03_img;
        ImageView fa_dan_04_img;


        public NewHomeHead(View itemView) {
            super(itemView);
            newhomeViewPager = (ViewPager) itemView.findViewById(R.id.newhome_head_viewpager);
            layoutDot = (LinearLayout) itemView.findViewById(R.id.newHomeDotLayout);
            cheatText = (MarqueeView) itemView.findViewById(R.id.newhome_cheat_text);
            priceBackgroud = (ImageView) itemView.findViewById(R.id.price_img);
            relFreeLiangFang = (RelativeLayout) itemView.findViewById(R.id.relFreeLiangFang);
            relXuanSheJi = (RelativeLayout) itemView.findViewById(R.id.relXuanSheJi);
            relKanAnLi = (RelativeLayout) itemView.findViewById(R.id.relKanAnLi);
            relZhaoZhuangXiu = (RelativeLayout) itemView.findViewById(R.id.relZhaoZhuangXiu);
            relZhuangXiuKetang = (RelativeLayout) itemView.findViewById(R.id.relZhuangXiuKetang);
            relFreeBaojia = (RelativeLayout) itemView.findViewById(R.id.relFreeBaojia);
            relFreeSheji = (RelativeLayout) itemView.findViewById(R.id.relFreeSheji);
            relProfessalTuiJian = (RelativeLayout) itemView.findViewById(R.id.relProfessalTuiJian);
            tvGoGet = (TextView) itemView.findViewById(R.id.tvGoGet);
            rel_cuiying = (RelativeLayout) itemView.findViewById(R.id.rel_cuiying);

            // 案例
            relMoreAnli = (RelativeLayout) itemView.findViewById(R.id.rel_more_anli);
            newhomeGvAnli = (CustomGridView) itemView.findViewById(R.id.newhome_gv_anli);


            // 设计
            relMoreSheji = (RelativeLayout) itemView.findViewById(R.id.rel_more_sheji);
            rvSheji = (BetterRecyclerView) itemView.findViewById(R.id.newhome_recyclerview_sheji);
            shejiSwipeRefreshLayout = (MySwipeRefreshLayout) itemView.findViewById(R.id.swipe_newhome_sheji);


            // 课堂
            relMoreClass = (RelativeLayout) itemView.findViewById(R.id.rel_more_class);
            newhomeRecyclerviewClass = (BetterRecyclerView) itemView.findViewById(R.id.newhome_recyclerview_class);
            newhomeRecyclerviewKetang = (MyListView) itemView.findViewById(R.id.newhome_ketang_recyclerview);

            fa_dan_01_img = itemView.findViewById(R.id.fa_dan_01_img);
            fa_dan_02_img = itemView.findViewById(R.id.fa_dan_02_img);
            fa_dan_03_img = itemView.findViewById(R.id.fa_dan_03_img);
            fa_dan_04_img = itemView.findViewById(R.id.fa_dan_04_img);
        }
    }


    private ArrayList<ImageView> imageViewList = new ArrayList<ImageView>();
    private ArrayList<View> dotViewsList = new ArrayList<View>();
    private ArrayList<String> urlList = new ArrayList<String>();
    private ScheduledExecutorService scheduledExecutorService;

    private void initBannerAdapter(ViewPager bannerPager, LinearLayout dotLayout, List<NewHomeDataItem.NewhomeDataBean.BannerBean> bannerList) {
        dotLayout.removeAllViews();
        urlList.clear();
        if (bannerList != null && bannerList.size() > 0) {
            for (int i = 0; i < bannerList.size(); i++) {
                if (bannerList.get(i).getContent_url().contains("?")) {
                    urlList.add(bannerList.get(i).getContent_url() + "&channel=app&subchannel=android&chcode=" + AppInfoUtil.getChannType(MyApplication.getContext()) + Constant.APP_TYPE + "&tbschcode=" + AppInfoUtil.getNewChannType(MyApplication.getContext()));
                } else {
                    urlList.add(bannerList.get(i).getContent_url() + "?channel=app&subchannel=android&chcode=" + AppInfoUtil.getChannType(MyApplication.getContext()) + Constant.APP_TYPE + "&tbschcode=" + AppInfoUtil.getNewChannType(MyApplication.getContext()));
                }
                ImageView view = new ImageView(context);
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(context).load(bannerList.get(i).getImg_url()).into(view);
                imageViewList.add(view);
                ImageView dotView = new ImageView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = 8;
                params.rightMargin = 8;
                dotLayout.addView(dotView, params);
                dotViewsList.add(dotView);
            }
            bannerPager.setFocusable(true);
            bannerPager.setAdapter(new MyPagerAdapter(imageViewList, urlList));
            bannerPager.setOnPageChangeListener(new MyPageChangeListener(bannerPager));
        }
        startSlide();
    }

    private int currentItem = 0;

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        ViewPager pager;
        boolean isAutoPlay = false;

        public MyPageChangeListener(ViewPager pager) {
            this.pager = pager;
        }

        @Override
        public void onPageScrollStateChanged(int position) {

            switch (position) {
                case 1:
                    isAutoPlay = false;
                    break;
                case 2:
                    isAutoPlay = true;
                    break;
                case 0:
                    if (pager.getCurrentItem() == pager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        pager.setCurrentItem(0);
                    } else if (pager.getCurrentItem() == 0 && !isAutoPlay) {
                        pager.setCurrentItem(pager.getAdapter().getCount() - 1);
                    }
                    break;
            }
        }


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int pos) {
            currentItem = pos;
            for (int i = 0; i < dotViewsList.size(); i++) {
                if (i == pos) {
                    ((View) dotViewsList.get(pos)).setBackgroundResource(R.drawable.selecteds);
                } else {
                    ((View) dotViewsList.get(i)).setBackgroundResource(R.drawable.not_select);
                }
            }
        }
    }

    private void startSlide() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new NewHomeAdapter.ShowTask(), 1, 4, TimeUnit.SECONDS);
    }

    private class ShowTask implements Runnable {
        public ShowTask() {
        }

        @Override
        public void run() {
            currentItem = (currentItem + 1) % imageViewList.size();
            handler.obtainMessage().sendToTarget();
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            newhomeViewPager.setCurrentItem(currentItem);
        }

    };


    private class MyPagerAdapter extends PagerAdapter {
        ArrayList<ImageView> viewList;
        ArrayList<String> urlStrings;

        public MyPagerAdapter(ArrayList<ImageView> viewList, ArrayList<String> urlStrings) {
            this.viewList = viewList;
            this.urlStrings = urlStrings;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            ((ViewPager) container).removeView(viewList.get(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            ((ViewPager) container).addView(viewList.get(position));
            viewList.get(position).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    bannerClick(dataSource.getBanner().get(position).getId());
                    Intent webIntent;
                    // TODO: 2018/7/6 10周年活动跳转
                    if (urlStrings.get(position).contains(Constant.TEN_YEARS_ACTIVITY)) {
                        webIntent = new Intent(context, AcWebActivity.class);
                    } else {
                        webIntent = new Intent(context, NewWebViewActivity.class);
                    }
                    webIntent.putExtra("mLoadingUrl", urlStrings.get(position));
                    context.startActivity(webIntent);
                }
            });
            return viewList.get(position);
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    private void initCheatText(final MarqueeView cheatView) {
        String[] datas = new String[64];
        for (int i = 0; i < datas.length; i++) {
            datas[i] = " " + getSecond() + "秒前," + getRandomText(citys) + getRandomText(familyName) + getLadyOrGentalman() + getBigBagText(fares, i) + "";
        }

        List<Marquee> marquees = new ArrayList<>();
        for (int i = 0; i < cheatImageArr.length; i++) {
            Marquee marquee = new Marquee();
            marquee.setImgId(cheatImageArr[i]);
            marquee.setTitle(datas[i]);
            marquees.add(marquee);
        }
        cheatView.setImage(true);
        cheatView.startWithList(marquees);
    }


    private String getLadyOrGentalman() {
        int num = new Random().nextInt(10) + 1;
        if (num % 2 != 0) {
            return "先生";
        } else {
            return "女士";
        }
    }

    private String getBigBagText(String[] textArr, int position) {
        int len = textArr.length;
        if (position >= len) {
            int pos = position % len;
            return textArr[pos];
        } else {
            return textArr[position];
        }
    }

    private String getRandomText(String[] textArr) {
        Random random = new Random();
        int index = random.nextInt(textArr.length - 1) + 1;
        return textArr[index];
    }

    private int getSecond() {
        Random r = new Random();
        return r.nextInt(59) + 1;
    }

    class NewHomeZhuanti extends RecyclerView.ViewHolder {

        ImageView iv;
        TextView tvZhuantiTime;
        TextView tvZhuantiTitle;
        TextView tvZhuantiDesc;

        public NewHomeZhuanti(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.newhome_zhuanti_item_img);
            tvZhuantiTime = (TextView) itemView.findViewById(R.id.tv_newhome_zhuanti_item_time);
            tvZhuantiTitle = (TextView) itemView.findViewById(R.id.newhome_zhuanti_item_title);
            tvZhuantiDesc = (TextView) itemView.findViewById(R.id.tv_newhome_zhuanti_item_desc);
        }

    }


    private void bannerClick(String id) {
        HashMap<String, Object> click = new HashMap<String, Object>();
        click.put("token", Util.getDateToken());
        click.put("id", id);
        OKHttpUtil.post(Constant.BANNER_CLICK_URL, click, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Util.setErrorLog(TAG, "banner 点击失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Util.setErrorLog(TAG, "banner 点击成功");
            }
        });
    }


    private void fadanClick(String port_code, String ip) {
        HashMap<String, Object> click = new HashMap<String, Object>();
        click.put("token", Util.getDateToken());
        click.put("port_code", port_code);
        click.put("ip", ip);
        OKHttpUtil.post(Constant.BANNER_CLICK_URL, click, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Util.setErrorLog(TAG, "newhome 发单 点击失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Util.setErrorLog(TAG, "newhome 发单 点击成功");
            }
        });
    }

    /**
     * 底部类
     */
    class NewHomeFoot extends RecyclerView.ViewHolder {
        ProgressBar bar;
        TextView textLoadMore;

        public NewHomeFoot(View itemView) {
            super(itemView);
            bar = (ProgressBar) itemView.findViewById(R.id.newhome_progressbar);
            textLoadMore = (TextView) itemView.findViewById(R.id.newhome_loadmore);
        }
    }
}
