package com.tbs.tobosupicture.utils;

import android.content.Context;
import android.text.TextUtils;
import com.tbs.tobosupicture.bean.City;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CityData {
	private Context context;
	private List<HashMap<String, City>> city_list = new ArrayList<HashMap<String, City>>();

	public CityData(Context context) {
		this.context = context;
	}

	public List<City> getAllCity(String json) {
		List<City> list = new ArrayList<City>();
		try {
			JSONObject object = new JSONObject(json);
			if(object.getInt("status") == 200){
				JSONObject cityObject = object.getJSONObject("data");
				JSONArray cities = cityObject.getJSONArray("opened_city");
				int length = cities.length();
				for (int i = 0; i < length; i++) {
					JSONObject cityObj = cities.getJSONObject(i);
					String id = cityObj.getString("city_id");
					String name = cityObj.getString("city_name");
					String py = cityObj.getString("pinyin");
					City city = new City(id, name, py, py.toUpperCase());
					HashMap<String, City> map = new HashMap<String, City>();
					map.put(name, city);
					list.add(city);
					city_list.add(map);
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public City getCity(String city) {
		if (TextUtils.isEmpty(city))
			return null;
		City item = getCityInfo(parseName(city));
		if (item == null) {
			item = getCityInfo(city);
		}
		return item;
	}

	private String parseName(String city) {
		if (city.contains("市")) {
			String subStr[] = city.split("市");
			city = subStr[0];
		} else if (city.contains("县")) {
			String subStr[] = city.split("县");
			city = subStr[0];
		}
		return city;
	}

	private City getCityInfo(String city) {

		for (HashMap<String, City> map : city_list) {
			if (map.containsKey(city)) {
				City item = map.get(city);
				return item;
			}
		}
		return null;
	}



//	// w
//	public List<City> getAllCity1(Context context) {
//		List<City> list = new ArrayList<City>();
//		String cityJson = CacheManager.getCityJson(context);
//		try {
//			JSONObject jsonObject = new JSONObject(cityJson);
//
//			if(jsonObject.getInt("error_code")==0){
//				JSONArray array = jsonObject.getJSONArray("data");
//				for (int i = 0; i < array.length(); i++) {
//					JSONObject cityObj = array.getJSONObject(i);
//					String id = cityObj.getString("cityid");
//					String nm = cityObj.getString("simpname");
//					String py = cityObj.getString("pinyin_sort");
//					City city = new City(id, nm, py, py.toUpperCase());
//					HashMap<String, City> map = new HashMap<String, City>();
//					map.put(nm, city);
//					list.add(city);
//					city_list.add(map);
//				}
//			}
//			return list;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally {
//			System.out.println("----CityData-- getAllCity1");
//		}
//		return null;
//	}

}