package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.CustomDialog;
import com.tbs.tobosutype.customview.RoundImageView;
import com.tbs.tobosutype.customview.SelectCityDialog;
import com.tbs.tobosutype.customview.SelectCityDialog.Builder;
import com.tbs.tobosutype.customview.SelectSexPopupWindow;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.MyApplication;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.HttpServer;
import com.tbs.tobosutype.utils.Util;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;


import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * 业主个人资料信息页
 *
 * @author dec
 */
public class MyOwnerAccountManagerActivity extends Activity implements OnClickListener {
    private static final String TAG = MyOwnerAccountManagerActivity.class.getSimpleName();

    /**
     * 修改昵称
     */
    private static final int MODIFY_NICKNAME_REQUEST_CODE = 1;

    /**
     * 修改小区
     */
    private static final int MODIFY_COMMUNITY_REQUEST_CODE = 2;

    /**
     * 修改城市
     */
    private static final int MODIFY_CITY_REQUEST_CODE = 3;

    /**
     * 修改电话
     */
    private static final int BIND_CELLPHONE_NUM_REQUEST_CODE = 5;

    /**
     * 昵称参数标识
     * 1
     */
    private static final int FIELD_NICKNAME = 1;

    /**
     * 性别参数标识
     * 2
     */
    private static final int FIELD_GENDER = 2;

    /**
     * 城市参数标识
     * 3
     */
    private static final int FIELD_CITY = 3;

    /**
     * 小区参数标识
     * 4
     */
    private static final int FIELD_COMMUNITY = 4;

    /**
     * 头像参数标识
     * 5
     */
    private static final int FIELD_HEAD_PICTURE = 5;


    private Context mContext;

    private String nickname;
    private String icon;
    private String token;
    private String gender;
    private String cityname;
    private String wechatCheck;
    private String cellphone;
    private String province;
    private String icommunity;

    private TextView tv_nickname;
    private TextView tv_gender;
    private TextView tv_place;
    private TextView tv_icommunity;
    private TextView tv_cellphone_myowner_account_info;
    private TextView tv_weixin;

    private RelativeLayout rl_nickname;
    private RelativeLayout rl_gender;
    private RelativeLayout rl_place;
    private RelativeLayout rl_icommunity;
    private RelativeLayout rl_cellphone;
    private RelativeLayout rl_weixin;
    private RelativeLayout rl_banner;
    private RoundImageView iv_icon;
    private ImageView iv_back_myowner_personal_info;
    private TextView tv_btn_exit;

    private RequestParams changeInfoParams;
    private RequestParams bindThirdPartyParams;

