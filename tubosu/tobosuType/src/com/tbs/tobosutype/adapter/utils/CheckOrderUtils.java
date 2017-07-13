package com.tbs.tobosutype.adapter.utils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tbs.tobosutype.activity.AllOrderDetailActivity;
import com.tbs.tobosutype.activity.AllOrderListActivity;
import com.tbs.tobosutype.activity.MyCompanyActivity;
import com.tbs.tobosutype.customview.CheckOrderPwdPopupWindow;
import com.tbs.tobosutype.customview.InputWarnDialog;
import com.tbs.tobosutype.customview.SettingOrderPwdPopupWindow;
import com.tbs.tobosutype.global.Constant;

public class CheckOrderUtils {
	
	/***未设置过订单密码*/
	private static final int NEVER_SET_PSD= 20301;
	
	/**pc端重新设置过密码*/
	private static final int PC_RESET_PSD = 20302;
	
//	/**没有密码*/
//	private static final int NO_PSD = 0;

	/**订单密码已设置*/
	private static final int HAS_SET_PSD = 0;
	
	private String token;
	
	/***订单是否设置密码*/
	private String hasOrderPwdUrl = Constant.TOBOSU_URL + "tapp/order/hasOrderPwd";
	
	/***订单填写密码*/
	private String setOrderPwdUrl = Constant.TOBOSU_URL + "tapp/passport/setOrderPwd";
	
	/***订单验证密码*/
	private String requestOrderPwdUrl = Constant.TOBOSU_URL + "tapp/order/checkOrderPwd";
	
	/***有密码-请求参数对象*/
	private RequestParams hasOrderParams;
	
	/***需要密码-请求参数对象*/
	private RequestParams requestOrderParams;
	
	/**设置密码-请求参数对象*/
	private RequestParams setOrderParams;
	
	private Context context;
	private View view;
	
	/**是否验证了订单密码*/
	private boolean isCheckOrderPwd;
	private Activity activity;
	
	/**类型*/
	private String kind;
	
	public CheckOrderUtils(View view, Context context, String token, String kind) {
		this.view = view;
		this.context = context;
		this.kind = kind;
		activity = (Activity) context;
		this.token = token;
		isCheckOrderPwd = context.getSharedPreferences("CheckOrderPwd", 0).getBoolean("CheckOrderPwd", false);
		requestHasOrderPwd();
	}

