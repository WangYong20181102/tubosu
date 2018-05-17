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
import com.tbs.tobosutype.activity.WelcomeActivity;
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.bean._AppEvent;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.utils.AppManager;
import com.tbs.tobosutype.utils.SpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 推送H5页面显示的内容页面
 * 在App未启动的情况下查看的页面
 * 1.报价推送  所以得关了右侧的报价按钮
 * 2.文章详情  弹出右侧的报价按钮
 * <p>
 * create by lin
 */
public class PushAppNotStartWebActivity extends BaseActivity {

    @BindView(R.id.pans_webview_back)
    LinearLayout pansWebviewBack;
    @BindView(R.id.pans_webview_title)
    TextView pansWebviewTitle;
    @BindView(R.id.pans_webview_banner_rl)
    RelativeLayout pansWebviewBannerRl;
    @BindView(R.id.pans_webview_web)
    WebView pansWebviewWeb;
    @BindView(R.id.push_not_start_fadan)
    ImageView pushNotStartFadan;

    private String TAG = "PushAppNotStartWebActivity";
    private Context mContext;
    private Intent mIntent;
    private String mLoadingUrl = "";//加载数据的URL
    private String mShowing = "";//是否显示单按钮  默认显示
    private String mEnableStatistics = "";//是否拼接点击流
    private Gson mGson;
    private _AppEvent mAppEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_app_not_start_web);
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
        Log.e(TAG, "App未启动当前启动的是===============" + TAG);
        mIntent = getIntent();
        mGson = new Gson();
        mAppEvent = new _AppEvent();
        pansWebviewBannerRl.setBackgroundColor(Color.parseColor("#ffffff"));
        mLoadingUrl = mIntent.getStringExtra("mLoadingUrl");
        mShowing = mIntent.getStringExtra("mShowing");
        mEnableStatistics = mIntent.getStringExtra("mEnableStatistics");
        if (mShowing.equals("1")) {
            //显示
            pushNotStartFadan.setVisibility(View.VISIBLE);
            showAnim();
        } else {
            pushNotStartFadan.setVisibility(View.GONE);
        }
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
        //统计用
        if (mLoadingUrl.contains("?")) {
            if (mEnableStatistics != null && mEnableStatistics.equals("0")) {
                //不拼接
                pansWebviewWeb.loadUrl(mLoadingUrl);
                Log.e(TAG, "bu拼接==============="+mEnableStatistics);
            } else {
                //拼接
                pansWebviewWeb.loadUrl(mLoadingUrl + "&equipmentInfo=" + mGson.toJson(mAppEvent) + "&app_ref=" + AppManager.lastSecoundActivityName());
                Log.e(TAG, "拼接==============="+mEnableStatistics);
            }
        } else {
            if (mEnableStatistics != null && mEnableStatistics.equals("0")) {
                //不拼接
                pansWebviewWeb.loadUrl(mLoadingUrl);
                Log.e(TAG, "bu拼接==============="+mEnableStatistics);
            } else {
                //拼接
                pansWebviewWeb.loadUrl(mLoadingUrl + "?equipmentInfo=" + mGson.toJson(mAppEvent) + "&app_ref=" + AppManager.lastSecoundActivityName());
                Log.e(TAG, "拼接==============="+mEnableStatistics);
            }
        }
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

    //展示动画
    private void showAnim() {
        pushNotStartFadan.setVisibility(View.VISIBLE);
        TranslateAnimation mAnimShowGif = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, -0.3f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        mAnimShowGif.setDuration(500);
        pushNotStartFadan.startAnimation(mAnimShowGif);
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


    @OnClick({R.id.pans_webview_back, R.id.push_not_start_fadan})
    public void onViewClickedInAppNotStartWebActivity(View view) {
        switch (view.getId()) {
            case R.id.pans_webview_back:
                //返回 启动App
                startActivity(new Intent(mContext, WelcomeActivity.class));
                finish();
                break;
            case R.id.push_not_start_fadan:
                //跳转到发单页面
                Intent intent = new Intent(mContext, NewWebViewActivity.class);
                intent.putExtra("mLoadingUrl", Constant.QUOTE);
                mContext.startActivity(intent);
                break;
        }
    }
}
