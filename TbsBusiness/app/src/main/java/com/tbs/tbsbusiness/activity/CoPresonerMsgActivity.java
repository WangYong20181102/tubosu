package com.tbs.tbsbusiness.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.base.BaseActivity;
import com.tbs.tbsbusiness.bean.EC;
import com.tbs.tbsbusiness.bean.Event;
import com.tbs.tbsbusiness.bean._User;
import com.tbs.tbsbusiness.config.Constant;
import com.tbs.tbsbusiness.customview.CustomDialog;
import com.tbs.tbsbusiness.util.EventBusUtil;
import com.tbs.tbsbusiness.util.OKHttpUtil;
import com.tbs.tbsbusiness.util.SpUtil;
import com.tbs.tbsbusiness.util.Util;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 装修公司端
 * 个人信息页
 */
public class CoPresonerMsgActivity extends BaseActivity {

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
    private _User mUser;
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
        param.put("uid", SpUtil.getUserId(mContext));
        param.put("device_id", SpUtil.getPushRegisterId(mContext));
        OKHttpUtil.post(Constant.GET_USER_INFO, param, new Callback() {
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
                        mUser = mGson.fromJson(data, _User.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //用户的昵称
                                coPreNickTv.setText("" + mUser.getNickname());
                                SpUtil.setNickname(mContext, mUser.getNickname());
                                //用户是否绑定手机
                                if (mUser.getCellphone_check().equals("0")) {
                                    //未绑定
                                    coPreBindPhone.setTextColor(Color.parseColor("#ff4529"));
                                    coPreBindPhone.setText("未绑定");
                                } else {
                                    //已绑定
                                    coPreBindPhone.setTextColor(Color.parseColor("#999999"));
                                    coPreBindPhone.setText("" + mUser.getCellphone());
                                }
                                //微信的绑定
                                if (mUser.getWechat_check().equals("0")) {
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
                    Log.e(TAG, "点击绑定微信");
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
                //极光推送下线
                jpushOffline();
                //清除本地用户信息
                getSharedPreferences("userInfo", 0).edit().clear().commit();
                dialog.cancel();
                //清除微信缓存
                umShareAPI.deleteOauth(CoPresonerMsgActivity.this, SHARE_MEDIA.WEIXIN, null);
                //友盟统计关闭
                MobclickAgent.onProfileSignOff();
                //将MainActivity销毁 各大主页销毁
                EventBusUtil.sendEvent(new Event(EC.EventCode.USER_LOGIN_OUT));
                //进入登录页面
                startActivity(new Intent(mContext, LoginActivity.class));
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

    //极光推送下线
    private void jpushOffline() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("device_id", SpUtil.getPushRegisterId(mContext));
        param.put("company_id", SpUtil.getCompany_id(mContext));
        OKHttpUtil.post(Constant.SMS_PUSH_OFFLINE, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败============" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "推送线下链接成功===============" + json);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JPushInterface.clearAllNotifications(mContext);
                    }
                });
            }
        });
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
                    String weiXinUserId = map.get("unionid");//微信的unionid
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
    private void HttpWeixinBind(String unionid, String nickname, String icon) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", SpUtil.getUserId(mContext));
        param.put("unionid", unionid);
        param.put("nickname", nickname);
        param.put("icon", icon);
        //参数输出
//        Iterator iterator = param.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry entry = (Map.Entry) iterator.next();
//            Log.e(TAG + "请求的参数:", "==key==" + entry.getKey() + "==val==" + entry.getValue());
//        }
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
