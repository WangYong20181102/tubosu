package com.tbs.tobosutype.activity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.CustomDialog;
import com.tbs.tobosutype.customview.RoundImageView;
import com.tbs.tobosutype.customview.SelectCityDialog;
import com.tbs.tobosutype.customview.SelectCityDialog.Builder;
import com.tbs.tobosutype.customview.SelectSexPopupWindow;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.MyApplication;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.socialize.bean.SHARE_MEDIA;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 业主个人资料信息页
 * @author dec
 *
 */
public class MyCompanyChangeInfoActivity extends Activity implements OnClickListener {
	private String nickname;
	private String icon;
	private String token;
	private String gender;
	private String cityname;
	private String wechatCheck;
	private String cellphone;
	private String province;
	private String icommunity;
	private TextView tv_nickname;
	private TextView tv_gender;
	private TextView tv_place;
	private TextView tv_icommunity;
	private TextView tv_cellphone;
	private TextView tv_weixin;
	private RelativeLayout rl_nickname;
	private RelativeLayout rl_gender;
	private RelativeLayout rl_place;
	private RelativeLayout rl_icommunity;
	private RelativeLayout rl_cellphone;
	private RelativeLayout rl_weixin;
	private RoundImageView iv_icon;
	private ImageView iv_back;
	private RelativeLayout rl_exit;
	
	/**修改用户信息*/
	private String userChageInfoUrl = Constant.TOBOSU_URL + "tapp/user/chage_user_info";
	
	/**第三方绑定*/
	private String bindThirdPartyUrl = Constant.TOBOSU_URL + "tapp/passport/bindThirdParty";
	
