package com.tbs.tbsbusiness.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tbs.tbsbusiness.activity.MainActivity;
import com.tbs.tbsbusiness.activity.OrderNoteActivity;
import com.tbs.tbsbusiness.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Mr.Lin on 2018/6/2 10:11.
 */
public class MyJpushReceiver extends BroadcastReceiver {
    private String TAG = "MyJpushReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        //网端代码   测试用
        Bundle bundle = intent.getExtras();
        Log.d("TAG", "onReceive - " + intent.getAction());
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            Log.e(TAG, "注册极光============");
            //逻辑代码
            Log.e(TAG, "获取的极光推送注册id=================" + JPushInterface.getRegistrationID(context));
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "用户收到了通知========creat by lin");
            // TODO: 2017/12/25 收到消息处理相关事件 在这里可以做些统计，或者做些其他工作
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Log.e(TAG, "title=======" + title + "=====content=====" + content + "====extras===" + extras);
            Log.e(TAG, "获取的极光推送注册id=================" + JPushInterface.getRegistrationID(context));
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            System.out.println("用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
            Log.e(TAG, "用户点开了通知========creat by lin");
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Log.e(TAG, "title=======" + title + "=====content=====" + content + "====extras===" + extras);
            try {
                JSONObject jsonObject = new JSONObject(extras);
                String mNoticeType = jsonObject.optString("notice_type");
                Log.e(TAG, "获取消息的属性值==============" + mNoticeType);
                if (3 == Util.getAppSatus(context, "com.tbs.tbsbusiness")) {
                    //App未启动的情况下 打开推送相关的信息  点击返回时启动App
                    if (mNoticeType != null && mNoticeType.equals("1")) {
                        //进入订单列表
                        Intent intentToNotice = new Intent(context, OrderNoteActivity.class);
                        intentToNotice.putExtra("back_key", "1");
                        intentToNotice.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intentToNotice);
                    } else if (mNoticeType != null && mNoticeType.equals("5")) {
                        //进入订单列表
                        Intent intentToNotice = new Intent(context, OrderNoteActivity.class);
                        intentToNotice.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(intentToNotice);
                    }
                } else {
                    //App已经启动
                    if (mNoticeType != null && mNoticeType.equals("1")) {
                        //进入订单列表
                        Intent intentToNotice = new Intent(context, OrderNoteActivity.class);
                        intentToNotice.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(intentToNotice);
                    } else if (mNoticeType != null && mNoticeType.equals("5")) {
                        //进入订单列表
                        Intent intentToNotice = new Intent(context, OrderNoteActivity.class);
                        intentToNotice.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intentToNotice);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("TAG", "Unhandled intent - " + intent.getAction());
        }
    }
}
