package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.CustomWaitDialog;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.HintInput;
import com.tbs.tobosutype.utils.Util;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 免费预约页面  走android端发单接口
 *
 * @author dec
 */
public class FreeYuyueActivity extends Activity implements OnClickListener {
    private Context mContext;
    private ImageView free_yuyue_back;
    private EditText free_yuyue_budget;
    private EditText free_yuyue_input_area;
    private EditText free_yuyue_inputphone;
    private EditText free_yuyue_input_obtainphone;
    private Button free_yuyue_submit;
    private RelativeLayout rel_ativeLayout1;

    /**
     * 获取保存本地的城市参数
     */
    private SharedPreferences getCitySharePre;

    private HashMap<String, String> pubOrderParams;
    private String phone;
    private String cityid;
    private String userid;
    private String budget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//		AppInfoUtil.setActivityTheme(this, R.color.whole_color_theme);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_freeyuyue);
        mContext = FreeYuyueActivity.this;
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        free_yuyue_back = (ImageView) findViewById(R.id.free_yuyue_back);
        free_yuyue_budget = (EditText) findViewById(R.id.free_yuyue_budget);
        free_yuyue_input_area = (EditText) findViewById(R.id.free_yuyue_input_area);
        free_yuyue_inputphone = (EditText) findViewById(R.id.free_yuyue_inputphone);
        free_yuyue_input_obtainphone = (EditText) findViewById(R.id.free_yuyue_input_obtainphone);
        free_yuyue_submit = (Button) findViewById(R.id.free_yuyue_submit);
        rel_ativeLayout1 = (RelativeLayout) findViewById(R.id.rel_ativeLayout1);
        rel_ativeLayout1.setBackgroundColor(Color.parseColor("#ff882e"));
    }

    private void initData() {
        pubOrderParams = AppInfoUtil.getPublicHashMapParams(getApplicationContext());
    }

    private void initEvent() {
        free_yuyue_submit.setOnClickListener(this);
        free_yuyue_back.setOnClickListener(this);
        new HintInput(11, free_yuyue_inputphone, FreeYuyueActivity.this);
    }


    private CustomWaitDialog waitDialog;

    private void showLoadingView() {
        waitDialog = new CustomWaitDialog(mContext);
        waitDialog.show();
    }

    private void hideLoadingView() {
        if (waitDialog != null) {
            waitDialog.dismiss();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.free_yuyue_back:
                finish();
                break;

            case R.id.free_yuyue_submit:

                getCitySharePre = this.getSharedPreferences("Save_City_Info", MODE_PRIVATE);
                String city = getCitySharePre.getString("save_city_now", "");
                Log.d("FreeYuyueActivity", "发单城市是" + city);
                phone = free_yuyue_inputphone.getText().toString();
                String area = free_yuyue_input_area.getText().toString();
                budget = free_yuyue_budget.getText().toString();
                if (area.length() >= 6) {
                    Toast.makeText(getApplicationContext(), "建筑面积输入有误！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(area)) {
                    Toast.makeText(getApplicationContext(), "您的建筑面积还没有填写！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(budget)) {
                    Toast.makeText(getApplicationContext(), "您的装修预算还没有填写！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (budget.length() >= 5) {
                    Toast.makeText(getApplicationContext(), "装修预算输入有误！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Long.parseLong(budget) - 0 == 0) {
                    Toast.makeText(getApplicationContext(), "您的装修预算不能为0！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(getApplicationContext(), "您的手机号码还没有填写！", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (Constant.checkNetwork(mContext)) {
                    showLoadingView();
                    if (!TextUtils.isEmpty(free_yuyue_input_obtainphone.getText() + "")) {
                        pubOrderParams.put("housearea", area);
                        pubOrderParams.put("orderprice", budget);
                        pubOrderParams.put("cellphone", phone);
                        pubOrderParams.put("city", city);//深圳
                        System.out.println("FreeYuYueActivity 免费预约的城市是" + city);
                        pubOrderParams.put("urlhistory", Constant.PIPE);
                        // 发单入口
                        pubOrderParams.put("comeurl", Constant.PIPE);
                        if (!TextUtils.isEmpty(userid)) {
                            pubOrderParams.put("userid", userid);
                        } else {
                            pubOrderParams.put("userid", "0");
                        }
                        pubOrderParams.put("source", "894");
                        requestPubOrder();
                    }
                } else {
                    Constant.toastNetOut(mContext);
                }
                break;
        }

    }

    /***
     * 发单接口请求
     */
    private void requestPubOrder() {
        OKHttpUtil.post(Constant.PUB_ORDERS, pubOrderParams, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Util.setToast(mContext, "请稍后再试~");
                        hideLoadingView();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("error_code") == 0) {
                                Intent intent = new Intent(FreeYuyueActivity.this, ApplyforSuccessActivity.class);
                                intent.putExtra("phone", phone);
                                startActivity(intent);
                                finish();
                            } else {
                                Util.setToast(mContext, jsonObject.getString("msg"));
                            }

                            hideLoadingView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}