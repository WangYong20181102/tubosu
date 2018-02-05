package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.Intent;
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
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
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
 * 手机验证
 */
public class CheckPhoneNumActivity extends AppCompatActivity {
    @BindView(R.id.banner_dever)
    View bannerDever;
    @BindView(R.id.check_phone_num)
    EditText checkPhoneNum;
    @BindView(R.id.check_phone_num_clean)
    ImageView checkPhoneNumClean;
    @BindView(R.id.check_code_num)
    EditText checkCodeNum;
    @BindView(R.id.check_get_code)
    TextView checkGetCode;
    @BindView(R.id.check_next_step_btn)
    TextView checkNextStepBtn;
    @BindView(R.id.check_phone_num_back)
    RelativeLayout checkPhoneNumBack;
    private String TAG = "CheckPhoneNumActivity";
    private Context mContext;
    private boolean isTimeDown = false;
    private int count = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_phone_num);
        ButterKnife.bind(this);
        mContext = this;
    }

    @OnClick({R.id.check_phone_num_clean, R.id.check_get_code, R.id.check_next_step_btn, R.id.check_phone_num_back})
    public void onViewClickedInCheckPhoneNumActivity(View view) {
        switch (view.getId()) {
            case R.id.check_phone_num_back:
                finish();
                break;
            case R.id.check_phone_num_clean:
                //清除输入的密码
                checkPhoneNum.setText("");
                break;
            case R.id.check_get_code:
                //获取验证码
                if (!TextUtils.isEmpty(checkPhoneNum.getText().toString())) {
                    if (checkPhoneNum.getText().toString().length() == 11) {
                        //进行发送验证码
                        HttpCheckThePhoneIsRegiest(checkPhoneNum.getText().toString());
                    } else {
                        Toast.makeText(mContext, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "请输入您的手机号", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.check_next_step_btn:
                //下一步 （在此之前校验手机号和验证码的正确性）
                if (!TextUtils.isEmpty(checkPhoneNum.getText().toString())) {
                    if (!TextUtils.isEmpty(checkCodeNum.getText().toString())) {
                        if (checkCodeNum.getText().toString().length() == 6) {
                            HttpCheckPhoneNumAndCode(checkPhoneNum.getText().toString(), checkCodeNum.getText().toString());
                        } else {
                            Toast.makeText(mContext, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, "请输入验证码", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "请输入您的手机号", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //校验手机号码是否注册过
    private void HttpCheckThePhoneIsRegiest(String phoneNum) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("cellphone", phoneNum);
        OKHttpUtil.post(Constant.IS_EXIST_USER, param, new Callback() {
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
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    final String msg = jsonObject.optString("msg");
                    if (status.equals("200")) {
                        HttpGetYanZhengCode(checkPhoneNum.getText().toString());
                    } else {
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

    //校验手机号码和验证码是否正确
    private void HttpCheckPhoneNumAndCode(final String phoneNum, final String code) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("cellphone", phoneNum);
        param.put("verify_code", code);
        OKHttpUtil.post(Constant.VERIFY_SMS_CODE, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "链接服务器失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接服务器成功======" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    final String msg = jsonObject.optString("msg");
                    if (status.equals("200")) {
                        //校验成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //校验成功进入修改密码页面 将手机号码 以及验证码传入
                                Intent intent = new Intent(mContext, ChangePassWordActivity.class);
                                intent.putExtra("mPhoneNum", phoneNum);
                                intent.putExtra("mCode", code);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else if (status.equals("203")) {
                        //60秒内重复获取
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
                                checkGetCode.setTextColor(Color.parseColor("#ffffff"));
                                checkGetCode.setTextSize(11);
                                GradientDrawable gradientDrawable = new GradientDrawable();
                                gradientDrawable.setColor(Color.parseColor("#d9d9d9"));
                                gradientDrawable.setCornerRadius(2);
                                checkGetCode.setBackground(gradientDrawable);
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
            if (count > 0 && checkGetCode != null) {
                checkGetCode.setText(count + "s后重新获取");
                checkGetCode.setClickable(false);
                checkGetCode.setEnabled(false);
            } else {
                isTimeDown = false;
                if (checkGetCode != null) {
                    checkGetCode.setTextColor(Color.parseColor("#ff6c20"));
                    checkGetCode.setTextSize(12);
                    GradientDrawable gradientDrawable = new GradientDrawable();
                    gradientDrawable.setColor(Color.parseColor("#ffffff"));
                    gradientDrawable.setCornerRadius(2);
                    gradientDrawable.setStroke(1, Color.parseColor("#ff6c20"));
                    checkGetCode.setBackground(gradientDrawable);
                    checkGetCode.setText("重新获取");
                    checkGetCode.setEnabled(true);
                    checkGetCode.setClickable(true);
                    count = 60;
                }
            }
        }
    }
}
