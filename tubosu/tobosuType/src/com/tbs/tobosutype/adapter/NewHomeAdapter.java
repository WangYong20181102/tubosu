package com.tbs.tobosutype.adapter;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.VerticalMarqueeView;
import com.tbs.tobosutype.utils.DensityUtil;
import com.tbs.tobosutype.utils.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by Lie on 2017/10/23.
 */

public class NewHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = NewHomeAdapter.class.getSimpleName();
    private ViewPager newhomeViewPager;
    private VerticalMarqueeView cheatText;
    private Context context;
    private LayoutInflater inflater;
    private RecyclerView newhomeRecyclerView;
    private SwipeRefreshLayout newhomeSwipRefreshLayout;

    private final int ADAPTER_ITEM_HEAD = 0;
    private final int ADAPTER_ITEM_ITEM = 1;
    private final int ADAPTER_ITEM_FOOT = 2;

    private View adapterHeadView;
    private View adapterItemView;
    private View adapterFootView;

    public NewHomeAdapter(Context context){
        this.context = context;
        this.inflater = LayoutInflater.from(context);

    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ADAPTER_ITEM_HEAD){
            adapterHeadView = inflater.inflate(R.layout.layout_new_home_head, parent, false);
            NewHomeHead newHomeHead = new NewHomeHead(adapterHeadView);
            return  newHomeHead;
        }

        if(viewType == ADAPTER_ITEM_ITEM){

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
            List<String> imgList = null;  // FIXME 数据源
            initBannerAdapter(newhomeViewPager, headHolder.layoutDot, imgList);
            initCheatText();

            headHolder.relFreeLiangFang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.setToast(context, "免费量房");
                }
            });
            headHolder.relXuanSheJi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.setToast(context, "选设计");
                }
            });
            headHolder.relKanAnLi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.setToast(context, "看案例");
                }
            });
            headHolder.relZhaoZhuangXiu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.setToast(context, "找装修");
                }
            });
            headHolder.relFreeBaojia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.setToast(context, "免费报价");
                }
            });
            headHolder.relFreeSheji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.setToast(context, "免费设计");
                }
            });
            headHolder.relProfessalTuiJian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.setToast(context, "专业推荐");
                }
            });
            headHolder.tvGoGet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.setToast(context, "走你");
                }
            });




        }

        if(holder instanceof NewHomeItem){
            NewHomeItem newHomeItem = (NewHomeItem) holder;

        }

        if(holder instanceof NewHomeFoot){
            NewHomeFoot newHomeFoot = (NewHomeFoot) holder;

        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    /**
     * 头部类
     */
    class NewHomeHead extends RecyclerView.ViewHolder{
        LinearLayout layoutDot;
        ImageView cheatIcon;
        RelativeLayout relFreeLiangFang;
        RelativeLayout relXuanSheJi;
        RelativeLayout relKanAnLi;
        RelativeLayout relZhaoZhuangXiu;
        RelativeLayout relFreeBaojia;
        RelativeLayout relFreeSheji;
        RelativeLayout relProfessalTuiJian;
        TextView tvGoGet;

        public NewHomeHead(View itemView) {
            super(itemView);
            newhomeViewPager = (ViewPager) itemView.findViewById(R.id.newhome_head_viewpager);
            layoutDot = (LinearLayout) itemView.findViewById(R.id.newHomeDotLayout);
            cheatIcon = (ImageView) itemView.findViewById(R.id.newhome_cheat_icon);
            cheatText = (VerticalMarqueeView) itemView.findViewById(R.id.newhome_cheat_text);
            relFreeLiangFang = (RelativeLayout) itemView.findViewById(R.id.relFreeLiangFang);
            relXuanSheJi = (RelativeLayout) itemView.findViewById(R.id.relXuanSheJi);
            relKanAnLi = (RelativeLayout) itemView.findViewById(R.id.relKanAnLi);
            relZhaoZhuangXiu = (RelativeLayout) itemView.findViewById(R.id.relZhaoZhuangXiu);
            relFreeBaojia = (RelativeLayout) itemView.findViewById(R.id.relFreeBaojia);
            relFreeSheji = (RelativeLayout) itemView.findViewById(R.id.relFreeSheji);
            relProfessalTuiJian = (RelativeLayout) itemView.findViewById(R.id.relProfessalTuiJian);
            tvGoGet = (TextView) itemView.findViewById(R.id.tvGoGet);
        }
    }


    private ArrayList<ImageView> imageViewList = new ArrayList<ImageView>();
    private ArrayList<View> dotViewsList = new ArrayList<View>();
    private ScheduledExecutorService scheduledExecutorService;
    private void initBannerAdapter(ViewPager bannerPager, LinearLayout dotLayout, List<String> bannerList){
        dotLayout.removeAllViews();
        if (bannerList != null && bannerList.size() > 0) {
            for(int i=0;i<bannerList.size();i++){
                ImageView view = new ImageView(context);
                view.setTag(bannerList.get(i));
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageViewList.add(view);
                // 广播图的定位点
                ImageView dotView = new ImageView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        DensityUtil.dip2px(context, 10),
                        DensityUtil.dip2px(context, 10));
                params.leftMargin = 8;
                params.rightMargin = 8;
                dotLayout.addView(dotView, params);
                dotViewsList.add(dotView);
            }
            bannerPager.setFocusable(true);
            bannerPager.setAdapter(new MyPagerAdapter(imageViewList));
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
                    ((View) dotViewsList.get(pos)).setBackgroundResource(R.drawable.dot_select);
                } else {
                    ((View) dotViewsList.get(i)).setBackgroundResource(R.drawable.dot_normal);
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

        public MyPagerAdapter(ArrayList<ImageView> viewList){
            this.viewList = viewList;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(viewList.get(position));
        }

        @Override
        public Object instantiateItem(View container, final int position) {
            ImageView imageView = viewList.get(position);
            imageView.setBackgroundResource(R.drawable.firstloaderror);


            ((ViewPager) container).addView(viewList.get(position));

            viewList.get(position).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

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

    private void initCheatText(){
//        交互规则：播报内容固定位置显示，每3秒向上翻动一次。
//        报告内容：“时间”+前，“城市”+“姓氏”+“性别”+“福利”
//        时间：1到59秒，随机一个时间；
//        城市：已开通的城市列表中随机一个城市；
//        姓氏：赵、钱、孙、李、周、吴、郑、王、冯、陈、褚、卫、蒋、沈、韩、杨、何、吕、施、张、孔、曹、严、华、金、魏、陶、姜、戚、谢、邹、章、苏、潘、葛、奚、范、彭、鲁、韦、昌、马、苗、凤、花、方、俞、袁、柳、酆、鲍、史、唐、费、廉、岑、薛、雷、贺、倪、汤、滕、殷、罗、毕、郝、邬、安、常、乐、于、齐、黄、萧、尹、姚、祁、宋、熊、舒、屈、项、祝、董、梁、杜、席、贾、江、郭、林、钟、徐、邱、高、田、胡、邓，随机一个；
//        性别：先生、女士，随机一个；
//        福利：领取了免费设计、获得了免费报价、获得了专业推荐、领取了装修大礼包，随机一个。
        String[] familyName = new String[] {"赵","钱","孙","李","周","吴","郑","王","冯","陈","褚","卫","蒋","沈","韩","杨","何","吕","施","张","孔","曹","严","华","金","魏","陶","姜","戚","谢","邹","章","苏","潘","葛","奚","范","彭","鲁","韦","昌","马","苗","凤","花","方","俞","袁","柳","酆","鲍","史","唐","费","廉","岑","薛","雷","贺","倪","汤","滕","殷","罗","毕","郝","邬","安","常","乐","于","齐","黄","萧","尹","姚","祁","宋","熊","舒","屈","项","祝","董","梁","杜","席","贾","江","郭","林","钟","徐","邱","高","田","胡","邓"};
        String[] fares = new String[]{"领取了免费设计","获得了免费报价","获得了专业推荐","领取了装修大礼包"};
        String[] citys = new String[]{"北京市"," 天津市"," 石家庄市"," 唐山市"," 秦皇岛市"," 太原市"," 大同市"," 长治市","呼和浩特市", "包头市", "沈阳市", "大连市", "长春市", "哈尔滨市", "上海市", "南京市", "无锡市", "徐州市", "常州市", "苏州市", "南通市", "扬州市", "杭州市", "宁波市", "温州市", "嘉兴市", "湖州市", "绍兴市", "金华市", "台州市", "合肥市", "福州市", "厦门市", "南昌市", "济南市", "青岛市", "烟台市", "潍坊市", "威海市", "郑州市", "武汉市", "长沙市", "广州市", "深圳市", "珠海市", "佛山市", "东莞市", "中山市","南宁市", "重庆市", "成都市", "贵阳市", "昆明市", "西安市", "兰州市", "江阴市", "宜兴市", "昆山市", "张家港市", "余姚市", "慈溪市"};

        String[] datas = new String[64];
        for(int i=0;i<datas.length;i++){
            datas[i] = getSecond() + "秒前," + getRandomText(citys) + getRandomText(familyName) + getLadyOrGentalman() + getRandomText(fares) + "。";
        }

        cheatText.datas(datas);
        cheatText.startScroll();
    }

    public void stopVerticalMarqueeView(){
        cheatText.stopScroll();
    }

    private String getLadyOrGentalman(){
        int num = new Random().nextInt(10) + 1;
        if(num % 2 != 0){
            return "先生";
        }else {
            return "女士";
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

    class NewHomeItem extends RecyclerView.ViewHolder{

        public NewHomeItem(View itemView) {
            super(itemView);
        }

    }

    /**
     * 底部类
     */
    class NewHomeFoot extends RecyclerView.ViewHolder{
        ProgressBar bar;
        TextView textLoadMore;

        public NewHomeFoot(View itemView) {
            super(itemView);
            bar = (ProgressBar) itemView.findViewById(R.id.newhome_progressbar);
            textLoadMore = (TextView) itemView.findViewById(R.id.newhome_loadmore);
        }
    }


}
