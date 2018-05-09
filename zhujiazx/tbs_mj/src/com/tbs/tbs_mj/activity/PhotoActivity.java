package com.tbs.tbs_mj.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.customview.ZoomImageView;
import com.tbs.tbs_mj.utils.AppInfoUtil;

/**
 * 没使用
 * @author dec
 * 
 */
public class PhotoActivity extends Activity {
	private ZoomImageView iv_photo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppInfoUtil.setTranslucentStatus(this);
		setContentView(R.layout.activity_photo);

		byte[] result = getIntent().getExtras().getByteArray("result");
		iv_photo = (ZoomImageView) findViewById(R.id.iv_photo);
		Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);
		iv_photo.setImage(bitmap);
	}
}
