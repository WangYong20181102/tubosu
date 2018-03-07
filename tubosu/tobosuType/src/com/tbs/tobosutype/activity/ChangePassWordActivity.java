package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.Intent;
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
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.MD5Util;
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

public class ChangePassWordActivity extends com.tbs.tobosutype.base.BaseActivity {
    @BindView(R.id.banner_dever)
    View bannerDever;
    @BindView(R.id.change_pass_word)
    EditText changePassWord;
    @BindView(R.id.change_pass_word_clean)
    ImageView changePassWordClean;
    @BindView(R.id.change_pass_word_btn)
    TextView changePassWordBtn;
    @BindView(R.id.change_pass_word_cancel)
    RelativeLayout changePassWordCancel;
    private String TAG = "ChangePassWordActivity";
    private Context mContext;
    private Intent mIntent;
    private String mPhoneNum;//手机号码  从上一个页面传来
    private String mCode;//验证码 从上一个页面传来


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass_word);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
        mIntent = getIntent();
        mPhoneNum = mIntent.getStringExtra("mPhoneNum");
        mCode = mIntent.getStringExtra("mCode");
    }

    @OnClick({R.id.change_pass_word_clean, R.id.change_pass_word_btn, R.id.change_pass_word_cancel})
    public void onViewClickedInChangePassword(View view) {
        switch (view.getId()) {
            case R.id.change_pass_word_cancel:
                finish();
                break;
            case R.id.change_pass_word_clean:
                //清除新输入的密码
                changePassWord.setText("");
                break;
            case R.id.change_pass_word_btn:
                //进行密码的校验
                if (!TextUtils.isEmpty(changePassWord.getText().toString())) {
                    String Md5PassWord = MD5Util.md5(changePassWord.getText().toString());
                    HttpChangePassWord(Md5PassWord);
                } else {
                    Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void HttpChangePassWord(String Md5PassWord) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("cellphone", mPhoneNum);
        param.put("verify_code", mCode);
        param.put("new_password", Md5PassWord);
        OKHttpUtil.post(Constant.FORGET_PASSWORD, param, new Callback() {
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
                Log.e(TAG, "链接成功========" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    final String msg = jsonObject.optString("msg");
                    if (status.equals("200")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //密码修改成功
                                Toast.makeText(mContext, "密码修改成功！", Toast.LENGTH_SHORT).show();
                                finish();
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
}
