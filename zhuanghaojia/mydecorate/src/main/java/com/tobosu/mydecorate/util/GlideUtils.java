package com.tobosu.mydecorate.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Mr.Lin on 2017/5/25 09:14.
 */

public class GlideUtils {
    public static final int CIRCLE_IMAGE = 0;//圆形
    public static final int ROUND_IMAGE = 1;//圆角

    /**
     * 处理原生的图片
     *
     * @param context
     * @param imgUrl
     * @param erroImg
     * @param emptyImg
     * @param imageView
     */
    public static void glideLoader(Context context, String imgUrl, int erroImg, int emptyImg, ImageView imageView) {
        Glide.with(context).load(imgUrl).placeholder(emptyImg).error(erroImg).into(imageView);
    }

    /**
     * 处理圆角图片或者圆形图片
     *
     * @param context
     * @param url
     * @param erroImg
     * @param emptyImg
     * @param iv
     * @param imageType      0==圆形图片   1==圆角图片
     */
    public static void glideLoader(Context context, String url, int erroImg, int emptyImg, ImageView iv, int imageType) {
        if (CIRCLE_IMAGE == imageType) {
            Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).transform(new GlideCircleTransform(context)).into(iv);
        } else if (ROUND_IMAGE == imageType) {
            Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).transform(new GlideRoundTransform(context, 10)).into(iv);
        }
    }
}
