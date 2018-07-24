package com.tbs.tbsbusiness.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.activity.CheckPhoneNumActivity;
import com.tbs.tbsbusiness.activity.LoginActivity;
import com.tbs.tbsbusiness.activity.MainActivity;
import com.tbs.tbsbusiness.base.BaseFragment;
import com.tbs.tbsbusiness.bean.EC;
import com.tbs.tbsbusiness.bean.Event;
import com.tbs.tbsbusiness.bean._User;
import com.tbs.tbsbusiness.config.Constant;
import com.tbs.tbsbusiness.util.EventBusUtil;
import com.tbs.tbsbusiness.util.LogUtil;
import com.tbs.tbsbusiness.util.MD5Util;
import com.tbs.tbsbusiness.util.OKHttpUtil;
import com.tbs.tbsbusiness.util.SpUtil;
import com.tbs.tbsbusiness.util.ToastUtil;
import com.tbs.tbsbusiness.util.Util;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Lin on 2018/6/4 13:34.
 */
public class LoginFragmentAccount extends BaseFragment {
    @BindView(R.id.fal_account)
    EditText falAccount;
    @BindView(R.id.fal_clean_account)
    ImageView falCleanAccount;
    @BindView(R.id.fal_password)
    EditText falPassword;
    @BindView(R.id.fal_clean_password)
    ImageView falCleanPassword;
    @BindView(R.id.fal_forget_password)
    TextView falForgetPassword;
    @BindView(R.id.fal_login_btn)
    TextView falLoginBtn;
    @BindView(R.id.fal_weixin_login_btn)
    RelativeLayout falWeixinLoginBtn;
    Unbinder unbinder;
    private String TAG = "NewLoginFragmentAccount";
    private Context mContext;
    private Gson mGson;
    private LoginActivity mLoginActivity;
    private UMShareAPI umShareAPI;

