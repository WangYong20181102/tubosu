package com.tbs.tbsbusiness.activity;

import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.NotificationBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener;
import com.allenliu.versionchecklib.v2.callback.ForceUpdateListener;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.adapter.UpdateDialogAdapter;
import com.tbs.tbsbusiness.base.BaseDialog;
import com.tbs.tbsbusiness.base.BaseTabActivity;
import com.tbs.tbsbusiness.bean.EC;
import com.tbs.tbsbusiness.bean.Event;
import com.tbs.tbsbusiness.bean._AppConfig;
import com.tbs.tbsbusiness.bean._UpdateInfo;
import com.tbs.tbsbusiness.config.Constant;
import com.tbs.tbsbusiness.util.OKHttpUtil;
import com.tbs.tbsbusiness.util.SpUtil;
import com.tbs.tbsbusiness.util.Util;

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

public class MainActivity extends BaseTabActivity {

    //    @BindView(R.id.main_tab_content_fl)
//    FrameLayout mainTabContentFl;
    @BindView(R.id.main_tab_dingdang_iv)
    ImageView mainTabDingdangIv;
    @BindView(R.id.main_tab_dingdang_tv)
    TextView mainTabDingdangTv;
    @BindView(R.id.main_tab_dingdang_rl)
    RelativeLayout mainTabDingdangRl;
    @BindView(R.id.main_tab_message_iv)
    ImageView mainTabMessageIv;
    @BindView(R.id.main_tab_message_tv)
    TextView mainTabMessageTv;
    @BindView(R.id.main_tab_message_rl)
    RelativeLayout mainTabMessageRl;
    @BindView(R.id.main_tab_mine_iv)
    ImageView mainTabMineIv;
    @BindView(R.id.main_tab_mine_tv)
    TextView mainTabMineTv;
    @BindView(R.id.main_tab_mine_rl)
    RelativeLayout mainTabMineRl;
    private Context mContext;
    private String TAG = "MainActivity";
    private Gson mGson;
    private _UpdateInfo mUpdateInfo;
    private static TabHost mTabHost;
    private DownloadBuilder builder;
    private Intent mIntent;
    private String mSelect_tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        //初始化基本事务
        initEvent();
        //获取App配置信息
        getAppConfigOnNet();
        //检测更新
        checkAppUpdata();
        //用户推送上线
        Util.initPushEventPushOnline(mContext,TAG);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.USER_LOGIN_OUT:
                Log.e(TAG, "MainActivity退出====");
                finish();
                break;
        }
    }

    //检测App是否有更新
    private void checkAppUpdata() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("system_plat", "1");
        param.put("chcode", Util.getChannType(mContext));
        param.put("version", Util.getAppVersionName(mContext));
        Log.e(TAG, "检测用户的更新数据=====参数=====chcode====" + Util.getChannType(mContext) + "=====version=====" + Util.getAppVersionName(mContext));
        OKHttpUtil.post(Constant.CHECK_APP_IS_UPDATA, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "数据获取失败===============" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "检测用户的更新数据==========" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        //数据获取成功
                        String data = jsonObject.optString("data");
                        mUpdateInfo = mGson.fromJson(data, _UpdateInfo.class);
                        if (mUpdateInfo.getIs_update().equals("1")) {
                            //有更新的情况
                            if (mUpdateInfo.getType().equals("2")) {
                                //非强制更新的情况下不设置更新的flag
                                SpUtil.setIsShowUpdataDialog(mContext, "showing");
                            } else {
                                SpUtil.setIsShowUpdataDialog(mContext, "");
                            }
                            //有更新
                            if (Util.isWifiConnected(mContext)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showUpdateDialog(mUpdateInfo);
                                    }
                                });
                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    return;
                                }
                            });
                        }
                    } else {
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }
        });
    }

    //弹窗更新
    //显示更新弹窗
    private void showUpdateDialog(final _UpdateInfo updateInfo) {
        builder = AllenVersionChecker.getInstance().downloadOnly(UIData.create()
                .setDownloadUrl(updateInfo.getApk_url()));
        builder.setCustomVersionDialogListener(new CustomVersionDialogListener() {
            @Override
            public Dialog getCustomVersionDialog(Context context, UIData versionBundle) {
                BaseDialog baseDialog = new BaseDialog(context, R.style.BaseDialog, R.layout.dialog_updata);
                RecyclerView update_dialog_msg = baseDialog.findViewById(R.id.update_dialog_msg);
                TextView versionchecklib_version_dialog_cancel = baseDialog.findViewById(R.id.versionchecklib_version_dialog_cancel);
                if (updateInfo.getType().equals("1")) {
                    versionchecklib_version_dialog_cancel.setText("退出");
                }
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                UpdateDialogAdapter updateDialogAdapter = new UpdateDialogAdapter(mContext, updateInfo.getContent());
                update_dialog_msg.setLayoutManager(linearLayoutManager);
                update_dialog_msg.setAdapter(updateDialogAdapter);
                updateDialogAdapter.notifyDataSetChanged();
                return baseDialog;
            }
        });
        //设置是否得强制更新
        if (updateInfo.getType().equals("1")) {
            builder.setForceUpdateListener(new ForceUpdateListener() {
                @Override
                public void onShouldForceUpdate() {
                    finish();
                    System.exit(0);
                }
            });
        }
        builder.setShowDownloadingDialog(false);
        builder.setNotificationBuilder(NotificationBuilder.create().setRingtone(true)
                .setIcon(R.mipmap.ic_launcher).setContentTitle("土拨鼠商家版").setContentText("正在下载最新土拨鼠商家版安装包..."));
        builder.excuteMission(mContext);
    }


    //获取App配置信息
    private void getAppConfigOnNet() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("type", "1");
        OKHttpUtil.post(Constant.GET_CONFIG, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "获取配置信息失败==========" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "获取App配置信息=================" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String data = jsonObject.optString("data");
                    _AppConfig mAppConfig = mGson.fromJson(data, _AppConfig.class);
                    //存储App配置信息
                    SpUtil.setCustom_service_tel(mContext, mAppConfig.getCustom_service_tel());//电话
                    SpUtil.setCustom_service_qq(mContext, mAppConfig.getCustom_service_qq());//QQ
                    SpUtil.setApplets_name(mContext, mAppConfig.getApplets_name());//土拨鼠查单小程序
                    SpUtil.setPublic_number(mContext, mAppConfig.getOfficial_accounts());//土拨鼠微信公众号
                    SpUtil.setCellphonePartern(mContext, mAppConfig.getCellphone_partern());//存储手机号的正则
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initEvent() {
        mGson = new Gson();
        mTabHost = this.getTabHost();
        TabHost.TabSpec spec;
        //订单页面
        Intent mIntent = new Intent(mContext, OrderActivity.class);
        spec = mTabHost.newTabSpec("one").setIndicator("订单").setContent(mIntent);
        mTabHost.addTab(spec);
        //消息页面
        mIntent = new Intent(mContext, MessageActivity.class);
        spec = mTabHost.newTabSpec("two").setIndicator("消息").setContent(mIntent);
        mTabHost.addTab(spec);
        //我的界面
        mIntent = new Intent(mContext, MineActivity.class);
        spec = mTabHost.newTabSpec("three").setIndicator("我的").setContent(mIntent);
        mTabHost.addTab(spec);
        //初始选择
        selectTab(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIntent = getIntent();
        mSelect_tab = mIntent.getStringExtra("select_tab");
        Log.e(TAG, "select_tab==========" + mSelect_tab);
//        selectTab(mSelect_tab);
    }

    private void selectTab(int position) {
        switch (position) {
            case 0:
                //订单
                mTabHost.setCurrentTab(position);
                mTabHost.setCurrentTabByTag("one");
                tabIconChange(position);
                break;
            case 1:
                //消息
                mTabHost.setCurrentTab(position);
                mTabHost.setCurrentTabByTag("two");
                tabIconChange(position);
                break;
            case 2:
                //我的
                mTabHost.setCurrentTab(position);
                mTabHost.setCurrentTabByTag("three");
                tabIconChange(position);
                break;
        }
    }

    private void tabIconChange(int position) {
        switch (position) {
            case 0:
                //按钮的的变化
                Glide.with(mContext).load(R.drawable.tab_dingdang_click).asBitmap().into(mainTabDingdangIv);
                Glide.with(mContext).load(R.drawable.tab_message).asBitmap().into(mainTabMessageIv);
                Glide.with(mContext).load(R.drawable.tab_mine).asBitmap().into(mainTabMineIv);
                //文字变化
                mainTabDingdangTv.setTextColor(Color.parseColor("#ff6c20"));
                mainTabMessageTv.setTextColor(Color.parseColor("#999999"));
                mainTabMineTv.setTextColor(Color.parseColor("#999999"));
                break;
            case 1:
                //按钮的的变化
                Glide.with(mContext).load(R.drawable.tab_dingdang).asBitmap().into(mainTabDingdangIv);
                Glide.with(mContext).load(R.drawable.tab_message_click).asBitmap().into(mainTabMessageIv);
                Glide.with(mContext).load(R.drawable.tab_mine).asBitmap().into(mainTabMineIv);
                //文字变化
                mainTabDingdangTv.setTextColor(Color.parseColor("#999999"));
                mainTabMessageTv.setTextColor(Color.parseColor("#ff6c20"));
                mainTabMineTv.setTextColor(Color.parseColor("#999999"));
                break;
            case 2:
                //按钮的的变化
                Glide.with(mContext).load(R.drawable.tab_dingdang).asBitmap().into(mainTabDingdangIv);
                Glide.with(mContext).load(R.drawable.tab_message).asBitmap().into(mainTabMessageIv);
                Glide.with(mContext).load(R.drawable.tab_mine_click).asBitmap().into(mainTabMineIv);
                //文字变化
                mainTabDingdangTv.setTextColor(Color.parseColor("#999999"));
                mainTabMessageTv.setTextColor(Color.parseColor("#999999"));
                mainTabMineTv.setTextColor(Color.parseColor("#ff6c20"));
                break;
        }
    }

    @OnClick({R.id.main_tab_dingdang_rl,
            R.id.main_tab_message_rl, R.id.main_tab_mine_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_tab_dingdang_rl:
                //点击了订单模块
                mTabHost.setCurrentTabByTag("one");
                selectTab(0);
                break;
            case R.id.main_tab_message_rl:
                //消息模块
                mTabHost.setCurrentTabByTag("two");
                selectTab(1);
                break;
            case R.id.main_tab_mine_rl:
                //我的模块
                mTabHost.setCurrentTabByTag("three");
                selectTab(2);
                break;
        }
    }
}
