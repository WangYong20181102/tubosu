package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

/**
 * creat by lin
 * 设置新密码页面
 */
public class SetNewPasswordActivity extends BaseActivity {
    private Context mContext;
    private String TAG = "SetNewPasswordActivity";

    @BindView(R.id.set_new_pw_back)
    LinearLayout setNewPwBack;//返回按钮
    @BindView(R.id.set_new_pw)
    EditText setNewPw;//新密码的输入框
    @BindView(R.id.next_step)
    TextView nextStep;//下一步

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);
        ButterKnife.bind(this);
        mContext = this;
    }

    @OnClick({R.id.set_new_pw_back, R.id.set_new_pw, R.id.next_step})
    public void onViewClickedInSetNewPasswordActivity(View view) {
        switch (view.getId()) {
            case R.id.set_new_pw_back:
                finish();
                break;
            case R.id.next_step:
                intoNextStep();
                break;
        }
    }

    //点击下一步进入密码的修改
    private void intoNextStep() {
        if (!TextUtils.isEmpty(setNewPw.getText().toString())) {
            if (setNewPw.getText().toString().length() >= 6 && setNewPw.getText().toString().length() <= 16) {
                HttpSetNewPassword();
            } else {
                Toast.makeText(mContext, "新密码长度在6-16位之间哦~", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mContext, "新密码不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    //TODO 设置新的密码 还没写完  参数未定
    private void HttpSetNewPassword() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        HttpUtils.doPost(UrlConstans.SET_NEW_PASSWORD, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败==" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {

                    } else {
                        //获取数据失败
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }
}
