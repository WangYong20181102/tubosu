package com.tbs.tbs_mj.web;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.activity.MainActivity;
import com.tbs.tbs_mj.activity.PopOrderActivity;
import com.tbs.tbs_mj.base.BaseActivity;
import com.tbs.tbs_mj.bean._AppEvent;
import com.tbs.tbs_mj.utils.AppManager;
import com.tbs.tbs_mj.utils.CacheManager;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdvWebActivity extends BaseActivity {

    @BindView(R.id.adv_webview_back)
    LinearLayout advWebviewBack;
    @BindView(R.id.adv_webview_title)
    TextView advWebviewTitle;
    @BindView(R.id.adv_webview_banner_rl)
    RelativeLayout advWebviewBannerRl;
    @BindView(R.id.adv_webview_web)
    WebView advWebviewWeb;
    @BindView(R.id.adv_webview_share)
    RelativeLayout advWebviewShare;
    private Context mContext;
    private String TAG = "AdvWebActivity";
    private Intent mIntent;
    private String mLoadingUrl = "";//加载数据的URL
    private Gson mGson;
    private _AppEvent mAppEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv_web);
        ButterKnife.bind(this);
        mContext = this;

    }

    @Override
    protected void onResume() {
        super.onResume();
        initViewEvent();
    }

    private void initViewEvent() {
        mIntent = getIntent();
        mGson = new Gson();
        mAppEvent = new _AppEvent();
        advWebviewBannerRl.setBackgroundColor(Color.parseColor("#ffffff"));
        mLoadingUrl = mIntent.getStringExtra("mLoadingUrl");
        advWebviewWeb.getSettings().setJavaScriptEnabled(true);
        //支持缩放
        advWebviewWeb.getSettings().setBuiltInZoomControls(true);
        //自适应屏幕
        advWebviewWeb.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        advWebviewWeb.getSettings().setUseWideViewPort(true);
        advWebviewWeb.getSettings().setLoadWithOverviewMode(true);
        advWebviewWeb.getSettings().setSavePassword(true);
        advWebviewWeb.getSettings().setSaveFormData(true);
        advWebviewWeb.getSettings().setGeolocationEnabled(true);
        advWebviewWeb.getSettings().setDomStorageEnabled(true);

        advWebviewWeb.setWebChromeClient(webChromeClient);
        advWebviewWeb.setWebViewClient(webViewClient);
        //统计用
        if (mLoadingUrl.contains("?")) {
            advWebviewWeb.loadUrl(mLoadingUrl + "&equipmentInfo=" + mGson.toJson(mAppEvent) + "&app_ref=" + AppManager.lastSecoundActivityName());
        } else {
            advWebviewWeb.loadUrl(mLoadingUrl + "?equipmentInfo=" + mGson.toJson(mAppEvent) + "&app_ref=" + AppManager.lastSecoundActivityName());
        }
    }

    private WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            advWebviewTitle.setText(title);
        }
    };

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && advWebviewWeb.canGoBack()) {
            advWebviewWeb.goBack();
            return true;
        } else {
            intoMainActivity();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void intoMainActivity() {
        Log.e(TAG, "跳转到首页=====================");
        if ("".equals(CacheManager.getAppEntryOrderPre(mContext))) {
            //初次进入我们的App进入发单页面
            CacheManager.setAppEntryOrderPre(mContext, "abc"); // 标识已经进入过发单页面
//            Intent intent = new Intent(mContext, GuideOneActivity.class);
            Intent intent = new Intent(mContext, PopOrderActivity.class);
            startActivity(intent);
        } else {
            startActivity(new Intent(mContext, MainActivity.class));
        }
        finish();
        System.gc();
    }

    @OnClick({R.id.adv_webview_back, R.id.adv_webview_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.adv_webview_back:
                Log.e(TAG, "跳转到主页===================");
                intoMainActivity();
                break;
            case R.id.adv_webview_share:
                shareWeb();
                break;
        }
    }

    private void shareWeb() {
        UMWeb web = new UMWeb(mLoadingUrl);
        web.setThumb(new UMImage(mContext, R.drawable.app_share));
        //分享相关的标题以及描述
        if (mLoadingUrl.contains("free_price_page")) {
            //免费报价
            web.setTitle("免费获取装修预算");
            web.setDescription("10秒估算装修报价，给您专业、公正、透明的装修报价！");
        } else if (mLoadingUrl.contains("quote")) {
            //免费设计
            web.setTitle("4套装修设计方案，0元领");
            web.setDescription("现在预约，即可免费获得4套装修设计方案！");
        } else if (mLoadingUrl.contains("rec_company")) {
            //专业推荐
            web.setTitle("智能推荐装修公司");
            web.setDescription("无法分辨装修公司好坏？专业顾问，为您推荐！");
        } else if (mLoadingUrl.contains("free_design")) {
            //装修秘籍
            web.setTitle("0元搞定全屋设计");
            web.setDescription("全国限额2000名，免费进行全屋设计！");
        } else if (mLoadingUrl.contains("company_gift")) {
            //装修礼包
            web.setTitle("装修大礼包");
            web.setDescription("土拨鼠10周年庆，三重装修大礼回馈业主！");
        } else {
            web.setTitle("住家装修");
            web.setDescription("" + advWebviewTitle.getText());
        }

        new ShareAction(this).withMedia(web)
                .setDisplayList(SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ)
                .setCallback(umShareListener).open();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

        }
    };
}
