package com.tbs.tobosutype.utils;

import android.content.Context;

import com.tbs.tobosutype.global.Constant;

/**
 * Created by Mr.Lin on 2017/11/17 11:17.
 * 存储的工具类
 */

public class SpUtil {
    //单图列表进入详情时的大数据过度 存储
    public static void setSingImageListJson(Context context, String mSingImageListJson) {
        context.getSharedPreferences("SingImageListJson", 0).edit().putString("mSingImageListJson", mSingImageListJson).commit();
    }

    //单图列表进入详情时的大数据过度 获取
    public static String getSingImageListJson(Context context) {
        return context.getSharedPreferences("SingImageListJson", 0).getString("mSingImageListJson", "");
    }

    //套图列表进入详情时的大数据过度 存储
    public static void setDoubleImageListJson(Context context, String mDoubleImageListJson) {
        context.getSharedPreferences("DoubleImageListJson", 0).edit().putString("mDoubleImageListJson", mDoubleImageListJson).commit();
    }

    //套图列表进入详情时的大数据过度 获取
    public static String getDoubleImageListJson(Context context) {
        return context.getSharedPreferences("DoubleImageListJson", 0).getString("mDoubleImageListJson", "");
    }

    //用户是否第一次进入图片查看
    public static void setFristIntoImageLook(Context context, String mFristIntoImageLook) {
        context.getSharedPreferences("FristIntoImageLook", 0).edit().putString("mFristIntoImageLook", mFristIntoImageLook).commit();
    }

    //获取用户的是否第一次进入图片查看器
    public static String getFristIntoImageLook(Context context) {
        return context.getSharedPreferences("FristIntoImageLook", 0).getString("mFristIntoImageLook", "");
    }


    //用户在查看图册时每天一次的发单
    public static void setImageDetailDataToken(Context context, String mImageDetailDataToken) {
        context.getSharedPreferences("ImageDetailDataToken", 0).edit().putString("mImageDetailDataToken", mImageDetailDataToken).commit();
    }

    //用户在查看图册时每天一次的发单
    public static String getImageDetailDataToken(Context context) {
        return context.getSharedPreferences("ImageDetailDataToken", 0).getString("mImageDetailDataToken", "");
    }

    //存储纬度信息
    public static void setLatitude(Context context, String mLatitude) {
        context.getSharedPreferences("mlLatitude", 0).edit().putString("mLatitude", mLatitude).commit();
    }

    //获取纬度信息
    public static String getLatitude(Context context) {
        return context.getSharedPreferences("mlLatitude", 0).getString("mLatitude", "");
    }

    //存储经度信息
    public static void setLongitude(Context context, String mLongitude) {
        context.getSharedPreferences("mlLongitude", 0).edit().putString("mLongitude", mLongitude).commit();
    }

    //获取经度信息
    public static String getLongitude(Context context) {
        return context.getSharedPreferences("mlLongitude", 0).getString("mLongitude", "");
    }

    //存储城市信息
    public static void setCity(Context context, String City) {
        context.getSharedPreferences("mlCity", 0).edit().putString("mCity", City).commit();
    }

    //获取城市信息
    public static String getCity(Context context) {
        return context.getSharedPreferences("mlCity", 0).getString("mCity", "");
    }

    //存储城市信息
    public static void setRadius(Context context, String Radius) {
        context.getSharedPreferences("mlRadius", 0).edit().putString("mRadius", Radius).commit();
    }

    //获取城市信息
    public static String getRadius(Context context) {
        return context.getSharedPreferences("mlRadius", 0).getString("mRadius", "");
    }

    //存储学装修分类信息
    public static void setArticleType(Context context, String ArticleType) {
        context.getSharedPreferences("mlArticleType", 0).edit().putString("mArticleType", ArticleType).commit();
    }

    //获取学装修分类信息
    public static String getArticleType(Context context) {
        return context.getSharedPreferences("mlArticleType", 0).getString("mArticleType", "");
    }

    //存储推送提醒的时间
    public static void setNoticeTime(Context context, String NoticeTime) {
        context.getSharedPreferences("mNoticeTime", 0).edit().putString("mNoticeTime", NoticeTime).commit();
    }

