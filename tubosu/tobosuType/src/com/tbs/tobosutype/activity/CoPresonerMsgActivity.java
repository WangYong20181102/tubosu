package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.base.*;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.bean._PresonerInfo;
import com.tbs.tobosutype.customview.CustomDialog;
import com.tbs.tobosutype.global.Constant;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 装修公司端
 * 个人信息页  3.7版本新增
 */
public class CoPresonerMsgActivity extends com.tbs.tobosutype.base.BaseActivity {
    @BindView(R.id.co_pre_phone_num_back)
    RelativeLayout coPrePhoneNumBack;
    @BindView(R.id.co_pre_bind_phone)
    TextView coPreBindPhone;
    @BindView(R.id.co_pre_bind_phone_rl)
    RelativeLayout coPreBindPhoneRl;
    @BindView(R.id.co_pre_bind_wechat)
    TextView coPreBindWechat;
    @BindView(R.id.co_pre_bind_wechat_rl)
    RelativeLayout coPreBindWechatRl;
    @BindView(R.id.co_pre_login_out)
    TextView coPreLoginOut;
    @BindView(R.id.banner_dever)
    View bannerDever;
    @BindView(R.id.co_pre_nick_tv)
    TextView coPreNickTv;
    private Context mContext;
    private String TAG = "CoPresonerMsgActivity";
    private UMShareAPI umShareAPI;
    private _PresonerInfo presonerInfo;
    private Gson mGson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_presoner_msg);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.BAND_PHONE_SUCCESS:
                //绑定手机号码成功
                coPreBindPhone.setText("已绑定");
                coPreBindPhone.setTextColor(Color.parseColor("#999999"));
                AppInfoUtil.setUserCellphone_check(mContext, "1");
                break;
        }
    }

    private void initViewEvent() {
        umShareAPI = UMShareAPI.get(mContext);
        mGson = new Gson();
        HttpGetUserMsg();
    }

    //获取用户的信息
    private void HttpGetUserMsg() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", AppInfoUtil.getUserid(mContext));
        OKHttpUtil.post(Constant.USER_INFO, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "链接服务器失败~", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功=======" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    String data = jsonObject.optString("data");
                    if (status.equals("200")) {
                        //请求回来的数据正确
                        presonerInfo = mGson.fromJson(data, _PresonerInfo.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //用户的头像
                                AppInfoUtil.setUserIcon(mContext, presonerInfo.getIcon());//重置信息
                                //用户的昵称
                                coPreNickTv.setText("" + presonerInfo.getNickname());
                                AppInfoUtil.setUserNickname(mContext, presonerInfo.getNickname());
                                //用户是否绑定手机
                                if (presonerInfo.getCellphone_check().equals("0")) {
                                    //未绑定
                                    coPreBindPhone.setTextColor(Color.parseColor("#ff4529"));
                                    coPreBindPhone.setText("未绑定");
                                } else {
                                    //已绑定
                                    coPreBindPhone.setTextColor(Color.parseColor("#999999"));
                                    coPreBindPhone.setText("" + presonerInfo.getCellphone());
                                }
                                //微信的绑定
                                if (presonerInfo.getWechat_check().equals("0")) {
                                    //未绑定
                                    coPreBindWechat.setTextColor(Color.parseColor("#ff4529"));
                                    coPreBindWechat.setText("未绑定");
                                } else {
                                    //已绑定
                                    coPreBindWechat.setTextColor(Color.parseColor("#999999"));
                                    coPreBindWechat.setText("已绑定");
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @OnClick({R.id.co_pre_phone_num_back, R.id.co_pre_bind_phone_rl, R.id.co_pre_bind_wechat_rl, R.id.co_pre_login_out})
    public void onViewClickedInCoPresonerMsgActivity(View view) {
        switch (view.getId()) {
            case R.id.co_pre_phone_num_back:
                finish();
                break;
            case R.id.co_pre_bind_phone_rl:
                //绑定手机号码
                if (coPreBindPhone.getText().toString().equals("未绑定")) {
                    //手机未绑定的情况下进行手机的绑定
                    //进入绑定
                    startActivity(new Intent(mContext, BandPhoneActivity.class));
                } else {
                    Toast.makeText(mContext, "您已绑定手机", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.co_pre_bind_wechat_rl:
                //绑定微信
                if (coPreBindWechat.getText().toString().equals("未绑定")) {
                    bindWeChat();
                } else {
                    Toast.makeText(mContext, "您已绑定微信", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.co_pre_login_out:
                userLoginOut();
                break;
        }
    }

    //用户退出 清空本地用户存储的信息
    private void userLoginOut() {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);

        builder.setMessage("你确定退出账号吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                AppInfoUtil.ISJUSTLOGIN = true;
                getSharedPreferences("userInfo", 0).edit().clear().commit();
                dialog.cancel();
                umShareAPI.deleteOauth(CoPresonerMsgActivity.this, SHARE_MEDIA.WEIXIN, null);
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

    //绑定微信
    private void bindWeChat() {
        if (coPreBindWechat.getText().toString().equals("未绑定")) {
            //进行微信的绑定
            umShareAPI.getPlatformInfo(CoPresonerMsgActivity.this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
                @Override
                public void onStart(SHARE_MEDIA share_media) {

                }

                @Override
                public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                    String weiXinUserName = map.get("name");//微信的昵称
                    String weiXinImageUrl = map.get("iconurl");//微信的头像
                    String weiXinUserId = map.get("openid");//微信的openid
                    HttpWeixinBind(weiXinUserId, weiXinUserName, weiXinImageUrl);
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
        } else {
            //微信已经绑定
            Toast.makeText(mContext, "您已绑定手机", Toast.LENGTH_SHORT).show();
        }
    }

    //绑定微信
    private void HttpWeixinBind(String openid, String nickname, String icon) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", AppInfoUtil.getUserid(mContext));
        param.put("openid", openid);
        param.put("nickname", nickname);
        param.put("icon", icon);
        OKHttpUtil.post(Constant.BIND_WE_CHAT, param, new Callback() {
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
                Log.e(TAG, "链接成功=============" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString(json);
                    final String msg = jsonObject.optString("msg");
                    if (status.equals("200")) {
                        //微信绑定成功 修改绑定的状态
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                coPreBindWechat.setText("已绑定");
                                coPreBindWechat.setTextColor(Color.parseColor("#999999"));
                            }
                        });
                    } else if (status.equals("0")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
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
