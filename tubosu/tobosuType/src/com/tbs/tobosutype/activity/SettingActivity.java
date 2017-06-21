package com.tbs.tobosutype.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.CallDialogCompany;
import com.tbs.tobosutype.customview.CustomProgressDialog;
import com.tbs.tobosutype.model.DownLoadManager;
import com.tbs.tobosutype.protocol.JpyProtocol;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.CheckUpdateUtils;
import com.tbs.tobosutype.utils.DataCleanManager;
import com.tbs.tobosutype.utils.HttpServer;
import com.tbs.tobosutype.utils.ShareUtil;
import com.tbs.tobosutype.utils.ToastUtil;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
/**
 * 个人中心设置页面
 * @author dec
 *
 */
public class SettingActivity extends Activity {
	private static final String TAG = SettingActivity.class.getSimpleName();
	private Context context;
	private ImageView myset_back;
	
	/**版本升级布局*/
	private RelativeLayout myset_layout_version_upgrade;
	
	/**客服电话布局*/
	private TextView tv_company_service_tel;
	
	/**土拨鼠微信布局*/
	private RelativeLayout myset_layout_tbs_weixin;
	
	/**分享应用给好友布局*/
	private RelativeLayout rel_layout_share;
	
	/**清除缓存布局*/
	private RelativeLayout rel_clear_cache;
	
	/**缓存text*/
	private TextView tv_cache;
	
	/**推送*/
	private SharedPreferences pushSharedPreferences;
	
	/**
	 *  true 开启推送  <br/>
	 *  false 关闭推送
	 */
	private boolean isPush = false;
	
	private ImageView iv_push_flag;
	
	/**申请合作布局*/
	private RelativeLayout rel_applyfor_cooperation; //FIXME 未做
	
	/**给好评布局*/
	private RelativeLayout rel_good_praise;
	
	private Window window;
	private LayoutParams params;
	
	private long appCacheDataSize = 0L;
	
	//FIXME bug
	private TextView myset_logo_name;
	
