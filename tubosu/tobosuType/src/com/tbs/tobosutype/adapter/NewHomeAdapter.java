package com.tbs.tobosutype.adapter;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.DecorationCaseActivity;
import com.tbs.tobosutype.activity.DecorationCaseDetailActivity;
import com.tbs.tobosutype.activity.ImageDetailNewActivity;
import com.tbs.tobosutype.activity.NewWebViewActivity;
import com.tbs.tobosutype.activity.TopicDetailActivity;
import com.tbs.tobosutype.bean.NewHomeDataItem;
import com.tbs.tobosutype.customview.BetterRecyclerView;
import com.tbs.tobosutype.customview.CustomGridView;
import com.tbs.tobosutype.customview.Marquee;
import com.tbs.tobosutype.customview.MarqueeView;
import com.tbs.tobosutype.customview.MyItemDecoration;
import com.tbs.tobosutype.customview.MyListView;
import com.tbs.tobosutype.customview.MySwipeRefreshLayout;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.DensityUtil;
import com.tbs.tobosutype.utils.EndlessRecyclerOnScrollListener;
import com.tbs.tobosutype.utils.FullyLinearLayoutManager;
import com.tbs.tobosutype.utils.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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


/**
 * Created by Lie on 2017/10/23.
 */

public class NewHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = NewHomeAdapter.class.getSimpleName();
    private NewHomeDataItem.NewhomeDataBean dataSource;
    private ViewPager newhomeViewPager;
    private Context context;
    private LayoutInflater inflater;

    private static final int[] cheatImageArr = new int[]{R.drawable.cheat1,R.drawable.cheat2,R.drawable.cheat3,R.drawable.cheat4,R.drawable.cheat5,R.drawable.cheat6,R.drawable.cheat7,R.drawable.cheat8,R.drawable.cheat9,R.drawable.cheat10,R.drawable.cheat11,R.drawable.cheat12,R.drawable.cheat13,R.drawable.cheat14,R.drawable.cheat15,R.drawable.cheat16,R.drawable.cheat17,R.drawable.cheat18,R.drawable.cheat19,R.drawable.cheat20};
    private static String[] familyName = new String[] {"赵","钱","孙","李","周","吴","郑","王","冯","陈","褚","卫","蒋","沈","韩","杨","何","吕","施","张","孔","曹","严","华","金","魏","陶","姜","戚","谢","邹","章","苏","潘","葛","奚","范","彭","鲁","韦","昌","马","苗","凤","花","方","俞","袁","柳","酆","鲍","史","唐","费","廉","岑","薛","雷","贺","倪","汤","滕","殷","罗","毕","郝","邬","安","常","乐","于","齐","黄","萧","尹","姚","祁","宋","熊","舒","屈","项","祝","董","梁","杜","席","贾","江","郭","林","钟","徐","邱","高","田","胡","邓"};
    private static final String[] fares = new String[]{"领取了免费设计","获得了免费报价","获得了专业推荐","领取了装修大礼包"};
    private static String[] citys = new String[]{"北京市"," 天津市"," 石家庄市"," 唐山市"," 秦皇岛市"," 太原市"," 大同市"," 长治市","呼和浩特市", "包头市", "沈阳市", "大连市", "长春市", "哈尔滨市", "上海市", "南京市", "无锡市", "徐州市", "常州市", "苏州市", "南通市", "扬州市", "杭州市", "宁波市", "温州市", "嘉兴市", "湖州市", "绍兴市", "金华市", "台州市", "合肥市", "福州市", "厦门市", "南昌市", "济南市", "青岛市", "烟台市", "潍坊市", "威海市", "郑州市", "武汉市", "长沙市", "广州市", "深圳市", "珠海市", "佛山市", "东莞市", "中山市","南宁市", "重庆市", "成都市", "贵阳市", "昆明市", "西安市", "兰州市", "江阴市", "宜兴市", "昆山市", "张家港市", "余姚市", "慈溪市"};


    private final int ADAPTER_ITEM_HEAD = 0;
