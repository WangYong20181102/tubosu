package com.tobosu.mydecorate.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.tobosu.mydecorate.application.MyApplication;
import com.tobosu.mydecorate.global.HomeListener;
import com.tobosu.mydecorate.util.AppManager;
import com.tobosu.mydecorate.util.SpUtil;
import com.tobosu.mydecorate.util.Util;

/**
 * Created by Mr.Lin on 2018/6/25 11:06.
 */
public class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    private HomeListener mHomeListener = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        //监听home键事件
        mHomeListener = new HomeListener(mContext);
        mHomeListener.setInterface(keyFun);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHomeListener.startListen();
        AppManager.addActivity(this);
//        Log.e(TAG, "BaseActivity执行onResume====获取栈顶的Activity====" + AppManager.currentActivityName() + "===页面属性===" + SpUtil.getStatisticsEventPageId(MyApplication.getContext()) + "====上一个Activity====" + AppManager.lastSecoundActivityName());
        Util.useStatisticsEventVistEvent(true, AppManager.lastSecoundActivityName(), AppManager.currentActivityName());

    }

    @Override
    protected void onPause() {
        super.onPause();
        mHomeListener.stopListen();
        if (havePageId()) {
            //有页面属性
            Util.useStatisticsEventVistEvent(false, AppManager.lastSecoundActivityName() + "/" + SpUtil.getStatisticsEventPageId(MyApplication.getContexts()), AppManager.currentActivityName() + "/" + SpUtil.getStatisticsEventPageId(MyApplication.getContexts()));
        } else {
            Util.useStatisticsEventVistEvent(false, AppManager.lastSecoundActivityName(), AppManager.currentActivityName());
        }
        if (!TextUtils.isEmpty(SpUtil.getStatisticsEventPageId(MyApplication.getContexts()))) {
            SpUtil.setStatisticsEventPageId(MyApplication.getContexts(), "");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected boolean havePageId() {
        return false;
    }


    //home键监听事件
    private HomeListener.KeyFun keyFun = new HomeListener.KeyFun() {
        @Override
        public void home() {
//            Log.e(TAG,"按键监听_lin=======点击了home键");
            if (!MyApplication.evBeanArrayList.isEmpty()) {
                Util.HttpPostUserUseInfo();
            }
        }

        @Override
        public void recent() {
//            Log.e(TAG,"按键监听_lin=======点击了任务键");
            if (!MyApplication.evBeanArrayList.isEmpty()) {
                Util.HttpPostUserUseInfo();
            }
        }

        @Override
        public void longHome() {
//            Log.e(TAG,"按键监听_lin=======长按了home键");
            if (!MyApplication.evBeanArrayList.isEmpty()) {
                Util.HttpPostUserUseInfo();
            }
        }
    };

}