    //获取推送提醒的时间
    public static String getNoticeTime(Context context) {
        return context.getSharedPreferences("mNoticeTime", 0).getString("mNoticeTime", "");
    }

    //存储用户按的Tab
    public static void setMainTabPosition(Context context, int mMainTabPosition) {
        context.getSharedPreferences("mMainTabPosition", 0).edit().putInt("mMainTabPosition", mMainTabPosition).commit();
    }

    //获取用户按的Tab
    public static int getMainTabPosition(Context context) {
        return context.getSharedPreferences("mMainTabPosition", 0).getInt("mMainTabPosition", 0);
    }


    //存储网络端获取的手机正则校验
    public static void setCellphonePartern(Context context, String mCellphonePartern) {
        context.getSharedPreferences("mCellphonePartern", 0).edit().putString("mCellphonePartern", mCellphonePartern).commit();
    }

    //获取网络端获取的手机正则校验
    public static String getCellphonePartern(Context context) {
        return context.getSharedPreferences("mCellphonePartern", 0).getString("mCellphonePartern", "");
    }


    //存储用户在App升级是做的删除信息操作
    public static void setCleanUserInfoFlag(Context context, String flag) {
        context.getSharedPreferences("mCleanUserInfoFlag", 0).edit().putString("mCleanUserInfoFlag", flag).commit();
    }

    //获取用户在App升级是做的删除信息操作
    public static String getCleanUserInfoFlag(Context context) {
        return context.getSharedPreferences("mCleanUserInfoFlag", 0).getString("mCleanUserInfoFlag", "");
    }

    //首页每天的弹窗逻辑
    //设置每天的token标识
    public static void setTodayToken(Context context, String mTodayToken) {
        context.getSharedPreferences("mTodayToken", 0).edit().putString("mTodayToken", mTodayToken).commit();
    }

    //获取每天的token标识
    public static String getTodayToken(Context context) {
        return context.getSharedPreferences("mTodayToken", 0).getString("mTodayToken", "");
    }

    //弹窗数据
    public static void cleanDialogInfo(Context context) {
        context.getSharedPreferences("dialogInfo", 0).edit().clear().commit();
    }

    //是否弹更新弹窗
    public static String getIsShowUpdataDialog(Context context) {
        return context.getSharedPreferences("dialogInfo", 0).getString("updataDialog", "");
    }

    public static void setIsShowUpdataDialog(Context context, String updataDialog) {
        context.getSharedPreferences("dialogInfo", 0).edit().putString("updataDialog", updataDialog).commit();
    }

    //是否弹运营活动弹窗
    public static String getIsShowActivityDialog(Context context) {
        return context.getSharedPreferences("dialogInfo", 0).getString("activityDialog", "");
    }

    public static void setIsShowActivityDialog(Context context, String activityDialog) {
        context.getSharedPreferences("dialogInfo", 0).edit().putString("activityDialog", activityDialog).commit();
    }

    //是否弹推送弹窗
    public static String getIsShowPushDialog(Context context) {
        return context.getSharedPreferences("dialogInfo", 0).getString("pushDialog", "");
    }

    public static void setIsShowPushDialog(Context context, String pushDialog) {
        context.getSharedPreferences("dialogInfo", 0).edit().putString("pushDialog", pushDialog).commit();
    }

    //首页 和装修公司主页用的城市信息  首页或者是装修公司页面修改了城市的信息得以同步
    public static String getHomeAndCompanyUsingCity(Context context) {
        return context.getSharedPreferences("mHomeAndCompanyUsingCity", 0).getString("mHomeAndCompanyUsingCity", "");
    }

    public static void setHomeAndCompanyUsingCity(Context context, String mHomeAndCompanyUsingCity) {
        context.getSharedPreferences("mHomeAndCompanyUsingCity", 0).edit().putString("mHomeAndCompanyUsingCity", mHomeAndCompanyUsingCity).commit();
    }

    // TODO: 2018/3/1  统计相关的数据**********************************************************************************************************↓
    //存储统计的访问时间
    public static long getStatisticsEventVistTime(Context context) {
        return context.getSharedPreferences("StatisticsEvent", 0).getLong("m_vist_time", 0);
    }

