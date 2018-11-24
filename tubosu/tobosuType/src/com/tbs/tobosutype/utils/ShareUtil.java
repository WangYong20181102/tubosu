package com.tbs.tobosutype.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.SelectPicPopupWindow;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.MyApplication;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;


/**
 * 集成的分享工具类
 *
 * @author dec
 */
public class ShareUtil {
    private Context context;
    /**
     * 标题
     */
    private String title;
    /**
     * 分享url
     */
    private String fenxiang_url;
    /**
     * 分享图片url
     */
    private String imageUrl = "";
    /**
     * 文字简短描述
     */
    private String desc;
    /**
     * 友盟分享图片
     */
    private UMImage urlImage;

    /***
     * 分享方法
     * @param context 上下文
     * @param title 分享标题
     * @param desc 描述
     * @param url 分享url
     */
    public ShareUtil(Context context, String title, String desc, String url) {
        this.context = context;
        this.title = title;
        System.out.println("====-拼接前 >>" + url);
        this.fenxiang_url = url + Constant.ANDROID_SHARE; //加这个链接
        System.out.println("77777====-分享url拼接后 >>" + fenxiang_url);
        this.desc = desc;
        setShareContent();
    }

    /**
     * 分享带图片
     *
     * @param context  上下文
     * @param title    标题
     * @param desc     内容
     * @param imageUrl 图片url
     * @param url      分享url
     */
    public ShareUtil(Context context, String title, String desc, String imageUrl, String url) {
        this.context = context;
        this.title = title;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.fenxiang_url = url;
        setShareContent();
    }

    /***
     * 分享方法
     * @param context 上下文
     * @param view 分享按钮
     * @param title 分享标题
     * @param desc 描述
     * @param url 分享url
     * @param flag 没有用的
     */
    public ShareUtil(Context context, View view, String title, String desc, String url, boolean flag) {
        this.context = context;
        this.title = title;
        if (flag) {
            System.out.println("t====-util 拼接前boolean >>" + url);
            this.fenxiang_url = url; // + Constant.M_POP_SHARE; // 装修课堂 精选详情分享 加这个链接
            System.out.println("t====-util 分享url拼接后boolean >>" + fenxiang_url);
        } else {
            System.out.println("f====-util 拼接前boolean >>" + url);
            this.fenxiang_url = url + "?from=app&channel=seo&subchannel=android&chcode=" + AppInfoUtil.getChannType(MyApplication.getContext()) + "&tbsfrom=share";
            System.out.println("f====-util 分享url拼接后boolean >>" + fenxiang_url);
        }
        this.desc = desc;
        setShareContent();
    }

    //设置分享内容
    private void setShareContent() {
        if (imageUrl.isEmpty()) {
            urlImage = new UMImage(context, R.drawable.app_icon);   //本地图片
        } else {
            urlImage = new UMImage(context, imageUrl);  //网络图片
        }
        UMWeb web = new UMWeb(fenxiang_url);
        web.setTitle(title);
        web.setThumb(urlImage);
        web.setDescription(desc);
        new ShareAction((Activity) context).withMedia(web)
                .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .setCallback(umShareListener).open();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

        }
    };
}
