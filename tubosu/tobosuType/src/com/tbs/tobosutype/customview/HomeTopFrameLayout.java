package com.tbs.tobosutype.customview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.FreeActivity;
import com.tbs.tobosutype.activity.PopOrderActivity;
import com.tbs.tobosutype.activity.WebViewActivity;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.MyApplication;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.DensityUtil;
import com.tbs.tobosutype.utils.HttpServer;
import com.tbs.tobosutype.utils.MD5Util;
import com.tbs.tobosutype.utils.Util;
import com.umeng.analytics.MobclickAgent;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 首页顶部广告
 *
 * @author dec
 */
public class HomeTopFrameLayout extends FrameLayout {
    private static final String TAG = HomeTopFrameLayout.class.getSimpleName();
    private Context context;
    private String cityName;
    private int mTime = 10;//计时用 10S内点击之后再点击不进行统计
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private final static boolean isAutoPlay = true;
    private String[] imageUrls;
    private List<ImageView> imageViewsList;
    private List<View> dotViewsList;
    private HashMap<String, String> countParams;

    /**
     * 包含h5发单url
     */
    private String containFreeUrl = "http://m.tobosu.com/app/pub?";
//									 http://m.tobosu.com/app/pub?
//	                                 http://m.tobosu.com/app/pub?channel=sem&subchannel=android&chcode=product

    /**
     * 根据不同城市而呈现不同的图片
     */
    private String urlString = Constant.TOBOSU_URL + "tapp/util/carousel_figure";/*?city=*/
    //									http://www.tobosu.com/tapp/util/carousel_figure
    //统计所用的Api地址
    private String urlCount = Constant.TOBOSU_URL + "tapp/banner/banner_views";

    private ViewPager viewPager;
    private int currentItem = 0;

    /**
     * 定时周期执行指定的任务
     */
    private ScheduledExecutorService scheduledExecutorService;

