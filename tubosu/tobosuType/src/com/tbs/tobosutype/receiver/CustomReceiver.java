package com.tbs.tobosutype.receiver;


import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tbs.tobosutype.activity.MyCompanyActivity;
import com.tbs.tobosutype.activity.SelectedImageDetailActivity;
import com.tbs.tobosutype.activity.SettingActivity;
import com.tbs.tobosutype.activity.WebViewActivity;
import com.tbs.tobosutype.activity.WelcomeActivity;
import com.tbs.tobosutype.global.MyApplication;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

public class CustomReceiver extends XGPushBaseReceiver {
	
	private static final String TAG = CustomReceiver.class.getSimpleName();
	
	/***
	 * type  <br/>
	 * 		1  跳转 加载h5页面 <br/>
	 * 		2  跳转 加载全部订单的明细页面 ,  2016-07-27改为跳转我的[装修公司]页面 <br/>
	 * 		3  跳转 效果精选 详情 页面 <br/>
	 * 		4  跳转 设置页面 
	 */
	private String type;
	
	
	private String id;
	
	
	private String url;
	

	@Override
	public void onDeleteTagResult(Context context, int errorCode, String tagName) {
		
	}

	@Override
	public void onNotifactionClickedResult(Context context, XGPushClickedResult message) {
		
		if (context == null || message == null) {
			return;
		}
	    
	    
		if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) { 
			// 通知在通知栏被点击啦....
			// APP自己处理点击的相关动作
			String customContent = message.getCustomContent();
			if (customContent != "" || customContent.length() != 0) {
				try {
					JSONObject js = new JSONObject(customContent);
					type = js.getString("type");
					id = js.getString("id");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			if ("1".equals(type)) {
				try {
					JSONObject js = new JSONObject(customContent);
					url = js.getString("url");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Intent intent = new Intent(context, WebViewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("link", url);
				intent.putExtras(bundle);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			} else if ("2".equals(type)) {
				//TODO 订单推送
				if(getAppRunningState(context)){
					// 在前台 后台
					MyApplication.ISPUSHLOOKORDER = true;
//					Intent intent = new Intent(context, AllOrderDetailActivity.class);
					Intent intent = new Intent(context, MyCompanyActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("id", id);
					intent.putExtras(bundle);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}else{
					// 还没启动
					MyApplication.ISPUSHLOOKORDER = true;
//					Intent intent = new Intent(context, AllOrderDetailActivity.class);
					Intent intent = new Intent(context, WelcomeActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("id", id);
					intent.putExtras(bundle);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}
				
			} else if ("3".equals(type)) {
				//TODO 精选详情
				Intent intent = new Intent(context, SelectedImageDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("id", id);
				intent.putExtra(id, bundle);
				intent.putExtras(bundle);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			} else if ("9".equals(type)) {
				try {
					//TODO 设置
					JSONObject jsonObject = new JSONObject(customContent);
					String url = jsonObject.getString("url");
					Intent intent = new Intent(context, SettingActivity.class);
					intent.putExtra("url", url);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
			
		}else if(message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
			// 通知被清除啦....
			// APP自己处理通知被清除后的相关动作
			System.out.println("删除通知栏");
		}else if(message.getActionType() == XGPushClickedResult.NOTIFACTION_OPEN_CANCEL_TYPE){
			System.out.println("清除通知栏");
		}
		
		
	}
	
	
	
	@Override
	public void onNotifactionShowedResult(Context context, XGPushShowedResult notifiShowedRlt) {
		
	}

	@Override
	public void onRegisterResult(Context context, int errorCode, XGPushRegisterResult registerMessage) {
		
	}

	@Override
	public void onSetTagResult(Context context, int errorCode, String tagName) {
		
	}

	@Override
	public void onTextMessage(Context context, XGPushTextMessage message) {
	}

	@Override
	public void onUnregisterResult(Context context, int errorCode) {
		
	}
	
	/**
	 *  是否已经启动了
	 * @param context
	 * @return
	 */
	private boolean getAppRunningState(Context context){
		String state = "";
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
	    List<RunningTaskInfo> list = am.getRunningTasks(25); // 最大数
	    for (RunningTaskInfo info : list) {
	        if (info.topActivity.getPackageName().equals(context.getPackageName()) || info.baseActivity.getPackageName().equals(context.getPackageName())) {
	        	state = "running";
	            break;
	        }
	    }
	    
	    if(!"".equals(state)&&"running".equals(state)){
			return true;
		}else{
			return false;
		}
	}
	

}
