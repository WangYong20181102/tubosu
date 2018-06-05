package com.tbs.tbsbusiness.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import com.tbs.tbsbusiness.activity.LoginActivity;
import com.tbs.tbsbusiness.activity.MainActivity;
import com.tbs.tbsbusiness.base.BaseFragment;
import com.tbs.tbsbusiness.bean.EC;
import com.tbs.tbsbusiness.bean.Event;
import com.tbs.tbsbusiness.bean._User;
import com.tbs.tbsbusiness.config.Constant;
import com.tbs.tbsbusiness.util.EventBusUtil;
import com.tbs.tbsbusiness.util.OKHttpUtil;
import com.tbs.tbsbusiness.util.SpUtil;
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
 * Created by Mr.Lin on 2018/6/4 11:43.
 * 手机号码登录
 */
public class LoginFragmentPhone extends BaseFragment {

    @BindView(R.id.fpl_phone_num)
    EditText fplPhoneNum;
    @BindView(R.id.fpl_phone_num_clean)
    ImageView fplPhoneNumClean;
    @BindView(R.id.fpl_code_num)
    EditText fplCodeNum;
    @BindView(R.id.fpl_get_code)
    TextView fplGetCode;
    @BindView(R.id.fpl_phone_login_btn)
    TextView fplPhoneLoginBtn;
    @BindView(R.id.fpl_weixin_login_btn)
    RelativeLayout fplWeixinLoginBtn;
    Unbinder unbinder;
    private String TAG = "NewLoginFragmentPhone";
    private Context mContext;
    private Gson mGson;
    private int count = 60;
    private boolean isTimeDown = false;//是否需要倒计时
    private LoginActivity mLoginActivity;
    private UMShareAPI umShareAPI;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_login, null);
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
        mLoginActivity = new LoginActivity();
        fplPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    fplPhoneNumClean.setVisibility(View.GONE);
                } else {
                    fplPhoneNumClean.setVisibility(View.VISIBLE);
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

    @OnClick({R.id.fpl_phone_num_clean, R.id.fpl_get_code,
            R.id.fpl_phone_login_btn, R.id.fpl_weixin_login_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fpl_phone_num_clean:
                //清除输入的手机号码
                fplPhoneNum.setText("");
                break;
            case R.id.fpl_get_code:
                //获取验证码
                //获取验证码
                if (!TextUtils.isEmpty(fplPhoneNum.getText().toString())) {
                    if (fplPhoneNum.getText().toString().length() == 11) {
                        //进行发送验证码
                        HttpGetYanZhengCode(fplPhoneNum.getText().toString());
                    } else {
                        Toast.makeText(mContext, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(mContext, "请输入您的手机号", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.fpl_phone_login_btn:
                //手机号登录按钮
                if (!TextUtils.isEmpty(fplPhoneNum.getText().toString()) && (fplPhoneNum.getText().toString().length() == 11)) {
                    if (!TextUtils.isEmpty(fplCodeNum.getText().toString()) && (fplCodeNum.getText().toString().length() == 6)) {
                        //手机号码以及验证码的格式均正确 进行手机号码登录
                        HttpPhoneNumLogin(fplPhoneNum.getText().toString(), fplCodeNum.getText().toString());
                    } else {
                        Toast.makeText(mContext, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.fpl_weixin_login_btn:
                weiXinLogin();
                break;
        }
    }

    // TODO: 2018/6/4 微信登录
    private void weiXinLogin() {
        umShareAPI.getPlatformInfo(getActivity(), SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//                String weiXinUserName = map.get("name");//微信的昵称
//                String weiXinImageUrl = map.get("iconurl");//微信的头像
                String weiXinUserId = map.get("unionid");//  unionid打通用户在同一平台开发
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
    private void HttpWeixinLogin(String unionid) {
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


    // TODO: 2018/6/4  手机号码登录  有关参数以及逻辑待完善
    private void HttpPhoneNumLogin(String phoneNum, String msgCode) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("cellphone", phoneNum);
        param.put("verify_code", msgCode);
        param.put("client_type", "1");
        param.put("device_id", SpUtil.getPushRegisterId(mContext));
        OKHttpUtil.post(Constant.SMS_CODE_LOGIN, param, new Callback() {
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
                Log.e(TAG, "获取数据成功============" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    final String msg = jsonObject.optString("msg");
                    if (status.equals("200")) {
                        //获取数成功
                        String data = jsonObject.optString("data");
                        // TODO: 2018/6/4  存储用户的信息
                        saveUserInfo(data);
                    } else {
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
                mLoginActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
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
        SpUtil.setNickname(mContext, mUser.getNickname());
        SpUtil.setIcon(mContext, mUser.getIcon());
        SpUtil.setOrder_count(mContext, String.valueOf(mUser.getOrder_count()));
        SpUtil.setGrade(mContext, String.valueOf(mUser.getGrade()));
        SpUtil.setCity_name(mContext, mUser.getCity_name());
        SpUtil.setProvince_name(mContext, mUser.getProvince_name());
        SpUtil.setNew_order_count(mContext, String.valueOf(mUser.getNew_order_count()));
        SpUtil.setNot_lf_order_count(mContext, String.valueOf(mUser.getNot_lf_order_count()));
        SpUtil.setLf_order_count(mContext, String.valueOf(mUser.getLf_order_count()));
        SpUtil.setIs_new_sms(mContext, String.valueOf(mUser.getIs_new_sms()));
        Log.e(TAG, "用户信息存储==============" + SpUtil.getUserId(mContext));
        //推送上线
        Util.initPushEventPushOnline(mContext, TAG);
        //登录成功 数据存储成功 进入主页 并通知登录页关闭
        startActivity(new Intent(mContext, MainActivity.class));
        EventBusUtil.sendEvent(new Event(EC.EventCode.NOTIC_LOGINACTIVITY_FNISHI));

    }


    //获取验证码
    private void HttpGetYanZhengCode(String phoneNum) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("cellphone", phoneNum);
        OKHttpUtil.post(Constant.DUANXIN_URL, param, new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mLoginActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "链接失败=================" + e.getMessage());
                        Toast.makeText(mContext, "链接服务器错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String json = new String(response.body().string());
                Log.e(TAG, "发送验证码请求接口返回数据=====" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    final String msg = jsonObject.getString("msg");
                    if (status.equals("200")) {
                        //获取验证码成功
                        mLoginActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isTimeDown = true;
                                Toast.makeText(mContext, "验证码已发送", Toast.LENGTH_SHORT).show();
                                // TODO: 2018/1/9  开始倒计时 同时按钮的颜色开始变化
                                fplGetCode.setTextColor(Color.parseColor("#ffffff"));
                                fplGetCode.setTextSize(11);
                                fplGetCode.setBackgroundResource(R.drawable.shape_get_code_affter);
                                startCount();
                            }
                        });
                    } else if (status.equals("203")) {
                        //重复获取验证码
                        mLoginActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (status.equals("0")) {
                        //验证手机号码错误
                        mLoginActivity.runOnUiThread(new Runnable() {
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
                while (isTimeDown) {
                    try {
                        mLoginActivity.runOnUiThread(new MyTask());
                        Thread.sleep(1000);
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
            if (count > 0 && fplGetCode != null) {
                fplGetCode.setText(count + "s后重新获取");
                fplGetCode.setClickable(false);
                fplGetCode.setEnabled(false);
            } else {
                isTimeDown = false;
                if (fplGetCode != null) {
                    fplGetCode.setTextColor(Color.parseColor("#ff6c20"));
                    fplGetCode.setTextSize(12);
                    fplGetCode.setBackgroundResource(R.drawable.shape_login_get_code);
                    fplGetCode.setText("重新获取");
                    fplGetCode.setEnabled(true);
                    fplGetCode.setClickable(true);
                    count = 60;
                }
            }
        }
    }
}
