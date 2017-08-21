package com.tobosu.mydecorate.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.util.CacheManager;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.view.CustomWaitDialog;
import com.tobosu.mydecorate.view.GetVerificationPopupwindow;
import com.tobosu.mydecorate.view.LoadingWindow;
import com.tobosu.mydecorate.view.VerifyCodeDialog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by dec on 2016/9/26.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Context mContext;
    private EditText et_phonenum;
    private EditText et_verify_code;
    private TextView tv_verify_code;
    private ImageView iv_login;

    private String phone_code_login_url = Constant.ZHJ + "tapp/passport/fast_register_mt";

    private String weixin_login_url = Constant.ZHJ + "tapp/passport/login_third_party";

    private GetVerificationPopupwindow popupwindow = null;

    private RequestQueue loginRequestQueue;

    private StringRequest loginStringRequest;

    private String token = "";

    private Button btn_login;

    private RelativeLayout rel_wechat;

    private RelativeLayout rel_username;

    private CustomWaitDialog waitDialog;

    private int count = 60;

    private LoadingWindow wechatWindow;

    // 友盟分享的服务
    private com.umeng.socialize.controller.UMSocialService controller = null;

    private SendCountReceiver receiver = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = LoginActivity.this;
        controller = UMServiceFactory.getUMSocialService("com.umeng.share");
        initReceiver();
        weixinConfig();
        initViews();
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter(Constant.SEND_STARTCOUNT_ACTION);
        receiver = new SendCountReceiver();
        registerReceiver(receiver, filter);
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

    private void initViews() {
        et_phonenum = (EditText) findViewById(R.id.et_phonenum);
        et_phonenum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.length() == 0) return;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    if (i != 3 && i != 8 && s.charAt(i) == '-') {
                        continue;
                    } else {
                        sb.append(s.charAt(i));
                        if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != '-') {
                            sb.insert(sb.length() - 1, '-');
                        }
                    }
                }
                if (!sb.toString().equals(s.toString())) {
                    int index = start + 1;
                    if (sb.charAt(start) == '-') {
                        if (before == 0) {
                            index++;
                        } else {
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }
                    et_phonenum.setText(sb.toString());
                    et_phonenum.setSelection(index);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        et_verify_code = (EditText) findViewById(R.id.et_verify_code);

        tv_verify_code = (TextView) findViewById(R.id.tv_verify_code);
        tv_verify_code.setEnabled(true);

        iv_login = (ImageView) findViewById(R.id.iv_login);
        iv_login.setOnClickListener(this);
        tv_verify_code.setOnClickListener(this);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        rel_wechat = (RelativeLayout) findViewById(R.id.rel_wechat);
        rel_wechat.setOnClickListener(this);
        rel_username = (RelativeLayout) findViewById(R.id.rel_username);
        rel_username.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_login:
                finish();
                break;
            case R.id.tv_verify_code:
                String tempPhone = et_phonenum.getText().toString().trim().replace("-", "");
                if("".equals(tempPhone)){
                    Util.setToast(mContext, "电话号码不能为空");
                    return;
                }

                if (Util.judgePhone(mContext, tempPhone)) {
//                    getVerifyCode();
                    VerifyCodeDialog.Builder builder = new VerifyCodeDialog.Builder(mContext, tv_verify_code, tempPhone);
                    builder.setCancelButton(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.create().show();

                }

                break;
            case R.id.btn_login:
                MobclickAgent.onEvent(mContext, "click_login_mobile");
                if ("".equals(et_phonenum.getText().toString().trim())) {
                    Toast.makeText(mContext, "电话号码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if ("".equals(et_verify_code.getText().toString().trim())) {
                    Toast.makeText(mContext, "短信验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (et_verify_code.getText().toString().trim().length() != 4) {
                    Toast.makeText(mContext, "短信验证码不正确", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (Util.isNetAvailable(mContext)) {
                        showLoadingView();

                        loginRequestQueue = Volley.newRequestQueue(mContext);
//                        token = AppUtil.getToekn(mContext);
                        loginStringRequest = new StringRequest(Request.Method.POST, phone_code_login_url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                hideLoadingView();
                                System.out.println("请求结果:" + s);
                                parseLoginJson(s);
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                System.out.println("--login_error_" + volleyError.getMessage());
                            }
                        }) {
                            // 携带参数
                            @Override
                            protected HashMap<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> hashMap = new HashMap<String, String>();
//                                hashMap.put("token", ""); //必须穿空字符串
                                hashMap.put("system_type", "1"); // 1是安卓， 2是ios
                                hashMap.put("platform_type", "2"); // 1是土拨鼠， 2是装好家
                                hashMap.put("chcode", Util.getChannType(getApplicationContext()));
                                hashMap.put("mobile", et_phonenum.getText().toString().trim().replaceAll("-", ""));
                                hashMap.put("msg_code", et_verify_code.getText().toString().trim());
                                return hashMap;
                            }

                        };
                        loginStringRequest.setTag("volley_request_login");
                        loginRequestQueue.add(loginStringRequest);

                    } else {
                        hideLoadingView();
                        Toast.makeText(mContext, "请检查网络是否可用", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }

                break;
            case R.id.rel_wechat:
                MobclickAgent.onEvent(mContext, "click_login_weixin");
                do_weixin_login();
                break;
            case R.id.rel_username:
                MobclickAgent.onEvent(mContext, "click_login_account");
                Intent intent = new Intent(mContext, UserLoginActivity.class);
                startActivityForResult(intent, Constant.LOGIN_TO_USERACCOUNT_LOGIN_REQUESTCODE);
                break;
        }
    }

    private void parseLoginJson(String loginJson) {
        String headPicUrl = "";
        String userName = "";
        String mark = "";
        String phone = "";
        try {
            JSONObject loginJsonObject = new JSONObject(loginJson);
            if (loginJsonObject.getInt("error_code") == 0) {
                MobclickAgent.onEvent(mContext, "click_login_mobile_succeed");
                JSONObject data = loginJsonObject.getJSONObject("data");


                headPicUrl = data.getString("icon");
                Log.e(TAG, "登录获取用户的头像=====" + headPicUrl);
                userName = data.getString("name");
                mark = data.getString("mark");
                token = data.getString("token");
                String userid = data.getString("uid");
                CacheManager.setUserUid(mContext, userid); // 保存uid
                String cityname = data.getString("cityname");
                phone = data.getString("cellphone");

                /*
                    "id":"27**0",
                    "mark":"1",1--用户  2--设计师  3---装修公司
                    "name":"帝X哥",
                    "realname":"",
                    "cityid":"199",
                    "icon":"http://aliyun.tbscache.com/res/common/images/icon/icon8.jpg",
                    "mobile":"18********5",
                    "newuid":"29**61",
                    "nickname":"帝X哥",
                    "cityname":"深圳市",
                    "province":"广东省",
                    "uid":"29**61",
                    "cellphone":"1xxxxxxxssss",
                    "login_time":"2016-10-11 11:24:58",
                    "token":"C/1P19mW49z+YXqQ...
                 */
                MobclickAgent.onProfileSignIn(userid);
                SharedPreferences saveInfo = getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = saveInfo.edit();
                editor.putString("user_name", userName);
                editor.putString("head_pic_url", headPicUrl);
                editor.putString("mark", mark);
                editor.putString("user_id", userid);
                editor.putString("token", token);
                editor.putString("cellphone", phone);
                editor.putString("city_name", cityname);
                editor.commit();
                hideLoadingView();

            } else {
                Toast.makeText(mContext, loginJsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "====" + loginJsonObject.getString("msg"));
                hideLoadingView();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "LoginFragmentPhone --解析错误--");
        }

        if (et_verify_code.getText().toString().trim().equals("0")) {
            Toast.makeText(mContext, "请1分钟后登录", Toast.LENGTH_SHORT).show();
            return;
        }
        setResult(Constant.LOGIN_RESULTCODE);
        finish();
    }


//    private void getVerifyCode() {
//        if (Util.isNetAvailable(mContext)) {
//            popupwindow = new GetVerificationPopupwindow(mContext);
//            popupwindow.phone = et_phonenum.getText().toString().trim().replaceAll("-", "");
//            popupwindow.version = Util.getAppVersionName(mContext);
//            popupwindow.showAtLocation(findViewById(R.id.rel_login_activity), Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
//        } else {
//            Toast.makeText(mContext, "请检查网络是否可用", Toast.LENGTH_SHORT).show();
//            return;
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** 使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = controller.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

        switch (resultCode) {
            case Constant.USER_LOGIN_RESULTCODE:
                setResult(Constant.LOGIN_RESULTCODE);
                finish();
                break;
        }
    }

    /**
     * 倒计时方法
     */
    public void startDownCount() {
        new Thread() {
            @Override
            public void run() {
                while (count > 0) {
                    SystemClock.sleep(1000);
                    runOnUiThread(new CountDownTask());
                    count--;
                }
            }
        }.start();
    }


    /**
     * 60秒倒计时类
     */
    private class CountDownTask implements Runnable {

        @Override
        public void run() {
            if (count > 0) {
                tv_verify_code.setText(count + " 秒");
                tv_verify_code.setEnabled(false);
            } else {
                Log.d(TAG, true + "");
                tv_verify_code.setText("重新获取");
                tv_verify_code.setEnabled(true);
                count = 60;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (loginRequestQueue != null) {
            loginRequestQueue.cancelAll("volley_request_login");
        }
    }

    @Override
    protected void onDestroy() {
        if (popupwindow != null) {
            popupwindow.dismiss();
        }
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    class SendCountReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.SEND_STARTCOUNT_ACTION)) {
                Util.setToast(context, "验证码错误!");
            }
        }
    }


    private void do_weixin_login() {

        controller.doOauthVerify(mContext, SHARE_MEDIA.WEIXIN, new SocializeListeners.UMAuthListener() {

            @Override
            public void onStart(SHARE_MEDIA platform) {
                Toast.makeText(mContext, "正在加载中...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {
                Toast.makeText(mContext, "授权失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(Bundle value, SHARE_MEDIA platform) {
                // 获取uid
                String uid = value.getString("uid");
                // uid不为空，说明授权成功
                if (!TextUtils.isEmpty(uid)) {
                    // 记录用的是哪个平台的授权，退出时要用到
                    getUserInfo(platform);
                } else {
                    Toast.makeText(mContext, "授权失败...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                Toast.makeText(mContext, "授权取消", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 获取用户信息
     *
     * @param platform
     */
    private void getUserInfo(SHARE_MEDIA platform) {
        controller.getPlatformInfo(mContext, platform, new SocializeListeners.UMDataListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(int status, Map<String, Object> info) {

                Log.d("----LoginActivity--->>", info.toString());
                if (info != null) {
                    String head_url = info.get("headimgurl").toString();
                    String nickname = info.get("nickname").toString();
                    String weixin_id = info.get("unionid").toString();
                    getWeixinInfoLogin(head_url, nickname, weixin_id);
                }

            }
        });
    }

    private void getWeixinInfoLogin(String head_url, String nick, String id) {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("kind", "weixin");
        params.put("icon", head_url);
        params.put("nickname", nick);
        params.put("account", id);
        okHttpUtil.post(weixin_login_url, params, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(com.squareup.okhttp.Response response, String json) {
                System.out.println("--登录成功json-- " + json);

                String headPicUrl = "";
                String userName = "";
                String mark = "";
                String token = "";
                String userid = "";
                String cityname = "";

                try {
                    JSONObject object = new JSONObject(json);
                    if (object.getInt("error_code") == 0) {
                        MobclickAgent.onEvent(mContext, "click_login_weixin_succeed");
                        //微信登录成功
                        JSONObject data = object.getJSONObject("data");
                        headPicUrl = data.getString("icon");
                        userName = data.getString("name");
                        mark = data.getString("mark");
                        token = data.getString("token");
                        userid = data.getString("uid");
                        CacheManager.setUserUid(mContext, userid); // 保存uid
                        cityname = data.getString("cityname");

                        SharedPreferences saveInfo = getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = saveInfo.edit();
                        editor.putString("user_name", userName);
                        editor.putString("head_pic_url", headPicUrl);
                        editor.putString("mark", mark);
                        editor.putString("user_id", userid);
                        editor.putString("token", token);
                        editor.putString("city_name", cityname);
                        editor.commit();

                        setResult(Constant.LOGIN_RESULTCODE);
                        finish();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(com.squareup.okhttp.Request request, IOException e) {

            }

            @Override
            public void onError(com.squareup.okhttp.Response response, int code) {

            }
        });
    }

    private void weixinConfig() {
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(mContext, Constant.WEIXIN_APP_ID, Constant.WEIXIN_APP_SECRET);
        wxHandler.addToSocialSDK();
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(mContext, Constant.WEIXIN_APP_ID, Constant.WEIXIN_APP_SECRET);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }


}