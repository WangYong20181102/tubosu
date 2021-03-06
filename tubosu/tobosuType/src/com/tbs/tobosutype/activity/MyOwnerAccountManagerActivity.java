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
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.base.*;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.customview.CustomDialog;
import com.tbs.tobosutype.customview.RoundImageView;
import com.tbs.tobosutype.customview.SelectSexPopupWindow;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.MyApplication;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.Util;
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
 * 业主个人资料信息页
 *
 * @author dec
 */
public class MyOwnerAccountManagerActivity extends com.tbs.tobosutype.base.BaseActivity implements OnClickListener {
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

    private HashMap<String, Object> changeInfoParams;
    private HashMap<String, Object> bindThirdPartyParams;

    private Intent intent;

    private String weiXinUserName;
    private String weiXinImageUrl;
    private String weiXinUserId;
    private UMShareAPI umShareAPI;


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

        changeInfoParams = AppInfoUtil.getPublicHashMapParams(getApplicationContext());
        bindThirdPartyParams = AppInfoUtil.getPublicHashMapParams(getApplicationContext());


        Bundle bundle = getIntent().getBundleExtra("data");
        nickname = bundle.getString("nickname");
        tv_nickname.setText(nickname);
        icon = bundle.getString("icon");
        MyApplication.imageLoader.displayImage(icon, iv_icon, MyApplication.options);
        token = bundle.getString("token");
        gender = bundle.getString("gender");
        tv_gender.setText(gender);

        wechatCheck = bundle.getString("wechat_check");
        if(wechatCheck!=null){
            if (wechatCheck.equals("1")) {
                tv_weixin.setTextColor(getResources().getColor(R.color.color_neutralgrey));
                tv_weixin.setText("已绑定");
            } else {
                tv_weixin.setTextColor(getResources().getColor(R.color.color_red));
                tv_weixin.setText("未绑定");
            }
        }else {
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
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_myowner_personal_info:
                finish();
                break;
            case R.id.rl_weixin:

                if(wechatCheck!=null){
                    if (wechatCheck.equals("1")) {
                        Toast.makeText(mContext, "您已经绑定过了！", Toast.LENGTH_SHORT).show();
                    } else {
                        //新的微信绑定
                        umShareAPI.getPlatformInfo(MyOwnerAccountManagerActivity.this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
                            @Override
                            public void onStart(SHARE_MEDIA share_media) {

                            }

                            @Override
                            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                                weiXinUserName = map.get("name");//微信的昵称
                                weiXinImageUrl = map.get("iconurl");//微信的头像
                                weiXinUserId = map.get("openid");//微信的openid
                                operBindThirdParty();
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
                // 修改城市
                Intent selectCityIntent = new Intent(mContext, SelectCtiyActivity.class);
                Bundle b = new Bundle();
                b.putString("fromMyOwenerAccountManager", "464");
                selectCityIntent.putExtra("accountBundle", b);
                startActivityForResult(selectCityIntent, MODIFY_CITY_REQUEST_CODE);
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


                changeInfoParams.put("token", token);
                changeInfoParams.put("field", FIELD_GENDER + "");
                changeInfoParams.put("new", genderFlage + "");
                tv_gender.setText(modify_gender);
                popupWindow.dismiss();
                requestChangeInfo();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            changeInfoParams = AppInfoUtil.getPublicHashMapParams(getApplicationContext());
            changeInfoParams.put("token", token);
            switch (requestCode) {
                case MODIFY_NICKNAME_REQUEST_CODE:
                    changeInfoParams.put("field", FIELD_NICKNAME + "");
                    changeInfoParams.put("new", data.getStringExtra("result"));  //  修改后的昵称
                    tv_nickname.setText(data.getStringExtra("result"));
                    requestChangeInfo();
                    break;
                case MODIFY_COMMUNITY_REQUEST_CODE:
                    changeInfoParams.put("field", FIELD_COMMUNITY + "");
                    changeInfoParams.put("new", data.getStringExtra("result"));  //  修改后的小区
                    tv_icommunity.setText(data.getStringExtra("result"));
                    requestChangeInfo();
                    break;
                case BIND_CELLPHONE_NUM_REQUEST_CODE:
                    tv_cellphone_myowner_account_info.setText(data.getStringExtra("result"));  //  修改后的电话号码
                    break;

                default:
                    break;
            }
        }
    }


    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()){
            case EC.EventCode.SELECT_CITY_CODE:
                String city = (String) event.getData();
                changeInfoParams.put("field", FIELD_CITY + "");
                Util.setErrorLog(TAG, "找到城市===>>" + city);
                if (!TextUtils.isEmpty(city)) {
                    changeInfoParams.put("new", city);  // 修改后的城市
                    tv_place.setText(city);
                    requestChangeInfo();
                }
                break;
        }
    }

    /**
     * 修改信息接口
     */
    private void requestChangeInfo() {
        changeInfoParams.put("token", token);
        Util.setErrorLog(TAG, changeInfoParams.toString());
        OKHttpUtil.post(Constant.USER_CHANGE_INFO_URL, changeInfoParams, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "修改信息失败，请稍后重试...", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
                Util.setErrorLog(TAG, json);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            int error_code = jsonObject.getInt("error_code");
                            if (error_code == 0) {
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

    private void operExit() {

        CustomDialog.Builder builder = new CustomDialog.Builder(this);

        builder.setMessage("你确定退出账号吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                getSharedPreferences("userInfo", 0).edit().clear().commit();
                AppInfoUtil.ISJUSTLOGIN = true;
                getSharedPreferences("userInfo", 0).edit().clear().commit();
                dialog.cancel();
                umShareAPI.deleteOauth(MyOwnerAccountManagerActivity.this, SHARE_MEDIA.WEIXIN, null);
//                Intent i = new Intent();
//                i.setAction(Constant.LOGOUT_ACTION);
//                sendBroadcast(i);
                MobclickAgent.onProfileSignOff();
                finish();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create().show();

    }


    private void operBindThirdParty() {

        bindThirdPartyParams.put("token", token);
        bindThirdPartyParams.put("kind", "weixin");
        bindThirdPartyParams.put("icon", weiXinImageUrl);
        bindThirdPartyParams.put("nickname", weiXinUserName);
        bindThirdPartyParams.put("account", weiXinUserId);

        OKHttpUtil.post(Constant.BIND_THIRD_PARTY_URL, bindThirdPartyParams, new Callback() {
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