package com.tbs.tobosutype.activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.NewHomeAdapter;
import com.tbs.tobosutype.bean.NewHomeDataItem;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.CacheManager;
import com.tbs.tobosutype.utils.Util;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Lie on 2017/10/23.
 */

public class NewHomeActivity extends BaseActivity {
    private View home_view;
    private RelativeLayout rel_newhomebar;
    private RelativeLayout relSelectCity;
    private TextView newhomeCity;
    private String cityId;
    private String cityName;
    private RecyclerView recyclerView;
    private TextView tubosu;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private NewHomeAdapter newHomeAdapter;
    private boolean showAnli = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = NewHomeActivity.this;
        TAG = NewHomeActivity.class.getSimpleName();
        setContentView(R.layout.layout_new_activity);
        initView();
        initBaiduMap();
        cityId = "0";
        setClick();
        getDataFromNet();
        initReceiver();
    }

    private void initView(){
        home_view = (View)findViewById(R.id.home_view);
        tubosu = (TextView) findViewById(R.id.app_title_text);
        rel_newhomebar = (RelativeLayout) findViewById(R.id.rel_newhomebar);
        rel_newhomebar.setAlpha(0);
        relSelectCity = (RelativeLayout) findViewById(R.id.relSelectCity);
        newhomeCity = (TextView) findViewById(R.id.newhomeCity);
        recyclerView = (RecyclerView) findViewById(R.id.newhome_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.newhome_swiprefreshlayout);

        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //得到当前显示的最后一个item的view
                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount()-1);
                //得到lastChildView的bottom坐标值
                int lastChildBottom = lastChildView.getBottom();
                //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
                int recyclerBottom =  recyclerView.getBottom()-recyclerView.getPaddingBottom();
                //通过这个lastChildView得到这个view当前的position值
                int lastPosition  = recyclerView.getLayoutManager().getPosition(lastChildView);

                //判断lastChildView的bottom值跟recyclerBottom
                //判断lastPosition是不是最后一个position
                //如果两个条件都满足则说明是真正的滑动到了底部
                if(lastChildBottom == recyclerBottom && lastPosition == recyclerView.getLayoutManager().getItemCount()-1 ){
                    if(newHomeAdapter!=null){
                        newHomeAdapter.loadMoreData(true);
                    }
                }
            }
        });

        //初始化swipeRreshLayout
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(swipeLister);
        swipeRefreshLayout.setOnTouchListener(onTouchListener);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //设置其透明度
                float alpha = 0;
                int scollYHeight = getScollYHeight(true, tubosu.getHeight());

                int baseHeight = 574;
                if(scollYHeight >= baseHeight) {
                    alpha = 1;
                }else {
                    alpha = scollYHeight / (baseHeight*1.0f);
                    if(alpha>4){
                        home_view.setVisibility(View.INVISIBLE);
                    }
                }
                rel_newhomebar.setAlpha(alpha);
            }
        });
    }

    private int getScollYHeight(boolean hasHead, int headerHeight) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //获取到第一个可见的position,其添加的头部不算其position当中
        int position = layoutManager.findFirstVisibleItemPosition();
        //通过position获取其管理器中的视图
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        //获取自身的高度
        int itemHeight = firstVisiableChildView.getHeight();
        //有头部
        if(hasHead) {
            return headerHeight + itemHeight*position - firstVisiableChildView.getTop();
        }else {
            return itemHeight*position - firstVisiableChildView.getTop();
        }
    }


    //下拉刷新监听事件
    private SwipeRefreshLayout.OnRefreshListener swipeLister = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新数据 重新初始化各种数据
            swipeRefreshLayout.setRefreshing(false);
            getDataFromNet();
        }
    };

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //处于下拉刷新时列表不允许点击  死锁问题
            if (swipeRefreshLayout.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };

    private void setClick(){
        relSelectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectCityIntent = new Intent(mContext, SelectCtiyActivity.class);
                Bundle cityBundle = new Bundle();
                cityBundle.putString("fromHomeActivity", "101");
                selectCityIntent.putExtra("HomeActivitySelectcityBundle", cityBundle);
                startActivityForResult(selectCityIntent, 3);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //重新选择城市 重新加载网络加载数据
        if (requestCode == 3) {
            if(data!=null && data.getBundleExtra("city_bundle") != null){
                cityName = data.getBundleExtra("city_bundle").getString("ci");
                cityId = data.getBundleExtra("city_bundle").getString("cid");
                getSharedPreferences("Save_City_Info", MODE_PRIVATE).edit().putString("save_city_now", cityName).commit();
                AppInfoUtil.setCityName(mContext, cityName);
            }
            newhomeCity.setText(cityName);

            Intent selectCityIntent = new Intent(Constant.ACTION_HOME_SELECT_CITY);
            Bundle b = new Bundle();
            b.putString("city_selected", cityName);
            selectCityIntent.putExtra("f_select_city_bundle",b);
            sendBroadcast(selectCityIntent);
            getSharedPreferences("city", 0).edit().putString("cityName", cityName).commit();
            getDataFromNet();
        }
    }

    private void getDataFromNet(){
        if(Util.isNetAvailable(mContext)){
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("token", Util.getDateToken());
            param.put("city_id", getCityName_Id(0));
            param.put("city_name", getCityName_Id(1));
            OKHttpUtil.post(Constant.NEWHOME_URL, param, new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    Util.setErrorLog(TAG, "---onFailure-->>首页请求网络失败--");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    Util.setErrorLog(TAG, "---banner结果-->>" + result);
                    CacheManager.setNewhomeJson(mContext, result);
                    initData(result);
                }
            });
        }else {
            String temp = CacheManager.getNewhomeJson(mContext);
            if(!"".equals(temp)){
                initData(temp);
            }
        }
    }



    private void initData(final String json){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Gson gson = new Gson();
                final NewHomeDataItem dataItem = gson.fromJson(json, NewHomeDataItem.class);
                String msg = dataItem.getMsg();
                Util.setErrorLog(TAG, dataItem.getMsg());
                if(dataItem.getStatus() == 200){
                    newHomeAdapter = new NewHomeAdapter(mContext, dataItem.getData());
                    recyclerView.setAdapter(newHomeAdapter);
                    newHomeAdapter.notifyDataSetChanged();

                }else if(dataItem.getStatus() == 0){
                    Util.setToast(mContext, msg);
                }else {
                    Util.setErrorLog(TAG, msg);
                }
            }
        });
    }


    private String getCityName_Id(int flag){
        if(flag>0){
            // 城市名
            return cityName;
        }else{
            // 城市id
            return cityId;
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(anliReceiver!=null){
            unregisterReceiver(anliReceiver);
        }
    }

    private LocationClient mLocationClient;
    private void initBaiduMap(){
//        SDKInitializer.initialize(getApplicationContext());
        initLocationSetting();
    }

    private void initLocationSetting(){
        try{
            mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
            mLocationClient.start();

            LocationClientOption option = new LocationClientOption();
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
            int span=1000;
            option.setIsNeedAddress(true);
            option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
            option.setOpenGps(true);//可选，默认false,设置是否使用gps
            option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
            option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
            option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
            option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
            option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
            option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
            mLocationClient.setLocOption(option);

            if (mLocationClient != null && mLocationClient.isStarted()){
                System.out.println("--SelectCityActivity-->>" + mLocationClient.requestLocation());
            }else{
                System.out.println("SelectCityActivity === locClient is null or not started");
            }

            mLocationClient.registerLocationListener(new MyLocationListener());    //注册监听函数
        }catch (Exception e){
            e.printStackTrace();
        }

        needPermissions();
    }

    private  void needPermissions(){
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permission = Util.getPermissionList(mContext);
            if (permission.size() > 0) {
                requestPermissions(permission.toArray(new String[permission.size()]), 101);
            }
        }
    }


    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
