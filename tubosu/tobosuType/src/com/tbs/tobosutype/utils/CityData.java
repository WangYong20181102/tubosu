package com.tbs.tobosutype.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.tbs.tobosutype.model.City;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("DefaultLocale")
public class CityData {
	private Context context;
	private List<HashMap<String, City>> city_list = new ArrayList<HashMap<String, City>>();

	public CityData(Context context) {
		this.context = context;
	}

	public List<City> getAllCity() {
		List<City> list = new ArrayList<City>();
		InputStream is = null;
		try {
			is = context.getAssets().open("cities.json");
			int len = 0;
			byte[] buf = new byte[1024];
			StringBuilder builder = new StringBuilder();
			while ((len = is.read(buf)) != -1) {
				builder.append(new String(buf, 0, len));
			}
			JSONObject json = new JSONObject(builder.toString());
			JSONArray cities = json.getJSONArray("data");
			int length = cities.length();
			for (int i = 0; i < length; i++) {
				JSONObject cityObj = cities.getJSONObject(i);
				String id = cityObj.getString("cityid");
				String nm = cityObj.getString("simpname");
				String py = cityObj.getString("pinyin_sort");
				City city = new City(id, nm, py, py.toUpperCase());
				HashMap<String, City> map = new HashMap<String, City>();
				map.put(nm, city);
				list.add(city);
				city_list.add(map);
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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



	// w
	public List<City> getAllCity1(Context context) {
		List<City> list = new ArrayList<City>();
		String cityJson = CacheManager.getCityJson(context);
		try {
			JSONObject jsonObject = new JSONObject(cityJson);

			if(jsonObject.getInt("error_code")==0){
				JSONArray array = jsonObject.getJSONArray("data");
				for (int i = 0; i < array.length(); i++) {
					JSONObject cityObj = array.getJSONObject(i);
					String id = cityObj.getString("cityid");
					String nm = cityObj.getString("simpname");
					String py = cityObj.getString("pinyin_sort");
					City city = new City(id, nm, py, py.toUpperCase());
					HashMap<String, City> map = new HashMap<String, City>();
					map.put(nm, city);
					list.add(city);
					city_list.add(map);
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			System.out.println("----CityData-- getAllCity1");
		}
		return null;
	}

}