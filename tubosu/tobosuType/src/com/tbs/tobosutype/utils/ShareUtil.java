package com.tbs.tobosutype.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.SelectPicPopupWindow;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.MyApplication;
import com.umeng.analytics.MobclickAgent;
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
     * 文字简短描述
     */
    private String desc;

    //	private UMSocialService mController = UMServiceFactory.getUMSocialService(Constant.DESCRIPTOR_SHARE);
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
        System.out.println("====-拼接前 >>" + url);
        this.fenxiang_url = url + Constant.ANDROID_SHARE; //加这个链接
        System.out.println("77777====-分享url拼接后 >>" + fenxiang_url);
        this.desc = desc;
//		popupWindow = new SelectPicPopupWindow(context, itemsOnClick);
//		popupWindow.showAtLocation(view.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//		configPlatforms();
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
            //=android&chcode=
            System.out.println("f====-util 分享url拼接后boolean >>" + fenxiang_url);
        }

        this.desc = desc;
//		popupWindow = new SelectPicPopupWindow(context, itemsOnClick);
//		popupWindow.showAtLocation(view.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//		configPlatforms();
        setShareContent();
    }

    //	private void configPlatforms() {
//		addQQQZonePlatform();
//		addWXPlatform();
//	}
    //设置分享内容
    private void setShareContent() {
        UMImage urlImage = new UMImage(context, R.drawable.app_icon);
//		CircleShareContent circleMedia = new CircleShareContent();
//		circleMedia.setShareContent(desc);
//		circleMedia.setTitle(title);
//		circleMedia.setShareMedia(urlImage);
//		circleMedia.setTargetUrl(fenxiang_url);
//		mController.setShareMedia(circleMedia);
//		WeiXinShareContent weixinContent = new WeiXinShareContent();
//		weixinContent.setShareContent(desc);
//		weixinContent.setTitle(title);
//		weixinContent.setTargetUrl(fenxiang_url);
//		weixinContent.setShareMedia(urlImage);
//		mController.setShareMedia(weixinContent);
//		QQShareContent qqShareContent = new QQShareContent();
//		qqShareContent.setShareContent(desc);
//		qqShareContent.setTitle(title);
//		qqShareContent.setShareMedia(urlImage);
//		qqShareContent.setTargetUrl(fenxiang_url);
//		mController.setShareMedia(qqShareContent);
//		SinaShareContent sinaContent = new SinaShareContent();
//		sinaContent.setShareContent(desc);
//		sinaContent.setTitle(title);
//		sinaContent.setShareMedia(urlImage);
//		sinaContent.setTargetUrl(fenxiang_url);
//		mController.setShareMedia(sinaContent);
        //新的分享设置
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
//	private void addQQQZonePlatform() {
//		String appId = "1104922493";
//		String appKey = "nkNEZUxxoxa44u7i";
//		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) context, appId, appKey);
//		qqSsoHandler.setTargetUrl(fenxiang_url);
//		qqSsoHandler.addToSocialSDK();
//		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity) context, appId, appKey);
//		qZoneSsoHandler.addToSocialSDK();
//	}
//
//	private void addWXPlatform() {
//		String appId = "wx20c4f4560dcd397a";
//		String appSecret = "9b06e848d40bcb04205d75335df6b814";
//		UMWXHandler wxHandler = new UMWXHandler(context, appId, appSecret);
//		wxHandler.addToSocialSDK();
//
//		UMWXHandler wxCircleHandler = new UMWXHandler(context, appId, appSecret);
//		wxCircleHandler.setToCircle(true);
//		wxCircleHandler.addToSocialSDK();
//	}

//	private void performShare(SHARE_MEDIA platform) {
//
//		mController.postShare(context, platform, new SnsPostListener() {
//
//			@Override
//			public void onStart() {
//
//			}
//
//			@Override
//			public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
//				MobclickAgent.onEvent(context, "click_find_decoration_array_favorite(share/share succeed)");
//			}
//		});
//
//	}

//	private OnClickListener itemsOnClick = new OnClickListener() {
//		@Override
//		public void onClick(View v) {
//			popupWindow.dismiss();
//			switch (v.getId()) {
//			case R.id.image_detail_share_weixin_circle:
//				performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
//				break;
//			case R.id.image_detail_share_weixin:
//				performShare(SHARE_MEDIA.WEIXIN);
//				break;
//			case R.id.image_detail_share_sina:
//				performShare(SHARE_MEDIA.SINA);
//				break;
//			case R.id.image_detail_share_qq:
//				performShare(SHARE_MEDIA.QQ);
//				break;
//			default:
//				break;
//			}
//		};
//	};
}
