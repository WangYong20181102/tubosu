package com.tbs.tbs_mj.global;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.widget.Toast;

import com.tbs.tbs_mj.utils.AppInfoUtil;

/**
 * 所有的接口链接都在这个页面
 */

public class Constant {


    /**
     * todo 全局正式环境**********************************************************************************
     */
//    //接口地址
    public static final String TOBOSU_URL = "https://www.tobosu.com/";
    //    //数据流上传接口 正式
    public static final String TBS_DATA_STREAM = "https://www.tobosu.com/trace";
    //    //M站 发单跳转 线上环境
    public static final String M_TOBOSU_URL = "https://m.tobosu.com/";


    /**
     * todo 全局测试dev环境*******************************************************************************
     */
    //接口地址
//    public static final String TOBOSU_URL = "http://www.dev.tobosu.com/";
    //dev点击流上传接口
//    public static final String TBS_DATA_STREAM = "http://trace.dev.tobosu.com/";
    //M站test接口
//    public static final String M_TOBOSU_URL = "http://m.test.tobosu.com/szs/";
    //M站dev接口
//    public static final String M_TOBOSU_URL = "http://m.dev.tobosu.com/";


    public static final String APP_TYPE = "&app_type=4";


    /*----------以下是新增加的------------*/


    public static final int POP_RESULTCODE = 0x0000027;
    public static final int HOMEFRAGMENT_REQUESTCODE = 0x0000028;
    public static final int FINISH_MAINACTIVITY = 0x0000030;

    public static final String CALCULATER_SHARE_URL = M_TOBOSU_URL + "app/share_h5?";

    public static final String NET_STATE_ACTION = "com.tobosu.app.net_state";

    public static final String LOGOUT_ACTION = "logout_action";

    public static final String ACTION_HOME_SELECT_CITY = "action_home_select_city";

    public static final String ACTION_GOTO_EDIT_FRAGMENT = "action_goto_edit_fragment";

    /**
     * 广告页面
     */
    public static final String IMG_PATH = Environment.getExternalStorageDirectory() + "/tbs_adpic/";

    //欢迎页
    public static final String SURVIVAL_URL = TOBOSU_URL + "resapp/DataCount/survival_count";


    //下载存储路径
    public static final String DOWNLOAD_IMG_PATH = Environment.getExternalStorageDirectory() + "/tbs_image/";

    public static final String DEFAULT_USER_ID = "263616"; // 263616   333568

    /**
     * 获取数据
     */
    public static final String ACTION_GET_FRAGMENT_DATA = "action_get_fragment_data";
    /**
     * 人力
     */
    public static final String ACTION_MANPOWER_FRAGMENT_DATA = "action_manpower_fragment_data";

    /**
     * 建材
     */
    public static final String ACTION_MATERIAL_FRAGMENT_DATA = "action_material_fragment_data";
    /**
     * 五金
     */
    public static final String ACTION_STEEL_FRAGMENT_DATA = "action_steel_fragment_data";
    /**
     * 家具
     */
    public static final String ACTION_FURNITURE_FRAGMENT_DATA = "action_furniture_fragment_data";
    /**
     * 厨卫
     */
    public static final String ACTION_KITCHEN_FRAGMENT_DATA = "action_kitchen_fragment_data";


    public static final String SUMMIT_BUDGET_URL = TOBOSU_URL + "resapp/RenovateExpense/expense_bookkeep";

    /**
     * activity_url
     */
    public static final String ACTIVITY_URL = TOBOSU_URL + "resapp/Activity/get_activity";


    public static final String ALL_ORDER_URL = TOBOSU_URL + "tapi/order/pub_order";

    /**
     * loading
     */
    public static final String GET_LOADING_AD_URL = TOBOSU_URL + "resapp/Advert/get_advert";


    /**
     * 编辑装修开支
     */
    public static final String EDIT_DECORATE_OUTCOME_URL = TOBOSU_URL + "resapp/RenovateExpense/add_renovate_expense";

    public static final String SHEJISHI_URL = TOBOSU_URL + "resapp/designer/designer_info";
    /**
     * 修改记录
     */
    public static final String MODIFY_RECORD_URL = TOBOSU_URL + "resapp/RenovateExpense/edit_renovate_expense";

    /**
     * 装修开支主页
     */
    public static final String OUTCOME_HOMEPAGE_URL = TOBOSU_URL + "resapp/RenovateExpense/get_renovate_expense";

