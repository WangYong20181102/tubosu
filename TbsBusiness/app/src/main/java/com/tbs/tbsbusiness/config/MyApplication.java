package com.tbs.tbsbusiness.config;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.umeng.commonsdk.UMConfigure;

/**
 * Created by Mr.Lin on 2018/6/2 10:22.
 */
public class MyApplication extends MultiDexApplication {
    private static Context context;
    public static boolean IS_CHECK_COMPANY_ORDER_PASSWORD = false;//是否验证装修公司的查单密码

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //友盟初始化
        initUm();
    }
    //初始化相关事件
    private void initData() {
        IS_CHECK_COMPANY_ORDER_PASSWORD = false;//是否验证装修公司的查单密码
    }

    //初始化友盟相关
    private void initUm() {
        UMConfigure.init(this, "5b120d59a40fa31c5100007a", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        UMConfigure.setLogEnabled(false);
    }


    public static Context getContext() {
        return context;
    }

}
