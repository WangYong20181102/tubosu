package com.tbs.tobosutype.useless;

import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.MainActivity;
import com.tbs.tobosutype.customview.GetVerificationPopupwindow;
import com.tbs.tobosutype.customview.LoadingWindow;
import com.tbs.tobosutype.global.AllConstants;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.HintInput;
import com.tbs.tobosutype.utils.HttpServer;
import com.tbs.tobosutype.utils.MD5Util;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.weixin.controller.UMWXHandler;

/**
 * 不再使用 2016-09-02
 */
public class LoginFragment extends Fragment implements OnClickListener, OnKeyListener {

    public static final String BUNDLE_TITLE = "title";
    private String title;
    private View view;

    /**
     * 账户登录时--填写账号布局
     */
    private LinearLayout ll_account_login;

    /**
     * 手机快捷登陆时--填写手机号码和验证码布局
     */
    private LinearLayout ll_phone_login;

    private TextView tv_input_username;

    /**
     * 微信快捷登录布局
     */
    private LinearLayout ll_obtain_weixin;

    /**
     * 登录按钮
     */
    private Button bt_obtain_enter_login;

    /**
     * 验证码按钮
     */
    private Button bt_obtain_verif_login;

    /**
     * 输入用户名
     */
    private EditText et_input_username;

    /**
     * 输入密码
     */
    private EditText et_input_password_login;

    /**
     * 输入电话号码
     */
    private EditText et_input_phone;

    /**
     * 输入验证码
     */
    private EditText et_input_verification_login;

    private UMSocialService mController = UMServiceFactory.getUMSocialService(AllConstants.DESCRIPTOR);

    private String weiXinUserName;
    private String weiXinImageUrl;
    private String weiXinUserId;

    /**
     * 微信第三方登录接口
     */
    private String weixinLoginUrl = AllConstants.TOBOSU_URL + "tapp/passport/login_third_party";
    private RequestParams weixinLoginParams;

    /**
     * 登录接口
     */
    private String userLoginUrl = AllConstants.TOBOSU_URL + "tapp/passport/app_login";
    private RequestParams userLoginParams;

    /**
     * 注册接口
     */
    private String fastLoginUrl = AllConstants.TOBOSU_URL + "tapp/passport/fast_register";

    private RequestParams fastLoginParams;
    private String phone;

    private ReceiveBroadCast receiveBroadCast;
    private IntentFilter filter;
    private Activity activity;

    private CountDownTimer timer;

