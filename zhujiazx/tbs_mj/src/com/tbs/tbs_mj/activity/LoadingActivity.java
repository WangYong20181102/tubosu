package com.tbs.tbs_mj.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.utils.AppInfoUtil;
import com.tbs.tbs_mj.utils.CacheManager;
import com.tbs.tbs_mj.utils.Util;
import com.tbs.tbs_mj.web.AcWebActivity;
import com.tbs.tbs_mj.web.AdvWebActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Lie on 2017/9/21.
 */

public class LoadingActivity extends BaseActivity {
    //    private String loadingUrl;
    private int stayTime = 0;
    private TextView tvCountDownText;
    private ImageView ivImg;
    private Timer timer = new Timer();
    private String mJumpUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_loading);
        mContext = LoadingActivity.this;
        TAG = LoadingActivity.class.getSimpleName();
        initView();
        getIntentData();
    }

    private void initView() {
        tvCountDownText = (TextView) findViewById(R.id.tv_count_down_text);
        ivImg = (ImageView) findViewById(R.id.iv_loading_img);
        ivImg.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mJumpUrl != null && !TextUtils.isEmpty(mJumpUrl)) {
                Intent intentToWebActivity;
                // TODO: 2018/7/6 10周年活动跳转
                if (mJumpUrl.contains(Constant.TEN_YEARS_ACTIVITY)) {
                    intentToWebActivity = new Intent(mContext, AcWebActivity.class);
                    intentToWebActivity.putExtra("mWhereFrom", "LoadingActivity");
                } else {
                    intentToWebActivity = new Intent(mContext, AdvWebActivity.class);
                }
                intentToWebActivity.putExtra("mLoadingUrl", mJumpUrl);
                startActivity(intentToWebActivity);
                timer.cancel();
                finish();
            }
        }
    };

    private void getIntentData() {

        Intent intent = getIntent();
        if (intent != null) {
            String url = intent.getStringExtra("loading_img_url");
            url = url.replace("\\/", "/");
            mJumpUrl = intent.getStringExtra("jump_url");
            if (url != null && !"".equals(url)) {
                stayTime = intent.getIntExtra("staytime", 4);
                tvCountDownText.setText(stayTime + " 点击跳过");
                Glide.with(mContext).load(url).into(ivImg);
            }
        }

        tvCountDownText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                goMainActivity();
            }
        });

        timer.schedule(task, 1000, 1000);       // timeTask
    }


    private TimerTask task = new TimerTask() {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    stayTime--;
                    tvCountDownText.setText(stayTime + " 点击跳过");
                    if (stayTime <= 0) {
                        timer.cancel();
                        if ("".equals(CacheManager.getAppEntryOrderPre(mContext))) {
                            CacheManager.setAppEntryOrderPre(mContext, "abc"); // 标识已经进入过发单页面
                            getSharedPreferences("Go_PopOrderActivity_SP", Context.MODE_PRIVATE).edit().putString("go_poporder_string", "5").commit();
//                            Intent intent = new Intent(mContext, GuideOneActivity.class);
                            Intent intent = new Intent(mContext, PopOrderActivity.class);
                            startActivity(intent);
                        } else {
                            CacheManager.setPageFlag(mContext, "welcome");
                            Intent mianIntent = new Intent(mContext, MainActivity.class);
                            startActivity(mianIntent);
                        }
                        finish();
                        System.gc();
                    }
                }
            });
        }
    };

    private void goMainActivity() {

        if ("".equals(CacheManager.getAppEntryOrderPre(mContext))) {
            //初次进入我们的App进入发单页面
            CacheManager.setAppEntryOrderPre(mContext, "abc"); // 标识已经进入过发单页面
//            Intent intent = new Intent(mContext, GuideOneActivity.class);
            Intent intent = new Intent(mContext, PopOrderActivity.class);
            startActivity(intent);
        } else {
            startActivity(new Intent(mContext, MainActivity.class));
        }
        finish();
        System.gc();
    }

}
