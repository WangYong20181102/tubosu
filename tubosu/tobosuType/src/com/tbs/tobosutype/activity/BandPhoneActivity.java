package com.tbs.tobosutype.activity;

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

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.base.*;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.EventBusUtil;
import com.tbs.tobosutype.utils.Util;

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
public class BandPhoneActivity extends com.tbs.tobosutype.base.BaseActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_phone);
        ButterKnife.bind(this);
        mContext = this;
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
        param.put("uid", AppInfoUtil.getUserid(mContext));
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
