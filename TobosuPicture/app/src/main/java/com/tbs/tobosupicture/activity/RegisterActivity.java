package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
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

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean.EC;
import com.tbs.tobosupicture.bean.Event;
import com.tbs.tobosupicture.bean._User;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.EventBusUtil;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.Md5Utils;
import com.tbs.tobosupicture.utils.SpUtils;
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

public class RegisterActivity extends BaseActivity {
    private Context mContext;
    private String TAG = "RegisterActivity";
    private int count = 60;//倒计时时间60s
    private Gson mGson;

    @BindView(R.id.register_back)
    LinearLayout registerBack;//返回按钮
    @BindView(R.id.register_phone_num)
    EditText registerPhoneNum;//输入手机号框
    @BindView(R.id.register_clean_phone_num)
    ImageView registerCleanPhoneNum;//清除手机号码
    @BindView(R.id.register_code)
    EditText registerCode;//输入验证码
    @BindView(R.id.register_get_code)
    TextView registerGetCode;//获取验证码按钮
    @BindView(R.id.register_password)
    EditText registerPassword;//用户输入密码
    @BindView(R.id.register_ok)
    TextView registerOk;//注册按钮


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
        mGson = new Gson();
        registerPhoneNum.addTextChangedListener(textWatcher);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 0) {
                registerCleanPhoneNum.setVisibility(View.GONE);
            } else {
                registerCleanPhoneNum.setVisibility(View.VISIBLE);
            }
            if (s.length() == 11) {
                //获取验证码的按钮变颜色
                registerGetCode.setBackgroundResource(R.drawable.shape_get_code_yellow);
                registerGetCode.setTextColor(Color.parseColor("#ffffff"));
            } else {
                registerGetCode.setBackgroundResource(R.drawable.shape_get_code);
                registerGetCode.setTextColor(Color.parseColor("#86898f"));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @OnClick({R.id.register_back, R.id.register_phone_num, R.id.register_clean_phone_num, R.id.register_code, R.id.register_get_code, R.id.register_password, R.id.register_ok})
    public void onViewClickedInRegisterActivity(View view) {
        switch (view.getId()) {
            case R.id.register_back:
                finish();
                break;
            case R.id.register_clean_phone_num:
                cleanPhoneNum();
                break;
            case R.id.register_get_code:
                getCode();
                break;
            case R.id.register_ok:
                //各种验证最后才进入注册成功页
                intoRegister();
                break;
        }
    }

    //清空号码
    private void cleanPhoneNum() {
        registerPhoneNum.setText("");
    }

    //获取验证码
    private void getCode() {
        //判断手机号码是否合法
        String phoneNum = registerPhoneNum.getText().toString();
        if (!TextUtils.isEmpty(phoneNum) && phoneNum.matches(UrlConstans.PHONE_NUM)) {
            //手机号码当前合法 开始倒计时 获取验证码的按钮不可点击
            startCount();//开始倒计时
            HttpGetCode(phoneNum);//获取验证码
        } else {
            Toast.makeText(mContext, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
        }
    }

    //获取验证码
    private void HttpGetCode(String phoneNum) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("cellphone", phoneNum);
        HttpUtils.doPost(UrlConstans.GET_PHONE_CODE_URL, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "连接失败==" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "连接成功==" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    final String msg = jsonObject.getString("msg");
                    if (status.equals("200")) {
                        //获取数据成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "验证码已发送，请注意查收~", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (status.equals("203")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //获取验证码的倒计时
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
            if (count > 0 && registerGetCode != null) {
                registerGetCode.setText(count + "s");
                registerGetCode.setClickable(false);
                registerGetCode.setEnabled(false);
            } else {
                if (registerGetCode != null) {
                    registerGetCode.setText("重新获取");
                    registerGetCode.setEnabled(true);
                    registerGetCode.setClickable(true);
                    count = 60;
                }
            }
        }
    }

    //用户点击注册
    private void intoRegister() {
        /**
         * 处理逻辑
         * 1.验证手机号码的合法性
         * 2.验证服务器发来的验证码是否为空以及是否为6位
         * 3.验证密码的长度是否符合规定（6-16位）
         * 以上验证成功再走注册接口
         */
        if (!TextUtils.isEmpty(registerPhoneNum.getText().toString())
                && registerPhoneNum.getText().toString().matches(UrlConstans.PHONE_NUM)) {
            //验证 验证码的合法性
            if (!TextUtils.isEmpty(registerCode.getText().toString())
                    && registerCode.getText().toString().length() == 6) {
                //验证密码的合法性
                if (registerPassword.getText().toString().length() >= 6
                        && registerPassword.getText().toString().length() <= 16) {
                    //拿到数据进行注册
                    String phoneNum = registerPhoneNum.getText().toString();
                    String code = registerCode.getText().toString();
                    String md5PassWord = Md5Utils.md5(registerPassword.getText().toString());
                    HttpIntoRegister(phoneNum, code, md5PassWord);
                } else {
                    Toast.makeText(mContext, "输入的密码长度在6-16位之间哦~", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, "请输入正确的验证码！", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mContext, "输入的手机号码不合法！", Toast.LENGTH_SHORT).show();
        }
    }

    //用户用手机号码注册
    private void HttpIntoRegister(String phoneNum, String code, final String md5PassWord) {
        HashMap<String, Object> param = new HashMap<>();
        Log.e(TAG, "请求参数==phoneNum==" + phoneNum + "==code==" + code + "==md5PassWord==" + md5PassWord);
        param.put("token", Utils.getDateToken());
        param.put("cellphone", phoneNum);
        param.put("verify_code", code);
        param.put("password", md5PassWord);
        param.put("chcode", Utils.getChannType(mContext));
        param.put("system_type", "1");
        param.put("platform_type", "3");
        HttpUtils.doPost(UrlConstans.PHONE_NUM_REGISTER_URL, param, new Callback() {
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
                        //注册成功 将信息保存 然后跳转到个人信息页
                        String data = jsonObject.getString("data");
                        _User user = mGson.fromJson(data, _User.class);
                        SpUtils.saveUserUid(mContext, user.getUid());
                        SpUtils.saveUserNick(mContext, user.getNick());
                        SpUtils.saveUserIcon(mContext, user.getIcon());
                        SpUtils.saveUserType(mContext, user.getUser_type());
                        SpUtils.saveUserPersonalSignature(mContext, user.getPersonal_signature());
                        Intent intent = new Intent(mContext, PersonInfoActivity.class);
                        intent.putExtra("from", "RegisterActivity");
                        mContext.startActivity(intent);
                        EventBusUtil.sendEvent(new Event(EC.EventCode.FNISHI_LOGINACTIVITY));
                        finish();
                    } else {
                        //获取数据失败
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "注册失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }
}
