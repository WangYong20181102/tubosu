package com.tbs.tobosutype.web;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.WelcomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 推送H5页面显示的内容页面
 * 在App启动的情况下查看的页面
 * create by lin
 */
public class PushAppStartWebActivity extends AppCompatActivity {
    @BindView(R.id.pas_webview_back)
    LinearLayout pasWebviewBack;
    @BindView(R.id.pas_webview_title)
    TextView pasWebviewTitle;
    @BindView(R.id.pas_webview_banner_rl)
    RelativeLayout pasWebviewBannerRl;
    @BindView(R.id.pas_webview_web)
    WebView pasWebviewWeb;

    private String TAG = "PushAppStartWebActivity";
    private Context mContext;
    private Intent mIntent;
    private String mLoadingUrl = "";//加载数据的URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_app_start_web);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
        Log.e(TAG, "App正在前台运行当前启动的是===============" + TAG);
        mIntent = getIntent();
        pasWebviewBannerRl.setBackgroundColor(Color.parseColor("#ffffff"));
        mLoadingUrl = mIntent.getStringExtra("mLoadingUrl");
        pasWebviewWeb.getSettings().setJavaScriptEnabled(true);
        pasWebviewWeb.getSettings().setBuiltInZoomControls(true);
        pasWebviewWeb.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        pasWebviewWeb.getSettings().setUseWideViewPort(true);
        pasWebviewWeb.getSettings().setLoadWithOverviewMode(true);
        pasWebviewWeb.getSettings().setSavePassword(true);
        pasWebviewWeb.getSettings().setSaveFormData(true);
        pasWebviewWeb.getSettings().setGeolocationEnabled(true);
        pasWebviewWeb.getSettings().setGeolocationEnabled(true);
        pasWebviewWeb.getSettings().setDomStorageEnabled(true);

        pasWebviewWeb.setWebChromeClient(webChromeClient);
        pasWebviewWeb.setWebViewClient(webViewClient);
        pasWebviewWeb.loadUrl(mLoadingUrl);
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
            pasWebviewTitle.setText(title);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && pasWebviewWeb.canGoBack()) {
            pasWebviewWeb.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (pasWebviewWeb != null) {
            pasWebviewWeb.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            pasWebviewWeb.clearHistory();

            ((ViewGroup) pasWebviewWeb.getParent()).removeView(pasWebviewWeb);
            pasWebviewWeb.destroy();
            pasWebviewWeb = null;
        }
        super.onDestroy();
    }


    @OnClick(R.id.pas_webview_back)
    public void onViewClickedInPASActivity() {
        finish();
    }
}
