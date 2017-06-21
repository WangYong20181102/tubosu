package com.tobosu.mydecorate.util;

/**
 * Created by dec on 2016/10/26.
 */

public class SocailShareUtil {
//    private static final String TAG = SocailShareUtil.class.getSimpleName();
//
//    private Context context;
//
//    private String share_url;
//
//    private String title;
//    private String desc;
//    private String pic_url;
//    private View view;
//
//    private ShareViewPopupWindow shareWindow;
//
//    /**
//     * 选择方式来分享
//     * @param context
//     * @param view
//     * @param article_id
//     * @param title
//     * @param desc
//     * @param pic_url
//     */
//    public SocailShareUtil(Context context, View view, String article_id, String title, String desc, String pic_url){
//        this.context = context;
//        this.share_url = Constant._SHARE_URL + article_id + ".html" + Constant.SOCAIL_SHARE;
//        this.title = title;
//        this.desc = desc;
//        this.pic_url = pic_url;
//        setShareContents();
//        shareWindow = new ShareViewPopupWindow(context, viewClick);
//        shareWindow.showAtLocation(view.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//    }
//
//    /**
//     *
//     * @param context
//     * @param article_id
//     * @param title
//     * @param desc
//     * @param pic_url
//     */
//    public SocailShareUtil(Context context, String type, String article_id, String title, String desc, String pic_url){
//        this.context = context;
//        this.share_url = Constant._SHARE_URL + article_id + ".html" + Constant.SOCAIL_SHARE;
//        this.title = title;
//        this.desc = desc;
//        this.pic_url = pic_url;
//        selecteType(type);
//        setShareContents();
//    }
//
//    private void selecteType(String type){
//        if(type.equals("weixin")){
//            doShare(SHARE_MEDIA.WEIXIN);
//        }else if(type.equals("weixin_circle")){
//            doShare(SHARE_MEDIA.WEIXIN_CIRCLE);
//        }else if(type.equals("weibo")){
//            doShare(SHARE_MEDIA.SINA);
//        }else if(type.equals("weibo")){
//            doShare(SHARE_MEDIA.QZONE);
//        }
//    }
//
//
//    private void setShareContents() {
//
//    }
//
//
//
//
//
//
//    private View.OnClickListener viewClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            switch (view.getId()){
//                case R.id.tv_share_weixin:
//                    doShare(SHARE_MEDIA.WEIXIN);
//                    break;
//                case R.id.tv_share_weixin_circle:
//                    doShare(SHARE_MEDIA.WEIXIN_CIRCLE);
//                    break;
//                case R.id.tv_share_qq_space:
//                    doShare(SHARE_MEDIA.QZONE);
//                    break;
//                case R.id.tv_share_sina:
//                    doShare(SHARE_MEDIA.SINA);
//                    break;
//                default:
//
//                    break;
//            }
//        }
//    };
//
//
////    private void calculateSocialShare(SHARE_MEDIA media){
////        UMPlatformData platform = new UMPlatformData(UMedia.SINA_WEIBO, "user_id");
////        platform.setWeiboId("weiboId");
////        MobclickAgent.onSocialEvent(this, media);
////    }
//
//
//    // 友盟分享的服务
//    private com.umeng.socialize.controller.UMSocialService controller = null;
//
//    private void setShareConfig(){
//        controller = UMServiceFactory.getUMSocialService("com.umeng.share");
//        weixinConfig();
//        qqConfig();
//    }
//
//    private void weixinConfig(){
//        // 添加微信平台
//        UMWXHandler wxHandler = new UMWXHandler(context, Constant.WEIXIN_APP_ID, Constant.WEIXIN_APP_SECRET);
//        wxHandler.addToSocialSDK();
//        // 添加微信朋友圈
//        UMWXHandler wxCircleHandler = new UMWXHandler(context, Constant.WEIXIN_APP_ID, Constant.WEIXIN_APP_SECRET);
//        wxCircleHandler.setToCircle(true);
//        wxCircleHandler.addToSocialSDK();
//    }
//
//    private void qqConfig(){
//        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((AppCompatActivity)context, Constant.QQ_APP_ID, Constant.QQ_APP_SECRET);
//        qqSsoHandler.addToSocialSDK();
//
//        // 添加QQ空间分享平台
//        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((AppCompatActivity)context, Constant.QQ_APP_ID, Constant.QQ_APP_SECRET);
//        qZoneSsoHandler.addToSocialSDK();
//    }
//
//    private void sinaConfig(){
//        controller.getConfig().setSsoHandler(new SinaSsoHandler(this));
//    }
}
