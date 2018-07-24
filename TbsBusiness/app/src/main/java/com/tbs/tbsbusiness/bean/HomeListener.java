package com.tbs.tbsbusiness.bean;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Created by Mr.Lin on 2018/7/21 14:49.
 */
public class HomeListener {
    public KeyFun mKeyFun;
    public Context mContext;
    public IntentFilter mHomeBtnIntentFilter = null;
    public HomeBtnReceiver mHomeBtnReceiver = null;
    public static final String TAG = "HomeListener";

    public HomeListener(Context context) {
        mContext = context;
        mHomeBtnIntentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        mHomeBtnReceiver = new HomeBtnReceiver();
    }

    public void startListen() {
        if (mContext != null )
            mContext.registerReceiver(mHomeBtnReceiver, mHomeBtnIntentFilter);
        else
            Log.e(TAG, "mContext is null and startListen fail");

    }
    public void stopListen() {
        if (mContext != null )
            mContext.unregisterReceiver(mHomeBtnReceiver);
        else
            Log.e(TAG, "mContext is null and stopListen fail");
    }

    public void setInterface(KeyFun keyFun){
        mKeyFun = keyFun;

    }
    class HomeBtnReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra("reason");
                if (reason != null) {
                    if(null != mKeyFun ){
                        if (reason.equals("homekey")) {
                            //按Home按键
                            mKeyFun.home();
                        } else if (reason.equals("recentapps")) {
                            //最近任务键也就是菜单键
                            mKeyFun.recent();
                        } else if (reason.equals("assist")) {
                            //常按home键盘
                            mKeyFun.longHome();
                        }
                    }
                }
            }
        }
    }

    public interface KeyFun {
        public void home();
        public void recent();
        public void longHome();
    }
}
