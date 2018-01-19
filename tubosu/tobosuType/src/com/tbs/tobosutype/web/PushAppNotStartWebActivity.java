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
 * 在App未启动的情况下查看的页面
 * create by lin
 */
public class PushAppNotStartWebActivity extends AppCompatActivity {

    @BindView(R.id.pans_webview_back)
    LinearLayout pansWebviewBack;
    @BindView(R.id.pans_webview_title)
    TextView pansWebviewTitle;
    @BindView(R.id.pans_webview_banner_rl)
    RelativeLayout pansWebviewBannerRl;
    @BindView(R.id.pans_webview_web)
    WebView pansWebviewWeb;

    private String TAG = "PushAppNotStartWebActivity";
    private Context mContext;
    private Intent mIntent;
    private String mLoadingUrl = "";//加载数据的URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_app_not_start_web);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
        Log.e(TAG, "App未启动当前启动的是===============" + TAG);
        mIntent = getIntent();
        pansWebviewBannerRl.setBackgroundColor(Color.parseColor("#ffffff"));
        mLoadingUrl = mIntent.getStringExtra("mLoadingUrl");
        pansWebviewWeb.getSettings().setJavaScriptEnabled(true);
        pansWebviewWeb.getSettings().setBuiltInZoomControls(true);
        pansWebviewWeb.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        pansWebviewWeb.getSettings().setUseWideViewPort(true);
        pansWebviewWeb.getSettings().setLoadWithOverviewMode(true);
        pansWebviewWeb.getSettings().setSavePassword(true);
        pansWebviewWeb.getSettings().setSaveFormData(true);
        pansWebviewWeb.getSettings().setGeolocationEnabled(true);
        pansWebviewWeb.getSettings().setGeolocationEnabled(true);
        pansWebviewWeb.getSettings().setDomStorageEnabled(true);

        pansWebviewWeb.setWebChromeClient(webChromeClient);
        pansWebviewWeb.setWebViewClient(webViewClient);
        pansWebviewWeb.loadUrl(mLoadingUrl);
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
            pansWebviewTitle.setText(title);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && pansWebviewWeb.canGoBack()) {
            pansWebviewWeb.goBack();
            return true;
        } else {
            //启动App
            startActivity(new Intent(mContext, WelcomeActivity.class));
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (pansWebviewWeb != null) {
            pansWebviewWeb.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            pansWebviewWeb.clearHistory();

            ((ViewGroup) pansWebviewWeb.getParent()).removeView(pansWebviewWeb);
            pansWebviewWeb.destroy();
            pansWebviewWeb = null;
        }
        super.onDestroy();
    }

    @OnClick(R.id.pans_webview_back)
    public void onViewClickedInPansActivity() {
        //返回 启动App
        startActivity(new Intent(mContext, WelcomeActivity.class));
        finish();
    }
}
