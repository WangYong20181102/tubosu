package com.tbs.tobosutype.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtil {
	public static final String CITY_SHAREPRE_FILE = "city";
	private static final String CASH_CITY = "_city";
	private static final String SIMPLE_CLIMATE = "simple_climate";
	private static final String SIMPLE_TEMP = "simple_temp";
	private static final String TIMESAMP = "timesamp";
	private static final String TIME = "time";
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;

	public SharePreferenceUtil(Context context, String file) {
		sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
		editor = sp.edit();
	}
	public void setCity(String city) {
		editor.putString(CASH_CITY, city);
		editor.commit();
	}

	public String getCity() {
		return sp.getString(CASH_CITY, "");
	}
	public void setSimpleClimate(String climate) {
		editor.putString(SIMPLE_CLIMATE, climate);
		editor.commit();
	}

	public String getSimpleClimate() {
		return sp.getString(SIMPLE_CLIMATE, "N/A");
	}
	public void setSimpleTemp(String temp) {
		editor.putString(SIMPLE_TEMP, temp);
		editor.commit();
	}

	public String getSimpleTemp() {
		return sp.getString(SIMPLE_TEMP, "");
	}
	public void setTimeSamp(long time) {
		editor.putLong(TIMESAMP, time);
		editor.commit();
	}

	public long getTimeSamp() {
		return sp.getLong(TIMESAMP, System.currentTimeMillis());
	}
	public void setTime(String time) {
		editor.putString(TIME, time);
		editor.commit();
	}

	public String getTime() {
		return sp.getString(TIME, "");
	}

	//未登录情况下点赞状态（详情页）
	public static boolean getDetailLikeStatus(Context context) {
		return context.getSharedPreferences("likeStatue", 0).getBoolean("detailLikeStatue", false);
	}
	//未登录情况下点赞状态（详情页）
	public static void setDetailLikeStatus(Context context) {
		context.getSharedPreferences("likeStatue", 0).edit().putBoolean("detailLikeStatue", true).commit();
	}
	//未登录情况下点赞状态（评论页）
	public static boolean getCommentLikeStatus(Context context) {
		return context.getSharedPreferences("likeStatue", 0).getBoolean("commentLikeStatus", false);
	}
	//未登录情况下点赞状态（评论页）
	public static void setCommentLikeStatus(Context context) {
		context.getSharedPreferences("likeStatue", 0).edit().putBoolean("commentLikeStatus", true).commit();
	}


}
