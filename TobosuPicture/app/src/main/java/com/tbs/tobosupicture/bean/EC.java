package com.tbs.tobosupicture.bean;

/**
 * Created by Mr.Lin on 2017/6/30 08:29.
 * Eventbus 处理事务总线
 * 事件总线的code使用：
 * wtb同学 0x10000N
 * lcw同学 0x00000N
 */

public final class EC {
    public static final class EventCode {
        public static final int EvetA = 0x000001;//事件代码
        public static final int SHOW_TEMPLATE_FRAGMENT = 0x000002;//显示样板图
        public static final int SHOW_DECORATIONCASE_FRAGMENT = 0x000003;//显示案例
        public static final int SHOW_IMAGETOFRIEND_FRAGMENT = 0x000004;//显示以图会友
        public static final int SHOW_MINE_FRAGMENT = 0x000005;//显示我的
        public static final int WELCOMETOMAIN = 0x000006;//测试欢迎页
        public static final int LOGIN_INITDATA = 0x000007;//登录成功以后刷新相关页面的数据
        public static final int LOGIN_OUT = 0x000008;//用户登出App
        public static final int SHOW_ABOUT_ME = 0x000009;//显示有关于我
        public static final int MY_JOIN_NUM = 0x000010;//通过这个可以拿到  我参加的数量
        public static final int MY_ORGIN_NUM = 0x000011;//通过这个可以拿到 我发起的数量
        public static final int REFRESH_MY_ORGIN_NUM = 0x000012;//刷新我发起的页面获取fragment中的提示
        public static final int REFRESH_MY_JOIN_NUM = 0x000013;//刷新我参与的页面获取fragment中的提示
        public static final int GOTO_SEND_DYNAMIC = 0x000014;//去发动态
        public static final int FNISHI_LOGINACTIVITY = 0x000015;//销毁登录页面
        public static final int CHECK_ABOUTME_MYORG_HAS_MSG = 0x000016;//点击了关于我按钮刷新数据
        public static final int PERSON_INFO_ACTIVITY_CHANGE_CITY = 0x000017;//个人信息 点击城市选择
        public static final int PERSON_INFO_ACTIVITY_CHANGE_MSG = 0x000018;//个人信息 修改了数据  重新刷新数据
        public static final int GOTO_ZUIXIN = 0x000019;//当我的参与页面没有任何数据的时候去最新页面
        public static final int INIT_MY_FRIEND_DYNAMIC = 0x000020;//初始化我的图友动态
        public static final int SEND_TITLE_MAP = 0x000021;//传递标题map
        public static final int MY_JOIN_ICON = 0x000022;//拿到我参加的头像
        public static final int MY_ORGIN_ICON = 0x000024;//拿到我发起的头像
        public static final int MY_ORGIN_MSG = 0x000025;//拿到我发起消息
        public static final int MY_JOIN_MSG = 0x000026;//拿到我参与的消息
        public static final int REFRESH_MY_ORGIN = 0x000027;//刷新我的发起数据（用户第一次发动态的时候调用）
        public static final int SHOW_MINE_RED_DOT = 0x000028;//显示我的界面中的搜索装修案例的红点提示
        public static final int HINT_MINE_RED_DOT = 0x000029;//隐藏我的界面中的搜索装修案例的红点提示


        public static final int UPDATE_OWNER_SEARCH_CASE_STATUS = 0x100001; // 改变状态
        public static final int CHOOSE_CITY_CODE_FOR_PRICE = 0x100002;//报价获取城市名称
        public static final int CHOOSE_CITY_CODE_FOR_FREE_PRICE = 0x100003;//报价获取城市名称
        public static final int CHOOSE_CITY_TO_GET_DATA_FROM_NET_HOUSE = 0x100004;
        public static final int CHOOSE_CITY_TO_GET_DATA_FROM_NET_FACTORY = 0x100006;
        public static final int UPDATE_OWNER_SEARCH_CASE = 0x100007; // 重新请求
        public static final int CHOOSE_CITY_FOR_BOTH_FRAGMENT = 0x100008;
        public static final int USER_LOGIN_TYPE = 0x100009; // 用户登录的身份
    }
}
