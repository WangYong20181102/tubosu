package com.tbs.tobosutype.bean;

/**
 * Created by Mr.Lin on 2017/10/27 10:32.
 * 事件总线的事件码
 */

public class EC {
    public static final class EventCode{
        public static final int EvetA = 0x000001;//事件代码
        public static final int CLICK_IMAGE_IN_LOOK_PHOTO = 0x000002;//点击看图传递事件
        public static final int LOOG_CLICK_IMAGE_IN_LOOK_PHOTO = 0x000003;//看图长按事件

        public static final int DELETE_TAOTU_CODE = 0x100000;//删除收藏
    }

}