	private HashMap<String, String> chageInfoParams;
	private HashMap<String, String> bindThirdPartyParams;
	private Intent intent;
	private Context mContext;
	private String weiXinUserName;
	private String weiXinImageUrl;
	private String weiXinUserId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
		initEvent();
	}

	private void initView() {
		AppInfoUtil.setTranslucentStatus(this);
//		setContentView(R.layout.activity_personaldata);
		tv_nickname = (TextView) findViewById(R.id.tv_nickname);
//		tv_gender = (TextView) findViewById(R.id.tv_gender);
		tv_place = (TextView) findViewById(R.id.tv_place);
		tv_icommunity = (TextView) findViewById(R.id.tv_icommunity);
		tv_cellphone = (TextView) findViewById(R.id.tv_cellphone);
		tv_weixin = (TextView) findViewById(R.id.tv_weixin);
		rl_nickname = (RelativeLayout) findViewById(R.id.rl_nickname);
		rl_gender = (RelativeLayout) findViewById(R.id.rl_gender);
		rl_place = (RelativeLayout) findViewById(R.id.rl_place);
		rl_icommunity = (RelativeLayout) findViewById(R.id.rl_icommunity);
		rl_cellphone = (RelativeLayout) findViewById(R.id.rl_cellphone);
		rl_weixin = (RelativeLayout) findViewById(R.id.rl_weixin);
		iv_icon = (RoundImageView) findViewById(R.id.iv_icon);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		rl_exit = (RelativeLayout) findViewById(R.id.rl_exit);
	}

	private void initData() {
		Bundle bundle = getIntent().getBundleExtra("data");
		nickname = bundle.getString("nickname");
		tv_nickname.setText(nickname);
		icon = bundle.getString("icon");
		MyApplication.imageLoader.displayImage(icon, iv_icon,
				MyApplication.options);
		token = bundle.getString("token");
		gender = bundle.getString("gender");
		tv_gender.setText(gender);
		wechatCheck = bundle.getString("wechat_check");
		if (wechatCheck.equals("1")) {
			tv_weixin.setTextColor(getResources().getColor(
					R.color.color_neutralgrey));
			tv_weixin.setText("已绑定");
		} else {
			tv_weixin.setTextColor(getResources().getColor(R.color.color_red));
			tv_weixin.setText("未绑定");
		}
		if (TextUtils.isEmpty(cellphone) || "未绑定".equals(cellphone)) {
			tv_cellphone.setTextColor(getResources()
					.getColor(R.color.color_red));
			tv_cellphone.setText("未绑定");
		} else {
			tv_cellphone.setTextColor(getResources().getColor(
					R.color.color_neutralgrey));
			tv_cellphone.setText(cellphone);
		}
		cellphone = bundle.getString("cellphone");
		icommunity = bundle.getString("icommunity");
		tv_icommunity.setText(icommunity);
		province = bundle.getString("province");
		cityname = bundle.getString("cityname");
		tv_place.setText(province + " - " + cityname);
		intent = new Intent(MyCompanyChangeInfoActivity.this, ChangeInfoActivity.class);
	}

	private void initEvent() {
		iv_back.setOnClickListener(this);
		rl_exit.setOnClickListener(this);
		rl_cellphone.setOnClickListener(this);
		rl_gender.setOnClickListener(this);
		rl_icommunity.setOnClickListener(this);
		rl_nickname.setOnClickListener(this);
		rl_place.setOnClickListener(this);
		rl_weixin.setOnClickListener(this);
		rl_place.setOnClickListener(this);
		rl_gender.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.rl_weixin:
			if (wechatCheck.equals("1")) {
				Toast.makeText(getApplicationContext(), "您已经绑定过了！",
						Toast.LENGTH_SHORT).show();
			} else {
//				UMWXHandler wxHandler = new UMWXHandler(
//						MyCompanyChangeInfoActivity.this, "wx20c4f4560dcd397a",
//						"9b06e848d40bcb04205d75335df6b814");
//				wxHandler.addToSocialSDK();
//				bindThirdParty(SHARE_MEDIA.WEIXIN);
			}
			break;
		case R.id.rl_exit:
			operExit();
			break;
		case R.id.rl_nickname:
			intent.putExtra("title", "修改昵称");
			startActivityForResult(intent, 1);
			break;
		case R.id.rl_icommunity:
			intent.putExtra("title", "修改小区");
			startActivityForResult(intent, 2);
			break;
		case R.id.rl_place:
			Builder builder = new SelectCityDialog.Builder(this);
			builder.setPositiveButton("继续",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent selectCityIntent = new Intent(
									MyCompanyChangeInfoActivity.this,
									SelectCtiyActivity.class);
							selectCityIntent.putExtra("isSelectCity", true);
							startActivityForResult(selectCityIntent, 3);
							dialog.cancel();
						}
					});

			builder.create().show();

			break;
		case R.id.rl_gender:
			operSelectSex();
			break;
		case R.id.rl_cellphone:
			Intent bindingPhoneIntent = new Intent(this,
					BindingPhoneActivity.class);
			bindingPhoneIntent.putExtra("token", token);
			startActivityForResult(bindingPhoneIntent, 5);
			break;

		default:
			break;
		}
	}

	private void operSelectSex() {
		final SelectSexPopupWindow popupWindow = new SelectSexPopupWindow(
				getApplicationContext());
		popupWindow.showAtLocation(rl_gender.getRootView(), Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
		popupWindow.tv_enter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				boolean isMale = popupWindow.bt_male.isChecked();
//				String sex = null;
//				if (isMale) {
//					sex = "男";
//				} else {
//					sex = "女";
//				}
//				chageInfoParams = AppInfoUtil.getPublicParams(getApplicationContext());
//				chageInfoParams.put("token", token);
//				chageInfoParams.put("field", "2");
//				chageInfoParams.put("new", sex);
//				tv_gender.setText(sex);
//				popupWindow.dismiss();
//				requestChangeInfo();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			chageInfoParams = AppInfoUtil.getPublicHashMapParams(getApplicationContext());
			chageInfoParams.put("token", token);
			switch (requestCode) {
			case 1:
				chageInfoParams.put("field", "1");
				chageInfoParams.put("new", data.getStringExtra("result"));
				tv_nickname.setText(data.getStringExtra("result"));
				requestChangeInfo();
				break;
			case 2:
				chageInfoParams.put("field", "4");
				chageInfoParams.put("new", data.getStringExtra("result"));
				tv_icommunity.setText(data.getStringExtra("result"));
				requestChangeInfo();
				break;
			case 3:
				chageInfoParams.put("field", "3");
				if (!TextUtils.isEmpty(data.getStringExtra("result"))) {
					chageInfoParams.put("new", data.getStringExtra("result"));
					tv_place.setText(data.getStringExtra("result"));
					requestChangeInfo();
				}
				break;
			case 5:
				tv_cellphone.setText(data.getStringExtra("result"));
				break;

			default:
				break;
			}
		}
	}

	
	private void requestChangeInfo() {
		OKHttpUtil.post(userChageInfoUrl, chageInfoParams, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {

			}
		});
	}

	private void operExit() {

		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage("你确定退出账号吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int id) {
						getSharedPreferences("userInfo", 0).edit().clear()
								.commit();
						AppInfoUtil.ISJUSTLOGIN = true;
						getSharedPreferences("userInfo", 0).edit().clear()
								.commit();
						XGPushManager.unregisterPush(getApplicationContext());
						dialog.cancel();
						finish();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		builder.create().show();

	}

//	/**
//	 * 授权。如果授权成功，则获取用户信息 ---第三方登录
//	 *
//	 * @param platform
//	 */
//	private void bindThirdParty(final SHARE_MEDIA platform) {
//		mController.doOauthVerify(MyCompanyChangeInfoActivity.this, platform,
//				new UMAuthListener() {
//
//					@Override
//					public void onStart(SHARE_MEDIA platform) {
//						Toast.makeText(MyCompanyChangeInfoActivity.this, "授权开始",
//								Toast.LENGTH_SHORT).show();
//					}
//
//					@Override
//					public void onError(SocializeException e,
//							SHARE_MEDIA platform) {
//						Toast.makeText(MyCompanyChangeInfoActivity.this, "授权失败",
//								Toast.LENGTH_SHORT).show();
//					}
//
//					@Override
//					public void onComplete(Bundle value, SHARE_MEDIA platform) {
//						// 获取uid
//						String uid = value.getString("uid");
//						if (!TextUtils.isEmpty(uid)) {
//							getUserInfo(platform);
//
//						} else {
//							Toast.makeText(MyCompanyChangeInfoActivity.this,
//									"绑定失败...", Toast.LENGTH_LONG).show();
//						}
//					}
//
//					@Override
//					public void onCancel(SHARE_MEDIA platform) {
//						Toast.makeText(MyCompanyChangeInfoActivity.this, "绑定取消",
//								Toast.LENGTH_SHORT).show();
//					}
//				});
//	}
//
//	/**
//	 * 获取用户信息
//	 *
//	 * @param platform
//	 */
//	private void getUserInfo(final SHARE_MEDIA platform) {
//		mController.getPlatformInfo(MyCompanyChangeInfoActivity.this, platform,
//				new UMDataListener() {
//
//					@Override
//					public void onStart() {
//						Toast.makeText(getApplicationContext(), "获取平台数据开始...",
//								Toast.LENGTH_SHORT).show();
//					}
//
//					@Override
//					public void onComplete(int status, Map<String, Object> info) {
//						if (status == 200 && info != null) {
//							weiXinUserName = (String) info.get("nickname");
//							weiXinImageUrl = (String) info.get("headimgurl");
//							weiXinUserId = (String) info.get("unionid");
//							operBindThirdParty();
//						} else {
//							Toast.makeText(getApplicationContext(),
//									"获取用户信息失败！", Toast.LENGTH_SHORT).show();
//							return;
//						}
//					}
//
//				});
//	}

	private void operBindThirdParty() {
		bindThirdPartyParams = AppInfoUtil.getPublicHashMapParams(getApplicationContext());
		bindThirdPartyParams.put("token", token);
		bindThirdPartyParams.put("kind", "weixin");
		bindThirdPartyParams.put("icon", weiXinImageUrl);
		bindThirdPartyParams.put("nickname", weiXinUserName);
		bindThirdPartyParams.put("account", weiXinUserId);

		OKHttpUtil.post(bindThirdPartyUrl, bindThirdPartyParams, new Callback() {
					@Override
					public void onFailure(Call call, IOException e) {

					}

					@Override
					public void onResponse(Call call, Response response) throws IOException {
						final String json = response.body().string();
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								try {
									JSONObject jsonObject = new JSONObject(json);
									Toast.makeText(getApplicationContext(),
											jsonObject.getString("msg"),
											Toast.LENGTH_SHORT).show();
									if (jsonObject.getInt("error_code") == 0) {
										tv_weixin.setTextColor(getResources().getColor(
												R.color.color_neutralgrey));
										tv_weixin.setText("已绑定");
									} else {
										Toast.makeText(getApplicationContext(),
												jsonObject.getString("msg"),
												Toast.LENGTH_SHORT).show();
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						});
					}
				});
	}

}