package com.tbs.tbs_mj.utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


	/***
	 *  找装修页面 解析找到公司的json得到公司数据
	 * @author dec
	 *
	 */
public class PrseJsonUtil {
	
	public static ArrayList<HashMap<String, String>> jsonToComList(String jsonString) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject object = new JSONObject(jsonString);
			if (object.getInt("error_code") == 0) {
				JSONObject object2 = object.getJSONObject("data");
				JSONArray array = object2.getJSONArray("com");
				for (int i = 0; i < array.length(); i++) {
					HashMap<String, String> dataMap = new HashMap<String, String>();
					JSONObject dataObject = array.getJSONObject(i);
					dataMap.put("comid", dataObject.getString("comid"));
					dataMap.put("is_hot", dataObject.getString("is_hot"));
					dataMap.put("comsimpname", dataObject.getString("comsimpname"));
					dataMap.put("logosmall", dataObject.getString("logosmall"));
					dataMap.put("districtid", dataObject.getString("districtid"));
					dataMap.put("address", dataObject.getString("address"));
					dataMap.put("dis", dataObject.getString("dis"));
					dataMap.put("certification", dataObject.getString("certification"));
					dataMap.put("jjb_logo", dataObject.getString("jjb_logo"));
					dataMap.put("dis", dataObject.getString("dis"));
					dataMap.put("casenormalcount", dataObject.getString("casenormalcount"));
					dataMap.put("anliCount", dataObject.getString("anliCount"));
					dataMap.put("activity", dataObject.getString("activity"));
					if ("1".equals(dataObject.getString("activity").toString())) {
						dataMap.put("activityTitle", dataObject.getString("activityTitle"));
					}
					list.add(dataMap);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;

	}
}
