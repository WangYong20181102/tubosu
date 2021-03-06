package com.tobosu.mydecorate.util;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.application.MyApplication;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.view.SelectPicPopupWindow;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * Created by Mr.Lin on 2017/5/22 13:58.
 */

public class ShareUtil {
    private Context context;

    /**标题*/
    private String title;

    /**分享url*/
    private String fenxiang_url;

    /**文字简短描述*/
    private String desc;

    private UMSocialService mController = UMServiceFactory.getUMSocialService(Constant.DESCRIPTOR);
    private SelectPicPopupWindow popupWindow;

    /***
     * 分享方法
     * @param context 上下文
     * @param view 分享按钮
     * @param title 分享标题
     * @param desc 描述
     * @param url 分享url
     */
    public ShareUtil(Context context, View view, String title, String desc, String url) {
        this.context = context;
        this.title = title;
        System.out.println("====-拼接前 >>"+url);
        this.fenxiang_url = url + Constant.ANDROID_SHARE; //加这个链接
        System.out.println("77777====-分享url拼接后 >>"+fenxiang_url);
        this.desc = desc;
        popupWindow = new SelectPicPopupWindow(context, itemsOnClick);
        popupWindow.showAtLocation(view.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        configPlatforms();
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

        if(flag){
            System.out.println("t====-util 拼接前boolean >>"+url);
            this.fenxiang_url = url; // + AllConstants.M_POP_SHARE; // 装修课堂 精选详情分享 加这个链接
            System.out.println("t====-util 分享url拼接后boolean >>"+fenxiang_url);
        }else{
            System.out.println("f====-util 拼接前boolean >>"+url);
            this.fenxiang_url = url + "?from=app&channel=seo&subchannel=android&chcode="+ Util.getChannType(MyApplication.getContexts())+"&tbsfrom=share";
            //=android&chcode=
            System.out.println("f====-util 分享url拼接后boolean >>"+fenxiang_url);
        }

        this.desc = desc;
        popupWindow = new SelectPicPopupWindow(context, itemsOnClick);
        popupWindow.showAtLocation(view.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        configPlatforms();
        setShareContent();
    }

    private void configPlatforms() {
        addQQQZonePlatform();
        addWXPlatform();
    }

    private void setShareContent() {
        UMImage urlImage = new UMImage(context, R.drawable.app_icon);
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(desc);
        circleMedia.setTitle(title);
        circleMedia.setShareMedia(urlImage);
        circleMedia.setTargetUrl(fenxiang_url);
        mController.setShareMedia(circleMedia);
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(desc);
        weixinContent.setTitle(title);
        weixinContent.setTargetUrl(fenxiang_url);
        weixinContent.setShareMedia(urlImage);
        mController.setShareMedia(weixinContent);
        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(desc);
        qqShareContent.setTitle(title);
        qqShareContent.setShareMedia(urlImage);
        qqShareContent.setTargetUrl(fenxiang_url);
        mController.setShareMedia(qqShareContent);
        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent.setShareContent(desc);
        sinaContent.setTitle(title);
        sinaContent.setShareMedia(urlImage);
        sinaContent.setTargetUrl(fenxiang_url);
        mController.setShareMedia(sinaContent);
    }

    private void addQQQZonePlatform() {
        String appId = "1104922493";
        String appKey = "nkNEZUxxoxa44u7i";
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) context, appId, appKey);
        qqSsoHandler.setTargetUrl(fenxiang_url);
        qqSsoHandler.addToSocialSDK();
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity) context, appId, appKey);
        qZoneSsoHandler.addToSocialSDK();
    }

    private void addWXPlatform() {
        String appId = "wx20c4f4560dcd397a";
        String appSecret = "9b06e848d40bcb04205d75335df6b814";
        UMWXHandler wxHandler = new UMWXHandler(context, appId, appSecret);
        wxHandler.addToSocialSDK();

        UMWXHandler wxCircleHandler = new UMWXHandler(context, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    private void performShare(SHARE_MEDIA platform) {

        mController.postShare(context, platform, new SocializeListeners.SnsPostListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                MobclickAgent.onEvent(context, "click_find_decoration_array_favorite(share/share succeed)");
            }
        });

    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            popupWindow.dismiss();
            switch (v.getId()) {
                case R.id.image_detail_share_weixin_circle:
                    performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                    break;
                case R.id.image_detail_share_weixin:
                    performShare(SHARE_MEDIA.WEIXIN);
                    break;
                case R.id.image_detail_share_sina:
                    performShare(SHARE_MEDIA.SINA);
                    break;
                case R.id.image_detail_share_qq:
                    performShare(SHARE_MEDIA.QQ);
                    break;
                default:
                    break;
            }
        };
    };
}