//    private final int ADAPTER_ITEM_ITEM = 1;
    private final int ADAPTER_ITEM_FOOT = 2;

    private View adapterHeadView;
    private View adapterItemViewAnli;
    private View adapterItemViewSheji;
    private View adapterItemViewKetang;
    private View adapterItemViewZhuanti;
    private View adapterFootView;

    private final int ITEM_VIEW_TYPE_ANLI = 6;
    private final int ITEM_VIEW_TYPE_SHEJI = 7;
    private final int ITEM_VIEW_TYPE_KETANG = 8;
    private final int ITEM_VIEW_TYPE_ZHUANTI = 9;

    private boolean zhuantiMore = false;
    private List<NewHomeDataItem.NewhomeDataBean.TopicBean> topicList = new ArrayList<NewHomeDataItem.NewhomeDataBean.TopicBean>();

    public NewHomeAdapter(Context context, NewHomeDataItem.NewhomeDataBean dataSource){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dataSource = dataSource;
    }


    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return ADAPTER_ITEM_HEAD;
        }else if(position == 1){
            return ITEM_VIEW_TYPE_ANLI;
        }else if(position == 2){
            return ITEM_VIEW_TYPE_SHEJI;
        }else if(position == 3){
            return ITEM_VIEW_TYPE_KETANG;
        }else if(position == 4){
            return ITEM_VIEW_TYPE_ZHUANTI;
        }else {
            return ADAPTER_ITEM_FOOT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ADAPTER_ITEM_HEAD){
            adapterHeadView = inflater.inflate(R.layout.layout_new_home_head, parent, false);
            NewHomeHead newHomeHead = new NewHomeHead(adapterHeadView);
            return  newHomeHead;
        }

        if(viewType == ITEM_VIEW_TYPE_ANLI){
            adapterItemViewAnli = inflater.inflate(R.layout.layout_newhome_anli, parent, false);
            NewHomeAnli newHomeAnli = new NewHomeAnli(adapterItemViewAnli);
            return newHomeAnli;
        }

        if(viewType == ITEM_VIEW_TYPE_SHEJI){
            adapterItemViewSheji = inflater.inflate(R.layout.layout_newhome_sheji, parent, false);
            NewHomeSheji newHomeSheji = new NewHomeSheji(adapterItemViewSheji);
            return newHomeSheji;
        }

        if(viewType == ITEM_VIEW_TYPE_KETANG){
            adapterItemViewKetang = inflater.inflate(R.layout.layout_newhome_ketang, parent, false);
            NewHomeKetang newHomeKetang = new NewHomeKetang(adapterItemViewKetang);
            return newHomeKetang;
        }

        if(viewType == ITEM_VIEW_TYPE_ZHUANTI){
            adapterItemViewZhuanti = inflater.inflate(R.layout.layout_newhome_zhuanti, parent, false);
            NewHomeZhuanti newHomeZhuanti = new NewHomeZhuanti(adapterItemViewZhuanti);
            return newHomeZhuanti;
        }

        if(viewType == ADAPTER_ITEM_FOOT){
            adapterFootView = inflater.inflate(R.layout.layout_new_home_foot, parent, false);
            NewHomeFoot newHomeFoot = new NewHomeFoot(adapterFootView);
            return newHomeFoot;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof NewHomeHead){
            NewHomeHead headHolder = (NewHomeHead) holder;
            List<NewHomeDataItem.NewhomeDataBean.BannerBean> bannDataList = dataSource.getBanner();
            initBannerAdapter(newhomeViewPager, headHolder.layoutDot, bannDataList);
            initCheatText(headHolder.cheatText);
            Glide.with(context).load(R.drawable.price_gif).asGif().into(headHolder.priceBackgroud);
            headHolder.relFreeLiangFang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent webIntent = new Intent(context, NewWebViewActivity.class);
                    webIntent.putExtra("mLoadingUrl", Constant.DALIBAO);
                    context.startActivity(webIntent);
                }
            });
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

            headHolder.relZhuangXiuKetang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent webIntent = new Intent(context, NewWebViewActivity.class);
                    webIntent.putExtra("mLoadingUrl", Constant.POP_URL + Constant.M_POP_PARAM + Constant.WANGJIANLIN);
                    context.startActivity(webIntent);
                }
            });


            headHolder.relFreeBaojia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent webIntent = new Intent(context, NewWebViewActivity.class);
                    webIntent.putExtra("mLoadingUrl", Constant.MIANFEI_BAOJIA);
                    context.startActivity(webIntent);
                }
            });
            headHolder.relFreeSheji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.setToast(context, "等通知");
                }
            });
            headHolder.relProfessalTuiJian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent webIntent = new Intent(context, NewWebViewActivity.class);
                    webIntent.putExtra("mLoadingUrl", Constant.TUIJIAN);
                    context.startActivity(webIntent);
                }
            });
            headHolder.tvGoGet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent webIntent = new Intent(context, NewWebViewActivity.class);
                    webIntent.putExtra("mLoadingUrl", Constant.DALIBAO);
                    context.startActivity(webIntent);
                }
            });
        }

        if(holder instanceof NewHomeAnli){
            NewHomeAnli newHomeAnli = (NewHomeAnli) holder;
            newHomeAnli.relMoreAnli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, DecorationCaseActivity.class));
                }
            });

            NewhomeCasesGridAdapter caseAdapter = new NewhomeCasesGridAdapter(context, dataSource.getCases());
            newHomeAnli.newhomeGvAnli.setAdapter(caseAdapter);
            caseAdapter.notifyDataSetChanged();
            newHomeAnli.newhomeGvAnli.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(context, DecorationCaseDetailActivity.class);
                    intent.putExtra("deco_case_id", dataSource.getCases().get(position).getId());
                    context.startActivity(intent);
                }
            });

