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
        public static final int NOTIF_DESIGN_CASE_ACTIVITY_MODE_IS_COLLECT = 0x000013;//通知设计方案列表页修改数据模型（已收藏）
        public static final int NOTIF_DESIGN_CASE_ACTIVITY_MODE_IS_NOT_COLLECT = 0x000014;//通知设计方案列表页修改数据模型（未收藏）
        public static final int NOTIF_DECCOMACTIVITY_MODE_IS_COLLECT = 0x000015;//通知装修公司主页修改数据模型（已收藏）
        public static final int NOTIF_DECCOMACTIVITY_MODE_IS_NOT_COLLECT = 0x000016;//通知装修公司主页修改数据模型（未收藏）


        public static final int DELETE_TAOTU_CODE = 0x100000;//删除收藏
        public static final int DELETE_TAOTU_LIST_CODE = 0x100001;//删除收藏套图
        public static final int DELETE_DANTU_LIST_CODE = 0x100002;//删除收藏单图
        public static final int SELECT_CITY_CODE = 0x100003;//选择城市
        public static final int DELETE_COMPANY_CODE = 0x100004;//选择城市
        public static final int COLLECT_COMPANY_CODE = 0x100005;//选择城市
        public static final int CHOOSE_CITY_CODE = 0x100006;//找装修公司选择城市
        public static final int SHAIXUAN_CITY_CODE = 0x100007;//选择城市
        public static final int QUEDING_SHAIXUAN_CITY_CODE = 0x100008;//确定筛选城市
        public static final int HOMEACTIVITY_CITY_CODE = 0x100009;//确定首页城市
        public static final int CHOOSE_PROVINCE_CODE = 0x100010;//确定首页城市
        public static final int CHOOSE_PROVINCE_CODE1 = 0x100011;//选择城市
    }

}
