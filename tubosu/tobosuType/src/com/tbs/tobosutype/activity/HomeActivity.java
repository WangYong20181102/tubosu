package com.tbs.tobosutype.activity;
import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.HomeDecorateClassAdapter;
import com.tbs.tobosutype.customview.GalleryGridView;
import com.tbs.tobosutype.customview.HomeTopFrameLayout;
import com.tbs.tobosutype.customview.RoundAngleImageView;
import com.tbs.tobosutype.customview.RoundImageView;
import com.tbs.tobosutype.customview.ScrollViewExtend;
import com.tbs.tobosutype.customview.ScrollViewExtend.OnScrollChangedListener;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.MyApplication;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.CacheManager;
import com.tbs.tobosutype.utils.DensityUtil;
import com.tbs.tobosutype.utils.HttpServer;
import com.tbs.tobosutype.utils.Util;
import com.umeng.analytics.MobclickAgent;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 首页
 *
 * @author dec
 */
public class HomeActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = HomeActivity.class.getSimpleName();

    /**
     * 顶部标题栏布局
     */
    private RelativeLayout headLayout;

    /**
     * ---------顶部标题栏隐藏和出现---------
     */
    private static final int START_ALPHA = 0;
    private static final int END_ALPHA = 255;
    private int fadingHeight = 300;
    /*-----------------------------------*/

    /**
     * 首页标题
     */
    private String banner_title;

    private TextView home_city;

    /**
     * 选择城市
     */
    private ImageView home_select_city;

    private String cityName;

    /**
     * 百度地图相关
     */
    private LocationClient mLocationClient;
    private LocationMode tempMode = LocationMode.Device_Sensors;
    private String tempcoor = "gcj02";

    /**
     * 首页顶部公司logo图片
     */
    private ImageView home_top_logo;


    private ImageView ivHomePop;

    /**
     * 精选图片
     */
    private RoundAngleImageView ivHomeFullySelectedImages;

    /**
     * 首页效果精选的一张图下面的标题
     */
    private TextView banner_textview_title;


	/*--------------------归类----------------------*/
    /**
     * 设计与报价
     */
    private LinearLayout layout_price;

    /**
     * 预约装修
     */
    private LinearLayout layout_homeactivity_yuyue_decoration;

    /**
     * 装好家
     */
    private LinearLayout layout_decorate_myhouse;

    /**
     * 计算器
     */
    private LinearLayout layout_calculater;
    /*--------------------归类----------------------*/


    /**
     * 全部装修课堂
     */
    private TextView home_decorate_class_all;

    /**
     * 装修课堂数据
     */
    private List<String> decorationClassDatas;

    /**
     * 装修课堂横向布局ScrollView
     */
    private HorizontalScrollView decorateClassScrollView;

    /**
     * 装修课堂gridview
     */
    private GalleryGridView decorate_class_gridview;

    /**
     * 装修课堂适配器
     */
    private HomeDecorateClassAdapter homeDecorateClassAdapter;

//	/**装修课堂 嵌套在horizontalscrollview中的横向布局*/
//	private LinearLayout liearlayout_decorate_class;

    /**
     * 装修课堂横向
     */
    private LinearLayout ll_decorate_class_gallery;

    /**
     * 本地优惠布局
     */
    private LinearLayout ll_local_dicount_layout;

    /**
     * 本地优惠数据集合
     */
    private List<HashMap<String, String>> localDiscountList;

    /**
     * 本地优惠横向
     */
    private LinearLayout liearlayout_local_discount;

	/*-------------------------------------------------*/
//	/** 本地优惠数据适配器 */
//	private LocalDiscountAdapter localDiscountAdapter;

//	/**本地优惠GridView*/
//	private GalleryGridView local_discount_gridview;

//	/** 本地优惠横向 */
//	private LinearLayout linearlayout_local_discount;

//	/**本地优惠横向*/
//	private HorizontalScrollView localDiscountHorizontalScrollView;

//	/** 本地优惠Gallery */  // 已使用GridView代替Gallery
//	private Gallery preferential_gallery;
    /*-------------------------------------------------*/


    /**
     * 效果图精选  更多 按钮
     */
    private TextView tv_home_project_more;

    /**
     * 首页效果图精选图的id
     */
    private String id;