    private LoadingWindow wechatWindow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater);
    }

    /***
     * 初始化fragment界面
     *
     * @param inflater
     * @return
     */
    private View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_login, null);
        Bundle arguments = getArguments();
        if (arguments != null) {
            title = arguments.getString(BUNDLE_TITLE);
        }

        ll_account_login = (LinearLayout) view.findViewById(R.id.ll_account_login);
        ll_phone_login = (LinearLayout) view.findViewById(R.id.ll_phone_login);
        tv_input_username = (TextView) view.findViewById(R.id.tv_input_username);
        bt_obtain_enter_login = (Button) view.findViewById(R.id.bt_obtain_enter_login);
        bt_obtain_verif_login = (Button) view.findViewById(R.id.bt_obtain_verif_login);
        et_input_username = (EditText) view.findViewById(R.id.et_input_username);
        et_input_phone = (EditText) view.findViewById(R.id.et_input_phone_login);
        et_input_password_login = (EditText) view.findViewById(R.id.et_input_password_login);
        et_input_verification_login = (EditText) view.findViewById(R.id.et_input_verification_login);
        ll_obtain_weixin = (LinearLayout) view.findViewById(R.id.ll_obtain_weixin_login);

        initEvent();

        if ("账户登录".equals(title)) {
            ll_account_login.setVisibility(View.VISIBLE);
            ll_phone_login.setVisibility(View.GONE);
        } else {
            ll_account_login.setVisibility(View.GONE);
            ll_phone_login.setVisibility(View.VISIBLE);
            tv_input_username.setText("手机号");
        }
        return view;
    }

    private void initEvent() {
        ll_obtain_weixin.setOnClickListener(this);
        bt_obtain_enter_login.setOnClickListener(this);
        bt_obtain_verif_login.setOnClickListener(this);
        new HintInput(11, et_input_phone, getActivity());
        new HintInput(4, et_input_verification_login, getActivity());
        et_input_verification_login.setOnKeyListener(this);
    }

    public static LoginFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        if (AllConstants.checkNetwork(getActivity())) {
            switch (v.getId()) {
                case R.id.ll_obtain_weixin_login:
                    loginWeixin();
                    break;
                case R.id.bt_obtain_enter_login:
                    userLogin();
                    break;
                case R.id.bt_obtain_verif_login:
                    getVerificationCode();
                    break;

                default:
                    break;
            }
        } else {
            Toast.makeText(getActivity(), "网络断开请检查网络链接~", Toast.LENGTH_SHORT).show();
        }
    }

    /***
     * 获取验证码
     */
    private void getVerificationCode() {
        if ("重新获取".equals(bt_obtain_verif_login.getText().toString()) || "获取验证码".equals(bt_obtain_verif_login.getText().toString())) {
            phone = et_input_phone.getText().toString().trim();
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(getActivity(), "手机号码不能为空！", Toast.LENGTH_SHORT).show();
                return;
            }
            GetVerificationPopupwindow popupwindow = new GetVerificationPopupwindow(
                    getActivity());
            popupwindow.phone = phone;
            popupwindow.version = AppInfoUtil.getAppVersionName(getActivity());
            popupwindow.showAtLocation(view.findViewById(R.id.bt_obtain_verif_login),
                    Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        } else {
            Toast.makeText(getActivity(), "您已经获取过了！", Toast.LENGTH_SHORT).show();
        }
    }

    public void startCount() {
        timer = new CountTime(60000, 1000).start();
    }

    /***
     * 广播接收器 -- 处理验证码按钮上的提示信息
     *
     * @author dec
     */
    class ReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            bt_obtain_verif_login.setText(intent.getExtras().getString("seconds"));

        }
    }

    @Override
    public void onAttach(Activity activity) {
        this.activity = activity;
        receiveBroadCast = new ReceiveBroadCast();
        filter = new IntentFilter();
        filter.addAction("updateUi");
        activity.registerReceiver(receiveBroadCast, filter);
        super.onAttach(activity);
    }

    @Override
    public void onDestroyView() {
        getActivity().unregisterReceiver(receiveBroadCast);
        super.onDestroyView();
    }

    /***
     * 验证码填写倒计时
     *
     * @author dec
     */
    class CountTime extends CountDownTimer {
        public CountTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Intent intent = new Intent();
            intent.setAction("updateUi");
            int seconds = (int) (millisUntilFinished / 1000) - 1;
            if (seconds != 0) {
                intent.putExtra("seconds", seconds + "S");
            } else {
                intent.putExtra("seconds", "重新获取");
            }
            if (getActivity() != null) {
                getActivity().sendBroadcast(intent);
            }
        }
    }

    /***
     * 用户注册 登录
     */
    private void userLogin() {
        if ("账户登录".equals(title)) {
            // 账户登录页面
            String userName = et_input_username.getText().toString().trim();
            if (TextUtils.isEmpty(userName)) {
                Toast.makeText(getActivity(), "账户信息不能为空！", Toast.LENGTH_SHORT).show();
                return;
            }
            String passWord = et_input_password_login.getText().toString().trim();
            if (TextUtils.isEmpty(passWord)) {
                Toast.makeText(getActivity(), "密码不能为空！", Toast.LENGTH_SHORT).show();
                return;
            }
            userLoginParams = AppInfoUtil.getPublicParams(getActivity());
            userLoginParams.put("mobile", userName);
            userLoginParams.put("pass", MD5Util.md5(passWord));
            requestUserLogin();
        } else {
            // 手机快速登录页面
            if (TextUtils.isEmpty(et_input_verification_login.getText().toString().trim())) {
                Toast.makeText(getActivity(), "验证码不能为空！", Toast.LENGTH_SHORT).show();
                return;
            } else {
                fastLoginParams = AppInfoUtil.getPublicParams(getActivity());
                fastLoginParams.put("mobile", et_input_phone.getText().toString().trim());
                fastLoginParams.put("msg_code", et_input_verification_login.getText().toString().trim());
                requestFastLogin();
            }
        }
    }

    /***
     * 用户登录接口请求
     */
    private void requestUserLogin() {
        if (!AllConstants.checkNetwork(getActivity())) {
            AllConstants.toastNetOut(getActivity());
            return;
        }

        HttpServer.getInstance().requestPOST(userLoginUrl, userLoginParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] body) {

                try {
                    JSONObject jsonObject = new JSONObject(new String(body));
                    if (jsonObject.getInt("error_code") == 0) {
                        parseJson(jsonObject);
                    } else {
                        Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

            }
        });
    }

    /**
     * 用户快速注册 登录接口请求
     */
    private void requestFastLogin() {
        if (!AllConstants.checkNetwork(getActivity())) {
            AllConstants.toastNetOut(getActivity());
            return;
        }

        HttpServer.getInstance().requestPOST(fastLoginUrl, fastLoginParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] body) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(body));
                    if (jsonObject.getInt("error_code") == 0) {
                        parseJson(jsonObject);
                    } else {
                        Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

            }
        });
    }

    /***
     * 微信登录接口请求
     */
    private void loginWeixin() {
        //FIXME
        wechatWindow = new LoadingWindow(getActivity());
        UMWXHandler wxHandler = new UMWXHandler(getActivity(), "wx20c4f4560dcd397a", "9b06e848d40bcb04205d75335df6b814");
        wxHandler.addToSocialSDK();
        weixinThirdParty(SHARE_MEDIA.WEIXIN);
    }

    /***
     * 微信第三方登录
     *
     * @param platform
     */
    private void weixinThirdParty(final SHARE_MEDIA platform) {
        mController.doOauthVerify(getActivity(), platform, new UMAuthListener() {

            @Override
            public void onStart(SHARE_MEDIA platform) {

            }

            @Override
            public void onError(SocializeException e,
                                SHARE_MEDIA platform) {
            }

            @Override
            public void onComplete(Bundle value, SHARE_MEDIA platform) {
                // 获取uid
                if (!TextUtils.isEmpty(value.getString("uid"))) {
                    getUserInfo(platform);
                    //FIXME
                    if (wechatWindow != null && wechatWindow.isShowing()) {
                        wechatWindow.dismiss();
                        wechatWindow = null;
                    }
                } else {
                    Toast.makeText(getActivity(), "登陆失败...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                Toast.makeText(getActivity(), "登陆取消", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /***
     * 获取微信用户的信息
     *
     * @param platform
     */
    private void getUserInfo(final SHARE_MEDIA platform) {
        mController.getPlatformInfo(getActivity(), platform, new UMDataListener() {

            @Override
            public void onStart() {
                Toast.makeText(getActivity(), "获取平台数据开始...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(int status, Map<String, Object> info) {
                if (status == 200 && info != null) {
                    weiXinUserName = (String) info.get("nickname");
                    weiXinImageUrl = (String) info.get("headimgurl");
                    weiXinUserId = (String) info.get("unionid");
                    requestWeixinLogin();
                } else {
                    Toast.makeText(getActivity(), "获取用户信息失败！", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

        });
    }

    /***
     * 微信用户登录接口请求
     */
    private void requestWeixinLogin() {
        weixinLoginParams = AppInfoUtil.getPublicParams(getActivity());
        weixinLoginParams.put("kind", "weixin");
        weixinLoginParams.put("icon", weiXinImageUrl);
        weixinLoginParams.put("nickname", weiXinUserName);
        weixinLoginParams.put("account", weiXinUserId);

        HttpServer.getInstance().requestPOST(weixinLoginUrl, weixinLoginParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] body) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(body));
                    if (jsonObject.getInt("error_code") == 0) {
                        parseJson(jsonObject);
                    } else {
                        Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

            }
        });
    }

    /***
     * 将json解析为用户信息
     *
     * @param jsonObject
     * @throws JSONException
     */
    private void parseJson(JSONObject jsonObject) throws JSONException {
        String mark = "";
        String token = "";
        try {
            JSONObject data = jsonObject.getJSONObject("data");
            String icon = data.getString("icon");
            String nickname = data.getString("name");
            mark = data.getString("mark");
            token = data.getString("token");
            String userid = data.getString("uid");
            String cityname = data.getString("cityname");
            String cellphone = data.getString("cellphone");

            SharedPreferences saveInfo = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE); // 登录成功后 存储用户标记mark
            SharedPreferences.Editor editor = saveInfo.edit();
            editor.putString("nickname", nickname);
            editor.putString("icon", icon);
            editor.putString("mark", mark);
            editor.putString("userid", userid);
            editor.putString("token", token);
            editor.putString("cityname", cityname);
            editor.putString("cellphone", cellphone);
            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("LoginFragment -- 解析错误");
        }

        Intent favIntent = getActivity().getIntent();

        if (favIntent.getBooleanExtra("isFav", false)) {
            favIntent.putExtra("token", token);
            getActivity().setResult(0, favIntent);
            getActivity().finish();
        } else {
            if ("1".equals(mark) || "3".equals(mark)) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MainActivity.class);
                AppInfoUtil.ISJUSTLOGIN = true;
                getActivity().startActivity(intent);
//				getActivity().finish();
            } else {
                Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_SHORT).show();
            }
        }
        et_input_password_login.setText("");
        et_input_username.setText("");
        et_input_phone.setText("");
        et_input_verification_login.setText("");
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                activity.unregisterReceiver(receiveBroadCast);
                timer.cancel();
            }
        }
        return false;
    }
}
