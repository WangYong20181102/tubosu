package com.tbs.tobosutype.activity;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.GetVerificationPopupwindow;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.HintInput;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 绑定手机号页面
 *
 * @author dec
 */
public class BindingPhoneActivity extends Activity implements OnClickListener {
    private Context mContext;
    private EditText et_input_phone;
    private EditText et_input_verification;
    private ImageView iv_back_binding;
    private Button bt_obtain_verif;
    private Button bt_obtain_enter;
    private String phone;
    int count = 60;

    private String bangdingCellphoneUrl = Constant.TOBOSU_URL + "tapp/passport/app_bangding_cellphone";
    private HashMap<String, String > bangdingCellphoneParms;
    private String verfication;
    private String token;
    private RelativeLayout rl_banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppInfoUtil.setActivityTheme1(this, R.color.whole_color_theme);
//		AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_binding_mobile_phone);

        mContext = BindingPhoneActivity.this;
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        et_input_phone = (EditText) findViewById(R.id.et_input_phone);
        et_input_verification = (EditText) findViewById(R.id.et_input_verification);
        iv_back_binding = (ImageView) findViewById(R.id.iv_back_binding);
        bt_obtain_verif = (Button) findViewById(R.id.bt_obtain_verif);
        bt_obtain_enter = (Button) findViewById(R.id.bt_obtain_enter);
        rl_banner = (RelativeLayout) findViewById(R.id.rl_banner);
        rl_banner.setBackgroundColor(Color.parseColor("#ff882e"));
    }

    private void initData() {
        bangdingCellphoneParms = AppInfoUtil.getPublicHashMapParams(getApplicationContext());
        token = getIntent().getStringExtra("token");
    }

    private void initEvent() {
        bt_obtain_verif.setOnClickListener(this);
        bt_obtain_enter.setOnClickListener(this);
        iv_back_binding.setOnClickListener(this);

        new HintInput(11, et_input_phone, getApplicationContext());
        new HintInput(4, et_input_verification, getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_binding:
                finish();
                break;
            case R.id.bt_obtain_verif:
                phone = et_input_phone.getText().toString().trim();
                if (isPhoneNum(phone)) {
                    if ("重新获取".equals(bt_obtain_verif.getText().toString()) || "获取验证码".equals(bt_obtain_verif.getText().toString())) {
                        GetVerificationPopupwindow popupwindow = new GetVerificationPopupwindow(BindingPhoneActivity.this);
                        popupwindow.phone = phone;
                        popupwindow.version = AppInfoUtil.getAppVersionName(getApplicationContext());
                        popupwindow.showAtLocation(findViewById(R.id.bt_obtain_verif), Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                    } else {
                        Toast.makeText(mContext, "您已经获取过了!", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.bt_obtain_enter:
                verfication = et_input_verification.getText().toString().trim();
                if (TextUtils.isEmpty(verfication)) {
                    Toast.makeText(mContext, "验证码不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                bangdingCellphoneParms.put("mobile", phone);
                bangdingCellphoneParms.put("msg_code", verfication);
                bangdingCellphoneParms.put("token", token);
                OKHttpUtil.post(bangdingCellphoneUrl, bangdingCellphoneParms, new Callback() {
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
                                    if (jsonObject.getInt("error_code") == 0) {
                                        Toast.makeText(mContext, "修改绑定成功!", Toast.LENGTH_SHORT).show();
                                        Intent intent = getIntent();
                                        intent.putExtra("result", phone);
                                        BindingPhoneActivity.this.setResult(0, intent);
                                        finish();
                                    } else {
                                        Toast.makeText(mContext, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
                break;
            default:
                break;
        }
    }


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
     * 判断电话号码是否合法
     * @param phoneNum
     * @return
     */
    public boolean isPhoneNum(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum) || "".equals(phoneNum)) {
            Toast.makeText(mContext, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            String MOBILE = "^1(3[0-9]|5[0-35-9]|7[0136-8]|8[0-9])\\d{8}$";
            Pattern pattern = Pattern.compile(MOBILE);
            Matcher matcher = pattern.matcher(phoneNum);
            boolean flag = matcher.matches();
            if (flag == false) {
                Toast.makeText(mContext, "请输入合法手机号码", Toast.LENGTH_SHORT).show();
            }
            return matcher.matches();
        }
    }

}