    //获取统计的访问时间
    public static void setStatisticsEventVistTime(Context context, long vist_time) {
        context.getSharedPreferences("StatisticsEvent", 0).edit().putLong("m_vist_time", vist_time).commit();
    }

    //存储统计的离开时间
    public static long getStatisticsEventLeaveTime(Context context) {
        return context.getSharedPreferences("StatisticsEvent", 0).getLong("m_leave_time", 0);
    }

    //获取统计的离开时间
    public static void setStatisticsEventLeaveTime(Context context, long leave_time) {
        context.getSharedPreferences("StatisticsEvent", 0).edit().putLong("m_leave_time", leave_time).commit();
    }

    //存储统计的from_where
    public static String getStatisticsEventFromWhere(Context context) {
        return context.getSharedPreferences("StatisticsEvent", 0).getString("m_from_where", "");
    }

    //获取统计的from_where
    public static void setStatisticsEventFromWhere(Context context, String from_where) {
        context.getSharedPreferences("StatisticsEvent", 0).edit().putString("m_from_where", from_where).commit();
    }

    //存储统计的页面的pageId
    public static String getStatisticsEventPageId(Context context) {
        return context.getSharedPreferences("StatisticsEvent", 0).getString("m_page_id", "");
    }

    //获取统计的from_where
    public static void setStatisticsEventPageId(Context context, String page_id) {
        context.getSharedPreferences("StatisticsEvent", 0).edit().putString("m_page_id", page_id).commit();
    }

    // TODO: 2018/3/1  统计相关的数据**********************************************************************************************************↑
    //存储推送的唯一标识
    public static String getPushRegisterId(Context context) {
        return context.getSharedPreferences("PushRegisterId", 0).getString("mPushRegisterId", "");
    }

    //获取推送的唯一标识
    public static void setPushRegisterId(Context context, String mPushRegisterId) {
        context.getSharedPreferences("PushRegisterId", 0).edit().putString("mPushRegisterId", mPushRegisterId).commit();
    }


    //获取官网电话
    public static String getCustom_service_tel(Context context) {
        return context.getSharedPreferences("CustomServiceTel", 0).getString("custom_service_tel", "400-696-2221");
    }

    //存储官网电话
    public static void setCustom_service_tel(Context context, String custom_service_tel) {
        context.getSharedPreferences("CustomServiceTel", 0).edit().putString("custom_service_tel", custom_service_tel).commit();
    }


    //获取官网QQ
    public static String getCustom_service_qq(Context context) {
        return context.getSharedPreferences("CustomServiceQQ", 0).getString("custom_service_qq", "4006062221");
    }

    //存储官网QQ
    public static void setCustom_service_qq(Context context, String custom_service_qq) {
        context.getSharedPreferences("CustomServiceQQ", 0).edit().putString("custom_service_qq", custom_service_qq).commit();
    }

    //获取小程序
    public static String getApplets_name(Context context) {
        return context.getSharedPreferences("AppletsName", 0).getString("applets_name", "土拨鼠查订单");
    }

    //存储小程序
    public static void setApplets_name(Context context, String applets_name) {
        context.getSharedPreferences("AppletsName", 0).edit().putString("applets_name", applets_name).commit();
    }


    //获取公众号
    public static String getPublic_number(Context context) {
        return context.getSharedPreferences("Public_number", 0).getString("public_number", "itobosu");
    }

    //存储公众号
    public static void setPublic_number(Context context, String public_number) {
        context.getSharedPreferences("Public_number", 0).edit().putString("public_number", public_number).commit();
    }

    //获取城市JSON
    public static String getLocalCityJson(Context context) {
        return context.getSharedPreferences("LocalCityJson", 0).getString("mLocalCityJson", "");
    }

    //存储城市JSON
    public static void setLocalCityJson(Context context, String localCityJson) {
        context.getSharedPreferences("LocalCityJson", 0).edit().putString("mLocalCityJson", localCityJson).commit();
    }

    //获取省份option
    public static int getShengOption(Context context) {
        return context.getSharedPreferences("ShengOption", 0).getInt("mShengOption", 0);
    }