	/***
	 * 订单是否设置密码接口请求
	 */
	private void requestHasOrderPwd() {
		hasOrderParams = new RequestParams();
		hasOrderParams.put("token", token);
		
		HttpServer.getInstance().requestPOST(hasOrderPwdUrl, hasOrderParams, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] body) {
						String result = new String(body);
						try {
							JSONObject jsonObject = new JSONObject(result);
							
							//订单密码已设置     -->>[debug得知]
							if (isCheckOrderPwd && jsonObject.getInt("error_code") == HAS_SET_PSD) {
//								Util.setToast(context, "-  标记 1  -");
								if (activity instanceof MyCompanyActivity) {
//									Util.setToast(context, "-  标记 2  -");
									// 跳转到全部订单
									goAllOrder(kind);
								}
								if (activity instanceof AllOrderDetailActivity) {
//									Util.setToast(context, "-  标记 3  -");
									// 获取订单详情
									((AllOrderDetailActivity) activity).requestOrderDetailPost();
								}
								return;
							}
							

							// 没设密码，则需要设置密码  
							if (jsonObject.getInt("error_code") == NEVER_SET_PSD) {
								InputWarnDialog.Builder builder = new InputWarnDialog.Builder(context);
								builder.setTitle("提示")/*.setMessage("您还没设置订单密码，请先设置")*/;
								builder.setMessage("你确定退出吗？").setPositiveButton("设置订单密码", new DialogInterface.OnClickListener() {
									
													@Override
													public void onClick(DialogInterface dialog, int id) {
														dialog.dismiss();
//														Util.setToast(context, "-  标记 4  -");
														//去设置密码
														operSetOrderPwd();
													}
												})
										.setNegativeButton("取消", new DialogInterface.OnClickListener() {
											
													@Override
													public void onClick(DialogInterface dialog, int id) {
														dialog.cancel();
													}
												});
								
								builder.create().show();

								return;
							}


							// 20302
							if (jsonObject.getInt("error_code") == PC_RESET_PSD || !isCheckOrderPwd) {
								// 输入密码查看
//								Util.setToast(context, "-  标记 5  -");
								operCheckOrderPwd();
								return;
							}


						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

					}
				});
	}

	/****
	 * 需要输入订单密码才能进入全部订单列表
	 */
	private void operCheckOrderPwd() {
		final CheckOrderPwdPopupWindow checkOrderPwdPopupWindow = new CheckOrderPwdPopupWindow(context);
		checkOrderPwdPopupWindow.showAtLocation(view.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		checkOrderPwdPopupWindow.bt_subit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String pwd = checkOrderPwdPopupWindow.et_check_order_pwd.getText().toString().trim();
						if (TextUtils.isEmpty(pwd)) {
							Toast.makeText(context, "密码不能为空！", Toast.LENGTH_SHORT).show();
							return;
						}
						// 验证订单秘密
						requestCheckOrderPwd(pwd, checkOrderPwdPopupWindow);
					}
				});
	}

	/***
	 * 
	 * 订单验证接口请求
	 * @param pwd
	 * @param poWindow
	 */
	private void requestCheckOrderPwd(String pwd, final PopupWindow poWindow) {
		requestOrderParams = new RequestParams();
		requestOrderParams.add("token", token);
		requestOrderParams.add("orderpwd", MD5Util.md5(pwd));
		HttpServer.getInstance().requestPOST(requestOrderPwdUrl, requestOrderParams, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] body) {
						String result = new String(body);
						try {
							JSONObject jsonObject = new JSONObject(result);
							if (jsonObject.getInt("error_code") == HAS_SET_PSD) {
								if (activity instanceof MyCompanyActivity) {
									// 去全部订单列表页面
									goAllOrder(kind);
								}
								// 去订单详情页面
								if (activity instanceof AllOrderDetailActivity) {
									((AllOrderDetailActivity) activity).requestOrderDetailPost();
								}
								context.getSharedPreferences("CheckOrderPwd", 0).edit().putBoolean("CheckOrderPwd", true).commit();
								poWindow.dismiss();
							} else {
								Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

					}
				});
	}

	/****
	 * 根据不同的类型跳转到全部订单页面
	 * 
	 * @param kind
	 */
	public void goAllOrder(String kind) {
		Intent allOrderIntent = new Intent(activity, AllOrderListActivity.class);
		Bundle allOrderBundle = new Bundle();
		allOrderBundle.putString("kind", kind);
		allOrderIntent.putExtras(allOrderBundle);
		activity.startActivity(allOrderIntent);
	}

	/**
	 * 给订单设置密码
	 */
	private void operSetOrderPwd() {
		final SettingOrderPwdPopupWindow popuWindow = new SettingOrderPwdPopupWindow(context);
		popuWindow.showAtLocation(view.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		popuWindow.bt_subit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String pwd1 = popuWindow.et_setting_order_pwd1.getText().toString().trim();
				String pwd2 = popuWindow.et_setting_order_pwd2.getText().toString().trim();

				if("".equals(pwd1) || "".equals(pwd2)){
					Util.setToast(context, "密码不能为空");
					return;
				}else{
					if(pwd1.equals(pwd2)){
						if(pwd1.length()>16 || pwd1.length()<6){
							Util.setToast(context, "密码长度在6-16位之间");
							return;
						}
						requestSetOrderPwd(popuWindow, pwd1, pwd2);
					}else{
						Util.setToast(context, "两次输入密码不一样，请重新输入");
						return;
					}
				}

			}
		});
	}

	
	/***
	 * 提交订单填写密码接口请求
	 * @param popuWindow
	 * @param pwd1
	 * @param pwd2
	 */
	protected void requestSetOrderPwd(final SettingOrderPwdPopupWindow popuWindow, String pwd1, String pwd2){
		setOrderParams = new RequestParams();
		setOrderParams.put("token", token);
		setOrderParams.put("orderpwd", MD5Util.md5(pwd1));
		setOrderParams.put("orderpwd1", MD5Util.md5(pwd2));
		HttpServer.getInstance().requestPOST(setOrderPwdUrl, setOrderParams, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] body) {
						String result = new String(body);
						try {
							JSONObject jsonObject = new JSONObject(result);
							int code = jsonObject.getInt("error_code");
							switch (code) {
							case HAS_SET_PSD:  // 已经设置密码  HAS_SET_PSD == 0
							case PC_RESET_PSD: // pc端重置过密码，需重新验证   PC_RESET_PSD == 20302
								context.getSharedPreferences("CheckOrderPwd", 0).edit().putBoolean("CheckOrderPwd", false).commit();
								
								// 输入密码来查看
								operCheckOrderPwd();
								Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
								popuWindow.dismiss();
								break;

							default:
								Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
								break;
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

					}
				});
	}
}
