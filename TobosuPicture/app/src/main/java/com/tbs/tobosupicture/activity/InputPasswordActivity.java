package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.Md5Utils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;

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

public class InputPasswordActivity extends BaseActivity {

    @BindView(R.id.check_pw_back)
    LinearLayout checkPwBack;
    @BindView(R.id.check_pw)
    EditText checkPw;
    @BindView(R.id.bind_phone_ok)
    TextView bindPhoneOk;

    private Context mContext;
    private String TAG = "InputPasswordActivity";
    private Intent mIntent;
    private String cellphone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_password);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
        mIntent = getIntent();
        cellphone = mIntent.getStringExtra("cellphone");
    }

    @OnClick({R.id.check_pw_back, R.id.bind_phone_ok})
    public void onViewClickedInInputPasswordActivity(View view) {
        switch (view.getId()) {
            case R.id.check_pw_back:
                finish();
                break;
            case R.id.bind_phone_ok:
                //验证密码是否正确 之后确认绑定
                if (TextUtils.isEmpty(checkPw.getText().toString())) {
                    Toast.makeText(mContext, "输入的密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    HttpBindPhoneNum(cellphone, Md5Utils.md5(checkPw.getText().toString()));
                }
                break;
        }
    }

    private void HttpBindPhoneNum(String cellphone, String md5Passwrod) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("cellphone", cellphone);
        param.put("password", md5Passwrod);
        HttpUtils.doPost(UrlConstans.BIND_CELLPHONE, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败=====" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功====" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //验证成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "绑定成功！", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    } else {
                        //验证失败
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "验证密码失败~", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
