package com.tbs.tobosupicture.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

//绑定手机号码
public class BindPhoneNumActivity extends BaseActivity {

    @BindView(R.id.bind_phone_back)
    LinearLayout bindPhoneBack;
    @BindView(R.id.bind_phone_num)
    EditText bindPhoneNum;
    @BindView(R.id.find_pw_clean_phone_num)
    ImageView findPwCleanPhoneNum;
    @BindView(R.id.bind_phone_code)
    EditText bindPhoneCode;
    @BindView(R.id.bind_phone_get_code)
    TextView bindPhoneGetCode;
    @BindView(R.id.bind_phone_ok)
    TextView bindPhoneOk;

    private Context mContext;
    private String TAG = "BindPhoneNumActivity";
    private int count = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone_num);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    //初始化控件
    private void initViewEvent() {
        bindPhoneNum.addTextChangedListener(textWatcher);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 0) {
                findPwCleanPhoneNum.setVisibility(View.GONE);
            } else {
                findPwCleanPhoneNum.setVisibility(View.VISIBLE);
            }
            if (s.length() == 11) {
                //获取验证码的按钮变颜色
                bindPhoneGetCode.setBackgroundResource(R.drawable.shape_get_code_yellow);
                bindPhoneGetCode.setTextColor(Color.parseColor("#ffffff"));
            } else {
                bindPhoneGetCode.setBackgroundResource(R.drawable.shape_get_code);
                bindPhoneGetCode.setTextColor(Color.parseColor("#86898f"));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @OnClick({R.id.bind_phone_back, R.id.find_pw_clean_phone_num, R.id.bind_phone_get_code, R.id.bind_phone_ok})
    public void onViewClickedInBindPhoneActivity(View view) {
        switch (view.getId()) {
            case R.id.bind_phone_back:
                finish();
                break;
            case R.id.find_pw_clean_phone_num:
                //清除输入的手机号码
                bindPhoneNum.setText("");
                break;
            case R.id.bind_phone_get_code:
                //获取验证码  在这之前要进行手机号码是否注册验证
                getPhoenCode();
                break;
            case R.id.bind_phone_ok:
                //下一步 进入输入密码页面====输入密码再次验证
                if (!TextUtils.isEmpty(bindPhoneNum.getText().toString())
                        && bindPhoneNum.getText().toString().matches(UrlConstans.PHONE_NUM)) {
                    if (bindPhoneCode.getText().toString().length() == 6) {
                        HttpCheckCode(bindPhoneNum.getText().toString(), bindPhoneCode.getText().toString());
                    } else {
                        Toast.makeText(mContext, "您输入的验证码长度不符！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "您输入的手机号码有误！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //获取验证码
    private void getPhoenCode() {
        if (!TextUtils.isEmpty(bindPhoneNum.getText().toString())
                && bindPhoneNum.getText().toString().matches(UrlConstans.PHONE_NUM)) {
            //验证手机号码是否被注册过
            checkPhoneNumIsUser(bindPhoneNum.getText().toString());
        } else {
            Toast.makeText(mContext, "您输入的手机号码有误！", Toast.LENGTH_SHORT).show();
        }
    }

    //验证手机是否注册过
    private void checkPhoneNumIsUser(final String phoneNum) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("cellphone", phoneNum);
        HttpUtils.doPost(UrlConstans.IS_REGISTER, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败=============" + e.toString());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功========" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //手机号未注册
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startCount();
                            }
                        });
                        HttpGetCode(phoneNum);
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "该手机号码已经被注册，无法进行绑定。", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //获取验证码倒计时
    private void startCount() {
        new Thread() {
            @Override
            public void run() {
                while (count > 0) {
                    try {
                        Thread.sleep(1000);
                        runOnUiThread(new MyTask());
                        count--;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    //计时任务
    private class MyTask implements Runnable {

        @Override
        public void run() {
            if (count > 0 && bindPhoneGetCode != null) {
                bindPhoneGetCode.setText(count + " s");
                bindPhoneGetCode.setEnabled(false);
                bindPhoneGetCode.setClickable(false);
            } else {
                if (bindPhoneGetCode != null) {
                    bindPhoneGetCode.setText("重新获取");
                    bindPhoneGetCode.setEnabled(true);
                    bindPhoneGetCode.setClickable(true);
                    count = 60;
                }
            }
        }
    }

    //获取验证码
    //网络请求获取验证码
    private void HttpGetCode(String phoneNum) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("cellphone", phoneNum);
        HttpUtils.doPost(UrlConstans.GET_PHONE_CODE_URL, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败===" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功===" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "获取验证码成功请注意查收~", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void HttpCheckCode(final String phoneNum, String code) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("cellphone", phoneNum);
        param.put("verify_code", code);
        HttpUtils.doPost(UrlConstans.CHECK_VERIFY_CODE, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败=========" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功=====" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //验证码校验成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(mContext, InputPasswordActivity.class);
                                intent.putExtra("cellphone", phoneNum);
                                mContext.startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "您输入的验证码有误~", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
