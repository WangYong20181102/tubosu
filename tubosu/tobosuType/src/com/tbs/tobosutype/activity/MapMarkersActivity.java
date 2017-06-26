package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapTouchListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.HttpServer;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
 /**
  * 地图标记页面
  * @author dec
  *
  */
public class MapMarkersActivity extends Activity implements OnClickListener {
	private static final String TAG = MapMarkersActivity.class.getSimpleName();
	
	private Context mContext;
	
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private ImageView mSelectImg;
	private ImageView map_markers_back;
	private TextView tv_enter;
	
	private String token;
	
	/**修改坐标的接口*/
	private String mapMarkersUrl = Constant.TOBOSU_URL + "/tapp/company/set_coor";
	
	private RequestParams mapMarkersParams;
	
	private LocationClient mLocationClient = null;
	private MyBDLocationListner mListner = null;
	private BitmapDescriptor mCurrentMarker = null;
	
	private double mLantitude;
	private double mLongtitude;
	private LatLng mLoactionLatLng;
	boolean isFirstLoc = true;
	private Point mCenterPoint = null;
	private GeoCoder mGeoCoder = null;
	private ListView mListView;
	private PlaceListAdapter mAdapter;
	private List<PoiInfo> mInfoList;
	private PoiInfo mCurentInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		AppInfoUtil.setActivityTheme(this, R.color.color_icon);
		AppInfoUtil.setTranslucentStatus(this);
		setContentView(R.layout.activity_map_markers);
		mContext = MapMarkersActivity.this;
//		initView();
//		initData();
//		initEvent();
	}

	private void initView() {
		mMapView = (MapView) findViewById(R.id.bmapView);
		map_markers_back = (ImageView) findViewById(R.id.map_markers_back);
		tv_enter = (TextView) findViewById(R.id.tv_enter);
		mMapView.showZoomControls(false);
		mBaiduMap = mMapView.getMap();
		
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(17.0f);
		mBaiduMap.setMapStatus(msu);
		mBaiduMap.setOnMapTouchListener(touchListener);
		mInfoList = new ArrayList<PoiInfo>();
		mCenterPoint = mBaiduMap.getMapStatus().targetScreen;
		mLoactionLatLng = mBaiduMap.getMapStatus().target;
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
		mListView = (ListView) findViewById(R.id.place_list);
		mListView.setOnItemClickListener(itemClickListener);
		mAdapter = new PlaceListAdapter(getLayoutInflater(), mInfoList);
		mListView.setAdapter(mAdapter);
		mCenterPoint = mBaiduMap.getMapStatus().targetScreen;// mBaiduMap是BaiduMap类的实例
		mSelectImg = new ImageView(this);
	}

	private void initData() {
		mapMarkersParams = AppInfoUtil.getPublicParams(getApplicationContext());
		mLongtitude = Double.parseDouble(getIntent().getExtras().getString("lng"));
		mLantitude = Double.parseDouble(getIntent().getExtras().getString("lat"));
		token = AppInfoUtil.getToekn(getApplicationContext());
		mapMarkersParams.put("token", token);
	}

	private void initEvent() {
		map_markers_back.setOnClickListener(this);
		tv_enter.setOnClickListener(this);
	}

	public void turnBack(View view) {
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(mLoactionLatLng);
		mBaiduMap.animateMapStatus(u);

		mBaiduMap.clear();
		mGeoCoder.reverseGeoCode((new ReverseGeoCodeOption()).location(mLoactionLatLng));

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mLocationClient.stop();
		mGeoCoder.destroy();
	}

	/**
	 * 百度地图监听
	 * 
	 * */
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

	OnGetGeoCoderResultListener GeoListener = new OnGetGeoCoderResultListener() {
		@Override
		public void onGetGeoCodeResult(GeoCodeResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			}
		}

		@Override
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			}
			else {
				mCurentInfo = new PoiInfo();
				mCurentInfo.address = result.getAddress();
				mCurentInfo.location = result.getLocation();
				mCurentInfo.name = "[位置]";
				mInfoList.clear();
				mInfoList.add(mCurentInfo);
				if (result.getPoiList() != null) {
					mInfoList.addAll(result.getPoiList());
				}
				mAdapter.notifyDataSetChanged();
			}
		}
	};

	OnMapTouchListener touchListener = new OnMapTouchListener() {
		@Override
		public void onTouch(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {

				if (mCenterPoint == null) {
					return;
				}

				LatLng currentLatLng;
				currentLatLng = mBaiduMap.getProjection().fromScreenLocation(mCenterPoint);
				mLantitude = currentLatLng.latitude;
				mLongtitude = currentLatLng.longitude;
				mGeoCoder.reverseGeoCode((new ReverseGeoCodeOption()).location(currentLatLng));

			}
		}
	};

	OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			mAdapter.setNotifyTip(position);

			BitmapDescriptor mSelectIco = BitmapDescriptorFactory.fromResource(R.drawable.map_flag);
			mBaiduMap.clear();
			PoiInfo info = (PoiInfo) mAdapter.getItem(position);
			LatLng la = info.location;
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(la);
			mBaiduMap.animateMapStatus(u);
			OverlayOptions ooA = new MarkerOptions().position(la).icon(mSelectIco).anchor(0.5f, 0.5f);
			mBaiduMap.addOverlay(ooA);
			mAdapter.notifyDataSetChanged();
		}

	};
	

	/***
	 *  FIXME 适配器
	 * @author dec
	 *
	 */
	public class PlaceListAdapter extends BaseAdapter {

		List<PoiInfo> mList;
		LayoutInflater mInflater;
		int notifyTip;

		private class MyViewHolder {
			TextView placeName;
			TextView placeAddree;
			TextView placeHook;
		}

		public PlaceListAdapter(LayoutInflater mInflater, List<PoiInfo> mList) {
			super();
			this.mList = mList;
			this.mInflater = mInflater;
			notifyTip = 0;
		}

		/**
		 * 设置第几个item被选择
		 * 
		 * @param notifyTip
		 */
		public void setNotifyTip(int notifyTip) {
			this.notifyTip = notifyTip;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MyViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_map_listview, parent, false);
				holder = new MyViewHolder();
				holder.placeName = (TextView) convertView.findViewById(R.id.place_name);
				holder.placeAddree = (TextView) convertView.findViewById(R.id.place_adress);
				holder.placeHook = (TextView) convertView.findViewById(R.id.place_hook);
				holder.placeName.setText(mList.get(position).name);
				holder.placeAddree.setText(mList.get(position).address);
				convertView.setTag(holder);
			} else {
				holder = (MyViewHolder) convertView.getTag();
			}
			holder.placeName.setText(mList.get(position).name);
			holder.placeAddree.setText(mList.get(position).address);
			if (notifyTip == position) {
				holder.placeName.setTextColor(Color.parseColor("#01ca5f"));
				holder.placeAddree.setTextColor(Color.parseColor("#01ca5f"));
				holder.placeHook.setTextColor(Color.parseColor("#01ca5f"));
				mLantitude = mList.get(position).location.latitude;
				mLongtitude = mList.get(position).location.longitude;
				holder.placeHook.setVisibility(View.VISIBLE);
			} else {
				holder.placeHook.setVisibility(View.GONE);
				holder.placeName.setTextColor(getResources().getColor(R.color.color_neutralgrey));
				holder.placeAddree.setTextColor(getResources().getColor(R.color.color_neutralgrey));
			}
			return convertView;
		}
	}

	/***
	 * 修改坐标接口方法
	 */
	private void requestMarkers() {
		mapMarkersParams.put("token", getSharedPreferences("userInfo", 0)
				.getString("token", ""));
		mapMarkersParams.put("lat", mLantitude);
		mapMarkersParams.put("lng", mLongtitude);
		
		HttpServer.getInstance().requestPOST(mapMarkersUrl, mapMarkersParams,  new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] result) {
						try {
							JSONObject jsonObject = new JSONObject(new String(result));
							if (jsonObject.getInt("error_code") == 0) {
								Toast.makeText(mContext, "标记成功", Toast.LENGTH_SHORT).show();
								finish();
							} else {
								Toast.makeText(mContext, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						Toast.makeText(mContext, "标记失败！", Toast.LENGTH_SHORT).show();
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.map_markers_back:
			finish();
			break;
		case R.id.tv_enter:
			requestMarkers();
			break;

		default:
			break;
		}
	}
	
	@Override  
    protected void onPause() {  
        super.onPause();  
        // activity 暂停时同时暂停地图控件  
        mMapView.onPause();  
    }  
  
    @Override  
    protected void onResume() {  
        super.onResume();  
        // activity 恢复时同时恢复地图控件  
        mMapView.onResume();  
    }  
}