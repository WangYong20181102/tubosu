package com.tbs.tbs_mj.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.lidroid.xutils.BitmapUtils;
import com.tbs.tbs_mj.R;

public class ImageLoaderUtil {

    public static void loadImage(Context context, View view, String url) {
//		BitmapUtils bitmapUtils = new BitmapUtils(context);
//		bitmapUtils.configDefaultBitmapMaxSize(1024, 0);
//		bitmapUtils.configDefaultAutoRotation(true);
//		bitmapUtils.configDefaultLoadFailedImage(R.drawable.icon_tobosu_default);
//		bitmapUtils.configDefaultLoadingImage(R.drawable.icon_tobosu_default);
//		bitmapUtils.configDefaultReadTimeout(3000);
//		bitmapUtils.display(view, url);

        Glide.with(context).load(url).placeholder(R.drawable.new_home_loading).into((ImageView) view);

    }

    public static void loadImages(Context context, View view, String url) {
//		BitmapUtils bitmapUtils = new BitmapUtils(context);
//		bitmapUtils.configDefaultBitmapMaxSize(1024, 0);
//		bitmapUtils.configDefaultAutoRotation(true);
//		bitmapUtils.configDefaultLoadingImage(R.drawable.icon_tobosu_default);
//		bitmapUtils.configDefaultReadTimeout(3000);
//		bitmapUtils.display(view, url);
        Glide.with(context).load(url).placeholder(R.drawable.new_home_loading).into((ImageView) view);
    }

    //处理圆形图片
    public static void loadCircleImage(final Context context,final ImageView iv, String url) {
        Glide.with(context).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                iv.setImageDrawable(circularBitmapDrawable);
            }
        });
    }
}