//            Util.setErrorLog(TAG, "定位码" + location.getLocType());
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            AppInfoUtil.setLat(mContext, location.getLatitude()+"");
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            AppInfoUtil.setLng(mContext, location.getLongitude()+"");
            sb.append("\nradius : ");
            cityName = location.getCity();
            if(cityName!=null){
                if(cityName.contains("市") || cityName.contains("县")){
                    cityName = cityName.substring(0, cityName.length()-1);
                }
                CacheManager.setCity(mContext, cityName);
                newhomeCity.setText(cityName);
            }else{
                newhomeCity.setText("深圳");
            }


//            sb.append(location.getRadius());
//            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
//                sb.append("\nspeed : ");
//                sb.append(location.getSpeed());// 单位：公里每小时
//                sb.append("\nsatellite : ");
//                sb.append(location.getSatelliteNumber());
//                sb.append("\nheight : ");
//                sb.append(location.getAltitude());// 单位：米
//                sb.append("\ndirection : ");
//                sb.append(location.getDirection());// 单位度
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//                sb.append("\ndescribe : ");
//                sb.append("gps定位成功");
//
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//                //运营商信息
//                sb.append("\noperationers : ");
//                sb.append(location.getOperators());
//                sb.append("\ndescribe : ");
//                sb.append("网络定位成功");
//            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//                sb.append("\ndescribe : ");
//                sb.append("离线定位成功，离线定位结果也是有效的");
//            } else if (location.getLocType() == BDLocation.TypeServerError) {
//                sb.append("\ndescribe : ");
//                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
//            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//                sb.append("\ndescribe : ");
//                sb.append("网络不同导致定位失败，请检查网络是否通畅");
//            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//                sb.append("\ndescribe : ");
//                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
//            }
//            sb.append("\nlocationdescribe : ");
//            sb.append(location.getLocationDescribe());// 位置语义化信息
//            List<Poi> list = location.getPoiList();// POI数据
//            if (list != null) {
//                sb.append("\npoilist size = : ");
//                sb.append(list.size());
//                for (Poi p : list) {
//                    sb.append("\npoi= : ");
//                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
//                }
//            }

//            Log.d(TAG, "--NewhomeActivity 草--" + sb);
        }

    }



    private void initReceiver(){
        anliReceiver = new AnliReceiver();
        IntentFilter intentFilter = new IntentFilter("anli_list_is_empty");
        registerReceiver(anliReceiver, intentFilter);
    }

    private AnliReceiver anliReceiver;
    private class AnliReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("anli_list_is_empty")){
                getDataFromNet();
                Util.setErrorLog(TAG, "重新请求案例了");
            }
        }
    }
}
