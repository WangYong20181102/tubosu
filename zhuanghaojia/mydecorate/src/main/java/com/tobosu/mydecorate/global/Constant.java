package com.tobosu.mydecorate.global;

import com.tobosu.mydecorate.application.MyApplication;
import com.tobosu.mydecorate.util.AppInfoUtil;
import com.tobosu.mydecorate.util.Util;

/**
 * Created by dec on 2016/9/12.
 * <p>
 * 常量类
 */
public class Constant {
    /**
     * 正式环境
     */
//    public static final String ZHJ = "https://www.tobosu.com/";
    /**
     * 测试环境
     */
    public static final String ZHJ = "http://www.dev.tobosu.com/";

    /**
     * M站 发单跳转 线上环境
     */
//    public static final String M_TOBOSU_URL = "https://m.tobosu.com/";
    /**
     * test 环境
     */
//    public static final String M_TOBOSU_URL = "http://m.test.tobosu.com/szs/";

    /**
     * M站 发单跳转 dev环境
     */
        public static final String M_TOBOSU_URL = "http://m.dev.tobosu.com/";


    //数据点击流上传地址
    public static final String TBS_DATA_STREAM = "https://www.tobosu.com/trace";
    /**
     * 免费设计发单链接
     */
    public static final String FREE_DISIGN_FADAN = M_TOBOSU_URL + "quote?channel=app&subchannel=android&tsbchcode=" + Util.getChannType(MyApplication.getContexts()) + "&app_type=2";
    /**
     * 免费报价链接
     */
    public static final String FREE_BAOJIA_FADAN = M_TOBOSU_URL + "free_price_page?channel=app&subchannel=android&tbschcode=" + Util.getChannType(MyApplication.getContexts()) + "&app_type=2";

    public static final String APP_TYPE = "&app_type=2";

    public static final int CHANGE_USERNAME_RESULTCODE = 0x000014;
    public static final int CHANGE_USERNAME_REQUESTCODE = 0x000015;
    public static final int TAKE_PHOTO_REQUEST_CODE = 0x000016;
    public static final int PICK_PHOTO_REQUEST_CODE = 0x000017;
    public static final int CHANGE_USER_INFO_REQUESTCODE = 0x0000018;
    public static final int LOGIN_TO_USERACCOUNT_LOGIN_REQUESTCODE = 0x0000019;
    public static final int WEIXIN_LOGIN_RESULTCODE = 0x0000020;
    public static final int APP_LOGOUT = 0x0000020;
    public static final int APP_LOGIN = 0x0000023;
    public static final int DETAIL_FINISH_RESULTCODE = 0x0000021;
    public static final int GOBACK_MAINACTIVITY_RESULTCODE = 0x0000022;
    public static final int MAINACTIVITY_REQUESTCODE = 0x0000022;

    public static final int TRANSMIT_TO_LOGIN_REQUESTCODE = 0x0000023;
    public static final int LOGIN_RESULTCODE = 0x0000024;
    public static final int USER_LOGIN_RESULTCODE = 0x0000025;

    public static final int PHOTO_CROP = 0x0000026;

    public static final int POP_RESULTCODE = 0x0000027;

    public static final int HOMEFRAGMENT_REQUESTCODE = 0x0000028;

    public static final int FINISH_MAINACTIVITY = 0x0000030;

    public static final int SEARCH_RESULT_CODE = 0x000031;

    public static final int BIBLE_POP_RESULTCODE = 0x0000032;

    public static final String SEND_STARTCOUNT_ACTION = "com.tobosu.app.start_count";
    public static final String SWITCH_CHANNEL_ACTION = "switch_channel_action";
    public static final String LOGOUT_ACTION_ACTION = "com.tobosu.app.logout_string";
    public static final String ADD_CONCERN_ACTION = "com.tobosu.app.add_concern_string";
    public static final String GO_DECORATE_BIBLE_ACTION = "go_decorate_bible_action_string";

    public static final String WEIXIN_LOGIN_ACTION = "com.tobosu.app.weixin.login";

    public static final String REFRESH_MINEFRAGMENT_DATA_ACITION = "com.tobosu.refresh_mine_data";