    /**
     * 装修公司开支记录  分页使用的
     */

    public static final String CITY_JSON = TOBOSU_URL + "resapp/common/province_city_district_info";

    /**
     * 删除装修记录
     */
    public static final String DELETE_DECORATE_RECORD = TOBOSU_URL + "resapp/RenovateExpense/del_renovate_expense";

    /**
     * 装修公司优惠报名订单列表接口
     */
    public static final String DECORATION_COMPANY_PREFERENTIAL_APPLYFOR = TOBOSU_URL + "resapp/company/activitySignupList";

    public static final String clickUrl = TOBOSU_URL + "resapp/companyBanner/click_count";
    /**
     * -
     * 第三方绑定接口
     */
    public static final String BIND_THIRD_PARTY_URL = TOBOSU_URL + "resapp/passport/bindThirdParty";


    public static final String MYFAV_URL = TOBOSU_URL + "resapp/collect/my_collect";

    public static final String FIND_DECORATE_COMPANY_URL = TOBOSU_URL + "/resapp/company/company_list";
    public static final String COMPANY_TOP_LIST = TOBOSU_URL + "resapp/company/company_top_list";
    public static final String GETGONGSIURL = TOBOSU_URL + "resapp/company/company_list";

    public static final String BANNER_CLICK_URL = TOBOSU_URL + "resapp/banner/click_count";

    public static final String FAV_TU_URL = TOBOSU_URL + "resapp/collect/collect_single_list";

    public static final String FAV_TAO_TU_URL = TOBOSU_URL + "resapp/collect/collect_suite_list";

    public static final String GET_TOOL_URL = TOBOSU_URL + "resapp/company/get_home_tools";
    /**
     * 修改用户信息接口
     */
    public static final String USER_CHANGE_INFO_URL = TOBOSU_URL + "resapp/user/chage_user_info";

    /**
     * 业主订单列表接口
     */
    public static final String MY_OWNER_ODER_URL = TOBOSU_URL + "tapp/order/user_order_list";

    public static final String NEWHOME_URL = TOBOSU_URL + "resapp/index/new_index";
    public static final String NEW_HOME_PAGE_URL = TOBOSU_URL + "resapp/index/newVersion_index";

    /**
     * 发单接口地址
     */
    public static final String PUB_ORDERS = TOBOSU_URL + "tapi/order/pub_order";


    public static final String MORE_SHEJI_URL = TOBOSU_URL + "resapp/designer/design_pic_list";
    public static final String MORE_ANLI_URL = TOBOSU_URL + "resapp/designer/anli_list";
    /**
     * app_type 1是土拨鼠
     * 2是装好家
     * 3效果图
     */
//    public static final String PIPE = "https://m.tobosu.com/app/pub?channel=seo&subchannel=android&chcode=" + AppInfoUtil.getChannType(MyApplication.getContext()) + "&app_type=1";
    public static final String PIPE = M_TOBOSU_URL + "app/pub?channel=app&subchannel=android&chcode=" + AppInfoUtil.getChannType(MyApplication.getContext()) + APP_TYPE + "&tbschcode=" + AppInfoUtil.getNewChannType(MyApplication.getContext());

    public static final String WANGJIANLIN = "&tbsfrom=share";

    public static final String POP_URL = M_TOBOSU_URL + "class/?from=app";
    public static final String M_POP_PARAM = "&channel=seo&subchannel=android&chcode=" + AppInfoUtil.getChannType(MyApplication.getContext()) + APP_TYPE;

    public static final String ANDROID_SHARE = "&channel=seo&subchannel=android&chcode=" + AppInfoUtil.getChannType(MyApplication.getContext()) + "&tbsfrom=share";

    // 公司收藏
    public static final String GONGSI_URL = TOBOSU_URL + "resapp/collect/collect_company_list";

    public static final String SHANCHU_URL1 = TOBOSU_URL + "resapp/collect/batch_quit_collect";
    public static final String DUANXIN_URL = TOBOSU_URL + "resapp/smsCode/send_sms_code";

    /**
     * 检测网络状态<br/>
     *
     * @return true 连接网络
     * false 没有网络
     */
    public static boolean checkNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * 无网提示
     *
     * @param mContext
     */
    public static void toastNetOut(Context mContext) {
        Toast.makeText(mContext, "网络断开，请检查网络~", Toast.LENGTH_SHORT).show();
    }

