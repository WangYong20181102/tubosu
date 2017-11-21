package com.tbs.tobosutype.activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Lie on 2017/08/23.
 */

public class NewHomeActivity extends BaseActivity {
    private ImageView home_view;
    private ImageView ivYingying;
    private View rel_newhomebar;
    private ImageView iv_sanjiaoxing;
    private ImageView iv_add;
    private ImageView home_kefu;
    private TextView newhomeCity;
    private TextView app_title_text;
    private RelativeLayout relSelectCity;
    private String choose;
    private String chooseId;
    private String cityName;
    private RecyclerView recyclerView;
    private TextView tubosu;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private NewHomeAdapter newHomeAdapter;
    private boolean isSheji = true;
    private Gson mGson;
    private ArrayList<NewHomeDataItem.NewhomeDataBean.TopicBean> topicBeansList = new ArrayList<NewHomeDataItem.NewhomeDataBean.TopicBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = NewHomeActivity.this;
        TAG = NewHomeActivity.class.getSimpleName();
        setContentView(R.layout.layout_new_activity);
        initView();
        initBaiduMap();
        chooseId = "0";
        setClick();
        getDataFromNet(false);
        initReceiver();
    }

    private void initView() {
        mGson = new Gson();
        home_view = (ImageView) findViewById(R.id.home_view);
        ivYingying = (ImageView) findViewById(R.id.ivYingying);
        ivYingying.setVisibility(View.GONE);
        home_view.setFocusable(true);
        home_view.setFocusableInTouchMode(true);
        home_view.requestFocus();
        home_view.setVisibility(View.VISIBLE);
        tubosu = (TextView) findViewById(R.id.app_title_text);
        rel_newhomebar = (View) findViewById(R.id.newhomeView);
        rel_newhomebar.setAlpha(0);
        relSelectCity = (RelativeLayout) findViewById(R.id.relSelectCity);
        newhomeCity = (TextView) findViewById(R.id.newhomeCity);

        iv_sanjiaoxing = (ImageView) findViewById(R.id.iv_sanjiaoxing );
        iv_add = (ImageView) findViewById(R.id.iv_add );
        home_kefu = (ImageView) findViewById(R.id.home_kefu );
        app_title_text = (TextView) findViewById(R.id.app_title_text );

        recyclerView = (RecyclerView) findViewById(R.id.newhome_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.newhome_swiprefreshlayout);

        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        //初始化swipeRreshLayout
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(swipeLister);
        swipeRefreshLayout.setOnTouchListener(onTouchListener);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                //得到当前显示的最后一个item的view
                int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && lastPosition + 2 >= recyclerView.getLayoutManager().getItemCount()) {

                    if (newHomeAdapter != null) {
                        newHomeAdapter.setLoadMoreFlag(true);
                        page++;
                        getDataFromNet(true);
                    }
                }


                //设置其透明度
                float alpha = 0;
                int scollYHeight = getScollYHeight(true, tubosu.getHeight());

                int baseHeight = 574;
                if (scollYHeight >= baseHeight) {
                    alpha = 1;
                } else {
                    alpha = scollYHeight / (baseHeight * 1.0f);
                    if (alpha > 0.44) {
                        ivYingying.setVisibility(View.VISIBLE);
                        home_view.setVisibility(View.INVISIBLE);// 白色渐变 隐藏
                        rel_newhomebar.setVisibility(View.VISIBLE);
                        iv_sanjiaoxing.setBackgroundResource(R.drawable.tt);
                        iv_add.setBackgroundResource(R.drawable.sdf);
                        home_kefu.setBackgroundResource(R.drawable.kefu_black);
                        newhomeCity.setTextColor(Color.parseColor("#000000"));
                        app_title_text.setTextColor(Color.parseColor("#000000"));
                        rel_newhomebar.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }else {

                        ivYingying.setVisibility(View.GONE);
                        home_view.setVisibility(View.VISIBLE);
                        rel_newhomebar.setVisibility(View.INVISIBLE);
                        iv_sanjiaoxing.setBackgroundResource(R.drawable.sanjiaoxing);
                        iv_add.setBackgroundResource(R.drawable.ad_icon);
                        home_kefu.setBackgroundResource(R.drawable.home_kefu);
                        newhomeCity.setTextColor(Color.parseColor("#FFFFFF"));
                        app_title_text.setTextColor(Color.parseColor("#FFFFFF"));
                        rel_newhomebar.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    }

                }
                rel_newhomebar.setAlpha(alpha);
