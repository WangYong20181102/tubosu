package com.tobosu.mydecorate.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.base.BaseActivity;
import com.tobosu.mydecorate.bean._AppEvent;
import com.tobosu.mydecorate.util.AppManager;
import com.tobosu.mydecorate.util.SpUtil;
import com.umeng.socialize.utils.Log;

/**
 * 用来加载h5页面
 * WebViewActivity走的不是安卓发单入口
 *
 * @author dec
 */
public class WebViewActivity extends BaseActivity {
    private static final String TAG = WebViewActivity.class.getSimpleName();
    private Context mContext;

    private WebView webView;
    private TextView viewTitle;
    private String title;
    private ImageView iv_back;
    private String url;

    private Gson mGson;
    private _AppEvent mAppEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        mContext = WebViewActivity.this;
        mGson = new Gson();
        mAppEvent = new _AppEvent();

        initView();
        initData();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SpUtil.setStatisticsEventPageId(mContext, url);
    }

    @Override
    protected boolean havePageId() {
        return true;
    }

    private void initView() {

        webView = (WebView) findViewById(R.id.my_webview);
        viewTitle = (TextView) findViewById(R.id.web_title);
        iv_back = (ImageView) findViewById(R.id.iv_back_webview_activity);
    }


    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("WebView_Bundle");
        url = bundle.getString("link") + "?channel=seo&subchannel=zhjandroid&from=banner";
        Log.d(TAG, "传过来的url是" + url);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        if (url.contains("?")) {
            webView.loadUrl(url + "&equipmentInfo=" + mGson.toJson(mAppEvent) + "&app_ref=" + AppManager.lastSecoundActivityName());
        } else {
            webView.loadUrl(url + "?equipmentInfo=" + mGson.toJson(mAppEvent) + "&app_ref=" + AppManager.lastSecoundActivityName());
        }
        webView.loadUrl(url);
    }


    private void initEvent() {

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                viewTitle.setText(view.getTitle());

                System.out.println("webview=======" + url);
                if (url.contains("http://m.tobosu.com/app/success")) { // 原来是 equals的
                    Intent intent = new Intent(mContext, ApplyforSuccessActivity.class);
                    startActivityForResult(intent, 10);
                    finish();
                }

            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//				System.out.println("webview======="+url);
//				if (url.contains("http://m.tobosu.com/app/success")) { // 原来是 equals的
//					Intent intent = new Intent(mContext, ApplyforSuccessActivity.class);
//					startActivity(intent);
//					finish();
//				}
            }


        });

        webView.setWebChromeClient(new WebChromeClient());

        iv_back.setOnClickListener(new OnClickListener() {

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 66) {
            finish();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

}
