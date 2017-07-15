package com.tbs.tobosutype.adapter.utils;

import com.umeng.socialize.utils.Log;

public class LogUtil {
	static boolean flag = true;

	public static void printDugLog(String Tag, String result) {

		if (flag) {
			Log.d(Tag, result);
		}
	}

	public static void printErrLog(String Tag, String result) {

		if (flag) {
			Log.e(Tag, result);
		}
	}

}
