package com.tbs.tobosutype.utils;

import com.tbs.tobosutype.bean.Event;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Mr.Lin on 2017/10/27 10:24.
 */

public class EventBusUtil {
    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    //发送普通事件
    public static void sendEvent(Event event) {
        EventBus.getDefault().post(event);
    }

    //发送粘性事件
    public static void sendStickyEvent(Event event) {
        EventBus.getDefault().postSticky(event);
    }
}
