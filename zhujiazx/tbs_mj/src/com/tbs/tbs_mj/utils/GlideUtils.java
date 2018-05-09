package com.tbs.tbs_mj.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.File;

/**
 * Created by Mr.Lin on 2017/6/28 18:06.
 */

public class GlideUtils {
    private static String TAG = "GlideUtils";
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
        Glide.with(context).load(imgUrl).asBitmap().placeholder(emptyImg).error(erroImg).centerCrop().into(imageView);
    }
    public static void glideLoaderUriImage(Context context, Uri imgUri, int erroImg, int emptyImg, ImageView imageView) {
        Glide.with(context).load(imgUri).asBitmap().placeholder(emptyImg).error(erroImg).into(imageView);
    }
    public static void glideLoaderFileImage(Context context, File imgFile, int erroImg, int emptyImg, ImageView imageView) {
        Glide.with(context).load(imgFile).asBitmap().placeholder(emptyImg).error(erroImg).centerCrop().into(imageView);
    }
    /**
     * 处理圆角图片或者圆形图片
     *
     * @param context
     * @param url
     * @param erroImg
     * @param emptyImg
     * @param iv
     * @param imageType 0==圆形图片 1==圆角图片
     */
    public static void glideLoader(final Context context, String url, int erroImg, int emptyImg, final ImageView iv, int imageType) {
        if (CIRCLE_IMAGE == imageType) {
            Glide.with(context).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    iv.setImageDrawable(circularBitmapDrawable);
                }
            });
        } else if (ROUND_IMAGE == imageType) {
            Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).transform(new GlideRoundTransform(context, 6)).into(iv);
        }
    }

    public static void glideLoader(final Context context, Uri uri, int erroImg, int emptyImg, final ImageView iv, int imageType) {
        if (CIRCLE_IMAGE == imageType) {
            Glide.with(context).load(uri).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    iv.setImageDrawable(circularBitmapDrawable);
                }
            });
        } else if (ROUND_IMAGE == imageType) {
            Glide.with(context).load(uri).placeholder(emptyImg).error(erroImg).transform(new GlideRoundTransform(context, 6)).into(iv);
        }
    }

    public static void glideLoader(final Context context, int drawableId, int erroImg, int emptyImg, final ImageView iv, int imageType) {
        if (CIRCLE_IMAGE == imageType) {
            Glide.with(context).load(drawableId).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    iv.setImageDrawable(circularBitmapDrawable);
                }
            });
        } else if (ROUND_IMAGE == imageType) {
            Glide.with(context).load(drawableId).placeholder(emptyImg).error(erroImg).transform(new GlideRoundTransform(context, 6)).into(iv);
        }
    }
}
