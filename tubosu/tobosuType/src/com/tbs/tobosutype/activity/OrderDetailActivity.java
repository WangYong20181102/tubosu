package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.utils.AppInfoUtil;

/**
 * 业主的订单详情
 *
 * @author dec
 */
public class OrderDetailActivity extends Activity {
    private static final String TAG = OrderDetailActivity.class.getSimpleName();
    private static final int FILECHOOSER_RESULTCODE = 0x00012;
    private Context mContext;
    private WebView webView;
    private ImageView free_back;
    private TextView title_name;
    private RelativeLayout headfree_banner;
    private String url;

//	private ImageView head_right_image_share;

    private ValueCallback<Uri> mUploadMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_order_detial);
        mContext = OrderDetailActivity.this;

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        headfree_banner = (RelativeLayout) findViewById(R.id.headfree_banner);
        webView = (WebView) findViewById(R.id.order_detail_activity_wv);
        free_back = (ImageView) findViewById(R.id.free_activity_back);
        title_name = (TextView) findViewById(R.id.title_name);
        headfree_banner.setBackgroundColor(Color.parseColor("#ff882e"));
        title_name.setText("订单详情");
//		head_right_image_share =  (ImageView) findViewById(R.id.head_right_image_share);
//		head_right_image_share.setVisibility(View.INVISIBLE); // 不需要的
    }

    private void initEvent() {
        free_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        url = getIntent().getStringExtra("url");

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setSaveFormData(false);
        settings.setSupportZoom(false);
        settings.setLoadWithOverviewMode(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowContentAccess(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadsImagesAutomatically(true);


        webView.setScrollBarStyle(0);

        webView.setWebViewClient(new WebViewClient());

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                if (message != null) {
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    result.confirm();
                }
                return true;
            }

        });


        webView.loadUrl(url);


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
