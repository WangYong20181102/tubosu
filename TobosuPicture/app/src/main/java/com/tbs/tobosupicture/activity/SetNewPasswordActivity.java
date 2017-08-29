package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean.EC;
import com.tbs.tobosupicture.bean.Event;
import com.tbs.tobosupicture.bean._User;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.EventBusUtil;
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

/**
 * creat by lin
 * 设置新密码页面
 */
public class SetNewPasswordActivity extends BaseActivity {
    private Context mContext;
    private String TAG = "SetNewPasswordActivity";
    private String cellPhone;
    private Intent mIntent;
    private Gson mGson;

    @BindView(R.id.set_new_pw_back)
    LinearLayout setNewPwBack;//返回按钮
    @BindView(R.id.set_new_pw)
    EditText setNewPw;//新密码的输入框
    @BindView(R.id.next_step)
    TextView nextStep;//下一步(确认并登录)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
        mGson = new Gson();
        mIntent = getIntent();
        cellPhone = mIntent.getStringExtra("cellphone");
    }

    @OnClick({R.id.set_new_pw_back, R.id.set_new_pw, R.id.next_step})
    public void onViewClickedInSetNewPasswordActivity(View view) {
        switch (view.getId()) {
            case R.id.set_new_pw_back:
                finish();
                break;
            case R.id.next_step:
                //确认并登录
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

    //TODO 设置新的密码
    private void HttpSetNewPassword() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("cellphone", cellPhone);
        param.put("password", Md5Utils.md5(setNewPw.getText().toString()));
        param.put("system_type", "1");
        param.put("platform_type", "3");
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
                        //TODO 进入设置页面   在此之前将用户登录 录入用户的信息 将之前的Activity销毁
                        String data = jsonObject.getString("data");
                        _User user = mGson.fromJson(data, _User.class);
                        SpUtils.saveUserNick(mContext, user.getNick());
                        SpUtils.saveUserIcon(mContext, user.getIcon());
                        SpUtils.saveUserPersonalSignature(mContext, user.getPersonal_signature());
                        SpUtils.saveUserUid(mContext, user.getUid());
                        SpUtils.saveUserType(mContext, user.getUser_type());
                        Log.e(TAG, "获取用户的uid====" + SpUtils.getUserUid(mContext));
                        EventBusUtil.sendEvent(new Event(EC.EventCode.LOGIN_INITDATA));
                        EventBusUtil.sendEvent(new Event(EC.EventCode.FNISHI_LOGINACTIVITY));
                        finish();
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
