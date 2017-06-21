package com.tbs.tobosutype.global;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.tbs.tobosutype.utils.AppInfoUtil;


public class AllConstants {
    public static final String DESCRIPTOR = "com.umeng.login";

    public static final String UMENG_KEY = "5657b48a67e58ed2b30040bc";
    private static final String TIPS = "请移步官方网站 ";

    private static final String END_TIPS = ", 查看相关说明.";

    public static final String TENCENT_OPEN_URL = TIPS + "http://wiki.connect.qq.com/android_sdk使用说明" + END_TIPS;

    public static final String PERMISSION_URL = TIPS + "http://wiki.connect.qq.com/openapi权限申请" + END_TIPS;

    public static final String SOCIAL_LINK = "http://www.umeng.com/social";
    public static final String SOCIAL_TITLE = "友盟社会化组件帮助应用快速整合分享功能";
    public static final String SOCIAL_IMAGE = "http://www.umeng.com/images/pic/banner_module_social.png";

    public static final String SOCIAL_CONTENT = "友盟社会化组件（SDK）让移动应用快速整合社交分享功能，我们简化了社交平台的接入，为开发者提供坚实的基础服务：（一）支持各大主流社交平台，"
            + "（二）支持图片、文字、gif动图、音频、视频；@好友，关注官方微博等功能"
            + "（三）提供详尽的后台用户社交行为分析。http://www.umeng.com/social";

	/*----------以下是新增加的------------*/

    public static final int POP_RESULTCODE = 0x0000027;
    public static final int HOMEFRAGMENT_REQUESTCODE = 0x0000028;
    public static final int FINISH_MAINACTIVITY = 0x0000030;

    /***标记推送*/
    public static String app_push_flag = "0";

    public static final String CALCULATER_SHARE_URL = "http://m.tobosu.com/app/share_h5?";

    /**
     * 手机网络状态广播标记
     */
    public static final String NET_STATE_ACTION = "com.tobosu.app.net_state";

    public static final String LOGOUT_ACTION = "logout_action";

    public static final String LOGIN_ACTION = "login_action";

    public static final String ACTION_HOME_SELECT_CITY = "action_home_select_city";

    /**线上测试环境*/
//	public static final String TOBOSU_WEIXIN_URL = "http://www.wxdev.tobosu.com/";

    /**测试环境*/
//	public static final String TOBOSU_URL = "http://www.dev.tobosu.com/";

    /**
     * 正式环境
     */
    public static final String TOBOSU_URL = "http://www.tobosu.com/";


//	 public static final String TOBOSU_URL = "www.api.tobosu.com/";


    /**
     * 装修公司优惠报名订单列表接口
     */
    public static final String DECORATION_COMPANY_PREFERENTIAL_APPLYFOR = TOBOSU_URL + "tapp/company/activitySignupList";


    /**
     * 第三方绑定接口
     */
    public static String BIND_THIRD_PARTY_URL = TOBOSU_URL + "tapp/passport/bindThirdParty";

    /**
     * 修改用户信息接口
     * <br/> field  <br/>
     * 1 昵称 <br/>
     * 2 性别 <br/>
     * 3 城市 <br/>
     * 4 小区名id <br/>
     * 5 头像
     */
    public static String USER_CHANGE_INFO_URL = TOBOSU_URL + "tapp/user/chage_user_info";

    /**
     * 业主订单列表接口
     */
    public static String MY_OWNER_ODER_URL = TOBOSU_URL + "tapp/order/user_order_list";

    /**
     * 发单接口地址
     */
    public static final String PUB_ORDERS = TOBOSU_URL + "tapi/order/pub_order";
    //                              http://www.tobosu.com/tapi/order/pub_order


    //    public static final String PIPE = "http://m.tobosu.com/app/pub?channel=seo&subchannel=android&chcode=" + AllConstants.CHANNEL_TYPE;
    public static final String PIPE = "http://m.tobosu.com/app/pub?channel=seo&subchannel=android&chcode=" + AppInfoUtil.getChannType(MyApplication.getContext());

