package com.tbs.tbs_mj.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.customview.CustomWaitDialog;
import com.tbs.tbs_mj.customview.GetVerificationPopupwindow;
import com.tbs.tbs_mj.customview.MyChatView;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.global.MyApplication;
import com.tbs.tbs_mj.global.OKHttpUtil;
import com.tbs.tbs_mj.utils.AppInfoUtil;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class FreeDesignPrice extends Activity {
    private String TAG = "FreeDesignPrice";
    private Intent intent;
    private Context mContext;
    private int count = 60;//倒计时时间
    private HashMap<String, Object> fastLoginParams;//快速注册参数
    private String fastLoginUrl = Constant.TOBOSU_URL + "tapp/passport/fast_register";//快速注册接口
    private CustomWaitDialog customWaitDialog;
    private MyChatView myChatView;
    //要从上个界面传来的数据  显示在这个界面
    private int mPrice;//半包总价格
    private float mWeiPrice;//卫生间价格 占比12.63%
    private float mTingPrice;//客厅价格  占比22.81%
    private float mChuPrice;//厨房价格  占比9.19%
    private float mWoshiPrice;//卧室价格  占比26.37%
    private float mYangtaiPrice;//阳台价格  占比9.00%
    private float mOtherPrice;//其他价格  占比20.00%

    private List<Float> floatList = new ArrayList<>();

    private String mPhoneNum;//上个界面传来的用户手机号码

    //控件
    private ImageView fdpBack;//返回键
    private TextView fdpPrice;//显示的半包总价
    private LinearLayout fdpLL;//显示发送验证码界面
    private RelativeLayout fdpRl;//标题栏
    /**
     * 饼图上显示的价格
     */
    private TextView fdpTextWeishengjian;//显示卫生间的半包价格
    private TextView fdpTextKeting;//显示客厅的半包价格
    private TextView fdpTextQita;//显示其他的半包价格
    private TextView fdpTextChufang;//显示厨房的半包价格
    private TextView fdpTextWoshi;//显示卧室的半包价格
    private TextView fdpTextYangtai;//显示阳台的半包价格

    /**
     * 表格显示的价格
     */
    private TextView fdpKtPrice;
    private TextView fdpWsjPrice;
    private TextView fdpCfPrice;
    private TextView fdpYtPrice;
    private TextView fdpWsPrice;
    private TextView fdpQtPrice;
    /**
     * 获取验证码组件
     */
    private TextView fdpPhonenum;
    private Button fdpGetcode;//获取验证码按钮
    private ImageView fdpOksend;//确认提交按钮
    private EditText fdpInputCode;//输入验证码窗口


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        AppInfoUtil.setTitleBarColor(FreeDesignPrice.this, R.color.bg_fd_title);
        setContentView(R.layout.activity_free_design_price);
        mContext = FreeDesignPrice.this;
        intent = getIntent();
        customWaitDialog = new CustomWaitDialog(mContext);
        bindView();
        initView();
    }

    //绑定控件
    private void bindView() {
        floatList.add(9.19f);
        floatList.add(26.37f);
        floatList.add(9f);
        floatList.add(20f);
        floatList.add(12.63f);
        floatList.add(22.81f);
        myChatView = (MyChatView) findViewById(R.id.fdp_my_chat_view);
        fdpLL = (LinearLayout) findViewById(R.id.fdp_ll);

        fdpBack = (ImageView) findViewById(R.id.fdp_back);
        fdpPrice = (TextView) findViewById(R.id.fdp_price);
        fdpRl= (RelativeLayout) findViewById(R.id.fd_title);
        fdpTextWeishengjian = (TextView) findViewById(R.id.fdp_text_weishengjian);
        fdpTextKeting = (TextView) findViewById(R.id.fdp_text_keting);
        fdpTextQita = (TextView) findViewById(R.id.fdp_text_qita);
        fdpTextChufang = (TextView) findViewById(R.id.fdp_text_chufang);
        fdpTextWoshi = (TextView) findViewById(R.id.fdp_text_woshi);
        fdpTextYangtai = (TextView) findViewById(R.id.fdp_text_yangtai);

        fdpKtPrice = (TextView) findViewById(R.id.fdp_keiting_price);
        fdpWsjPrice = (TextView) findViewById(R.id.fdp_weishengjian_price);
        fdpCfPrice = (TextView) findViewById(R.id.fdp_chufang_price);
        fdpYtPrice = (TextView) findViewById(R.id.fdp_yangtai_price);
        fdpWsPrice = (TextView) findViewById(R.id.fdp_woshi_price);
        fdpQtPrice = (TextView) findViewById(R.id.fdp_qita_price);

        fdpPhonenum = (TextView) findViewById(R.id.fdp_phone_num);
        fdpGetcode = (Button) findViewById(R.id.fdp_get_code);
        fdpOksend = (ImageView) findViewById(R.id.fdp_ok_send);
        fdpInputCode = (EditText) findViewById(R.id.fpd_input_code);
    }

    private void initView() {
        fdpRl.setBackgroundColor(Color.parseColor("#ff882e"));
        myChatView.setFloatList(floatList);
        //设置点击事件监听
        fdpBack.setOnClickListener(occl);
        fdpGetcode.setOnClickListener(occl);
        fdpOksend.setOnClickListener(occl);
        //获取上一个界面的传来的数据
        mPhoneNum = intent.getStringExtra("mPhoneNum");
        mPrice = intent.getIntExtra("mPrice", 0);

        mWeiPrice = intent.getFloatExtra("mWeiPrice", 0);
        mTingPrice = intent.getFloatExtra("mTingPrice", 0);
        mChuPrice = intent.getFloatExtra("mChuPrice", 0);
        mWoshiPrice = intent.getFloatExtra("mWoshiPrice", 0);
        mYangtaiPrice = intent.getFloatExtra("mYangtaiPrice", 0);
        mOtherPrice = intent.getFloatExtra("mOtherPrice", 0);
        setText(mPhoneNum, mPrice, mWeiPrice, mTingPrice, mChuPrice, mWoshiPrice, mYangtaiPrice, mOtherPrice);
        Log.e(TAG, "当前登录用户的相关信息==Token" + AppInfoUtil.getToekn(mContext) + "==CellPhone" + AppInfoUtil.getCellPhone(mContext));
        if (TextUtils.isEmpty(AppInfoUtil.getToekn(mContext))) {
            //未登录 显示该组件
            fdpLL.setVisibility(View.VISIBLE);
        } else if (!TextUtils.isEmpty(AppInfoUtil.getToekn(mContext)) && (TextUtils.isEmpty(AppInfoUtil.getCellPhone(mContext)) || AppInfoUtil.getCellPhone(mContext).equals("未绑定"))) {
            //用户已经登录但是未绑定 显示该组件
            fdpLL.setVisibility(View.VISIBLE);
        } else {
            //其他情况隐藏该组件
            fdpLL.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener occl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fdp_back:
                    finish();
                    break;
                case R.id.fdp_get_code:
                    //走获取验证码的方法
                    if (Constant.checkNetwork(mContext)) {
                        if ("重新获取".equals(fdpGetcode.getText().toString()) || "获取验证码".equals(fdpGetcode.getText().toString())) {
                            GetVerificationPopupwindow popupwindow = new GetVerificationPopupwindow(mContext);
                            popupwindow.phone = mPhoneNum;
                            popupwindow.version = AppInfoUtil.getAppVersionName(mContext);
                            popupwindow.showAtLocation(fdpGetcode.getRootView(), Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                        } else {
                            Toast.makeText(getApplicationContext(), "您已经获取过了!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, "当前无网络连接", Toast.LENGTH_LONG).show();
                    }

                    break;
                case R.id.fdp_ok_send:
                    //走接口  快速注册
                    if (Constant.checkNetwork(mContext)) {
                        if (!TextUtils.isEmpty(fdpInputCode.getText().toString().trim())) {
                            customWaitDialog.show();
                            fastLoginParams = AppInfoUtil.getPublicHashMapParams(mContext);
                            fastLoginParams.put("mobile", mPhoneNum);
                            fastLoginParams.put("platform_type", "1");
                            fastLoginParams.put("system_type", "1");
                            fastLoginParams.put("source", "1112");
                            fastLoginParams.put("chcode", AppInfoUtil.getChannType(MyApplication.getContext()));
                            fastLoginParams.put("msg_code", fdpInputCode.getText().toString().trim());
                            fastLoginParams.put("urlhistory", Constant.PIPE); // 渠道代码
                            fastLoginParams.put("comeurl", Constant.PIPE); //订单发布页面
                            requestFastLogin();
                        } else {
                            Toast.makeText(mContext, "验证码不能为空！", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, "当前无网络连接", Toast.LENGTH_LONG).show();
                    }

                    break;
            }
        }
    };

    private void setText(String mPhoneNum, float mPrice,
                         float mWeiPrice, float mTingPrice,
                         float mChuPrice, float mWoshiPrice,
                         float mYangtaiPrice, float mOtherPrice) {
        fdpPhonenum.setText("" + mPhoneNum);
        fdpPrice.setText("¥" + mPrice);

        fdpTextWeishengjian.setText("¥" + mWeiPrice);
        fdpWsjPrice.setText("" + mWeiPrice);

        fdpTextKeting.setText("¥" + mTingPrice);
        fdpKtPrice.setText("" + mTingPrice);

        fdpTextChufang.setText("¥" + mChuPrice);
        fdpCfPrice.setText("" + mChuPrice);

        fdpTextWoshi.setText("¥" + mWoshiPrice);
        fdpWsPrice.setText("" + mWoshiPrice);

        fdpTextYangtai.setText("¥" + mYangtaiPrice);
        fdpYtPrice.setText("" + mYangtaiPrice);

        fdpTextQita.setText("¥" + mOtherPrice);
        fdpQtPrice.setText("¥" + mOtherPrice);
    }

    //    验证倒计时
    public void startCount() {
        new Thread() {
            @Override
            public void run() {
                while (count > 0) {
                    SystemClock.sleep(1000);
                    runOnUiThread(new Task());
                    count--;
                }
            }

            ;
        }.start();
    }

    private class Task implements Runnable {

        @Override
        public void run() {
            if (count > 0) {
                fdpGetcode.setText(count + "S");
                fdpGetcode.setEnabled(false);
            } else {
                fdpGetcode.setEnabled(true);
                fdpGetcode.setText("重新获取");
                count = 60;
            }
        }
    }

    /***
     * 快速注册用户接口请求
     */
    private void requestFastLogin() {
        OKHttpUtil.post(fastLoginUrl, fastLoginParams, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "请求失败！");
                        customWaitDialog.dismiss();
                        Toast.makeText(mContext, "服务端请求失败！", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if (jsonObject.getInt("error_code") == 0) {
                                customWaitDialog.dismiss();
                                parseJson(jsonObject);
                            } else {
                                customWaitDialog.dismiss();
                                Toast.makeText(mContext, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    //解析json
    private void parseJson(JSONObject jsonObject) throws JSONException {
        JSONObject data = jsonObject.getJSONObject("data");
        Log.e("请求获取的数据", "=====" + data.toString());
        String icon = data.getString("icon");
        String nickname = data.getString("name");
        String mark = data.getString("mark");
        String token = data.getString("token");
        String userid = data.getString("uid");
        String cityname = data.getString("cityname");
        String cellphone = data.getString("cellphone");

        SharedPreferences settings = getSharedPreferences("userInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("nickname", nickname);
        editor.putString("icon", icon);
        editor.putString("mark", mark);
        editor.putString("userid", userid);
        editor.putString("token", token);
        editor.putString("cityname", cityname);
        editor.putString("cellphone", cellphone);
        editor.commit();
        Toast.makeText(this, "绑定成功！", Toast.LENGTH_SHORT).show();
        finish();
    }
}
