package com.tbs.tobosutype.activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.CacheManager;
import com.tbs.tobosutype.utils.Util;
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

    private void initView(){
        tvCountDownText = (TextView) findViewById(R.id.tv_count_down_text);
        ivImg = (ImageView) findViewById(R.id.iv_loading_img);
    }

    private void getIntentData() {

        Intent intent = getIntent();
        if(intent!=null){
            String url = intent.getStringExtra("loading_img_url");
            url = url.replace("\\/", "/");
            if(url!=null && !"".equals(url)){
                stayTime = intent.getIntExtra("staytime", 4);
                tvCountDownText.setText(stayTime + " 点击跳过");
                Glide.with(mContext).load(url).into(ivImg);
            }
        }

        tvCountDownText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    if(stayTime <= 0){
                        timer.cancel();
                        if ("".equals(CacheManager.getAppEntryOrderPre(mContext))) {
                            CacheManager.setAppEntryOrderPre(mContext, "abc"); // 标识已经进入过发单页面
                            getSharedPreferences("Go_PopOrderActivity_SP", Context.MODE_PRIVATE).edit().putString("go_poporder_string", "5").commit();
                            Intent intent = new Intent(mContext, PopOrderActivity.class);
                            startActivity(intent);
                        } else {
                            CacheManager.setPageFlag(mContext, "welcome");
                            Intent mianIntent = new Intent(mContext, MainActivity.class);
                            startActivity(mianIntent);
                            Util.setErrorLog(TAG, "--进入 SelectCtiyActivity 页面--");
                        }
                        finish();
                        System.gc();
                    }
                }
            });
        }
    };

    private void goMainActivity(){
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
        System.gc();
    }

}