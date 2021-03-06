package com.tbs.tobosutype.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.MainActivity;
import com.tbs.tobosutype.activity.MyOwnerAccountManagerActivity;
import com.tbs.tobosutype.customview.GetVerificationPopupwindow;
import com.tbs.tobosutype.customview.LoadingWindow;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.MyApplication;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.CacheManager;
import com.tbs.tobosutype.utils.Util;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 手机登录
 *
 * @author dec
 */
public class LoginFragmentPhone extends Fragment implements OnClickListener, OnKeyListener {
    private static final String TAG = LoginFragmentPhone.class.getSimpleName();
    private RelativeLayout phone_login_rl;
    /**
     * 本页页面
     */
    private View view;

    /**
     * 用户电话号码
     */
    private EditText et_login_userphone;

    /**
     * 用户密码
     */
    private EditText et_login_userphone_verify_code;

    /**
     * 获取验证码按钮
     */
    private TextView tv_get_verifycode;

    private String phone;

    /**
     * 用户手机登陆
     */
    private TextView tv_phonelogin;

    /**
     * 用户微信登陆
     */
    private LinearLayout ll_obtain_weixin_login2;


    /*-------------手机登录相关------------*/
    private HashMap<String, Object> fastLoginParams;

    /**
     * 快速登录接口 和 注册接口
     */
    private String fastLoginUrl = Constant.TOBOSU_URL + "tapp/passport/fast_register";
    /*-------------手机登录相关------------*/


    /*-------------微信登陆相关------------*/
    private LoadingWindow wechatWindow;

    private String weiXinUserName;
    private String weiXinImageUrl;
    private String weiXinUserId;
    private UMShareAPI umShareAPI;
    private Context mContext;

    /**
     * 微信第三方登陆接口
     */
    private String weixinLoginUrl = Constant.TOBOSU_URL + "tapp/passport/login_third_party";

    /**
     * 微信参数对象
     */
    private HashMap<String, Object> weixinLoginParams;

