package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseActivity;

public class WebViewActivity extends BaseActivity {
    private String TAG = "WebViewActivity";
    private Context mContext;


    private WebView webView;
    private TextView detailslideshow_title;
    private String title;
    private ImageView detail_share;
    private ImageView iv_back;
    private String url;
    private String currentUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mContext = this;
        initView();
        initData();
        initEvent();
    }

    private void initView() {

        webView = (WebView) findViewById(R.id.my_webview);
        detailslideshow_title = (TextView) findViewById(R.id.detailslideshow_title);
        detail_share = (ImageView) findViewById(R.id.detail_share);
        iv_back = (ImageView) findViewById(R.id.iv_back_webview_activity);
    }

    private void initData() {
        Intent intent = getIntent();
        url = intent.getStringExtra("web_url");
        Log.d(TAG, "传过来的url是" + url);
        webView.getSettings().setJavaScriptEnabled(true);


        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(url);
    }

    private void initEvent() {

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                title = view.getTitle();
                detailslideshow_title.setText(title);
                Log.d(TAG, "=====当前的url是 " + url);
                currentUrl = url;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                Log.d(TAG, "=====shouldOverrideUrlLoading前的url是 " + url);
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient());


        // 分享本页面到朋友圈去
        detail_share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //封装好的分享工具类
                Log.d(TAG, "====- 好了没有 " + url + "】");
                boolean flag = url.equals(currentUrl);
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
