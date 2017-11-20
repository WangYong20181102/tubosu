package com.tbs.tobosutype.bean;

/**
 * Created by Mr.Lin on 2017/10/27 10:32.
 * 事件总线的事件码
 */

public class EC {
    public static final class EventCode {
        public static final int EvetA = 0x000001;//事件代码
        public static final int CLICK_IMAGE_IN_LOOK_PHOTO = 0x000002;//点击看图传递事件
        public static final int LOOG_CLICK_IMAGE_IN_LOOK_PHOTO = 0x000003;//看图长按事件
        public static final int CLOSE_POP_WINDOW_IN_NEW_IMAGE_ACTIVITY = 0x000004;//看图长按事件
        public static final int CLICK_SIMAGE_IN_LOOK_PHOTO = 0x000005;//点击查看单图传递事件
        public static final int LOOG_CLICK_SIMAGE_IN_LOOK_PHOTO = 0x000006;//长按单图传递事件
        public static final int NOTIF_SHOUCANG_DATA_CHANGE_IS_COLLECT = 0x000007;//单图查看详情中改变了收藏的状态（已收藏）通知列表的数据更新
        public static final int NOTIF_SHOUCANG_DATA_CHANGE_IS_NOT_COLLECT = 0x000008;//单图查看详情中改变了收藏的状态（未收藏）通知列表的数据更新
        public static final int NOTIF_D_SHOUCANG_DATA_CHANGE_IS_COLLECT = 0x000009;//套图查看详情中改变了收藏的状态（已收藏）通知列表的数据更新
        public static final int NOTIF_D_SHOUCANG_DATA_CHANGE_IS_NOT_COLLECT = 0x000010;//单图查看详情中改变了收藏的状态（未收藏）通知列表的数据更新
        public static final int CLICK_DIMAGE_IN_LOOK_PHOTO = 0x000011;//点击查看套图传递事件
        public static final int LOOG_CLICK_DIMAGE_IN_LOOK_PHOTO = 0x000012;//长按套图传递事件

        public static final int DELETE_TAOTU_CODE = 0x100000;//删除收藏
    }

}
