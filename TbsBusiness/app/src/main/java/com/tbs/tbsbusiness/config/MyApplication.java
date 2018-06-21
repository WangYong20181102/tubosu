package com.tbs.tbsbusiness.config;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

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
        initData();
    }

    //初始化相关事件
    private void initData() {
        IS_CHECK_COMPANY_ORDER_PASSWORD = false;//是否验证装修公司的查单密码
    }

    //初始化友盟相关
    private void initUm() {
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "");
        UMConfigure.setLogEnabled(false);
        UMShareAPI.get(this);
//        PlatformConfig.setWeixin("wxf35d53207d312279", "7417d440a885a0435b815e09e5d8d6f3");
    }

    {
        PlatformConfig.setWeixin("wxf35d53207d312279", "7417d440a885a0435b815e09e5d8d6f3");
    }

    public static Context getContext() {
        return context;
    }

}
