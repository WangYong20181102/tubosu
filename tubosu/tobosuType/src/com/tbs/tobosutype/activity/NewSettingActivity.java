package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.DataCleanManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewSettingActivity extends AppCompatActivity {
    @BindView(R.id.new_setting_back)
    RelativeLayout newSettingBack;
    @BindView(R.id.new_setting_clean_cache)
    RelativeLayout newSettingCleanCache;
    @BindView(R.id.new_setting_suggestion)
    RelativeLayout newSettingSuggestion;
    @BindView(R.id.new_setting_about_our)
    RelativeLayout newSettingAboutOur;
    @BindView(R.id.new_setting_version)
    TextView newSettingVersion;
    @BindView(R.id.banner_dever)
    View bannerDever;
    @BindView(R.id.new_setting_cache)
    TextView newSettingCache;
    private String TAG = "NewSettingActivity";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_setting);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
        //显示当前的缓存大小
        showCache();
        //显示当前的版本
        showVersion();
    }

    private void showVersion() {
        newSettingVersion.setText("v " + AppInfoUtil.getAppVersionName(mContext));
    }

    private void showCache() {
        try {
            if ("0.0Byte".equals(DataCleanManager.getTotalCacheSize(getApplicationContext()))) {
                newSettingCache.setText("0 M");
            } else {
                newSettingCache.setText(DataCleanManager.getTotalCacheSize(getApplicationContext()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.new_setting_back, R.id.new_setting_clean_cache, R.id.new_setting_suggestion, R.id.new_setting_about_our, R.id.new_setting_version})
    public void onViewClickedInNewSetting(View view) {
        switch (view.getId()) {
            case R.id.new_setting_back:
                finish();
                break;
            case R.id.new_setting_clean_cache:
                //清理缓存
                if (!"0 M".equals(newSettingCache.getText())) {
                    DataCleanManager.clearAllCache(getApplicationContext());
                    newSettingCache.setText("0 M");
                    Toast.makeText(mContext, "缓存已清理", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "已清理至最佳状态", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.new_setting_suggestion:
                //建议
                startActivity(new Intent(mContext, SuggestionActivity.class));
                break;
            case R.id.new_setting_about_our:
                //跳转到关于我们
//                startActivity(new Intent(mContext, AboutTbsActivity.class));
                Intent intent = new Intent(mContext, NewWebViewActivity.class);
                intent.putExtra("mLoadingUrl", "http://m.tobosu.com/introduce");
                mContext.startActivity(intent);
                break;
            case R.id.new_setting_version:
                //版本信息
                break;
        }
    }
}
