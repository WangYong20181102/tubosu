package com.tbs.tbsbusiness.config;

import android.app.Application;
import android.content.Context;

/**
 * Created by Mr.Lin on 2018/6/2 10:22.
 */
public class MyApplication extends Application{
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }


    public static Context getContext() {
        return context;
    }

}
