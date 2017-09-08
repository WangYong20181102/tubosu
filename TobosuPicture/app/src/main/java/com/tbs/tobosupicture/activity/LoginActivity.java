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
import com.tbs.tobosupicture.MyApplication;
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
import com.tbs.tobosupicture.view.CustomWaitDialog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.weixin.handler.UmengWXHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    private UMShareAPI mShareAPI;
    private CustomWaitDialog customWaitDialog;


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
        mShareAPI = UMShareAPI.get(mContext);
        customWaitDialog = new CustomWaitDialog(mContext);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.FNISHI_LOGINACTIVITY:
                finish();
                break;
        }
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
                customWaitDialog.show();
                //先清除token
//                mShareAPI.deleteOauth(LoginActivity.this, SHARE_MEDIA.WEIXIN, null);
                weChatLogin();
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "链接服务器失败！", Toast.LENGTH_SHORT).show();
                    }
                });
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
                        EventBusUtil.sendEvent(new Event(EC.EventCode.REFRESH_MY_ORGIN_NUM));
                        //友盟的账号统计:统计规则  平台+UId
                        MobclickAgent.onProfileSignIn(Utils.getChannType(MyApplication.getContexts()), SpUtils.getUserUid(mContext));
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

    //微信登录
    private void weChatLogin() {
        //再次进行授权
        mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                //授权成功 获取信息
                String icon = map.get("iconurl");//微信的头像
                String nickname = map.get("name");//微信的昵称
                String account = map.get("openid");//微信的openid
                Log.e(TAG, "授权成功==头像==" + icon + "===nickname===" + nickname + "===account===" + account);
                HttpWeChatLogin(icon, nickname, account);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                //授权出错
                customWaitDialog.dismiss();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                //取消微信授权
                customWaitDialog.dismiss();
                Toast.makeText(mContext, "取消了微信授权登录！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void HttpWeChatLogin(String icon, String nickname, String account) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("icon", icon);
        param.put("nickname", nickname);
        param.put("account", account);
        param.put("system_type", "1");
        param.put("chcode", Utils.getChannType(mContext));
        HttpUtils.doPost(UrlConstans.WECHAT_LOGIN, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败========" + e.toString());
                customWaitDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功======" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //登录成功
                        String data = jsonObject.getString("data");
                        _User user = mGson.fromJson(data, _User.class);
                        //将数据注入Sp中
                        SpUtils.saveUserNick(mContext, user.getNick());
                        SpUtils.saveUserIcon(mContext, user.getIcon());
                        SpUtils.saveUserPersonalSignature(mContext, user.getPersonal_signature());
                        SpUtils.saveUserUid(mContext, user.getUid());
                        SpUtils.saveUserType(mContext, user.getUser_type());
                        Log.e(TAG, "获取用户的uid====" + SpUtils.getUserUid(mContext));
                        EventBusUtil.sendEvent(new Event(EC.EventCode.LOGIN_INITDATA));
                        EventBusUtil.sendEvent(new Event(EC.EventCode.REFRESH_MY_ORGIN_NUM));
                        //友盟统计微信登录的用户数据
                        MobclickAgent.onProfileSignIn("weixin", SpUtils.getUserUid(mContext));
                        customWaitDialog.dismiss();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
