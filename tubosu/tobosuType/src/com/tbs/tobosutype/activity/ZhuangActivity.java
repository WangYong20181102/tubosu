package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.JsResult;
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
import com.tbs.tobosutype.global.MyApplication;
import com.tbs.tobosutype.utils.AppInfoUtil;

/***
 * android发单入口 页面
 * @author dec
 *
 */
public class ZhuangActivity extends Activity {
    private Context mContext;
    private WebView webView;
    private ImageView zhuang_back;
    //	private ImageView image_share;
//	private FrameLayout frame_layout_zhuang;
    private TextView title_name;

    private ProgressDialog pbDialog;

    private String zhuanghaojia_url = "http://m.tobosu.com/mt?channel=seo&subchannel=zhjandroid&chcode=" + AppInfoUtil.getChannType(MyApplication.getContext()) + "&from=share&tbsNative=1";
    private RelativeLayout rl_banner;


    private Handler myHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 12:

                    if (pbDialog.isShowing()) {
                        pbDialog.dismiss();
                    }
                    break;

                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_zhuang);
        mContext = ZhuangActivity.this;
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.wv_free_activity);
//		frame_layout_zhuang = (FrameLayout) findViewById(R.id.frame_layout_zhuang);
//		frame_layout_zhuang.bringToFront();
        zhuang_back = (ImageView) findViewById(R.id.free_activity_back);
//		image_share = (ImageView) findViewById(R.id.head_right_image_share);
//		image_share.setVisibility(View.GONE);
        title_name = (TextView) findViewById(R.id.title_name);
        rl_banner = (RelativeLayout) findViewById(R.id.rl_banner);
        rl_banner.setBackgroundColor(Color.parseColor("#ff882e"));
        pbDialog = new ProgressDialog(mContext);
        pbDialog.setMessage("正加载中...");
        pbDialog.show();
    }

    private void initEvent() {
        zhuang_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                System.out.println("---url->>>" + url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                title_name.setText("装好家");


            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                ZhuangActivity.this.setProgress(progress);
                if (progress > 70) {
                    Message msg = new Message();
                    msg.what = 12;
                    myHandler.sendMessage(msg);
                }
            }
        });
    }

    private void initData() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("GBK");
        settings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        settings.setAppCacheEnabled(true);
//		webView.setScrollBarStyle(0);

        webView.loadUrl(zhuanghaojia_url);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                if (message != null) {
                    Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        } else {
            finish();
        }
        return false;
    }

}