//                Util.setErrorLog(TAG, "====>>>" + alpha);
            }
        });

        home_kefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showZixunPopwindow();
            }
        });
    }

    private View zixunPopView;
    private PopupWindow zixunPopupWindow;
    private void showZixunPopwindow() {
        zixunPopView = View.inflate(mContext, R.layout.popwindow_zixun, null);
        TextView qq_lianxi = (TextView) zixunPopView.findViewById(R.id.qq_lianxi);
        TextView dianhua_lianxi = (TextView) zixunPopView.findViewById(R.id.dianhua_lianxi);
        RelativeLayout pop_zixun_rl = (RelativeLayout) zixunPopView.findViewById(R.id.pop_zixun_rl);
        zixunPopupWindow = new PopupWindow(zixunPopView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        zixunPopupWindow.setFocusable(true);
        zixunPopupWindow.setOutsideTouchable(true);
        zixunPopupWindow.update();
        //打开QQ
        qq_lianxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹窗  尝试打开QQ
                String url = "http://wpa.b.qq.com/cgi/wpa.php?ln=2&uin=4006062221";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                zixunPopupWindow.dismiss();
            }
        });
        //打开电话联系
        dianhua_lianxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出窗口电话联系
                showOpenPhone();
                zixunPopupWindow.dismiss();
            }
        });
        //界面消失
        pop_zixun_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zixunPopupWindow.dismiss();
            }
        });
        //窗口显示的位置
        zixunPopupWindow.showAtLocation(zixunPopView, Gravity.CENTER, 0, 0);
    }


    private void showOpenPhone() {
        View popview = View.inflate(mContext, R.layout.popwindow_qqzixun, null);
        TextView quxiao_phone = (TextView) popview.findViewById(R.id.quxiao_phone);
        TextView open_phone = (TextView) popview.findViewById(R.id.open_phone);
        RelativeLayout pop_phone_zixun = (RelativeLayout) popview.findViewById(R.id.pop_phone_zixun);
        final PopupWindow popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        //打电话
        open_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打电话
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "4006062221"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        //取消
        quxiao_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        pop_phone_zixun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(popview, Gravity.CENTER, 0, 0);
    }

    private int page = 1;
    private boolean isLoading = false;//是否正在加载数据
    private int getScollYHeight(boolean hasHead, int headerHeight) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //获取到第一个可见的position,其添加的头部不算其position当中
        int position = layoutManager.findFirstVisibleItemPosition();
        //通过position获取其管理器中的视图
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        //获取自身的高度
        int itemHeight = firstVisiableChildView.getHeight();
        //有头部
        if (hasHead) {
            return headerHeight + itemHeight * position - firstVisiableChildView.getTop();
        } else {
            return itemHeight * position - firstVisiableChildView.getTop();
        }
    }


    //下拉刷新监听事件
    private SwipeRefreshLayout.OnRefreshListener swipeLister = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新数据 重新初始化各种数据
            topicBeansList.clear();
            newHomeAdapter = null;
            swipeRefreshLayout.setRefreshing(false);
            page = 1;
            getDataFromNet(false);
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

    private void setClick() {
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
            if (data != null && data.getBundleExtra("city_bundle") != null) {
                choose = data.getBundleExtra("city_bundle").getString("ci");
                chooseId = data.getBundleExtra("city_bundle").getString("cid");
                getSharedPreferences("Save_City_Info", MODE_PRIVATE).edit().putString("save_city_now", cityName).commit();
                AppInfoUtil.setCityName(mContext, cityName);
                Util.setErrorLog(TAG, chooseId + " <<#id == choose>>> " + choose);
                newhomeCity.setText(choose);
                CacheManager.setStartFlag(NewHomeActivity.this, 1);

                getDataFromNet(false);
            }


            Intent selectCityIntent = new Intent(Constant.ACTION_HOME_SELECT_CITY);
            Bundle b = new Bundle();
            b.putString("city_selected", cityName);
            selectCityIntent.putExtra("f_select_city_bundle", b);
            sendBroadcast(selectCityIntent);
            getSharedPreferences("city", 0).edit().putString("cityName", cityName).commit();

        }
    }


    /**
     *
     * @param num
     * @return
     */
    private HashMap<String, Object> getParam(int num) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("token", Util.getDateToken());
        String city = newhomeCity.getText().toString();
        if (num == 0) {
            param.put("city_id", "0");
            param.put("city_name", city);
            param.put("type", "1");
            Util.setErrorLog("-zengzhaozhong-", "id = 0    cityname = " + city);
        } else {
            // 选择
            param.put("city_id", chooseId);
            param.put("city_name", choose);
            param.put("type", "1");
            Util.setErrorLog("-zengzhaozhong-", "#id = " + chooseId + "    cityname = " + city);
        }
        return param;
    }

    private void getDataFromNet(boolean more) {
        if (Util.isNetAvailable(mContext)) {
            isLoading = true;
            // start 1 选择过城市，  start 0 未选择过城市
            int start = CacheManager.getStartFlag(NewHomeActivity.this);
            Util.setErrorLog(TAG, "---zengzhaozhong--start>>" + start);
            if(more){
                // 加载更多
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                hashMap.put("token", Util.getDateToken());
                hashMap.put("page", page);
                hashMap.put("type", "1");
                hashMap.put("page_size", 5);
                OKHttpUtil.post(Constant.ZHUANTI_URL, hashMap, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        isLoading = false;
                        Util.setErrorLog(TAG, "请求失败");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Util.setToast(mContext, "加载更多专题失败");
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        final String json = new String(response.body().string());
                        Util.setErrorLog(TAG, json);
                        // 有数据 这样处理是不至于无数据的时候出现app闪退
                        if(json.contains("data")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        List<NewHomeDataItem.NewhomeDataBean.TopicBean> zhuantiList = new ArrayList<NewHomeDataItem.NewhomeDataBean.TopicBean>();
                                        JSONObject zhuantiObject = new JSONObject(json);
                                        int status = zhuantiObject.getInt("status");
                                        String msg = zhuantiObject.getString("msg");
                                        if (status == 200) {
                                            JSONArray arr = zhuantiObject.getJSONArray("data");
                                            for (int i = 0; i < arr.length(); i++) {
                                                NewHomeDataItem.NewhomeDataBean.TopicBean bean = mGson.fromJson(arr.get(i).toString(), NewHomeDataItem.NewhomeDataBean.TopicBean.class);
//                                        NewHomeDataItem.NewhomeDataBean.TopicBean bean = new NewHomeDataItem.NewhomeDataBean.TopicBean();
//                                        bean.setAdd_time(arr.getJSONObject(i).getString("add_time"));
//                                        bean.setDesc(arr.getJSONObject(i).getString("desc"));
//                                        bean.setId(arr.getJSONObject(i).getString("id"));
//                                        bean.setCover_url(arr.getJSONObject(i).getString("cover_url"));
//                                        bean.setTitle(arr.getJSONObject(i).getString("title"));o
                                                zhuantiList.add(bean);
                                            }

                                            topicBeansList.addAll(zhuantiList);
//                                        // TODO: 2017/11/2
//                                        if(newHomeAdapter1==null){
//                                            newHomeAdapter1 = new NewHomeAdapter(mContext, bigData, topicBeansList);
//                                            recyclerView.setAdapter(newHomeAdapter1);
//                                            newHomeAdapter1.notifyDataSetChanged();
//                                        }else {
//                                            newHomeAdapter1.notifyDataSetChanged();
//                                        }
                                            initData();

                                        } else if (status == 0) {
                                            Util.setToast(mContext, msg);
                                        } else if (status == 201) {
                                            Util.setToast(mContext, msg);
                                        } else {
                                            Util.setErrorLog(TAG, " 错误请求码是 [" + status + "]");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }else {
                            // 无数据 这样处理是不至于无数据的时候出现app闪退
                            Util.setErrorLog(TAG, "后台无数据返回给我===1===如下：" +json);
                        }
                    }
                });
            }else {
                newHomeAdapter = null;
                bigData = null;
                // 第一次加载
                OKHttpUtil.post(Constant.NEWHOME_URL, getParam(start), new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        Util.setErrorLog(TAG, "---onFailure-->>首页请求网络失败--");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = new String(response.body().string());
                        // 有数据 这样处理是不至于无数据的时候出现app闪退
                        if(result.contains("data")){
                            Util.setErrorLog(TAG, "---zengzhaozhong-->>" + result);
                            CacheManager.setNewhomeJson(mContext, result);
                            Gson gson = new Gson();
                            final NewHomeDataItem dataItem = gson.fromJson(result, NewHomeDataItem.class);
                            final String msg = dataItem.getMsg();
                            Util.setErrorLog(TAG, dataItem.getMsg());
                            if (dataItem.getStatus() == 200) {
                                bigData = dataItem.getData();
                                initData();
                            }else if (dataItem.getStatus() == 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Util.setToast(mContext, msg);
                                    }
                                });

                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Util.setToast(mContext, msg);
                                    }
                                });
                            }
                            if (swipeRefreshLayout.isRefreshing()) {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }else {
                            // 无数据 这样处理是不至于无数据的时候出现app闪退
                            Util.setErrorLog(TAG, "后台无数据返回给我===2===如下：" + result);
                        }
                    }
                });
            }

        }else {
            Util.setErrorLog(TAG, "无网络");
        }

    }


    private NewHomeDataItem.NewhomeDataBean bigData;
    private void initData() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                isLoading = false;
                if(newHomeAdapter==null){
                    newHomeAdapter = new NewHomeAdapter(mContext, bigData, topicBeansList);
                    recyclerView.setAdapter(newHomeAdapter);
                    newHomeAdapter.notifyDataSetChanged();
                }else {
                    if(topicBeansList.size()>0){
                        newHomeAdapter.setTopicData(topicBeansList);
                    }
                    newHomeAdapter.notifyDataSetChanged();
                }
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

