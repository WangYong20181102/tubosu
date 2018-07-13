package com.tbs.tbs_mj.receiver;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.utils.Util;
import com.tbs.tbs_mj.web.AcWebActivity;
import com.tbs.tbs_mj.web.PushAppNotStartWebActivity;
import com.tbs.tbs_mj.web.PushAppStartWebActivity;
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
//            if (!TextUtils.isEmpty(message.getExtra().get("url"))) {
//                Intent intent = new Intent(context, PushAppNotStartWebActivity.class);
//                intent.putExtra("mLoadingUrl", message.getExtra().get("url"));
//                Log.e(TAG, "收到消息=========消息中包含的tag=====" + message.getExtra().get("tag"));
//                if (message.getExtra().containsKey("tag")) {
//                    //包含推送发单的key  传值通知下一个页面  展示发单的key
//                    if (message.getExtra().get("tag").equals("1")) {
//                        //显示发标按钮
//                        intent.putExtra("mShowing", "1");
//                    } else {
//                        intent.putExtra("mShowing", "0");
//                    }
//                } else {
//                    intent.putExtra("mShowing", "0");
//                }
//
//                //是否拼接点击流信息
//                if (message.getExtra().containsKey("enable_statistics")) {
//                    if (message.getExtra().get("enable_statistics").equals("1")) {
//                        //带入点击流
//                        intent.putExtra("mEnableStatistics", "1");
//
//                    } else {
//                        intent.putExtra("mEnableStatistics", "0");
//                    }
//                } else {
//                    intent.putExtra("mEnableStatistics", "0");
//                }
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            }
            if (message.getExtra().containsKey("notice_type")) {
                String notice_type = message.getExtra().get("notice_type");
                if (notice_type.equals("1")) {
                    //推送订单页面  这个是由后台直接推送的
                } else if (notice_type.equals("2")) {
                    //推送文章页
                    if (message.getExtra().containsKey("url")) {
                        // TODO: 2018/7/6 10周年活动
                        Intent intentToNotStartWebActivity;
                        if (message.getExtra().get("url").contains(Constant.TEN_YEARS_ACTIVITY)) {
                            intentToNotStartWebActivity = new Intent(context, AcWebActivity.class);
                            intentToNotStartWebActivity.putExtra("mWhereFrom", "MyXiaomiPushReceiver");
                        } else {
                            intentToNotStartWebActivity = new Intent(context, PushAppNotStartWebActivity.class);
                        }
                        //有url链接才能启动
                        intentToNotStartWebActivity.putExtra("mLoadingUrl", message.getExtra().get("url"));
                        //是否包含tag
                        if (message.getExtra().containsKey("tag")) {
                            //含tag  获取tag的值传给目标
                            intentToNotStartWebActivity.putExtra("mShowing", message.getExtra().get("tag"));
                        } else {
                            //不含tag 默认不含
                            intentToNotStartWebActivity.putExtra("mShowing", "0");
                        }
                        //是否拼接点击流字段
                        if (message.getExtra().containsKey("enable_statistics")) {
                            //含有点击流的字段
                            intentToNotStartWebActivity.putExtra("mEnableStatistics", message.getExtra().get("enable_statistics"));
                        } else {
                            //不含点击流字段 默认拼接
                            intentToNotStartWebActivity.putExtra("mEnableStatistics", "1");
                        }
                        intentToNotStartWebActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intentToNotStartWebActivity);
                    }
                }
            }
        } else {
            //程序已经启动
//            if (!TextUtils.isEmpty(message.getExtra().get("url"))) {
//                Intent intent = new Intent(context, PushAppStartWebActivity.class);
//                intent.putExtra("mLoadingUrl", message.getExtra().get("url"));
//                Log.e(TAG, "收到消息=========消息中包含的tag=====" + message.getExtra().get("tag"));
//                if (message.getExtra().containsKey("tag")) {
//                    //包含推送发单的key  传值通知下一个页面  展示发单的key
//                    if (message.getExtra().get("tag").equals("1")) {
//                        //显示发标按钮
//                        intent.putExtra("mShowing", "1");
//                    } else {
//                        intent.putExtra("mShowing", "0");
//                    }
//                } else {
//                    intent.putExtra("mShowing", "0");
//                }
//                //是否拼接点击流信息
//                if (message.getExtra().containsKey("enable_statistics")) {
//                    if (message.getExtra().get("enable_statistics").equals("1")) {
//                        //带入点击流
//                        intent.putExtra("mEnableStatistics", "1");
//                    } else {
//                        intent.putExtra("mEnableStatistics", "0");
//                    }
//                } else {
//                    intent.putExtra("mEnableStatistics", "0");
//                }
//
//
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            }

            if (message.getExtra().containsKey("notice_type")) {
                String notice_type = message.getExtra().get("notice_type");
                if (notice_type.equals("1")) {
                    //推送订单页面  这个是由后台直接推送的
                } else if (notice_type.equals("2")) {
                    //推送文章页
                    if (message.getExtra().containsKey("url")) {
                        Intent intentToStartWebActivity;
                        // TODO: 2018/7/6 10周年活动
                        if (message.getExtra().get("url").contains(Constant.TEN_YEARS_ACTIVITY)) {
                            intentToStartWebActivity = new Intent(context, AcWebActivity.class);
                        } else {
                            intentToStartWebActivity = new Intent(context, PushAppStartWebActivity.class);
                        }
                        //有url链接才能启动
                        intentToStartWebActivity.putExtra("mLoadingUrl", message.getExtra().get("url"));
                        //是否包含tag
                        if (message.getExtra().containsKey("tag")) {
                            //含tag  获取tag的值传给目标
                            intentToStartWebActivity.putExtra("mShowing", message.getExtra().get("tag"));
                        } else {
                            //不含tag 默认不含
                            intentToStartWebActivity.putExtra("mShowing", "0");
                        }
                        //是否拼接点击流字段
                        if (message.getExtra().containsKey("enable_statistics")) {
                            //含有点击流的字段
                            intentToStartWebActivity.putExtra("mEnableStatistics", message.getExtra().get("enable_statistics"));
                        } else {
                            //不含点击流字段 默认拼接
                            intentToStartWebActivity.putExtra("mEnableStatistics", "1");
                        }
                        intentToStartWebActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intentToStartWebActivity);
                    }
                }
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