//	/** 接口请求对象 */
//	private RequestParams getSpecialParams;

    /**
     * 装修课堂图片资源放在Drawable中
     */
    private Drawable drawable;

    /**
     * 本页面的滑动ScrollView
     */
    private ScrollViewExtend scrollViewExtend;


    /**
     * 装修课堂接口
     */
    private String decorationClassUrl = Constant.TOBOSU_URL + "tapp/util/decorationClass";

    /**
     * 本地优惠接口
     */
    private String discountUrl = Constant.TOBOSU_URL + "tapp/util/discount";

//	/** 精选图册接口 */
//	private String getSpecialUrl = Constant.TOBOSU_URL + "tapp/util/get_special";

    private RequestParams decorationClassParams;
    private RequestParams localDiscountParams;


    /**
     * 重新保存本地的城市参数
     */
    private SharedPreferences saveCitySharePre;



    /**
     * 点这里获取4套免费设计
     */
    private TextView tv_cancel_get_design;

    private int chooseStyle = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mContext = HomeActivity.this;


        needPermissions();
        initView();
        initEvent();
        initData();

        //装修课堂
        initDecorateCalss();
        //装修课堂接口请求
        localDiscountParams = initLocalDiscountParam(getSharedPreferences("Save_City_Info", MODE_PRIVATE).getString("save_city_now", cityName));
//		Log.d("你看看——返回时城市",getSharedPreferences("Save_City_Info", MODE_PRIVATE).getString("save_city_now", cityName));
        requestDecorationClass();
        //本地优惠接口请求
//		requestLocalDiscountData(); // 要求删除
//		//精选图
//		operGetSpecial();
        Log.e(TAG + "获取的版本信息", "======" + AppInfoUtil.getAppVersionName(mContext));
        Log.e(TAG + "获取的PIPE信息", "======" + Constant.PIPE);
//        Log.e("获取Java代码中的渠道信息", "======" + Constant.CHANNEL_TYPE);
//        Log.e("获取Manifest文件中的渠道信息", "======" + AppInfoUtil.getChannType(mContext));

        getHuoDongPicture();
    }

    private RequestParams initLocalDiscountParam(String cityName) {
        RequestParams params = new RequestParams();
        params.put("city", cityName);
        return params;
    }

    @SuppressWarnings("deprecation")
    private void initView() {
        /*----------- 首页 整页滑动控件 -----------*/
        scrollViewExtend = (ScrollViewExtend) findViewById(R.id.scrollViewExtend);

		/*----------- 首页topbar -----------*/
        headLayout = (RelativeLayout) findViewById(R.id.rl_head);
        home_city = (TextView) headLayout.findViewById(R.id.tv_home_choose_city);
        home_select_city = (ImageView) headLayout.findViewById(R.id.iv_home_select_city);
        ivHomePop = (ImageView) headLayout.findViewById(R.id.iv_home_pop);
        home_top_logo = (ImageView) findViewById(R.id.home_top_logo);
        drawable = getResources().getDrawable(R.drawable.color_first_head);
        drawable.setAlpha(START_ALPHA);
        headLayout.setBackgroundDrawable(drawable);
        headLayout.setOnClickListener(this);


		/*----------- 首页 设计报价&预约装修&装修课堂&装修公司 -----------*/
        layout_price = (LinearLayout) findViewById(R.id.layout_price);
        layout_calculater = (LinearLayout) findViewById(R.id.layout_calculater);
        layout_homeactivity_yuyue_decoration = (LinearLayout) findViewById(R.id.layout_homeactivity_yuyue_decoration);
        layout_decorate_myhouse = (LinearLayout) findViewById(R.id.layout_decorate_myhouse);

		/*----------- 首页装修课堂 -----------*/
        decorate_class_gridview = (GalleryGridView) findViewById(R.id.decorate_gridview);
        ll_decorate_class_gallery = (LinearLayout) findViewById(R.id.linearlayout_decorate_class);
        decorateClassScrollView = (HorizontalScrollView) findViewById(R.id.decorate_class_horizontal_scrollView);

		/*----------- 首页本地优惠 -----------*/
        ll_local_dicount_layout = (LinearLayout) findViewById(R.id.ll_local_dicount_layout);
        home_decorate_class_all = (TextView) findViewById(R.id.home_decorate_class_all);
//		liearlayout_decorate_class = (LinearLayout) findViewById(R.id.liearlayout_decorate_class);
        liearlayout_local_discount = (LinearLayout) findViewById(R.id.liearlayout_local_discount);

		/*----------- 首页效果图精选 -----------*/
        ivHomeFullySelectedImages = (RoundAngleImageView) findViewById(R.id.iv_home_fully_selected_images);
        banner_textview_title = (TextView) findViewById(R.id.banner_textview_title);
        tv_home_project_more = (TextView) findViewById(R.id.tv_home_project_more);

//		preferential_gallery = (Gallery) findViewById(R.id.preferential_gallery); // 已被local_discount_gridview代替
//		local_discount_gridview = (GalleryGridView) findViewById(R.id.local_discount_gridview);
//		linearlayout_local_discount = (LinearLayout) findViewById(R.id.linearlayout_local_discount);
//		localDiscountHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.local_discount_horizontalScrollView);


        initType();
    }


    /***
     * 已请求网络接口 现在填充数据
     */
    private void initData() {
        cityName = AppInfoUtil.getCityName(this);
        home_city.setText(cityName);
        decorationClassDatas = new ArrayList<String>();
//		requestLocalDiscountData();

//		String specialResult = getSharedPreferences("SpecialCache", 0).getString("result", "");
//		Log.d(TAG, "--这里是获取缓存的数据 -- 精选--[" + specialResult + "]--");
//		if (!TextUtils.isEmpty(specialResult)) {
//			parseData(specialResult);
//		}
        String discountResult = getSharedPreferences("discountResult", 0).getString("result", "");
        localDiscountList = new ArrayList<HashMap<String, String>>();
        if (!TextUtils.isEmpty(discountResult)) {
            parseLocalDiscounts(discountResult);
        }

    }

    private  void needPermissions(){
        if(Build.VERSION.SDK_INT >= 23){
            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE,

                    Manifest.permission.ACCESS_NETWORK_STATE
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.ACCESS_WIFI_STATE

            };

            requestPermissions(permissions, 101);
        }
    }

