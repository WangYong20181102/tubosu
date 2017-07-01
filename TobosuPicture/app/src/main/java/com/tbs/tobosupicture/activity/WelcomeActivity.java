package com.tbs.tobosupicture.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean.EC;
import com.tbs.tobosupicture.bean.Event;
import com.tbs.tobosupicture.utils.EventBusUtil;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;

import java.util.TimerTask;

/**
 * 欢迎启动页 用于加载闪屏
 */
public class WelcomeActivity extends BaseActivity {
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mContext = this;
        //去主界面
        EventBusUtil.sendStickyEvent(new Event(EC.EventCode.WELCOMETOMAIN));
        intoMainActivity();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    private void intoMainActivity() {
        if (Utils.isNetAvailable(mContext)) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!TextUtils.isEmpty(SpUtils.getUserIsFristLogin(mContext))) {
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                        WelcomeActivity.this.finish();
                    } else {
                        //用户第一次登入我们的App
                        startActivity(new Intent(WelcomeActivity.this, FristActivity.class));
                        WelcomeActivity.this.finish();
                    }
                }
            }, 3000);
        } else {
            //网络未连接的时候弹出相关的提示 进行网络的设置
//            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//            builder.setTitle("网络连接提示");
//            builder.setMessage("当前网络未连接哦~");
//            builder.setNegativeButton("设置好了", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    intoMainActivity();
//                }
//            });
//            builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                }
//            });
//            AlertDialog dialog = builder.create();
//            dialog.show();
        }
    }
}
