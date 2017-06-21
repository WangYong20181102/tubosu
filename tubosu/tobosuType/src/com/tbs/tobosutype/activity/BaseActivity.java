package com.tbs.tobosutype.activity;
/**
 * [首页] [逛图库] [找装修] [我]   
 * 这四个页面的父类
 */
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tbs.tobosutype.customview.CustomDialog;
import com.tbs.tobosutype.protocol.JpyProtocol;
import com.tbs.tobosutype.utils.AppInfoUtil;

public class BaseActivity extends Activity implements JpyProtocol.MDataUpdateNotify {
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			CustomDialog.Builder builder = new CustomDialog.Builder(this);
			builder.setMessage("你确定退出吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
									finish();
									System.exit(0);
								}
							})
					.setNegativeButton("再看看",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			builder.create().show();

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean OnNewDataArrived(int aRequestType, int aErrCode, Object aData) {
		return false;
	}
}