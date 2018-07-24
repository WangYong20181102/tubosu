package com.tbs.tbsbusiness.base;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.activity.CoPresonerMsgActivity;
import com.tbs.tbsbusiness.activity.LoginActivity;
import com.tbs.tbsbusiness.bean.Event;
import com.tbs.tbsbusiness.bean.HomeListener;
import com.tbs.tbsbusiness.config.Constant;
import com.tbs.tbsbusiness.config.MyApplication;
import com.tbs.tbsbusiness.util.AppManager;
import com.tbs.tbsbusiness.util.EventBusUtil;
import com.tbs.tbsbusiness.util.OKHttpUtil;
import com.tbs.tbsbusiness.util.SpUtil;
import com.tbs.tbsbusiness.util.Util;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Lin on 2018/6/2 10:39.
 */
public class BaseActivity extends AppCompatActivity {
    protected static String TAG = BaseActivity.class.getSimpleName();
    protected Context mContext;
    public boolean isForeground = true;//是否从后台唤起
    private HomeListener mHomeListener = null;
    private UMShareAPI umShareAPI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = this;
        if (isRegisterEventBus()) {
            EventBusUtil.register(this);
        }
        //监听物理按键
        mHomeListener = new HomeListener(mContext);
        mHomeListener.setInterface(keyFun);
        umShareAPI = UMShareAPI.get(mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHomeListener.startListen();
        MobclickAgent.onResume(this);
        AppManager.addActivity(this);
        if (SpUtil.getUserId(MyApplication.getContext()) != null
                && !TextUtils.isEmpty(SpUtil.getUserId(mContext))) {
            //用户信息不为空的时候进行信息校验
            if (!isForeground) {
                //从后台唤起时进行数据的校验
                Log.e(TAG, "用户的信息校验。。。。。");
                isForeground = true;
                checkUserInfoIsChange();
            }

        }
    }

    //用户的登录信息数据的校验
    private void checkUserInfoIsChange() {
        Log.e(TAG, "正在检测用户的信息----");

        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", SpUtil.getUserId(MyApplication.getContext()));
        param.put("account", SpUtil.getUserLoginAccount(MyApplication.getContext()));
        param.put("type", SpUtil.getUserLoginType(MyApplication.getContext()));
        OKHttpUtil.post(Constant.LOGIN_CHECK_INFO, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "检测用户信息是否修改链接失败======" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    Log.e(TAG, "检测用户信息修改================" + status);
                    if (status.equals("201")) {
                        //用户的信息已经修改
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(mContext, LoginActivity.class);
                                intent.putExtra("mWhereComeFrom", "BaseActivity");
                                startActivity(intent);
                                //清除已有的用户信息
                                cleanUserInfo();
                                //清除栈内除了Login的Activity
                                AppManager.finishOutLoginActivity();

                            }
                        });
                    } else if (status.equals("200")) {
                        //用户的信息没有修改
                        Log.e(TAG, "用户的信息未做修改~");
                    } else {
                        //参数错误
                        Log.e(TAG, "参数错误~");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void cleanUserInfo() {
        //极光推送下线
        jpushOffline();
        //清除微信缓存
        umShareAPI.deleteOauth(this, SHARE_MEDIA.WEIXIN, null);
        //友盟统计关闭
        MobclickAgent.onProfileSignOff();
        //清除本地用户信息
        getSharedPreferences("userInfo", 0).edit().clear().commit();
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
                        //清除已存在的推送通知
                        JPushInterface.clearAllNotifications(mContext);
                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHomeListener.stopListen();
        MobclickAgent.onPause(this);
    }

    /**
     * 是否注册事件分发
     *
     * @return true绑定EventBus事件分发，默认不绑定，子类需要绑定的话复写此方法返回true.
     */
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(Event event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyEventBusCome(Event event) {
        if (event != null) {
            receiveStickyEvent(event);
        }
    }

    /**
     * 接收到分发到事件
     *
     * @param event 事件
     */
    protected void receiveEvent(Event event) {

    }

    /**
     * 接受到分发的粘性事件
     *
     * @param event 粘性事件
     */
    protected void receiveStickyEvent(Event event) {

    }


    //物理按键的监听
    private HomeListener.KeyFun keyFun = new HomeListener.KeyFun() {
        @Override
        public void home() {
            Log.e(TAG, "按键监听_lin=======点击了home键");
            isForeground = false;
        }

        @Override
        public void recent() {
            Log.e(TAG, "按键监听_lin=======点击了任务键");
            isForeground = false;
        }

        @Override
        public void longHome() {
            Log.e(TAG, "按键监听_lin=======长按了home键");

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRegisterEventBus()) {
            EventBusUtil.unregister(this);
        }
    }

}
