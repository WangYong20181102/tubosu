package com.tbs.tbs_mj.fragment;

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
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.activity.CheckPhoneNumActivity;
import com.tbs.tbs_mj.activity.NewLoginActivity;
import com.tbs.tbs_mj.base.BaseFragment;
import com.tbs.tbs_mj.bean.EC;
import com.tbs.tbs_mj.bean.Event;
import com.tbs.tbs_mj.bean._User;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.global.OKHttpUtil;
import com.tbs.tbs_mj.utils.AppInfoUtil;
import com.tbs.tbs_mj.utils.CacheManager;
import com.tbs.tbs_mj.utils.EventBusUtil;
import com.tbs.tbs_mj.utils.MD5Util;
import com.tbs.tbs_mj.utils.SpUtil;
import com.tbs.tbs_mj.utils.Util;
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
 * Created by Mr.Lin on 2018/1/9 10:39.
 * 3.7版本新增
 * 包含账号登录以及微信号登录
 * 账号登录
 */

public class NewLoginFragmentAccount extends BaseFragment {
    @BindView(R.id.fal_account)
    EditText falAccount;
    @BindView(R.id.fal_clean_account)
    ImageView falCleanAccount;
    @BindView(R.id.fal_password)
    EditText falPassword;
    @BindView(R.id.fal_forget_password)
    TextView falForgetPassword;
    @BindView(R.id.fal_login_btn)
    TextView falLoginBtn;
    @BindView(R.id.fal_weixin_login_btn)
    RelativeLayout falWeixinLoginBtn;
    Unbinder unbinder;
    @BindView(R.id.fal_clean_password)
    ImageView falCleanPassword;
    private String TAG = "NewLoginFragmentAccount";
    private Context mContext;
    private Gson mGson;
    private NewLoginActivity newLoginActivity;
    private UMShareAPI umShareAPI;

    public NewLoginFragmentAccount() {

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

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    private void initViewEvent() {
        mGson = new Gson();
        umShareAPI = UMShareAPI.get(mContext);
        newLoginActivity = new NewLoginActivity();
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

    @OnClick({R.id.fal_clean_account, R.id.fal_forget_password,
            R.id.fal_login_btn, R.id.fal_weixin_login_btn, R.id.fal_clean_password})
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
                //忘记密码  计入修改密码
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
                //微信登录
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
        param.put("version", AppInfoUtil.getAppVersionName(mContext));
        param.put("client_type", "1");
        param.put("device_id", SpUtil.getPushRegisterId(mContext));
        OKHttpUtil.post(Constant.ACCOUNT_LOGIN, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                newLoginActivity.runOnUiThread(new Runnable() {
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
                        //存储Md5密码
                        AppInfoUtil.setUserMd5PassWord(mContext, Md5Password);
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
                String weiXinUserName = map.get("name");//微信的昵称
                String weiXinImageUrl = map.get("iconurl");//微信的头像
                String weiXinUserId = map.get("unionid");//微信的openid  unionid打通用户在同一平台开发
                Log.e("微信的配置信息", "==================" + map.toString());
                HttpWeixinLogin(weiXinUserId, weiXinUserName, weiXinImageUrl);
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
    private void HttpWeixinLogin(String openid, String nickname, String icon) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("openid", openid);
        param.put("nickname", nickname);
        param.put("icon", icon);
        param.put("version", AppInfoUtil.getAppVersionName(mContext));
        param.put("city_name", SpUtil.getCity(mContext));
        param.put("chcode", AppInfoUtil.getChannType(mContext));
        param.put("system_type", "1");
        param.put("device_id", SpUtil.getPushRegisterId(mContext));
        Log.e(TAG, "微信登录的参数=============openid====" + openid + "===nickname===" + nickname + "===version===" + AppInfoUtil.getAppVersionName(mContext) + "==city_name==" + SpUtil.getCity(mContext) + "==icon==" + icon);
        OKHttpUtil.post(Constant.WECHAT_LOGIN, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                newLoginActivity.runOnUiThread(new Runnable() {
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
                        String data = jsonObject.optString("data");
                        saveUserInfo(data);
                    } else if (status.equals("0")) {
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

    //存储用户的信息  作用于微信  手机号登录
    private void saveUserInfo(String data) {
        _User mUser = mGson.fromJson(data, _User.class);
        AppInfoUtil.setUserNickname(getActivity(), mUser.getNickname());//昵称
        AppInfoUtil.setId(getActivity(), mUser.getId());//用户的id
        AppInfoUtil.setUserIcon(getActivity(), mUser.getIcon());//用户的头像
        AppInfoUtil.setMark(getActivity(), mUser.getUser_type());//用户的身份标识
        AppInfoUtil.setUserid(getActivity(), mUser.getUid());//用户的id
        AppInfoUtil.setToken(getActivity(), mUser.getToken());//token
        AppInfoUtil.setUserCity(getActivity(), mUser.getCity_name());//用户所在的城市
        AppInfoUtil.setUserProvince(getActivity(), mUser.getProvince_name());//用户所在的省份
        AppInfoUtil.setUuid(getActivity(), mUser.getId());//用户的id
        AppInfoUtil.setTypeid(getActivity(), mUser.getUser_type());//用户的身份标识
        AppInfoUtil.setUserGrade(getActivity(), mUser.getGrade());//用户的会员等级
        AppInfoUtil.setUserCellphone_check(getActivity(), mUser.getCellphone_check());//用户是否绑定手机号码
        AppInfoUtil.setUserOrder_count(getActivity(), mUser.getOrder_count());//用户订单数量

        AppInfoUtil.setUserNewOrderCount(getActivity(), mUser.getNew_order_count() + "");//用户新订单数量
        AppInfoUtil.setUserNotLfOrderCount(getActivity(), mUser.getNot_lf_order_count() + "");//用户未量房数量
        AppInfoUtil.setUserLfOrderCount(getActivity(), mUser.getLf_order_count() + "");//用户量房数量
        AppInfoUtil.setUserIsNewSms(getActivity(), mUser.getIs_new_sms() + "");//用户是否有新消息  1-有  0-无

        Log.e(TAG, "获取用户的订单数量=========mUser.getOrder_count()=======" + mUser.getOrder_count());
        Log.e(TAG, "获取用户的订单数量=========AppInfoUtil.getUserOrder_count=======" + AppInfoUtil.getUserOrder_count(mContext));
        CacheManager.setDecorateBudget(getActivity(), mUser.getExpected_cost());//装修日志的花费

        //推送上线
        initPushEvent();
        // TODO: 2018/1/10 登录成功并且将数据存储成功之后通知将这个页面销毁
        EventBusUtil.sendEvent(new Event(EC.EventCode.CLOSE_NEW_LOGIN_ACTIVITY));
    }


    //定点推送相关
    private void initPushEvent() {
        if (!TextUtils.isEmpty(AppInfoUtil.getUserid(mContext))) {
            //用户已经登录
            HashMap<String, Object> param = new HashMap<>();
            param.put("user_id", AppInfoUtil.getId(mContext));
            param.put("user_type", AppInfoUtil.getTypeid(mContext));
            param.put("system_type", "1");
            param.put("app_type", "1");
            param.put("device_id", SpUtil.getPushRegisterId(mContext));
            OKHttpUtil.post(Constant.FLUSH_SMS_PUSH, param, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "链接失败===============" + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = new String(response.body().string());
                    Log.e(TAG, "推送相关数据链接成功===========" + json);
                }
            });
        }
    }
}
