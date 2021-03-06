package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.CustomDialog;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 装修公司账户信息页
 *
 * @author dec
 */
public class MyCompanyAccountManagerActivity extends Activity implements OnClickListener {
    private Context mContext;
    private String companyNickname;
    private String token;
    private String wechatCheck;
    private String cellphone;
    private String TAG;
    private TextView tv_companyinfo_nickname;
    private TextView tv_cellphone;
    private TextView tv_weixin;
    private RelativeLayout rl_nickname;
    private RelativeLayout rl_cellphone;
    private RelativeLayout rl_weixin;
    private RelativeLayout rl_banner;
    private ImageView company_user_iv_back;
    private TextView tv_btn_company_info_exit;

    /**
     * 第三方绑定接口
     */
    private String bindThirdPartyUrl = Constant.TOBOSU_URL + "tapp/passport/bindThirdParty";
    private UMShareAPI umShareAPI;



    private HashMap<String, Object> bindThirdPartyParams;


    private String weiXinUserName;
    private String weiXinImageUrl;
    private String weiXinUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_company_account_manager);
        mContext = MyCompanyAccountManagerActivity.this;
        umShareAPI = UMShareAPI.get(mContext);
        TAG = "MyCompanyAccountManagerActivity";
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        rl_banner = (RelativeLayout) findViewById(R.id.rl_banner);
        tv_companyinfo_nickname = (TextView) findViewById(R.id.tv_companyinfo_nickname);
        tv_cellphone = (TextView) findViewById(R.id.tv_cellphone);
        tv_weixin = (TextView) findViewById(R.id.tv_weixin);
        rl_nickname = (RelativeLayout) findViewById(R.id.rl_nickname);
        rl_cellphone = (RelativeLayout) findViewById(R.id.rl_cellphone);
        rl_weixin = (RelativeLayout) findViewById(R.id.rl_weixin);
        company_user_iv_back = (ImageView) findViewById(R.id.company_user_iv_back);
        tv_btn_company_info_exit = (TextView) findViewById(R.id.tv_btn_company_info_exit);
        rl_banner.setBackgroundColor(Color.parseColor("#ff882e"));
    }

    private void initData() {
        Bundle bundle = getIntent().getBundleExtra("data");
        companyNickname = bundle.getString("company_name");
        token = bundle.getString("token");
        wechatCheck = bundle.getString("wechat_check");
        cellphone = bundle.getString("cellphone");
        tv_companyinfo_nickname.setText(companyNickname);
        if ("1".equals(wechatCheck)) {
            tv_weixin.setTextColor(getResources().getColor(R.color.color_neutralgrey));
            tv_weixin.setText("已绑定");
        } else {
            tv_weixin.setTextColor(getResources().getColor(R.color.color_red));
            tv_weixin.setText("未绑定");
        }
        if (TextUtils.isEmpty(cellphone) || "未绑定".equals(cellphone)) {
            tv_cellphone.setTextColor(getResources().getColor(R.color.color_red));
            tv_cellphone.setText("未绑定");
        } else {
            tv_cellphone.setTextColor(getResources().getColor(R.color.color_neutralgrey));
            tv_cellphone.setText(cellphone);
        }
        cellphone = bundle.getString("cellphone");
    }

    private void initEvent() {
        company_user_iv_back.setOnClickListener(this);
        tv_btn_company_info_exit.setOnClickListener(this);
        rl_cellphone.setOnClickListener(this);
        rl_nickname.setOnClickListener(this);
        rl_weixin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.company_user_iv_back:
                finish();
                break;
            case R.id.rl_weixin:
                if ("1".equals(wechatCheck)) {
                    Toast.makeText(mContext, "您已经绑定过了！", Toast.LENGTH_SHORT).show();
                } else {
//                    UMWXHandler wxHandler = new UMWXHandler(MyCompanyAccountManagerActivity.this, "wx20c4f4560dcd397a", "9b06e848d40bcb04205d75335df6b814");
//                    wxHandler.addToSocialSDK();
//                    bindThirdParty(SHARE_MEDIA.WEIXIN);

                    //新的微信绑定
                    umShareAPI.getPlatformInfo(MyCompanyAccountManagerActivity.this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
                        @Override
                        public void onStart(SHARE_MEDIA share_media) {

                        }

                        @Override
                        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                            weiXinImageUrl = map.get("iconurl");//微信的头像
                            weiXinUserName = map.get("name");//微信的昵称
                            weiXinUserId = map.get("openid");//微信的openid
                            operBindThirdParty();
                        }

                        @Override
                        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                            Log.e(TAG, "授权出错=======" + throwable.getMessage());
                            Toast.makeText(mContext, "授权出错", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancel(SHARE_MEDIA share_media, int i) {
                            Toast.makeText(mContext, "取消授权", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            case R.id.tv_btn_company_info_exit:
                operExit();
                break;
            case R.id.rl_cellphone:
                Intent bindingPhoneIntent = new Intent(mContext, BindingPhoneActivity.class);
                bindingPhoneIntent.putExtra("token", token);
                startActivityForResult(bindingPhoneIntent, 5);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (requestCode) {
                case 5:
                    tv_cellphone.setTextColor(getResources().getColor(R.color.color_neutralgrey));
                    tv_cellphone.setText(data.getStringExtra("result"));
                    break;

                default:
                    break;
            }
        }
    }

    private void operExit() {

        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage("你确定退出账号吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                getSharedPreferences("userInfo", 0).edit().clear().commit();
                AppInfoUtil.ISJUSTLOGIN = true;
                getSharedPreferences("userInfo", 0).edit().clear().commit();
                dialog.cancel();
                //退出之后清除微信相关的信息
                umShareAPI.deleteOauth(MyCompanyAccountManagerActivity.this, SHARE_MEDIA.WEIXIN, null);
//                Intent i = new Intent();
//                i.setAction(Constant.LOGOUT_ACTION);
//                sendBroadcast(i);
                MobclickAgent.onProfileSignOff();
                finish();
            }
        })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create().show();

    }

//    /***
//     *  授权
//     * @param platform
//     */
//    private void bindThirdParty(final SHARE_MEDIA platform) {
//        mController.doOauthVerify(MyCompanyAccountManagerActivity.this, platform, new UMAuthListener() {
//
//            @Override
//            public void onStart(SHARE_MEDIA platform) {
//                Toast.makeText(mContext, "授权开始", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(SocializeException e, SHARE_MEDIA platform) {
//                Toast.makeText(mContext, "授权失败", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onComplete(Bundle value, SHARE_MEDIA platform) {
//                String uid = value.getString("uid");
//                if (!TextUtils.isEmpty(uid)) {
//                    getUserInfo(platform);
//
//                } else {
//                    Toast.makeText(mContext, "绑定失败...", Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onCancel(SHARE_MEDIA platform) {
//                Toast.makeText(mContext, "绑定取消", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    /**
//     * 获取用户数据
//     *
//     * @param platform
//     */
//    private void getUserInfo(final SHARE_MEDIA platform) {
//        mController.getPlatformInfo(MyCompanyAccountManagerActivity.this, platform, new UMDataListener() {
//
//            @Override
//            public void onStart() {
//                Toast.makeText(mContext, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onComplete(int status, Map<String, Object> info) {
//                if (status == 200 && info != null) {
//                    weiXinUserName = (String) info.get("nickname");
//                    weiXinImageUrl = (String) info.get("headimgurl");
//                    weiXinUserId = (String) info.get("unionid");
//                    operBindThirdParty();
//                } else {
//                    Toast.makeText(mContext, "获取用户信息失败！", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//            }
//
//        });
//    }

    /***
     * 第三方绑定接口的请求方法
     */
    private void operBindThirdParty() {
        bindThirdPartyParams = AppInfoUtil.getPublicHashMapParams(getApplicationContext());
        bindThirdPartyParams.put("token", token);
        bindThirdPartyParams.put("kind", "weixin");
        bindThirdPartyParams.put("icon", weiXinImageUrl);
        bindThirdPartyParams.put("nickname", weiXinUserName);
        bindThirdPartyParams.put("account", weiXinUserId);
        OKHttpUtil.post(bindThirdPartyUrl, bindThirdPartyParams, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            Toast.makeText(mContext, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            if (jsonObject.getInt("error_code") == 0) {
                                tv_weixin.setTextColor(getResources().getColor(R.color.color_neutralgrey));
                                tv_weixin.setText("已绑定");
                            } else {
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

}