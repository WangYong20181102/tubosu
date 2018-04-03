package com.tbs.tobosutype.receiver;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.tbs.tobosutype.utils.Util;
import com.tbs.tobosutype.web.PushAppNotStartWebActivity;
import com.tbs.tobosutype.web.PushAppStartWebActivity;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.List;

/**
 * Created by Mr.Lin on 2018/1/6 11:47.
 */

public class MyXiaomiPushReceiver extends PushMessageReceiver {
    private String TAG = "MyXiaomiPushReceiver";
    private String mRegId;
    private long mResultCode = -1;
    private String mReason;
    private String mCommand;
    private String mMessage;
    private String mTopic;
    private String mAlias;
    private String mUserAccount;
    private String mStartTime;
    private String mEndTime;

    //透传消息
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        System.out.println("透传消息到达了");
        System.out.println("透传消息是" + message.toString());
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        //打印消息方便测试

        System.out.println("用户点击了通知消息====当前App的运行状态===" + Util.getAppSatus(context, "com.tbs.tobosutype"));
        System.out.println("通知消息是" + message.toString());
        System.out.println("点击后,会进入应用");
        //程序未启动的情况下
        if (3 == Util.getAppSatus(context, "com.tbs.tobosutype")) {
            /**
             * 程序未启动的情况下:
             * 1.打开查看推送的相关的信息
             * 2.点返回将启动App
             */
            if (!TextUtils.isEmpty(message.getExtra().get("url"))) {
                Intent intent = new Intent(context, PushAppNotStartWebActivity.class);
                intent.putExtra("mLoadingUrl", message.getExtra().get("url"));
                Log.e(TAG, "收到消息=========消息中包含的tag=====" + message.getExtra().get("tag"));
                if (message.getExtra().containsKey("tag")) {
                    //包含推送发单的key  传值通知下一个页面  展示发单的key
                    if (message.getExtra().get("tag").equals("1")) {
                        //显示发标按钮
                        intent.putExtra("mShowing", "1");
                    } else {
                        intent.putExtra("mShowing", "0");
                    }
                } else {
                    intent.putExtra("mShowing", "0");
                }

                //是否拼接点击流信息
                if (message.getExtra().containsKey("enable_statistics")) {
                    if (message.getExtra().get("enable_statistics").equals("1")) {
                        //带入点击流
                        intent.putExtra("mEnableStatistics", "1");

                    } else {
                        intent.putExtra("mEnableStatistics", "0");
                    }
                } else {
                    intent.putExtra("mEnableStatistics", "0");
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } else {
            //程序已经启动
            if (!TextUtils.isEmpty(message.getExtra().get("url"))) {
                Intent intent = new Intent(context, PushAppStartWebActivity.class);
                intent.putExtra("mLoadingUrl", message.getExtra().get("url"));
                Log.e(TAG, "收到消息=========消息中包含的tag=====" + message.getExtra().get("tag"));
                if (message.getExtra().containsKey("tag")) {
                    //包含推送发单的key  传值通知下一个页面  展示发单的key
                    if (message.getExtra().get("tag").equals("1")) {
                        //显示发标按钮
                        intent.putExtra("mShowing", "1");
                    } else {
                        intent.putExtra("mShowing", "0");
                    }
                } else {
                    intent.putExtra("mShowing", "0");
                }
                //是否拼接点击流信息
                if (message.getExtra().containsKey("enable_statistics")) {
                    if (message.getExtra().get("enable_statistics").equals("1")) {
                        //带入点击流
                        intent.putExtra("mEnableStatistics", "1");
                    } else {
                        intent.putExtra("mEnableStatistics", "0");
                    }
                } else {
                    intent.putExtra("mEnableStatistics", "0");
                }


                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }

    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        System.out.println("通知消息到达了");
        System.out.println("通知消息是" + message.toString());
        System.out.println("通知消息到达了,拿到的url==================" + message.getExtra().get("url"));
        Log.e(TAG, "收到小米发送的消息======拿到的URL=====" + message.getExtra().get("url"));
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                Log.e(TAG, "小米推送注册测Regid=================" + mRegId);
            }
        }
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mStartTime = cmdArg1;
                mEndTime = cmdArg2;
            }
        }
    }
}
