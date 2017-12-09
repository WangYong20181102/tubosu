package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.overlayutil.DrivingRouteOverlay;
import com.tbs.tobosutype.utils.SpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BaiduMapActivity extends AppCompatActivity {


    @BindView(R.id.baidu_map_view)
    TextureMapView baiduMapView;
    @BindView(R.id.baidu_map_back_ll)
    LinearLayout baiduMapBackLl;
    private String TAG = "BaiduMapActivity";
    private Context mContext;
    private BaiduMap mBaiduMap;
    private BitmapDescriptor mCustomMarker;
    private Intent mIntent;
    //公司的经纬度
    private Double mComPointLat;//经度
    private Double mComPointLong;//纬度
    private String mCompanyNmae;//公司的名称
    private String mCompanyAddress;//公司地址
    private LatLng point;//公司所在的点
    //路线规划
    private RoutePlanSearch mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu_map);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    private void initView() {
        mIntent = getIntent();
        mSearch = RoutePlanSearch.newInstance();
        //获取经纬度
        mComPointLat = Double.valueOf(mIntent.getStringExtra("mComPointLat"));
        mComPointLong = Double.valueOf(mIntent.getStringExtra("mComPointLong"));
        //获取公司的名称以及公司的地址
        mCompanyNmae = mIntent.getStringExtra("mCompanyNmae");
        mCompanyAddress = mIntent.getStringExtra("mCompanyAddress");


        mBaiduMap = baiduMapView.getMap();
        Log.e(TAG, "获取定位的城市=====" + SpUtil.getCity(mContext) + "===获取定位的经度===" + SpUtil.getLatitude(mContext) + "===获取定位的纬度===" + SpUtil.getLongitude(mContext));
        MyLocationData locationData = new MyLocationData.Builder()
                .accuracy(Float.parseFloat(SpUtil.getRadius(mContext)))
                .direction(0)
                .latitude(Double.parseDouble(SpUtil.getLatitude(mContext)))
                .longitude(Double.parseDouble(SpUtil.getLongitude(mContext)))
                .build();
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMyLocationData(locationData);
        mCustomMarker = BitmapDescriptorFactory.fromResource(R.drawable.baidumap_mylocation);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING,
                true,
                mCustomMarker);
        mBaiduMap.setMyLocationConfiguration(config);

        /// TODO: 2017/11/28  显示装修公司的位置 Mark形式
        point = new LatLng(mComPointLat, mComPointLong);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_map_point, null);
        TextView item_map_company_name = view.findViewById(R.id.item_map_company_name);
        TextView item_map_address = view.findViewById(R.id.item_map_address);
        TextView item_map_get_line = view.findViewById(R.id.item_map_get_line);
        //设置名称
        item_map_company_name.setText("" + mCompanyNmae);
        item_map_address.setText("" + mCompanyAddress);

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(view);
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmapDescriptor);
        mBaiduMap.addOverlay(option);
        //改变地图状态
        MapStatus mapStatus = new MapStatus.Builder().target(point).zoom(12).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mBaiduMap.setMapStatus(mapStatusUpdate);
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.e(TAG, "点击了按钮============");
                /// TODO: 2017/11/29  设置路线的规划
                mSearch.setOnGetRoutePlanResultListener(listener);
                LatLng userLatLng = new LatLng(Double.parseDouble(SpUtil.getLatitude(mContext)), Double.parseDouble(SpUtil.getLongitude(mContext)));//用户所在的位置
                PlanNode stNode = PlanNode.withLocation(userLatLng);
                PlanNode enNode = PlanNode.withLocation(point);
                mSearch.drivingSearch((new DrivingRoutePlanOption())
                        .from(stNode)
                        .to(enNode));
                return true;
            }
        });
    }

    //导航监听
    private OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
            //驾车检索
            DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(mBaiduMap);
            drivingRouteOverlay.setData(drivingRouteResult.getRouteLines().get(0));
            drivingRouteOverlay.addToMap();
            drivingRouteOverlay.zoomToSpan();
        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        }
    };

    /**
     * 地图跟随Activity生命周期
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearch.destroy();
        baiduMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        baiduMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        baiduMapView.onPause();
    }

    @OnClick(R.id.baidu_map_back_ll)
    public void onViewClickedInBaiduMap() {
        finish();
    }
}
