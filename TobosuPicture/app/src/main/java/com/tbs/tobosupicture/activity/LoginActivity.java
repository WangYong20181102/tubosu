package com.tbs.tobosupicture.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class LoginActivity extends BaseActivity {
    @BindView(R.id.login_close)
    LinearLayout loginClose;
    @BindView(R.id.login_count)
    EditText loginCount;
    @BindView(R.id.login_password)
    EditText loginPassword;
    @BindView(R.id.login_forgot_password)
    LinearLayout loginForgotPassword;
    @BindView(R.id.login_regist)
    LinearLayout loginRegist;
    @BindView(R.id.login_login)
    TextView loginLogin;
    @BindView(R.id.login_weixin_login)
    LinearLayout loginWeixinLogin;


    private Context mContext;
    private String TAG = "LoginActivity";
    private Gson mGson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
        mGson = new Gson();

    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @OnClick({R.id.login_close, R.id.login_forgot_password, R.id.login_regist, R.id.login_login, R.id.login_weixin_login})
    public void onViewClickedInLoginActivity(View view) {
        switch (view.getId()) {
            case R.id.login_close:
                finish();
                break;
            case R.id.login_forgot_password:
                //忘记密码
                gotoFindPassWord();
                break;
            case R.id.login_regist:
                //注册
                gotoRegistAccount();
                break;
            case R.id.login_login:
                //登录
                gotoLogin(loginCount.getText().toString(), loginPassword.getText().toString());
                break;
            case R.id.login_weixin_login:
                //微信登录
                break;
        }
    }

    //去注册
    private void gotoRegistAccount() {
        Intent intent = new Intent(mContext, RegisterActivity.class);
        startActivity(intent);
    }

    //找回密码
    private void gotoFindPassWord() {
        Intent intent = new Intent(mContext, FindPasswordActivity.class);
        startActivity(intent);
    }

    //登录
    private void gotoLogin(String account, String password) {
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(mContext, "请输入账号~", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(mContext, "请输入密码~", Toast.LENGTH_SHORT).show();
            return;
        }
        final String md5PassWord = Md5Utils.md5(password);
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("account", account);
        param.put("password", md5PassWord);
        param.put("system_type", "1");
        HttpUtils.doPost(UrlConstans.USER_LOGIN, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败===" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    final String msg = jsonObject.getString("msg");
                    if (status.equals("200")) {
                        //用户登录成功将数据保存至Sp中
                        String data = jsonObject.getString("data");
                        _User user = mGson.fromJson(data, _User.class);
                        SpUtils.saveUserNick(mContext, user.getNick());
                        SpUtils.saveUserIcon(mContext, user.getIcon());
                        SpUtils.saveUserPersonalSignature(mContext, user.getPersonal_signature());
                        SpUtils.saveUserUid(mContext, user.getUid());
                        SpUtils.saveUserType(mContext, user.getUser_type());
                        Log.e(TAG, "获取用户的uid====" + SpUtils.getUserUid(mContext));
                        EventBusUtil.sendEvent(new Event(EC.EventCode.LOGIN_INITDATA));
                        finish();
                    } else if (status.equals("0")) {
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
}
