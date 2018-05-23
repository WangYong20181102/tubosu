package com.tbs.tobosutype.web;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.NewWebViewActivity;
import com.tbs.tobosutype.bean._AppEvent;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.utils.AppManager;
import com.tbs.tobosutype.utils.SpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 推送H5页面显示的内容页面
 * 在App启动的情况下查看的页面
 * create by lin
 */
public class PushAppStartWebActivity extends com.tbs.tobosutype.base.BaseActivity {
    @BindView(R.id.pas_webview_back)
    LinearLayout pasWebviewBack;
    @BindView(R.id.pas_webview_title)
    TextView pasWebviewTitle;
    @BindView(R.id.pas_webview_banner_rl)
    RelativeLayout pasWebviewBannerRl;
    @BindView(R.id.pas_webview_web)
    WebView pasWebviewWeb;
    @BindView(R.id.push_start_fadan)
    ImageView pushStartFadan;

    private String TAG = "PushAppStartWebActivity";
    private Context mContext;
    private Intent mIntent;
    private String mLoadingUrl = "";//加载数据的URL
    private String mShowing = "";//是否显示发单按钮
    private String mEnableStatistics = "";//是否拼接点击流
    private Gson mGson;
    private _AppEvent mAppEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_app_start_web);
        ButterKnife.bind(this);
        mContext = this;

    }

    @Override
    protected void onResume() {
        super.onResume();
        initViewEvent();
        SpUtil.setStatisticsEventPageId(mContext, mLoadingUrl);
    }

    @Override
    protected boolean havePageId() {
        return true;
    }

    private void initViewEvent() {
        Log.e(TAG, "App正在前台运行当前启动的是===============" + TAG);
        mIntent = getIntent();
        mGson = new Gson();
        mAppEvent = new _AppEvent();
        pasWebviewBannerRl.setBackgroundColor(Color.parseColor("#ffffff"));
        mLoadingUrl = mIntent.getStringExtra("mLoadingUrl");
        mShowing = mIntent.getStringExtra("mShowing");
        mEnableStatistics = mIntent.getStringExtra("mEnableStatistics");
        if (mShowing.equals("1")) {
            //显示
            pushStartFadan.setVisibility(View.VISIBLE);
            showAnim();
        } else {
            pushStartFadan.setVisibility(View.GONE);
        }
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
        //统计用
        if (mLoadingUrl.contains("?")) {
            if (mEnableStatistics != null && mEnableStatistics.equals("0")) {
                //不拼接
                pasWebviewWeb.loadUrl(mLoadingUrl);
                Log.e(TAG, "bu拼接===============" + mEnableStatistics);
            } else {
                //拼接
                pasWebviewWeb.loadUrl(mLoadingUrl + "&equipmentInfo=" + mGson.toJson(mAppEvent) + "&app_ref=" + AppManager.lastSecoundActivityName());
                Log.e(TAG, "拼接===============" + mEnableStatistics);
            }
        } else {
            if (mEnableStatistics != null && mEnableStatistics.equals("0")) {
                //不拼接
                pasWebviewWeb.loadUrl(mLoadingUrl);
                Log.e(TAG, "bu拼接===============" + mEnableStatistics);
            } else {
                //拼接
                pasWebviewWeb.loadUrl(mLoadingUrl + "?equipmentInfo=" + mGson.toJson(mAppEvent) + "&app_ref=" + AppManager.lastSecoundActivityName());
                Log.e(TAG, "拼接===============" + mEnableStatistics);
            }
        }
        Log.e(TAG, "测试传值和H5交互========" + mLoadingUrl + "&equipmentInfo=" + mGson.toJson(mAppEvent) + "&app_ref=" + AppManager.lastSecoundActivityName());
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

    //展示动画
    private void showAnim() {
        pushStartFadan.setVisibility(View.VISIBLE);
        TranslateAnimation mAnimShowGif = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, -0.3f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        mAnimShowGif.setDuration(500);
        pushStartFadan.startAnimation(mAnimShowGif);
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


    @OnClick({R.id.pas_webview_back, R.id.push_start_fadan})
    public void onViewClickedInPASActivity(View view) {
        switch (view.getId()) {
            case R.id.pas_webview_back:
                finish();
                break;
            case R.id.push_start_fadan:
                //跳转到发单页面
                Intent intent = new Intent(mContext, NewWebViewActivity.class);
                intent.putExtra("mLoadingUrl", SpUtil.getTbsAj31(mContext));
                mContext.startActivity(intent);
                break;
        }
    }
}
