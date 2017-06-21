package com.tbs.tobosutype.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tbs.tobosutype.customview.CustomDialog;
import com.tbs.tobosutype.customview.CustomProgressDialog;
import com.tbs.tobosutype.global.AllConstants;
import com.tbs.tobosutype.global.MyApplication;
import com.tbs.tobosutype.model.DownLoadManager;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
 /**
  * 检测版本更新工具
  * @author dec
  *
  */
public class CheckUpdateUtils {
	private static final String TAG = CheckUpdateUtils.class.getSimpleName();
	private Context context;
	private String checkUpdateUrl = AllConstants.TOBOSU_URL + "tapp/util/boot_img";

	public CheckUpdateUtils(Context context) {
		this.context = context;
	}

	/**
	 * 检测是否有新版本
	 */
	public void cheackUpdate() {
		RequestParams qequestParams = new RequestParams();
		qequestParams.add("vname", getVersionName());
		
		HttpServer.getInstance().requestPOST(checkUpdateUrl, qequestParams, new AsyncHttpResponseHandler() {
			
			public void onFailure(int paramInt, Header[] paramArrayOfHeader, byte[] paramArrayOfByte, Throwable paramThrowable) {
				
			}

			public void onSuccess(int paramInt, Header[] paramArrayOfHeader, byte[] paramArrayOfByte) {
				String result = new String(paramArrayOfByte);

				if(result.contains("data")){
					System.out.println("--升级信息-->>" + result + "<<<<");
					try {
						JSONObject jsonObject = new JSONObject(result);
						if (jsonObject.getInt("error_code") == 0) {
							JSONObject data = jsonObject.getJSONObject("data");
							MyApplication.NEWVERSIONCODE = data.optString("newestversion");
							MyApplication.DOWNLOADURL = data.optString("downpath");

							if ("1".equals(data.getString("must_update"))) {
								MyApplication.ISMUSTUPDATE = true;
								return;
							}else{
								MyApplication.ISMUSTUPDATE = false;
								return;
							}
						}
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}else{
					System.out.println("-无升级信息-->>" + result + "<<<<");
				}
			}
		});
	}

	/**
	 * 有新版本 下载新版本apk
	 */
	protected void downLoadApk() {
		final CustomProgressDialog pd; 
		pd = new CustomProgressDialog(context);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载新版的APP!");
		pd.show();
		new Thread() {
			@Override
			public void run() {
				try {
					File file = DownLoadManager.getFileFromServer(MyApplication.DOWNLOADURL, pd);
					Thread.sleep(3000);
					installApk(file);
					Log.d(TAG,"--安装新版apk--");
					pd.dismiss(); 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * 安装apk
	 * @param paramFile
	 */
	protected void installApk(File paramFile) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.setDataAndType(Uri.fromFile(paramFile), "application/vnd.android.package-archive");
		this.context.startActivity(intent);
	}

	/***
	 * 更新版本的弹框提示信息
	 */
	public void startUpdata() { 
		if(MyApplication.ISMUSTUPDATE){
			// 强制更新
			if (MyApplication.DOWNLOADURL != null) {
				CustomDialog.Builder builder = new CustomDialog.Builder(this.context);
				builder.setTitle("提示");
				builder.setMessage("土拨鼠APP即将更新到 " + MyApplication.NEWVERSIONCODE + " !");
				builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface paramDialogInterface, int paramInt) {
								CheckUpdateUtils.this.downLoadApk();
							}
						});
				builder.create().show();
				return;
			}
		}else{
			// 非强制更新
			if (MyApplication.DOWNLOADURL != null) {
				CustomDialog.Builder builder = new CustomDialog.Builder(this.context);
				builder.setTitle("提示");
				builder.setMessage("土拨鼠APP即将更新到 " + MyApplication.NEWVERSIONCODE + " !");
				builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface paramDialogInterface, int paramInt) {
								CheckUpdateUtils.this.downLoadApk();
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface paramDialogInterface, int paramInt) {
								if (!MyApplication.ISMUSTUPDATE)
									paramDialogInterface.cancel();
							}
						});
				builder.create().show();
				return;
			}
		}
		Toast.makeText(this.context, "暂无可更新的版本！", Toast.LENGTH_SHORT).show();
	}

	/***
	 * 获取apk版本名称
	 * @return
	 */
	private String getVersionName() {
		PackageManager packageManager = this.context.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(this.context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (PackageManager.NameNotFoundException eception) {
			eception.printStackTrace();
			return null;
		}
	}
}