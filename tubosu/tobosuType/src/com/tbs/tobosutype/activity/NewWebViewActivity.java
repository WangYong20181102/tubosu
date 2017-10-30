package com.tbs.tobosutype.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.base.*;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新的webview页面
 */
public class NewWebViewActivity extends com.tbs.tobosutype.base.BaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_web_view);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
        mIntent = getIntent();
        newWebviewBannerRl.setBackgroundColor(Color.parseColor("#ffffff"));
        mLoadingUrl = mIntent.getStringExtra("mLoadingUrl");
        newWebviewWeb.getSettings().setJavaScriptEnabled(true);
        newWebviewWeb.setWebChromeClient(webChromeClient);
        newWebviewWeb.setWebViewClient(webViewClient);
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
