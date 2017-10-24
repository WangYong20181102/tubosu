package com.tbs.tobosutype.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.MainActivity;
import com.tbs.tobosutype.customview.LoadingWindow;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.MyApplication;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.CacheManager;
import com.tbs.tobosutype.utils.MD5Util;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 账号登陆
 *
 * @author dec
 */
public class LoginFragmentAccount extends Fragment implements OnClickListener {
    private static final String TAG = LoginFragmentAccount.class.getSimpleName();
    private Context mContext;

    /**
     * 账号
     */
    private EditText et_login_useraccount;

    /**
     * 密码
     */
    private EditText et_login_useraccount_password;


    /**
     * 账户登录
     */
    private TextView tv_accountlogin;

    /**
     * 账号页面 微信登录
     */
    private LinearLayout ll_obtain_weixin_login_account;

	/*-------------账号登陆相关------------*/
    /**
     * 登录接口
     */
    private String userLoginUrl = Constant.TOBOSU_URL + "tapp/passport/app_login";
    private HashMap<String, Object> userLoginParams;
    /*-------------账号登陆相关------------*/


    /*-------------微信登陆相关------------*/
    private UMShareAPI umShareAPI;
    private LoadingWindow wechatWindow;

    private String weiXinUserName;
    private String weiXinImageUrl;
    private String weiXinUserId;

    /**
     * 微信第三方登陆接口
     */
    private String weixinLoginUrl = Constant.TOBOSU_URL + "tapp/passport/login_third_party";

