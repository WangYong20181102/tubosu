package com.tbs.tobosutype.fragment;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.CoPresonerMsgActivity;
import com.tbs.tobosutype.activity.NewLoginActivity;
import com.tbs.tobosutype.activity.PresonerMsgActivity;
import com.tbs.tobosutype.base.BaseFragment;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.bean._User;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.CacheManager;
import com.tbs.tobosutype.utils.EventBusUtil;
import com.tbs.tobosutype.utils.SpUtil;
import com.tbs.tobosutype.utils.Util;
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
 * Created by Mr.Lin on 2018/1/9 10:40.
 * 3.7版本新增
 * 手机号码登录
 * 包含微信登录
 */

public class NewLoginFragmentPhone extends BaseFragment {
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
    private NewLoginActivity newLoginActivity;
    private UMShareAPI umShareAPI;

    public NewLoginFragmentPhone() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_login, null);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        initViewEvent();
        return view;
    }

    private void initViewEvent() {
        mGson = new Gson();
        umShareAPI = UMShareAPI.get(mContext);
        newLoginActivity = new NewLoginActivity();
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
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fpl_phone_num_clean, R.id.fpl_get_code, R.id.fpl_phone_login_btn, R.id.fpl_weixin_login_btn})
    public void onViewClickedInLoginFragPhone(View view) {
        switch (view.getId()) {
            case R.id.fpl_phone_num_clean:
                //清除输入的手机号码
                fplPhoneNum.setText("");
                break;
            case R.id.fpl_get_code:
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
                //微信登录按钮
                weiXinLogin();
                break;
        }
    }

    //手机号码登录
    private void HttpPhoneNumLogin(String phoneNum, String msgCode) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("cellphone", phoneNum);
        param.put("verify_code", msgCode);
        param.put("version", AppInfoUtil.getAppVersionName(mContext));
        param.put("city_name", SpUtil.getCity(mContext));
        param.put("chcode", AppInfoUtil.getChannType(mContext));
        param.put("system_type", "1");
        param.put("device_id", SpUtil.getPushRegisterId(mContext));
        OKHttpUtil.post(Constant.SMS_CODE_LOGIN, param, new Callback() {
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
                Log.e(TAG, "获取数据成功============" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    final String msg = jsonObject.optString("msg");
                    if (status.equals("200")) {
                        //获取数成功
                        String data = jsonObject.optString("data");
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
                newLoginActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
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
                Log.e(TAG, "");
                String weiXinUserName = map.get("name");//微信的昵称
                String weiXinImageUrl = map.get("iconurl");//微信的头像
                String weiXinUserId = map.get("unionid");//微信的openid
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
        param.put("device_id", SpUtil.getPushRegisterId(mContext));
        param.put("system_type", "1");
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
                        final String data = jsonObject.optString("data");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                saveUserInfo(data);
                            }
                        });
                    } else if (status.equals("0")) {
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
        AppInfoUtil.setUuid(getActivity(), mUser.getId());//用户的id 本质和id一致
        AppInfoUtil.setTypeid(getActivity(), mUser.getUser_type());//用户的身份标识
        AppInfoUtil.setUserGrade(getActivity(), mUser.getGrade());//用户的会员等级
        AppInfoUtil.setUserCellphone_check(getActivity(), mUser.getCellphone_check());//用户是否绑定手机号码
        AppInfoUtil.setUserOrder_count(getActivity(), mUser.getOrder_count());//用户订单数量

        AppInfoUtil.setUserNewOrderCount(getActivity(), mUser.getNew_order_count() + "");//用户新订单数量
        AppInfoUtil.setUserNotLfOrderCount(getActivity(), mUser.getNot_lf_order_count() + "");//用户未量房数量
        AppInfoUtil.setUserLfOrderCount(getActivity(), mUser.getLf_order_count() + "");//用户量房数量
        AppInfoUtil.setUserIsNewSms(getActivity(), mUser.getIs_new_sms() + "");//用户是否有新消息  1-有  0-无


        CacheManager.setDecorateBudget(getActivity(), mUser.getExpected_cost());//装修日志的花费

        // TODO: 2018/1/10 登录成功并且将数据存储成功之后通知将这个页面销毁
        Log.e(TAG, "发送登录成功的通知====================" + AppInfoUtil.getMark(mContext));
        //推送上线
        initPushEvent();
        //关闭页面
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

    //获取验证码
    private void HttpGetYanZhengCode(String phoneNum) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("cellphone", phoneNum);
        OKHttpUtil.post(Constant.DUANXIN_URL, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                newLoginActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "链接服务器错误", Toast.LENGTH_SHORT).show();
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
                        //获取验证码成功
                        newLoginActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isTimeDown = true;
                                Toast.makeText(mContext, "验证码已发送", Toast.LENGTH_SHORT).show();
                                // TODO: 2018/1/9  开始倒计时 同时按钮的颜色开始变化
                                fplGetCode.setTextColor(Color.parseColor("#ffffff"));
                                fplGetCode.setTextSize(11);
                                GradientDrawable gradientDrawable = new GradientDrawable();
                                gradientDrawable.setColor(Color.parseColor("#d9d9d9"));
                                gradientDrawable.setCornerRadius(2);
                                fplGetCode.setBackground(gradientDrawable);
                                startCount();
                            }
                        });
                    } else if (status.equals("203")) {
                        //重复获取验证码
                        newLoginActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (status.equals("0")) {
                        //验证手机号码错误
                        newLoginActivity.runOnUiThread(new Runnable() {
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
                        newLoginActivity.runOnUiThread(new MyTask());
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
                    GradientDrawable gradientDrawable = new GradientDrawable();
                    gradientDrawable.setColor(Color.parseColor("#ffffff"));
                    gradientDrawable.setCornerRadius(2);
                    gradientDrawable.setStroke(1, Color.parseColor("#ff6c20"));
                    fplGetCode.setBackground(gradientDrawable);
                    fplGetCode.setText("重新获取");
                    fplGetCode.setEnabled(true);
                    fplGetCode.setClickable(true);
                    count = 60;
                }
            }
        }
    }
}
