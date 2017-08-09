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

/**
 * create by lin
 * 找回密码页面
 */
public class FindPasswordActivity extends BaseActivity {

    private Context mContext;
    private String TAG = "FindPasswordActivity";
    private int count = 60;

    @BindView(R.id.find_pw_back)
    LinearLayout findPwBack;
    @BindView(R.id.find_pw_phone_num)
    EditText findPwPhoneNum;
    @BindView(R.id.find_pw_clean_phone_num)
    ImageView findPwCleanPhoneNum;
    @BindView(R.id.find_pw_code)
    EditText findPwCode;
    @BindView(R.id.find_pw_get_code)
    TextView findPwGetCode;
    @BindView(R.id.find_pw_ok)
    TextView findPwOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    //初始化控件事件
    private void initViewEvent() {
        findPwPhoneNum.addTextChangedListener(textWatcher);
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
                findPwGetCode.setBackgroundResource(R.drawable.shape_get_code_yellow);
                findPwGetCode.setTextColor(Color.parseColor("#ffffff"));
            } else {
                findPwGetCode.setBackgroundResource(R.drawable.shape_get_code);
                findPwGetCode.setTextColor(Color.parseColor("#86898f"));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @OnClick({R.id.find_pw_back, R.id.find_pw_clean_phone_num, R.id.find_pw_get_code, R.id.find_pw_ok})
    public void onViewClickedInFindPasswordActivity(View view) {
        switch (view.getId()) {
            case R.id.find_pw_back:
                finish();
                break;
            case R.id.find_pw_clean_phone_num:
                //清除输入的手机号码
                findPwPhoneNum.setText("");
                break;
            case R.id.find_pw_get_code:
                //获取验证码
                getPhoenCode();
                break;
            case R.id.find_pw_ok:
                //下一步
                nextStep();
                break;
        }
    }

    //获取验证码
    private void getPhoenCode() {
        if (!TextUtils.isEmpty(findPwPhoneNum.getText().toString())
                && findPwPhoneNum.getText().toString().matches(UrlConstans.PHONE_NUM)) {
            startCount();
            HttpGetCode(findPwPhoneNum.getText().toString());
        } else {
            Toast.makeText(mContext, "您输入的手机号码有误！", Toast.LENGTH_SHORT).show();
        }
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
            if (count > 0 && findPwGetCode != null) {
                findPwGetCode.setText(count + " s");
                findPwGetCode.setEnabled(false);
                findPwGetCode.setClickable(false);
            } else {
                if (findPwGetCode != null) {
                    findPwGetCode.setText("重新获取");
                    findPwGetCode.setEnabled(true);
                    findPwGetCode.setClickable(true);
                    count = 60;
                }
            }
        }
    }

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

    //TODO 进行下一步操作 验证码验证是否成功验证成功则进入下一步 设置新的密码 在下一个界面按钮确认登录
    private void nextStep() {
        //验证去进入下一个页面修改密码
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("cellphone", findPwPhoneNum.getText().toString());
        param.put("verify_code", findPwCode.getText().toString());
        HttpUtils.doPost(UrlConstans.SEARCH_PASSWORD, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败=======" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功====" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //验证成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Intent intent = new Intent(mContext,);
                            }
                        });
                    } else {
                        //验证失败
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
