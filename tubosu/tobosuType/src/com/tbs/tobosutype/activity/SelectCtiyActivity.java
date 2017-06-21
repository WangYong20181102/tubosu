package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.okhttp.Request;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.CityAdapter;
import com.tbs.tobosutype.customview.BladeView;
import com.tbs.tobosutype.customview.BladeView.OnItemClickListener;
import com.tbs.tobosutype.customview.FirstGridView;
import com.tbs.tobosutype.customview.PinnedHeaderListView;
import com.tbs.tobosutype.customview.SwitchCityDialog;
import com.tbs.tobosutype.global.AllConstants;
import com.tbs.tobosutype.global.MyApplication;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.model.City;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.CacheManager;
import com.tbs.tobosutype.utils.HttpServer;
import com.tbs.tobosutype.utils.LogUtil;
import com.tbs.tobosutype.utils.MD5Util;
import com.tbs.tobosutype.utils.NetUtil;
import com.tbs.tobosutype.utils.ToastUtil;
import com.tbs.tobosutype.utils.Util;

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
import java.util.Map;

/**
 * 选择城市 页
 *
 * @author dec
 */
public class SelectCtiyActivity extends Activity implements OnClickListener, MyApplication.EventHandler {
    private static final String TAG = SelectCtiyActivity.class.getSimpleName();
    private String DOWNLOAD_COUNT_URL = AllConstants.TOBOSU_URL + "tapp/DataCount/download_count";
    /**
     * 首次安装
     */
    private static final String FIRST_INSTALL = "5";

    private Context context;
    private View mSearchContainer;

    private PinnedHeaderListView mCityListView;

    private SharedPreferences selectCitySP;

    private boolean isFirstSelectCity = false;

    /***右边字母列表view*/
    private BladeView mLetter;

    private ListView mSearchListView;
    private List<City> mCities;
    private CityAdapter mCityAdapter;
    private List<String> mSections;
    private Map<String, List<City>> mMap;
    private List<Integer> mPositions;
    private Map<String, Integer> mIndexer;
    private MyApplication mApplication;

    /**
     * 热门城市gridview
     */
    private FirstGridView mCity_gridView;

    /***定位中的提示信息*/
    private TextView select_positioning;

    /***退出或返回上一层*/
    private ImageView city_title_back;

    private View headView;

    /***选择城市正在加载网络时的布局*/
    private LinearLayout select_loading;

    /***所有城市接口*/
    private String cityUrl = AllConstants.TOBOSU_URL + "tapp/util/change_city";

    private List<String> hotCityNames = new ArrayList<String>();
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient = null;
    private MyBDLocationListner mListner = null;
    private BitmapDescriptor mCurrentMarker = null;
    private GeoCoder mGeoCoder = null;
    private double mLantitude;
    private double mLongtitude;
    private LatLng mLoactionLatLng;
    private boolean isFirstLoc = true;
    private Point mCenterPoint = null;
    private Double lat;
    private Double lng;

    /**
     * 定位得到的真实城市地理地址
     */
    private String realLocationCity = "";

    /**
     * 保存本地的城市参数
     */
    private SharedPreferences citySharePre;

    /**
     * 选择城市页面的所有布局
     */
    private LinearLayout select_city_layout;

    private LinearLayout select_city_activity_netoutview;


    private String MAC_CODE = "";
    private String _TOKEN = "";


