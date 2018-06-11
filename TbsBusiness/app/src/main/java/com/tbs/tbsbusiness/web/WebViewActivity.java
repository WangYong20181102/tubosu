package com.tbs.tbsbusiness.web;

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
import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebViewActivity extends BaseActivity {
    @BindView(R.id.new_webview_back)
    LinearLayout newWebviewBack;
    @BindView(R.id.new_webview_title)
    TextView newWebviewTitle;
    @BindView(R.id.new_webview_banner_rl)
    RelativeLayout newWebviewBannerRl;
    @BindView(R.id.new_webview_web)
    WebView newWebviewWeb;
    private Context mContext;
    private String TAG = "WebViewActivity";
    private Intent mIntent;
    private String mLoadingUrl = "";//加载数据的URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
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
        newWebviewWeb.loadUrl(mLoadingUrl);
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
    public void onViewClicked() {
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
