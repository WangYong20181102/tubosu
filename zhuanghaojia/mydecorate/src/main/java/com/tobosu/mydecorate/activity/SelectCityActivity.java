package com.tobosu.mydecorate.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.adapter.CityAdapter;
import com.tobosu.mydecorate.application.MyApplication;
import com.tobosu.mydecorate.entity.City;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.util.CacheManager;
import com.tobosu.mydecorate.util.CityData;
import com.tobosu.mydecorate.view.BladeView;
import com.tobosu.mydecorate.view.FirstGridView;
import com.tobosu.mydecorate.view.PinnedHeaderListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 选择城市 页
 * 
 * @author dec
 * 
 */
public class SelectCityActivity extends Activity implements OnClickListener{
	private static final String TAG = SelectCityActivity.class.getSimpleName();
	
	private Context context;
	private View mSearchContainer;
	
	private PinnedHeaderListView mCityListView;
	private LinearLayout citysListEmpty;
	
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

	private int from = -1;
	
	/**热门城市gridview*/
	private FirstGridView mCity_gridView;
	
	/***定位中的提示信息*/
	private TextView select_positioning;
	
	/***退出或返回上一层*/
	private ImageView city_title_back;
	
	private View headView;
	
//	/***选择城市正在加载网络时的布局*/
//	private LinearLayout select_loading;
	

	private List<String> hotCityNames = new ArrayList<String>();
//	private MapView mMapView;
//	private BaiduMap mBaiduMap;
	private LocationClient mLocationClient = null;
	private BitmapDescriptor mCurrentMarker = null;
//	private double mLantitude;
//	private double mLongtitude;
//	private LatLng mLoactionLatLng;
//	private boolean isFirstLoc = true;
//	private Point mCenterPoint = null;
//	private Double lat;
//	private Double lng;
	
	/**定位得到的真实城市地理地址*/
	private String realLocationCity = "";
	

	/**选择城市页面的所有布局*/
	private LinearLayout select_city_layout;
	
	private LinearLayout select_city_activity_netoutview;

