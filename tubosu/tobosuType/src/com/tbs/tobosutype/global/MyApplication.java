package com.tbs.tobosutype.global;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.model.City;
import com.tbs.tobosutype.utils.CityData;
import com.tbs.tobosutype.utils.NetUtil;
import com.tbs.tobosutype.utils.SharePreferenceUtil;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends android.app.Application {
    public static String iconUrl;
    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    public TextView mLocationResult;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    private static final int CITY_LIST_SCUESS = 0;
    private static MyApplication mApplication;
    private static Context context;
    private SharePreferenceUtil mSpUtil;
    private CityData mCityData;
    private List<City> mCityList;
    private List<City> hotCityList;
    private boolean isCityListComplite;
    private static final String FORMAT = "^[a-z,A-Z].*$";
    private static String NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    public static ArrayList<EventHandler> mListeners = new ArrayList<EventHandler>();
    public static int mNetWorkState;
    public static int urrentItemFragment = 0;

    private List<String> mSections;
    private Map<String, List<City>> mMap;
    private List<Integer> mPositions;
    private Map<String, Integer> mIndexer;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case CITY_LIST_SCUESS:
                    isCityListComplite = true;
                    if (mListeners.size() > 0)
                        for (EventHandler handler : mListeners) {
                            handler.onCityComplite();
                        }
                    break;
                default:
                    break;
            }
        }
    };

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        SDKInitializer.initialize(this);
        //初始化友盟
        UMShareAPI.get(this);
        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        context = getApplicationContext();
        try {
            mLocationClient.registerLocationListener(mMyLocationListener);    //注册监听函数
        } catch (Exception e) {
            e.printStackTrace();
        }

        initData();

        initImageLoader(getApplicationContext());
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.decorate_detail_loading)
                .showImageForEmptyUri(R.drawable.decorate_detail_empty)
                .showImageOnFail(R.drawable.decorate_detail_failure)
                .cacheInMemory().cacheOnDisc().build();
        imageDetailsOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.decorate_detail_loading)
                .cacheInMemory().cacheOnDisc().build();
        orderFeedBackOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.app_icon)
                .showImageForEmptyUri(R.drawable.app_icon)
                .showImageOnFail(R.drawable.app_icon)
                .cacheInMemory().cacheOnDisc().build();

    }
    //平台信息配置
    {
        PlatformConfig.setWeixin("wx20c4f4560dcd397a", "9b06e848d40bcb04205d75335df6b814");
        PlatformConfig.setQQZone("1104958391", "M0L4G2G3SEgFNP35");
    }
    public static Context getContext() {
        return context;
    }

    private void initCityList() {
        mCityList = new ArrayList<City>();
        hotCityList = new ArrayList<City>();
        mSections = new ArrayList<String>();
        mMap = new HashMap<String, List<City>>();
        mPositions = new ArrayList<Integer>();
        mIndexer = new HashMap<String, Integer>();
        mCityData = new CityData(this);
        new Thread(new Runnable() {

            @Override
            public void run() {
                isCityListComplite = false;
                prepareCityList();
                mHandler.sendEmptyMessage(CITY_LIST_SCUESS);
            }
        }).start();
    }

    private boolean prepareCityList() {
        mCityList = mCityData.getAllCity();
        hotCityList = mCityData.getHotCity();
        for (City city : mCityList) {
            String firstName = city.getFirstPY();
            if (firstName.matches(FORMAT)) {
                if (mSections.contains(firstName)) {
                    mMap.get(firstName).add(city);
                } else {
                    mSections.add(firstName);
                    List<City> list = new ArrayList<City>();
                    list.add(city);
                    mMap.put(firstName, list);
                }
            } else {
                if (mSections.contains("#")) {
                    mMap.get("#").add(city);
                } else {
                    mSections.add("#");
                    List<City> list = new ArrayList<City>();
                    list.add(city);
                    mMap.put("#", list);
                }
            }
        }
        Collections.sort(mSections);
        int position = 0;
        for (int i = 0; i < mSections.size(); i++) {
            mIndexer.put(mSections.get(i), position);
            mPositions.add(position);
            position += mMap.get(mSections.get(i)).size();
        }

        return true;
    }

    public static abstract interface EventHandler {
        public abstract void onCityComplite();

        public abstract void onNetChange();
    }

    public static synchronized MyApplication getInstance() {
        return mApplication;
    }

    public synchronized SharePreferenceUtil getSharePreferenceUtil() {
        if (mSpUtil == null)
            mSpUtil = new SharePreferenceUtil(this, SharePreferenceUtil.CITY_SHAREPRE_FILE);
        return mSpUtil;
    }

    private void initData() {
        mApplication = this;
        mNetWorkState = NetUtil.getNetworkState(this);
        initCityList();
        mSpUtil = new SharePreferenceUtil(this, SharePreferenceUtil.CITY_SHAREPRE_FILE);
        IntentFilter filter = new IntentFilter(NET_CHANGE_ACTION);
        registerReceiver(netChangeReceiver, filter);
    }

    public synchronized CityData getCityData() {
        if (mCityData == null)
            mCityData = new CityData(this);
        return mCityData;
    }

    BroadcastReceiver netChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(NET_CHANGE_ACTION)) {
                if (mListeners.size() > 0)
                    for (EventHandler handler : mListeners) {
                        handler.onNetChange();
                    }
            }
            mNetWorkState = NetUtil.getNetworkState(mApplication);
        }

    };

    public boolean isCityListComplite() {
        return isCityListComplite;
    }

    public List<City> getCityList() {
        return mCityList;
    }

    public List<City> getAllHotCity(){
        return hotCityList;
    }

    public List<String> getSections() {
        return mSections;
    }

    public Map<String, List<City>> getMap() {
        return mMap;
    }

    public List<Integer> getPositions() {
        return mPositions;
    }

    public Map<String, Integer> getIndexer() {
        return mIndexer;
    }


    public static ImageLoader imageLoader = ImageLoader.getInstance();
    public static DisplayImageOptions options;
    public static DisplayImageOptions orderFeedBackOptions;
    public static String NEWVERSIONCODE;
    public static String DOWNLOADURL;
    public static boolean ISMUSTUPDATE;
    public static boolean ISPUSHLOOKORDER;
    public static DisplayImageOptions imageDetailsOptions;

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO) // Not
                .build();
        ImageLoader.getInstance().init(config);
    }


    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            StringBuffer sb = new StringBuffer(256);

            sb.append(location.getLatitude());
            sb.append(":");
            sb.append(location.getLongitude());
            sb.append(":");
            if (sb.toString().split(":").length > 2) {
                shared = getSharedPreferences("cityInfo", 0);
                editor = shared.edit();
                editor.putString("lat", sb.toString().split(":")[0]);
                editor.putString("lng", sb.toString().split(":")[1]);
                editor.putString("cityName", sb.toString().split(":")[2]);
                editor.commit();
                logMsg(sb.toString().split(":")[2]);
            }
        }

        public void logMsg(String str) {
            try {
                if (mLocationResult != null)
                    mLocationResult.setText(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}