    public static final String UMENG_APP_KEY = "5810176707fe6553ed0025da";

    public static final String WEIXIN_APP_ID = "wx241c241a97def740";
    public static final String WEIXIN_APP_SECRET = "fc398b9b80bdcb0c963bc5d9e0c7f90c";

    public static final String GET_TITLE_STRING_LIST_ACTION = "get_title_string_list_action";

    public static final String QQ_APP_ID = "1105797946";
    public static final String QQ_APP_SECRET = "dnh9Eimn3MFeE1KB";

    public static final String SINA_APP_KEY = "2436049926";
    public static final String SINA_APP_SECRET = "0a2ebcc3a4076b3f262a5dd50284eb87";

//    /*----可能不需要----*/
//    public static final String BAIDU_APP_ID = "8839599";
//    public static final String baidu_app_key = "qrwmlkO0lHRXrlxxScTcRs41nH8KSzjj";
//    public static final String BAIDU_APP_SECRET_KEY = "XauSduIo437HiYaK7Yaep0RLeL6tZGGQ";

    public static final String BAIDU_MAP_LOCATION_AK = "P3pKf4iasjNBpe7HG4u2wHDDoR0cmDWa";

    public static final String APP_PACKAGE_SIGNED = "b74a9d676ff9c92ba8128f5d1c42a881";// MD5根据应用包名来生成的一个签名

    public static final String _SHARE_URL = "http://m.tobosu.com/mt/";

    //    public static final String SOCAIL_SHARE = "?channel=seo&subchannel=zhjandroid&chcode="+ Constant.CHANNEL_TYPE +"&tbsfrom=share";
    public static final String SOCAIL_SHARE = "?channel=app&subchannel=android&tbschcode=" + Util.getChannType(MyApplication.getContexts()) + "&tbsfrom=share";

    public static final String XINGE_APP_ACCESS_ID = "2100240588";
    public static final String XINGE_APP_ACCESS_KEY = "APX5Z466T2YA";
    public static final String XINGE_APP_SECRET_KEY = "9d8c40d75a1a76e959a5c8a17273d4dd";


    /**
     * banner链接拼接的内容
     */
    public static final String BANNER_STRING = "?channel=seo&subchannel=zhjandroid&from=banner";

    /**
     * app_type 1是土拨鼠
     * 2是装好家
     */
    public static final String PIPE_CODE = M_TOBOSU_URL + "app/pub?channel=app&subchannel=android&tbschcode=" + Util.getChannType(MyApplication.getContexts()) + "&app_type=2";
    //****************************************************** creat by lin
    public static final String DESCRIPTOR = "com.umeng.share";
    public static final String ANDROID_SHARE = "&channel=app&subchannel=android&tbschcode=" + Util.getChannType(MyApplication.getContexts()) + "&tbsfrom=share";
    public static final String CALCULATER_SHARE_URL = M_TOBOSU_URL+"app/share_h5?";
    /**
     * 装好家首页请求的接口
     */
    public static final String HOME_FRAGMENT_URL = ZHJ + "zapp/index/index";//首页相关信息的接口
    public static final String USER_PAGE_MESSAGE = ZHJ + "zapp/myself/index";//我的界面信息接口
    public static final String GET_MY_COLLECT = ZHJ + "zapp/myself/my_collect";//获取我的收藏
    //****************************************************** creat by lin


    /**
     * 猜你喜欢url
     */
    public static final String GUESS_YOULIKE_URL = ZHJ + "zapp/DecorateBook/get_key_word";

    /**
     * 搜索url
     */
    public static final String SEARCH_URL = ZHJ + "zapp/DecorateBook/get_article_by_title";

