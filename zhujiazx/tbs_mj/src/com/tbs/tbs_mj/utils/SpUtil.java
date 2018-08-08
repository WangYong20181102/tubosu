package com.tbs.tbs_mj.utils;

import android.content.Context;

import com.tbs.tbs_mj.global.Constant;

/**
 * Created by Mr.Lin on 2017/11/17 11:17.
 * 存储的工具类
 */

public class SpUtil {

    //存储引导发单城市信息
    public static void setFdCity(Context context, String City) {
        context.getSharedPreferences("mFdCity", 0).edit().putString("mFdCity", City).commit();
    }

    //获取引导发单城市信息
    public static String getFdCity(Context context) {
        return context.getSharedPreferences("mFdCity", 0).getString("mFdCity", "");
    }


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

    // TODO: 2018/3/1  统计相关的数据
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

    // TODO: 2018/5/7  App发单地址********根据获取配置表的信息去取最新的值********
    //todo 1.首页-免费报价       zjzxaj01
    //获取
    public static String getzjzxaj01(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj01", Constant.FREE_PRICE_PAGE);
    }

    //存储
    public static void setzjzxaj01(Context context, String zjzxaj01) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj01", zjzxaj01).commit();
    }

    //todo 2.首页-免费设计       zjzxaj02
    public static String getzjzxaj02(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj02", Constant.QUOTE);
    }

    //存储
    public static void setzjzxaj02(Context context, String zjzxaj02) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj02", zjzxaj02).commit();
    }

    //todo 3.首页-专业推荐       zjzxaj03
    public static String getzjzxaj03(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj03", Constant.REC_COMPANY);
    }

    //存储
    public static void setzjzxaj03(Context context, String zjzxaj03) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj03", zjzxaj03).commit();
    }

    //todo 4.首页-装修大礼包     zjzxaj04
    public static String getzjzxaj04(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj04", Constant.FREE_DESIGN);
    }

    //存储
    public static void setzjzxaj04(Context context, String zjzxaj04) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj04", zjzxaj04).commit();
    }

    //todo 5.首页-滚动信息-免费设计    zjzxaj05
    public static String getzjzxaj05(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj05", Constant.QUOTE);
    }

    //存储
    public static void setzjzxaj05(Context context, String zjzxaj05) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj05", zjzxaj05).commit();
    }

    //todo 6.首页-滚动信息-免费报价    zjzxaj06
    public static String getzjzxaj06(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj06", Constant.FREE_PRICE_PAGE);
    }

    //存储
    public static void setzjzxaj06(Context context, String zjzxaj06) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj06", zjzxaj06).commit();
    }

    //todo 7.首页-滚动信息-大礼包    zjzxaj07
    public static String getzjzxaj07(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj07", Constant.COMPANY_GIFT);
    }

    //存储
    public static void setzjzxaj07(Context context, String zjzxaj07) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj0", zjzxaj07).commit();
    }

    //todo 8.首页-滚动信息-专业推荐     zjzxaj08
    public static String getzjzxaj08(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj08", Constant.REC_COMPANY);
    }

    //存储
    public static void setzjzxaj08(Context context, String zjzxaj08) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj08", zjzxaj08).commit();
    }

    //todo 9.效果图列表-弹窗-我家装成什么样    zjzxaj09
    public static String getzjzxaj09(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj09", Constant.QUOTE);
    }

    //存储
    public static void setzjzxaj09(Context context, String zjzxaj09) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj09", zjzxaj09).commit();
    }

    //todo 10.效果图详情(单图)弹窗-免费设计    zjzxaj10
    public static String getzjzxaj10(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj10", Constant.QUOTE);
    }

    //存储
    public static void setzjzxaj10(Context context, String zjzxaj10) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj10", zjzxaj10).commit();
    }

    //todo 11.效果图详情(套图)弹窗-获取报价    zjzxaj11
    public static String getzjzxaj11(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj11", Constant.FREE_PRICE_PAGE);
    }

    //存储
    public static void setzjzxaj11(Context context, String zjzxaj11) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj11", zjzxaj11).commit();
    }

    //todo 12.底部-立即预约    zjzxaj12
    public static String getzjzxaj12(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj12", Constant.QUOTE);
    }

    //存储
    public static void setzjzxaj12(Context context, String zjzxaj12) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj12", zjzxaj12).commit();
    }

    //todo 13.装修公司列表-弹窗—我家装修花多少钱    zjzxaj13
    public static String getzjzxaj13(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj13", Constant.FREE_PRICE_PAGE);
    }

    //存储
    public static void setzjzxaj13(Context context, String zjzxaj13) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj13", zjzxaj13).commit();
    }

    //todo 14.装修公司列表-侧边键    zjzxaj14
    public static String getzjzxaj14(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj14", Constant.REC_COMPANY);
    }

    //存储
    public static void setzjzxaj14(Context context, String zjzxaj14) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj14", zjzxaj14).commit();
    }

    //todo 15.装修公司网店-底部    zjzxaj15
    public static String getzjzxaj15(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj15", Constant.FREE_PRICE_PAGE);
    }

    //存储
    public static void setzjzxaj15(Context context, String zjzxaj15) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj15", zjzxaj15).commit();
    }

    //todo 16.设计方案列表-获取此设计    zjzxaj16
    public static String getzjzxaj16(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj16", Constant.QUOTE);
    }

    //存储
    public static void setzjzxaj16(Context context, String zjzxaj16) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj16", zjzxaj16).commit();
    }

    //todo 17.设计方案列表-底部    zjzxaj17
    public static String getzjzxaj17(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj17", Constant.QUOTE);
    }

    //存储
    public static void setzjzxaj17(Context context, String zjzxaj17) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj17", zjzxaj17).commit();
    }

    //todo 18.装修案列表-底部 0元品质   zjzxaj18
    public static String getzjzxaj18(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj18", Constant.FREE_PRICE_PAGE);
    }

    //存储
    public static void setzjzxaj18(Context context, String zjzxaj18) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj18", zjzxaj18).commit();
    }

    //todo 19.装修案例详情-找ta免费设计    zjzxaj19
    public static String getzjzxaj19(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj19", Constant.QUOTE);
    }

    //存储
    public static void setzjzxaj19(Context context, String zjzxaj19) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj19", zjzxaj19).commit();
    }

    //todo 20.装修案例详情-立即报价    zjzxaj20
    public static String getzjzxaj20(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj20", Constant.FREE_PRICE_PAGE);
    }

    //存储
    public static void setzjzxaj20(Context context, String zjzxaj20) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj20", zjzxaj20).commit();
    }

    //todo 21.优惠活动-立即领取    zjzxaj21
    public static String getzjzxaj21(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj21", Constant.PROMOTIONS);
    }

    //存储
    public static void setzjzxaj21(Context context, String zjzxaj21) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj21", zjzxaj21).commit();
    }

    //todo 22.设计团队列表-找TA免费设计    zjzxaj22
    public static String getzjzxaj22(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj22", Constant.QUOTE);
    }

    //存储
    public static void setzjzxaj22(Context context, String zjzxaj22) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj22", zjzxaj22).commit();
    }

    //todo 23.设计团队列表页-底部    zjzxaj23
    public static String getzjzxaj23(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj23", Constant.QUOTE);
    }

    //存储
    public static void setzjzxaj23(Context context, String zjzxaj23) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj23", zjzxaj23).commit();
    }

    //todo 24.设计师个人页-找TA免费设计    zjzxaj24
    public static String getzjzxaj24(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj24", Constant.QUOTE);
    }

    //存储
    public static void setzjzxaj24(Context context, String zjzxaj24) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj24", zjzxaj24).commit();
    }

    //todo 25.设计师个人页-获取此设计    zjzxaj25
    public static String getzjzxaj25(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj25", Constant.QUOTE);
    }

    //存储
    public static void setzjzxaj25(Context context, String zjzxaj25) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj25", zjzxaj25).commit();
    }

    //todo 26.设计师个人页-装修案例列表-获取报价   zjzxaj26
    public static String getzjzxaj26(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj26", Constant.FREE_PRICE_PAGE);
    }

    //存储
    public static void setzjzxaj26(Context context, String zjzxaj26) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj26", zjzxaj26).commit();
    }

    //todo 27.学装修首页  底部-立即计算  zjzxaj27
    public static String getzjzxaj27(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj27", Constant.FREE_PRICE_PAGE);
    }

    //存储
    public static void setzjzxaj27(Context context, String zjzxaj27) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj27", zjzxaj27).commit();
    }

    //todo 28.学装修首页-红包    zjzxaj28
    public static String getzjzxaj28(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj28", Constant.QUOTE);
    }

    //存储
    public static void setzjzxaj28(Context context, String zjzxaj28) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj28", zjzxaj28).commit();
    }

    //todo 29.文章详情页 侧边抢免费设计   zjzxaj29
    public static String getzjzxaj29(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj29", Constant.QUOTE);
    }

    //存储
    public static void setzjzxaj29(Context context, String zjzxaj29) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj29", zjzxaj29).commit();
    }

    //todo 30.看案例列表页 底部立即报价    zjzxaj30
    public static String getzjzxaj30(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj30", Constant.FREE_PRICE_PAGE);
    }

    //存储
    public static void setzjzxaj30(Context context, String zjzxaj30) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj30", zjzxaj30).commit();
    }

    //todo 31.专题详情页-底部    zjzxaj31
    public static String getzjzxaj31(Context context) {
        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj31", Constant.FREE_PRICE_PAGE);
    }

    //存储
    public static void setzjzxaj31(Context context, String zjzxaj31) {
        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj31", zjzxaj31).commit();
    }