    /**
     * 获取手机mac地址<br/>
     * 错误返回12个0
     */
    public static String getMacAddress(Context context) {
        // 获取mac地址：
        String macAddress = "000000000000";
        try {
            WifiManager wifiMgr = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = (null == wifiMgr ? null : wifiMgr
                    .getConnectionInfo());
            if (null != info) {
                if (!TextUtils.isEmpty(info.getMacAddress()))
                    macAddress = info.getMacAddress().replace(":", "");
                else
                    return macAddress;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return macAddress;
        }
        return macAddress;
    }


    private void getSetting(){
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        MAC_CODE = getMacAddress(context);
        _TOKEN = MD5Util.md5(MD5Util.md5(MAC_CODE+1)+date);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = SelectCtiyActivity.this;
        getSetting();
        initView();
        initData();
        initEvent();

    }

    private String fromFindDecorateCompany = "";
    private String fromFreeDesign = "";

    private int from = -1;

    private String fromHome = "";

    private void initView() {
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_selectcity);

        if (getIntent() != null && getIntent().getBundleExtra("pop_bundle") != null) {
            Bundle b = getIntent().getBundleExtra("pop_bundle");
            from = b.getInt("frompop");
            city_title_back = (ImageView) findViewById(R.id.city_title_back);

            if (from == 31) {
                city_title_back.setVisibility(View.INVISIBLE);
            } else {
                city_title_back.setVisibility(View.VISIBLE);
            }
        }


        if (getIntent() != null && getIntent().getBundleExtra("freeDesignBundle") != null) {
            Bundle b = getIntent().getBundleExtra("freeDesignBundle");
            fromFreeDesign = b.getString("fromFreeDesign");
            city_title_back = (ImageView) findViewById(R.id.city_title_back);
            city_title_back.setVisibility(View.VISIBLE);
        }


//        b.putString("fromHomeActivity", "101");
//        selectCityIntent.putExtra("HomeActivitySelectcityBundle", b);

        if (getIntent() != null && getIntent().getBundleExtra("HomeActivitySelectcityBundle") != null) {
            Bundle b = getIntent().getBundleExtra("HomeActivitySelectcityBundle");
            fromHome = b.getString("fromHomeActivity");
            city_title_back = (ImageView) findViewById(R.id.city_title_back);
            city_title_back.setVisibility(View.VISIBLE);
        }


        if (getIntent() != null && getIntent().getBundleExtra("findDecorateCompanySelectcityBundle") != null) {
            Bundle b = getIntent().getBundleExtra("findDecorateCompanySelectcityBundle");
            fromFindDecorateCompany = b.getString("fromFindCompany");
            city_title_back = (ImageView) findViewById(R.id.city_title_back);
            city_title_back.setVisibility(View.VISIBLE);
        }


        SDKInitializer.initialize(getApplicationContext());
        mMapView = new MapView(SelectCtiyActivity.this);
        headView = getLayoutInflater().inflate(R.layout.head_view_select_city, null);
        mCity_gridView = (FirstGridView) headView.findViewById(R.id.city_gridView);
        select_loading = (LinearLayout) findViewById(R.id.ll_loading);
        select_positioning = (TextView) headView.findViewById(R.id.select_positioning);
        city_title_back = (ImageView) findViewById(R.id.city_title_back);
        city_title_back.setOnClickListener(this);
        mSearchContainer = findViewById(R.id.search_content_container);
        mCityListView = (PinnedHeaderListView) findViewById(R.id.pinned_header_citys_list);
        mCityListView.addHeaderView(headView);
        mCityListView.setEmptyView(findViewById(R.id.citys_list_empty));

        select_city_layout = (LinearLayout) findViewById(R.id.select_city_layout);
        select_city_activity_netoutview = (LinearLayout) findViewById(R.id.select_city_activity_netoutview);

        mLetter = (BladeView) findViewById(R.id.citys_bladeview);

        mLetter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(String s) {
                String key = s;
                if (mIndexer.get(key) != null) {
                    mCityListView.setSelection(mIndexer.get(key).intValue() + 1);
                }
            }
        });

        mLetter.setVisibility(View.GONE);
        mSearchListView = (ListView) findViewById(R.id.getcity_listview);
        mSearchListView.setEmptyView(findViewById(R.id.search_empty));
        mSearchContainer.setVisibility(View.GONE);

        mSearchListView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        mCityListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityString = (mCityAdapter.getItem(position - 1)).getNm();
                if (cityString.contains("市") || cityString.contains("县")) {
                    cityString = cityString.substring(0, cityString.length() - 1);
                }
                startActivityWithCity(cityString);
            }
        });

        initBDMap();
    }

    private void initBDMap() {
        try {
            mMapView.showZoomControls(false);
            mBaiduMap = mMapView.getMap();
            MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(17.0f);
            mBaiduMap.setMapStatus(msu);
            mBaiduMap.setMyLocationEnabled(true);
            mLocationClient = new LocationClient(this);
            mListner = new MyBDLocationListner();

            mLocationClient.registerLocationListener(mListner);
            LocationClientOption option = new LocationClientOption();
            option.setOpenGps(true);
            option.setCoorType("bd09ll");
            option.setScanSpan(1000);
            mLocationClient.setLocOption(option);
            mLocationClient.start();
            mGeoCoder = GeoCoder.newInstance();
            mGeoCoder.setOnGetGeoCodeResultListener(GeoListener);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initData() {

        initNetMessage();

        selectCitySP = context.getSharedPreferences("First_Select_City", MODE_PRIVATE);
        isFirstSelectCity = selectCitySP.getBoolean("first_select_city", true);
        if (isFirstSelectCity) {
            Editor editor = selectCitySP.edit();
            editor.putBoolean("first_select_city", false); // 首次进入,将不是首次进入，则设置为false
            editor.commit();
        }


        MyApplication.mListeners.add(this);
        mApplication = MyApplication.getInstance();

        //TODO
        if (mApplication.isCityListComplite()) {
            mCities = mApplication.getCityList();
            mSections = mApplication.getSections();
            mMap = mApplication.getMap();
            mPositions = mApplication.getPositions();
            mIndexer = mApplication.getIndexer();
            mCityAdapter = new CityAdapter(SelectCtiyActivity.this, mCities, mMap, mSections, mPositions);
            mCityListView.setAdapter(mCityAdapter);
            mCityListView.setOnScrollListener(mCityAdapter);
            mLetter.setVisibility(View.VISIBLE);
        }
        // 缓存至本地
        String result = getSharedPreferences("selectCityCache", 0).getString("result", "");
//		Log.d("--SelectCityActivity--", "获取本地缓存的城市列表数据【"+result+"】");
        if (TextUtils.isEmpty(result)) {
            // 如果缓存的是空，则需要请求网络，获取城市列表
            requestSelectCity();
        } else {
            // 缓存不为空， 则解析本地缓存，而获取城市集合
            prseSelectDataJson(result);
        }

        mCity_gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        // 热门城市跳转
        mCity_gridView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String hot = hotCityNames.get(position);
                if (hot.contains("市") || hot.contains("县")) {
                    hot = hot.substring(0, hot.length() - 1);
                }
                startActivityWithCity(hot);
            }
        });
    }

    /***
     * 请求所有城市的接口
     */
    private void requestSelectCity() {
        HttpServer.getInstance().requestPOST(cityUrl, null, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {

                String result = new String(responseBody);
//						Log.d("--SelectCityActivity--", "获取的城市列表数据【"+result+"】");
                LogUtil.printDugLog("--SelectCityActivity--", result);
                //缓存至本地
                getSharedPreferences("selectCityCache", 0).edit().putString("result", result).commit();
                prseSelectDataJson(result);
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

            }
        });
    }

    /***
     * 解析所有城市
     * @param result
     */
    private void prseSelectDataJson(String result) {
        try {
            JSONObject object = new JSONObject(result);
            if (object.getInt("error_code") == 0) {
                JSONArray array = object.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject dataObject = array.getJSONObject(i);
                    String _cityName = dataObject.getString("simpname");
                    String hotFlag = dataObject.getString("hot_flag");
                    if (hotFlag.equals("1")) {
                        hotCityNames.add(_cityName);
                    }
                }

                loadCityGridView();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /****
     * 加载热门城市gridview
     */
    private void loadCityGridView() {
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < hotCityNames.size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("style", hotCityNames.get(i));
            list.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.item_city_gridview, new String[]{"style"}, new int[]{R.id.item_city_tv});
        mCity_gridView.setAdapter(adapter);
        select_loading.setVisibility(View.GONE);
    }

    /**
     * 获取城市， 跳转
     *
     * @param city
     */
    private void startActivityWithCity(final String city) {

        getSharedPreferences("Save_City_Info", MODE_PRIVATE).edit().putString("save_city_now", city).commit(); //将城市保存在Save_City_Info.xml文件中

        //保存经纬度
        getSharedPreferences("city", 0).edit().putString("lat", lat + "").commit();
        getSharedPreferences("city", 0).edit().putString("lng", lng + "").commit();


//		showWarnText(realLocationCity, cityFromClick);
        if (realLocationCity.contains("市") || realLocationCity.contains("县")) {
            realLocationCity = realLocationCity.substring(0, realLocationCity.length() - 1);
        }

        if (!"".equals(realLocationCity) && !realLocationCity.equals(city)) {
            SwitchCityDialog.Builder builder = new SwitchCityDialog.Builder(this);
            builder.setMessage("您当前定位在" + realLocationCity + "， 是否切换到" + city + "？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            confirmCity(city);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

            builder.create().show();
        } else {
            confirmCity(city);
        }

    }


    private void confirmCity(String cityName) {

        getSharedPreferences("city", Context.MODE_PRIVATE).edit().putString("cityName", cityName).commit();

        if (from == 31) {
            // 来自poporder页面
            Intent cityData = new Intent();
            Bundle b = new Bundle();
            b.putString("ci", cityName);
            cityData.putExtra("city_bundle", b);
            setResult(77, cityData);
            finish();
            return;
        }


        if (fromFreeDesign.equals("66")) {
            // 来自智能報價页面
            Intent cityData = new Intent();
            Bundle b = new Bundle();
            b.putString("ci", cityName);
            cityData.putExtra("city_bundle", b);
            setResult(39, cityData);
            finish();
            return;
        }


        if(fromFindDecorateCompany.equals("64")){
            // 来自找装修公司页面
            Intent cityData = new Intent();
            Bundle b = new Bundle();
            b.putString("ci", cityName);
            cityData.putExtra("city_bundle", b);
            setResult(644, cityData);
            finish();
            return;
        }


        if (from == 34) {
            // 来自智能發單页面
            Intent cityData = new Intent();
            Bundle b = new Bundle();
            b.putString("ci", cityName);
            cityData.putExtra("city_bundle", b);
            setResult(78, cityData);
            finish();
            return;
        }


        //getIntent().getBooleanExtra("isSelectCity", false) == true 是指从首页选择城市 跳转过来重新选择城市
        // 													 == false 是指第一次安装后从欢迎页面过来选择城市
//        if (getIntent().getBooleanExtra("isSelectCity", false)) {
//            Intent intent = getIntent();
//            intent.putExtra("result", cityName);
//            this.setResult(0, intent);
//            finish();
//        } else {

        if(fromHome.equals("101")){
            Intent it = new Intent();
            Bundle b = new Bundle();
            b.putString("ci", cityName);
            it.putExtra("city_bundle", b);
            setResult(104, it);
            finish();
        }else{

            // 首次安装
            if (FIRST_INSTALL.equals(getSharedPreferences("Go_PopOrderActivity_SP", Context.MODE_PRIVATE).getString("go_poporder_string", "0"))) {
                countDownloadNum();
                // FIRST_INSTALL.equals(getSharedPreferences("Go_PopOrderActivity_SP", Context.MODE_PRIVATE).getString("go_poporder_string", "0"))
                // 改为不是 FIRST_INSTALL
//				getSharedPreferences("Go_PopOrderActivity_SP", Context.MODE_PRIVATE).edit().putString("go_poporder_string", "3").commit();
                startActivity(new Intent(context, PopOrderActivity.class));
            } else {
                // 进入选择装修类型和面积发单入口
                startActivity(new Intent(context, MainActivity.class));
                Log.d(TAG, "--直接进入 MainActivity 页面--");
            }
            finish();
        }
    }


    /***
     * 首次安装调用
     */
    private void countDownloadNum(){
        OKHttpUtil client = new OKHttpUtil();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("mac_code", MAC_CODE);
        map.put("type","1");
        map.put("_token", _TOKEN);
        client.post(DOWNLOAD_COUNT_URL, map, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(com.squareup.okhttp.Response response, String json) {
                Util.setLog(TAG, "songchengcai >>>"+json);
            }

            @Override
            public void onFail(Request request, IOException e) {
                e.printStackTrace();
                Util.setLog(TAG, "songchengcai >>>onFail");
            }

            @Override
            public void onError(com.squareup.okhttp.Response response, int code) {
                Util.setLog(TAG, code+"<<<<songchengcai <<<<");
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.city_title_back:

                if(fromFreeDesign.equals("66")){
                    // 来自 智能报价
                    finish();
                    return;
                }

                if(fromHome.equals("101")){
                    // 来自 首页
                    finish();
                    return;
                }

                if(fromFindDecorateCompany.equals("64")){
                    // 来自找装修公司页面
                    finish();
                    return;
                }

                operEdit();

            default:
                break;
        }

    }

    /**
     * 选择城市
     */
    private void operEdit() {
//        if (getIntent().getBooleanExtra("isSelectCity", false)) {
//            Intent intent = getIntent();
//            intent.putExtra("result", "");
//            this.setResult(0, intent);
//        } else {
//			startActivity(new Intent(context, PopOrderActivity.class)); // 应该跳转 PopOrderActivity 原来是MainActivity
            if (FIRST_INSTALL.equals(getSharedPreferences("Go_PopOrderActivity_SP", Context.MODE_PRIVATE).getString("go_poporder_string", "0"))) {
                startActivity(new Intent(context, PopOrderActivity.class));
            } else {
                // 进入选择装修类型和面积发单入口
                startActivity(new Intent(context, MainActivity.class));
                Log.d(TAG, "--直接进入 MainActivity 页面--");
            }
//        }
        finish();
    }

    @Override
    public void onBackPressed() {
        operEdit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mGeoCoder.destroy();
        MyApplication.mListeners.remove(this);
    }

    @Override
    public void onCityComplite() {
        // 城市列表加载完的回调函数
        mCities = mApplication.getCityList();
        mSections = mApplication.getSections();
        mMap = mApplication.getMap();
        mPositions = mApplication.getPositions();
        mIndexer = mApplication.getIndexer();

        mCityAdapter = new CityAdapter(SelectCtiyActivity.this, mCities, mMap, mSections, mPositions);
        mLetter.setVisibility(View.VISIBLE);
        mCityListView.setAdapter(mCityAdapter);
        mCityListView.setOnScrollListener(mCityAdapter);
    }

    /***
     * 百度地图地位监听
     *
     * @author dec
     *
     */
    private class MyBDLocationListner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mMapView == null)
                return;
            MyLocationData data = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(data);

            MyLocationConfiguration config = new MyLocationConfiguration(LocationMode.NORMAL, true, null);
            mBaiduMap.setMyLocationConfigeration(config);
            mLantitude = location.getLatitude();
            mLongtitude = location.getLongitude();
            LatLng currentLatLng = new LatLng(mLantitude, mLongtitude);
            mLoactionLatLng = new LatLng(mLantitude, mLongtitude);

            if (isFirstLoc) {
                isFirstLoc = false;
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(currentLatLng);
                mBaiduMap.animateMapStatus(u);

                mGeoCoder.reverseGeoCode((new ReverseGeoCodeOption()).location(currentLatLng));
                return;
            }
        }

    }

    /**
     * 地理位置信息监听
     */
    OnGetGeoCoderResultListener GeoListener = new OnGetGeoCoderResultListener() {

        @Override
        public void onGetGeoCodeResult(GeoCodeResult result) {

        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {

            } else {
                String address = result.getAddress();
                realLocationCity = result.getAddressDetail().city;
                lat = result.getLocation().latitude;
                lng = result.getLocation().longitude;

                if (address != null) {
                    select_positioning.setText(realLocationCity);
                    // 首次进入则定位后 自动跳转  非首次 则不用自动跳入
                    if (selectCitySP.getBoolean("first_select_city", false)) {
                        operEdit();
                    }
                } else {
                    select_positioning.setText("定位失败！");
                }
            }
        }
    };

    public void positioning(View v) {

    }

    public void turnBack(View view) {
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(mLoactionLatLng);
        mBaiduMap.animateMapStatus(u);

        mBaiduMap.clear();
        mGeoCoder.reverseGeoCode((new ReverseGeoCodeOption()).location(mLoactionLatLng));
    }

    @Override
    public void onNetChange() {
        if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE)
            ToastUtil.showLong(this, R.string.net_err);
    }

    private void initEvent() {
        select_positioning.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (select_positioning.getText().equals("定位失败！") || select_positioning.getText().equals("定位中……")) {

                } else {
                    // 定位成功

                    if (realLocationCity.contains("市") || realLocationCity.contains("县")) {
                        realLocationCity = realLocationCity.substring(0, realLocationCity.length() - 1);
                    }

                    startActivityWithCity(realLocationCity);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK && fromFindDecorateCompany.equals("64")){
            // 来自 找公司
            finish();
            return true;
        }


        if(keyCode == KeyEvent.KEYCODE_BACK && fromFreeDesign.equals("66")){
            // 来自 智能报价
            finish();
            return true;
        }


        if(keyCode == KeyEvent.KEYCODE_BACK && fromHome.equals("101")){
            // 来自 首页城市选择
            finish();
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK && "welcome".equals(CacheManager.getPageFlag(context))) {
            // 来自welcome
            CacheManager.setPageFlag(context, "not_welcome");
            startActivity(new Intent(context, PopOrderActivity.class));
            //现在无法获取，则标记为 1 ，即在首再次显示让用户获取
            getSharedPreferences("Cancel_Get_Design", Context.MODE_PRIVATE).edit().putInt("cancel_String", 1).commit();
            finish();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK && FIRST_INSTALL.equals(getSharedPreferences("Go_PopOrderActivity_SP", Context.MODE_PRIVATE).getString("go_poporder_string", "0"))) {
            // 首次安装app
            Intent intent = new Intent();
            intent.setClass(context, MainActivity.class);
            Bundle b = new Bundle();
            b.putString("first_install_string", "10");
            intent.putExtra("first_install_bundle", b);
            startActivity(intent);
            finish();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK && "pop_order".equals(CacheManager.getPageFlag(context))) {
            // 来自发单页面的选择城市
            if (16 == CacheManager.getPopFlag(context)) {
                // 已经发过单
            } else {
                // 默认==19 未发过单
                // 保证首页能够出现发单按钮
                getSharedPreferences("Cancel_Get_Design", Context.MODE_PRIVATE).edit().putInt("cancel_String", 1).commit();
            }

            finish();
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initNetMessage();
    }


    private void initNetMessage() {
        Message m = new Message();
        if (checkNetState()) {
            m.what = 0x000012;
        } else {
            m.what = 0x000013;
        }
        netHandler.sendMessage(m);
    }


    private Handler netHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x000012: //有网络
                    select_city_layout.setVisibility(View.VISIBLE);
                    select_city_activity_netoutview.setVisibility(View.GONE);
                    break;
                case 0x000013: // 无网络
                    select_city_layout.setVisibility(View.GONE);
                    select_city_activity_netoutview.setVisibility(View.VISIBLE);
                    select_city_activity_netoutview.findViewById(R.id.tv_reload_data).setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (!checkNetState()) {
                                Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                initData();
                            }
                        }
                    });
                    break;
                default:
                    break;
            }
        }

        ;
    };

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
}
