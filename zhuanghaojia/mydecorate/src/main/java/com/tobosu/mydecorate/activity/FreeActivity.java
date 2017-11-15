package com.tobosu.mydecorate.activity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.view.BackDialog;
import com.umeng.analytics.MobclickAgent;



public class FreeActivity extends Activity {
	private Context mContext;
	private WebView webView;
	private RelativeLayout rel_free_back;
	private ProgressDialog pbDialog;
//	private String dataurl = "";
	private String html_url = "http://m.tobosu.com/app/pub?channel=seo&subchannel=android";
//	                           http://m.tobosu.com/app/pub?channel=seo&amp;subchannel=android

	private Handler myHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 12:
					if(pbDialog.isShowing()&& FreeActivity.this!=null && !FreeActivity.this.isFinishing()){
						pbDialog.dismiss();
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
		setContentView(R.layout.activity_free);
		mContext = FreeActivity.this;
		initData();
		initEvent();
	}
	private void initData() {
		webView = (WebView) findViewById(R.id.wv_free_activity);
		rel_free_back = (RelativeLayout) findViewById(R.id.rel_free_back);
		pbDialog = new ProgressDialog(mContext);
		pbDialog.setMessage("正在加载中...");
		pbDialog.show();

		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setDefaultTextEncodingName("GBK");
		settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		settings.setAppCacheEnabled(true);

		webView.loadUrl(html_url);

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

	private void initEvent() {
		rel_free_back.setOnClickListener(new View.OnClickListener() {

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
				if (url.contains("http://m.tobosu.com/app/success")) {

					MobclickAgent.onEvent(mContext,"click_free_design_success");
					System.out.println("发单成功");
//					showTipDilog();
					startActivityForResult(new Intent(mContext, ApplyforSuccessActivity.class), 65);
				}
			}


			@Override
			public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
				return null;
			}

			@Override
			public void onPageFinished(WebView view, String url) {

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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode){
			case 66:
				finish();
				break;
		}
	}

		private void showTipDilog(){
		BackDialog.Builder builder = new BackDialog.Builder(mContext);
		builder.setTitle("预约成功")
				.setMessage("请留意手机，我们会尽快以0574开头座机联系您！")
				.setNegativeButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								finish();
							}
						});
		builder.create().show();
		return;
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		webView.setVisibility(View.GONE);
	}
}