//	/***
//	 * 本地优惠请求网络获取数据  2017-01-13 要求删除
//	 */
//	private void requestLocalDiscountData() {
//
//
//		HttpServer.getInstance().requestPOST(discountUrl, localDiscountParams, new AsyncHttpResponseHandler() {
//
//					@Override
//					public void onSuccess(int arg0, Header[] arg1, byte[] body) {
//						String result = new String(body);
////						Toast.makeText(mContext, !"".equals(result)? "再次请求成功":"再次请求失败", Toast.LENGTH_SHORT).show();
//						try {
//							JSONObject jsonObject = new JSONObject(result);
//							if (jsonObject.getInt("error_code") == 0) {
//								parseLocalDiscounts(result);
//							}
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//					}
//
//					@Override
//					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
//
//					}
//				});
//	}

    /***
     * 解析本地优惠的json
     *
     * @param result
     */
    private void parseLocalDiscounts(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getInt("error_code") == 0) {
                JSONArray data = jsonObject.getJSONArray("data");
                localDiscountList.clear();
                for (int i = 0; i < data.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("endtime", data.getJSONObject(i).getString("endtime"));
                    map.put("title", data.getJSONObject(i).getString("title"));
                    map.put("activityid", data.getJSONObject(i).getString("activityid"));
                    String logosmall = data.getJSONObject(i).getString("logosmall");
                    if (TextUtils.isEmpty(logosmall)) {
                        map.put("logosmall", data.getJSONObject(i).getString("logo"));
                    } else {
                        map.put("logosmall", logosmall);
                    }
                    localDiscountList.add(map);
                }
                if (localDiscountList.size() > 0) {
                    ll_local_dicount_layout.setVisibility(View.VISIBLE);
                } else {
                    ll_local_dicount_layout.setVisibility(View.GONE);
                }

                initLocalDiscountData();
//				Log.d(TAG, "重新解析拉");
//				operateLocalDiscount();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<Drawable> view_bg;

    /**
     * 初始化 本地优惠数据
     */
    private void initLocalDiscountData() {

        view_bg = new ArrayList<Drawable>();
        view_bg.add(mContext.getResources().getDrawable(R.drawable.icon_discount_coupon1));
        view_bg.add(mContext.getResources().getDrawable(R.drawable.icon_discount_coupon2));
        view_bg.add(mContext.getResources().getDrawable(R.drawable.icon_discount_coupon3));

        for (int i = 0; i < localDiscountList.size(); i++) {
            View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.item_local_discount_gridview_item, liearlayout_local_discount, false);

            setBackgroundOfVersion(view, view_bg.get(i % 3));
            RoundImageView riv_logo = (RoundImageView) view.findViewById(R.id.riv_logo);
            MyApplication.imageLoader.displayImage(localDiscountList.get(i % localDiscountList.size()).get("logosmall"), riv_logo, MyApplication.options);
            setTextStyle((TextView) view.findViewById(R.id.tv_local_discount_desc), localDiscountList.get(i % localDiscountList.size()).get("title"), (TextView) view.findViewById(R.id.tv_end_time), localDiscountList.get(i % localDiscountList.size()).get("endtime") + "截止", i);

            if (liearlayout_local_discount.getChildCount() < localDiscountList.size()) {
                liearlayout_local_discount.addView(view);
            }

            view.setOnClickListener(new MyOnclick(i, localDiscountList.get(i).get("activityid")));
        }

    }


    /**
     * 在API16以前使用setBackgroundDrawable，在API16以后使用setBackground
     * API16<---->4.1
     *
     * @param view
     * @param drawable
     */
    private void setBackgroundOfVersion(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //Android系统大于等于API 16，使用setBackground
            view.setBackground(drawable);
        } else {
            //Android系统小于API 16，使用setBackground
            view.setBackgroundDrawable(drawable);
        }
    }


    private void setTextStyle(TextView textViewTitle, String title, TextView textViewTime, String time, int position) {
        if (position % 3 == 0) {
            textViewTitle.setTextColor(Color.parseColor("#994B3D"));
        } else if (position % 3 == 1) {
            textViewTitle.setTextColor(Color.parseColor("#3D8799"));
        } else if (position % 3 == 2) {
            textViewTitle.setTextColor(Color.parseColor("#997C3D"));
        }
        textViewTitle.setText(title);
        textViewTime.setText(time);
        textViewTime.setTextColor(Color.parseColor("#BBB2B0"));
    }


    /***
     * 本地优惠点击事件
     *
     * @author dec
     */
    class MyOnclick implements OnClickListener {

        private int position;
        private String string;

        public MyOnclick(int position, String string) {
            this.position = position;
            this.string = string;
        }

        @Override
        public void onClick(View v) {
            MobclickAgent.onEvent(mContext, "click_index_local_discount_id");
            Intent intent = new Intent(HomeActivity.this, LocalDiscountDetailActivity.class);
            intent.putExtra("activityid", string);
            startActivity(intent);

        }
    }


    private void initEvent() {
        layout_price.setOnClickListener(this);
        layout_calculater.setOnClickListener(this);
        layout_homeactivity_yuyue_decoration.setOnClickListener(this);
        layout_decorate_myhouse.setOnClickListener(this);
        home_select_city.setOnClickListener(this);
        home_city.setOnClickListener(this);
        home_decorate_class_all.setOnClickListener(this);
        tv_home_project_more.setOnClickListener(this);
        scrollViewExtend.setOnScrollChangedListener(scrollChangedListener);
        mLocationClient = MyApplication.getInstance().mLocationClient;
        initLocation();
        mLocationClient.start();
        mLocationClient.requestLocation();


        ivHomePop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PopOrderActivity.class);
                startActivity(intent);
            }
        });

        ivHomeFullySelectedImages.setClickable(false);

        // 走装修公司课堂h5页面
        decorate_class_gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        decorate_class_gridview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (decorationClassDatas.size() == 0) {
                    Toast.makeText(mContext, "刷新数据中,请稍等...", Toast.LENGTH_SHORT).show();
                } else {
                    MobclickAgent.onEvent(mContext, "click_index_decoration_class");
                    Util.setErrorLog(TAG,TAG + "====-前 中 后，选材，设计，风水 传过去的 拼接前 :" + decorationClassDatas.get(position));
                    String content_url = decorationClassDatas.get(position) + Constant.M_POP_PARAM + Constant.WANGJIANLIN;
                    Util.setErrorLog(TAG,TAG + "====-前 中 后，选材，设计，风水 传过去的 拼接后 content_url :" + content_url);
                    // Log.d(TAG, "下面加载h5页面");
                    Intent detailIntent = new Intent(HomeActivity.this, WebViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("link", content_url);
                    detailIntent.putExtras(bundle);
                    startActivity(detailIntent);
                }

            }
        });

        // 走精选详情页面
        ivHomeFullySelectedImages.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (id != null && id.length() > 0) {
                    // 精选详情
                    Intent intent = new Intent(HomeActivity.this, SelectedImageDetailActivity.class);
                    // 这里传id SelectedImageDetailActivity的item点击是传url的。
                    intent.putExtra("id", id);
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "数据还没有加载完!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        home_city.setText(AppInfoUtil.getCityName(mContext));

        if (checkNetState()) {
//			Log.d(TAG, "现在有网络了..");
            initData();
            new HomeTopFrameLayout(HomeActivity.this);
            //装修课堂
            initDecorateCalss();
            //装修课堂接口请求
            requestDecorationClass();
//			//精选图
//			operGetSpecial();

        }

        initView();
        super.onResume();
    }

    private boolean checkNetState() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private LinearLayout layout_a_style;
    private RelativeLayout rel_local_a, rel_style_a, rel_space_a, rel_home_sytle_a, rel_special_a;
    private LinearLayout layout_b_style;
    private LinearLayout home_local, home_style, home_space, home_house_style, home_specail;

    private void initType() {
        initJingXuanView();

        chooseStyle = (int) (Math.random() * 10);//产生0-10的整数随机数
//        // 需要保留在本地 且请求接口。
//        // AB测试状态。0不做任何处理、1选择A、2选择B
//        if ("1".equals(getSharedPreferences("AB_TEST", mContext.MODE_PRIVATE).getString("status", "0"))) {
//            // 1 是 A
//            MobclickAgent.onEvent(mContext, "you_choose_a_test");
//            layout_a_style.setVisibility(View.VISIBLE);
//            layout_b_style.setVisibility(View.GONE);
//        } else if ("2".equals(getSharedPreferences("AB_TEST", mContext.MODE_PRIVATE).getString("status", "0"))) {
//            // 2 是 B
//            MobclickAgent.onEvent(mContext, "you_choose_b_test");
//            layout_a_style.setVisibility(View.GONE);
//            layout_b_style.setVisibility(View.VISIBLE);
//        } else {
//            if ("".equals(getSharedPreferences("PRE_AB_TEST", mContext.MODE_PRIVATE).getString("pre_test", ""))) {
//                if (chooseStyle % 2 == 0) {
//                    // 双数 选择b
//                    MobclickAgent.onEvent(mContext, "you_choose_b_test");
//                    layout_a_style.setVisibility(View.GONE);
//                    layout_b_style.setVisibility(View.VISIBLE);
//                    getSharedPreferences("PRE_AB_TEST", mContext.MODE_PRIVATE).edit().putString("pre_test", "b").commit();
//                    Util.setErrorLog(TAG,"----测试 期间 你选择的是b测------");
//                } else {
//                    // 单数 选择a
//                    MobclickAgent.onEvent(mContext, "you_choose_a_test");
//                    layout_a_style.setVisibility(View.VISIBLE);
//                    layout_b_style.setVisibility(View.GONE);
//                    getSharedPreferences("PRE_AB_TEST", mContext.MODE_PRIVATE).edit().putString("pre_test", "a").commit();
//                    Util.setErrorLog(TAG,"----测试 期间 你选择的是a测------");
//                }
//            } else {
//                if ("b".equals(getSharedPreferences("PRE_AB_TEST", mContext.MODE_PRIVATE).getString("pre_test", ""))) {
//                    // 双数 选择b
//                    MobclickAgent.onEvent(mContext, "you_choose_b_test");
//                    layout_a_style.setVisibility(View.GONE);
//                    layout_b_style.setVisibility(View.VISIBLE);
//                    Util.setErrorLog(TAG,"----启动后 已选定了 -->>> b 测<<<------");
//                } else if ("a".equals(getSharedPreferences("PRE_AB_TEST", mContext.MODE_PRIVATE).getString("pre_test", ""))) {
//                    // 单数 选择a
//                    MobclickAgent.onEvent(mContext, "you_choose_a_test");
//                    layout_a_style.setVisibility(View.VISIBLE);
//                    layout_b_style.setVisibility(View.GONE);
//                    Util.setErrorLog(TAG,"----启动后 已选定了 -->>> a 测<<<------");
//                }
//            }
//        }

        MobclickAgent.onEvent(mContext, "you_choose_b_test");
        layout_a_style.setVisibility(View.GONE);
        layout_b_style.setVisibility(View.VISIBLE);
    }

    private void initJingXuanView() {
        layout_a_style = (LinearLayout) findViewById(R.id.layout_a_style);
        layout_b_style = (LinearLayout) findViewById(R.id.layout_b_style);

        rel_local_a = (RelativeLayout) findViewById(R.id.rel_local_a);
        rel_local_a.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                goProjectPictActivity("local");
            }
        });
        rel_style_a = (RelativeLayout) findViewById(R.id.rel_style_a);
        rel_style_a.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                goProjectPictActivity("style");
            }
        });
        rel_space_a = (RelativeLayout) findViewById(R.id.rel_space_a);
        rel_space_a.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                goProjectPictActivity("space");
            }
        });
        rel_home_sytle_a = (RelativeLayout) findViewById(R.id.rel_home_sytle_a);
        rel_home_sytle_a.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                goProjectPictActivity("home_sytle");
            }
        });
        rel_special_a = (RelativeLayout) findViewById(R.id.rel_special_a);
        rel_special_a.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                goProjectPictActivity("special");
            }
        });


        home_local = (LinearLayout) findViewById(R.id.home_local);
        home_local.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                goProjectPictActivity("local");
            }
        });
        home_style = (LinearLayout) findViewById(R.id.home_style);
        home_style.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // 风格欣赏
                goProjectPictActivity("style");
            }
        });
        home_space = (LinearLayout) findViewById(R.id.home_space);
        home_space.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                goProjectPictActivity("space");
            }
        });
        home_house_style = (LinearLayout) findViewById(R.id.home_house_style);
        home_house_style.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // 户型设计
                goProjectPictActivity("home_sytle_design");
            }
        });
        home_specail = (LinearLayout) findViewById(R.id.home_specail);
        home_specail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                goProjectPictActivity("special");
            }
        });
    }

    private void goProjectPictActivity(String type) {
        Intent it = new Intent(mContext, ProjectPictActivity.class);
        Bundle b = new Bundle();
        if ("local".equals(type)) {
            b.putString("type", type);
        } else if ("style".equals(type)) {
            b.putString("type", type);
        } else if ("space".equals(type)) {
            b.putString("type", type);
        } else if ("home_sytle_design".equals(type)) {
            b.putString("type", type);
        } else if ("special".equals(type)) {
            b.putString("type", type);
        }
        it.putExtra("JingXuanBundle", b);
        startActivity(it);
    }


