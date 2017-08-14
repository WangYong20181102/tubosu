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
import com.tbs.tobosupicture.bean._ReceiveMsg;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.fragment.DecorationCaseFragment;
import com.tbs.tobosupicture.fragment.ImgToFriendFragment;
import com.tbs.tobosupicture.fragment.MineFragment;
import com.tbs.tobosupicture.fragment.TemplateFragment;
import com.tbs.tobosupicture.utils.EventBusUtil;
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
    private boolean isLoop = false;//是否需要循环
    private Gson mGson;
    private int mMyOrginNum = 0;//我的发起的数量
    private int mMyJoinNum = 0;//我的参与的数量
    private String is_exist_case = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        initFragment();
        initUserMsg();
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
            isLoop = true;
            new Thread(new MyThread()).start();
            new Thread(new MyThread2()).start();
        }
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
                setIndexSelect(0);
                break;
            case R.id.rb_second:
                //点击第二个选项 显示案例
                setIndexSelect(1);
                break;
            case R.id.rb_third:
                //点击第三个选项 显示以图会友
                if (mImgToFriendTag.getText().toString().equals("0")) {
                    //图标上没有数据
                } else {
                    //TODO 图标上有数据
                    EventBusUtil.sendEvent(new Event(EC.EventCode.SHOW_ABOUT_ME));
//                    EventBusUtil.sendEvent(new Event(EC.EventCode.CHECK_ABOUTME_MYORG_HAS_MSG));
                    Log.e(TAG, "已发送消息===SHOW_ABOUT_ME===");
                }
                setIndexSelect(2);
                break;
            case R.id.rb_fourth:
                //点击第四个选项 显示我的  其中我的界面要分情况考虑
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
                //样板图
//                rbFirst.performClick();
//                setIndexSelect(0);
                break;
            case EC.EventCode.SHOW_DECORATIONCASE_FRAGMENT:
                //装修案例
                Log.e(TAG, "收到案例相关消息");
//                rbSecond.performClick();
//                setIndexSelect(1);
                break;
            case EC.EventCode.SHOW_IMAGETOFRIEND_FRAGMENT:
                Log.e(TAG, "收到点击以图会友相关消息");
                //以图会友
//                rbThird.performClick();
//                setIndexSelect(2);
                break;
            case EC.EventCode.SHOW_MINE_FRAGMENT:
                //显示我的
//                rbFourth.performClick();
//                setIndexSelect(3);
                break;
            case EC.EventCode.LOGIN_INITDATA:
                //用户登录的事件通知 这里开始循环请求
                isLoop = true;
                new Thread(new MyThread()).start();
                new Thread(new MyThread2()).start();
                Log.e(TAG, "用户登录成功====处理登录事件isloop的值===" + isLoop);
                break;
            case EC.EventCode.LOGIN_OUT:
                //用户登出事件通知
                isLoop = false;
                Log.e(TAG, "用户登出成功====处理登出事件===isloop的值===" + isLoop);
                mImgToFriendTag.setBadgeCount(0);
                break;
        }
    }

    //循环拉取消息 的定时任务
    class MyThread implements Runnable {

        @Override
        public void run() {
            while (isLoop) {
                try {
                    Thread.sleep(1000);
                    HttpGetMsg();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //循环拉取用户搜索案例的红点
    class MyThread2 implements Runnable {

        @Override
        public void run() {
            while (isLoop) {
                try {
                    //5分钟拉取一次
                    Thread.sleep(300000);
                    HttpGetIsHaveNewCast();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

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
                            if (is_exist_case.equals("1")) {
                                mianAboutReddot.setVisibility(View.VISIBLE);
                            } else {
                                mianAboutReddot.setVisibility(View.GONE);
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
                        mMyOrginNum = Integer.parseInt(receiveMsg.getMy_sponsor().getMsg_count());//发起的数量
                        mMyJoinNum = Integer.parseInt(receiveMsg.getMy_participation().getMsg_count());//参与的数量
                        EventBusUtil.sendEvent(new Event(EC.EventCode.MY_JOIN_NUM, mMyJoinNum));
                        EventBusUtil.sendEvent(new Event(EC.EventCode.MY_ORGIN_NUM, mMyOrginNum));
                        //将数值布局
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //显示数量
                                mImgToFriendTag.setBadgeCount(Integer.parseInt(receiveMsg.getAll_msg_count()));
                            }
                        });
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
