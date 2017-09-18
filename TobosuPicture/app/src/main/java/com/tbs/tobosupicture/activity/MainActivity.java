package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jauker.widget.BadgeView;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean.EC;
import com.tbs.tobosupicture.bean.Event;
import com.tbs.tobosupicture.bean._DSTime;
import com.tbs.tobosupicture.bean._ReceiveMsg;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.fragment.DecorationCaseFragment;
import com.tbs.tobosupicture.fragment.ImageToFriendFragment;
import com.tbs.tobosupicture.fragment.ImgToFriendFragment;
import com.tbs.tobosupicture.fragment.MineFragment;
import com.tbs.tobosupicture.fragment.TemplateFragment;
import com.tbs.tobosupicture.utils.Base64Util;
import com.tbs.tobosupicture.utils.EventBusUtil;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends BaseActivity {
    @BindView(R.id.main_frameLayout)
    FrameLayout mainFrameLayout;
    @BindView(R.id.rb_first)
    RadioButton rbFirst;
    @BindView(R.id.rb_second)
    RadioButton rbSecond;
    @BindView(R.id.rb_third)
    RadioButton rbThird;
    @BindView(R.id.rb_fourth)
    RadioButton rbFourth;
    @BindView(R.id.main_radioGroup)
    RadioGroup mainRadioGroup;
    @BindView(R.id.mian_show_num3)
    View mianShowNum3;
    @BindView(R.id.mian_about_reddot)
    TextView mianAboutReddot;

    private Fragment[] mFragments;//fragment集合
    private int mIndex;//数据下标
    private String TAG = "MainActivity";
    private Context mContext;
    private BadgeView mImgToFriendTag;//以图会友提示
    private Gson mGson;
    private String is_exist_case = "";
    private Socket mSocket;
    private boolean isSocketConnect = false;
    private _ReceiveMsg receiveMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        initFragment();
//        HttpGetTime();
        initUserMsg();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    private void initFragment() {
        //实例化解析工具
        mGson = new Gson();
        //样板图
        TemplateFragment templateFragment = new TemplateFragment();
        //案例
        DecorationCaseFragment decorationCaseFragment = new DecorationCaseFragment();
        //以图会友
//        ImageToFriendFragment imageToFriendFragment = new ImageToFriendFragment();//用来测试的fragment
        ImgToFriendFragment imageToFriendFragment = new ImgToFriendFragment();
        //我的
        MineFragment mineFragment = new MineFragment();
        //将fragment添加至数组中
        mFragments = new Fragment[]{templateFragment, decorationCaseFragment, imageToFriendFragment, mineFragment};
        //处理相关的事务
        FragmentTransaction ft =
                getSupportFragmentManager().beginTransaction();
        //添加首页
        ft.add(R.id.main_frameLayout, templateFragment).commit();
        //默认设置第0个
        setIndexSelect(0);


        //以图会友红点提示
        mImgToFriendTag = new BadgeView(mContext);
        //红点提示框的圆形角度以及背景颜色
        mImgToFriendTag.setBackground(8, Color.parseColor("#FFF10606"));
        //设置依附在那个控件上
        mImgToFriendTag.setTargetView(mianShowNum3);
        //依附的位置
        mImgToFriendTag.setBadgeGravity(Gravity.CENTER | Gravity.TOP);
        //设置边距
        mImgToFriendTag.setBadgeMargin(12, 0, 0, 0);
    }

    //初始化用户的消息提示模块
    private void initUserMsg() {
        if (Utils.userIsLogin(mContext)) {
            //建立通并监听
            LinkSocket();
            //监听Socket是否链接成功
            CheckSocketConnectLister();
        }
    }

    //监听链接状态
    private void CheckSocketConnectLister() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isSocketConnect) {
                    try {
                        Log.e(TAG, "检测Socket链接状态=======" + mSocket.connected());
                        if (mSocket.connected()) {
                            isSocketConnect = true;
                            Log.e(TAG, "检测Socket链接状态====成功====" + mSocket.connected());
                            //发送事件
                            SendLoginEvent(SpUtils.getUserUid(mContext));
                            //通知后台登录成功
                            Thread.sleep(1000);
                            HttpIsConnect(SpUtils.getUserUid(mContext));
                        }
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //建立通信的方法
    private void LinkSocket() {
        //初始化
        try {
            mSocket = IO.socket(UrlConstans.SOCKET_URL);
            mSocket.on("new_msg", onNewMsg);
            mSocket.on("login", loginMsg);
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    //socket登录监听
    private Emitter.Listener loginMsg = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "监听登录事件==========" + args[0].toString());
        }
    };

    //通知服务器建立连接
    private void  HttpIsConnect(String uid) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", uid);
        HttpUtils.doPost(UrlConstans.SOCKET_IS_CONNECT, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "socket链接失败=======" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.e(TAG, "请求连接成功之后返回的msg==========" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 发送登录事件
    private void SendLoginEvent(String uid) {
        if (!TextUtils.isEmpty(uid)) {
            Log.e(TAG, "发送Socket登录事件==============");
            mSocket.emit("login", uid);
        }
    }

    //监听消息
    private Emitter.Listener onNewMsg = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "接收到服务器的推送消息===没有处理前===" + args[0].toString());
            //base64解密
            String json = Base64Util.getFromBase64(args[0].toString());
            if (!TextUtils.isEmpty(json)) {
                Log.e(TAG, "解密之后的数据=======" + json);
                receiveMsg = mGson.fromJson(json, _ReceiveMsg.class);
                //我的发起的消息 包含消息的数量消息的头像
                EventBusUtil.sendEvent(new Event(EC.EventCode.MY_JOIN_MSG, receiveMsg.getMy_participation()));//我的参与
                EventBusUtil.sendEvent(new Event(EC.EventCode.MY_ORGIN_MSG, receiveMsg.getMy_sponsor()));//我的发起
                //将数值布局
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //显示数量
                        int msgNum = Integer.parseInt(receiveMsg.getAll_msg_count());
                        if (msgNum >= 100) {
                            mImgToFriendTag.setText("99+");
                        } else {
                            mImgToFriendTag.setBadgeCount(msgNum);
                        }

                    }
                });
            }
        }
    };

    //断开socket的链接
    private void disConnectSocket() {
        mSocket.disconnect();
        mSocket.off("new_msg", onNewMsg);
        isSocketConnect = false;
    }

    //选择所在页面
    private void setIndexSelect(int index) {
        if (mIndex == index) {
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //隐藏不必要的布局
        ft.hide(mFragments[mIndex]);
        //处理逻辑 如果没有添加则添加 添加了则显示
        if (!mFragments[index].isAdded()) {
            ft.add(R.id.main_frameLayout, mFragments[index]).show(mFragments[index]);
        } else {
            ft.show(mFragments[index]);
        }
        ft.commit();
        mIndex = index;
    }

    @OnClick({R.id.rb_first, R.id.rb_second, R.id.rb_third, R.id.rb_fourth})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_first:
                //点击第一个选项 显示样板图
                MobclickAgent.onEvent(mContext, "click_xiao_guo_tu_tab");
                setIndexSelect(0);
                break;
            case R.id.rb_second:
                //点击第二个选项 显示案例
                MobclickAgent.onEvent(mContext, "click_an_li_tu_tab");
                setIndexSelect(1);
                if (mSocket != null) {
                    Log.e(TAG, "socket链接状态=======" + mSocket.connected());
                }
                break;
            case R.id.rb_third:
                //点击第三个选项 显示以图会友
                if (mImgToFriendTag.getText().toString().equals("0")) {
                    //图标上没有数据
                } else {
                    //TODO 图标上有数据
                    EventBusUtil.sendEvent(new Event(EC.EventCode.SHOW_ABOUT_ME));
                    Log.e(TAG, "已发送消息===SHOW_ABOUT_ME===");
                }
                MobclickAgent.onEvent(mContext, "click_yi_tu_hui_you_tab");
                setIndexSelect(2);
                if (mSocket != null) {
                    Log.e(TAG, "socket链接状态=======" + mSocket.connected());
                }
                break;
            case R.id.rb_fourth:
                //点击第四个选项 显示我的  其中我的界面要分情况考虑
                MobclickAgent.onEvent(mContext, "click_wo_de_tu_tab");
                setIndexSelect(3);
                break;
        }
    }

    //是否使用Eventbus
    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    //使用Eventbus处理回来显示 返回原来所点击的模块  目前仅做测试使用
    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.SHOW_TEMPLATE_FRAGMENT:
                break;
            case EC.EventCode.SHOW_DECORATIONCASE_FRAGMENT:
                break;
            case EC.EventCode.SHOW_IMAGETOFRIEND_FRAGMENT:
                break;
            case EC.EventCode.SHOW_MINE_FRAGMENT:
                break;
            case EC.EventCode.LOGIN_INITDATA:
                //用户登录的事件通知
                initUserMsg();
                break;
            case EC.EventCode.LOGIN_OUT:
                //用户登出事件通知
                disConnectSocket();
                mImgToFriendTag.setBadgeCount(0);
                mianAboutReddot.setVisibility(View.GONE);
                EventBusUtil.sendEvent(new Event(EC.EventCode.HINT_MINE_RED_DOT));
                break;
            case EC.EventCode.ABOUT_ME_GET_MSG_NUM:
                //有关于我的界面获取消息的数量
                EventBusUtil.sendEvent(new Event(EC.EventCode.MY_JOIN_MSG, receiveMsg.getMy_participation()));//我的参与
                EventBusUtil.sendEvent(new Event(EC.EventCode.MY_ORGIN_MSG, receiveMsg.getMy_sponsor()));//我的发起
                break;
            case EC.EventCode.MY_ORGIN_FRAGMENT_GET_MSG:
                EventBusUtil.sendEvent(new Event(EC.EventCode.MY_ORGIN_MSG, receiveMsg.getMy_sponsor()));//我的发起
                break;
            case EC.EventCode.MY_JOIN_FRAGMENT_GET_MSG:
                EventBusUtil.sendEvent(new Event(EC.EventCode.MY_JOIN_MSG, receiveMsg.getMy_participation()));//我的参与
                break;
        }
    }

    //循环拉取消息 的定时任务
