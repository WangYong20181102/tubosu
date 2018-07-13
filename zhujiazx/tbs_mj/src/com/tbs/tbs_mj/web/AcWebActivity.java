package com.tbs.tbs_mj.web;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.activity.MainActivity;
import com.tbs.tbs_mj.activity.NewLoginActivity;
import com.tbs.tbs_mj.activity.WelcomeActivity;
import com.tbs.tbs_mj.base.BaseActivity;
import com.tbs.tbs_mj.bean.EC;
import com.tbs.tbs_mj.bean.Event;
import com.tbs.tbs_mj.bean._AppEvent;
import com.tbs.tbs_mj.utils.AppInfoUtil;
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
 * 活动页面
 * 用于和H5前端联调的活动页面
 */

public class AcWebActivity extends BaseActivity {
    @BindView(R.id.ac_webview_back_ll)
    LinearLayout acWebviewBackLl;
    @BindView(R.id.ac_webview_title_tv)
    TextView acWebviewTitleTv;
    @BindView(R.id.ac_webview_banner_rl)
    RelativeLayout acWebviewBannerRl;
    @BindView(R.id.ac_webview_html)
    WebView acWebviewHtml;
    @BindView(R.id.ac_webview_share)
    RelativeLayout acWebviewShare;
    private String TAG = "AcWebActivity";
    private Context mContext;
    private Intent mIntent;
    private String mLoadingUrl = "";//加载数据的URL
    private String mWhereFrom = "";//从那个页面进来
    private Gson mGson;
    private _AppEvent mAppEvent;
    private WebSettings mWebSettings;
    private final int mVersion = Build.VERSION.SDK_INT;
    //App调用H5的方法名1
    private String H5FunctionName1 = "sendUserId";
    //App调用H5的参数
    private String H5FunctionParam1 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac_web);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected boolean havePageId() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.CLOSE_NEW_LOGIN_ACTIVITY:
                //用户登录成功将重载网络
                initView();
                break;
        }
    }

    private void initView() {
        mIntent = getIntent();
        mGson = new Gson();
        mAppEvent = new _AppEvent();
        mLoadingUrl = mIntent.getStringExtra("mLoadingUrl");
        mWhereFrom = mIntent.getStringExtra("mWhereFrom");
        initWebView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SpUtil.setStatisticsEventPageId(mContext, mLoadingUrl);
    }

    private void initWebView() {
        mWebSettings = acWebviewHtml.getSettings();
        //设置允许js交互
        mWebSettings.setJavaScriptEnabled(true);
        //支持缩放
        mWebSettings.setBuiltInZoomControls(true);
        //自适应屏幕
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setSavePassword(true);
        mWebSettings.setSaveFormData(true);
        mWebSettings.setGeolocationEnabled(true);
        mWebSettings.setDomStorageEnabled(true);
        //设置响应js的弹窗
        acWebviewHtml.setWebChromeClient(mWebChromeClient);
        acWebviewHtml.setWebViewClient(webViewClient);
        //在加载页面时由前端调起发送用户的userid
        acWebviewHtml.addJavascriptInterface(new GetAppUserId(), "linkAppGetUserId");
        //前端调起 原生登录
        acWebviewHtml.addJavascriptInterface(new AppLogin(), "linkAppCallAppLogin");
        //载入页面
//        acWebviewHtml.loadUrl("http://m.dev.tobosu.com/test_app/");
        //统计时要用的玩意
        if (mLoadingUrl.contains("?")) {
//            acWebviewHtml.loadUrl(mLoadingUrl + "&equipmentInfo=" + mGson.toJson(mAppEvent) + "&app_ref=" + AppManager.lastSecoundActivityName() + "&userid=" + "\"" + AppInfoUtil.getUserid(mContext) + "\"");
            acWebviewHtml.loadUrl(mLoadingUrl + "&equipmentInfo=" + mGson.toJson(mAppEvent) + "&app_ref=" + AppManager.lastSecoundActivityName() + "&userid=" + AppInfoUtil.getUserid(mContext));
        } else {
//            acWebviewHtml.loadUrl(mLoadingUrl + "?equipmentInfo=" + mGson.toJson(mAppEvent) + "&app_ref=" + AppManager.lastSecoundActivityName() + "&userid=" + "\"" + AppInfoUtil.getUserid(mContext) + "\"");
            acWebviewHtml.loadUrl(mLoadingUrl + "?equipmentInfo=" + mGson.toJson(mAppEvent) + "&app_ref=" + AppManager.lastSecoundActivityName() + "&userid=" + AppInfoUtil.getUserid(mContext));
        }
        Log.e(TAG, "链接拼接===========" + acWebviewHtml.getUrl());
    }

    //设置响应
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            acWebviewTitleTv.setText("" + title);
        }
    };
    //内部加载外链接
    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    };

    @OnClick({R.id.ac_webview_back_ll, R.id.ac_webview_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ac_webview_back_ll:
                goBack();
                break;
            case R.id.ac_webview_share:
                //分享
                shareWeb();
                break;
        }
    }


    //回退逻辑
    private void goBack() {
        if (mWhereFrom != null
                && !TextUtils.isEmpty(mWhereFrom)) {
            if (mWhereFrom.equals("MyXiaomiPushReceiver")) {
                //从推送进来的———点击回退键启动App
                startActivity(new Intent(mContext, WelcomeActivity.class));
                finish();
            } else if (mWhereFrom.equals("LoadingActivity")) {
                //从闪屏页面进来的
                startActivity(new Intent(mContext, MainActivity.class));
                finish();
            }

        } else {
            finish();
        }
    }

    private void shareWeb() {
        if (AppInfoUtil.getUserid(mContext) != null
                && !TextUtils.isEmpty(AppInfoUtil.getUserid(mContext))) {
            //进行用户的分享
            UMWeb web;
            if (mLoadingUrl.contains("?")) {
                web = new UMWeb(mLoadingUrl + "&userid=" + AppInfoUtil.getUserid(mContext));
            } else {
                web = new UMWeb(mLoadingUrl + "?userid=" + AppInfoUtil.getUserid(mContext));
            }

            web.setThumb(new UMImage(mContext, R.drawable.ten_years_share));
            web.setTitle("土拨鼠10周年庆，签约抢1000万豪礼，这可能是今年最大的一波");
            web.setDescription("邀请好友完成签约拿双重豪礼，不信你可以试试哟~");
            new ShareAction(this).withMedia(web)
                    .setDisplayList(SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ)
                    .setCallback(umShareListener).open();
        } else {
            //调起原生的登录
            Toast.makeText(mContext, "您还没有登录，请登录后再分享~", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(mContext, NewLoginActivity.class));
        }
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

    // 由前端主动调起  向前端发送用户的id方法
    private class GetAppUserId extends Object {
        @JavascriptInterface
        public void getAppUserId() {
            Log.e(TAG, "====getAppUserId===前端通信调起，通知App传id==========");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //将数据传送给前端
                    sendUserId();
                }
            });
        }
    }

    private void sendUserId() {
        H5FunctionParam1 = "'{\"value\":{\"userId\":" + AppInfoUtil.getUserid(mContext) + "}}'";
        if (mVersion >= Build.VERSION_CODES.KITKAT) {
            //Android版本大于等于19
//            Log.e(TAG, "====javascript:appCallback===App调起前端方法传id给前端==========" + H5FunctionParam1);
            // TODO: 2018/7/5  暂时写一个js的方法可以根据前端的方法修改
//            acWebviewHtml.evaluateJavascript("javascript:appCallback('" + H5FunctionName1 + "," + H5FunctionParam1 + "')", new ValueCallback<String>() {
//            acWebviewHtml.evaluateJavascript("javascript:appCallback('" + H5FunctionName1 + "','" + H5FunctionParam1 + "')", new ValueCallback<String>() {
            acWebviewHtml.evaluateJavascript("javascript:appCallback('" + AppInfoUtil.getUserid(mContext) + "')", new ValueCallback<String>() {

                @Override
                public void onReceiveValue(String value) {
                    Log.e(TAG, "联调返回值=====" + value);
                }
            });
        } else {
            //Android版本小于19
            acWebviewHtml.loadUrl("javascript:appCallback('" + AppInfoUtil.getUserid(mContext) + "')");
        }
    }

    //由前端调起的App登录的方法
    private class AppLogin extends Object {
        @JavascriptInterface
        public void appLogin() {
            Log.e(TAG, "====appLogin===前端通信调起，通知App进入登录页面==========");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (AppInfoUtil.getUserid(mContext) != null
                            && !TextUtils.isEmpty(AppInfoUtil.getUserid(mContext))) {
                        Toast.makeText(mContext, "用户已经登录", Toast.LENGTH_SHORT).show();
                        //重新加载
                        initView();
//                        sendUserId();
                    } else {
                        //调起原生的登录
                        startActivity(new Intent(mContext, NewLoginActivity.class));
                    }
                }
            });
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && acWebviewHtml.canGoBack()) {
            acWebviewHtml.goBack();
            return true;
        } else {
            goBack();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        destroyH5();
        super.onDestroy();
    }

    private void destroyH5() {
        if (acWebviewHtml != null) {
            acWebviewHtml.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            acWebviewHtml.clearHistory();

            ((ViewGroup) acWebviewHtml.getParent()).removeView(acWebviewHtml);
            acWebviewHtml.destroy();
            acWebviewHtml = null;
        }
    }
}
