package com.tbs.tbs_mj.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.base.*;
import com.tbs.tbs_mj.bean._AppEvent;
import com.tbs.tbs_mj.utils.AppManager;
import com.tbs.tbs_mj.utils.SpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * creat by lin  发单跳转页面
 * 新的webview页面
 */
public class NewWebViewActivity extends com.tbs.tbs_mj.base.BaseActivity {

    @BindView(R.id.new_webview_back)
    LinearLayout newWebviewBack;
    @BindView(R.id.new_webview_title)
    TextView newWebviewTitle;
    @BindView(R.id.new_webview_web)
    WebView newWebviewWeb;
    @BindView(R.id.new_webview_banner_rl)
    RelativeLayout newWebviewBannerRl;

    private Context mContext;
    private String TAG = "NewWebViewActivity";
    private Intent mIntent;
    private String mLoadingUrl = "";//加载数据的URL
    private Gson mGson;
    private _AppEvent mAppEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_web_view);
        ButterKnife.bind(this);
        mContext = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViewEvent();
        SpUtil.setStatisticsEventPageId(mContext, mLoadingUrl);
    }

    //该页面为复用页面  根据不同的属性生成不同的数据 用于数据的统计
    @Override
    protected boolean havePageId() {
        return true;
    }


    private void initViewEvent() {
        mIntent = getIntent();
        mGson = new Gson();
        mAppEvent = new _AppEvent();
        newWebviewBannerRl.setBackgroundColor(Color.parseColor("#ffffff"));
        mLoadingUrl = mIntent.getStringExtra("mLoadingUrl");
        newWebviewWeb.getSettings().setJavaScriptEnabled(true);
        newWebviewWeb.getSettings().setBuiltInZoomControls(true);
        newWebviewWeb.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        newWebviewWeb.getSettings().setUseWideViewPort(true);
        newWebviewWeb.getSettings().setLoadWithOverviewMode(true);
        newWebviewWeb.getSettings().setSavePassword(true);
        newWebviewWeb.getSettings().setSaveFormData(true);
        newWebviewWeb.getSettings().setGeolocationEnabled(true);
        newWebviewWeb.getSettings().setGeolocationEnabled(true);
        newWebviewWeb.getSettings().setDomStorageEnabled(true);

        newWebviewWeb.setWebChromeClient(webChromeClient);
        newWebviewWeb.setWebViewClient(webViewClient);
        //统计用
        if (mLoadingUrl.contains("?")) {
            newWebviewWeb.loadUrl(mLoadingUrl + "&equipmentInfo=" + mGson.toJson(mAppEvent) + "&app_ref=" + AppManager.lastSecoundActivityName());
        } else {
            newWebviewWeb.loadUrl(mLoadingUrl + "?equipmentInfo=" + mGson.toJson(mAppEvent) + "&app_ref=" + AppManager.lastSecoundActivityName());
        }
        Log.e(TAG, "统计传值=====" + mLoadingUrl + "&equipmentInfo=" + mGson.toJson(mAppEvent) + "&app_ref=" + AppManager.lastSecoundActivityName());
        Log.e(TAG, "加载的链接=====" + mLoadingUrl);
    }

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    };
    private WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            newWebviewTitle.setText(title);
        }
    };

    @OnClick(R.id.new_webview_back)
    public void onViewClickedInNewWebView() {
        //返回按钮
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && newWebviewWeb.canGoBack()) {
            newWebviewWeb.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (newWebviewWeb != null) {
            newWebviewWeb.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            newWebviewWeb.clearHistory();

            ((ViewGroup) newWebviewWeb.getParent()).removeView(newWebviewWeb);
            newWebviewWeb.destroy();
            newWebviewWeb = null;
        }
        super.onDestroy();
    }
}
