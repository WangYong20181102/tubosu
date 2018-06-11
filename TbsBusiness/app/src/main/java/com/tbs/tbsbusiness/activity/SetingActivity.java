package com.tbs.tbsbusiness.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.base.BaseActivity;
import com.tbs.tbsbusiness.util.Util;
import com.tbs.tbsbusiness.web.WebViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetingActivity extends BaseActivity {
    @BindView(R.id.new_setting_back)
    RelativeLayout newSettingBack;
    @BindView(R.id.new_setting_cache)
    TextView newSettingCache;
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
    private Context mContext;
    private String TAG = "SetingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seting);
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

    private void showCache() {
        try {
            if ("0.0Byte".equals(Util.getTotalCacheSize(getApplicationContext()))) {
                newSettingCache.setText("0 M");
            } else {
                newSettingCache.setText(Util.getTotalCacheSize(getApplicationContext()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showVersion() {
        newSettingVersion.setText("v " + Util.getAppVersionName(mContext));
    }

    @OnClick({R.id.new_setting_back, R.id.new_setting_cache, R.id.new_setting_clean_cache, R.id.new_setting_suggestion, R.id.new_setting_about_our, R.id.new_setting_version, R.id.banner_dever})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.new_setting_back:
                finish();
                break;
            case R.id.new_setting_cache:
                break;
            case R.id.new_setting_clean_cache:
                //清理缓存
                if (!"0 M".equals(newSettingCache.getText())) {
                    Util.clearAllCache(getApplicationContext());
                    newSettingCache.setText("0 M");
                    Toast.makeText(mContext, "缓存已清理", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "已清理至最佳状态", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.new_setting_suggestion:
                startActivity(new Intent(mContext, SuggestionActivity.class));
                break;
            case R.id.new_setting_about_our:
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("mLoadingUrl", "http://m.tobosu.com/introduce");
                mContext.startActivity(intent);
                break;
            case R.id.new_setting_version:
                break;
            case R.id.banner_dever:
                break;
        }
    }
}
