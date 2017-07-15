package com.tbs.tobosupicture.activity;

import android.content.Context;
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
            HttpGetCode();
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
    private void HttpGetCode() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
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

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //TODO 进行下一步操作 验证码验证是否成功验证成功则进入下一步
    private void nextStep() {

    }
}
