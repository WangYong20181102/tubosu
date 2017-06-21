package com.tobosu.mydecorate.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tobosu.mydecorate.application.MyApplication;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.view.CustomDialog;
import com.tobosu.mydecorate.view.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * 检测版本更新工具
 * @author dec
 *
 */
public class CheckUpdateUtils {
	private static final String TAG = CheckUpdateUtils.class.getSimpleName();
	private Context context;
	private String checkUpdateUrl = Constant.ZHJ + "tapp/util/checkMtAppUpdate";


	private String new_version_name = "";
	private String new_version_downpath = "";
	private String must_update = "";

	public CheckUpdateUtils(Context context) {
		this.context = context;
	}


	/**
	 * 检测是否有新版本 ok
	 */
	public void checkNewVersion(){
		OKHttpUtil okHttpUtil = new OKHttpUtil();
		final HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("mt_vname", Util.getAppVersionName(context));
		okHttpUtil.post(checkUpdateUrl, hashMap, new OKHttpUtil.BaseCallBack() {

			@Override
			public void onSuccess(Response response, String json) {
				try {
					JSONObject obj = new JSONObject(json);
					if(obj.getInt("error_code")==0){
						MyApplication.HAS_NEW_VERSION = true;
						// 有版本更新 继续进行下一步
						JSONObject versionObj = obj.getJSONObject("data");
						new_version_name = versionObj.getString("mt_newestversion");
						new_version_downpath = versionObj.getString("mt_downpath");
						must_update = versionObj.getString("mt_must_update");
						if(must_update.equals("1")){
							MyApplication.ISMUSTUPDATE = 1;
						}else {
							MyApplication.ISMUSTUPDATE = 0;
						}
					}else{
						MyApplication.HAS_NEW_VERSION = false;
						System.out.println("--无版本更新--");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFail(Request request, IOException e) {

			}

			@Override
			public void onError(Response response, int code) {

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
					getDownLoad(new_version_downpath, pd);
					Thread.sleep(3000);
					if(apkFile!=null){
						installApk(apkFile);
					}else{
						System.out.println("安装失败");
					}
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
		context.startActivity(intent);
	}

	/***
	 * 更新版本的弹框提示信息 ok
	 */
	public void startUpdata() {
		if("1".equals(String.valueOf(MyApplication.ISMUSTUPDATE))){
			// 强制更新
			if (!"".equals(new_version_downpath)) {
				CustomDialog.Builder builder = new CustomDialog.Builder(context);
				builder.setTitle("提示");
				builder.setMessage("装好家app将更新到 " + new_version_name + " !");
				builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface paramDialogInterface, int paramInt) {
						downLoadApk();
					}
				});
				builder.create().show();
				return;
			}
		}else{
			// 非强制更新
			if (!"".equals(new_version_downpath)) {
				CustomDialog.Builder builder = new CustomDialog.Builder(context);
				builder.setTitle("提示");
				builder.setMessage("装好家APP即将更新到 " + new_version_name + " !");
				builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface paramDialogInterface, int paramInt) {
						downLoadApk();
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//								if (MyApplication.ISMUSTUPDATE)
								paramDialogInterface.cancel();
							}
						});
				builder.create().show();
				return;
			}
		}
//		Toast.makeText(this.context, "暂无可更新的版本！", Toast.LENGTH_SHORT).show();
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

	private File apkFile = null;
	private void getDownLoad(String path, final CustomProgressDialog c_pd){
		Request request = new Request.Builder().url(path).build();
		OkHttpClient mOkHttpClient = new OkHttpClient();
		mOkHttpClient.newCall(request).enqueue(new Callback() {

			@SuppressWarnings("resource")
			@Override
			public void onResponse(Response response) throws IOException {
				InputStream is = null;
				byte[] buf = new byte[2048];
				int len = 0;
				FileOutputStream fos = null;
				String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
				try {
					is = response.body().byteStream();
					long total = response.body().contentLength();
					apkFile = new File(SDPath, "ZhuangHaoJia.apk");
					fos = new FileOutputStream(apkFile);
					long sum = 0;
					while ((len = is.read(buf)) != -1) {
						fos.write(buf, 0, len);
						sum += len;
						c_pd.setProgress((int)sum);
					}
					fos.flush();


					Log.d(TAG, "文件下载成功");
				} catch (Exception e) {
					Log.d(TAG, "文件下载失败");
				} finally {
					try {
						if (is != null)
							is.close();
					} catch (IOException e) {
					}
					try {
						if (fos != null)
							fos.close();
					} catch (IOException e) {
					}
				}
			}

			@Override
			public void onFailure(Request arg0, IOException arg1) {
				Log.d(TAG, "onFailure");
			}
		});
	}
}