//            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
//            newHomeAnli.newhomeGvAnli.setLayoutManager(gridLayoutManager);
//
//            NewhomeAnliAdapter anliAdapter = new NewhomeAnliAdapter(context, dataSource.getCases());
//            newHomeAnli.newhomeGvAnli.setAdapter(anliAdapter);
//            anliAdapter.notifyDataSetChanged();
//            anliAdapter.setmOnCaseClickListener(new NewhomeAnliAdapter.OnCaseClickListener() {
//                @Override
//                public void onCaseClickListener(View parent, final int ps) {
//                    Intent intent = new Intent(context, DecorationCaseDetailActivity.class);
//                    intent.putExtra("deco_case_id", dataSource.getCases().get(ps).getId());
//                    context.startActivity(intent);
//                }
//            });
        }

        if(holder instanceof NewHomeSheji){
            NewHomeSheji newHomeSheji = (NewHomeSheji) holder;
            newHomeSheji.relMoreSheji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent("goto_activity_xiaoguotu");
                    it.putExtra("position", 1);
                    context.sendBroadcast(it);
                }
            });

            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            newHomeSheji.rvSheji.setLayoutManager(linearLayoutManager);
//            newHomeSheji.shejiSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
//            newHomeSheji.shejiSwipeRefreshLayout.setColorSchemeResources(R.color.color_white);

            final NewhomeShejiAdapter shejiAdapter = new NewhomeShejiAdapter(context, dataSource.getImpression());
            newHomeSheji.rvSheji.setAdapter(shejiAdapter);
            shejiAdapter.notifyDataSetChanged();
            shejiAdapter.setOnItemClickListener(new NewhomeShejiAdapter.OnRecyclerViewItemClickListener() {

                @Override
                public void onRecyclerViewItemClick(View view, int position) {

                    Intent intent = new Intent(context, ImageDetailNewActivity.class);
                    intent.putExtra("id", dataSource.getImpression().get(position).getId());
                    context.startActivity(intent);
                }
            });
            newHomeSheji.rvSheji.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    int lastVisiableItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (newState == RecyclerView.SCROLL_STATE_IDLE  && lastVisiableItem <linearLayoutManager.getItemCount()) {
                        shejiAdapter.notifyDataSetChanged();
                    }
                }
            });
            newHomeSheji.rvSheji.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
                @Override
                public void onLoadMore() {
                    shejiAdapter.showLoadMore(false);
                    shejiAdapter.showLoadMore(true);
                    Intent it = new Intent("goto_activity_xiaoguotu");
                    it.putExtra("position", 1);
                    context.sendBroadcast(it);
                }
            });
            shejiAdapter.notifyDataSetChanged();

        }

        if(holder instanceof NewHomeKetang){
            NewHomeKetang newHomeKetang = (NewHomeKetang) holder;
            newHomeKetang.relMoreClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent webIntent = new Intent(context, NewWebViewActivity.class);
                    webIntent.putExtra("mLoadingUrl", dataSource.getCourse_list_url());
                    context.startActivity(webIntent);
                }
            });

            LinearLayoutManager classManager = new LinearLayoutManager(context);
            classManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            newHomeKetang.newhomeRecyclerviewClass.setLayoutManager(classManager);
            NewhomeDecorationClassAdapter classAdapter = new NewhomeDecorationClassAdapter(context/*, dataSource.getCourseType()*/);
            newHomeKetang.newhomeRecyclerviewClass.setAdapter(classAdapter);
            classAdapter.notifyDataSetChanged();
            classAdapter.setOnItemClickListener(new NewhomeDecorationClassAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onRecyclerViewItemClick(View view, int position) {
                    Intent webIntent = new Intent(context, NewWebViewActivity.class);
                    webIntent.putExtra("mLoadingUrl", dataSource.getCourse_type().get(position).getJump_url());
                    context.startActivity(webIntent);
                }
            });

            KeTangAdapter adapter = new KeTangAdapter(context, dataSource.getCourse());
            newHomeKetang.newhomeRecyclerviewKetang.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            newHomeKetang.newhomeRecyclerviewKetang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent webIntent = new Intent(context, NewWebViewActivity.class);
                    webIntent.putExtra("mLoadingUrl", dataSource.getCourse().get(position).getJump_url());
                    context.startActivity(webIntent);
                }
            });
