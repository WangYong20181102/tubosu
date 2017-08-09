package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.utils.Utils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SystemActivity extends AppCompatActivity {
    //TODO 还有去看系统消息 反馈建议 分享好友下载App的Url没有配置
    private Context mContext;
    private String TAG = "SystemActivity";

    @BindView(R.id.system_back)
    LinearLayout systemBack;
    @BindView(R.id.system_msg)
    RelativeLayout systemMsg;
    @BindView(R.id.system_suggest)
    RelativeLayout systemSuggest;
    @BindView(R.id.system_versions)
    TextView systemVersions;
    @BindView(R.id.system_clean)
    RelativeLayout systemClean;
    @BindView(R.id.system_evaluate)
    RelativeLayout systemEvaluate;
    @BindView(R.id.system_share)
    RelativeLayout systemShare;
    @BindView(R.id.system_cashe)
    TextView systemCashe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    private void initView() {
        systemVersions.setText("v" + Utils.getAppVersionName(mContext));
        try {
            if ("0.0Byte".equals(Utils.getTotalCacheSize(getApplicationContext()))) {
                systemCashe.setText("0 M");
            } else {
                systemCashe.setText("" + Utils.getTotalCacheSize(getApplicationContext()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.system_back, R.id.system_msg, R.id.system_suggest, R.id.system_clean, R.id.system_evaluate, R.id.system_share})
    public void onViewClickedInSystemActivity(View view) {
        switch (view.getId()) {
            case R.id.system_back:
                finish();
                break;
            case R.id.system_msg:
                //进入系统消息
                break;
            case R.id.system_suggest:
                //反馈意见
                if (Utils.userIsLogin(mContext)) {
                    intoSuggest();
                } else {
                    Utils.gotoLogin(mContext);
                }

                break;
            case R.id.system_clean:
                //清理缓存
                cleanCache();
                break;
            case R.id.system_evaluate:
                //评价App
                evaluateApp();
                break;
            case R.id.system_share:
                //分享App
                shareApp();
                break;
        }
    }

    //清除App缓存
    private void cleanCache() {
        if (!"0 M".equals(systemCashe.getText().toString())) {
            Utils.clearAllCache(getApplicationContext());
            systemCashe.setText("0 M");
        } else {
            Toast.makeText(mContext, "你已清理过缓存", Toast.LENGTH_SHORT).show();
        }
    }

    //去评价App
    private void evaluateApp() {
        String mAddress = "market://details?id=" + getPackageName();
        Intent marketIntent = new Intent("android.intent.action.VIEW");
        marketIntent.setData(Uri.parse(mAddress));
        startActivity(marketIntent);
    }

    //进行App的分享
    private void shareApp() {
        Log.e(TAG, "点击了分享按钮。。");
        UMWeb web = new UMWeb("http://t.cn/R4p6kwr");
        web.setTitle("邀你一起下载装修看看App");
        web.setThumb(new UMImage(mContext, R.mipmap.ic_launcher));
        web.setDescription("装修看看，看得见的装修。");
        new ShareAction(SystemActivity.this).withMedia(web)
                .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                .setCallback(umShareListener).open();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

        }
    };

    private void intoSuggest() {
        Intent intent = new Intent(mContext, SuggestActivity.class);
        mContext.startActivity(intent);
    }
}