//    class MyThread implements Runnable {
//
//        @Override
//        public void run() {
//            while (isLoop) {
//                try {
//                    if (!TextUtils.isEmpty(SpUtils.getMsgTime(mContext))) {
//                        Thread.sleep(Long.parseLong(SpUtils.getMsgTime(mContext)) * 1000);
//                        Log.e(TAG, "网络获取消息的定时时间=======" + Long.parseLong(SpUtils.getMsgTime(mContext)) * 1000);
//                    } else {
//                        Thread.sleep(2345);
//                    }
//                    HttpGetMsg();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    //循环拉取用户搜索案例的红点
//    class MyThread2 implements Runnable {
//
//        @Override
//        public void run() {
//            while (isLoop) {
//                try {
//                    //5分钟拉取一次
//                    if (!TextUtils.isEmpty(SpUtils.getAnliTime(mContext))) {
//                        Thread.sleep(Long.parseLong(SpUtils.getAnliTime(mContext)) * 1000);
//                        Log.e(TAG, "网络获取案例的定时时间=======" + Long.parseLong(SpUtils.getAnliTime(mContext)) * 1000);
//                    } else {
//                        Thread.sleep(300000);
//                    }
//                    HttpGetIsHaveNewCast();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    //网络拉取我的动态相关
    private void HttpGetIsHaveNewCast() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        HttpUtils.doPost(UrlConstans.OWNER_GET_MSG, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败=========" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功====" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONObject data = jsonObject.getJSONObject("data");
                    is_exist_case = data.getString("is_exist_case");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mianAboutReddot != null) {
                                if (is_exist_case.equals("1")) {
                                    Log.e(TAG, "显示‘我的’红点提示======");
                                    mianAboutReddot.setVisibility(View.VISIBLE);
                                    EventBusUtil.sendEvent(new Event(EC.EventCode.SHOW_MINE_RED_DOT));//在我的界面中显示红点
                                } else {
                                    Log.e(TAG, "隐藏‘我的’红点提示======");
                                    mianAboutReddot.setVisibility(View.GONE);
                                    EventBusUtil.sendEvent(new Event(EC.EventCode.HINT_MINE_RED_DOT));//在我的界面中隐藏红点
                                }
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //网络请求消息
    private void HttpGetMsg() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("is_icon", "1");
        HttpUtils.doPost(UrlConstans.RECEIVE_INFORMATION, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败=====" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        String data = jsonObject.getString("data");
                        final _ReceiveMsg receiveMsg = mGson.fromJson(data, _ReceiveMsg.class);
//                        mMyOrginNum = Integer.parseInt(receiveMsg.getMy_sponsor().getMsg_count());//发起的数量
//                        mMyOrginIconUrl = receiveMsg.getMy_sponsor().getIcon();//发起的数量
//                        mMyJoinNum = Integer.parseInt(receiveMsg.getMy_participation().getMsg_count());//参与的数量
//                        mMyJoinIconUrl = receiveMsg.getMy_participation().getIcon();//参与的数量
//
//                        EventBusUtil.sendEvent(new Event(EC.EventCode.MY_JOIN_NUM, mMyJoinNum));
//                        EventBusUtil.sendEvent(new Event(EC.EventCode.MY_JOIN_ICON, mMyJoinIconUrl));
//                        EventBusUtil.sendEvent(new Event(EC.EventCode.MY_ORGIN_NUM, mMyOrginNum));
//                        EventBusUtil.sendEvent(new Event(EC.EventCode.MY_ORGIN_ICON, mMyOrginIconUrl));
//                        //我的发起的消息 包含消息的数量消息的头像
//                        _ReceiveMsg.MySponsor mySponsor = new _ReceiveMsg.MySponsor(receiveMsg.getMy_sponsor().getMsg_count(), mMyOrginIconUrl);
//                        _ReceiveMsg.MyParticipation myParticipation = new _ReceiveMsg.MyParticipation(receiveMsg.getMy_participation().getMsg_count(), mMyJoinIconUrl);
//                        EventBusUtil.sendEvent(new Event(EC.EventCode.MY_JOIN_MSG, myParticipation));
//                        EventBusUtil.sendEvent(new Event(EC.EventCode.MY_ORGIN_MSG, mySponsor));
//                        //将数值布局
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                //显示数量
//                                int msgNum = Integer.parseInt(receiveMsg.getAll_msg_count());
//                                if (msgNum >= 100) {
//                                    mImgToFriendTag.setText("99+");
//                                } else {
//                                    mImgToFriendTag.setBadgeCount(msgNum);
//                                }
//
//                            }
//                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //重新返回按钮
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("你确定要退出吗？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SpUtils.setHouseSpaceNum(mContext, 0);
                    SpUtils.setHouseStyleNum(mContext, 0);
                    SpUtils.setHousePartNum(mContext, 0);
                    SpUtils.setHouseHuxingNum(mContext, 0);
                    SpUtils.setHouseColorNum(mContext, 0);
                    disConnectSocket();
                    MainActivity.this.finish();
                }
            }).setNegativeButton("再看看", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disConnectSocket();
        ButterKnife.bind(this).unbind();
    }

    /**
     * 在基本的四个Fragment中涉及到分享相关的处理都在MainActivity的OnActivityResult中处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
