package com.tbs.tbs_mj.utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

/**
 * 逛图库页面请求网络的数据解析工具
 *
 * @author dec
 */
public class PrseImageJsonUtil {
    private static ArrayList<HashMap<String, String>> imagelistDatas;

    public static ArrayList<HashMap<String, String>> parsingJson(String result, Context context, String comid, String flag) {

        imagelistDatas = new ArrayList<HashMap<String, String>>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            Log.d("--PrseImageJsonUtil--", "请求后的数据是【" + result + "】");
            if (jsonObject.getInt("error_code") == 0) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    String id = jsonArray.getJSONObject(i).getString("id");
                    map.put("id", id);
                    String index_image_url = jsonArray.getJSONObject(i).getString("index_image_url");
                    map.put("index_image_url", index_image_url);
                    String check_time = jsonArray.getJSONObject(i).getString("check_time");
                    map.put("check_time", check_time);
                    String title = jsonArray.getJSONObject(i).getString("title");
                    map.put("title", title);

                    String img1 = jsonArray.getJSONObject(i).getString("img1");
                    map.put("img1", img1);
                    String img2 = jsonArray.getJSONObject(i).getString("img2");
                    map.put("img2", img2);
                    if ("DesignChartAcitivity".equals(flag)) {
                        // 设计图册
                        map.put("comsimpname", jsonArray.getJSONObject(i).getString("comsimpname"));
                        map.put("logosmall", jsonArray.getJSONObject(i).getString("logosmall"));
                        map.put("comid", comid);

                    }
                    imagelistDatas.add(map);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return imagelistDatas;
    }
}