    /**
     * 文章详情
     */
    public static final String ARTICLE_DETAIL_URL = ZHJ + "zapp/DecorateBook/article_detail";
    /**
     * 用户关注作者
     */
    public static final String FOLLOW_URL = ZHJ + "zapp/DecorateBook/follow";
    /**
     * 用户收藏文章
     */
    public static final String COLLECT_URL = ZHJ + "zapp/DecorateBook/collect";
    /**
     * 一般[标题接口]
     */
    public static final String COMMON_ARTICLE_LIST_TOP_TITLE_URL = ZHJ + "zapp/DecorateBook/get_catalog";
    /**
     * 用户定制[标题接口]
     */
    public static final String PRIVATE_USER_TOP_TITLE_URL = ZHJ + "zapp/DecorateBook/my_channel";
    /**
     * 用户修改title标题
     */
    public static final String PRIVATE_USER_MODIFY_TITLE_URL = ZHJ + "zapp/DecorateBook/modify_my_channel";
    /**
     * 装修列表
     */
    public static final String DECORATE_CHILD_FRAGMENT_URL = ZHJ + "zapp/DecorateBook/get_article";
    /**
     * 获取发单总量
     */
    public static final String GET_BILL_COUNT_URL = ZHJ + "zapp/index/get_bill_count";
    /**
     * 发单接口地址
     */
    public static final String PUB_ORDER_URL = ZHJ + "tapi/order/pub_order";
    /**
     * 我的关注
     */
    public static final String MY_ATTENTION_URL = ZHJ + "zapp/myself/my_attention";
    /**
     * 获取我的历史记录
     */
    public static final String MY_HISTORY_URL = ZHJ + "zapp/myself/history_record";
    /**
     * 获取用户的个人信息
     */
    public static final String USER_INFO_URL = ZHJ + "zapp/myself/user_info";
    /**
     * 设置用户的性别信息
     */
    public static final String SET_SEX_URL = ZHJ + "zapp/myself/set_gender";
    /**
     * 设置用户的城市信息
     */
    public static final String SET_CITY_URL = ZHJ + "zapp/myself/set_city";
    /**
     * 设置用户的装修阶段
     */
    public static final String SET_DECORATE_URL = ZHJ + "zapp/myself/set_decorate";
    /**
     * 用户对智能报价打分
     */
    public static final String QUOTE_RECORD_URL = ZHJ + "zapp/index/decorate_quote_record";
    /**
     * 统计文章的浏览情况
     */
    public static final String RECORD_VIEW_COUNT_URL = ZHJ + "zapp/DecorateBook/record_view_count";
    /**
     * 用户点赞
     */
    public static final String USER_LIKE_URL = ZHJ + "zapp/DecorateBook/like";
    /**
     * 用户删除选中的收藏
     */
    public static final String DEL_COLLECT_URL = ZHJ + "zapp/myself/del_collect";
    /**
     * 用户删除选所有的收藏
     */
    public static final String DEL_ALL_COLLECT_URL = ZHJ + "zapp/myself/batch_del_collect";
    /**
     * 获取用户点赞列表
     */
    public static final String GET_MYLIKE_URL = ZHJ + "zapp/myself/my_like";
    /**
     * 删除选中点赞
     */
    public static final String DEL_MYLIKE_URL = ZHJ + "zapp/myself/del_like";
    /**
     * 删除选中点赞
     */
    public static final String DEL_ALL_MYLIKE_URL = ZHJ + "zapp/myself/batch_del_like";
    /**
     * 用户是否关注某个作者
     */
    public static final String IS_FOLLOW_URL = ZHJ + "zapp/index/is_follow";
    /**
     * 进入作者详情
     */
    public static final String AUTHOR_DETAIL_URL = ZHJ + "zapp/index/author_detail";
    /**
     * 获取APP的配置信息
     */
    public static final String GET_APP_CONFIG = ZHJ + "zapp/public/get_config";


    // TODO: 2018/6/26 发单页地址******************************************
    //免费报价
    public static final String FREE_PRICE_PAGE = M_TOBOSU_URL + "free_price_page?channel=app&subchannel=android&chcode=" + AppInfoUtil.getChannType(MyApplication.getContexts()) + APP_TYPE + "&tbschcode=" + AppInfoUtil.getNewChannType(MyApplication.getContexts());
    //免费设计
    public static final String QUOTE = M_TOBOSU_URL + "quote?channel=app&subchannel=android&chcode=" + AppInfoUtil.getChannType(MyApplication.getContexts()) + APP_TYPE + "&tbschcode=" + AppInfoUtil.getNewChannType(MyApplication.getContexts());
    ;


}