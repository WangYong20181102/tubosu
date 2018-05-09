package com.tbs.tbs_mj.web;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.activity.MainActivity;
import com.tbs.tbs_mj.activity.PopOrderActivity;
import com.tbs.tbs_mj.base.BaseActivity;
import com.tbs.tbs_mj.bean._AppEvent;
import com.tbs.tbs_mj.utils.AppManager;
import com.tbs.tbs_mj.utils.CacheManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdvWebActivity extends BaseActivity {

    @BindView(R.id.adv_webview_back)
    LinearLayout advWebviewBack;
    @BindView(R.id.adv_webview_title)
    TextView advWebviewTitle;
    @BindView(R.id.adv_webview_banner_rl)
    RelativeLayout advWebviewBannerRl;
    @BindView(R.id.adv_webview_web)
    WebView advWebviewWeb;
    private Context mContext;
    private String TAG = "AdvWebActivity";
    private Intent mIntent;
    private String mLoadingUrl = "";//加载数据的URL
    private Gson mGson;
    private _AppEvent mAppEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv_web);
        ButterKnife.bind(this);
        mContext = this;

    }

    @Override
    protected void onResume() {
        super.onResume();
        initViewEvent();
    }

    private void initViewEvent() {
        mIntent = getIntent();
        mGson = new Gson();
        mAppEvent = new _AppEvent();
        advWebviewBannerRl.setBackgroundColor(Color.parseColor("#ffffff"));
        mLoadingUrl = mIntent.getStringExtra("mLoadingUrl");
        advWebviewWeb.getSettings().setJavaScriptEnabled(true);
        advWebviewWeb.getSettings().setBuiltInZoomControls(true);
        advWebviewWeb.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        advWebviewWeb.getSettings().setUseWideViewPort(true);
        advWebviewWeb.getSettings().setLoadWithOverviewMode(true);
        advWebviewWeb.getSettings().setSavePassword(true);
        advWebviewWeb.getSettings().setSaveFormData(true);
        advWebviewWeb.getSettings().setGeolocationEnabled(true);
        advWebviewWeb.getSettings().setGeolocationEnabled(true);
        advWebviewWeb.getSettings().setDomStorageEnabled(true);

        advWebviewWeb.setWebChromeClient(webChromeClient);
        advWebviewWeb.setWebViewClient(webViewClient);
        //统计用
        if (mLoadingUrl.contains("?")) {
            advWebviewWeb.loadUrl(mLoadingUrl + "&equipmentInfo=" + mGson.toJson(mAppEvent) + "&app_ref=" + AppManager.lastSecoundActivityName());
        } else {
            advWebviewWeb.loadUrl(mLoadingUrl + "?equipmentInfo=" + mGson.toJson(mAppEvent) + "&app_ref=" + AppManager.lastSecoundActivityName());
        }
    }

    private WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            advWebviewTitle.setText(title);
        }
    };

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    };

    @OnClick(R.id.adv_webview_back)
    public void onViewClickedOnAdvActivity() {
        Log.e(TAG, "跳转到主页===================");
        intoMainActivity();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && advWebviewWeb.canGoBack()) {
            advWebviewWeb.goBack();
            return true;
        } else {
            intoMainActivity();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void intoMainActivity() {
        Log.e(TAG, "跳转到首页=====================");
        if ("".equals(CacheManager.getAppEntryOrderPre(mContext))) {
            //初次进入我们的App进入发单页面
            CacheManager.setAppEntryOrderPre(mContext, "abc"); // 标识已经进入过发单页面
            Intent intent = new Intent(mContext, PopOrderActivity.class);
            startActivity(intent);
        } else {
            startActivity(new Intent(mContext, MainActivity.class));
        }
        finish();
        System.gc();
    }

}