    //存储省份option
    public static void setShengOption(Context context, int mShengOption) {
        context.getSharedPreferences("ShengOption", 0).edit().putInt("mShengOption", mShengOption).commit();
    }

    //获取城市option
    public static int getShiOption(Context context) {
        return context.getSharedPreferences("ShiOption", 0).getInt("mShiOption", 0);
    }

    //存储城市option
    public static void setShiOption(Context context, int mShiOption) {
        context.getSharedPreferences("ShiOption", 0).edit().putInt("mShiOption", mShiOption).commit();
    }

    //获取区域option
    public static int getQuOption(Context context) {
        return context.getSharedPreferences("QuOption", 0).getInt("mQuOption", 0);
    }

    //存储城市option
    public static void setQuOption(Context context, int mQuOption) {
        context.getSharedPreferences("QuOption", 0).edit().putInt("mQuOption", mQuOption).commit();
    }

    // TODO: 2018/5/18  App发单地址********根据获取配置表的信息去取最新的值******** App的发单地址 33个地址
    //todo 1.首页-免费报价       tbsaj01
    //获取
    public static String getTbsAj01(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj01", Constant.FREE_PRICE_PAGE);
    }

    //存储
    public static void setTbsAj01(Context context, String tbsaj01) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj01", tbsaj01).commit();
    }

    //todo 2.首页-免费设计       tbsaj02
    public static String getTbsAj02(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj02", Constant.QUOTE);
    }

    //存储
    public static void setTbsAj02(Context context, String tbsaj02) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj02", tbsaj02).commit();
    }

    //todo 3.首页-专业推荐       tbsaj03
    public static String getTbsAj03(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj03", Constant.REC_COMPANY);
    }

    //存储
    public static void setTbsAj03(Context context, String tbsaj03) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj03", tbsaj03).commit();
    }

    //todo 4.首页-装修大礼包     tbsaj04
    public static String getTbsAj04(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj04", Constant.COMPANY_GIFT);
    }

    //存储
    public static void setTbsAj04(Context context, String tbsaj04) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj04", tbsaj04).commit();
    }

    //todo 5.首页-免费量房       tbsaj05
    public static String getTbsAj05(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj05", Constant.COMPANY_GIFT);
    }

    //存储
    public static void setTbsAj05(Context context, String tbsaj05) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj05", tbsaj05).commit();
    }

    //todo 6.首页-滚动信息-免费设计    tbsaj06
    public static String getTbsAj06(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj06", Constant.QUOTE);
    }

    //存储
    public static void setTbsAj06(Context context, String tbsaj06) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj06", tbsaj06).commit();
    }

    //todo 7.首页-滚动信息-免费报价    tbsaj07
    public static String getTbsAj07(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj07", Constant.FREE_PRICE_PAGE);
    }

    //存储
    public static void setTbsAj07(Context context, String tbsaj07) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj07", tbsaj07).commit();
    }

    //todo 8.首页-滚动信息-专业推荐    tbsaj08
    public static String getTbsAj08(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj08", Constant.COMPANY_GIFT);
    }

    //存储
    public static void setTbsAj08(Context context, String tbsaj08) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj08", tbsaj08).commit();
    }

    //todo 9.首页-滚动信息-大礼包     tbsaj09
    public static String getTbsAj09(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj09", Constant.REC_COMPANY);
    }

    //存储
    public static void setTbsAj09(Context context, String tbsaj09) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj09", tbsaj09).commit();
    }

    //todo 10.效果图列表-弹窗    tbsaj10
    public static String getTbsAj10(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj10", Constant.QUOTE);
    }

    //存储
    public static void setTbsAj10(Context context, String tbsaj10) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj10", tbsaj10).commit();
    }

    //todo 11.效果图详情(单图)弹窗-免费设计    tbsaj11
    public static String getTbsAj11(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj11", Constant.QUOTE);
    }

    //存储
    public static void setTbsAj11(Context context, String tbsaj11) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj11", tbsaj11).commit();
    }

    //todo 12.效果图详情(套图)弹窗-免费报价    tbsaj12
    public static String getTbsAj12(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj12", Constant.FREE_PRICE_PAGE);
    }

    //存储
    public static void setTbsAj12(Context context, String tbsaj12) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj12", tbsaj12).commit();
    }

    //todo 13.底部-立即预约    tbsaj13
    public static String getTbsAj13(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj13", Constant.QUOTE);
    }

    //存储
    public static void setTbsAj13(Context context, String tbsaj13) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj13", tbsaj13).commit();
    }

    //todo 14.装修公司列表-弹窗     tbsaj14
    public static String getTbsAj14(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj14", Constant.FREE_PRICE_PAGE);
    }

    //存储
    public static void setTbsAj14(Context context, String tbsaj14) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj14", tbsaj14).commit();
    }

    //todo 15.装修公司列表-侧边键    tbsaj15
    public static String getTbsAj15(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj15", Constant.REC_COMPANY);
    }

    //存储
    public static void setTbsAj15(Context context, String tbsaj15) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj15", tbsaj15).commit();
    }

    //todo 16.装修公司网店-底部    tbsaj16
    public static String getTbsAj16(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj16", Constant.FREE_PRICE_PAGE);
    }

    //存储
    public static void setTbsAj16(Context context, String tbsaj16) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj16", tbsaj16).commit();
    }

    //todo 17.设计方案列表-获取此设计    tbsaj17
    public static String getTbsAj17(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj17", Constant.QUOTE);
    }

    //存储
    public static void setTbsAj17(Context context, String tbsaj17) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj17", tbsaj17).commit();
    }

    //todo 18.设计方案列表-底部    tbsaj18
    public static String getTbsAj18(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj18", Constant.QUOTE);
    }

    //存储
    public static void setTbsAj18(Context context, String tbsaj18) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj18", tbsaj18).commit();
    }

    //todo 19.装修案列表-底部    tbsaj19
    public static String getTbsAj19(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj19", Constant.FREE_PRICE_PAGE);
    }

    //存储
    public static void setTbsAj19(Context context, String tbsaj19) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj19", tbsaj19).commit();
    }

    //todo 20.装修案例详情-找ta免费设计    tbsaj20
    public static String getTbsAj20(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj20", Constant.QUOTE);
    }

    //存储
    public static void setTbsAj20(Context context, String tbsaj20) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj20", tbsaj20).commit();
    }

    //todo 21.装修案例详情-立即报价    tbsaj21
    public static String getTbsAj21(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj21", Constant.FREE_PRICE_PAGE);
    }

    //存储
    public static void setTbsAj21(Context context, String tbsaj21) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj21", tbsaj21).commit();
    }

    //todo 22.优惠活动-立即领取    tbsaj22
    public static String getTbsAj22(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj22", Constant.PROMOTIONS);
    }

    //存储
    public static void setTbsAj22(Context context, String tbsaj22) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj22", tbsaj22).commit();
    }

    //todo 23.设计团队列表-找TA免费设计    tbsaj23
    public static String getTbsAj23(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj23", Constant.QUOTE);
    }

    //存储
    public static void setTbsAj23(Context context, String tbsaj23) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj23", tbsaj23).commit();
    }

    //todo 24.设计团队列表页-底部    tbsaj24
    public static String getTbsAj24(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj24", Constant.QUOTE);
    }

    //存储
    public static void setTbsAj24(Context context, String tbsaj24) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj24", tbsaj24).commit();
    }

    //todo 25.设计师个人页-找TA免费设计    tbsaj25
    public static String getTbsAj25(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj25", Constant.QUOTE);
    }

    //存储
    public static void setTbsAj25(Context context, String tbsaj25) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj25", tbsaj25).commit();
    }

    //todo 26.设计师个人页-获取此设计    tbsaj26
    public static String getTbsAj26(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj26", Constant.QUOTE);
    }

    //存储
    public static void setTbsAj26(Context context, String tbsaj26) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj26", tbsaj26).commit();
    }

    //todo 27.设计师个人页-获取报价    tbsaj27
    public static String getTbsAj27(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj27", Constant.FREE_PRICE_PAGE);
    }

    //存储
    public static void setTbsAj27(Context context, String tbsaj27) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj27", tbsaj27).commit();
    }

    //todo 28.我的-限量领取    tbsaj28
    public static String getTbsAj28(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj28", Constant.COMPANY_GIFT);
    }

    //存储
    public static void setTbsAj28(Context context, String tbsaj28) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj28", tbsaj28).commit();
    }

    //todo 29.学装修首页-底部    tbsaj29
    public static String getTbsAj29(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj29", Constant.FREE_PRICE_PAGE);
    }

    //存储
    public static void setTbsAj29(Context context, String tbsaj29) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj29", tbsaj29).commit();
    }

    //todo 30.学装修侧边-红包    tbsaj30
    public static String getTbsAj30(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj30", Constant.QUOTE);
    }

    //存储
    public static void setTbsAj30(Context context, String tbsaj30) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj30", tbsaj30).commit();
    }

    //todo 31.文章详情页-侧边    tbsaj31
    public static String getTbsAj31(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj31", Constant.QUOTE);
    }

    //存储
    public static void setTbsAj31(Context context, String tbsaj31) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj31", tbsaj31).commit();
    }

    //todo 32.专题详情页-底部    tbsaj32
    public static String getTbsAj32(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj32", Constant.FREE_PRICE_PAGE);
    }

    //存储
    public static void setTbsAj32(Context context, String tbsaj32) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj32", tbsaj32).commit();
    }

    //todo 33.看案例列表页-底部    tbsaj33 和按键19一致
    public static String getTbsAj33(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj33", Constant.QUOTE);
    }

    //存储
    public static void setTbsAj33(Context context, String tbsaj33) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj33", tbsaj33).commit();
    }

    // TODO: 2018/9/4 土拨鼠4.0版本 新的按键名称 
    // TODO: 2018/9/4 首页顶部 
    public static String getTbsAj34(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj34", Constant.BUDGET_TEST);
    }

    //存储
    public static void setTbsAj34(Context context, String tbsaj34) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj34", tbsaj34).commit();
    }

    // TODO: 2018/9/4 装修大礼包
    public static String getTbsAj35(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj35", Constant.COMPANY_GIFT);
    }

    //存储
    public static void setTbsAj35(Context context, String tbsaj35) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj35", tbsaj35).commit();
    }

    // TODO: 2018/9/4 免费设计
    public static String getTbsAj36(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj36", Constant.QUOTE);
    }

    //存储
    public static void setTbsAj36(Context context, String tbsaj36) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj36", tbsaj36).commit();
    }

    // TODO: 2018/9/4 免费报价
    public static String getTbsAj37(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj37", Constant.FREE_PRICE_PAGE);
    }

    //存储
    public static void setTbsAj37(Context context, String tbsaj37) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj37", tbsaj37).commit();
    }

    // TODO: 2018/9/4 专业量房
    public static String getTbsAj38(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj38", Constant.COMPANY_GIFT);
    }

    //存储
    public static void setTbsAj38(Context context, String tbsaj38) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj38", tbsaj38).commit();
    }

    // TODO: 2018/9/4 装修公司免费报价
    public static String getTbsAj39(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj39", Constant.FREE_PRICE_PAGE);
    }

    //存储
    public static void setTbsAj39(Context context, String tbsaj39) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj39", tbsaj39).commit();
    }

    // TODO: 2018/9/4 装修公司0元设计
    public static String getTbsAj40(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj40", Constant.FREE_DESIGN);
    }

    //存储
    public static void setTbsAj40(Context context, String tbsaj40) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj40", tbsaj40).commit();
    }

    // TODO: 2018/9/4 装修公司专业推荐
    public static String getTbsAj41(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj41", Constant.REC_COMPANY);
    }

    //存储
    public static void setTbsAj41(Context context, String tbsaj41) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj41", tbsaj41).commit();
    }

    // TODO: 2018/9/4 装修公司预算测试
    public static String getTbsAj42(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("tbsaj42", Constant.BUDGET_TEST);
    }

    //存储
    public static void setTbsAj42(Context context, String tbsaj42) {
        context.getSharedPreferences("BjAj", 0).edit().putString("tbsaj42", tbsaj42).commit();
    }

    //todo 首页的四个发单的图片地址 由后台获取  网络获取存入本地
    //存储1-免费设计图片地址  fadan01
    public static String getNewHomeMianfeishejiImgUrl(Context context) {
        return context.getSharedPreferences("NewHomeImgUrl", 0).getString("mianfeisheji_url", "");
    }

    //获取
    public static void setNewHomeMianfeishejiImgUrl(Context context, String mianfeibaojia_url) {
        context.getSharedPreferences("NewHomeImgUrl", 0).edit().putString("mianfeisheji_url", mianfeibaojia_url).commit();
    }

    //急速报价的url地址  fadan02
    public static String getNewHomeJisubaojiaImgUrl(Context context) {
        return context.getSharedPreferences("NewHomeImgUrl", 0).getString("jisubaojia_url", "");
    }

    //获取
    public static void setNewHomeJisubaojiaImgUrl(Context context, String mianfeisheji_url) {
        context.getSharedPreferences("NewHomeImgUrl", 0).edit().putString("jisubaojia_url", mianfeisheji_url).commit();
    }

    //专业量房  fadan03
    public static String getNewHomeZhuanyeliangfangImgUrl(Context context) {
        return context.getSharedPreferences("NewHomeImgUrl", 0).getString("zhuanyeliangfang_url", "");
    }

    public static void setNewHomeZhuanyeliangfangImgUrl(Context context, String zhuanyetuijian_url) {
        context.getSharedPreferences("NewHomeImgUrl", 0).edit().putString("zhuanyeliangfang_url", zhuanyetuijian_url).commit();
    }


    //大礼包 限时豪礼
    public static String getNewHomeXianshihaoliImgUrl(Context context) {
        return context.getSharedPreferences("NewHomeImgUrl", 0).getString("xianshihaoli_url", "");
    }

    public static void setNewHomeXianshihaoliImgUrl(Context context, String xianshihaoli_url) {
        context.getSharedPreferences("NewHomeImgUrl", 0).edit().putString("xianshihaoli_url", xianshihaoli_url).commit();
    }

    //装修公司模块 免费报价图片的url
    public static String getDecComMianFeiBaoJiaImgUrl(Context context) {
        return context.getSharedPreferences("DecComImgUrl", 0).getString("mianfeibaojia_url", "");
    }

    public static void setDecComMianFeiBaoJiaImgUrl(Context context, String mianfeibaojia_url) {
        context.getSharedPreferences("DecComImgUrl", 0).edit().putString("mianfeibaojia_url", mianfeibaojia_url).commit();
    }

    //装修公司模块 零元设计图片的url
    public static String getDecComLingYuanSheJiImgUrl(Context context) {
        return context.getSharedPreferences("DecComImgUrl", 0).getString("lingyuansheji_url", "");
    }

    public static void setDecComLingYuanSheJiImgUrl(Context context, String lingyuansheji_url) {
        context.getSharedPreferences("DecComImgUrl", 0).edit().putString("lingyuansheji_url", lingyuansheji_url).commit();
    }

    //装修公司模块 专业推荐图片的url
    public static String getDecComZhuanYeTuiJianImgUrl(Context context) {
        return context.getSharedPreferences("DecComImgUrl", 0).getString("zhuanyetuijian_url", "");
    }

    public static void setDecComZhuanYeTuiJianImgUrl(Context context, String zhuanyetuijian_url) {
        context.getSharedPreferences("DecComImgUrl", 0).edit().putString("zhuanyetuijian_url", zhuanyetuijian_url).commit();
    }

    //预算测试
    public static String getDecComYuSuanCeiShiImgUrl(Context context) {
        return context.getSharedPreferences("DecComImgUrl", 0).getString("yusuanceshi_url", "");
    }

    public static void setDecComYuSuanCeiShiImgUrl(Context context, String yusuanceshi_url) {
        context.getSharedPreferences("DecComImgUrl", 0).edit().putString("yusuanceshi_url", yusuanceshi_url).commit();
    }

    //装修公司页面的筛选条件
    public static String getDecComCheckInfo(Context context) {
        return context.getSharedPreferences("DecComCheckInfo", 0).getString("DecComCheckInfo", "");
    }

    public static void setDecComCheckInfo(Context context, String mDecComCheckInfo) {
        context.getSharedPreferences("DecComCheckInfo", 0).edit().putString("DecComCheckInfo", mDecComCheckInfo).commit();
    }
}
