package com.tbs.tobosupicture.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean.EC;
import com.tbs.tobosupicture.bean.Event;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.EventBusUtil;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * 欢迎启动页 用于加载闪屏
 */
public class WelcomeActivity extends BaseActivity {
    private Context mContext;
    private String TAG = "WelcomeActivity";
    //关于百度地图定位的相关
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mContext = this;
        //百度地图的相关设置
        needPermissions();
        checkNetConnect();
    }
    private void welcomeInit(){
        mLocationClient = new LocationClient(getApplicationContext());
        myListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myListener);
        initLocation();
        mLocationClient.start();
        //去主界面
        EventBusUtil.sendStickyEvent(new Event(EC.EventCode.WELCOMETOMAIN));
        intoMainActivity();
    }
    private void checkNetConnect(){
        if(Utils.isNetAvailable(mContext)){
            welcomeInit();
        }else {
            showNoNetDialog();
        }
    }
    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    private void intoMainActivity() {
        if (Utils.isNetAvailable(mContext)) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getDataFromNet();
                    if (!TextUtils.isEmpty(SpUtils.getUserIsFristLogin(mContext))) {
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                        WelcomeActivity.this.finish();
                    } else {
                        //用户第一次登入我们的App
                        startActivity(new Intent(WelcomeActivity.this, FristActivity.class));
                        WelcomeActivity.this.finish();
                    }
                }
            }, 2000);
        } else {
            //网络未连接的时候弹出相关的提示 进行网络的设置
            Toast.makeText(mContext, "当前网络未连接，请开启网络连接~", Toast.LENGTH_SHORT).show();
            intoMainActivity();
        }
    }

    private void showNoNetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("当前网络未连接，需要去设置吗？");
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (android.os.Build.VERSION.SDK_INT > 10) {
                    //3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
                    mContext.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                } else {
                    mContext.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                }
                showNoNetDialog();
            }
        }).setNegativeButton("设置好了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
               checkNetConnect();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //初始化百度定位
    //关于百度定位的一些设置
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 0;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }

    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //获取定位结果
            StringBuffer sb = new StringBuffer(256);

            sb.append("time : ");
            sb.append(location.getTime());    //获取定位时间

            sb.append("\nerror code : ");
            sb.append(location.getLocType());    //获取类型类型

            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());    //获取纬度信息

            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());    //获取经度信息

            sb.append("\nradius : ");
            sb.append(location.getRadius());    //获取定位精准度

            if (location.getLocType() == BDLocation.TypeGpsLocation) {

                // GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());    // 单位：公里每小时

                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());    //获取卫星数

                sb.append("\nheight : ");
                sb.append(location.getAltitude());    //获取海拔高度信息，单位米

                sb.append("\ndirection : ");
                sb.append(location.getDirection());    //获取方向信息，单位度

                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {

                // 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\noperationers : ");
                sb.append(location.getOperators());    //获取运营商信息

                sb.append("\ndescribe : ");
                sb.append("网络定位成功");

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                // 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");

            } else if (location.getLocType() == BDLocation.TypeServerError) {

                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");

            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

            }

            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());    //位置语义化信息

            List<Poi> list = location.getPoiList();    // POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.e(TAG, "定位1>>>>" + sb.toString() + "br/具体城市1>>>" + location.getCity());
            SpUtils.setBaiduLocationCity(WelcomeActivity.this, location.getCity());
            Log.i("BaiduLocationApiDem", sb.toString());
            if (!TextUtils.isEmpty(location.getCity()) && !(location.getCity().equals(SpUtils.getLocationCity(mContext)))) {
                //当获取的城市不为空且和之前的定位城市不相同则修改定位的城市
                SpUtils.setLocationCity(getApplicationContext(), location.getCity());
                Log.e(TAG, "定位>>>>" + sb.toString() + "br/具体城市>>>" + location.getCity());
            } else {
                Log.e(TAG, "定位>>>> br/具体城市>>>");
            }
            SpUtils.setLocationLat(getApplicationContext(), "" + location.getLatitude());
            SpUtils.setLocationLon(getApplicationContext(), "" + location.getLongitude());

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    // 动态获取权限
    private void needPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permission = getPermissionList(mContext);
            if (permission.size() > 0) {
                requestPermissions(permission.toArray(new String[permission.size()]), 101);
            }
        }
    }

    public List<String> getPermissionList(Context activity) {
        List<String> permission = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.READ_PHONE_STATE);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.ACCESS_WIFI_STATE);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.ACCESS_NETWORK_STATE);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.INTERNET);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.CHANGE_WIFI_STATE);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        return permission;
    }

    private void getDataFromNet() {
        if (Utils.isNetAvailable(mContext)) {
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("token", Utils.getDateToken());
            HttpUtils.doPost(UrlConstans.GET_HOUSE_DECORATE_STYLE_URL, param, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.setToast(mContext, "系统繁忙请稍后再试!");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
//                    Utils.setErrorLog(TAG, json);
                    SpUtils.setHouseStyleJson(mContext, json);
                }
            });


            HttpUtils.doPost(UrlConstans.GET_FACTORY_DECORATE_STYLE_SURL, param, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.setToast(mContext, "系统繁忙请稍后再试!");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    SpUtils.setFactoryStyleJson(mContext, json);
                    Utils.setErrorLog(TAG, json);
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 101:
//                SpUtils.setUserIsSetPro(mContext, "true");
//                Log.e(TAG, "权限获取之后====permissions===" + Arrays.toString(permissions) + "====grantResults====" + Arrays.toString(permissions));
                break;
        }
    }
}
