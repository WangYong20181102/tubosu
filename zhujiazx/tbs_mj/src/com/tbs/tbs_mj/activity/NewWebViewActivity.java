package com.tbs.tbs_mj.activity;


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
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.base.BaseActivity;
import com.tbs.tbs_mj.bean._AppEvent;
import com.tbs.tbs_mj.utils.AppManager;
import com.tbs.tbs_mj.utils.SpUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
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
    @BindView(R.id.new_webview_share)
    RelativeLayout newWebviewShare;

    private Context mContext;
    private String TAG = "NewWebViewActivity";
    private Intent mIntent;
    private String mLoadingUrl = "";//加载数据的URL
    private Gson mGson;
    private _AppEvent mAppEvent;


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
        //统计用
        if (mLoadingUrl.contains("?")) {
            newWebviewWeb.loadUrl(mLoadingUrl + "&equipmentInfo=" + mGson.toJson(mAppEvent) + "&app_ref=" + AppManager.lastSecoundActivityName());
        } else {
            newWebviewWeb.loadUrl(mLoadingUrl + "?equipmentInfo=" + mGson.toJson(mAppEvent) + "&app_ref=" + AppManager.lastSecoundActivityName());
        }
        Log.e(TAG, "统计传值=====" + mLoadingUrl + "&equipmentInfo=" + mGson.toJson(mAppEvent) + "&app_ref=" + AppManager.lastSecoundActivityName());
        Log.e(TAG, "加载的链接=====" + mLoadingUrl);
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
            newWebviewTitle.setText(title);
        }
    };

    @OnClick({R.id.new_webview_back, R.id.new_webview_share})
    public void onViewClickedInNewWebView(View view) {
        switch (view.getId()) {
            case R.id.new_webview_back:
                //返回按钮
                finish();
                break;
            case R.id.new_webview_share:
                //进行页面的分享
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
            web.setDescription("" + newWebviewTitle.getText());
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
}