    //新的装修案例 获取案例列表
    public static final String CASE_LIST = TOBOSU_URL + "resapp/case/case_list";
    //新的装修案例详情
    public static final String CASE_DETAIL = TOBOSU_URL + "resapp/case/case_detail";
    //新的装修案例详情
    public static final String TOPIC_DETAIL = TOBOSU_URL + "resapp/topic/topic_detail";
    public static final String ZHUANTI_URL = TOBOSU_URL + "resapp/topic/topic_list";


    // TODO: 2018/5/14 App全栈通一发单地址*****************************************************************************************************
    //免费报价 配置App类型
    public static final String FREE_PRICE_PAGE = M_TOBOSU_URL + "zjfree_price_page?channel=app&subchannel=android&chcode=" + AppInfoUtil.getChannType(MyApplication.getContext()) + APP_TYPE + "&tbschcode=" + AppInfoUtil.getNewChannType(MyApplication.getContext());
    //免费设计
    public static final String QUOTE = M_TOBOSU_URL + "zjquote?channel=app&subchannel=android&chcode=" + AppInfoUtil.getChannType(MyApplication.getContext()) + APP_TYPE + "&tbschcode=" + AppInfoUtil.getNewChannType(MyApplication.getContext());
    //顶部测试的链接
    public static final String BUDGET_TEST = M_TOBOSU_URL + "budget_test?channel=app&subchannel=android&chcode=" + AppInfoUtil.getChannType(MyApplication.getContext()) + APP_TYPE + "&tbschcode=" + AppInfoUtil.getNewChannType(MyApplication.getContext());

    //装修公司推荐
    public static final String REC_COMPANY = M_TOBOSU_URL + "zjrec_company?channel=app&subchannel=android&chcode=" + AppInfoUtil.getChannType(MyApplication.getContext()) + APP_TYPE + "&tbschcode=" + AppInfoUtil.getNewChannType(MyApplication.getContext());
    ;
    //装修大礼包  这个在住家装修中没有用到  免费量房去掉了
    public static final String COMPANY_GIFT = M_TOBOSU_URL + "zjcompany_gift?channel=app&subchannel=android&chcode=" + AppInfoUtil.getChannType(MyApplication.getContext()) + APP_TYPE + "&tbschcode=" + AppInfoUtil.getNewChannType(MyApplication.getContext());
    ;
    //发单地址
    public static final String FREE_DESIGN = M_TOBOSU_URL + "zjfree_design?channel=app&subchannel=android&chcode=" + AppInfoUtil.getChannType(MyApplication.getContext()) + APP_TYPE + "&tbschcode=" + AppInfoUtil.getNewChannType(MyApplication.getContext());
    ;
    //优惠活动
    public static final String PROMOTIONS = M_TOBOSU_URL + "zjpromotions?channel=app&subchannel=android&chcode=" + AppInfoUtil.getChannType(MyApplication.getContext()) + APP_TYPE + "&tbschcode=" + AppInfoUtil.getNewChannType(MyApplication.getContext());
    ;
    //10周年活动   测试连接http://m.dev.tobosu.com/test_app/
    public static final String TEN_YEARS_ACTIVITY = M_TOBOSU_URL + "signing_gifts/";

