package com.tobosu.mydecorate.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tobosu.mydecorate.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewWebViewActivity extends AppCompatActivity {

    @BindView(R.id.iv_back_webview_activity)
    ImageView ivBackWebviewActivity;
    @BindView(R.id.web_title)
    TextView webTitle;
    @BindView(R.id.my_webview)
    WebView myWebview;
    @BindView(R.id.new_web_view_rl)
    RelativeLayout newWebViewRl;
    @BindView(R.id.iv_back_webview_activity_rl)
    RelativeLayout ivBackWebviewActivityRl;


    private String TAG = "WebActivity";
    private Context mContext;
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
        webTitle.setBackgroundColor(Color.parseColor("#ffffff"));
        newWebViewRl.setBackgroundColor(Color.parseColor("#ffffff"));
        mLoadingUrl = mIntent.getStringExtra("mLoadingUrl");
        myWebview.getSettings().setJavaScriptEnabled(true);
        myWebview.getSettings().setBuiltInZoomControls(true);
        myWebview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        myWebview.getSettings().setUseWideViewPort(true);
        myWebview.getSettings().setLoadWithOverviewMode(true);
        myWebview.getSettings().setSavePassword(true);
        myWebview.getSettings().setSaveFormData(true);
        myWebview.getSettings().setGeolocationEnabled(true);
        myWebview.getSettings().setGeolocationEnabled(true);
        myWebview.getSettings().setDomStorageEnabled(true);

        myWebview.setWebChromeClient(webChromeClient);
        myWebview.setWebViewClient(webViewClient);
        myWebview.loadUrl(mLoadingUrl);
        Log.e(TAG, "加载数据的url================" + mLoadingUrl);
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
            webTitle.setText(title);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && myWebview.canGoBack()) {
            myWebview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (myWebview != null) {
            myWebview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            myWebview.clearHistory();
            ((ViewGroup) myWebview.getParent()).removeView(myWebview);
            myWebview.destroy();
            myWebview = null;
        }
        super.onDestroy();
    }

    @OnClick({R.id.iv_back_webview_activity, R.id.iv_back_webview_activity_rl})
    public void onViewClickedInNewWebViewActivity(View view) {
        switch (view.getId()) {
            case R.id.iv_back_webview_activity:
            case R.id.iv_back_webview_activity_rl:
                finish();
                break;
        }
    }
}
