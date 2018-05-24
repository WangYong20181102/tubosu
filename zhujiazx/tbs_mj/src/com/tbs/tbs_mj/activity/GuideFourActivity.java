package com.tbs.tbs_mj.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.base.BaseActivity;
import com.tbs.tbs_mj.bean.EC;
import com.tbs.tbs_mj.bean.Event;
import com.tbs.tbs_mj.customview.CustomWaitDialog;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.global.OKHttpUtil;
import com.tbs.tbs_mj.utils.AppInfoUtil;
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

public class GuideFourActivity extends BaseActivity {
    @BindView(R.id.guide_four_phone_num_tv)
    EditText guideFourPhoneNumTv;
    @BindView(R.id.guide_four_back_rl)
    RelativeLayout guideFourBackRl;
    @BindView(R.id.guide_four_close_rl)
    RelativeLayout guideFourCloseRl;
    @BindView(R.id.guide_four_ok_tv)
    TextView guideFourOkTv;
    private String TAG = "GuideFourActivity";
    private Context mContext;
    private CustomWaitDialog waitDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_four);
        ButterKnife.bind(this);
        mContext = this;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.FINISH_GUIDE_ACTIVITY:
                finish();
                break;
        }
    }

    private void showLoadingView() {
        waitDialog = new CustomWaitDialog(mContext);
        waitDialog.show();
    }

    private void hideLoadingView() {
        if (waitDialog != null) {
            waitDialog.dismiss();
        }
    }

    @OnClick({R.id.guide_four_back_rl, R.id.guide_four_close_rl, R.id.guide_four_ok_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.guide_four_back_rl:
                finish();
                break;
            case R.id.guide_four_close_rl:
                gotoMianActivity();
                EventBusUtil.sendEvent(new Event(EC.EventCode.FINISH_GUIDE_ACTIVITY));
                break;
            case R.id.guide_four_ok_tv:
                //请求发单
                if (!TextUtils.isEmpty(guideFourPhoneNumTv.getText().toString())
                        && guideFourPhoneNumTv.getText().toString().length() == 11) {
                    HttpGetOrder(guideFourPhoneNumTv.getText().toString());
                } else {
                    Toast.makeText(mContext, "请输入正确的手机号码~", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void HttpGetOrder(String phoneNum) {
        showLoadingView();
        HashMap<String, Object> param = new HashMap<>();
        param.put("cellphone", phoneNum);
        param.put("device", "android");
        param.put("source", "1222");
        param.put("device_id", Util.getDeviceID());
        if(TextUtils.isEmpty(AppInfoUtil.getCityName(mContext))){
            param.put("city", SpUtil.getCity(mContext));
        }else {
            param.put("city", AppInfoUtil.getCityName(mContext));
        }
        param.put("version", AppInfoUtil.getAppVersionName(mContext));
        param.put("urlhistory", Constant.PIPE); // 渠道代码
        param.put("comeurl", Constant.PIPE); //订单发布页面
        OKHttpUtil.post(Constant.ALL_ORDER_URL, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "领取失败，请稍后再试~", Toast.LENGTH_SHORT).show();
                        hideLoadingView();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.getInt("error_code") == 0) {
                        //获取发单成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideLoadingView();
                                Util.setToast(mContext, "领取成功 我们会尽快以0574开头座机联系您");
                                gotoMianActivity();
                                EventBusUtil.sendEvent(new Event(EC.EventCode.FINISH_GUIDE_ACTIVITY));
                            }
                        });
                    } else if (jsonObject.getInt("error_code") == 250) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideLoadingView();
                                Util.setToast(mContext, "您操作太频繁 请稍后再试!");
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Util.setToast(mContext, "领取失败 请稍后再试!");
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void gotoMianActivity() {
        startActivity(new Intent(mContext, MainActivity.class));
    }
}
