package com.tbs.tbs_mj.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.base.*;
import com.tbs.tbs_mj.bean._AppEvent;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.utils.AppManager;
import com.tbs.tbs_mj.utils.SpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ArticleWebViewActivity extends com.tbs.tbs_mj.base.BaseActivity {


    @BindView(R.id.art_webview_back)
    LinearLayout artWebviewBack;
    @BindView(R.id.art_webview_title)
    TextView artWebviewTitle;
    @BindView(R.id.art_webview_banner_rl)
    RelativeLayout artWebviewBannerRl;
    @BindView(R.id.art_webview_web)
    WebView artWebviewWeb;
    @BindView(R.id.art_webview_fadan)
    ImageView artWebviewFadan;

    private Context mContext;
    private String TAG = "NewWebViewActivity";
    private Intent mIntent;
    private String mLoadingUrl = "";//加载数据的URL
    private boolean isShowingFadan = false;
    private Gson mGson;
    private _AppEvent mAppEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_web_view);
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
        mIntent = getIntent();
        mGson = new Gson();
        mAppEvent = new _AppEvent();
        artWebviewBannerRl.setBackgroundColor(Color.parseColor("#ffffff"));
        mLoadingUrl = mIntent.getStringExtra("mLoadingUrl");
        artWebviewWeb.getSettings().setJavaScriptEnabled(true);
        artWebviewWeb.getSettings().setBuiltInZoomControls(true);
        artWebviewWeb.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        artWebviewWeb.getSettings().setUseWideViewPort(true);
        artWebviewWeb.getSettings().setLoadWithOverviewMode(true);
        artWebviewWeb.getSettings().setSavePassword(true);
        artWebviewWeb.getSettings().setSaveFormData(true);
        artWebviewWeb.getSettings().setGeolocationEnabled(true);
        artWebviewWeb.getSettings().setGeolocationEnabled(true);
        artWebviewWeb.getSettings().setDomStorageEnabled(true);

        artWebviewWeb.setWebChromeClient(webChromeClient);
        artWebviewWeb.setWebViewClient(webViewClient);
        artWebviewWeb.loadUrl(mLoadingUrl + "&equipmentInfo=" + mGson.toJson(mAppEvent)+ "&app_ref=" + AppManager.lastSecoundActivityName());
        Log.e(TAG, "统计传值=====" + mLoadingUrl + "&equipmentInfo=" + mGson.toJson(mAppEvent));
    }

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.e(TAG, "页面加载完成！！！");
            if (!isShowingFadan) {
                //加载发单动画
                isShowingFadan = true;
                showAnim();
            }
        }
    };

    //展示动画
    private void showAnim() {
        artWebviewFadan.setVisibility(View.VISIBLE);
        TranslateAnimation mAnimShowGif = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, -0.3f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        mAnimShowGif.setDuration(500);
        artWebviewFadan.startAnimation(mAnimShowGif);
    }

    private WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
//            artWebviewTitle.setText(title);
        }

    };


    @Override
    protected void onDestroy() {
        if (artWebviewWeb != null) {
            artWebviewWeb.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            artWebviewWeb.clearHistory();

            ((ViewGroup) artWebviewWeb.getParent()).removeView(artWebviewWeb);
            artWebviewWeb.destroy();
            artWebviewWeb = null;
        }
        super.onDestroy();
    }

    @OnClick({R.id.art_webview_back, R.id.art_webview_fadan})
    public void onViewClickedInArtWebViewActivity(View view) {
        switch (view.getId()) {
            case R.id.art_webview_back:
                finish();
                break;
            case R.id.art_webview_fadan:
                //跳转发单
                Intent intent = new Intent(mContext, NewWebViewActivity.class);
                intent.putExtra("mLoadingUrl",SpUtil.getzjzxaj29(mContext));
                mContext.startActivity(intent);
                break;
        }
    }
}
