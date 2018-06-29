package com.tobosu.mydecorate.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.base.BaseActivity;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.util.CacheManager;
import com.tobosu.mydecorate.util.MD5Util;
import com.tobosu.mydecorate.util.Util;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by dec on 2016/9/26.
 */
public class UserLoginActivity extends BaseActivity {
    private static final String TAG = UserLoginActivity.class.getSimpleName();
    private Context mContext;
    private RelativeLayout rel_username_login_back;
    private Button btn_user_login;

    private EditText et_user_account;
    private EditText et_useraccount_password;

    private String account_login_url = Constant.ZHJ + "tapp/passport/app_login";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);
        mContext = UserLoginActivity.this;
        initView();

    }

    private void initView() {
        rel_username_login_back = (RelativeLayout) findViewById(R.id.rel_username_login_back);
        rel_username_login_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        et_user_account = (EditText) findViewById(R.id.et_user_account);
        et_useraccount_password = (EditText) findViewById(R.id.et_useraccount_password);

        btn_user_login = (Button) findViewById(R.id.btn_user_login);
        btn_user_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 登录在此
                String name = et_user_account.getText().toString().trim();
                String psd = et_useraccount_password.getText().toString().trim();
                if ("".equals(name)) {
                    Toast.makeText(UserLoginActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if ("".equals(psd)) {
                    Toast.makeText(UserLoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Util.isNetAvailable(mContext)) {
                    accountLogin(name, psd);
                } else {
                    Toast.makeText(UserLoginActivity.this, "网络不佳,请稍后再试~", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void accountLogin(String accountName, String accountPsd) {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("mobile", accountName);
        hashMap.put("pass", MD5Util.md5(accountPsd));
        okHttpUtil.post(account_login_url, hashMap, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                try {
                    JSONObject loginObj = new JSONObject(json);
                    System.out.println("shejishi" + json);
                    if (loginObj.getInt("error_code") == 0) {
                        MobclickAgent.onEvent(mContext, "click_login_account_succeed");
                        JSONObject data = loginObj.getJSONObject("data");

                        String headPicUrl = data.getString("icon");
                        Log.e(TAG, "用户账号登录获取头像====" + headPicUrl);
                        String userName = data.getString("name");
                        String mark = data.getString("mark");
                        String token = data.getString("token");
                        String userid = data.getString("uid");
                        CacheManager.setUserUid(mContext, userid); // 保存uid
                        String cityname = data.getString("cityname");
                        String mark_type = data.getString("mark");
                        ;

                        MobclickAgent.onProfileSignIn(userid);
                        SharedPreferences saveInfo = getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = saveInfo.edit();
                        editor.putString("user_name", userName);
                        editor.putString("head_pic_url", headPicUrl);
                        editor.putString("mark", mark);
                        editor.putString("user_id", userid);
                        editor.putString("token", token);
                        editor.putString("city_name", cityname);
                        editor.putString("mark", mark_type);
                        editor.commit();
                        setResult(Constant.USER_LOGIN_RESULTCODE);
                        finish();

                    } else {
                        Toast.makeText(mContext, loginObj.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "----解析错误--");
                }

            }

            @Override
            public void onFail(Request request, IOException e) {
                Log.d(TAG, "Login --onFail--登录错误--");
            }

            @Override
            public void onError(Response response, int code) {
                Log.d(TAG, "Login -- onError --登录错误--");
            }
        });
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


}
