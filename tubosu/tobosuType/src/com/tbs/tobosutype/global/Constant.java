package com.tbs.tobosutype.global;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.tbs.tobosutype.utils.AppInfoUtil;


public class Constant {
    public static final String DESCRIPTOR = "com.umeng.login";
    public static final String DESCRIPTOR_SHARE = "com.umeng.share";

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

    public static final int UNLOGIN_TO_LOGIN_RESULTCODE = 0x000018;
    public static final int POP_RESULTCODE = 0x0000027;
    public static final int HOMEFRAGMENT_REQUESTCODE = 0x0000028;
    public static final int FINISH_MAINACTIVITY = 0x0000030;
    public static final int FINISH_SAVE_EDIT_OUTCOME = 0x0000031;

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


    /**
     * 测试环境
     */
    public static final String TOBOSU_URL = "http://www.dev.tobosu.com/";

    /**
     * 正式环境
     */
//    public static final String TOBOSU_URL = "http://www.tobosu.com/";


    /**
     * 编辑装修开支
     */
    public static final String EDIT_DECORATE_OUTCOME_URL = TOBOSU_URL + "mapp/RenovateExpense/add_renovate_expense";

    /**
     * 装修开支详情
     */
    public static final String OUTCOME_DETAIL_URL = TOBOSU_URL + "mapp/RenovateExpense/renovate_expense_detail";

    /**
     * 装修开支主页
     */
    public static final String OUTCOME_HOMEPAGE_URL = TOBOSU_URL + "mapp/RenovateExpense/get_renovate_expense";

    /**
     * 装修公司开支记录
     */
    public static final String OUTCOME_RECORD_URL = TOBOSU_URL + "mapp/RenovateExpense/get_decorate_record";

    /**
     * 删除装修记录
     */
    public static final String DELETE_DECORATE_RECORD = TOBOSU_URL + "mapp/RenovateExpense/del_renovate_expense";

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


    /**
     * app_type 1是土拨鼠
     * 2是装好家
     * 3效果图
     */
    public static final String PIPE = "http://m.tobosu.com/app/pub?channel=seo&subchannel=android&chcode=" + AppInfoUtil.getChannType(MyApplication.getContext()) + "&app_type=1";

    public static final String WANGJIANLIN = "&tbsfrom=share";

    public static final String POP_URL = "http://m.tobosu.com/class/?from=app";
    public static final String M_POP_PARAM = "&channel=seo&subchannel=android&chcode=" + AppInfoUtil.getChannType(MyApplication.getContext());

    public static final String ANDROID_SHARE = "&channel=seo&subchannel=android&chcode=" + AppInfoUtil.getChannType(MyApplication.getContext()) + "&tbsfrom=share";


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
