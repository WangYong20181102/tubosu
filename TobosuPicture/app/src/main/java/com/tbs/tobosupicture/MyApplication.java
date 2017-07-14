package com.tbs.tobosupicture;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.tbs.tobosupicture.utils.SpUtils;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.List;

/**
 * Created by Mr.Lin on 2017/5/10 15:40.
 * 全局Application
 */

public class MyApplication extends Application {
    private static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = MyApplication.this;
        //友盟初始化
        UMShareAPI.get(this);
    }

    public static Context getContexts() {
        return myApplication;
    }

    {
        //社会化分享所需要的配置 微信 微博 QQ的Appkey
        PlatformConfig.setWeixin("wx0bdd96841e80fac2", "d9794d9011a4344c0e60d643046c6ac6");
        PlatformConfig.setSinaWeibo("", "", "");
        PlatformConfig.setQQZone("", "");
    }
}