//            LinearLayoutManager ketangManager = new LinearLayoutManager(context);
//            ketangManager.setOrientation(LinearLayoutManager.VERTICAL);
//            newHomeKetang.newhomeRecyclerviewKetang.setLayoutManager(ketangManager);
//            NewhomeKetangAdapter ketangAdapter = new NewhomeKetangAdapter(context, dataSource.getCourse());
//            newHomeKetang.newhomeRecyclerviewKetang.setAdapter(ketangAdapter);
//            ketangAdapter.notifyDataSetChanged();
//            ketangAdapter.setOnItemClickListener(new NewhomeKetangAdapter.OnRecyclerViewItemClickListener() {
//                @Override
//                public void onRecyclerViewItemClick(View view, int position) {
//                    Util.setToast(context, dataSource.getCourseType().get(position) + " ] 下课堂");
//                }
//            });
        }

        if(holder instanceof NewHomeZhuanti){
            newHomeZhuanti = (NewHomeZhuanti) holder;

            FullyLinearLayoutManager zhuantiManager = new FullyLinearLayoutManager(context);
            zhuantiManager.setOrientation(LinearLayoutManager.VERTICAL);
            newHomeZhuanti.zhuantiRecyclerView.setLayoutManager(zhuantiManager);
            topicList.addAll(dataSource.getTopic());

            initZhuantiAdapter(newHomeZhuanti, topicList);

        }


        if(holder instanceof NewHomeFoot){
            NewHomeFoot footHolder = (NewHomeFoot) holder;
//            if(zhuantiMore){
//                footHolder.bar.setVisibility(View.VISIBLE);
//                footHolder.textLoadMore.setText("加载更多...");
//            }else {
//                footHolder.bar.setVisibility(View.GONE);
//                footHolder.textLoadMore.setVisibility(View.GONE);
//            }
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }


    /**
     * 头部类
     */
    class NewHomeHead extends RecyclerView.ViewHolder{
        LinearLayout layoutDot;
//        ImageView cheatIcon;
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

        public NewHomeHead(View itemView) {
            super(itemView);
            newhomeViewPager = (ViewPager) itemView.findViewById(R.id.newhome_head_viewpager);
            layoutDot = (LinearLayout) itemView.findViewById(R.id.newHomeDotLayout);
//            cheatIcon = (ImageView) itemView.findViewById(R.id.newhome_cheat_icon);
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
        }
    }


    private ArrayList<ImageView> imageViewList = new ArrayList<ImageView>();
    private ArrayList<View> dotViewsList = new ArrayList<View>();
    private ArrayList<String> urlList = new ArrayList<String>();
    private ScheduledExecutorService scheduledExecutorService;
    private void initBannerAdapter(ViewPager bannerPager, LinearLayout dotLayout, List<NewHomeDataItem.NewhomeDataBean.BannerBean> bannerList){
        dotLayout.removeAllViews();
        urlList.clear();
        if (bannerList != null && bannerList.size() > 0) {
            for(int i=0;i<bannerList.size();i++){
                urlList.add(bannerList.get(i).getContent_url());
                ImageView view = new ImageView(context);
//                view.setTag(bannerList.get(i).getImg_url());
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(context).load(bannerList.get(i).getImg_url()).into(view);
                imageViewList.add(view);
                ImageView dotView = new ImageView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        DensityUtil.dip2px(context, 10),
//                        DensityUtil.dip2px(context, 10));
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
        public MyPageChangeListener(ViewPager pager){
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

    private class ShowTask implements Runnable{
        public ShowTask(){}

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

        public MyPagerAdapter(ArrayList<ImageView> viewList, ArrayList<String> urlStrings){
            this.viewList = viewList;
            this.urlStrings = urlStrings;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(viewList.get(position));
        }

        @Override
        public Object instantiateItem(final View container, final int position) {
            ((ViewPager) container).addView(viewList.get(position));
            viewList.get(position).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent webIntent = new Intent(context, NewWebViewActivity.class);
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

    private void initCheatText(final MarqueeView cheatView){
//        交互规则：播报内容固定位置显示，每3秒向上翻动一次。
//        报告内容：“时间”+前，“城市”+“姓氏”+“性别”+“福利”
//        时间：1到59秒，随机一个时间；
//        城市：已开通的城市列表中随机一个城市；
//        姓氏：赵、钱、孙、李、周、吴、郑、王、冯、陈、褚、卫、蒋、沈、韩、杨、何、吕、施、张、孔、曹、严、华、金、魏、陶、姜、戚、谢、邹、章、苏、潘、葛、奚、范、彭、鲁、韦、昌、马、苗、凤、花、方、俞、袁、柳、酆、鲍、史、唐、费、廉、岑、薛、雷、贺、倪、汤、滕、殷、罗、毕、郝、邬、安、常、乐、于、齐、黄、萧、尹、姚、祁、宋、熊、舒、屈、项、祝、董、梁、杜、席、贾、江、郭、林、钟、徐、邱、高、田、胡、邓，随机一个；
//        性别：先生、女士，随机一个；
//        福利：领取了免费设计、获得了免费报价、获得了专业推荐、领取了装修大礼包，随机一个。
        String[] datas = new String[64];
        for(int i=0;i<datas.length;i++){
            datas[i] = " " + getSecond() + "秒前," + getRandomText(citys) + getRandomText(familyName) + getLadyOrGentalman() + getBigBagText(fares, i) + "。";
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


    private String getLadyOrGentalman(){
        int num = new Random().nextInt(10) + 1;
        if(num % 2 != 0){
            return "先生";
        }else {
            return "女士";
        }
    }

    private String getBigBagText(String[] textArr, int position){
        int len = textArr.length;
        if(position>=len){
            int pos = position % len;
            return textArr[pos];
        }else{
            return textArr[position];
        }
    }

    private String getRandomText(String[] textArr){
        Random random = new Random();
        int index = random.nextInt(textArr.length - 1) + 1;
        return textArr[index];
    }

    private int getSecond(){
        Random r = new Random();
        return r.nextInt(59) + 1;
    }

    class NewHomeAnli extends RecyclerView.ViewHolder{
        RelativeLayout relMoreAnli;
        CustomGridView newhomeGvAnli;
        public NewHomeAnli(View itemView) {
            super(itemView);
            relMoreAnli = (RelativeLayout) itemView.findViewById(R.id.rel_more_anli);
            newhomeGvAnli = (CustomGridView) itemView.findViewById(R.id.newhome_gv_anli);
        }
    }

    class NewHomeSheji extends RecyclerView.ViewHolder{
        RelativeLayout relMoreSheji;
        BetterRecyclerView rvSheji;
        MySwipeRefreshLayout shejiSwipeRefreshLayout;
        public NewHomeSheji(View itemView) {
            super(itemView);
            relMoreSheji = (RelativeLayout) itemView.findViewById(R.id.rel_more_sheji);
            rvSheji = (BetterRecyclerView) itemView.findViewById(R.id.newhome_recyclerview_sheji);
            shejiSwipeRefreshLayout = (MySwipeRefreshLayout) itemView.findViewById(R.id.swipe_newhome_sheji);
        }
    }

    class NewHomeKetang extends RecyclerView.ViewHolder{
        RelativeLayout relMoreClass;
        BetterRecyclerView newhomeRecyclerviewClass;
        MyListView newhomeRecyclerviewKetang;

        public NewHomeKetang(View itemView) {
            super(itemView);
            relMoreClass = (RelativeLayout) itemView.findViewById(R.id.rel_more_class);
            newhomeRecyclerviewClass = (BetterRecyclerView) itemView.findViewById(R.id.newhome_recyclerview_class);
            newhomeRecyclerviewKetang = (MyListView) itemView.findViewById(R.id.newhome_ketang_recyclerview);
        }
    }

    class NewHomeZhuanti extends RecyclerView.ViewHolder{
        RecyclerView zhuantiRecyclerView;
//        SwipeRefreshLayout zhuantiSwipRefreshLayout;

        public NewHomeZhuanti(View itemView) {
            super(itemView);
            zhuantiRecyclerView = (RecyclerView) itemView.findViewById(R.id.newhome_recyclerview_zhuanti);
//            zhuantiSwipRefreshLayout = (SwipeRefreshLayout) itemView.findViewById(R.id.swipe_newhome_zhuanti);
        }
    }

    public void loadMoreData(boolean more){
        page++;
        zhuantiMore = more;
        if(zhuantiMore){
            bar.setVisibility(View.VISIBLE);
            textLoadMore.setText("加载更多...");
        }else {
            bar.setVisibility(View.GONE);
            textLoadMore.setVisibility(View.GONE);
        }

        // 请求网络
        if(Util.isNetAvailable(context)){
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("token", Util.getDateToken());
            hashMap.put("page", page);
            hashMap.put("page_size", 10);
            OKHttpUtil.post(Constant.ZHUANTI_URL, hashMap, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Util.setErrorLog(TAG, "请求失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    List<NewHomeDataItem.NewhomeDataBean.TopicBean> zhuantiList = new ArrayList<NewHomeDataItem.NewhomeDataBean.TopicBean>();
                    String json = response.body().string();
                    Util.setErrorLog(TAG, json);
                    try {
                        JSONObject zhuantiObject = new JSONObject(json);
                        int status = zhuantiObject.getInt("status");
                        if(status== 200){
                            JSONArray arr = zhuantiObject.getJSONArray("data");
                            for(int i=0;i<arr.length();i++){
                                NewHomeDataItem.NewhomeDataBean.TopicBean bean = new NewHomeDataItem.NewhomeDataBean.TopicBean();
                                bean.setAdd_time(arr.getJSONObject(i).getString("add_time"));
                                bean.setDesc(arr.getJSONObject(i).getString("desc"));
                                bean.setId(arr.getJSONObject(i).getString("id"));
                                bean.setImage_url(arr.getJSONObject(i).getString("image_url"));
                                bean.setTitle(arr.getJSONObject(i).getString("title"));
                                zhuantiList.add(bean);
                            }
                            topicList.addAll(zhuantiList);
                            myhandler.sendEmptyMessage(44);

                        }else if(status == 0){
                            myhandler.sendEmptyMessage(43);

                        }else if (status == 201){
                            myhandler.sendEmptyMessage(42);
                        }else{
                            Util.setErrorLog(TAG, " 错误请求码是 [" + status + "]");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }

    }

    private Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 44:
                    initZhuantiAdapter(newHomeZhuanti, topicList);
                    break;
                case 42:
                    Util.setToast(context, "没有更多数据");
                    bar.setVisibility(View.GONE);
                    textLoadMore.setText("别扯了，到底啦");
                    break;
                case 43:
                    Util.setToast(context, "加载更多数据失败");
                    break;
            }
        }
    };

    /**
     * 底部类
     */
    class NewHomeFoot extends RecyclerView.ViewHolder{

        public NewHomeFoot(View itemView) {
            super(itemView);
            bar = (ProgressBar) itemView.findViewById(R.id.newhome_progressbar);
            textLoadMore = (TextView) itemView.findViewById(R.id.newhome_loadmore);
        }
    }
    private ProgressBar bar;
    private TextView textLoadMore;
    private int page = 0;
    private NewHomeZhuanti newHomeZhuanti;
    private NewhomeZhuantiAdapter zhuantiAdapter;
    private void initZhuantiAdapter(NewHomeZhuanti holder, final List<NewHomeDataItem.NewhomeDataBean.TopicBean> datalist){
        if(zhuantiAdapter == null){
            zhuantiAdapter = new NewhomeZhuantiAdapter(context, datalist);
            holder.zhuantiRecyclerView.setAdapter(zhuantiAdapter);
        }else {
            zhuantiAdapter.notifyDataSetChanged();
        }

        holder.zhuantiRecyclerView.smoothScrollToPosition(zhuantiAdapter.getItemCount());
        holder.zhuantiRecyclerView.addItemDecoration(new MyItemDecoration(context, LinearLayoutManager.HORIZONTAL,R.drawable.divider));
        zhuantiAdapter.notifyDataSetChanged();
        zhuantiAdapter.setOnItemClickListener(new NewhomeZhuantiAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onRecyclerViewItemClick(View view, int position) {
                Intent it = new Intent(context, TopicDetailActivity.class);
                it.putExtra("mTopicId", dataSource.getTopic().get(position).getId());
                context.startActivity(it);
            }
        });
    }

}