    /**
     * 微信参数对象
     */
    private HashMap<String, Object> weixinLoginParams;

	
	/*-------------微信登陆相关------------*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_account, null);
        mContext = getActivity();
        initView(view);
        return view;
    }


    private void initView(View view) {
        umShareAPI = UMShareAPI.get(mContext);
        et_login_useraccount = (EditText) view.findViewById(R.id.et_login_useraccount);
        et_login_useraccount_password = (EditText) view.findViewById(R.id.et_login_useraccount_password);
        tv_accountlogin = (TextView) view.findViewById(R.id.tv_accountlogin);
        tv_accountlogin.setOnClickListener(this);
        ll_obtain_weixin_login_account = (LinearLayout) view.findViewById(R.id.ll_obtain_weixin_login_account);
        ll_obtain_weixin_login_account.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_accountlogin: // 登录
                userLogin();
                hideEdittext();
                break;
            case R.id.ll_obtain_weixin_login_account: // 微信登录
                loginWeixin();
                hideEdittext();
                break;
            default:
                break;
        }

    }


    private String encode_pass = "";

    /***
     * 用户注册 登录
     */
    private void userLogin() {

        // 账户登录页面
        String userName = et_login_useraccount.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(getActivity(), "账户信息不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        String passWord = et_login_useraccount_password.getText().toString().trim();
        encode_pass = MD5Util.md5(passWord);

        if (TextUtils.isEmpty(passWord)) {
            Toast.makeText(getActivity(), "密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        userLoginParams = AppInfoUtil.getPublicHashMapParams(getActivity());
        userLoginParams.put("mobile", userName);
        userLoginParams.put("chcode", AppInfoUtil.getChannType(MyApplication.getContext()));
        userLoginParams.put("pass", encode_pass);
        requestUserLogin();
    }


    /***
     * 用户登录接口请求
     */
    private void requestUserLogin() {
        if (!Constant.checkNetwork(getActivity())) {
            Constant.toastNetOut(getActivity());
            return;
        }

        OKHttpUtil.post(userLoginUrl, userLoginParams, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Log.e("账号登录", "====" + result);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("error_code") == 0) {
                                Log.e("登录日志", "====" + jsonObject);
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
     * 微信登录接口请求
     */
    private void loginWeixin() {
        wechatWindow = new LoadingWindow(getActivity());
//        UMWXHandler wxHandler = new UMWXHandler(getActivity(), "wx20c4f4560dcd397a", "9b06e848d40bcb04205d75335df6b814");
//        wxHandler.addToSocialSDK();
//        weixinThirdParty(SHARE_MEDIA.WEIXIN);

        //新的微信的登录
        umShareAPI.getPlatformInfo(getActivity(), SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                //授权成功 获取用户的相关信息
                weiXinImageUrl = map.get("iconurl");//微信的头像
                weiXinUserName = map.get("name");//微信的昵称
                weiXinUserId = map.get("openid");//微信的openid
                if (wechatWindow != null && wechatWindow.isShowing()) {
                    wechatWindow.dismiss();
                    wechatWindow = null;
                }
                requestWeixinLogin();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Log.e(TAG, "授权出错=====" + throwable.getMessage());
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Toast.makeText(MyApplication.getContext(), "登陆取消", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    /***
//     * 微信第三方登录
//     *
//     * @param platform
//     */
//    private void weixinThirdParty(final SHARE_MEDIA platform) {
//        mController.doOauthVerify(getActivity(), platform, new UMAuthListener() {
//
//            @Override
//            public void onStart(SHARE_MEDIA platform) {
//
//            }
//
//            @Override
//            public void onError(SocializeException e, SHARE_MEDIA platform) {
//
//            }
//
//            @Override
//            public void onComplete(Bundle value, SHARE_MEDIA platform) {
//                // 获取uid
//                if (!TextUtils.isEmpty(value.getString("uid"))) {
//                    getUserInfo(platform);
//                    //FIXME
//                    if (wechatWindow != null && wechatWindow.isShowing()) {
//                        wechatWindow.dismiss();
//                        wechatWindow = null;
//                    }
//                } else {
//                    Toast.makeText(MyApplication.getContext(), "登陆失败...", Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onCancel(SHARE_MEDIA platform) {
//                Toast.makeText(MyApplication.getContext(), "登陆取消", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    /***
     * 获取微信用户的信息
     *
     * @param platform
     */
//    private void getUserInfo(final SHARE_MEDIA platform) {
//        mController.getPlatformInfo(getActivity(), platform, new UMDataListener() {
//
//            @Override
//            public void onStart() {
//                Toast.makeText(getActivity(), "获取平台数据开始...", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onComplete(int status, Map<String, Object> info) {
//                if (status == 200 && info != null) {
//                    weiXinUserName = (String) info.get("nickname");
//                    weiXinImageUrl = (String) info.get("headimgurl");
//                    weiXinUserId = (String) info.get("unionid");
//                    requestWeixinLogin();
//                } else {
//                    Toast.makeText(getActivity(), "获取用户信息失败！", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//            }
//
//        });
//    }

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


        OKHttpUtil.post(weixinLoginUrl, weixinLoginParams, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
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
        long time = new Date().getTime();
        try {
            JSONObject data = jsonObject.getJSONObject("data");
            String icon = data.getString("icon");
            String nickname = data.getString("name");
            mark = data.getString("mark");
            token = data.getString("token");
            String userid = data.getString("uid");
            String cityname = data.getString("cityname");
            String cellphone = data.getString("cellphone");
            String typeId = data.getString("type_id");//登录用户的类型
            String expected_cost = data.getString("expected_cost");
            CacheManager.setDecorateBudget(getActivity(), expected_cost);


            SharedPreferences saveInfo = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE); // 登录成功后 存储用户标记mark
            SharedPreferences.Editor editor = saveInfo.edit();
            editor.putString("nickname", nickname);
            editor.putString("icon", icon);
            editor.putString("mark", mark);
            editor.putString("encode_pass", encode_pass);
            editor.putString("userid", userid);
            editor.putLong("login_time", time);
            editor.putString("token", token);
            editor.putString("cityname", cityname);
            editor.putString("cellphone", cellphone);
            editor.putString("", "");
            editor.putString("typeid", typeId);

            editor.commit();
            AppInfoUtil.setToken(getActivity(), token);


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("LoginFragmentAccount -- 解析错误--");
        }

        Intent favIntent = getActivity().getIntent();
        if (favIntent != null) {
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
        }

        et_login_useraccount_password.setText("");
        et_login_useraccount.setText("");
    }


    private void hideEdittext() {
        InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getActivity() != null && getActivity().getCurrentFocus() != null) {
            im.hideSoftInputFromWindow(getActivity().getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

}
