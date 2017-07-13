package com.tbs.tobosutype.activity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.http.HttpPost;
import com.tbs.tobosutype.adapter.utils.AppInfoUtil;
import com.tbs.tobosutype.adapter.utils.HintInput;
/**
 * 用户注册页面1
 * @author dec
 *
 */
public class RegisterActivity1 extends Activity implements OnClickListener {
	private ImageView reg_one_back;
	private EditText reg_one_inputbundlephone;
	private Button reg_one_nextstep;
	private String bundlephone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppInfoUtil.setTranslucentStatus(this);
		setContentView(R.layout.activity_reg_one);

		reg_one_back = (ImageView) findViewById(R.id.reg_one_back);
		reg_one_inputbundlephone = (EditText) findViewById(R.id.reg_one_inputbundlephone);
		reg_one_nextstep = (Button) findViewById(R.id.reg_one_nextstep);
		reg_one_back.setOnClickListener(this);
		reg_one_nextstep.setOnClickListener(this);
		new HintInput(11, reg_one_inputbundlephone, this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reg_one_back:
			finish();
			break;
		case R.id.reg_one_nextstep:
			bundlephone = reg_one_inputbundlephone.getText().toString().trim();
			if (TextUtils.isEmpty(bundlephone)) {
				Toast.makeText(RegisterActivity1.this, getResources().getString(R.string.reg_toast),
						Toast.LENGTH_SHORT).show();
			} else {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("mobile", bundlephone);
				map.put("version", AppInfoUtil.getAppVersionName(getApplicationContext()));
				map.put("device", AppInfoUtil.getDeviceName());
				new SendMessageThread(map).start();
			}
		default:
			break;
		}

	}

	class SendMessageThread extends Thread {

		HashMap<String, String> map;

		public SendMessageThread(HashMap<String, String> map) {
			this.map = map;
		}

		@Override
		public void run() {
			Looper.prepare();
			sendMessage(map);
		}
	}

	private void sendMessage(HashMap<String, String> map) {
		String result = HttpPost.doPost(Constant.TOBOSU_URL + "tapp/passport/query_mobile?", map, "utf-8");
		try {
			if (result != null) {
				JSONObject object = new JSONObject(result);
				Message msg = new Message();
				msg.what = 0;
				msg.obj = object;
				handler.sendMessage(msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				JSONObject resultObject = (JSONObject) msg.obj;

				try {
					int result = (Integer) resultObject.get("error_code");
					String msgData = resultObject.getString("msg");
					if (result == 0) {
						Intent intent = new Intent(RegisterActivity1.this,RegisterActivity2.class);
						Bundle bundle = new Bundle();
						bundle.putString("mobile", bundlephone);
						intent.putExtras(bundle);
						intent.putExtra("phone", bundlephone);
						startActivity(intent);

					} else {
						Toast.makeText(RegisterActivity1.this, msgData, Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}
	};

}
