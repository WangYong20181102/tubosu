package com.tbs.tobosupicture;

import android.Manifest;
import android.app.Application;
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
    @Override
    public void onCreate() {
        super.onCreate();
        //友盟初始化
        UMShareAPI.get(this);
    }

    {
        //社会化分享所需要的配置
        PlatformConfig.setWeixin("", "");
        PlatformConfig.setSinaWeibo("", "", "");
        PlatformConfig.setQQZone("", "");
    }
}