//                newHomeAdapter.setmListener(new NewHomeAdapter.OnZhuantiItemClickListener() {
//                    @Override
//                    public void onZhuantiItemClickListener(int position) {
//                        Intent it = new Intent(mContext, TopicDetailActivity.class);
//                        it.putExtra("mTopicId", topicBeansList.get(position).getId());
//                        startActivity(it);
//                    }
//                });
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (anliReceiver != null) {
            unregisterReceiver(anliReceiver);
        }
    }


    private LocationClient mLocationClient;

    private void initBaiduMap() {
        try {
            mLocationClient = new LocationClient(NewHomeActivity.this.getParent());     //声明LocationClient类
            mLocationClient.start();

            LocationClientOption option = new LocationClientOption();
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
            int span = 1000;
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
            mLocationClient.registerLocationListener(new MyLocationListener());    //注册监听函数
        } catch (Exception e) {
            e.printStackTrace();
        }

        needPermissions();

    }

    private void needPermissions() {
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
            AppInfoUtil.setLat(mContext, location.getLatitude() + "");
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            AppInfoUtil.setLng(mContext, location.getLongitude() + "");
            sb.append("\nradius : ");
            cityName = location.getCity();
            if (CacheManager.getStartFlag(NewHomeActivity.this) == 0) {
                if (cityName != null) {
                    if (cityName.contains("市") || cityName.contains("县")) {
                        cityName = cityName.substring(0, cityName.length() - 1);
                    }
                    CacheManager.setCity(mContext, cityName);
                    newhomeCity.setText(cityName);
                } else {
                    newhomeCity.setText("深圳");
                }
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


//            Util.setErrorLog(TAG, ">>> " + sb.toString());
        }

    }


    private void initReceiver() {
        anliReceiver = new AnliReceiver();
        IntentFilter intentFilter = new IntentFilter("anli_list_is_empty");
        registerReceiver(anliReceiver, intentFilter);
    }

    private AnliReceiver anliReceiver;

    private class AnliReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (isSheji) {
                isSheji = false;
                if (intent.getAction().equals("anli_list_is_empty")) {
//                    getDataFromNet(false);
                    Util.setErrorLog(TAG, "重新请求案例了");
                }
            }
        }
    }
}