    /*-------------微信登陆相关------------*/
    public static ReceiveBroadCast receiveBroadCast;
    private IntentFilter filter;
    private Activity activity;
    private CountDownTimer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login_phone, null);
        mContext = getActivity();
        umShareAPI = UMShareAPI.get(mContext);
        initView(view);
        initReceiver();
        return view;
    }

    private void initView(View view) {
        et_login_userphone = (EditText) view.findViewById(R.id.et_login_userphone);
        et_login_userphone_verify_code = (EditText) view.findViewById(R.id.et_login_userphone_verify_code);
        tv_get_verifycode = (TextView) view.findViewById(R.id.tv_get_verifycode);
        tv_get_verifycode.setOnClickListener(this);
        tv_phonelogin = (TextView) view.findViewById(R.id.tv_phonelogin);
        tv_phonelogin.setOnClickListener(this);
        ll_obtain_weixin_login2 = (LinearLayout) view.findViewById(R.id.ll_obtain_weixin_login2);
        ll_obtain_weixin_login2.setOnClickListener(this);
        phone_login_rl = view.findViewById(R.id.phone_login_rl);
        phone_login_rl.setBackgroundColor(Color.parseColor("#ffffff"));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    private void initReceiver() {
        receiveBroadCast = new ReceiveBroadCast();
        filter = new IntentFilter();
        filter.addAction("updateUi");
        activity.registerReceiver(receiveBroadCast, filter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_get_verifycode: // 验证码
//                getVerificationCode();

                if ("重新获取".equals(tv_get_verifycode.getText().toString()) || "获取验证码".equals(tv_get_verifycode.getText().toString())) {
                    getMSMCode();
                }

                break;
            case R.id.tv_phonelogin: // 登录
                userPhoneLogin();
                hideEdittext();
                break;
            case R.id.ll_obtain_weixin_login2: // 微信登录
                loginWeixin();
                break;
            case R.id.rel_has_account: // 去账号登录界面
                break;
            default:
                break;
        }

    }

    private void getMSMCode() {
        if (Util.isNetAvailable(getActivity())) {
            OKHttpUtil okHttpUtil = new OKHttpUtil();
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            String token = Util.getDateToken();
            String number = et_login_userphone.getText().toString().trim();
            Util.setErrorLog(TAG, "==========" + token + "=============" + number);
            hashMap.put("token", token);
            hashMap.put("cellphone", number);

            okHttpUtil.post(Constant.DUANXIN_URL, hashMap, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.setToast(getActivity(), "网络繁忙，请稍后再试");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String json = response.body().string();
                    Util.setErrorLog(TAG, json);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                if (jsonObject.getInt("status") == 200) {
                                    Util.setToast(getActivity(), jsonObject.getString("msg"));
                                }
                                startCount();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    public void startCount() {
        // 倒计时
        timer = new CountTime(60000, 1000).start();
    }


    private void userPhoneLogin() {
        if ((TextUtils.isEmpty(et_login_userphone.getText().toString().trim()))) {
            Toast.makeText(getActivity(), "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(et_login_userphone_verify_code.getText().toString().trim())) {
            Toast.makeText(getActivity(), "验证码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }

        fastLoginParams = AppInfoUtil.getPublicHashMapParams(getActivity());
        fastLoginParams.put("chcode", AppInfoUtil.getChannType(MyApplication.getContext()));
        fastLoginParams.put("mobile", et_login_userphone.getText().toString().trim());
        fastLoginParams.put("msg_code", et_login_userphone_verify_code.getText().toString().trim());
        fastLoginParams.put("platform_type", "1"); // 1是土拨鼠  2是装好家
        fastLoginParams.put("system_type", "1"); // 1是安卓， 2是ios
        Log.e("手机登录 账号", "=====" + et_login_userphone.getText().toString().trim());
        Log.e("手机登录 验证码", "=====" + et_login_userphone_verify_code.getText().toString().trim());
        requestFastLogin();

    }

    /**
     * 用户快速注册 登录接口请求
     */
    private void requestFastLogin() {
        Log.e("登录日志", "====进入请求接口");
        if (!Constant.checkNetwork(getActivity())) {
            Constant.toastNetOut(getActivity());
            return;
        }

        OKHttpUtil.post(fastLoginUrl, fastLoginParams, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            Log.e("登录日志", "====>>>" + jsonObject.toString() + "<<<<-");
                            if (jsonObject.getInt("error_code") == 0) {

                                parseJson(jsonObject);
                            } else {
                                Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    /***
     * 获取验证码
     */
    private void getVerificationCode() {
        if (isPhoneNum(et_login_userphone.getText().toString().trim())) {
            if ("重新获取".equals(tv_get_verifycode.getText().toString()) || "获取验证码".equals(tv_get_verifycode.getText().toString())) {
                phone = et_login_userphone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(getActivity(), "手机号码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                GetVerificationPopupwindow popupwindow = new GetVerificationPopupwindow(getActivity());
                popupwindow.phone = phone;
                popupwindow.version = AppInfoUtil.getAppVersionName(getActivity());
                popupwindow.showAtLocation(view.findViewById(R.id.tv_get_verifycode), Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
            } else {
                Toast.makeText(getActivity(), "您已经获取过了！", Toast.LENGTH_SHORT).show();
            }
        }

    }

    /***
     * 判断电话号码是否合法
     *
     * @param phoneNum
     * @return
     */
    public boolean isPhoneNum(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum) || "".equals(phoneNum)) {
            Toast.makeText(getActivity(), "请输入手机号码", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            String MOBILE = "^1(3[0-9]|5[0-35-9]|7[0136-8]|8[0-9])\\d{8}$";
            Pattern pattern = Pattern.compile(MOBILE);
            Matcher matcher = pattern.matcher(phoneNum);
            boolean flag = matcher.matches();
            if (flag == false) {
                Toast.makeText(getActivity(), "请输入合法手机号码", Toast.LENGTH_SHORT).show();
            }
            return matcher.matches();
        }
    }

    /***
     * 微信登录接口请求
     */
    private void loginWeixin() {
        //FIXME
//        wechatWindow = new LoadingWindow(getActivity());
//        UMWXHandler wxHandler = new UMWXHandler(getActivity(), "wx20c4f4560dcd397a", "9b06e848d40bcb04205d75335df6b814");
//        wxHandler.addToSocialSDK();
//        weixinThirdParty(SHARE_MEDIA.WEIXIN);
        //新的微信登录
        umShareAPI.getPlatformInfo(getActivity(), SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                weiXinUserName = map.get("name");//微信的昵称
                weiXinImageUrl = map.get("iconurl");//微信的头像
                weiXinUserId = map.get("openid");//微信的openid
                requestWeixinLogin();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Log.e(TAG, "授权出错=====" + throwable.getMessage());
                Toast.makeText(mContext, "授权出错！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Toast.makeText(mContext, "取消微信登录！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /***
     * 微信用户登录接口请求
     */
    private void requestWeixinLogin() {
        weixinLoginParams = AppInfoUtil.getPublicHashMapParams(getActivity());
        weixinLoginParams.put("kind", "weixin");
        weixinLoginParams.put("icon", weiXinImageUrl);
        weixinLoginParams.put("chcode", AppInfoUtil.getChannType(MyApplication.getContext()));
        weixinLoginParams.put("nickname", weiXinUserName);
        weixinLoginParams.put("account", weiXinUserId);
//        Log.e(TAG, "微信登录获取头像========" + weiXinImageUrl);
        OKHttpUtil.post(weixinLoginUrl, weixinLoginParams, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if (jsonObject.getInt("error_code") == 0) {
                                parseJson(jsonObject);
                            } else {
                                Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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
            String nickname = data.getString("name"); //帝霸哥
            mark = data.getString("mark");
            token = data.getString("token");
            String id = data.getString("id");
            String userid = data.getString("uid");
            String cityname = data.getString("cityname");
            String cellphone = data.getString("cellphone");
            String typeId = data.getString("type_id");//登录用户的类型
            String expected_cost = data.getString("expected_cost");
            CacheManager.setDecorateBudget(getActivity(), expected_cost);

            SharedPreferences saveInfo = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE); // 登录成功后 存储用户标记mark
            SharedPreferences.Editor editor = saveInfo.edit();
            editor.putString("nickname", nickname);
            editor.putString("id", id);
            editor.putString("icon", icon);
            editor.putString("mark", mark);
            editor.putString("userid", userid);
            editor.putString("token", token);
            editor.putString("cityname", cityname);
            editor.putString("cellphone", cellphone);
            editor.putString("typeid", typeId);
            Log.e("获取绑定的手机号码", "====" + cellphone);
            editor.commit();

            AppInfoUtil.setToken(getActivity(), token);
            AppInfoUtil.setUuid(getActivity(), id);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("LoginFragmentPhone -- 解析错误--");
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
                getActivity().setResult(404);
//                Intent i = new Intent();
//                i.setAction(Constant.LOGIN_ACTION);
//                getActivity().sendBroadcast(i);


                getActivity().finish();
            } else {
                Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_SHORT).show();
            }
        }
        et_login_userphone.setText("");
        et_login_userphone_verify_code.setText("");
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
     * 广播接收器 -- 处理验证码按钮上的提示信息
     *
     * @author dec
     */
    public class ReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            tv_get_verifycode.setText(intent.getExtras().getString("seconds"));
        }

    }

    private void hideEdittext() {
        InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(et_login_userphone_verify_code.getWindowToken(), 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (activity != null && receiveBroadCast != null) {
            activity.unregisterReceiver(receiveBroadCast);
        }
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if(receiveBroadCast!=null){
//            MyApplication.getContext().unregisterReceiver(receiveBroadCast);
//        }
//    }
}
