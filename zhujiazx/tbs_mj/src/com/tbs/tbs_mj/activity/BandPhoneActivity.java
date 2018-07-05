package com.tbs.tbs_mj.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.base.*;
import com.tbs.tbs_mj.bean.EC;
import com.tbs.tbs_mj.bean.Event;
import com.tbs.tbs_mj.bean._User;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.global.OKHttpUtil;
import com.tbs.tbs_mj.utils.AppInfoUtil;
import com.tbs.tbs_mj.utils.CacheManager;
import com.tbs.tbs_mj.utils.EventBusUtil;
import com.tbs.tbs_mj.utils.SpUtil;
import com.tbs.tbs_mj.utils.Util;

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
 * 用户绑定手机号
 * 3.7版本新增
 */
public class BandPhoneActivity extends com.tbs.tbs_mj.base.BaseActivity {
    @BindView(R.id.band_phone_num_back)
    RelativeLayout bandPhoneNumBack;
    @BindView(R.id.banner_dever)
    View bannerDever;
    @BindView(R.id.band_phone_num)
    EditText bandPhoneNum;
    @BindView(R.id.band_phone_num_clean)
    ImageView bandPhoneNumClean;
    @BindView(R.id.band_code_num)
    EditText bandCodeNum;
    @BindView(R.id.band_get_code)
    TextView bandGetCode;
    @BindView(R.id.band_ok_btn)
    TextView bandOkBtn;
    private Context mContext;
    private String TAG = "BandPhoneActivity";
    private boolean isTimeDown = false;
    private int count = 60;
    private Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_phone);
        ButterKnife.bind(this);
        mContext = this;
        mGson = new Gson();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @OnClick({R.id.band_phone_num_back, R.id.band_phone_num_clean, R.id.band_get_code, R.id.band_ok_btn})
    public void onViewClickedInBandPhoneActivity(View view) {
        switch (view.getId()) {
            case R.id.band_phone_num_back:
                finish();
                break;
            case R.id.band_phone_num_clean:
                //清除输入的数据
                bandPhoneNum.setText("");
                break;
            case R.id.band_get_code:
                //获取验证码
                if (!TextUtils.isEmpty(bandPhoneNum.getText().toString())) {
                    if (bandPhoneNum.getText().toString().length() == 11) {
                        //进行发送验证码
                        HttpGetYanZhengCode(bandPhoneNum.getText().toString());
                    } else {
                        Toast.makeText(mContext, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "请输入您的手机号", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.band_ok_btn:
                //确定绑定手机
                if (!TextUtils.isEmpty(bandPhoneNum.getText().toString())) {
                    if (!TextUtils.isEmpty(bandCodeNum.getText().toString())) {
                        HttpBandPhoneNum(bandPhoneNum.getText().toString(), bandCodeNum.getText().toString());
                    } else {
                        Toast.makeText(mContext, "请输入验证码", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "请输入您的手机号", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //绑定手机号
    private void HttpBandPhoneNum(String phoneNum, String code) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        //这个地方要用到不同的uid
        if (AppInfoUtil.getUserid(mContext).isEmpty() || AppInfoUtil.getUserid(mContext) == null) {
            //绑定手机号码
            param.put("uid", AppInfoUtil.getUserBindId(mContext));
        } else {
            param.put("uid", AppInfoUtil.getUserid(mContext));
        }
        param.put("cellphone", phoneNum);
        param.put("verify_code", code);
        OKHttpUtil.post(Constant.BIND_CELLPHONE, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "链接服务器失败~", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "数据获取成功============" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    final String msg = jsonObject.optString("msg");
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        //绑定手机号码成功 
                        String data = jsonObject.optString("data");
                        if (data != null) {
                            //存储用户的信息 因为有些新用户未绑定手机所有登录的时候会进入这个页面
                            saveUserInfo(data);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                EventBusUtil.sendEvent(new Event(EC.EventCode.BAND_PHONE_SUCCESS));
                                Toast.makeText(mContext, "绑定成功", Toast.LENGTH_SHORT).show();
                                AppInfoUtil.setUserCellphone_check(mContext, "1");
                                finish();
                            }
                        });

                    } else if (status.equals("0")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "绑定失败", Toast.LENGTH_SHORT).show();
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
        AppInfoUtil.setUserNickname(mContext, mUser.getNickname());//昵称
        AppInfoUtil.setId(mContext, mUser.getId());//用户的id
        AppInfoUtil.setUserIcon(mContext, mUser.getIcon());//用户的头像
        AppInfoUtil.setMark(mContext, mUser.getUser_type());//用户的身份标识
        AppInfoUtil.setUserid(mContext, mUser.getUid());//用户的id
        AppInfoUtil.setToken(mContext, mUser.getToken());//token
        AppInfoUtil.setUserCity(mContext, mUser.getCity_name());//用户所在的城市
        AppInfoUtil.setUserProvince(mContext, mUser.getProvince_name());//用户所在的省份
        AppInfoUtil.setUuid(mContext, mUser.getId());//用户的id
        AppInfoUtil.setTypeid(mContext, mUser.getUser_type());//用户的身份标识
        AppInfoUtil.setUserGrade(mContext, mUser.getGrade());//用户的会员等级
        AppInfoUtil.setUserCellphone_check(mContext, mUser.getCellphone_check());//用户是否绑定手机号码
        AppInfoUtil.setUserOrder_count(mContext, mUser.getOrder_count());//用户订单数量

        AppInfoUtil.setUserNewOrderCount(mContext, mUser.getNew_order_count() + "");//用户新订单数量
        AppInfoUtil.setUserNotLfOrderCount(mContext, mUser.getNot_lf_order_count() + "");//用户未量房数量
        AppInfoUtil.setUserLfOrderCount(mContext, mUser.getLf_order_count() + "");//用户量房数量
        AppInfoUtil.setUserIsNewSms(mContext, mUser.getIs_new_sms() + "");//用户是否有新消息  1-有  0-无

        Log.e(TAG, "获取用户的订单数量=========mUser.getOrder_count()=======" + mUser.getOrder_count());
        Log.e(TAG, "获取用户的mUser.getToken()=========mUser.getToken()=======" + mUser.getToken());
        Log.e(TAG, "获取用户的订单数量=========AppInfoUtil.getUserOrder_count=======" + AppInfoUtil.getUserOrder_count(mContext));
        CacheManager.setDecorateBudget(mContext, mUser.getExpected_cost());//装修日志的花费
        // TODO: 2018/1/10 登录成功并且将数据存储成功之后通知将这个页面销毁
        EventBusUtil.sendEvent(new Event(EC.EventCode.CLOSE_NEW_LOGIN_ACTIVITY));
    }

    //获取验证码
    private void HttpGetYanZhengCode(String phoneNum) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("cellphone", phoneNum);
        OKHttpUtil.post(Constant.DUANXIN_URL, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isTimeDown = true;
                                Toast.makeText(mContext, "验证码已发送", Toast.LENGTH_SHORT).show();
                                // TODO: 2018/1/9  开始倒计时 同时按钮的颜色开始变化
                                bandGetCode.setTextColor(Color.parseColor("#ffffff"));
                                bandGetCode.setTextSize(11);
                                GradientDrawable gradientDrawable = new GradientDrawable();
                                gradientDrawable.setColor(Color.parseColor("#d9d9d9"));
                                gradientDrawable.setCornerRadius(2);
                                bandGetCode.setBackground(gradientDrawable);
                                startCount();
                            }
                        });
                    } else if (status.equals("203")) {
                        //重复获取验证码
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (status.equals("0")) {
                        //验证手机号码错误
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
                while (isTimeDown) {
                    try {
                        runOnUiThread(new MyTask());
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
            if (count > 0 && bandGetCode != null) {
                bandGetCode.setText(count + "s后重新获取");
                bandGetCode.setClickable(false);
                bandGetCode.setEnabled(false);
            } else {
                isTimeDown = false;
                if (bandGetCode != null) {
                    bandGetCode.setTextColor(Color.parseColor("#ff6c20"));
                    bandGetCode.setTextSize(12);
                    GradientDrawable gradientDrawable = new GradientDrawable();
                    gradientDrawable.setColor(Color.parseColor("#ffffff"));
                    gradientDrawable.setCornerRadius(2);
                    gradientDrawable.setStroke(1, Color.parseColor("#ff6c20"));
                    bandGetCode.setBackground(gradientDrawable);
                    bandGetCode.setText("重新获取");
                    bandGetCode.setEnabled(true);
                    bandGetCode.setClickable(true);
                    count = 60;
                }
            }
        }
    }
}