//	/**
//	 * 精选图接口  【先保留  2017-01-07】
//	 */
//	private void operGetSpecial() {
//		getSpecialParams = AppInfoUtil.getPublicParams(this);
////		Log.d(TAG, "--HomeActivity--");
//		HttpServer.getInstance().requestPOST(getSpecialUrl, getSpecialParams, new AsyncHttpResponseHandler() {
//
//					@Override
//					public void onSuccess(int arg0, Header[] arg1, byte[] body) {
//						String result = new String(body);
//						getSharedPreferences("SpecialCache", 0).edit().putString("result", result).commit();
//						parseData(result);
//					}
//
//					@Override
//					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
//
//					}
//				});
//	}
//
//	// 解析精选json
//	private void parseData(String result) {
//		try {
//			JSONObject jsonObject = new JSONObject(result);
//			if (jsonObject.getInt("error_code") == 0) {
//				JSONArray data = jsonObject.getJSONArray("data");
//				ivHomeFullySelectedImages.setScaleType(ScaleType.FIT_XY);
//				//首页精选图一张
//				ImageLoaderUtil.loadImage(this, ivHomeFullySelectedImages, data.getJSONObject(0).getString("thumb_img_url"));
//				banner_title = data.getJSONObject(0).getString("banner_title");
//				id = data.getJSONObject(0).getString("id");
//				banner_textview_title.setText(banner_title);
//				if (data.getJSONObject(0).getString("thumb_img_url") != null) {
//					ivHomeFullySelectedImages.setClickable(true);
//				}
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}


    /***
     * 展示装修课堂
     */
    private void initDecorateCalss() {
        LinearLayout.LayoutParams decorateClassParams = (android.widget.LinearLayout.LayoutParams) decorateClassScrollView.getLayoutParams();
        decorateClassParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        decorateClassParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        decorateClassScrollView.setLayoutParams(decorateClassParams);

        FrameLayout.LayoutParams frameParams = (android.widget.FrameLayout.LayoutParams) ll_decorate_class_gallery.getLayoutParams();
        frameParams.height = DensityUtil.dip2px(this, 110);
        frameParams.width = DensityUtil.dip2px(this, 95) * 6;
        decorate_class_gridview.setColumnWidth(DensityUtil.dip2px(this, 88));
        ll_decorate_class_gallery.setLayoutParams(frameParams);
        homeDecorateClassAdapter = new HomeDecorateClassAdapter(HomeActivity.this);
        decorate_class_gridview.setAdapter(homeDecorateClassAdapter);
    }


    /**
     * 装修课堂接口
     */
    private void requestDecorationClass() {
        decorationClassParams = AppInfoUtil.getPublicParams(this);
        HttpServer.getInstance().requestPOST(decorationClassUrl, decorationClassParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] body) {
                String result = new String(body);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("error_code") == 0) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        decorationClassDatas.add(data.getString("qianqi"));
                        decorationClassDatas.add(data.getString("zhongqi"));
                        decorationClassDatas.add(data.getString("houqi"));
                        decorationClassDatas.add(data.getString("xuancai"));
                        decorationClassDatas.add(data.getString("sheji"));
                        decorationClassDatas.add(data.getString("fengshui"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_head:
                // 啥都不用做
                break;
            case R.id.layout_price:
                //走智能报价   走安卓发单接口
                MobclickAgent.onEvent(mContext, "click_index_book_decoration");
                Intent intent = new Intent(mContext, FreeDesign.class);
//			Intent intent = new Intent(mContext, FreeYuyueActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_calculater:
                // 计算器
                MobclickAgent.onEvent(mContext, "click_index_decoration_com");
                startActivity(new Intent(HomeActivity.this, CalculaterActivity.class));
                break;
            case R.id.layout_homeactivity_yuyue_decoration:
                // 走设计与报价 走安卓发单接口

                //MobclickAgent 友盟统计
                MobclickAgent.onEvent(mContext, "click_index_design_quote");
//                Intent freeIntent = new Intent(mContext, FreeActivity.class);
                Intent freeIntent = new Intent(mContext, GetPriceActivity.class);
                startActivity(freeIntent);

                break;
            case R.id.layout_decorate_myhouse:
                // 跳转html
//			http://m.tobosu.com/mt
                startActivity(new Intent(HomeActivity.this, ZhuangActivity.class));
                break;
            case R.id.home_decorate_class_all:
                MobclickAgent.onEvent(mContext, "click_index_decoration_com_total");
                Intent detailIntent = new Intent(mContext, WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("link", Constant.POP_URL + Constant.M_POP_PARAM + Constant.WANGJIANLIN);
                detailIntent.putExtras(bundle);
                startActivity(detailIntent);
                break;
            case R.id.tv_home_choose_city:
            case R.id.iv_home_select_city:
                Intent selectCityIntent = new Intent(mContext, SelectCtiyActivity.class);
                Bundle cityBundle = new Bundle();
                cityBundle.putString("fromHomeActivity", "101");
                selectCityIntent.putExtra("HomeActivitySelectcityBundle", cityBundle);
                startActivityForResult(selectCityIntent, 3);

//                Intent selectCityIntent = new Intent(mContext, SelectCtiyActivity.class);
//                selectCityIntent.putExtra("isSelectCity", true);
//                startActivityForResult(selectCityIntent, 3);
                break;
            case R.id.tv_home_project_more:
                //点击 精选图册 更多 按钮
                Intent it = new Intent(mContext, ProjectPictActivity.class);
                Bundle b = new Bundle();
                b.putString("type", "all");
                it.putExtra("JingXuanBundle", b);
                startActivity(it);
                break;

            default:

                break;
        }
    }

    /**
     * 顶部标题栏的透明度监控
     */
    private OnScrollChangedListener scrollChangedListener = new OnScrollChangedListener() {

        @Override
        public void onScrollChanged(ScrollView who, int x, int y, int oldx, int oldy) {
            if (y > fadingHeight) {
                y = fadingHeight;
            }
            drawable.setAlpha(y * (END_ALPHA - START_ALPHA) / fadingHeight + START_ALPHA);
            if (y >= END_ALPHA) {
                home_top_logo.setVisibility(View.VISIBLE);
            } else {
                home_top_logo.setVisibility(View.GONE);
            }

        }
    };

    /***
     * 百度定位初始化
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);
        option.setCoorType(tempcoor);

        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIgnoreKillProcess(true);

        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //重新选择城市 重新加载网络加载数据
        if (requestCode == 3) {
            if(data!=null && data.getBundleExtra("city_bundle") != null){
                cityName = data.getBundleExtra("city_bundle").getString("ci");
                saveCitySharePre = this.getSharedPreferences("Save_City_Info", MODE_PRIVATE);//将城市保存在Save_City_Info.xml文件中
                Editor editor = saveCitySharePre.edit();
                editor.putString("save_city_now", cityName);
                editor.commit();
                AppInfoUtil.setCityName(mContext, cityName);
            }
            home_city.setText(cityName);

            Intent selectCityIntent = new Intent(Constant.ACTION_HOME_SELECT_CITY);
            Bundle b = new Bundle();
            b.putString("city_selected", cityName);
            selectCityIntent.putExtra("f_select_city_bundle",b);
            sendBroadcast(selectCityIntent);

            getSharedPreferences("city", 0).edit().putString("cityName", cityName).commit();
            reSelectCityForLocalDiscount(cityName);
            homeTopBanner = new HomeTopFrameLayout(HomeActivity.this);
//			if (!TextUtils.isEmpty(cityName)) {
//				getSharedPreferences("city", 0).edit().putString("cityName", cityName).commit();
//				requestLocalDiscountData();
//			}
        }
    }

    private HomeTopFrameLayout homeTopBanner;

    /***
     * 重新选择城市获取本地优惠
     *
     * @param city
     */
    private void reSelectCityForLocalDiscount(String city) {

        localDiscountParams = initLocalDiscountParam(cityName);
        localDiscountList.clear();
        if (liearlayout_local_discount.getChildCount() > 0 && !"".equals(city)) {
            for (int i = 0; i < liearlayout_local_discount.getChildCount(); i++) {
                liearlayout_local_discount.removeViewAt(i);
            }
        }
//		requestLocalDiscountData();
    }



    private String activityId;
    private String activityImg_url;
    private String activityH5_url;
    private String activityType;
    private String activityName;

    /**
     * 获取活动信息
     */
    private void getHuoDongPicture(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String date = sdf.format(new Date());

        if(!"".equals(CacheManager.getLoadingHUODONG(mContext)) && date.equals(CacheManager.getLoadingHUODONG(mContext))){
            // 已经保存过当天日期

        }else {
            // 这是当天第一次
            if(Util.isNetAvailable(HomeActivity.this)){
                HashMap<String, String> hashMap = new HashMap<String, String>();
                OKHttpUtil.post(Constant.ACTIVITY_URL, hashMap, new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String json = response.body().string();
                        Util.setErrorLog(TAG, json);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = new JSONObject(json);
                                    if(jsonObject.getInt("error_code") == 0){
                                        JSONObject data = jsonObject.getJSONObject("data");
                                        activityId = data.getString("id");
                                        activityImg_url = data.getString("img_url");
                                        activityH5_url = data.getString("h5_url");
                                        activityType = data.getString("type");
                                        activityName = data.getString("name");
//                                    String picUrl = activityImg_url.replace("\\/\\/", "\\");
                                        CacheManager.setLoadingHUODONG(mContext, date);
                                        showTap(activityImg_url, activityH5_url);

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    /**
     * 显示蒙层
     * @param adUrl 是否显示蒙层
     */
    private void showTap(String adUrl, final String h5Url){
        if(!"".equals(adUrl)){
            final Dialog dialog = new Dialog(HomeActivity.this, R.style.popupDialog);
            View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.layout_home_tab_layout, null);

            Display display = this.getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();
            //设置dialog的宽高为屏幕的宽高
            ViewGroup.LayoutParams layoutParams = new  ViewGroup.LayoutParams(width, height);
            dialog.setContentView(view, layoutParams);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            ImageView adIv = (ImageView) dialog.findViewById(R.id.iv_main_ad);
            ImageView adIvClose = (ImageView) dialog.findViewById(R.id.iv_main_ad_close);
            Glide.with(HomeActivity.this).load(adUrl).into(adIv);
            Util.setErrorLog(TAG, adUrl);
            adIvClose.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dialog!=null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });

            adIv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, WebViewActivity.class);
                    Bundle b = new Bundle();
                    b.putString("link", h5Url);
                    it.putExtras(b);
                    startActivity(it);
                    dialog.dismiss();
                }
            });

            if(dialog!=null && !dialog.isShowing()) {
                dialog.show();
            }
        }else {

        }
    }
}