    //新的逛图库 套图
    public static final String SUITE_LIST = TOBOSU_URL + "resapp/impression/suite_list";
    //新的逛图库
    public static final String SUITE_CLASS = TOBOSU_URL + "resapp/impressionClass/suite_class";
    //收藏或者取消收藏套图或者单图
    public static final String IMAGE_COLLECT = TOBOSU_URL + "resapp/collect/collect";
    //新的逛图库 单图
    public static final String SINGLE_MAP_LIST = TOBOSU_URL + "resapp/impressionSingle/single_map_list";
    //新的逛图库 单图筛选条件
    public static final String SINGLE_CLASS = TOBOSU_URL + "resapp/impressionClass/single_class";
    //收藏或者取消收藏装修公司
    public static final String COMPANY_COLLECT = TOBOSU_URL + "resapp/collect/collect";
    //装修公司主页
    public static final String COMPANY_DETAIL = TOBOSU_URL + "resapp/company/company_detail";
    //优惠列表
    public static final String PROMOTION_LIST = TOBOSU_URL + "resapp/company/promotion_list";
    //装修公司设计方案列表
    public static final String COMPANY_SUITE_LIST = TOBOSU_URL + "resapp/company/suite_list";
    //装修公司案例列表
    public static final String COMPANY_CASE_LIST = TOBOSU_URL + "resapp/company/case_list";
    //装修公司的设计师列表
    public static final String DESIGNER_LIST = TOBOSU_URL + "resapp/designer/designer_list";
    //获取学装修的分类
    public static final String Z_ARTICLE_GET_TYPE = TOBOSU_URL + "resapp/ZArticle/get_type";
    //获取学装修列表数据
    public static final String Z_ARTICLE_LIST = TOBOSU_URL + "resapp/ZArticle/article_list";
    //装修详情点击量
    public static final String Z_ARTICLE_CLICK_COUNT = TOBOSU_URL + "resapp/ZArticle/click_count";
    //短信验证码登录
    public static final String SMS_CODE_LOGIN = TOBOSU_URL + "resapp/user/sms_code_login";
    //微信登录
    public static final String WECHAT_LOGIN = TOBOSU_URL + "resapp/user/wechat_login";
    //账号密码登录
    public static final String ACCOUNT_LOGIN = TOBOSU_URL + "resapp/user/login";
    //校验手机号和验证码的正确性
    public static final String VERIFY_SMS_CODE = TOBOSU_URL + "resapp/smsCode/verify_sms_code";
    //忘记密码
    public static final String FORGET_PASSWORD = TOBOSU_URL + "resapp/user/forget_password";
    //图片上传的地址
    public static final String UPLOAD_DYNAMIC_IMAGE = TOBOSU_URL + "cloud/upload/app_upload";
    // 用户反馈
    public static final String ADD_FEEDBACK = TOBOSU_URL + "resapp/feedback/add_feedback";
    //用户信息
    public static final String USER_INFO = TOBOSU_URL + "resapp/user/user_info";
    //修改用户信息
    public static final String MODIFY_USER_INFO = TOBOSU_URL + "resapp/user/modify_user_info";
    //绑定手机号码
    public static final String BIND_CELLPHONE = TOBOSU_URL + "resapp/user/bind_cellphone";
    //绑定微信
    public static final String BIND_WE_CHAT = TOBOSU_URL + "resapp/user/bind_wechat";
    //绑定微信
    public static final String CHECK_ORDER_PWD = TOBOSU_URL + "resapp/company/check_order_pwd";
    //App配置信息
    public static final String GET_CONFIG = TOBOSU_URL + "resapp/public/get_config";
    //检查用户是否存在
    public static final String IS_EXIST_USER = TOBOSU_URL + "resapp/user/is_exist_user";
    //检查当前App是否需要更新
    public static final String CHECK_APP_IS_UPDATA = TOBOSU_URL + "resapp/public/is_update_app";
    //检查当前登录的用户是否修改了密码
    public static final String CHECK_USER_PASSWORD_IS_CHANGE = TOBOSU_URL + "resapp/user/check_password";
    //获取订单列表
    public static final String GET_ORDER_LIST = TOBOSU_URL + "resapp/companyOrder/order_list";
    //改变订单的状态
    public static final String CHANGE_ORDER_STATE = TOBOSU_URL + "resapp/companyOrder/change_state";
    //获取订单详情
    public static final String GET_ORDER_DETAIL = TOBOSU_URL + "resapp/companyOrder/order_detail";
    //获取订单消息通知
    public static final String GET_ORDER_NOTICE = TOBOSU_URL + "resapp/smsPushRecord/company_sms_push";
    //已读消息标记
    public static final String READ_SMS_PUSH = TOBOSU_URL + "resapp/smsPushRecord/read_sms_push";
    //推送上线
    public static final String FLUSH_SMS_PUSH = TOBOSU_URL + "resapp/public/flush_sms_push";
    //推送下线
    public static final String SMS_PUSH_OFFLINE = TOBOSU_URL + "resapp/public/sms_push_offline";
    //反馈消息列表
    public static final String FEEDBACK_LIST = TOBOSU_URL + "resapp/CompanyFeedback/feedback_list";
    //添加反馈
    public static final String ADD_FEEDBACK_MSG = TOBOSU_URL + "resapp/CompanyFeedback/add_feedback";
    //网店管理
    public static final String COMPANY_MY_STORE = TOBOSU_URL + "resapp/company/my_store";
    //网店管理修改网店管理
    public static final String COMPANY_MODIFY_STORE = TOBOSU_URL + "resapp/company/modify_store";
    //获取闪屏的url
    public static final String GET_SHAN_PIN_URL = TOBOSU_URL + "resapp/Advert/get_advert_new";
}
