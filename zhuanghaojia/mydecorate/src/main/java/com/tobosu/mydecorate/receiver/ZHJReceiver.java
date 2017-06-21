package com.tobosu.mydecorate.receiver;

import android.content.Context;
import android.content.Intent;

import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;
import com.tobosu.mydecorate.activity.MessageCenterActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dec on 2016/11/7.
 */

public class ZHJReceiver extends XGPushBaseReceiver {
    private static final String TAG = ZHJReceiver.class.getSimpleName();
    private String aid = ""; // 文章id
    private String type = ""; // 类型
    private String title = ""; // 文章标题
    private String type_id = ""; // 类型id
    private String title_zdy = ""; // 自定义标题
    private String title_fu = ""; // 副标题




    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {

    }

    @Override
    public void onUnregisterResult(Context context, int i) {

    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {

    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {

    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {

    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {

        if (context == null || xgPushClickedResult == null) {
            return;
        }
        if (xgPushClickedResult.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
            // 通知在通知栏被点击啦....
            // APP自己处理点击的相关动作
            String customContent = xgPushClickedResult.getCustomContent();
            if (customContent != "" || customContent.length() != 0) {
                System.out.println("-----推送来了在这里--"+customContent);

                Intent intent = new Intent(context, MessageCenterActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("id", id);
//                intent.putExtra(id, bundle);
//                intent.putExtras(bundle);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                try {
                    JSONObject js = new JSONObject(customContent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

//            if ("1".equals(type)) {
//                try {
//                    JSONObject js = new JSONObject(customContent);
//                    url = js.getString("url");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Intent intent = new Intent(context, WebViewActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("link", url);
//                intent.putExtras(bundle);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            } else if ("2".equals(type)) {
//                //TODO 订单推送
//                if(getAppRunningState(context)){
//                    // 在前台 后台
//                    MyApplication.ISPUSHLOOKORDER = true;
////					Intent intent = new Intent(context, AllOrderDetailActivity.class);
//                    Intent intent = new Intent(context, MyCompanyActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("id", id);
//                    intent.putExtras(bundle);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
//                }else{
//                    // 还没启动
//                    MyApplication.ISPUSHLOOKORDER = true;
////					Intent intent = new Intent(context, AllOrderDetailActivity.class);
//                    Intent intent = new Intent(context, WelcomeActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("id", id);
//                    intent.putExtras(bundle);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
//                }
//
//            } else if ("3".equals(type)) {
//                //TODO 精选详情
//                Intent intent = new Intent(context, SelectedImageDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("id", id);
//                intent.putExtra(id, bundle);
//                intent.putExtras(bundle);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            } else if ("9".equals(type)) {
//                try {
//                    //TODO 设置
//                    JSONObject jsonObject = new JSONObject(customContent);
//                    String url = jsonObject.getString("url");
//                    Intent intent = new Intent(context, SettingActivity.class);
//                    intent.putExtra("url", url);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }

        }else if(xgPushClickedResult.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
            // 通知被清除啦....
            // APP自己处理通知被清除后的相关动作
            System.out.println("删除通知栏");
        }else if(xgPushClickedResult.getActionType() == XGPushClickedResult.NOTIFACTION_OPEN_CANCEL_TYPE){
            System.out.println("清除通知栏");
        }
    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {

    }
}