	private int fromCode = 101;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectcity);
		context = SelectCityActivity.this;

		initBaidu();
		initView();
		getCityJson();
		getDataIntent();

	}

	private void getDataIntent(){
		if(getIntent()!=null && getIntent().getBundleExtra("GetPriceSelectcityBundle")!=null){
			fromCode = Integer.parseInt(getIntent().getBundleExtra("GetPriceSelectcityBundle").getString("fromGetPrice"));
		}
	}

	private void initBaidu(){
		SDKInitializer.initialize(getApplicationContext());
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
			option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
			option.setOpenGps(true);//可选，默认false,设置是否使用gps
			option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
			option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
			option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
			option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
			option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
			option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
			mLocationClient.setLocOption(option);

			if (mLocationClient != null && mLocationClient.isStarted()){
				System.out.println("--MainActivity-->>" + mLocationClient.requestLocation());
			}else{
				System.out.println("MainActivity === locClient is null or not started");
			}

			mLocationClient.registerLocationListener(new MyLocationListener());    //注册监听函数
		}catch (Exception e){
			e.printStackTrace();
		}
	}


	private String locationCity = "";



	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			locationCity = location.getCity();
			if(locationCity!=null){
				locationCity= locationCity.replaceAll("[^\u4E00-\u9FA5]", "");
                System.out.println("=============有没有city>>>" + locationCity +"<<<");
				CacheManager.setCity(context, locationCity);
				select_positioning.setText(locationCity);
			}else{
				select_positioning.setText("定位中...");
			}


			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());// 单位：公里每小时
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\nheight : ");
				sb.append(location.getAltitude());// 单位：米
				sb.append("\ndirection : ");
				sb.append(location.getDirection());// 单位度
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append("\ndescribe : ");
				sb.append("gps定位成功");

			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				//运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
				sb.append("\ndescribe : ");
				sb.append("网络定位成功");
			} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
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
			sb.append(location.getLocationDescribe());// 位置语义化信息
			List<Poi> list = location.getPoiList();// POI数据
			if (list != null) {
				sb.append("\npoilist size = : ");
				sb.append(list.size());
				for (Poi p : list) {
					sb.append("\npoi= : ");
					sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
				}
			}

		}

	}

	private void getCityJson(){
		String cityJson = CacheManager.getCityJson(context);
		if("".equals(cityJson)){
			requestSelectCity();
		}else{
			System.out.println("------本地获取json-->>>");
			prseSelectDataJson(cityJson);
		}
	}


	private List<City> mCityList;
	private CityData mCityData;
	private void initCityList() {
		mCityList = new ArrayList<City>();
		mSections = new ArrayList<String>();
		mMap = new HashMap<String, List<City>>();
		mPositions = new ArrayList<Integer>();
		mIndexer = new HashMap<String, Integer>();
		mCityData = new CityData(this);
		new Thread(new Runnable() {

			@Override
			public void run() {
				if(prepareCityList()){
					mHandler.sendEmptyMessage(CITY_LIST_SCUESS);
				}
			}
		}).start();
	}

	private static final int CITY_LIST_SCUESS = 0;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
				case CITY_LIST_SCUESS:
					showList();
					break;
				default:
					break;
			}
		}
	};



	private void showList(){
		mCities = mCityList;
		mCityAdapter = new CityAdapter(this, mCities, mMap, mSections, mPositions);
		mCityListView.setAdapter(mCityAdapter);
		mCityListView.setOnScrollListener(mCityAdapter);
		mLetter.setVisibility(View.VISIBLE);
	}


	private static final String FORMAT = "^[a-z,A-Z].*$";
	private boolean prepareCityList() {
		mCityList = mCityData.getAllCity1(context);
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

	private void initView() {
		SDKInitializer.initialize(getApplicationContext());

		Bundle b;
		if(getIntent()!=null && getIntent().getBundleExtra("pop_bundle")!=null){
			b = getIntent().getBundleExtra("pop_bundle");
			from = b.getInt("frompop");
		}

		city_title_back = (ImageView) findViewById(R.id.city_title_back);

		if(from==31){
			city_title_back.setVisibility(View.INVISIBLE);
		}else{
			city_title_back.setVisibility(View.VISIBLE);
		}

//		mMapView = new MapView(SelectCityActivity.this);
		headView = getLayoutInflater().inflate(R.layout.head_view_select_city, null);
		mCity_gridView = (FirstGridView) headView.findViewById(R.id.city_gridView);
//		select_loading = (LinearLayout) findViewById(R.id.ll_loading);
		select_positioning = (TextView) headView.findViewById(R.id.select_positioning);
		select_positioning.setOnClickListener(this);

		city_title_back.setOnClickListener(this);

		mSearchContainer = findViewById(R.id.search_content_container);
		mCityListView = (PinnedHeaderListView) findViewById(R.id.pinned_header_citys_list);
		mCityListView.addHeaderView(headView);
//		mCityListView.setEmptyView(findViewById(R.id.citys_list_empty));

		citysListEmpty = (LinearLayout) findViewById(R.id.citys_list_empty);

		select_city_layout = (LinearLayout) findViewById(R.id.select_city_layout);
		select_city_activity_netoutview = (LinearLayout) findViewById(R.id.select_city_activity_netoutview);
		
		mLetter = (BladeView) findViewById(R.id.citys_bladeview);

		mLetter.setOnItemClickListener(new BladeView.OnItemClickListener() {

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
		
		mCityListView.setOnItemClickListener(new PinnedHeaderListView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						String cityString = (mCityAdapter.getItem(position - 1)).getNm();
						if (cityString.contains("市") || cityString.contains("县")) {
							cityString = cityString.substring(0, cityString.length() - 1);
						}
//						Util.setToast(context, "-->> " + cityString);
//						CacheManager.setCity(context, cityString); // 城市存本地
						choose = cityString;
						goActivity(cityString);
//						startActivityWithCity(cityString);
					}
				});

//		initBDMap();

		mCity_gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		// 热门城市跳转
		mCity_gridView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String hot = hotCityNames.get(position);
				if (hot.contains("市") || hot.contains("县")) {
					hot = hot.substring(0, hot.length() - 1);
				}
//				Util.setToast(context, "-->> " + hot);
//				CacheManager.setCity(context, hot); // 城市存本地
				choose = hot;
				goActivity(hot);
//				startActivityWithCity(hot);
			}
		});
	}

	private String choose = "";


	private static final String cityUrl = Constant.ZHJ + "tapp/util/change_city";
	private RequestQueue locationQueue;
	private StringRequest locationRequest;
	private void requestSelectCity() {
		locationQueue = Volley.newRequestQueue(context);
		locationRequest = new StringRequest(Request.Method.POST, cityUrl, new Response.Listener<String>() {
			@Override
			public void onResponse(String s) {
				CacheManager.setCityJson(context, s);
				System.out.println("------请求获取json-->>>" + s);
				prseSelectDataJson(s);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError volleyError) {
				System.out.println("-----获取省城市json失败-->>>");
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return null;
			}
		};
		locationRequest.setTag("cityJson");
		locationQueue.add(locationRequest);

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

		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.item_city_gridview, new String[] { "style" }, new int[] { R.id.item_city_tv });
		mCity_gridView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		citysListEmpty.setVisibility(View.GONE);

		initCityList();
	}


	/**
	 * 跳转不同activity
	 *
	 */
	private void goActivity(String ci){
		CacheManager.setCity(context, ci);

		if(from==31){
			Intent cityData = new Intent();
			Bundle b = new Bundle();
			b.putString("ci", ci);
			cityData.putExtra("city_bundle", b);
			setResult(77, cityData);
		}else if(fromCode == 647){
			Intent cityData = new Intent();
			Bundle b = new Bundle();
			b.putString("ci", ci);
			cityData.putExtra("city_bundle", b);
			setResult(70, cityData);
		}else{
			startActivity(new Intent(context, MainActivity.class));
		}
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 不能返回
		finish();
		return true;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.city_title_back:
		case R.id.select_positioning:
			goActivity(locationCity);
			break;
		}

	}


	@Override
	public void onBackPressed() {
		goActivity(choose);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(locationQueue!=null){
			locationQueue.cancelAll("cityJson");
		}
	}



	@Override
	protected void onResume() {
		super.onResume();
		initNetMessage();
	}


	private void initNetMessage() {
		Message m = new Message();
		if(checkNetState()){
			m.what = 0x000012;
		}else{
			m.what = 0x000013;
		}
		netHandler.sendMessage(m);
	}


	private Handler netHandler = new Handler(){
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
						if(!checkNetState()){
							Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
							return;
						}else{
							getCityJson();
						}
					}
				});
				break;
			}
		};
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
