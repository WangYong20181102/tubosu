package com.tbs.tobosupicture.bean;

/**
 * Created by Mr.Lin on 2017/6/30 08:29.
 * Eventbus 处理事务总线
 * 事件总线的code使用：
 * 吴天保同学 0x10000N
 * 林承文同学 0x00000N
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
        public static final int MY_JOIN_NUM = 0x000010;//我参加的数量
        public static final int MY_ORGIN_NUM = 0x000011;//我发起的数量
        public static final int REFRESH_MY_ORGIN_NUM = 0x000012;//刷新我发起的页面获取fragment中的提示
        public static final int REFRESH_MY_JOIN_NUM = 0x000013;//刷新我参与的页面获取fragment中的提示
        public static final int GOTO_SEND_DYNAMIC = 0x000014;//去发动态
        public static final int FNISHI_LOGINACTIVITY = 0x000015;//销毁登录页面
        public static final int CHECK_ABOUTME_MYORG_HAS_MSG = 0x000016;//点击了关于我按钮刷新数据
    }
}
