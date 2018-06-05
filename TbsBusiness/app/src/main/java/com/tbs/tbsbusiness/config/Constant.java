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
    public static final String VERIFY_SMS_CODE = TOBOSU_URL + "mapp/smsCode/verify_sms_code";
    //忘记密码
    public static final String FORGET_PASSWORD = TOBOSU_URL + "mapp/user/forget_password";
    //账号登录
    public static final String ACCOUNT_LOGIN = TOBOSU_URL + "comapp/login/login";
    //微信登录
    public static final String WECHAT_LOGIN = TOBOSU_URL + "comapp/login/wechat_login";
    //推送上线
    public static final String FLUSH_SMS_PUSH = TOBOSU_URL + "comapp/login/wechat_login";
    //获取订单列表
    public static final String GET_ORDER_LIST = TOBOSU_URL + "comapp/companyOrder/index";
    //改变订单状态
    public static final String CHANGE_ORDER_STATE = TOBOSU_URL + "comapp/companyOrder/change_state";
    //检测订单密码
    public static final String CHECK_ORDER_PWD = TOBOSU_URL + "mapp/company/check_order_pwd";
}
