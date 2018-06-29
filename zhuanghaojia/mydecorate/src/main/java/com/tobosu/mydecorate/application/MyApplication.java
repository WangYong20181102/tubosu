package com.tobosu.mydecorate.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.tobosu.mydecorate.bean._AppEvent;
import com.tobosu.mydecorate.database.SQLHelper;

import java.util.ArrayList;


/**
 * Created by dec on 2016/9/26.
 */
public class MyApplication extends Application {
    private static final String TAG = MyApplication.class.getSimpleName();

    public static boolean HAS_NEW_VERSION = false;

    public static String LOACTION_CITY_NAME = "";

    public static String LOACTION_CITY_SIMPLE_NAME = "";

    private static Context context;

    private static MyApplication mApplication;
    private SQLHelper sqlHelper;

    /**
     * 必须强制更新
     */
    public static int ISMUSTUPDATE = -1;
    public static String NEW_VERSION_NAME = "";
    public static String NEW_VERSION_DOWNPATH = "";
    // TODO: 2018/2/27 事件统计的全局集合
    public static ArrayList<_AppEvent.EvBean> evBeanArrayList;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplication = MyApplication.this;
        evBeanArrayList = new ArrayList<>();
    }



    public static Context getContexts() {
        return mApplication;
    }

    /** 获取Application */
    public static MyApplication getApp() {
        return mApplication;
    }

    /** 获取数据库Helper */
    public SQLHelper getSQLHelper() {
        if (sqlHelper == null)
            sqlHelper = new SQLHelper(mApplication);
        return sqlHelper;
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        if (sqlHelper != null)
            sqlHelper.close();
        super.onTerminate();
    }

}