    private Intent intent;
    private UMShareAPI umShareAPI;
    private String weiXinUserName;
    private String weiXinImageUrl;
    private String weiXinUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_myowner_account_manager);
        mContext = MyOwnerAccountManagerActivity.this;
        umShareAPI = UMShareAPI.get(mContext);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_gender = (TextView) findViewById(R.id.tv_gender_owner_info);
        tv_place = (TextView) findViewById(R.id.tv_place);
        tv_icommunity = (TextView) findViewById(R.id.tv_icommunity);
        tv_cellphone_myowner_account_info = (TextView) findViewById(R.id.tv_cellphone_myowner_account_info);
        tv_weixin = (TextView) findViewById(R.id.tv_weixin);
        rl_nickname = (RelativeLayout) findViewById(R.id.rl_nickname);
        rl_banner = (RelativeLayout) findViewById(R.id.rl_banner);
        rl_gender = (RelativeLayout) findViewById(R.id.rl_gender);
        rl_place = (RelativeLayout) findViewById(R.id.rl_place);
        rl_icommunity = (RelativeLayout) findViewById(R.id.rl_icommunity);
        rl_cellphone = (RelativeLayout) findViewById(R.id.rl_cellphone_myowner_personal_info);
        rl_weixin = (RelativeLayout) findViewById(R.id.rl_weixin);
        iv_icon = (RoundImageView) findViewById(R.id.iv_icon);
        iv_back_myowner_personal_info = (ImageView) findViewById(R.id.iv_back_myowner_personal_info);
        tv_btn_exit = (TextView) findViewById(R.id.tv_btn_exit);
        rl_banner.setBackgroundColor(Color.parseColor("#ff882e"));
    }

    private void initData() {
        Bundle bundle = getIntent().getBundleExtra("data");
        nickname = bundle.getString("nickname");
        tv_nickname.setText(nickname);
        icon = bundle.getString("icon");
        MyApplication.imageLoader.displayImage(icon, iv_icon, MyApplication.options);
        token = bundle.getString("token");
        gender = bundle.getString("gender");
        tv_gender.setText(gender);

        wechatCheck = bundle.getString("wechat_check");
        if (wechatCheck.equals("1")) {
            tv_weixin.setTextColor(getResources().getColor(R.color.color_neutralgrey));
            tv_weixin.setText("已绑定");
        } else {
            tv_weixin.setTextColor(getResources().getColor(R.color.color_red));
            tv_weixin.setText("未绑定");
        }

        cellphone = bundle.getString("cellphone");
        if (TextUtils.isEmpty(cellphone) || "未绑定".equals(cellphone)) {
            tv_cellphone_myowner_account_info.setTextColor(getResources().getColor(R.color.color_red));
            tv_cellphone_myowner_account_info.setText("未绑定");
        } else {
            tv_cellphone_myowner_account_info.setTextColor(getResources().getColor(R.color.color_neutralgrey));
            tv_cellphone_myowner_account_info.setText(cellphone);
        }

        cellphone = bundle.getString("cellphone");
        icommunity = bundle.getString("icommunity");
        tv_icommunity.setText(icommunity);
        province = bundle.getString("province");
        cityname = bundle.getString("cityname");
        tv_place.setText(province + " - " + cityname);
        intent = new Intent(mContext, ChangeInfoActivity.class);
    }

    private void initEvent() {
        iv_back_myowner_personal_info.setOnClickListener(this);
        tv_btn_exit.setOnClickListener(this);
        rl_cellphone.setOnClickListener(this);
        rl_gender.setOnClickListener(this);
        rl_icommunity.setOnClickListener(this);
        rl_nickname.setOnClickListener(this);
        rl_place.setOnClickListener(this);
        rl_weixin.setOnClickListener(this);
        rl_place.setOnClickListener(this);
        rl_gender.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_myowner_personal_info:
                finish();
                break;
            case R.id.rl_weixin:
                if (wechatCheck.equals("1")) {
                    Toast.makeText(mContext, "您已经绑定过了！", Toast.LENGTH_SHORT).show();
                } else {
//                    UMWXHandler wxHandler = new UMWXHandler(getApplicationContext(), "wx20c4f4560dcd397a", "9b06e848d40bcb04205d75335df6b814");
//                    wxHandler.addToSocialSDK();
//                    bindThirdParty(SHARE_MEDIA.WEIXIN);
                    //新的微信绑定
                    umShareAPI.getPlatformInfo(MyOwnerAccountManagerActivity.this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
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
                            Log.e(TAG, "微信绑定出错====" + throwable.getMessage());
                            Toast.makeText(mContext, "绑定出错~", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancel(SHARE_MEDIA share_media, int i) {
                            Toast.makeText(mContext, "取消绑定~", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            case R.id.tv_btn_exit:
                operExit();
                break;
            case R.id.rl_nickname:
                intent.putExtra("title", "修改昵称");
                intent.putExtra("textString", tv_nickname.getText() + "");
                startActivityForResult(intent, MODIFY_NICKNAME_REQUEST_CODE);
                break;
            case R.id.rl_icommunity:
                intent.putExtra("title", "修改小区");
                intent.putExtra("textString", tv_icommunity.getText() + "");
                startActivityForResult(intent, MODIFY_COMMUNITY_REQUEST_CODE);
                break;
            case R.id.rl_place:
                Builder builder = new SelectCityDialog.Builder(this);

                builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent selectCityIntent = new Intent(mContext, SelectCtiyActivity.class);
                        selectCityIntent.putExtra("isSelectCity", true); // FIXME 要检查
                        startActivityForResult(selectCityIntent, MODIFY_CITY_REQUEST_CODE);
                        dialog.cancel();
                    }
                });

                builder.create().show();

                break;
            case R.id.rl_gender:

                operSelectGender();
                break;
            case R.id.rl_cellphone_myowner_personal_info:
                Intent bindingPhoneIntent = new Intent(mContext, BindingPhoneActivity.class);
                bindingPhoneIntent.putExtra("token", token);
                startActivityForResult(bindingPhoneIntent, BIND_CELLPHONE_NUM_REQUEST_CODE);
                break;

            default:
                break;
        }
    }

    /***
     * 选择性别
     */
    private void operSelectGender() {
        final SelectSexPopupWindow popupWindow = new SelectSexPopupWindow(mContext);
        popupWindow.showAtLocation(rl_gender.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        popupWindow.tv_enter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean isMale = popupWindow.bt_male.isChecked();
                String modify_gender = "";
                int genderFlage = -1;
                if (isMale) {
                    modify_gender = "男";
                    genderFlage = 1;
                } else {
                    modify_gender = "女";
                    genderFlage = 0;
                }
                if (modify_gender.equals(tv_gender.getText())) {
                    Toast.makeText(mContext, "您还没修改!", Toast.LENGTH_SHORT).show();
                    return;
                }

                changeInfoParams = AppInfoUtil.getPublicParams(getApplicationContext());
                changeInfoParams.put("token", token);
                changeInfoParams.put("field", FIELD_GENDER);
                changeInfoParams.put("new", genderFlage);
                tv_gender.setText(modify_gender);
                popupWindow.dismiss();
                requestChangeInfo();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            changeInfoParams = AppInfoUtil.getPublicParams(getApplicationContext());
            changeInfoParams.put("token", token);
            switch (requestCode) {
                case MODIFY_NICKNAME_REQUEST_CODE:
                    changeInfoParams.put("field", FIELD_NICKNAME);
                    changeInfoParams.put("new", data.getStringExtra("result"));  //  修改后的昵称
                    tv_nickname.setText(data.getStringExtra("result"));
                    requestChangeInfo();
                    break;
                case MODIFY_COMMUNITY_REQUEST_CODE:
                    changeInfoParams.put("field", FIELD_COMMUNITY);
                    changeInfoParams.put("new", data.getStringExtra("result"));  //  修改后的小区
                    tv_icommunity.setText(data.getStringExtra("result"));
                    requestChangeInfo();
                    break;
                case MODIFY_CITY_REQUEST_CODE:
                    changeInfoParams.put("field", FIELD_CITY);
                    if (!TextUtils.isEmpty(data.getStringExtra("result"))) {
                        changeInfoParams.put("new", data.getStringExtra("result"));  //  修改后的城市
                        tv_place.setText(data.getStringExtra("result"));
                        requestChangeInfo();
                    }
                    break;
                case BIND_CELLPHONE_NUM_REQUEST_CODE:
                    tv_cellphone_myowner_account_info.setText(data.getStringExtra("result"));  //  修改后的电话号码
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * 修改信息接口
     */
    private void requestChangeInfo() {

        HttpServer.getInstance().requestPOST(Constant.USER_CHANGE_INFO_URL, changeInfoParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int code, Header[] arg1, byte[] result) {
                //FIXME
                try {
                    JSONObject jsonObject = new JSONObject(new String(result));
                    int error_code = jsonObject.getInt("error_code");
                    if (error_code == 0) {
                        Toast.makeText(mContext, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                Toast.makeText(mContext, "修改信息失败，请稍后重试...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void operExit() {

        CustomDialog.Builder builder = new CustomDialog.Builder(this);

        builder.setMessage("你确定退出账号吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
//                mController.deleteOauth(MyOwnerAccountManagerActivity.this, SHARE_MEDIA.WEIXIN, new SocializeListeners.SocializeClientListener() {
//                    @Override
//                    public void onStart() {
//
//                    }
//
//                    @Override
//                    public void onComplete(int i, SocializeEntity socializeEntity) {
//                        Util.setToast(mContext, "退出成功");
//                    }
//                });
//                Util.setErrorLog(TAG, "你确定删除了微信缓存了吧");
                getSharedPreferences("userInfo", 0).edit().clear().commit();
                AppInfoUtil.ISJUSTLOGIN = true;
                getSharedPreferences("userInfo", 0).edit().clear().commit();
                XGPushManager.unregisterPush(getApplicationContext());
                dialog.cancel();
                //清除微信的缓存
                umShareAPI.deleteOauth(MyOwnerAccountManagerActivity.this, SHARE_MEDIA.WEIXIN, null);
                MobclickAgent.onProfileSignOff();
// Intent i = new Intent();
//                i.setAction(Constant.LOGOUT_ACTION);
//                sendBroadcast(i);
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

//    /**
//     * 授权。如果授权成功，则获取用户信息 ---第三方登录
//     *
//     * @param platform
//     */
//    private void bindThirdParty(final SHARE_MEDIA platform) {
//
//        mController.doOauthVerify(mContext, platform, new UMAuthListener() {
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
//                // 获取uid
//                String uid = value.getString("uid");
//                if (!TextUtils.isEmpty(uid)) {
//                    getUserInfo(platform);
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
//
//    /**
//     * 获取用户信息
//     *
//     * @param platform
//     */
//    private void getUserInfo(final SHARE_MEDIA platform) {
//        mController.getPlatformInfo(mContext, platform, new UMDataListener() {
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

    private void operBindThirdParty() {
        bindThirdPartyParams = AppInfoUtil.getPublicParams(getApplicationContext());
        bindThirdPartyParams.put("token", token);
        bindThirdPartyParams.put("kind", "weixin");
        bindThirdPartyParams.put("icon", weiXinImageUrl);
        bindThirdPartyParams.put("nickname", weiXinUserName);
        bindThirdPartyParams.put("account", weiXinUserId);
        HttpServer.getInstance().requestPOST(Constant.BIND_THIRD_PARTY_URL, bindThirdPartyParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] body) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(body));
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

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

            }
        });
    }

}