    /**
     * banner条上左右滑动的item的url集合
     */
    private List<HashMap<String, Object>> slideShowList = new ArrayList<HashMap<String, Object>>();

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            viewPager.setCurrentItem(currentItem);
        }

    };

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 44:
                    String result = (String) msg.getData().get("result");
                    showImages(result);
                    break;
            }
        }
    };

    public HomeTopFrameLayout(Context context) {
        this(context, null);
    }

    public HomeTopFrameLayout(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public HomeTopFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;

        initImageLoader(context);

        cityName = context.getSharedPreferences("Save_City_Info", Context.MODE_PRIVATE).getString("save_city_now", cityName);
        if(cityName==null || "".equals(cityName)){
            cityName = "深圳";
        }

        getBanner();
        countParams = new HashMap<String, String>();
        if (isAutoPlay) {
            startPlay();
        }

    }

    private void startPlay() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4, TimeUnit.SECONDS);
    }


    private void getBanner() {
        Util.setErrorLog(TAG, "现在是" + cityName + "的轮播图片");
        if(Util.isNetAvailable(context)){
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("city", cityName);
            param.put("device", "android");
            param.put("version", AppInfoUtil.getAppVersionName(context));
            Util.setErrorLog(TAG, "---version-->>" + AppInfoUtil.getAppVersionName(context));
            OKHttpUtil.post(urlString, param, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Util.setErrorLog(TAG, "---onFailure-->>getBanner--");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    Util.setErrorLog(TAG, "---banner结果-->>" + result);
                    context.getSharedPreferences("NavigationCache", 0).edit().putString("result", result).commit();
                    Message msg = new Message();
                    msg.what = 44;
                    Bundle b = new Bundle();
                    b.putString("result", result);
                    msg.setData(b);
                    myHandler.sendMessage(msg);
                }
            });
        }
    }

    public void initUI(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_home_top_ad_view, this, true);
        LinearLayout dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
        dotLayout.removeAllViews();
        imageViewsList = new ArrayList<ImageView>();
        dotViewsList = new ArrayList<View>();
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        // 连续播放的图片
        if (imageUrls != null && imageUrls.length > 0) {
            for (int i = 0; i < imageUrls.length; i++) {
                ImageView view = new ImageView(context);
                view.setTag(imageUrls[i]);
                view.setScaleType(ScaleType.CENTER_CROP);
                imageViewsList.add(view);
                // 广播图的定位点
                ImageView dotView = new ImageView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        DensityUtil.dip2px(getContext(), 10),
                        DensityUtil.dip2px(getContext(), 10));
                params.leftMargin = 8;
                params.rightMargin = 8;
                dotLayout.addView(dotView, params);
                dotViewsList.add(dotView);
            }
            viewPager.setFocusable(true);
            viewPager.setAdapter(new MyPagerAdapter());
            viewPager.setOnPageChangeListener(new MyPageChangeListener());
        }
    }

    /***
     * 首页顶部左右滑动适配器
     * @author dec
     *
     */
    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(imageViewsList.get(position));
        }

        @Override
        public Object instantiateItem(View container, final int position) {
            ImageView imageView = imageViewsList.get(position);
            imageView.setBackgroundResource(R.drawable.firstloaderror);

            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheOnDisc(true)
                    .showImageOnFail(R.drawable.firstloaderror)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .resetViewBeforeLoading(true).build();

            imageLoader.displayImage(imageView.getTag() + "", imageView, options);
            
            
            ((ViewPager) container).addView(imageViewsList.get(position));

            imageViewsList.get(position).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    String get_content_url = (String) slideShowList.get(position).get("content_url");
                    String get_banner_id = (String) slideShowList.get(position).get("id");
                    //在此处加载点击统计事件 设置定时任务
                    Util.setErrorLog(TAG, "--请检查--发单接口【" + get_content_url + "】");
                    countParams.put("banner_id", get_banner_id);
                    countParams.put("type", "3");
                    countParams.put("chcode", AppInfoUtil.getChannType(MyApplication.getContext()));
                    countParams.put("version", AppInfoUtil.getAppVersionName(MyApplication.getContext()));
                    countParams.put("mac_code", AppInfoUtil.getMacAddress(MyApplication.getContext()));
                    countParams.put("app_platform", "1");
                    Date date = new Date();
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String mTime = format.format(date);
                    String _token = MD5Util.md5(MD5Util.md5(get_banner_id + "3" + AppInfoUtil.getChannType(MyApplication.getContext())) + mTime);
                    countParams.put("_token", _token);
                    Util.setErrorLog(TAG, "=banner_id=" + get_banner_id + "=chcode=" + AppInfoUtil.getChannType(MyApplication.getContext()) + "=version=" + AppInfoUtil.getAppVersionName(MyApplication.getContext()) + "=mac_code=" + AppInfoUtil.getMacAddress(MyApplication.getContext()) + "=time=" + mTime);
                    HttpCountClick(countParams);


                    //
                    if (!get_content_url.contains(containFreeUrl)) {
                        Util.setErrorLog(TAG, "--请检查--发单接口 走WebViewActivity原生【" + get_content_url + "】 【WebViewActivity走的不是安卓发单入口】");

                        /*----------------------------------走发单PopOrderActivity 2017-07-06 邝俊要求--------------------------------------*/

                        if(get_content_url.contains("guideBilling")){
                            context.startActivity(new Intent(context, PopOrderActivity.class));
                        } else {
                       /*----------------------------------走发单PopOrderActivity 2017-07-06 邝俊要求--------------------------------------*/
                            Intent detailIntent = new Intent(context, WebViewActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("link", get_content_url);
                            detailIntent.putExtras(bundle);
                            context.startActivity(detailIntent);
                        }



                    }else {
                        MobclickAgent.onEvent(context, "click_find_decoration_activite");
                        Util.setErrorLog(TAG, "--请检查--发单接口 走FreeActivity【注意FreeActivity是绝对走android发单入口的】");
                        Intent intent = new Intent(context, FreeActivity.class);
                        context.startActivity(intent);
                    }
                }
            });
            return imageViewsList.get(position);
        }

        @Override
        public int getCount() {
            return imageViewsList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(View arg0) {

        }
    }

    private void HttpCountClick(HashMap params) {
        OKHttpUtil.post(urlCount, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });

    }

    private class MyPageChangeListener implements OnPageChangeListener {

        boolean isAutoPlay = false;

        @Override
        public void onPageScrollStateChanged(int arg0) {

            switch (arg0) {
                case 1:
                    isAutoPlay = false;
                    break;
                case 2:
                    isAutoPlay = true;
                    break;
                case 0:
                    if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        viewPager.setCurrentItem(0);
                    } else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

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

    private class SlideShowTask implements Runnable {

        @Override
        public void run() {
            synchronized (viewPager) {
                currentItem = (currentItem + 1) % imageViewsList.size();
                handler.obtainMessage().sendToTarget();
            }
        }

    }

    /***
     *  显示顶部轮播图
     * @param result
     */
    private void showImages(String result) {
        slideShowList = parseSlideShowListJSON(result);
        if (slideShowList != null && slideShowList.size() > 0) {
            imageUrls = new String[slideShowList.size()];
            for (int i = 0; i < slideShowList.size(); i++) {
                imageUrls[i] = (String) slideShowList.get(i).get("img_url");
                Util.setLog(TAG, "顶部录播图片中第" + i + "个图片-->>" + imageUrls[i]);
            }
            initUI(context);
        }
    }

    /**
     * ImageLoader 图片组件初始化
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
    }

    private List<HashMap<String, Object>> parseSlideShowListJSON(String result) {
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        if (result.contains("data")) {
            try {
                JSONObject object1 = new JSONObject(result);
                JSONArray array = object1.getJSONArray("data");

                for (int i = 0; i < array.length(); i++) {
                    HashMap<String, Object> dataMap = new HashMap<String, Object>();
                    JSONObject dataObject = array.getJSONObject(i);
                    dataMap.put("id", dataObject.getString("id"));
                    dataMap.put("img_url", dataObject.getString("img_url"));
                    dataMap.put("content_url", dataObject.getString("content_url"));
                    list.add(dataMap);
                }
                return list;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}