package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
import android.widget.Toast;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.global.AllConstants;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.ShareUtil;

/***
 * android发单入口 页面
 * @author dec
 *
 */
public class FreeActivity extends Activity {
	private Context mContext;
	private WebView webView;
	private ImageView free_back;
	private ImageView image_share;
	
	private ProgressDialog pbDialog;
	
	private String title;
	
	
	private Handler myHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 12:
				if(isFinishing()){
					pbDialog = null;
				}else {
					if(pbDialog!=null && pbDialog.isShowing()){
						pbDialog.dismiss();
					}
				}

				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
//		AppInfoUtil.setActivityTheme(this, R.color.whole_color_theme);
		AppInfoUtil.setTranslucentStatus(this);
//		AppInfoUtil.setTranslucentStatus(this);1
		setContentView(R.layout.activity_free);
		mContext = FreeActivity.this;
		initView();
		initData();
		initEvent();
	}
	private void initView() {
		webView = (WebView) findViewById(R.id.wv_free_activity);
		free_back = (ImageView) findViewById(R.id.free_activity_back);
		image_share = (ImageView) findViewById(R.id.head_right_image_share);
		pbDialog = new ProgressDialog(mContext);
		pbDialog.show();
	}

	private void initEvent() {
		free_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				if (url.contains("http://m.tobosu.com/app/success")) { // 原来是 equals的
					Intent intent = new Intent(mContext, ApplyforSuccessActivity.class);
					startActivity(intent);
					finish();
				}
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				title = view.getTitle();
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

			}
		});
		
		webView.setWebChromeClient(new WebChromeClient(){
			@Override  
            public void onProgressChanged(WebView view, int progress){  
                FreeActivity.this.setProgress(progress);  
                if(progress > 70) {
                	Message msg = new Message();
                	msg.what = 12;
                	myHandler.sendMessage(msg);
                }  
            }  
		});
	}

	private void initData() {
		//初始化web界面上的数据
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setDefaultTextEncodingName("GBK");
		settings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		settings.setAppCacheEnabled(true);
//		webView.setScrollBarStyle(0);
		
		webView.loadUrl(AllConstants.PIPE);
		
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
				if (message != null) {
					Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
				}
				return true;
			}
		});
		
		/**分享*/
		image_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(title)) {
					new ShareUtil(mContext, image_share, title, title, AllConstants.PIPE);
				}
			}
		});
	}
}
