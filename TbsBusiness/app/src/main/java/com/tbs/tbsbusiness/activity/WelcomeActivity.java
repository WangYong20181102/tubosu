package com.tbs.tbsbusiness.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.base.BaseActivity;
import com.tbs.tbsbusiness.config.MyApplication;
import com.tbs.tbsbusiness.util.SpUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

/**
 * 欢迎页面(启动页面)
 */
public class WelcomeActivity extends BaseActivity {
    @BindView(R.id.welcome_img)
    ImageView welcomeImg;
    private String TAG = "WelcomeActivity";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        mContext = this;
        initView();
        needPermission();
    }

    private void initView() {
        Glide.with(mContext).load(R.drawable.welcome_image).asBitmap()
                .centerCrop().placeholder(R.drawable.welcome_image)
                .error(R.drawable.welcome_image).into(welcomeImg);
    }

    private void initJpush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(mContext);
        SpUtil.setPushRegisterId(MyApplication.getContext(), JPushInterface.getRegistrationID(MyApplication.getContext()));
        Log.e(TAG, "初始化推送=============" + SpUtil.getPushRegisterId(mContext));
    }

    //动态获取权限
    private void needPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permission = getPermissionList(mContext);//需要获取动态权限的集合 总6个
            if (permission.size() > 0) {
                requestPermissions(permission.toArray(new String[permission.size()]), 101);
            } else {
                welcomeInit();
            }
        } else {
            //低于6.0版本的系统不需要获取动态的权限
            welcomeInit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 101:
                //获取完权限以后执行的操作
                welcomeInit();
                break;
        }
    }

    //初始化相关的事务  顺带着跳转
    private void welcomeInit() {
        initJpush();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new IntoTask());
            }
        }, 3000);
    }

    private class IntoTask implements Runnable {
        @Override
        public void run() {
            // TODO: 2018/6/4 检测用户是否登录  如果未登录进入登录页面  如果已经登录则进入主页
            if (TextUtils.isEmpty(SpUtil.getUserId(mContext)) || SpUtil.getUserId(mContext) == null) {
                //进入登录页面
                startActivity(new Intent(mContext, LoginActivity.class));
                finish();
            } else {
                // TODO: 2018/6/4    进入主页
                startActivity(new Intent(mContext, MainActivity.class));
                finish();
            }
        }
    }

    //获取权限的集合
    public List<String> getPermissionList(Context activity) {
        List<String> permission = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.READ_PHONE_STATE);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.ACCESS_WIFI_STATE);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.ACCESS_NETWORK_STATE);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.INTERNET);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.CHANGE_WIFI_STATE);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            permission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        return permission;
    }
}
