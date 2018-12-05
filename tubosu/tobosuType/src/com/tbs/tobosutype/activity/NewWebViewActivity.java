package com.tbs.tobosutype.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.bean._AppEvent;
import com.tbs.tobosutype.utils.AppManager;
import com.tbs.tobosutype.utils.ShareUtil;
import com.tbs.tobosutype.utils.SpUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * creat by lin  发单跳转页面
 * 新的webview页面
 */
public class NewWebViewActivity extends BaseActivity {

    @BindView(R.id.new_webview_back)
    LinearLayout newWebviewBack;
    @BindView(R.id.new_webview_title)
    TextView newWebviewTitle;
    @BindView(R.id.new_webview_web)
    WebView newWebviewWeb;
    @BindView(R.id.new_webview_banner_rl)
    RelativeLayout newWebviewBannerRl;
    @BindView(R.id.ll_share)
    LinearLayout llShare;

    private Context mContext;
    private String TAG = "NewWebViewActivity";
    private Intent mIntent;
    private String mLoadingUrl = "";//加载数据的URL
    private boolean b = false;  //false不拼接，true拼接
    private Gson mGson;
    private _AppEvent mAppEvent;
    private String tittle = "";//标题

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
        b = mIntent.getBooleanExtra("bAnswer", false);
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
        if (!b) {
            //统计用
            if (mLoadingUrl.contains("?")) {
                mLoadingUrl = mLoadingUrl + "&equipmentInfo=" + mGson.toJson(mAppEvent) + "&app_ref=" + AppManager.lastSecoundActivityName();
            } else {
                mLoadingUrl = mLoadingUrl + "?equipmentInfo=" + mGson.toJson(mAppEvent) + "&app_ref=" + AppManager.lastSecoundActivityName();
            }
        }
        newWebviewWeb.loadUrl(mLoadingUrl);
        Log.e(TAG, "统计传值=====" + mLoadingUrl + "&equipmentInfo=" + mGson.toJson(mAppEvent) + "&app_ref=" + AppManager.lastSecoundActivityName());
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
            tittle = title;
            newWebviewTitle.setText(title);
        }
    };

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

    @OnClick({R.id.new_webview_back, R.id.ll_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.new_webview_back:
                //返回按钮
                finish();
                break;
            case R.id.ll_share: //分享
                new ShareUtil(this, tittle, "嘿，好东西分享给你，快打开看看", "", mLoadingUrl);
                break;
        }
    }
}
