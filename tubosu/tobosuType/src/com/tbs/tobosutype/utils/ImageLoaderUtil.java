package com.tbs.tobosutype.utils;

import android.content.Context;
import android.view.View;

import com.lidroid.xutils.BitmapUtils;
import com.tbs.tobosutype.R;

public class ImageLoaderUtil {
	
	public static void loadImage(Context context, View view, String url) {
		BitmapUtils bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDefaultBitmapMaxSize(1024, 0);
		bitmapUtils.configDefaultAutoRotation(true);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.icon_tobosu_default);
		bitmapUtils.configDefaultLoadingImage(R.drawable.icon_tobosu_default);
		bitmapUtils.configDefaultReadTimeout(3000);
		bitmapUtils.display(view, url);
	}

	public static void loadImages(Context context, View view, String url) {
		BitmapUtils bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDefaultBitmapMaxSize(1024, 0);
		bitmapUtils.configDefaultAutoRotation(true);
		bitmapUtils.configDefaultLoadingImage(R.drawable.icon_tobosu_default);
		bitmapUtils.configDefaultReadTimeout(3000);
		bitmapUtils.display(view, url);
	}
}