    public LoginFragmentAccount() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_login, null);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        initViewEvent();
        return view;
    }

    private void initViewEvent() {
        mGson = new Gson();
        umShareAPI = UMShareAPI.get(mContext);
        mLoginActivity = new LoginActivity();
        //密码输入监听
        falPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    falCleanPassword.setVisibility(View.GONE);
                } else {
                    falCleanPassword.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //账号输入监听
        falAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    falCleanAccount.setVisibility(View.GONE);
                } else {
                    falCleanAccount.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fal_clean_account, R.id.fal_clean_password,
            R.id.fal_forget_password, R.id.fal_login_btn,
            R.id.fal_weixin_login_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fal_clean_account:
                //清除输入的账号
                falAccount.setText("");
                falCleanAccount.setVisibility(View.GONE);
                break;
            case R.id.fal_clean_password:
                falPassword.setText("");
                falCleanPassword.setVisibility(View.GONE);
                break;
            case R.id.fal_forget_password:
                //忘记密码  进入修改密码
                mContext.startActivity(new Intent(mContext, CheckPhoneNumActivity.class));
                break;
            case R.id.fal_login_btn:
                //登录按钮
                if (!TextUtils.isEmpty(falAccount.getText().toString())) {
                    if (!TextUtils.isEmpty(falPassword.getText().toString())) {
                        //密码Md5 加密
                        String Md5PassWord = MD5Util.md5(falPassword.getText().toString());
                        HttpAccountLogin(falAccount.getText().toString(), Md5PassWord);
                    } else {
                        Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "请输入用户名", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.fal_weixin_login_btn:
                weiXinLogin();
                break;
        }
    }

    //账号登录
    private void HttpAccountLogin(String user_name, final String Md5Password) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("user_name", user_name);
        param.put("password", Md5Password);
        param.put("client_type", "1");
        param.put("device_id", SpUtil.getPushRegisterId(mContext));
        OKHttpUtil.post(Constant.ACCOUNT_LOGIN, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mLoginActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "链接服务器失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "数据获取成功=========" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    final String msg = jsonObject.optString("msg");
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        //登录成功
                        //存储相关信息
                        SpUtil.setUserLoginType(mContext, "1");
                        SpUtil.setUserLoginAccount(mContext, Md5Password);
                        String data = jsonObject.optString("data");
                        saveUserInfo(data);
                    } else {
                        umShareAPI.deleteOauth(getActivity(), SHARE_MEDIA.WEIXIN, null);
                        getActivity().runOnUiThread(new Runnable() {
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

    //微信号登录
    private void weiXinLogin() {
        umShareAPI.getPlatformInfo(getActivity(), SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//                String weiXinUserName = map.get("name");//微信的昵称
//                String weiXinImageUrl = map.get("iconurl");//微信的头像
                String weiXinUserId = map.get("unionid");// unionid打通用户在同一平台开发
                Log.e("微信的配置信息", "==================" + map.toString());
                HttpWeixinLogin(weiXinUserId);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Log.e(TAG, "授权出错=====" + throwable.getMessage());
                Toast.makeText(mContext, "授权出错！", Toast.LENGTH_SHORT).show();
                umShareAPI.deleteOauth(getActivity(), SHARE_MEDIA.WEIXIN, null);
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Toast.makeText(mContext, "取消微信登录！", Toast.LENGTH_SHORT).show();
                umShareAPI.deleteOauth(getActivity(), SHARE_MEDIA.WEIXIN, null);
            }
        });
    }

    //微信登录
    private void HttpWeixinLogin(final String unionid) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("unionid", unionid);
        param.put("client_type", "1");
        param.put("device_id", SpUtil.getPushRegisterId(mContext));
        OKHttpUtil.post(Constant.WECHAT_LOGIN, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mLoginActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "链接服务器失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功=============" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    final String msg = jsonObject.optString("msg");
                    if (status.equals("200")) {
                        //微信登录成功 解析数据
                        SpUtil.setUserLoginType(mContext, "2");
                        SpUtil.setUserLoginAccount(mContext, unionid);
                        String data = jsonObject.optString("data");
                        saveUserInfo(data);
                    } else if (status.equals("0")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //微信登录失败
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                UMShareAPI.get(mContext).deleteOauth(getActivity(), SHARE_MEDIA.WEIXIN, null);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //存储用户的信息  作用于微信  手机号登录
    private void saveUserInfo(String data) {
        _User mUser = mGson.fromJson(data, _User.class);
        SpUtil.setUserId(mContext, mUser.getUid());
        SpUtil.setCompany_id(mContext, mUser.getCompany_id());
        SpUtil.setCellphone(mContext, mUser.getCellphone());
        SpUtil.setCellphone_check(mContext, mUser.getCellphone_check());
        SpUtil.setWechat_check(mContext, mUser.getWechat_check());
        SpUtil.setNickname(mContext, mUser.getNickname());
        SpUtil.setIcon(mContext, mUser.getIcon());
        SpUtil.setGender(mContext, mUser.getGender());
        SpUtil.setOrder_count(mContext, String.valueOf(mUser.getOrder_count()));
        SpUtil.setGrade(mContext, String.valueOf(mUser.getGrade()));
        SpUtil.setCommunity(mContext, mUser.getCommunity());
        SpUtil.setCity_name(mContext, mUser.getCity_name());
        SpUtil.setProvince_name(mContext, mUser.getProvince_name());
        SpUtil.setNew_order_count(mContext, String.valueOf(mUser.getNew_order_count()));
        SpUtil.setNot_lf_order_count(mContext, String.valueOf(mUser.getNot_lf_order_count()));
        SpUtil.setLf_order_count(mContext, String.valueOf(mUser.getLf_order_count()));
        SpUtil.setIs_new_sms(mContext, String.valueOf(mUser.getIs_new_sms()));
        LogUtil.showErrorLog(TAG, "用户登录成功，获取用户数据============" + SpUtil.getUserId(mContext));
        mLoginActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(mContext, "登录成功！");
            }
        });
        //推送上线
        Util.initPushEventPushOnline(mContext, TAG);
        //登录成功 数据存储成功 进入主页 并通知登录页关闭
        startActivity(new Intent(mContext, MainActivity.class));
        EventBusUtil.sendEvent(new Event(EC.EventCode.NOTIC_LOGINACTIVITY_FNISHI));
    }
}
