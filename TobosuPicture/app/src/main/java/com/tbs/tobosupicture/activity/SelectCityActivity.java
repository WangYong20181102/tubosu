package com.tbs.tobosupicture.activity;
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
import com.tbs.tobosupicture.MyApplication;
import com.tbs.tobosupicture.R;
import com.baidu.location.LocationClient;
import com.tbs.tobosupicture.adapter.CityAdapter;
import com.tbs.tobosupicture.bean.City;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.CityData;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;
import com.tbs.tobosupicture.view.BladeView;
import com.tbs.tobosupicture.view.MyGridView;
import com.tbs.tobosupicture.view.PinnedHeaderListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 选择城市 页
 *
 * @author dec
 *
 */
public class SelectCityActivity extends Activity implements OnClickListener {
	private static final String TAG = SelectCityActivity.class.getSimpleName();

	private Context context;
	private View mSearchContainer;

	private PinnedHeaderListView mCityListView;
	private LinearLayout citysListEmpty;

	/***右边字母列表view*/
	private BladeView mLetter;

	private ListView mSearchListView;
	private List<City> mCities;
	private CityAdapter mCityAdapter;
	private List<String> mSections;
	private Map<String, List<City>> mMap;
	private List<Integer> mPositions;
	private Map<String, Integer> mIndexer;

	private int from = -1;

	/**热门城市gridview*/
	private MyGridView mCity_gridView;

	/***定位中的提示信息*/
	private TextView select_positioning;

	/***退出或返回上一层*/
	private ImageView city_title_back;

	private View headView;

	private List<String> hotCityNames = new ArrayList<String>();

	/**选择城市页面的所有布局*/
	private LinearLayout select_city_layout;

	private LinearLayout select_city_activity_netoutview;

	private int fromCode = 101;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectcity);
		context = SelectCityActivity.this;

		initView();
		getCityJson();
		getDataIntent();

	}

	private void getDataIntent(){
		if(getIntent()!=null && getIntent().getBundleExtra("GetPriceSelectcityBundle")!=null){
			fromCode = Integer.parseInt(getIntent().getBundleExtra("GetPriceSelectcityBundle").getString("fromGetPrice"));
		}
	}




	private void getCityJson(){
		if("".equals(cityJsonString)){
			requestSelectCity();
		}else{
			prseSelectDataJson(SpUtils.getCacheLocalCityJson(context));
		}
	}

	private void requestSelectCity(){
        if(Utils.isNetAvailable(context)){
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("token", Utils.getDateToken());
            HttpUtils.doPost(UrlConstans.HOT_CITY_URL, hashMap, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Utils.setErrorLog(TAG, e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String json = response.body().string();
					Utils.setErrorLog(TAG, json);
					SpUtils.setCacheLocalCityJson(context, json);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            prseSelectDataJson(json);
                        }
                    });
                }
            });
        }else {

        }

    }


    private String cityJsonString = "";

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
		mCityList = mCityData.getAllCity(cityJsonString);
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
		headView = getLayoutInflater().inflate(R.layout.head_view_select_city, null);
		mCity_gridView = (MyGridView) headView.findViewById(R.id.city_gridView);
		select_positioning = (TextView) headView.findViewById(R.id.select_positioning);
		String s = SpUtils.getLocationCity(context);
		String cit = "";
		if(s.contains("市") || s.contains("县")){
			cit = s.substring(0, s.length()-1);
		}else {
			cit = s;
		}
//		Utils.setToast(context, s);
		select_positioning.setText(cit);
		select_positioning.setOnClickListener(this);

		city_title_back.setOnClickListener(this);

		mSearchContainer = findViewById(R.id.search_content_container);
		mCityListView = (PinnedHeaderListView) findViewById(R.id.pinned_header_citys_list);
		mCityListView.addHeaderView(headView);

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
						choose = cityString;
						goActivity(cityString);
					}
				});


		mCity_gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		// 热门城市跳转
		mCity_gridView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String hot = hotCityNames.get(position);
				if (hot.contains("市") || hot.contains("县")) {
					hot = hot.substring(0, hot.length() - 1);
				}
				choose = hot;
				goActivity(hot);
			}
		});

//        select_positioning.setText(SpUtils.getLocationCity(context));
	}

	private String choose = "";

	private void prseSelectDataJson(String result) {
		try {
			JSONObject object = new JSONObject(result);
			if (object.getInt("status") == 200) {
				cityJsonString = result;
				JSONObject data = object.getJSONObject("data");

//				JSONArray arr = data.getJSONArray("opened_city");
//				for(int i=0;i<arr.length();i++){
//
//				}


				JSONArray hotArr = data.getJSONArray("popular_city");
				for(int i=0;i<hotArr.length();i++){
					hotCityNames.add(hotArr.getJSONObject(i).getString("city_name"));
				}

//				JSONArray array = object.getJSONArray("data");
//				for (int i = 0; i < array.length(); i++) {
//					JSONObject dataObject = array.getJSONObject(i);
//					String _cityName = dataObject.getString("simpname");
//					String hotFlag = dataObject.getString("hot_flag");
//					if (hotFlag.equals("1")) {
//						hotCityNames.add(_cityName);
//					}
//				}
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
			goActivity(SpUtils.getBaiduLocationCity(context));
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
