package com.tbs.tobosutype.activity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.http.HttpPost;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.HintInput;
/**
 *  注册页面 2
 * @author dec
 *
 */
public class RegisterActivity2 extends Activity implements OnClickListener {
	private ImageView reg_two_back;
	private EditText reg_two_input_verif;
	private Button reg_two_verif_obtain;
	private Button reg_two_button;
	
	/***注册接口*/
	private String registerUrl = Constant.TOBOSU_URL + "tapp/passport/fast_register?";

	/****/
	private String urlBase = Constant.TOBOSU_URL + "tapp/passport/get_pic_code?version=";
	private String phone, smsVerif;
	private String token;

	private static int count = 60;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppInfoUtil.setTranslucentStatus(this);
		setContentView(R.layout.activity_reg_two);
		urlBase = urlBase
				+ AppInfoUtil.getAppVersionName(getApplicationContext())
				+ "&device=android";
		reg_two_back = (ImageView) findViewById(R.id.reg_two_back);
		reg_two_input_verif = (EditText) findViewById(R.id.reg_two_input_verif);
		reg_two_verif_obtain = (Button) findViewById(R.id.reg_two_verif_obtain);
		reg_two_button = (Button) findViewById(R.id.reg_two_button);

		reg_two_back.setOnClickListener(this);
		reg_two_verif_obtain.setOnClickListener(this);
		reg_two_button.setOnClickListener(this);
		new HintInput(4, reg_two_input_verif, this);
		phone = getIntent().getExtras().getString("mobile");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reg_two_back:
			finish();
			break;
		case R.id.reg_two_verif_obtain:
			if ("重新获取".equals(reg_two_verif_obtain.getText().toString())
					|| "获取验证码"
							.equals(reg_two_verif_obtain.getText().toString())) {
				GetVerificationPopupwindow popupwindow = new GetVerificationPopupwindow(
						RegisterActivity2.this);
				phone = getIntent().getStringExtra("phone");
				popupwindow.phone = phone;
				popupwindow.version = AppInfoUtil
						.getAppVersionName(getApplicationContext());
				popupwindow.showAtLocation(findViewById(R.id.reg_two_button),
						Gravity.CENTER, 0, 0);
			} else {
				Toast.makeText(getApplicationContext(), "您已经获取过了!",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.reg_two_button:
			smsVerif = reg_two_input_verif.getText().toString().trim();
			if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(smsVerif)) {
				Toast.makeText(
						RegisterActivity2.this,
						getResources().getString(
								R.string.fast_logging_toast_all),
						Toast.LENGTH_SHORT).show();
			} else {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("mobile", phone);
				map.put("msg_code", smsVerif);
				new SendLoginThread(map).start();
			}
			break;
		default:
			break;
		}

	}

	class SendLoginThread extends Thread {
		HashMap<String, String> map;

		public SendLoginThread(HashMap<String, String> map) {
			this.map = map;
		}

		@Override
		public void run() {
			Looper.prepare();
			sendLoginMessage(map);
		}
	}

	private void sendLoginMessage(HashMap<String, String> map) {
		String result = HttpPost.doPost(registerUrl, map, "GBK"); 
		try {
			if (result != null) {
				JSONObject object = new JSONObject(result);
				Message msg = new Message();
				msg.what = 1;
				msg.obj = object;
				loginHandler.sendMessage(msg);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private Handler loginHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				try {
					JSONObject resultObject = (JSONObject) msg.obj;
					int result = (Integer) resultObject.get("error_code");
					String msgData = resultObject.getString("msg");

					if (result == 0) {
						JSONObject dataObject = resultObject.getJSONObject("data");
						String icon = dataObject.getString("icon");
						String nickname = dataObject.getString("nickname");
						String mark = dataObject.getString("mark");
						token = dataObject.getString("token");

						token = token.replaceAll("\\+", "%2B");
						token = token.replaceAll("\\/", "%2F");
						token = token.replaceAll("\\=", "%3D");

						SharedPreferences settings = getSharedPreferences("userInfo", 0);
						SharedPreferences.Editor editor = settings.edit();
						editor.putString("nickname", nickname);
						editor.putString("icon", icon);
						editor.putString("mark", mark);
						editor.putString("token", token);
						editor.commit();

						Intent intent = new Intent(RegisterActivity2.this, MainActivity.class);
						intent.putExtra("isLoginActivityIntent", true);
						startActivity(intent);
						finish();
					} else {
						Toast.makeText(RegisterActivity2.this, msgData, Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	};

	/***
	 * 倒计时
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
				reg_two_verif_obtain.setText(count + "s");
				reg_two_verif_obtain.setEnabled(false);
			} else {
				reg_two_verif_obtain.setEnabled(true);
				reg_two_verif_obtain.setText("重新获取");
				count = 60;
			}
		}
	}

}
