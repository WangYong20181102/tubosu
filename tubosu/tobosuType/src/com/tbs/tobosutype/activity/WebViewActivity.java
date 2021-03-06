package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.base.*;
import com.tbs.tobosutype.bean._AppEvent;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.AppManager;
import com.tbs.tobosutype.utils.ShareUtil;
import com.tbs.tobosutype.utils.SpUtil;

/**
 * 用来加载h5页面
 * WebViewActivity走的不是安卓发单入口
 *
 * @author dec
 */
public class WebViewActivity extends com.tbs.tobosutype.base.BaseActivity {
    private static final String TAG = WebViewActivity.class.getSimpleName();
    private Context mContext;


    private WebView webView;
    private TextView detailslideshow_title;
    private String title;
    private LinearLayout llShare;
    private ImageView iv_back;
    private String url;
    private String currentUrl = "";
    private RelativeLayout rl_banner;
    private Gson mGson;
    private _AppEvent mAppEvent;

//	private ProgressDialog pbDialog;
//	
//	private Handler myHandler = new Handler(){
//		public void handleMessage(android.os.Message msg) {
//			switch (msg.what) {
//			case 13:
//				if(pbDialog.isShowing()){
//					pbDialog.dismiss();
//				}
//				break;
//
//			default:
//				break;
//			}
//		};
//	};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInfoUtil.setTranslucentStatus(WebViewActivity.this);
        setContentView(R.layout.activity_webview);

        mContext = WebViewActivity.this;


    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        initData();
        initEvent();
        SpUtil.setStatisticsEventPageId(mContext, url);
    }

    @Override
    protected boolean havePageId() {
        return true;
    }

    private void initView() {
        mGson = new Gson();
        mAppEvent = new _AppEvent();
        webView = (WebView) findViewById(R.id.my_webview);
        detailslideshow_title = (TextView) findViewById(R.id.detailslideshow_title);
        llShare = (LinearLayout) findViewById(R.id.ll_share);
        iv_back = (ImageView) findViewById(R.id.iv_back_webview_activity);
        rl_banner = (RelativeLayout) findViewById(R.id.rl_banner);
        rl_banner.setBackgroundColor(Color.parseColor("#ffffff"));
//		pbDialog = new ProgressDialog(mContext);
//		pbDialog.show();
    }


    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        url = bundle.getString("link");
        Log.d(TAG, "传过来的url是" + url);
        webView.getSettings().setJavaScriptEnabled(true);
//		webView.setWebViewClient(new WebViewClient());

        webView.setWebChromeClient(new WebChromeClient());
        SpUtil.setStatisticsEventPageId(mContext, url);
        webView.loadUrl(url + "&equipmentInfo=" + mGson.toJson(mAppEvent) + "&app_ref=" + AppManager.lastSecoundActivityName());
//		webView_detailslideshow.addJavascriptInterface(object, name);
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


//        // 分享本页面到朋友圈去
//        detail_share.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                //封装好的分享工具类
//                Log.d(TAG, "====- 好了没有 " + url + "】");
//                boolean flag = url.equals(currentUrl);
//                if (flag) {
//                    new ShareUtil(WebViewActivity.this, detail_share, title, title, url, flag);
//                } else {
//                    new ShareUtil(WebViewActivity.this, detail_share, title, title, currentUrl, flag);
//                }
//
//            }
//        });

        llShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = url.equals(currentUrl);
                if (flag) {
                    new ShareUtil(WebViewActivity.this, llShare, title, "嘿，好东西分享给你，快打开看看", url, flag);
                } else {
                    new ShareUtil(WebViewActivity.this, llShare, title, "嘿，好东西分享给你，快打开看看", currentUrl, flag);
                }
            }
        });

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


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