	private TextView myset_version_upgrade;
	
	
	
//	/**我的收藏接口*/
//	private String urlDecorateStore = JpyProtocol.KBaseUrl + "tapp/user/my_fav";
//	private RequestParams decorateParams;
	
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppInfoUtil.setTranslucentStatus(this);
		setContentView(R.layout.activity_set);
		context = SettingActivity.this;
		initView();
		initData();
		initEvent();
//		initParams();
//		requestDecorateStorePost();
	}
	

	private void initView() {
		myset_back = (ImageView) findViewById(R.id.myset_back);
		rel_clear_cache = (RelativeLayout) findViewById(R.id.rel_clear_cache);
		tv_cache = (TextView) findViewById(R.id.tv_cache);
		myset_layout_version_upgrade = (RelativeLayout) findViewById(R.id.myset_layout_version_upgrade);
		tv_company_service_tel = (TextView) findViewById(R.id.tv_company_service_tel);
		myset_layout_tbs_weixin = (RelativeLayout) findViewById(R.id.myset_layout_tbs_weixin);
		rel_layout_share = (RelativeLayout) findViewById(R.id.rel_layout_share);
		rel_good_praise = (RelativeLayout) findViewById(R.id.rel_good_praise);
		iv_push_flag =  (ImageView) findViewById(R.id.iv_push_flag);
		
		pushSharedPreferences = getSharedPreferences("Push_Config", MODE_PRIVATE);//将需要记录的数据保存在config.xml文件中 
		
		isPush = pushSharedPreferences.getBoolean("Is_Push_Message", true);
		Log.d("pushMSG", "默认app推送功能是" + (isPush ? "开启的":"关闭的"));
		if(isPush){
			iv_push_flag.setBackgroundResource(R.drawable.icon_push_yes);
		}else{
			iv_push_flag.setBackgroundResource(R.drawable.icon_push_no);
		}
		
		myset_version_upgrade = (TextView) findViewById(R.id.myset_version_upgrade);
		myset_logo_name = (TextView) findViewById(R.id.myset_logo_name);
	}

	private void initData() {
		// 获取手机缓存
//		try {
//			appCacheDataSize = DataCleanManager.getFolderSize(context.getExternalCacheDir());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		try {
			if("0.0Byte".equals(DataCleanManager.getTotalCacheSize(getApplicationContext()))){
				tv_cache.setText("0 M");
			}else{
				tv_cache.setText(DataCleanManager.getTotalCacheSize(getApplicationContext()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//FIXME bug
		myset_logo_name.setText("土拨鼠装修  v"+AppInfoUtil.getAppVersionName(context));
		myset_version_upgrade.setText(AppInfoUtil.getAppVersionName(context));
		
		
		Intent intent = getIntent();
		String url = intent.getStringExtra("url");
		if (!TextUtils.isEmpty(url)) {
			downLoadApk(url);
		}
		
	}

	
	private void initEvent() {
		
		myset_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		rel_clear_cache.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!"0 M".equals(tv_cache.getText())){
					DataCleanManager.clearAllCache(getApplicationContext());
					tv_cache.setText("0 M");
				}else{
					Toast.makeText(context, "你已清理过缓存", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		iv_push_flag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//FIXME 推送功能
				if(isPush){
					isPush = false;
					Log.d("pushMSG", "点击后是" + (isPush ? "开启的":"关闭的"));
					XGPushConfig.enableDebug(getApplicationContext(), false);// 关闭debug开关
//					XGPushManager.unregisterPush(getApplicationContext(), new XGIOperateCallback() {
//						
//						@Override
//						public void onSuccess(Object obj, int i) {
//							
//						}
//						
//						@Override
//						public void onFail(Object obj, int i, String s) {
//							 Log.e(TAG, "手动注销失败, code - " + i + " message - " + obj.toString());  
//						}
//					});
					XGPushManager.unregisterPush(getApplicationContext());
//					Log.d(TAG, "手动注销成功, token - " + obj.toString());  
					if(pushSharedPreferences==null){
						pushSharedPreferences =  getSharedPreferences("Push_Config", MODE_PRIVATE);
					}
					Editor editor = pushSharedPreferences.edit();  
					editor.putBoolean("Is_Push_Message", false);
					editor.commit();
					iv_push_flag.setBackgroundResource(R.drawable.icon_push_no);
					
				}else{
					isPush = true;
					Log.d("pushMSG", "点击后是" + (isPush ? "开启的":"关闭的"));
					XGPushConfig.enableDebug(getApplicationContext(), true);// 打开debug开关
					XGPushManager.registerPush(getApplicationContext(), new XGIOperateCallback() {
			            @Override  
			            public void onSuccess(Object o, int i) {  
			                Log.d(TAG, "手动设置推送注册成功, token是" + o);  
			                if(pushSharedPreferences==null){
			                	pushSharedPreferences =  getSharedPreferences("Push_Config", MODE_PRIVATE);
			                }
			                Editor editor = pushSharedPreferences.edit();  
			                editor.putBoolean("Is_Push_Message", true);  
			                editor.commit();
			            }  
			  
			            @Override  
			            public void onFail(Object o, int i, String s) {  
			                Log.e(TAG, "手动设置推送注册失败, code - " + i + " 失败信息是 - " + s);  
			            }  
			        });  
					iv_push_flag.setBackgroundResource(R.drawable.icon_push_yes);
				}
			}
		});
		
		myset_layout_version_upgrade.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CheckUpdateUtils updateUtils = new CheckUpdateUtils(SettingActivity.this);
				updateUtils.startUpdata();
			}
		});
		

		rel_good_praise.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//FIXME 给我们好评
				String mAddress = "market://details?id=" + getPackageName();   
				Intent marketIntent = new Intent("android.intent.action.VIEW");    
				marketIntent.setData(Uri.parse(mAddress ));    
				startActivity(marketIntent);  
			}
		});
		
		
		myset_layout_tbs_weixin.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				cmb.setText("itobosu");
				ToastUtil.showShort(SettingActivity.this, "已复制微信号");
			}
		});
		
		rel_layout_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new ShareUtil(context, rel_layout_share, "土拨鼠装修网！", "土拨鼠装修网！", "http://www.tobosu.com/");
			}
		});
		
		tv_company_service_tel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String cellphone = "土拨鼠热线";
				String tel = "";
				CallDialogCompany callDialog = new CallDialogCompany(SettingActivity.this, R.style.callDialogTheme, cellphone, tel);
				window = callDialog.getWindow();

				params = window.getAttributes();
				params.width = LayoutParams.MATCH_PARENT;
				window.setGravity(Gravity.CENTER | Gravity.BOTTOM);
				callDialog.show();
			}
		});
		
	}

	protected void downLoadApk(final String downUrl) {
		final CustomProgressDialog pd;
		pd = new CustomProgressDialog(SettingActivity.this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载新版的APP !");
		pd.show();
		new Thread() {
			@Override
			public void run() {
				try {
					File file = DownLoadManager.getFileFromServer(downUrl, pd);
					sleep(3000);
					installApk(file);
					pd.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	protected void installApk(File paramFile) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.setDataAndType(Uri.fromFile(paramFile), "application/vnd.android.package-archive");
		this.startActivity(intent);
	}
}
