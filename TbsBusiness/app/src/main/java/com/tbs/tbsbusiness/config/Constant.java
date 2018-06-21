package com.tbs.tbsbusiness.config;

/**
 * Created by Mr.Lin on 2018/6/2 15:24.
 */
public class Constant {
    // TODO: 2018/6/2 正式环境
//    public static final String TOBOSU_URL = "https://www.tobosu.com/";

    // TODO: 2018/6/2 dev环境
    public static final String TOBOSU_URL = "http://www.dev.tobosu.com/";


    //获取短信验证码
    public static final String DUANXIN_URL = TOBOSU_URL + "comapp/SmsCode/send_sms_code";
    //短信验证码登录
    public static final String SMS_CODE_LOGIN = TOBOSU_URL + "comapp/login/sms_code_login";
    //检查用户是否存在
    public static final String IS_EXIST_USER = TOBOSU_URL + "mapp/user/is_exist_user";
    //校验手机号和验证码的正确性
    public static final String VERIFY_SMS_CODE = TOBOSU_URL + "comapp/SmsCode/check_code";
    //忘记密码 修改密码
    public static final String FORGET_PASSWORD = TOBOSU_URL + "comapp/login/modify_password";
    //账号登录
    public static final String ACCOUNT_LOGIN = TOBOSU_URL + "comapp/login/login";
    //微信登录
    public static final String WECHAT_LOGIN = TOBOSU_URL + "comapp/login/wechat_login";
    //推送上线
    public static final String FLUSH_SMS_PUSH = TOBOSU_URL + "comapp/public/flush_sms_push";
    //获取订单列表
    public static final String GET_ORDER_LIST = TOBOSU_URL + "comapp/companyOrder/index";
    //改变订单状态
    public static final String CHANGE_ORDER_STATE = TOBOSU_URL + "comapp/companyOrder/change_state";
    //检测订单密码
    public static final String CHECK_ORDER_PWD = TOBOSU_URL + "comapp/company/check_order_pwd";
    //获取订单详情
    public static final String GET_ORDER_DETAIL = TOBOSU_URL + "comapp/companyOrder/detail";
    //反馈消息
    public static final String FEEDBACK_LIST = TOBOSU_URL + "comapp/CompanyFeedback/index";
    //发送反馈消息
    public static final String ADD_FEEDBACK_MSG = TOBOSU_URL + "comapp/CompanyFeedback/add_feedback";
    //获取配置信息
    public static final String GET_CONFIG = TOBOSU_URL + "comapp/public/get_config";
    //检查当前App是否需要更新
    public static final String CHECK_APP_IS_UPDATA = TOBOSU_URL + "comapp/public/is_update_app";
    //获取用户的信息
    public static final String GET_USER_INFO = TOBOSU_URL + "comapp/company/info";
    //用户使用App的反馈
    public static final String ADD_FEEDBACK = TOBOSU_URL + "comapp/feedback/add_feedback";
    //图片上传
    public static final String UPLOAD_DYNAMIC_IMAGE = TOBOSU_URL + "/cloud/upload/app_upload";
    //网店管理
    public static final String COMPANY_MY_STORE = TOBOSU_URL + "comapp/company/my_store";
    //网店管理修改网店管理
    public static final String COMPANY_MODIFY_STORE = TOBOSU_URL + "comapp/company/modify_store";
    //获取省市区
    public static final String CITY_JSON = TOBOSU_URL + "mapp/common/province_city_district_info";
    //用户推送下线
    public static final String SMS_PUSH_OFFLINE = TOBOSU_URL + "comapp/public/sms_push_offline";
    //绑定手机号码
    public static final String BIND_CELLPHONE = TOBOSU_URL + "comapp/login/bind_cellphone";
    //绑定微信
    public static final String BIND_WE_CHAT = TOBOSU_URL + "comapp/login/bind_wechat";
    //获取消息列表信息
    public static final String GET_ORDER_NOTICE = TOBOSU_URL + "comapp/smsPushRecord/index";
    //消息已读
    public static final String READ_SMS_PUSH = TOBOSU_URL + "comapp/smsPushRecord/read_sms_push";

}
