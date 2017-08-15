package com.tbs.tobosupicture.utils;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

	public static String getChatTime(long time) {
		return getMinTime(time);
	}

	@SuppressLint("SimpleDateFormat")
	public static String getMinTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
		return format.format(new Date(time));
	}

	
}
