package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.GetVerificationPopupwindow;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.adapter.utils.AppInfoUtil;
import com.tbs.tobosutype.adapter.utils.HintInput;
import com.tbs.tobosutype.adapter.utils.HttpServer;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 预约申请页面
 *
 * @author dec
 */
public class ApplyforSuccessActivity extends Activity implements OnClickListener {
    private ImageView applyfor_back;
    private RelativeLayout rl_top;
    /***绑定手机布局*/
    private LinearLayout ll_bind_phone;
    private String phone;
    private TextView tv_phone;

    /***重新获取验证码*/
    private Button bt_obtain_verif;

    /***确定按钮*/
    private Button bt_obtain_enter;
    private EditText et_security_code;

    /**
     * 倒计时60s
     */
    private int count = 60;
    private RequestParams fastLoginParams;

    /**
     * 快速注册用户接口
     */
    private String fastLoginUrl = Constant.TOBOSU_URL + "tapp/passport/fast_register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_applyfor_success);
        initView();
        initEvent();
    }

    private void initView() {
//		AppInfoUtil.setActivityTheme(this, R.color.whole_color_theme);
        rl_top = (RelativeLayout) findViewById(R.id.rl_top);
        applyfor_back = (ImageView) findViewById(R.id.applyfor_back);
        ll_bind_phone = (LinearLayout) findViewById(R.id.ll_bind_phone);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        bt_obtain_verif = (Button) findViewById(R.id.bt_obtain_verif);
        bt_obtain_enter = (Button) findViewById(R.id.bt_obtain_enter);
        et_security_code = (EditText) findViewById(R.id.et_security_code);
        phone = getIntent().getStringExtra("phone");
        if (TextUtils.isEmpty(AppInfoUtil.getToekn(this))
                && !TextUtils.isEmpty(phone)) {
            ll_bind_phone.setVisibility(View.VISIBLE);
            bt_obtain_enter.setVisibility(View.VISIBLE);
        } else {
            ll_bind_phone.setVisibility(View.GONE);
            bt_obtain_enter.setVisibility(View.GONE);
        }
        tv_phone.setText(phone);
        rl_top.setBackgroundColor(Color.parseColor("#ff882e"));
    }

    private void initEvent() {
        applyfor_back.setOnClickListener(this);
        bt_obtain_verif.setOnClickListener(this);
        bt_obtain_enter.setOnClickListener(this);
        new HintInput(4, et_security_code, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.applyfor_back:
                finish();
                break;
            case R.id.bt_obtain_enter:
                if (TextUtils.isEmpty(et_security_code.getText().toString().trim())) {
                    Toast.makeText(this, "验证码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    fastLoginParams = AppInfoUtil.getPublicParams(this);
                    fastLoginParams.put("mobile", phone);
                    fastLoginParams.put("msg_code", et_security_code.getText().toString().trim());
                    requestFastLogin();
                }
                break;
            case R.id.bt_obtain_verif:
                if ("重新获取".equals(bt_obtain_verif.getText().toString()) || "获取验证码".equals(bt_obtain_verif.getText().toString())) {
                    GetVerificationPopupwindow popupwindow = new GetVerificationPopupwindow(this);
                    popupwindow.phone = phone;
                    popupwindow.version = AppInfoUtil.getAppVersionName(this);
                    popupwindow.showAtLocation(bt_obtain_verif.getRootView(), Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                } else {
                    Toast.makeText(getApplicationContext(), "您已经获取过了!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    /***
     * 开始倒数
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
            }

            ;
        }.start();
    }

    private class Task implements Runnable {

        @Override
        public void run() {
            if (count > 0) {
                bt_obtain_verif.setText(count + "S");
                bt_obtain_verif.setEnabled(false);
            } else {
                bt_obtain_verif.setEnabled(true);
                bt_obtain_verif.setText("重新获取");
                count = 60;
            }
        }
    }

    /***
     * 快速注册用户接口请求
     */
    private void requestFastLogin() {
        HttpServer.getInstance().requestPOST(fastLoginUrl, fastLoginParams,
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] body) {
                        try {
                            JSONObject jsonObject = new JSONObject(new String(body));
                            if (jsonObject.getInt("error_code") == 0) {

                                parseJson(jsonObject);
                            } else {
                                Toast.makeText(ApplyforSuccessActivity.this,
                                        jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
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

    /***
     * 解析json 得到数据
     * @param jsonObject
     * @throws JSONException
     */
    private void parseJson(JSONObject jsonObject) throws JSONException {
        JSONObject data = jsonObject.getJSONObject("data");
        String icon = data.getString("icon");
        String nickname = data.getString("name");
        String mark = data.getString("mark");
        String token = data.getString("token");
        String userid = data.getString("uid");
        String cityname = data.getString("cityname");
        SharedPreferences settings = getSharedPreferences("userInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("nickname", nickname);
        editor.putString("icon", icon);
        editor.putString("mark", mark);
        editor.putString("userid", userid);
        editor.putString("token", token);
        editor.putString("cityname", cityname);
        editor.commit();
        Toast.makeText(this, "绑定成功！", Toast.LENGTH_SHORT).show();
        finish();
    }
}