//    //todo 33.看案例列表页-底部    zjzxaj33 和按键19一致
//    public static String getzjzxaj33(Context context) {
//        return context.getSharedPreferences("BjAj", 0).getString("zjzxaj33", Constant.QUOTE);
//    }
//
//    //存储
//    public static void setzjzxaj33(Context context, String zjzxaj33) {
//        context.getSharedPreferences("BjAj", 0).edit().putString("zjzxaj33", zjzxaj33).commit();
//    }

    //首页的四个发单的图片地址 由后台获取  网络获取存入本地
    //存储1-免费报价 图片地址  fadan01
    public static String getNewHomeMianfeibaojiaImgUrl(Context context) {
        return context.getSharedPreferences("NewHomeImgUrl", 0).getString("mianfeibaojia_url", "");
    }

    //获取1-免费报价 图片地址
    public static void setNewHomeMianfeibaojiaImgUrl(Context context, String mianfeibaojia_url) {
        context.getSharedPreferences("NewHomeImgUrl", 0).edit().putString("mianfeibaojia_url", mianfeibaojia_url).commit();
    }

    //免费设计的url地址  fadan02
    public static String getNewHomeMianfeishejiImgUrl(Context context) {
        return context.getSharedPreferences("NewHomeImgUrl", 0).getString("mianfeisheji_url", "");
    }

    //获取1-免费报价 图片地址
    public static void setNewHomeMianfeishejiImgUrl(Context context, String mianfeisheji_url) {
        context.getSharedPreferences("NewHomeImgUrl", 0).edit().putString("mianfeisheji_url", mianfeisheji_url).commit();
    }

    //专业推荐  fadan03
    public static String getNewHomeZhuanyetuijianImgUrl(Context context) {
        return context.getSharedPreferences("NewHomeImgUrl", 0).getString("zhuanyetuijian_url", "");
    }

    //获取1-专业推荐 图片地址
    public static void setNewHomeZhuanyetuijianImgUrl(Context context, String zhuanyetuijian_url) {
        context.getSharedPreferences("NewHomeImgUrl", 0).edit().putString("zhuanyetuijian_url", zhuanyetuijian_url).commit();
    }


    //限时豪礼
    public static String getNewHomeXianshihaoliImgUrl(Context context) {
        return context.getSharedPreferences("NewHomeImgUrl", 0).getString("xianshihaoli_url", "");
    }

    //获取1-专业推荐 图片地址
    public static void setNewHomeXianshihaoliImgUrl(Context context, String xianshihaoli_url) {
        context.getSharedPreferences("NewHomeImgUrl", 0).edit().putString("xianshihaoli_url", xianshihaoli_url).commit();
    }

}
