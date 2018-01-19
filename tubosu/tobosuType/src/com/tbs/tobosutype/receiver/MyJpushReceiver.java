package com.tbs.tobosutype.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tbs.tobosutype.activity.NewWebViewActivity;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Mr.Lin on 2017/12/25 11:29.
 */

public class MyJpushReceiver extends BroadcastReceiver {
    private String TAG = "MyJpushReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
//        //含有JSON字符串的数据
////        if (intent.getAction().equals(JPushInterface.ACTION_MESSAGE_RECEIVED)) {
////            Bundle bundle = intent.getExtras();
//////            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
////            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
////            String msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
////            Log.e(TAG, "收到服务去推送的消息================" + title + "================" + msg);
////        }
//
//
////        Bundle mBundle = intent.getExtras();
////        Log.e(TAG, "收到服务去推送的消息================");
////        switch (intent.getAction()) {
////            case JPushInterface.EXTRA_EXTRA:
////                //含有JSON字符
////                String extras = mBundle.getString(JPushInterface.EXTRA_EXTRA);
////                Log.e(TAG, "收到含有json字符串============" + extras);
////                break;
////            case JPushInterface.EXTRA_TITLE:
////                //包含推送标题
////                String title = mBundle.getString(JPushInterface.EXTRA_TITLE);
////                Log.e(TAG, "收到含有title字符串============" + title);
////                break;
////            case JPushInterface.EXTRA_MESSAGE:
////                //消息内容
////                String msg = mBundle.getString(JPushInterface.EXTRA_MESSAGE);
////                Log.e(TAG, "收到含有title字符串============" + msg);
////                break;
////        }
//
//
//        //网端代码   测试用
//        Bundle bundle = intent.getExtras();
//        Log.d("TAG", "onReceive - " + intent.getAction());
//        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
//            Log.e(TAG, "收到消息============");
////逻辑代码
//        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//            System.out.println("收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//// 自定义消息不会展示在通知栏，完全要开发者写代码去处理
//        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//            Log.e(TAG, "用户收到了通知========creat by lin");
//            // TODO: 2017/12/25 收到消息处理相关事件 在这里可以做些统计，或者做些其他工作
//            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
//            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
//            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            Log.e(TAG, "title=======" + title + "=====content=====" + content + "====extras===" + extras);
//        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//            System.out.println("用户点击打开了通知");
//// 在这里可以自己写代码去定义用户点击后的行为
//            Log.e(TAG, "用户点开了通知========creat by lin");
//            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
//            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
//            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            Log.e(TAG, "title=======" + title + "=====content=====" + content + "====extras===" + extras);
//            try {
//                JSONObject jsonObject = new JSONObject(extras);
//                String url = jsonObject.getString("url");
//                Intent intoWebViewActivity = new Intent(context, NewWebViewActivity.class);
//                intoWebViewActivity.putExtra("mLoadingUrl", url);
//                intoWebViewActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intoWebViewActivity);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else {
//            Log.e("TAG", "Unhandled intent - " + intent.getAction());
//        }
    }
}
