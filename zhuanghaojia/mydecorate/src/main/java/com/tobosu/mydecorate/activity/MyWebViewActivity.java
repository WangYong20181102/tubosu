package com.tobosu.mydecorate.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.base.BaseActivity;
import com.umeng.socialize.utils.Log;

/**
 * 有问题 废弃  不再使用
 */
public class MyWebViewActivity extends BaseActivity {
	private static final String TAG = MyWebViewActivity.class.getSimpleName();
	private Context mContext;

	private WebView webView;
	private TextView viewTitle;
	private String title;
	private ImageView iv_back;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);

		mContext = MyWebViewActivity.this;

		initView();
		initData();
		initEvent();
	}

	private void initView() {

		webView = (WebView) findViewById(R.id.my_webview);
		viewTitle = (TextView) findViewById(R.id.web_title);
		iv_back = (ImageView) findViewById(R.id.iv_back_webview_activity);
	}



	private void initData() {
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("urlbundle");
		url = bundle.getString("urldata");
		Log.d(TAG, "传过来的url是" + url);

		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient());
		webView.setWebChromeClient(new WebChromeClient());
		webView.loadUrl(url);
	}


	private void initEvent() {

		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				viewTitle.setText(view.getTitle());
//
//				System.out.println("webview======="+url);
//				if (url.contains("http://m.tobosu.com/app/success")) { // 原来是 equals的
//					Intent intent = new Intent(mContext, ApplyforSuccessActivity.class);
//					startActivityForResult(intent, 10);
//					finish();
//				}

			}


			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
//				System.out.println("webview======="+url);
//				if (url.contains("http://m.tobosu.com/app/success")) { // 原来是 equals的
//					Intent intent = new Intent(mContext, ApplyforSuccessActivity.class);
//					startActivity(intent);
//					finish();
//				}
			}

			
			
		});
		
		webView.setWebChromeClient(new WebChromeClient());
		
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(webView.canGoBack()){
					webView.goBack();
				}else{
					finish();
				}
			}
		});
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		if(resultCode==66){
//			finish();
//		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event){
		if((keyCode==KeyEvent.KEYCODE_BACK)&&webView.canGoBack()){
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	} 
	
}
