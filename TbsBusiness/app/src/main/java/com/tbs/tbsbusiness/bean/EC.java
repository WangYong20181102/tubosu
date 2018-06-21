package com.tbs.tbsbusiness.bean;

/**
 * Created by Mr.Lin on 2018/6/2 17:08.
 */
public class EC {
    public static final class EventCode {
        public static final int EvetA = 0x000001;//事件代码
        public static final int NOTIC_LOGINACTIVITY_FNISHI = 0x000002;//事件代码
        public static final int NOTE_ORDER_FRAGMENT_UPDATE_DATA = 0x000003;//通知订单列表页刷新数据
        public static final int CHANGE_ORDER_DETAIL_ACTIVITY_TO_FEEDBACK_VIEW = 0x000004;//通知页面切换到反馈
        public static final int NOTICE_CO_NET_STORE_CHANGE_FULL_NAME = 0x000005;//通知装修公司网店修改全称
        public static final int NOTICE_CO_NET_STORE_CHANGE_SIMP_NAME = 0x000006;//通知装修公司网店修改简称
        public static final int NOTICE_CO_NET_STORE_CHANGE_STORE_LOGO = 0x000007;//通知装修公司网店修改网店logo
        public static final int NOTICE_CO_NET_STORE_CHANGE_PINPAI_LOGO = 0x000008;//通知装修公司网店修改品牌logo
        public static final int NOTICE_CO_NET_STORE_CHANGE_QIANTAI_LOGO = 0x000009;//通知装修公司网店修改前台图片
        public static final int NOTICE_CO_NET_STORE_CLEAN_QIANTAI_LOGO = 0x000010;//通知装修公司网店删除前台图片
        public static final int NOTICE_CO_NET_STORE_CLEAN_JIAZHUANG_FANWEI = 0x000011;//通知装修公司修改家装范围
        public static final int NOTICE_CO_NET_STORE_CLEAN_GONGZHUANG_FANWEI = 0x000012;//通知装修公司修改公装范围
        public static final int NOTICE_CO_NET_STORE_CLEAN_FENGGE_FANWEI = 0x000013;//通知装修公司修改风格范围
        public static final int NOTICE_CO_NET_STORE_CHANGE_RYZZ_DEL_IMAGE = 0x000014;//通知装修公司网店管理修改荣誉资质的图片删除数据（delimg）
        public static final int NOTICE_CO_NET_STORE_CHANGE_RYZZ_ADD_IMAGE = 0x000015;//通知装修公司网店管理修改荣誉资质的图片新增数据（needuploadImage）
        public static final int NOTICE_CO_NET_STORE_CHANGE_RYZZ_ALLDATE = 0x000016;//通知装修公司网店管理修改荣誉资质对象
        public static final int NOTICE_CO_NET_STORE_CHANGE_YINGYEZHIZHAO_ALLDATE = 0x000017;//通知装修公司网店管理修改营业执照的信息
        public static final int NOTICE_CO_NET_STORE_CHANGE_ADDRESS = 0x000018;//通知装修公司网店管理修改了地址信息
        public static final int NOTICE_CO_NET_STORE_CHANGE_FUWUQUYU_MSG = 0x000019;//通知装修公司网店管理修改了地址信息
        public static final int NOTICE_CO_NET_STORE_CHANGE_SERVICE_MSG = 0x000020;//通知装修公司网店管理修改了服务区域
        public static final int INIT_CITY_DATA_IS_OK = 0x000021;//初始化城市信息ok
        public static final int BAND_PHONE_SUCCESS = 0x000022;//用户绑定手机号码成功
        public static final int USER_LOGIN_OUT = 0x000023;//用户登出
        public static final int INIT_MESSAGE_IF_NULL = 0x000024;//刷新消息数据  当消息数为空的时候

    }
}
