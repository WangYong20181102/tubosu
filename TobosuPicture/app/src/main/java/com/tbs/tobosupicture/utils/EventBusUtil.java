package com.tbs.tobosupicture.utils;

import com.tbs.tobosupicture.bean.Event;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Mr.Lin on 2017/6/29 17:41.
 * eventbus 再度封装
 */

public class EventBusUtil {
    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    public static void sendEvent(Event event) {
        EventBus.getDefault().post(event);
    }

    public static void sendStickyEvent(Event event) {
        EventBus.getDefault().postSticky(event);
    }
}
