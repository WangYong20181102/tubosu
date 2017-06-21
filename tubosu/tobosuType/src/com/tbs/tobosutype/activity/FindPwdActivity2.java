package com.tbs.tobosutype.activity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.GetVerificationPopupwindow;
import com.tbs.tobosutype.global.AllConstants;
import com.tbs.tobosutype.http.HttpPost;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.HintInput;
import com.tbs.tobosutype.utils.MD5Util;
/**
 * 找回密码 2页面
 */
public class FindPwdActivity2 extends Activity implements OnClickListener {
	private Context mContext;
	private ImageView findpwd_two_back;
	private EditText findpwd_two_inputverif;
	private Button findpwd_two_obtain;
	private EditText findpwd_two_inputnewpwd;
	
	/**确定新密码按钮*/
	private Button findpwd_two_submit;
	private String mobile, smsVerif, newPwd;

	/**获取验证码的接口*/
	private String urlBase = AllConstants.TOBOSU_URL + "tapp/passport/get_pic_code?version=";

	private static int count = 60;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		AppInfoUtil.setActivityTheme(this, R.color.color_icon);
		AppInfoUtil.setTranslucentStatus(this);
		setContentView(R.layout.activity_findpwdtwo);
		
		mContext = FindPwdActivity2.this;
				
		urlBase = urlBase + getAppVersionName(this) + "&device=android";
		
		findpwd_two_back = (ImageView) findViewById(R.id.findpwd_two_back);
		findpwd_two_inputverif = (EditText) findViewById(R.id.findpwd_two_inputverif);
		new HintInput(4, findpwd_two_inputverif, this);
		findpwd_two_obtain = (Button) findViewById(R.id.findpwd_two_obtain);
		findpwd_two_inputnewpwd = (EditText) findViewById(R.id.findpwd_two_inputnewpwd);
		findpwd_two_submit = (Button) findViewById(R.id.findpwd_two_submit);
		
		findpwd_two_back.setOnClickListener(this);
		findpwd_two_obtain.setOnClickListener(this);
		findpwd_two_submit.setOnClickListener(this);

		mobile = getIntent().getExtras().getString("mobile");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.findpwd_two_back:
			finish();
			break;
		case R.id.findpwd_two_obtain:
			if ("重新获取".equals(findpwd_two_obtain.getText().toString()) || "获取验证码".equals(findpwd_two_obtain.getText().toString())) {
				GetVerificationPopupwindow popupwindow = new GetVerificationPopupwindow(FindPwdActivity2.this);
				popupwindow.phone = mobile;
				popupwindow.version = getAppVersionName(FindPwdActivity2.this);
				popupwindow.showAtLocation(findViewById(R.id.findpwd_two_submit), Gravity.CENTER, 0, 0);
			} else {
				Toast.makeText(mContext, "您已经获取过了!", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.findpwd_two_submit:
			smsVerif = findpwd_two_inputverif.getText().toString().trim();
			newPwd = findpwd_two_inputnewpwd.getText().toString().trim();
			
			if("".equals(findpwd_two_inputverif.getText().toString().trim())){
				Toast.makeText(mContext, "验证码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}else if("".equals(findpwd_two_inputnewpwd.getText().toString().trim())){
				Toast.makeText(mContext, "新密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("mobile", mobile);
			map.put("pass", MD5Util.md5(newPwd));
			map.put("msg_code", smsVerif);
			new SendFindPwdThread(map).start();
			
			break;
		default:
			break;
		}

	}

	/***
	 * 提交设置新密码接口
	 * @author dec
	 *
	 */
	class SendFindPwdThread extends Thread {
		HashMap<String, String> map;

		public SendFindPwdThread(HashMap<String, String> map) {
			this.map = map;
		}

		@Override
		public void run() {
			Looper.prepare();
			// 设置新密码接口
			String result = HttpPost.doPost(AllConstants.TOBOSU_URL + "tapp/passport/app_reset_password?", map, "utf-8");
			try {
				if (result != null) {
					JSONObject object = new JSONObject(result);
					Message msg = new Message();
					msg.what = 1;
					msg.obj = object;
					findPwdHandler.sendMessage(msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}


	private Handler findPwdHandler = new Handler() {

		// 在Handler中获取消息，重写handleMessage()方法
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				JSONObject resultObject = (JSONObject) msg.obj;
				try {
					int result = (Integer) resultObject.get("error_code");
					String msgData = resultObject.getString("msg");
					if (result == 0) {
						startCount();
						Toast.makeText(mContext, msgData, Toast.LENGTH_SHORT).show();
						Intent intent = new Intent();
		                //把返回数据存入Intent
		                intent.putExtra("resultData", "password has been changed");
		                setResult(RESULT_OK, intent);
		                finish();
					} else {
						Toast.makeText(mContext, msgData, Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	};

	/***
	 * 获取验证码倒计时
	 */
	public void startCount() {
		new Thread() {
			@Override
			public void run() {
				while (count > 0) {
					SystemClock.sleep(1000);
					runOnUiThread(new Task());
					count--;
				}
			};
		}.start();
	}

	private class Task implements Runnable {

		@Override
		public void run() {
			if (count > 0) {
				findpwd_two_obtain.setText(count + " s");
				findpwd_two_obtain.setEnabled(false);
			} else {
				findpwd_two_obtain.setEnabled(true);
				findpwd_two_obtain.setText("重新获取");
				count = 60;
			}
		}
	}

	/**
	 * 返回当前程序版本名
	 */
	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			
		}
		return versionName;
	}
	
}
