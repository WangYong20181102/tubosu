package com.tbs.tobosutype.base;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.global.HomeListener;
import com.tbs.tobosutype.global.MyApplication;
import com.tbs.tobosutype.utils.AppManager;
import com.tbs.tobosutype.utils.EventBusUtil;
import com.tbs.tobosutype.utils.SpUtil;
import com.tbs.tobosutype.utils.ToastUtil;
import com.tbs.tobosutype.utils.Util;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by Mr.Lin on 2017/10/27 10:29.
 */

public class BaseActivity extends AppCompatActivity {
    protected static String TAG = BaseActivity.class.getSimpleName();
    protected Context mContext;
    private HomeListener mHomeListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        if (isRegisterEventBus()) {
            EventBusUtil.register(this);
        }
        //监听home键事件
        mHomeListener = new HomeListener(mContext);
        mHomeListener.setInterface(keyFun);
//        Log.e(TAG, "BaseActivity执行onCreate========");
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Log.e(TAG, "BaseActivity执行onStart========");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        Log.e(TAG, "BaseActivity执行onRestart========");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHomeListener.startListen();
        MobclickAgent.onResume(this);
        // TODO: 2018/3/5 用自己写的任务栈管理获取
        AppManager.addActivity(this);
        Log.e(TAG, "BaseActivity执行onResume====获取栈顶的Activity====" + AppManager.currentActivityName() + "===页面属性===" + SpUtil.getStatisticsEventPageId(MyApplication.getContext()) + "====上一个Activity====" + AppManager.lastSecoundActivityName());
        Util.useStatisticsEventVistEvent(true, AppManager.lastSecoundActivityName(), AppManager.currentActivityName());

    }

    @Override
    protected void onPause() {
        super.onPause();
        mHomeListener.stopListen();
        MobclickAgent.onPause(this);
        Log.e(TAG, "BaseActivity执行onPause====获取栈顶的Activity====" + AppManager.currentActivityName() + "===页面属性===" + SpUtil.getStatisticsEventPageId(MyApplication.getContext()) + "====上一个Activity====" + AppManager.lastSecoundActivityName());
//        Log.e(TAG, "BaseActivity执行onPause========");
        if (havePageId()) {
            //有页面属性
            Util.useStatisticsEventVistEvent(false, AppManager.lastSecoundActivityName() + "/" + SpUtil.getStatisticsEventPageId(MyApplication.getContext()), AppManager.currentActivityName() + "/" + SpUtil.getStatisticsEventPageId(MyApplication.getContext()));
        } else {
            Util.useStatisticsEventVistEvent(false, AppManager.lastSecoundActivityName(), AppManager.currentActivityName());
        }
        if (!TextUtils.isEmpty(SpUtil.getStatisticsEventPageId(MyApplication.getContext()))) {
            SpUtil.setStatisticsEventPageId(MyApplication.getContext(), "");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        Log.e(TAG, "BaseActivity执行onStop========");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Log.e(TAG, "BaseActivity执行onDestroy========");
        if (isRegisterEventBus()) {
            EventBusUtil.unregister(this);
        }
    }

    /**
     * 是否注册事件分发
     *
     * @return true绑定EventBus事件分发，默认不绑定，子类需要绑定的话复写此方法返回true.
     */
    protected boolean isRegisterEventBus() {
        return false;
    }

    /**
     * 页面包含的属性id
     *
     * @return
     */
    protected boolean havePageId() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(Event event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyEventBusCome(Event event) {
        if (event != null) {
            receiveStickyEvent(event);
        }
    }

    /**
     * 接收到分发到事件
     *
     * @param event 事件
     */
    protected void receiveEvent(Event event) {

    }

    /**
     * 接受到分发的粘性事件
     *
     * @param event 粘性事件
     */
    protected void receiveStickyEvent(Event event) {

    }
    //home键监听事件
    private HomeListener.KeyFun keyFun = new HomeListener.KeyFun() {
        @Override
        public void home() {
            Log.e(TAG,"按键监听_lin=======点击了home键");
            if(!MyApplication.evBeanArrayList.isEmpty()){
                Util.HttpPostUserUseInfo();
            }
        }

        @Override
        public void recent() {
            Log.e(TAG,"按键监听_lin=======点击了任务键");
            if(!MyApplication.evBeanArrayList.isEmpty()){
                Util.HttpPostUserUseInfo();
            }
        }

        @Override
        public void longHome() {
            Log.e(TAG,"按键监听_lin=======长按了home键");
            if(!MyApplication.evBeanArrayList.isEmpty()){
                Util.HttpPostUserUseInfo();
            }
        }
    };
}