    public static final String WANGJIANLIN = "&tbsfrom=share";

    public static final String POP_URL = "http://m.tobosu.com/class/?from=app";
    public static final String M_POP_PARAM = "&channel=seo&subchannel=android&chcode=" + AppInfoUtil.getChannType(MyApplication.getContext());

    public static final String ANDROID_SHARE = "&channel=seo&subchannel=android&chcode=" + AppInfoUtil.getChannType(MyApplication.getContext()) + "&tbsfrom=share";


    //	public static final String CHANNEL_TYPE = "aisi";/**渠道-- > 爱思助手*/
//	public static final String CHANNEL_TYPE = "anbei";/**渠道-- > 安贝市场*/
//	public static final String CHANNEL_TYPE = "anfen";	/**渠道-- > 安粉*/
//	public static final String CHANNEL_TYPE = "anfeng";/**渠道-- > 安丰网*/
//	public static final String CHANNEL_TYPE = "anzhi";/**渠道-- > 安智市场*/
//	public static final String CHANNEL_TYPE = "anzhuo";/**渠道-- > 安卓市场*/
//	public static final String CHANNEL_TYPE = "anzhuofamily";/**渠道-- > 安卓之家*/
//	public static final String CHANNEL_TYPE = "anzhuoyuan";/**渠道-- > 安卓园市场*/
//	public static final String CHANNEL_TYPE = "appbaidu";/**渠道-- > 百度*/
//	public static final String CHANNEL_TYPE = "apphuawei";/**渠道-- > 华为应用商店*/
//	public static final String CHANNEL_TYPE = "applenovo";/**渠道-- > 联想应用商店*/
//	public static final String CHANNEL_TYPE = "appmeizu";/**渠道-- > 魅族应用商店*/
//	public static final String CHANNEL_TYPE = "appoppo";	/**渠道-- > oppo应用商店*/
//	public static final String CHANNEL_TYPE = "appqihu";/**渠道-- > 360手机助手*/
//	public static final String CHANNEL_TYPE = "appttt";/**渠道-- > 锤子应用商店*/
//	public static final String CHANNEL_TYPE = "appvivo";/**渠道-- > vivo应用商店*/
//	public static final String CHANNEL_TYPE = "appwdj";/**渠道-- > 豌豆荚应用商店*/
//	public static final String CHANNEL_TYPE = "appxiaomi";/**渠道-- > 小米应用商店*/
//	public static final String CHANNEL_TYPE = "appyyb";/**渠道-- > 腾讯应用宝*/
//	public static final String CHANNEL_TYPE = "bikeer";/**渠道-- > 比克尔*/
//	public static final String CHANNEL_TYPE = "changhong";/**渠道-- > 长虹*/
//	public static final String CHANNEL_TYPE = "feifan";/**渠道-- > 非凡软件*/
//	public static final String CHANNEL_TYPE = "fengfeng";/**渠道-- > 锋锋网*/
//	public static final String CHANNEL_TYPE = "gaoqu";/**渠道-- > 搞趣*/
//	public static final String CHANNEL_TYPE = "itools";/**渠道-- > itools*/
//	public static final String CHANNEL_TYPE = "jifeng";/**渠道-- > 机锋市场*/
//	public static final String CHANNEL_TYPE = "jinli";	/**渠道-- > 易用汇（金立手机）*/
//	public static final String CHANNEL_TYPE = "kuaiyong";/**渠道-- > 快用*/
//	public static final String CHANNEL_TYPE = "kuchuan";/**渠道-- > 酷传*/
//	public static final String CHANNEL_TYPE = "kupai";/**渠道-- > 酷派*/
//	public static final String CHANNEL_TYPE = "lenovowow";/**渠道-- > 中国联通沃商店*/
//	public static final String CHANNEL_TYPE = "leshi";/**渠道-- > 乐视应用商店*/
//	public static final String CHANNEL_TYPE = "liqu";/**渠道-- > 历趣网*/
//	public static final String CHANNEL_TYPE = "maopao";/**渠道-- > 冒泡堂*/
//	public static final String CHANNEL_TYPE = "mogu";/**渠道-- > 蘑菇市场*/
//	public static final String CHANNEL_TYPE = "mumayi";/**渠道-- > 木蚂蚁*/
//	public static final String CHANNEL_TYPE = "nduo";/**渠道-- > n多网*/
//	public static final String CHANNEL_TYPE = "nineoneMarket";/**渠道-- > 91手机助手*/
//	public static final String CHANNEL_TYPE = "nineonezhushou"; //  91zhushou 91手机助手
//	public static final String CHANNEL_TYPE = "pingguoyuan";/**渠道-- > 苹果园*/
//	public static final String CHANNEL_TYPE = "ppzhushou";/**渠道-- > pp助手*/
//	public static final String CHANNEL_TYPE = "shizimao";	/**渠道-- > 十宇猫*/
//	public static final String CHANNEL_TYPE = "shuajidashi";/**渠道-- > 刷机大师/应用酷*/
//	public static final String CHANNEL_TYPE = "sougou";/**渠道-- > 搜狗助手市场*/
//	public static final String CHANNEL_TYPE = "souhu";/**渠道-- > 搜狐*/
//	public static final String CHANNEL_TYPE = "sumsung";/**渠道-- > 三星应用商店*/
//	public static final String CHANNEL_TYPE = "suning";	/**渠道-- > 苏宁应用*/
//	public static final String CHANNEL_TYPE = "taobao";/**渠道-- > 淘宝手机市场*/
//	public static final String CHANNEL_TYPE = "threeMarket";/**渠道-- > 3G市场*/
//	public static final String CHANNEL_TYPE = "tianyi";/**渠道-- > 中国电信天翼*/
//	public static final String CHANNEL_TYPE = "tompda";/**渠道-- > TOMPDA*/
//	public static final String CHANNEL_TYPE = "tongbu";	/**渠道-- > 同步推*/
//	public static final String CHANNEL_TYPE = "ucmarket";	/**渠道-- > UC应用市场*/
//	public static final String CHANNEL_TYPE = "wangxun";/**渠道-- > 网讯安卓*/
//	public static final String CHANNEL_TYPE = "wangyi";/**渠道-- > 网易应用中心*/
//	public static final String CHANNEL_TYPE = "weibo";/**渠道-- > 微博应用中心*/
//	public static final String CHANNEL_TYPE = "wujiwang";/**渠道-- > 无极网*/
//	public static final String CHANNEL_TYPE = "xy";/**渠道-- > xy苹果助手*/
//	public static final String CHANNEL_TYPE = "yidongmm";/**渠道-- > 中国移动*/
//	public static final String CHANNEL_TYPE = "yingyonghui";/**渠道-- > 应用汇*/
//	public static final String CHANNEL_TYPE = "youyi";/**渠道-- > 优益市场*/
//	public static final String CHANNEL_TYPE = "zhonguanchun";/**渠道-- > 中关村*/
//	public static final String CHANNEL_TYPE = "zhongxin";/**渠道-- > 中兴应用*/
//	public static final String CHANNEL_TYPE = "zhuole";/**渠道-- > 卓乐*/
//	public static final String CHANNEL_TYPE = "zuimei";/**渠道-- > 最美应用*/
//    public static final String CHANNEL_TYPE = "tobosu1";

    /**
     * 渠道-- > 备用1
     */
//	public static final String CHANNEL_TYPE = "tobosu2";
//	public static final String CHANNEL_TYPE = "tobosu3";


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


    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断WIFI网络是否可用
     *
     * @param context
     * @return
     */
    public boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    /**
     * 判断MOBILE网络是否可用
     *
     * @param context
     * @return
     */
    public boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    /**
     * @param context
     * @return -1：没有网络  1：WIFI网络2：wap网络3：net网络
     */
    public static int GetNetype(Context context) {
        int netType = -1;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                netType = 3;
            } else {
                netType = 2;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1;
        }
        return netType;
    }


}
