package com.tbs.tobosutype.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tbs.tobosutype.R;

public class ToastUtil {
	private static Toast toast;

	public static void showShort(Context context, CharSequence message) {
		if (null == toast) {
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			// toast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			toast.setText(message);
		}
		toast.show();
	}

	public static void showShort(Context context, int message) {
		if (null == toast) {
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			// toast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			toast.setText(message);
		}
		toast.show();
	}

	public static void showLong(Context context, CharSequence message) {
		if (null == toast) {
			toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
		} else {
			toast.setText(message);
		}
		toast.show();
	}

	public static void showLong(Context context, int message) {
		if (null == toast) {
			toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
		} else {
			toast.setText(message);
		}
		toast.show();
	}
	public static void show(Context context, CharSequence message, int duration) {
		if (null == toast) {
			toast = Toast.makeText(context, message, duration);
		} else {
			toast.setText(message);
		}
		toast.show();
	}
	public static void show(Context context, int message, int duration) {
		if (null == toast) {
			toast = Toast.makeText(context, message, duration);
		} else {
			toast.setText(message);
		}
		toast.show();
	}

	public static void hideToast() {
		if (null != toast) {
			toast.cancel();
		}
	}

	/**
	 * 自定义吐司
	 * 屏幕中间显示
	 */
	public static void customizeToast(Context context,String content){
		View view = LayoutInflater.from(context).inflate(R.layout.custommize_toast,null);
		TextView tvToastContent = view.findViewById(R.id.tv_toast_content);	//吐司内容
        tvToastContent.setText(content);
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.CENTER,0,0);   //吐司显示位置
		toast.setDuration(Toast.LENGTH_SHORT);  //吐司显示时间
		toast.setView(view);
		toast.show();
	}
	/**
	 * 自定义吐司
	 * 屏幕中间显示
	 */
	public static void customizeToast1(Context context,String content){
		View view = LayoutInflater.from(context).inflate(R.layout.custommize_toast1,null);
		TextView tvToastContent = view.findViewById(R.id.tv_toast_content);	//吐司内容
        tvToastContent.setText(content);
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.CENTER,0,0);   //吐司显示位置
		toast.setDuration(Toast.LENGTH_SHORT);  //吐司显示时间
		toast.setView(view);
		toast.show();
	}
}
