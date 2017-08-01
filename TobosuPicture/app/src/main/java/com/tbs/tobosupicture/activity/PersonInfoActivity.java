package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean._PersonInfo;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.utils.HttpUtils;
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
 * 个人信息页面
 * 1--从注册成功页面跳转过来的 底部的按钮变成“确定”
 * 2--登录后点击头像进入的页面 底部的按钮变成“退出登录”
 */
public class PersonInfoActivity extends BaseActivity {

    @BindView(R.id.person_info_back)
    LinearLayout personInfoBack;
    @BindView(R.id.person_info_icon)
    ImageView personInfoIcon;
    @BindView(R.id.person_info_icon_rl)
    RelativeLayout personInfoIconRl;
    @BindView(R.id.person_info_nick_rl)
    RelativeLayout personInfoNickRl;
    @BindView(R.id.person_info_sex)
    TextView personInfoSex;
    @BindView(R.id.person_info_sex_rl)
    RelativeLayout personInfoSexRl;
    @BindView(R.id.person_info_city)
    TextView personInfoCity;
    @BindView(R.id.person_info_city_rl)
    RelativeLayout personInfoCityRl;
    @BindView(R.id.person_info_sign)
    TextView personInfoSign;
    @BindView(R.id.person_info_sign_rl)
    RelativeLayout personInfoSignRl;
    @BindView(R.id.person_info_bind_wx)
    TextView personInfoBindWx;
    @BindView(R.id.person_info_bind_wx_rl)
    RelativeLayout personInfoBindWxRl;
    @BindView(R.id.person_info_bind_phone)
    TextView personInfoBindPhone;
    @BindView(R.id.person_info_bind_phone_rl)
    RelativeLayout personInfoBindPhoneRl;
    @BindView(R.id.person_info_btn)
    TextView personInfoBtn;
    @BindView(R.id.person_info_btn_rl)
    RelativeLayout personInfoBtnRl;
    @BindView(R.id.person_info_nick)
    TextView personInfoNick;

    private Context mContext;
    private String TAG = "PersonInfoActivity";
    private Gson mGson;
    private Intent mIntent;
    private _PersonInfo personInfo;
    //TODO 从上一个界面传来的 标识是从那个Activity来的
    private String whereFrom = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
        HttpGetPersonInfo();
    }

    private void initViewEvent() {
        mIntent = getIntent();
        mGson = new Gson();
        whereFrom = mIntent.getStringExtra("from");
        if (whereFrom.equals("RegisterActivity")) {
            //从注册进来
            personInfoBtn.setText("确定");
            personInfoCity.setText(SpUtils.getLocationCity(mContext));
        } else if (whereFrom.equals("MineFragment")) {
            //从“我的”进来
            personInfoBtn.setText("退出登录");
        }
    }

    @OnClick({R.id.person_info_back, R.id.person_info_icon_rl, R.id.person_info_nick_rl,
            R.id.person_info_sex_rl, R.id.person_info_city_rl, R.id.person_info_sign_rl, R.id.person_info_bind_wx_rl, R.id.person_info_bind_phone_rl, R.id.person_info_btn_rl})
    public void onViewClickedInPersonInfoActivity(View view) {
        switch (view.getId()) {
            case R.id.person_info_back:
                finish();
                break;
            case R.id.person_info_icon_rl:
                //点击更换头像
                break;
            case R.id.person_info_nick_rl:
                //点击更换昵称
                break;
            case R.id.person_info_sex_rl:
                //点击更换性别
                break;
            case R.id.person_info_city_rl:
                //点击更换城市
                break;
            case R.id.person_info_sign_rl:
                //点击更换签名
                break;
            case R.id.person_info_bind_wx_rl:
                //点击进行微信绑定
                break;
            case R.id.person_info_bind_phone_rl:
                //点击进行手机号码的绑定
                break;
            case R.id.person_info_btn_rl:
                //底部按钮事件
                if (personInfoBtn.getText().equals("退出登录")) {
                    //清除用户数据
                    SpUtils.saveUserNick(mContext, "");
                    SpUtils.saveUserIcon(mContext, "");
                    SpUtils.saveUserPersonalSignature(mContext, "");
                    SpUtils.saveUserType(mContext, "");
                    SpUtils.saveUserUid(mContext, "");
                    finish();
                } else {
                    //注册之后的确定按钮
                    finish();
                }
                break;
        }
    }

    //请求个人信息数据
    private void HttpGetPersonInfo() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        HttpUtils.doPost(UrlConstans.PERSONAL_INFO, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败====" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功====" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //获取数据成功
                        String data = jsonObject.getString("data");
                        personInfo = mGson.fromJson(data, _PersonInfo.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initPersonInfo(personInfo);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initPersonInfo(_PersonInfo personInfo) {
        //设置头像
        GlideUtils.glideLoader(mContext, personInfo.getIcon(), R.mipmap.default_icon,
                R.mipmap.default_icon, personInfoIcon, 0);
        //设置昵称
        personInfoNick.setText(personInfo.getNick());
        //设置性别
        if(personInfo.getGender().equals("1")){
            //男
            personInfoSex.setText("男");
        }else {
            personInfoSex.setText("女");
        }

        //设置城市
        personInfoCity.setText(personInfo.getCity_name());
        //设置个性签名
        personInfoSign.setText(personInfo.getPersonal_signature());
        //设置绑定状态
        if (personInfo.getWechat_check().equals("1")) {
            personInfoBindWx.setText("已绑定");
        } else {
            personInfoBindWx.setTextColor(Color.parseColor("#f57a7a"));
            personInfoBindWx.setText("未绑定");
        }
        //设置手机号码绑定状态
        if (personInfo.getCellphone_check().equals("1")) {
            personInfoBindPhone.setText("已绑定");
        } else {
            personInfoBindPhone.setText("未绑定");
            personInfoBindPhone.setTextColor(Color.parseColor("#f57a7a"));
        }
    }
}
