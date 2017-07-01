package com.tbs.tobosupicture.bean;

/**
 * Created by Mr.Lin on 2017/6/30 08:29.
 * Eventbus 处理事务总线
 */

public final class EC {
    public static final class EventCode {
        public static final int EvetA = 0x000001;//事件代码
        public static final int SHOW_TEMPLATE_FRAGMENT = 0x000002;//显示样板图
        public static final int SHOW_DECORATIONCASE_FRAGMENT = 0x000003;//显示样板图
        public static final int SHOW_IMAGETOFRIEND_FRAGMENT = 0x000004;//显示以图会友
        public static final int SHOW_MINE_FRAGMENT = 0x000005;//显示我的

        public static final int WELCOMETOMAIN = 0x000006;//显示我的
    }
}
